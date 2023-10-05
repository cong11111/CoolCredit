package com.chocolate.moudle.scan.my;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chocolate.moudle.scan.BuildConfig;
import com.chocolate.moudle.scan.R;
import com.chocolate.moudle.scan.base.CameraXViewModel;
import com.chocolate.moudle.scan.base.FaceSaveState;
import com.chocolate.moudle.scan.base.GraphicOverlay;

public class CameraScanningFragment extends Fragment {

    public static final String TAG = "CameraScanningFragment";

    @DetectResult
    private int lightingStrengthResult = DetectResult.MEDIUM;

    @DetectResult
    private int facePosResult = DetectResult.NONE;

    @DetectResult
    private int lookStraightResult = DetectResult.NONE;

    private static final int UPDATE_FACT_DETECT_RESULT = 1111;
    private static final int UPDATE_LIGHT_DETECT_RESULT = 1112;

    private Bitmap bitmap;

    //overlay的真实高度
    private float overlayRealHeight = -1f;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_FACT_DETECT_RESULT:
                    updateDetectResultUi();
                    break;
                case UPDATE_LIGHT_DETECT_RESULT:
                    updateDetectLightResult();
                    break;
            }
            return false;
        }
    });

    private ProcessCameraProvider cameraProvider;
    private CameraFaceDetectorProcessor imageProcessor;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private CameraSelector cameraSelector;

    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private boolean needUpdateGraphicOverlayImageSourceInfo = false;
    private ResourceMgr mResourceMgr;

    //是否有权限
    private boolean hasPermission = false;

    private PreviewView pvView;
    private AppCompatTextView tvTips;
    private AppCompatTextView tvFacePos;
    private AppCompatTextView tvLookStraight;
    private AppCompatImageView ivTake;
    private GraphicOverlay overlay;
    private AppCompatImageView ivChangeCamera;
    private AppCompatImageView ivBack;
    private View viewTop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanning, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pvView = view.findViewById(R.id.scanning_preview_view);
        tvTips = view.findViewById(R.id.tv_face_scan_tips);
        tvFacePos = view.findViewById(R.id.tv_scanning_face_position);
        tvLookStraight = view.findViewById(R.id.tv_scanning_look_straight);
        overlay = view.findViewById(R.id.scanning_graphic_overlay);
        ivTake = view.findViewById(R.id.retake_iv);
        ivChangeCamera = view.findViewById(R.id.change_camera);
        ivBack = view.findViewById(R.id.iv_scanning_back);
        viewTop = view.findViewById(R.id.view_scanning_top);

        int statusBarH = BarUtils.getStatusBarHeight();
        ViewGroup.MarginLayoutParams topParam = (ViewGroup.MarginLayoutParams) viewTop.getLayoutParams();
        topParam.topMargin = statusBarH == 0 ? ConvertUtils.dp2px(25) : statusBarH ;
        viewTop.setLayoutParams(topParam);
        lensFacing = CameraSelector.LENS_FACING_FRONT;

        overlay.setTopSpace(0, overlayRealHeight);
        overlay.setNeedDrawFaceOval(true);
        overlay.post(new Runnable() {
            @Override
            public void run() {
                if (isDetached() || isRemoving() || getContext() == null) {
                    return;
                }
//                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvTips.getLayoutParams();
//                float bottom = overlay.getFaceOvalRect().bottom;
//                float tipTopMargin = ConvertUtils.dp2px(15);
//                layoutParams.topMargin = (int) (bottom + tipTopMargin);
//                tvTips.setLayoutParams(layoutParams);
//                tvTips.setVisibility(View.VISIBLE);
            }
        });

        ivTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageProcessor == null || pvView == null) {
                    return;
                }
                mHandler.removeCallbacksAndMessages(null);
                bitmap = pvView.getBitmap();
                FaceSaveState faceState = imageProcessor.getLastFace(overlay);
                if (faceState == null || faceState.getRect() == null || faceState.getRect().isEmpty()) {
                    if (BuildConfig.DEBUG) {
                        ToastUtils.showShort(" get faceRectF error ");
                    }
                    return;
                }
                if (getActivity() instanceof ScanActivity) {
                    ((ScanActivity) getActivity()).setBitmap(bitmap, faceState, new ScanActivity.CallBack() {
                        @Override
                        public void onSuccess() {
                            ((ScanActivity) getActivity()).onFinish();
                        }
                    });

                }
            }
        });

        ivChangeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageProcessor == null) {
                    return;
                }
                changeCamera();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof ScanActivity) {
                    ScanActivity scanActivity = (ScanActivity) getActivity();
                    scanActivity.finish();
                }
            }
        });

        checkPermission();
