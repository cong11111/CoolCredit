package com.tiny.cash.loan.card

import android.util.Log
import com.blankj.utilcode.util.ThreadUtils
import com.chocolate.moudle.scan.bean.UploadFileSignRequest
import com.chocolate.moudle.scan.bean.UploadFileSignResponse
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.server.LoggingInterceptor
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class UploadFilePresenter : BaseUploadFilePresenter() {

    private var requestUploadSignObserver : NetObserver<Response<UploadFileSignResponse>>? = null

    override fun requestUploadSign(imageType : String, file: File) {
        val request = UploadFileSignRequest()
        request.token = Constant.mToken
        request.imageType = imageType
        val observable: Observable<Response<UploadFileSignResponse>> =NetManager.getApiService2().uploadFileGetSign(request.token, request.imageType).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(requestUploadSignObserver)
        requestUploadSignObserver = object : NetObserver<Response<UploadFileSignResponse>>() {
            override fun onNext(response: Response<UploadFileSignResponse>) {
                if (response == null) {
                    return
                }
                if (response.isSuccess) {
                    val signRequest = response.body
                    if (BuildConfig.DEBUG) {
                        Log.e("Test", "request upload sign observer success = ${signRequest.accessKeyId}")
                    }
                    if (signRequest != null) {
                        onRequestSuccess(signRequest, file)
                    }
                }
            }

            override fun onException(netException: ResponseException) {

            }
        }
        observable.subscribeWith(requestUploadSignObserver)
    }

    override fun startUploadFile(uploadFile: UploadFileSignResponse?, file : File,  observer: UploadObserver) {
        ThreadUtils.executeByCached(object : ThreadUtils.SimpleTask<File>() {
            override fun doInBackground(): File {
                return startUploadInternal(uploadFile, file)
            }

            override fun onSuccess(result: File?) {
            }

        })
    }

    private fun startUploadInternal(uploadFile: UploadFileSignResponse?, file: File) : File {
        val url = uploadFile?.host
        if (BuildConfig.DEBUG) {
            Log.e("OkHttp", "start upload file to oss internal ... ")
        }
        val path = file.absolutePath
        var name = "jpg"
        val split = path.split(".")
        if (split != null && split.size == 2) {
            name = split[1]
        }
        if (BuildConfig.DEBUG) {
            Log.e("Okhttp", " name = " + name)
        }
        val fileBody = RequestBody.create(MediaType.get("image/$name"), file)
        //这边是把file写进来，也有写路径的，但我这边是写file文件，parse不行的话可以直接改这个"multipart/form-data"
        // 创建OkHttpClient实例,设置超时时间
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .addInterceptor(LoggingInterceptor())
            .build();
//        接口返回的 dir 字段， 自己要拼接上文件名
//        val keyStr = uploadFile?.dir + File.separator + file.name
        val keyStr = uploadFile?.dir + file.name
        // 不仅可以支持传文件，还可以在传文件的同时，传参数
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM) // 设置传参为form-data格式
            .addFormDataPart("OSSAccessKeyId", uploadFile?.accessKeyId)
            .addFormDataPart("policy", uploadFile?.policy)
            .addFormDataPart("Signature", uploadFile?.signature)
            .addFormDataPart("key", keyStr)
            .addFormDataPart("callback", uploadFile?.callback)
            .addFormDataPart("file", file.name, fileBody) // 中间参数为文件名
            .build();
        if (BuildConfig.DEBUG) {
            Log.i("Okhttp", " url = $url")
            Log.i("Okhttp", " keyStr = $keyStr")
        }
        // 构建request请求体，有需要传请求头自己加
        val request = Request.Builder().url(url).post(requestBody).build();
        var response : okhttp3.Response? = null;
        var result : String? = "";
        try {
            // 发送请求
            response = okHttpClient.newCall(request).execute()
            result = response.body()!!.string()
            if (!response.isSuccessful) {
                Log.e("Test", "请求失败")
            }
            response.body()!!.close();
            if (BuildConfig.DEBUG) {
                Log.e("Okhttp", " result = " + result)
            }
        } catch (e : IOException) {
            Log.e("Test", " exceprion = " + e.toString())
        }
        return file
    }
}