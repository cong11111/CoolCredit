//package com.chocolate.moudle.scan.my;
//
//import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED;
//import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED;
//import static com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED;
//
//import android.app.Application;
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.chocolate.moudle.scan.BuildConfig;
//import com.google.android.gms.common.moduleinstall.InstallStatusListener;
//import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse;
//import com.google.android.gms.common.moduleinstall.ModuleInstall;
//import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
//import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
//import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
//import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
////import com.google.android.gms.tflite.client.TfLiteClient;
////import com.google.android.gms.tflite.java.TfLite;
//
//public class GoogleModuleInstallHelper {
//    private boolean hasInstall = false;
//
//    private ModuleInstallClient mModuleInstallClient;
//
//    private static final String TAG = "TestScanning";
//
//    private static GoogleModuleInstallHelper instance;
//
//    public static GoogleModuleInstallHelper getInstance() {
//        synchronized (TAG) {
//            if (instance == null) {
//                synchronized (TAG) {
//                    instance = new GoogleModuleInstallHelper();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private Context appContext;
//
//    public void init(Application app) {
//        appContext = app.getApplicationContext();
//        if (appContext == null) {
//            return;
//        }
//        mModuleInstallClient = ModuleInstall.getClient(appContext);
//        TfLiteClient optionalModuleApi = TfLite.getClient(appContext);
//        mModuleInstallClient.areModulesAvailable(optionalModuleApi)
//                .addOnSuccessListener(new OnSuccessListener<ModuleAvailabilityResponse>() {
//                    @Override
//                    public void onSuccess(ModuleAvailabilityResponse response) {
//                        if (response.areModulesAvailable()) {
//                            hasInstall = true;
//                            // Modules are present on the device...
//                            if (BuildConfig.DEBUG) {
//                                Log.v(CameraScanningFragment.TAG, "success");
//                            }
//                        } else {
//                            hasInstall = false;
//                            installModule();
////                    LogSaver.logToFile("Modules are not present on the device.")
//                            Log.e(CameraScanningFragment.TAG, " has install all module = ");
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(CameraScanningFragment.TAG, " check module available failure ${it.message}");
//                        hasInstall = false;
////                LogSaver.logToFile("check module available failure = $it")
//                    }
//                });
//    }
//
//    public boolean hasInstall() {
//        return hasInstall;
//    }
//
//    private void installModule() {
//        TfLiteClient optionalModuleApi = TfLite.getClient(appContext);
////        LogSaver.logToFile("start install module . ")
//        ModuleInstallProgressListener listener = new ModuleInstallProgressListener(mModuleInstallClient);
//        ModuleInstallRequest moduleInstallRequest =
//                ModuleInstallRequest.newBuilder()
//                        .addApi(optionalModuleApi)
//                        // Add more APIs if you would like to request multiple optional modules.
//                        // .addApi(...)
//                        // Set the listener if you need to monitor the download progress.
//                        .setListener(listener)
//                        .build();
//        mModuleInstallClient.installModules(moduleInstallRequest)
//                .addOnSuccessListener(new OnSuccessListener<ModuleInstallResponse>() {
//                    @Override
//                    public void onSuccess(ModuleInstallResponse moduleInstallResponse) {
//                        if (moduleInstallResponse.areModulesAlreadyInstalled()) {
//                            // Modules are already installed when the request is sent.
//                            hasInstall = true;
//                        }
////                LogSaver.logToFile(" module hasInstall = $ " + it.areModulesAlreadyInstalled())
//                        Log.i(CameraScanningFragment.TAG, " module hasInstall = $ " + moduleInstallResponse.areModulesAlreadyInstalled());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //                LogSaver.logToFile(" module hasInstall failure = "
////                        + it.toString())
//                        // Handle failure…
//                        Log.e(CameraScanningFragment.TAG, " module hasInstall failure = "
//                                + e.toString());
//                    }
//                });
//
//    }
//
//
//    class ModuleInstallProgressListener implements InstallStatusListener {
//        private ModuleInstallClient mClient;
//
//        public ModuleInstallProgressListener(ModuleInstallClient client) {
//            mClient = client;
//        }
//
//        private boolean isTerminateState(@ModuleInstallStatusUpdate.InstallState int state) {
//            return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED;
//        }
//
//        @Override
//        public void onInstallStatusUpdated(@NonNull ModuleInstallStatusUpdate update) {
//            // Progress info is only set when modules are in the progress of downloading.
//            if (update.getProgressInfo() != null) {
//                ModuleInstallStatusUpdate.ProgressInfo info = update.getProgressInfo();
//                float progress = (info.getBytesDownloaded() * 100 / info.getTotalBytesToDownload());
//                // Set the progress for the progress bar.
//                Log.i(CameraScanningFragment.TAG, "download module progress = " + progress);
//            }
//            if (isTerminateState(update.getInstallState())) {
//                mClient.unregisterListener(this);
//            }
//        }
//    }
//}