//        if (!GoogleModuleInstallHelper.getInstance().hasInstall()) {
//            showNotExistFaceModuleError();
//        }
    }

    private void checkPermission() {
        hasPermission = PermissionUtils.isGranted(Manifest.permission.CAMERA);
        if (hasPermission) {
            startPreview();
        } else {
            pvView.setVisibility(View.GONE);
        }
    }


    private void startPreview() {
        hasPermission = true;
        if (!isAdded()) {
            return;
        }
        pvView.setVisibility(View.VISIBLE);
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication());
        new ViewModelProvider(CameraScanningFragment.this, (ViewModelProvider.Factory) factory)
                .get(CameraXViewModel.class)
                .getProcessCameraProvider().observe(requireActivity(), new Observer<ProcessCameraProvider>() {
                            @Override
                            public void onChanged(ProcessCameraProvider provider) {
                                cameraProvider = provider;
                                try {
                                    if (!hasFrontCamera()) {
                                        tvTips.setText("front camera is unavailable");
                                        Log.e(TAG, " not have front camera . ");
                                        return;
                                    }
                                    if (!hasBackCamera()) {
                                        tvTips.setText("back camera is unavailable");
                                        Log.e(TAG, " not have back camera . ");
                                        return;
                                    }
                                } catch (Exception e) {

                                }
                                bindAllCameraUseCases();
                            }
                        }
                );
        mResourceMgr = new ResourceMgr(getContext());
        mHandler.sendEmptyMessage(UPDATE_FACT_DETECT_RESULT);
    }


    private void changeCamera() {
        int newLensFacing;
        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            newLensFacing = CameraSelector.LENS_FACING_BACK;
        } else {
            newLensFacing = CameraSelector.LENS_FACING_FRONT;
        }
        CameraSelector newCameraSelector = new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
        try {
            if (cameraProvider.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing;
                facePosResult = DetectResult.NONE;
                lookStraightResult = DetectResult.NONE;
                mHandler.removeMessages(UPDATE_FACT_DETECT_RESULT);
                mHandler.removeMessages(UPDATE_LIGHT_DETECT_RESULT);
                mHandler.sendEmptyMessage(UPDATE_FACT_DETECT_RESULT);
                cameraSelector = newCameraSelector;
                bindAllCameraUseCases();
                return;
            }
        } catch (CameraInfoUnavailableException e) {
            initFailure(e, "This device does not have lens with facing: $newLensFacing", false);
        }
    }

    private void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    private void bindPreviewUseCase() {
        Log.i(TAG, " bind preview use case ");
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }
        Preview.Builder builder = new Preview.Builder();
        // TODO
