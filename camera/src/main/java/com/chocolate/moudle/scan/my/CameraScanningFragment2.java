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
import android.util.Size;
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
import com.chocolate.moudle.scan.base.FaceDetectorProcessor;
import com.chocolate.moudle.scan.base.FaceSaveState;
import com.chocolate.moudle.scan.base.GraphicOverlay;
import com.chocolate.moudle.scan.base.PreferenceUtils;

public class CameraScanningFragment2 extends Fragment {

    public static final String TAG = "CameraScanningFragment2";


    private static final int TYPE_BLINK_LEFT_EYE = 111;
    private static final int TYPE_BLINK_RIGHT_EYE = 112;
    private static final int TYPE_SMILE = 113;
    private static final int TYPE_CANCEL_BLINK_LEFT_EYE = 1113;
    private static final int TYPE_CANCEL_BLINK_RIGHT_EYE = 1114;
    private static final int TYPE_CANCEL_SMILE = 1115;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case TYPE_BLINK_LEFT_EYE:
                    tvLeft.setText("假设这有一只猫,眨左眼");
                    mHandler.removeMessages(TYPE_CANCEL_BLINK_LEFT_EYE);
                    mHandler.sendEmptyMessageDelayed(TYPE_CANCEL_BLINK_LEFT_EYE, 1000);
                    break;
                case TYPE_BLINK_RIGHT_EYE:
                    tvRight.setText("猫猫眨右眼");
                    mHandler.removeMessages(TYPE_CANCEL_BLINK_RIGHT_EYE);
                    mHandler.sendEmptyMessageDelayed(TYPE_CANCEL_BLINK_RIGHT_EYE, 1000);
                    break;
                case TYPE_SMILE:
                    tvRight.setText("猫猫微笑");
                    mHandler.removeMessages(TYPE_CANCEL_SMILE);
                    mHandler.sendEmptyMessageDelayed(TYPE_CANCEL_SMILE, 1000);
                    break;
                case TYPE_CANCEL_BLINK_LEFT_EYE:
                    tvLeft.setText("");
                    break;
                case TYPE_CANCEL_BLINK_RIGHT_EYE:
                    tvRight.setText("");
                    break;
                case TYPE_CANCEL_SMILE:
                    tvSmile.setText("");
                    break;
            }
            return false;
        }
    });

    private ProcessCameraProvider cameraProvider;
    private GameFaceDetectorProcessor imageProcessor;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private CameraSelector cameraSelector;

    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private boolean needUpdateGraphicOverlayImageSourceInfo = false;

    //是否有权限
    private boolean hasPermission = false;

    private PreviewView pvView;
    private GraphicOverlay overlay;
    private AppCompatTextView tvLeft, tvRight, tvSmile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanning2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pvView = view.findViewById(R.id.scanning_preview_view2);
        overlay = view.findViewById(R.id.scanning_graphic_overlay2);
        tvLeft = view.findViewById(R.id.tv_scanning2_left);
        tvRight = view.findViewById(R.id.tv_scanning2_right);
        tvSmile = view.findViewById(R.id.tv_scanning2_smile);

        lensFacing = CameraSelector.LENS_FACING_FRONT;

        overlay.setNeedDrawFaceOval(true);
        overlay.post(new Runnable() {
            @Override
            public void run() {
                if (isDetached() || isRemoving() || getContext() == null) {
                    return;
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
        new ViewModelProvider(CameraScanningFragment2.this, (ViewModelProvider.Factory) factory)
                .get(CameraXViewModel.class)
                .getProcessCameraProvider().observe(requireActivity(), new Observer<ProcessCameraProvider>() {
                            @Override
                            public void onChanged(ProcessCameraProvider provider) {
                                cameraProvider = provider;
                                try {
                                    if (!hasFrontCamera()) {
                                        Log.e(TAG, " not have front camera . ");
                                        return;
                                    }
                                } catch (Exception e) {

                                }
                                bindAllCameraUseCases();
                            }
                        }
                );
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
        // TODO 600x600
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(getContext(), lensFacing);
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
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
            imageProcessor = new GameFaceDetectorProcessor(getContext());
            imageProcessor.setObserver(new GameFaceDetectorProcessor.Observer() {
                @Override
                public void onLeftEyeBlink() {
                    if (mHandler != null) {
                        mHandler.removeMessages(TYPE_BLINK_LEFT_EYE);
                        mHandler.sendEmptyMessage(TYPE_BLINK_LEFT_EYE);
                    }
                }

                @Override
                public void onRightEyeBlink() {
                    if (mHandler != null) {
                        mHandler.removeMessages(TYPE_BLINK_RIGHT_EYE);
                        mHandler.sendEmptyMessage(TYPE_BLINK_RIGHT_EYE);
                    }
                }

                @Override
                public void onSmile(boolean smileFlag) {
                    mHandler.removeMessages(TYPE_SMILE);
                    if (smileFlag) {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(TYPE_SMILE);
                        }
                    } else {
                        mHandler.sendEmptyMessageDelayed(TYPE_CANCEL_SMILE, 1000);
                    }
                }

                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
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

    @Override
    public void onResume() {
        super.onResume();
        if (hasPermission) {
            bindAllCameraUseCases();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hasPermission) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
