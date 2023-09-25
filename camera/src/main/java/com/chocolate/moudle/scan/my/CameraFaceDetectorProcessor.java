package com.chocolate.moudle.scan.my;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chocolate.moudle.scan.BuildConfig;
import com.chocolate.moudle.scan.base.FaceDetectorProcessor;
import com.chocolate.moudle.scan.base.DeviceUtils;
import com.chocolate.moudle.scan.base.FaceSaveState;
import com.chocolate.moudle.scan.base.GraphicOverlay;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import java.util.ArrayList;
import java.util.List;

public class CameraFaceDetectorProcessor extends FaceDetectorProcessor {

    private SensorManager mSensorMgr;
    private boolean mIsShowPoint = false;
    private Context context;

    private float lightingStrengthValue = SensorManager.LIGHT_SUNRISE;
    private Face mFace;

    public void setShowPoint(boolean isShowPoint) {
        mIsShowPoint = isShowPoint;
    }

    public CameraFaceDetectorProcessor(Context context) {
        super(context);
        this.context = context;
        mSensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        registerListenerInternal();
    }

    public void onResume() {
        registerListenerInternal();
    }

    private void registerListenerInternal() {
        if (DeviceUtils.isEmulator(context)) {
            return;
        }
        try {
            // 第二个参数：传感器对象 光线传感器类型的常量：TYPE_LIGHT
            Sensor sensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (mSensorMgr != null) {
                mSensorMgr.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        } catch (Exception e) {
            lightingStrengthValue = SensorManager.LIGHT_SUNRISE;
            if (BuildConfig.DEBUG) {
                throw e;
            }
        }
    }

    public void onPause() {
        try {
            if (DeviceUtils.isEmulator(context)) {
                return;
            }
            if (mSensorMgr != null) {
                mSensorMgr.unregisterListener(listener);
            }
        } catch (Exception e) {
            lightingStrengthValue = SensorManager.LIGHT_SUNRISE;
            if (BuildConfig.DEBUG) {
                throw e;
            }
        }
    }

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //获取传感器类型
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                float[] values = event.values;
                lightingStrengthValue = values[0];
                if (BuildConfig.DEBUG) {
                    Log.d("Test", " on sensor changed = " + event.accuracy
                            + " lighting stength = " + values[0]);
                }
                @DetectResult int lightingStrengthResult = checkLightingStrength();
                if (lightingStrengthResult == mLightingStrengthResult){
                    return;
                }
                mLightingStrengthResult = lightingStrengthResult;
                if (mObserver != null) {
                    mObserver.onLightStrengthChange(lightingStrengthResult);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            Log.e(TAG, " on accuracy changed = " + accuracy + " sensor = " + sensor.getName());
        }
    };

    private int mLightingStrengthResult = -1;
    private int mFacePosResult = -1;
    private int mLookStraightResult = -1;