//        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(getContext(), lensFacing);
//        if (targetResolution != null) {
//            builder.setTargetResolution(targetResolution);
//        }
        previewUseCase = builder.build();
        previewUseCase.setSurfaceProvider(pvView.getSurfaceProvider());
        try {
            cameraProvider.bindToLifecycle( /* lifecycleOwner= */this, cameraSelector, previewUseCase);
        } catch (Exception e) {
            Log.e(TAG, "bindPreviewUseCase:" + e.getMessage());
            initFailure(e, "Failed to process", false);
        }

    }

    private void bindAnalysisUseCase() {
        Log.i(TAG, " bind analysis use case ");
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        if (imageProcessor != null) {
            imageProcessor.stop();
        }
        try {
            imageProcessor = new CameraFaceDetectorProcessor(getContext());
//            imageProcessor.setShowPoint(true);
            imageProcessor.setObserver(new CameraFaceDetectorProcessor.Observer() {
                @Override
                public void onFaceDetectChange(int lightingStrengthResult, int facePosResult, int lookStraightResult) {
                    CameraScanningFragment.this.lightingStrengthResult = lightingStrengthResult;
                    CameraScanningFragment.this.facePosResult = facePosResult;
                    CameraScanningFragment.this.lookStraightResult = lookStraightResult;
                    mHandler.removeMessages(UPDATE_FACT_DETECT_RESULT);
                    mHandler.removeMessages(UPDATE_LIGHT_DETECT_RESULT);
                    mHandler.sendEmptyMessage(UPDATE_FACT_DETECT_RESULT);
                }

                @Override
                public void onLightStrengthChange(int lightingStrengthResult) {
                    CameraScanningFragment.this.lightingStrengthResult = lightingStrengthResult;
                    mHandler.removeMessages(UPDATE_LIGHT_DETECT_RESULT);
                    mHandler.sendEmptyMessage(UPDATE_LIGHT_DETECT_RESULT);
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    initFailure(e, "Failed to process", false);
                }
            });
            //模拟Exception情况  throw Exception("Test Exception .")
        } catch (Exception e){
            initFailure(e, "Error, Can not create image processor. ", true);
            return;
        }
        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
        // TODO
//        builder.setTargetResolution(targetResolution);
        analysisUseCase = builder.build();
        needUpdateGraphicOverlayImageSourceInfo = true;
        analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(getContext()), new ImageAnalysis.Analyzer() {
            @SuppressLint("UnsafeOptInUsageError")
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    boolean isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT;
                    int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        overlay.setImageSourceInfo(imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
                    } else {
                        overlay.setImageSourceInfo(imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false;
                }
                try {
                    imageProcessor.processImageProxy(imageProxy, overlay);
//                throw MlKitException("Test Exception .", 111)
                } catch (Exception e){
                    initFailure(e, "Failed to process image. ", false);
                }
            }
        });
        try {
            cameraProvider.bindToLifecycle( /* lifecycleOwner= */this, cameraSelector, analysisUseCase);
        } catch (Exception e){
            Log.e(TAG, "bindAnalysisUseCase: ${e.message}");
            initFailure(e, "Failed to process image. ", false);
        }
    }

    private void updateDetectResultUi() {
        if (getContext() == null || isRemoving() || isDetached()) {
            return;
        }
        boolean lightingAvailable = lightingStrengthResult >= DetectResult.MEDIUM;
        updateDetectLightResult();

        boolean facePosAvailable = facePosResult >= DetectResult.MEDIUM;
        tvFacePos.setCompoundDrawables(mResourceMgr.getLeftIconRes(facePosResult),null,null, null);
        tvFacePos.setBackgroundResource(getBgRes(facePosResult));

        boolean lookStraightAvailable = lookStraightResult >= DetectResult.MEDIUM;
        tvLookStraight.setCompoundDrawables(mResourceMgr.getLeftIconRes(lookStraightResult), null, null, null);
        tvLookStraight.setBackgroundResource(getBgRes(lookStraightResult));

        boolean isEnable = lightingAvailable && facePosAvailable && lookStraightAvailable;
        if (overlay.isEnabled() != isEnable) {
            overlay.setEnabled(isEnable);
        }
        if (ivTake.isEnabled() != isEnable) {
            ivTake.setEnabled(isEnable);
        }
    }

    private void updateDetectLightResult() {
//        tvLighting.setCompoundDrawables(mResourceMgr.getLeftIconRes(lightingStrengthResult)
//                    ,null,null,null);
//        tvLighting.setBackgroundResource(getBgRes(lightingStrengthResult));
    }

    private int getBgRes(@DetectResult int result) {
        switch (result) {
            case DetectResult.HIGH:
            case DetectResult.MEDIUM:
                return R.drawable.bg_face_scan_high;
            case DetectResult.NONE:
                return R.drawable.bg_face_scan_none;
        }
        return R.drawable.bg_face_scan_none;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermission) {
            bindAllCameraUseCases();
            if (imageProcessor != null){
                imageProcessor.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasPermission) {
            if (imageProcessor != null){
                imageProcessor.onPause();
                imageProcessor.stop();
            }
            if (mResourceMgr != null){
                mResourceMgr.onDestroy();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mResourceMgr != null){
            mResourceMgr.onDestroy();
        }
        mHandler.removeCallbacksAndMessages(null);
        if (imageProcessor != null){
            imageProcessor.stop();
        }
    }

    private void initFailure(Exception e, String desc, boolean needShowErrorPage) {
        if (!isAdded() || isRemoving() || isDetached()) {
            return;
        }
        Log.e(TAG, desc + ",${e.message}");
        String str = desc + "" + e.getLocalizedMessage();
//        LogSaver.logToFile(str)
        tvTips.setText(desc);
        ToastUtils.showShort(str);
        if (needShowErrorPage) {
            overlay.setEnabled(false);
            pvView.setVisibility(View.GONE);
        }

    }

    private void showNotExistFaceModuleError() {
//        try {
//            val builder = ThemedAlertDialog.Builder(context)
//            builder.setMessage(getString(R.string.google_play_face_support_not_exist))
//            builder.setPositiveButton(android.R.string.ok, null)
//            builder.create().show()
//        } catch (e:Exception){
//            e.printStackTrace()
//        }
    }

    private boolean hasBackCamera() {
        if (cameraProvider == null) {
            return false;
        }
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (Exception e) {

        }
        return false;
    }

    private boolean hasFrontCamera() {
        if (cameraProvider == null) {
            return false;
        }
        try {
            return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA);
        } catch (Exception e) {

        }
        return false;
    }
}
