package com.chocolate.moudle.scan.camera2

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.chocolate.moudle.scan.BuildConfig
import com.chocolate.moudle.scan.CameraSdk
import com.chocolate.moudle.scan.bean.UploadFileSignResponse
import com.chocolate.moudle.scan.luban.Luban
import com.chocolate.moudle.scan.luban.OnCompressListener
import java.io.File
import java.util.Locale


abstract class BaseUploadFilePresenter {

    fun startUpload(imageType : String, file1 : File, observer: UploadObserver) {
        val context = CameraSdk.mAppContext
        val parentFile = File(context?.cacheDir, "scan/upload")
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        if (BuildConfig.DEBUG) {
            Log.i("Okhttp", " unzip before size = " + FileUtils.getSize(file1) + " path .  " + file1.absolutePath)
        }
        val photos = ArrayList<String>()
        photos.add(file1.absolutePath)
        observer?.onProgress(5)
        Luban.with(context)
            .load<String>(photos)
            .ignoreBy(100)
            .setTargetDir(parentFile.path)
            .filter { path ->
                !(TextUtils.isEmpty(path) || path.lowercase(Locale.getDefault())
                    .endsWith(".gif"))
            }
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {
                }

                override fun onSuccess(file: File) {
                    if (BuildConfig.DEBUG) {
                        Log.e("Okhttp", " unzip size = " + FileUtils.getSize(file) + " path .  " + file.absolutePath)
                    }
                    observer?.onProgress(20)
                    requestUploadSign(imageType, file, observer)
                }

                override fun onError(e: Throwable) {
                    observer.onFailure("compress picture error", e.message.toString())
                    if (BuildConfig.DEBUG) {
                        Log.e("Okhttp", " error = " + e.message)
                    }
                }
            }).launch()
    }

    abstract fun requestUploadSign(imageType : String, file : File , observer: UploadObserver)


    fun onRequestSuccess(uploadFile: UploadFileSignResponse?, file : File , observer: UploadObserver) {
        startUploadFile(uploadFile, file, observer)
    }

    fun onUploadProgressChange(progress: Int, observer: UploadObserver){
        val resultProgress : Int = 20 + (progress  * 7f / 10).toInt()
        observer?.onProgress(resultProgress)
    }

    fun onUploadFileSuccess(result: File?, observer: UploadObserver){
        observer?.onProgress(100)
        observer?.onSuccess()
    }

    fun onUploadFileFailure(errorMsg: String, observer: UploadObserver){
        observer?.onFailure(errorMsg, "error")
    }

    abstract fun startUploadFile(uploadFile: UploadFileSignResponse?, file : File, observer: UploadObserver)

    interface UploadObserver {
        fun onSuccess()

        fun onProgress(progress : Int)

        fun onFailure(errorDesc : String, errorMsg: String)
    }

    open fun onDestroy(){

    }
}