    @Override
    protected void onSuccess(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {
        @DetectResult int lightingStrengthResult = checkLightingStrength();
        if (faces.size() != 1) {
            notifyObserverChange(lightingStrengthResult, DetectResult.NONE, DetectResult.NONE);
            return;
        }
        if (mIsShowPoint) {
            super.onSuccess(faces, graphicOverlay);
        }
        for (Face face : faces) {
            @DetectResult int facePosResult = checkFacePosResult(face, graphicOverlay);
            @DetectResult int lookStraightResult = checkLookStraightResult(face);
            mFace = face;
            notifyObserverChange(lightingStrengthResult, facePosResult, lookStraightResult);
        }
    }

    private void notifyObserverChange(@DetectResult int lightingStrengthResult,
                                      @DetectResult int facePosResult,
                                      @DetectResult int lookStraightResult){
        if (mLightingStrengthResult == lightingStrengthResult && mFacePosResult == facePosResult && mLookStraightResult == lookStraightResult) {
            return;
        }
        mLightingStrengthResult = lightingStrengthResult;
        mFacePosResult = facePosResult;
        mLookStraightResult = lookStraightResult;
        if (mObserver != null) {
            mObserver.onFaceDetectChange(lightingStrengthResult, facePosResult, lookStraightResult);
        }
    }

    private @DetectResult
    int checkLightingStrength() {
        if (lightingStrengthValue >= 150) return DetectResult.HIGH;
        if (lightingStrengthValue >= 20) return DetectResult.MEDIUM;
        return DetectResult.NONE;
    }

    /**
     * 参考luvly逆向的逻辑 FaceDetectAnalyser.class
     * private static final e<Float> g = l.b(-35.0F, 35.0F);
     * private static final e<Float> h = l.b(-15.0F, 15.0F);
     *
     * @param face
     * @param overlay
     * @return
     */
    private @DetectResult
    int checkFacePosResult(Face face, GraphicOverlay overlay) {
        if (face == null) return DetectResult.NONE;
        boolean isFaceCenter = checkFaceIsCenter(face.getBoundingBox(), overlay, face);
        if (!isFaceCenter) return DetectResult.NONE;
        @DetectResult int lookStraight;
        if (checkIsInRange(face.getHeadEulerAngleX(), -15F, 15F)
                && checkIsInRange(face.getHeadEulerAngleY(), -15F, 15F)
                && checkIsInRange(face.getHeadEulerAngleZ(), -15F, 15F)) {
            lookStraight = DetectResult.HIGH;
        } else {
            if (checkIsInRange(face.getHeadEulerAngleX(), -35F, 35F)
                    && checkIsInRange(face.getHeadEulerAngleY(), -35F, 35F)
                    && checkIsInRange(face.getHeadEulerAngleZ(), -35F, 35F)) {
                lookStraight = DetectResult.MEDIUM;
            } else {
                lookStraight = DetectResult.NONE;
            }
        }
        return Math.min(isFaceCenter ? DetectResult.HIGH : DetectResult.NONE, lookStraight);
    }

    private RectF overlayRectF = new RectF();
    /**
     * 检测脸部是否在屏幕中间
     *
     * @param rect
     * @param overlay
     * @param face
     * @return
     */
    private boolean checkFaceIsCenter(Rect rect, GraphicOverlay overlay, Face face) {
        if (rect.isEmpty()) return false;
        if (overlay == null) return true;
        float x = translateX(face.getBoundingBox().centerX(), overlay);
        float y = translateY(face.getBoundingBox().centerY(), overlay);
        float left = x - scale(face.getBoundingBox().width() / 2.0f, overlay);
        float top = y - scale(face.getBoundingBox().height() / 2.0f, overlay);
        float right = x + scale(face.getBoundingBox().width() / 2.0f, overlay);
        float bottom = y + scale(face.getBoundingBox().height() / 2.0f, overlay);
        if (overlayRectF.isEmpty()){
            overlayRectF.set(overlay.getFaceOvalRect());
            float offsetY = overlay.getFaceOvalRect().height() / 10f;
            overlayRectF.inset(0, -offsetY);
        }
//        overlay.setFaceRect(new RectF(left,top,right,bottom));
        if (!overlayRectF.isEmpty()){
            return (left > 0 && right < overlay.getWidth() && top >= overlayRectF.top &&
                        bottom <= overlayRectF.bottom) ;
        } else {
            return (left > 0 && right < overlay.getWidth() && top > 0 && bottom < overlay.getHeight());
        }
    }

    public float translateX(float x, GraphicOverlay overlay) {
        if (overlay.isImageFlipped) {
            return overlay.getWidth() - (scale(x, overlay) - overlay.postScaleWidthOffset);
        } else {
            return scale(x, overlay) - overlay.postScaleWidthOffset;
        }
    }

    public float translateY(float y, GraphicOverlay overlay) {
        return scale(y, overlay) - overlay.postScaleHeightOffset;
    }

    public float scale(float imagePixel, GraphicOverlay overlay) {
        return imagePixel * overlay.scaleFactor;
    }

    private boolean checkIsInRange(float value, float start, float end) {
        return (value > start && value < end);
    }

    /**
     * 参考luvly逆向的逻辑 FaceDetectAnalyser.class
     * if (float_2 == null) {
     * float_2 = null;
     * } else if (float_2.floatValue() >= 0.6F) {
     * faceDetectQuality2 = FaceDetectQuality.HIGH;
     * } else if (faceDetectQuality2.floatValue() >= 0.3F) {
     * faceDetectQuality2 = FaceDetectQuality.MEDIUM;
     * } else {
     * faceDetectQuality2 = FaceDetectQuality.NONE;
     * }
     *
     * @param face
     * @return
     */
    private @DetectResult
    int checkLookStraightResult(Face face) {
        if (face == null) return DetectResult.NONE;

        float leftEyeProbability = -1;
        float rightEyeProbability = -1;
        float smileProbability = -1;

        Float leftEyeProbabilityF = face.getLeftEyeOpenProbability();
        if (leftEyeProbabilityF != null) {
            leftEyeProbability = leftEyeProbabilityF;
        }
        Float rightEyeProbabilityF = face.getRightEyeOpenProbability();
        if (rightEyeProbabilityF != null) {
            rightEyeProbability = rightEyeProbabilityF;
        }
        Float smileProbabilityF = face.getSmilingProbability();
        if (smileProbabilityF != null) {
            smileProbability = smileProbabilityF;
        }
        @DetectResult int leftEyeResult = checkEyeDetectResult(leftEyeProbability);
        @DetectResult int rightEyeResult = checkEyeDetectResult(rightEyeProbability);
        return Math.min(leftEyeResult, rightEyeResult);
    }

    private @DetectResult
    int checkEyeDetectResult(float value) {
        if (value >= 0.6) return DetectResult.HIGH;
        if (value >= 0.3) return DetectResult.MEDIUM;
        return DetectResult.NONE;
    }

    public FaceSaveState getLastFace(GraphicOverlay overlay){
        if (mFace == null) return null;
        FaceSaveState state = new FaceSaveState();
        float x = translateX(mFace.getBoundingBox().centerX(), overlay);
        float y = translateY(mFace.getBoundingBox().centerY(), overlay);
        int left = (int) (x - scale(mFace.getBoundingBox().width() / 2.0f, overlay));
        int top = (int) (y - scale(mFace.getBoundingBox().height() / 2.0f, overlay));
        int right = (int) (x + scale(mFace.getBoundingBox().width() / 2.0f, overlay));
        int bottom = (int) (y + scale(mFace.getBoundingBox().height() / 2.0f, overlay));
        //前置摄像头情况下,预览拍摄出来的照片是反的.所以调换一下Rect的left,right,top,bottom
        Rect rect = new Rect(Math.min(left, right), Math.min(top, bottom),
                Math.max(left, right), Math.max(top, bottom));

        state.setRect(rect);
        state.setImageFlipped(overlay.isImageFlipped);
        state.setFacePoints(buildList(FaceContour.FACE, overlay));
        state.setLeftCheekPoints(buildList(FaceContour.LEFT_CHEEK, overlay));
        state.setRightCheekPoints(buildList(FaceContour.RIGHT_CHEEK, overlay));
        state.setNoseBridgePoints(buildList(FaceContour.NOSE_BRIDGE, overlay));
        state.setUpperLipTopPoints(buildList(FaceContour.UPPER_LIP_TOP, overlay));
        state.setLowerLipBottomPoints(buildList(FaceContour.LOWER_LIP_BOTTOM, overlay));
        state.setLeftEyePoints(buildList(FaceContour.LEFT_EYE, overlay));
        state.setRightEyePoint(buildList(FaceContour.RIGHT_EYE, overlay));
        return state;
    }

    private ArrayList<PointF> buildList(int type, GraphicOverlay overlay){
        if (mFace == null) return null;
        FaceContour faceContour = mFace.getContour(type);
        if (faceContour == null || faceContour.getPoints().isEmpty()) return null;
        ArrayList<PointF> temp = new ArrayList();
        for (int i = 0; i < faceContour.getPoints().size(); i++){
            PointF p = faceContour.getPoints().get(i);
            temp.add(new PointF(translateX(p.x, overlay), translateY(p.y, overlay)));
        }
        return temp;
    }

    private Observer mObserver;

    public void setObserver(Observer observer) {
        mObserver = observer;
    }

    public interface Observer {
        void onFaceDetectChange(@DetectResult int lightingStrengthResult, @DetectResult int facePosResult, @DetectResult int lookStraightResult);

        void onLightStrengthChange(@DetectResult int lightingStrengthResult);

        void onFailure(@NonNull Exception e);
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        super.onFailure(e);
        if (mObserver != null) {
            mObserver.onFailure(e);
        }
    }
}
