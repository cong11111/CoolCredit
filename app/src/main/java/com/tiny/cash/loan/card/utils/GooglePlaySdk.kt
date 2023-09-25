package com.tiny.cash.loan.card.utils

import android.content.Context
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.blankj.utilcode.util.GsonUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.upload.InstallRequest
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GooglePlaySdk {
    var referrerClient: InstallReferrerClient? = null
    fun start() {
        referrerClient = InstallReferrerClient.newBuilder(mContext).build()
        referrerClient?.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.
                        var response: ReferrerDetails? = null
                        try {
                            if (referrerClient != null) {
                                response = referrerClient!!.installReferrer
                            }
                            if (response != null) {
                                val referrerUrl = response.installReferrer
                                if (!TextUtils.isEmpty(referrerUrl)) {
                                    // utmsource
                                    if (BuildConfig.DEBUG) {
                                        Log.e("Test", " url = $referrerUrl")
                                    }
                                    if (Constant.IS_COLLECT) {
                                        LogSaver.logToFile(" refer url = " + referrerUrl)
                                    }
                                    KvStorage.put(LocalConfig.LC_UTMSOURCE, referrerUrl)
//                                    var utmSource = tryGetUtmSource(referrerUrl)
//                                    if (!TextUtils.isEmpty(utmSource)) {
//                                        OkGo.getInstance().addCommonHeaders(BuildRequestJsonUtils.buildUtmSource(utmSource!!))
//                                    }
                                    var utmMedium =  tryGetUtmMedium(referrerUrl)
                                    if (TextUtils.isEmpty(utmMedium)) {
                                        utmMedium =  tryGetGCLID(referrerUrl)
                                    }
                                    if (!TextUtils.isEmpty(utmMedium)) {
                                        KvStorage.put(LocalConfig.LC_UTMMEDIUM, utmMedium)
                                    }
                                }
                                initInstanceId(referrerUrl)
                            }
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }

                        referrerClient?.endConnection()

                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                start()
            }
        })
    }

    //gclid=Cj0KCQiA99ybBhD9ARIsALvZavXgHa7-8tWDN5VPj2_f2GRsN8aLHbt7CUDkAvjo9EYwpeohYLqFci0aApFDEALw_wcB
    private fun tryGetGCLID(referrerUrl: String?) : String?{
        if (TextUtils.isEmpty(referrerUrl) || referrerUrl?.contains("gclid") != true){
            return null
        }
        val result = referrerUrl.replace("gclid=","")
        return result
    }

    private fun tryGetUtmSource(referrerUrl: String?) : String?{
        if (TextUtils.isEmpty(referrerUrl)){
            return null
        }
        var refererMap = getSplitData(referrerUrl!!)
        if (refererMap != null) {
            var map = refererMap.get("utm_source")
            return map
        }
        return null
    }
    private fun tryGetUtmMedium(referrerUrl: String?) : String?{
        if (TextUtils.isEmpty(referrerUrl)){
            return null
        }
        var refererMap = getSplitData(referrerUrl!!)
        if (refererMap != null) {
            var map = refererMap.get("utm_medium")
            return map
        }
        return null
    }

    /**
     * 把格式：utm_source=testSource&utm_medium=testMedium&utm_term=testTerm&utm_content=11
     * 这种格式的数据切割成key,value的形式并put进JSONObject对象，用于上传
     *
     * @param referer
     * @return
     */
    private fun getSplitData(referer: String) : MutableMap<String, String> {
        val map: MutableMap<String, String> = HashMap()
        if (referer.length > 2 && referer.contains("&")) {
            for (data in referer.split("&".toRegex()).toTypedArray()) {
                if (data.length > 2 && data.contains("=")) {
                    val split = data.split("=".toRegex()).toTypedArray()
                    for (i in split.indices) {
                        map[split[0]] = split[1]
                    }
                }
            }
        }
        return map
    }

    companion object {
        private var instance: GooglePlaySdk? = null
        var mContext: Context? = null
        fun getInstance(context: Context?): GooglePlaySdk? {
            if (instance == null) {
                instance = GooglePlaySdk()
            }
            mContext = context
            return instance
        }
    }

    private fun initInstanceId(referrerUrl : String) {
        if (mContext == null) {
            return
        }
        FirebaseAnalytics.getInstance(mContext!!).appInstanceId.addOnCompleteListener(
            object : OnCompleteListener<String> {
                override fun onComplete(p0: Task<String>) {
                    val analyticId = p0.result
                    uploadInstall(analyticId, referrerUrl)
                    LogSaver.logToFile("firebase analytic id =  " +  analyticId)
                }

            }
        )
    }

    private var mUploadInstallObserver: NetObserver<Response<*>>? = null
    private fun uploadInstall(instanceId : String, referrerUrl : String) {
        val mediumStr = tryGetUtmMedium(referrerUrl)
        var sourceStr = "other"
        val gclid = tryGetGCLID(referrerUrl)
        if (!TextUtils.isEmpty(gclid)) {
            sourceStr = "google play"
        } else {
            if (!TextUtils.isEmpty(mediumStr)) {
                sourceStr = mediumStr!!
            } else {
               val tempStr = tryGetUtmSource(referrerUrl)
                if (!TextUtils.isEmpty(tempStr)) {
                    sourceStr = tempStr!!
                }
            }
        }
        val request = InstallRequest()
        request.source = sourceStr
        request.instanceId = instanceId
        request.channel = BuildConfig.CREDIT_CHANNEL
        KvStorage.put(LocalConfig.LC_CHANNEL, mediumStr)
        if (Constant.IS_COLLECT) {
            LogSaver.logToFile("request install " + GsonUtils.toJson(request))
        }
        val observable: Observable<Response<*>> =
            NetManager.getApiService2().recordInstall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(mUploadInstallObserver)
        mUploadInstallObserver = object : NetObserver<Response<*>>() {
            override fun onNext(response: Response<*>) {
                if (response == null) {
                    return
                }
                if (response.isSuccess == true) {
                    KvStorage.put(LocalConfig.LC_FIREBASE_INSTANCE_ID, instanceId)
                    if (BuildConfig.DEBUG) {
                        Log.e("Test", " upload firebase instance id success = $instanceId")
                    }
                }
                if (Constant.IS_COLLECT) {
                    LogSaver.logToFile("request install " + GsonUtils.toJson(response))
                }
            }

            override fun onException(netException: ResponseException) {

            }
        }
        observable.subscribeWith(mUploadInstallObserver)
    }
}