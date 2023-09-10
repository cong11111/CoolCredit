package com.tiny.cash.loan.card.utils

import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.DeviceUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import okhttp3.internal.http.HttpHeaders
import org.json.JSONException

class BuildRequestJsonUtils {

//    fun buildHeadersNonLogin(): HttpHeaders {
//        val httpHeaders = HashMap()
//        try {
//            // device	String	Y	设备型号
//            httpHeaders.put("APP-Name", BuildConfig.APPLICATION_ID)  //设备型号
//            httpHeaders.put("APP-ID", "2")  //设备型号
//            //platform	String	Y	应用平台
//            httpHeaders.put("platform", "Google play")      //应用平台
//            // device	String	Y	设备型号
//            httpHeaders.put("device", DeviceUtils.getModel())  //设备型号
//            //  deviceId	String	Y	设备唯一标识
//            httpHeaders.put("deviceId", DeviceUtils.getUniqueDeviceId())   // 设备唯一标识
//            //brand	String	Y	手机品牌
//            httpHeaders.put("brand", DeviceUtils.getManufacturer())       //手机品牌
//            // os	String	Y	操作系统
//            httpHeaders.put("os", "Android")          //操作系统
//            // isSimulator	String	Y	是否为模拟器
//            httpHeaders.put("isSimulator", if (DeviceUtils.isEmulator()) "1" else "0") //是否为模拟器
//            // lang	String	Y	语言
//            httpHeaders.put("lang", "en")
//            //  innerVersionCode	Integer	Y	内部版本号
//            var innerVersionCode = MyAppUtils.getAppVersionCode()
////            LogSaver.logToFile("Test 111 =  " + innerVersionCode )
////            Log.e("Test" ," 111 =  " + innerVersionCode )
//            httpHeaders.put("innerVersionCode", innerVersionCode.toString())   //内部版本号
//            //   appVersion	String	Y	APP版本号
//            httpHeaders.put("appVersion", MyAppUtils.getAppVersionName())   //APP版本号
//            //  channel	String	Y	安装包发布的渠道
//            httpHeaders.put("channel", "google play")   //安装包发布的渠道
//            //H5,google play
//            // utmSource	String	Y	客户来源
////                httpHeaders.put("utmSource", "")   //客户来源
//            //banner,click,search words
//            //  utmMedium	String	Y	媒介
////                httpHeaders.put("utmMedium", "")   //媒介
//            //   imei
//            httpHeaders.put("imei", if (!TextUtils.isEmpty(Constant.imei)) Constant.imei
//            else DeviceUtils.getAndroidID())        //imei
//            // longitude	String	Y	经度
////                httpHeaders.put("longitude", "")   //经度
//            //latitude	String	Y	纬度
////                httpHeaders.put("latitude", "")
//        } catch (e: JSONException) {
//            Log.e("BuildRequestJsonUtils", e.toString())
//            e.printStackTrace()
//        }
//        return httpHeaders
//    }
}