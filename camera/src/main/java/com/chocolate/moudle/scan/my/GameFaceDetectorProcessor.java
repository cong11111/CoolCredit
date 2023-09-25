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
import com.chocolate.moudle.scan.base.DeviceUtils;
import com.chocolate.moudle.scan.base.FaceDetectorProcessor;
import com.chocolate.moudle.scan.base.FaceSaveState;
import com.chocolate.moudle.scan.base.GraphicOverlay;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import java.util.ArrayList;
import java.util.List;

public class GameFaceDetectorProcessor extends FaceDetectorProcessor {

    private boolean mIsShowPoint = false;

    private boolean leftEyeClose = false;
    private boolean rightEyeClose = false;
    private boolean lastSmileFlag = false;

    private Context context;

    public void setShowPoint(boolean isShowPoint) {
        mIsShowPoint = isShowPoint;
    }

    public GameFaceDetectorProcessor(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onSuccess(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {
        if (mIsShowPoint) {
            super.onSuccess(faces, graphicOverlay);
        }
        if (faces.size() != 1) {
            notifySmileChange(false);
            return;
        }
        for (Face face : faces) {
            if (face.getLeftEyeOpenProbability() != null) {
                if (face.getLeftEyeOpenProbability() > 0.6) {
                    if (leftEyeClose) {
                        mObserver.onLeftEyeBlink();
                    }
                    leftEyeClose = false;
                } else {
                    leftEyeClose = true;
                }
            }

            if (face.getRightEyeOpenProbability() != null) {
                if (face.getRightEyeOpenProbability() > 0.6) {
                    if (rightEyeClose) {
                        mObserver.onRightEyeBlink();
                    }
                    rightEyeClose = false;
                } else {
                    rightEyeClose = true;
                }
            }
            if (face.getRightEyeOpenProbability() != null) {
                if (face.getRightEyeOpenProbability() > 0.6) {
                    if (rightEyeClose) {
                        mObserver.onRightEyeBlink();
                    }
                    rightEyeClose = false;
                } else {
                    rightEyeClose = true;
                }
            }

            if (face.getSmilingProbability() != null && face.getSmilingProbability() > 0.6) {
                notifySmileChange(true);
            } else {
                notifySmileChange(false);
            }
        }
    }

    private void notifySmileChange(boolean smile){
        if (smile == lastSmileFlag){
            return;
        }
        lastSmileFlag = smile;
        if (mObserver != null){
            mObserver.onSmile(smile);
        }
    }

    private Observer mObserver;

    public void setObserver(Observer observer) {
        mObserver = observer;
    }

    public interface Observer {
        void onLeftEyeBlink();

        void onRightEyeBlink();

        void onSmile(boolean smileFlag);

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
