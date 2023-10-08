package com.tiny.cash.loan.card.collect

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ThreadUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.KudiCreditApp
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.response.HardwareResponseBean
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class CollectHardwareMgr {

    companion object {
        private const val TAG = "CollectHardwareMgr"
        val sInstance by lazy(LazyThreadSafetyMode.NONE) {
            CollectHardwareMgr()
        }
    }

    fun collectHardware(activity: Activity?, observer: Observer?) {
        var startMillions = System.currentTimeMillis()
        ThreadUtils.executeByCached(object : ThreadUtils.SimpleTask<Exception?>() {
            @Throws(Throwable::class)
            override fun doInBackground(): Exception? {
                try {
                    val duration = (System.currentTimeMillis() - startMillions)
                    logFile(" start collect hardware start = " + duration)

                    val jsonObject = buildRequestJsonObj(activity)
                    var jsonStr : String? = null
                    try {
                        jsonStr = jsonObject.toString()
                        jsonObject.put("jsonData", jsonStr) //以上参数的json集合
                    } catch ( e : Exception) {

                    }
                    getHardware(jsonObject,jsonStr, observer)
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        throw e
                    }
                    return e
                }
                return null
            }


            override fun onSuccess(result: Exception?) {
                if (result != null){
                    observer?.failure("collect data exception = " + result.toString())
                }
            }

        })
    }

    @SuppressLint("MissingPermission")
    private fun buildRequestJsonObj(context: Activity?) : JSONObject{
        val jsonObject = JSONObject()
        val acconutId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        jsonObject.put("accountId", acconutId)    //用户ID
        jsonObject.put("uniqDeviceId", DeviceUtils.getUniqueDeviceId()) //用户设备唯一标示
        jsonObject.put("osBoard", Build.BOARD)  //获取设备基板名称
        jsonObject.put("osBootLoader", Build.BOOTLOADER)       //获取设备引导程序版本号
        jsonObject.put("osBrand", Build.BRAND) //获取设备品牌
        val abIs = DeviceUtils.getABIs()
        if (abIs != null) {
            val abi1 = abIs[0]
            jsonObject.put("osCpuAbi", abi1) //获取设备指令集名称（CPU的类型）
            if (abIs.size > 1) {
                jsonObject.put("osCpuAbi2", abIs[1]) //获取第二个指令集名称
            }
        }
        jsonObject.put("osDevice", Build.DEVICE) //获取设备驱动名称
        jsonObject.put("osDisplay", Build.DISPLAY) //获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
        jsonObject.put("osHardware", Build.HARDWARE) //设备硬件名称,一般和基板名称一样（BOARD）
        jsonObject.put("osHost", Build.HOST) //设备主机地址
        jsonObject.put("osId", Build.ID) //设备版本号
        jsonObject.put("osModel", DeviceUtils.getModel()) //获取手机的型号 设备名称
        jsonObject.put("osManufacturer", DeviceUtils.getManufacturer()) //获取设备制造商
        jsonObject.put("osProduct", Build.PRODUCT) //整个产品的名称
        jsonObject.put("osTags", Build.TAGS) //设备标签。如release-keys 或测试的 test-keys
        jsonObject.put("osTime", Build.TIME) //时间
        jsonObject.put("osType", Build.TYPE) //设备版本类型  主要为"user" 或"eng".
        jsonObject.put("osUser", Build.USER) //设备用户名 基本上都为android-build
        jsonObject.put("osTime", Build.TIME) //时间
        jsonObject.put("osvRelease", Build.VERSION.RELEASE) //获取系统版本字符串。如4.1.2 或2.2 或2.3等
        jsonObject.put("osvCodename", Build.VERSION.CODENAME) //设备当前的系统开发代号，一般使用REL代替
        jsonObject.put("osvIncremental", Build.VERSION.INCREMENTAL) //系统源代码控制值，一个数字或者git hash值
        jsonObject.put("osvSdk", Build.VERSION.SDK) //系统的API级别 一般使用下面大的SDK_INT 来查看
        jsonObject.put("osvSdkInt", DeviceUtils.getSDKVersionCode()) //系统的API级别 数字
        jsonObject.put("appRecognitionVerdict", Constant.appRecognitionVerdict)
        jsonObject.put("deviceRecognitionVerdict", Constant.deviceRecognitionVerdict)
        jsonObject.put("appLicensingVerdict", Constant.appLicensingVerdict)
        try {
            if (context != null) {
                val dm = DisplayMetrics()//屏幕度量
                context?.windowManager?.defaultDisplay?.getMetrics(dm)

                val wm : WindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay

                val screen_width = display.width
                val screen_height = display.height

                val physicalWidth = screen_width / dm.xdpi
                val physicalHeight = screen_height / dm.ydpi
                val physicalSize = Math.sqrt(Math.pow(physicalWidth.toDouble(), 2.0) + Math.pow(
                    physicalHeight.toDouble(), 2.0
                ))

                jsonObject.put("device_width", screen_width) //分辨率
                jsonObject.put("device_height", screen_height) //分辨率
                jsonObject.put("physical_size", physicalSize)
            }

            val currentLocale = Locale.getDefault()
            val currentLanguage = currentLocale.getLanguage()
            val currentCountry = currentLocale.getCountry()

            jsonObject.put("language", currentLanguage + "_" + currentCountry)

            var is_usb_debug : Boolean = false
            KudiCreditApp.instance?.applicationContext?.let {
                is_usb_debug = (Settings.Secure.getInt( KudiCreditApp.instance?.applicationContext?.contentResolver, Settings.Secure.ADB_ENABLED, 0) > 0)
            }
            jsonObject.put("is_usb_debug", is_usb_debug)

            jsonObject.put("mac", NetworkUtils.getIPAddress(true))
            var netWorkType : String = ""
            when (NetworkUtils.getNetworkType()) {
                (NetworkUtils.NetworkType.NETWORK_ETHERNET) -> {
                    netWorkType = "ethernet"
                }
                (NetworkUtils.NetworkType.NETWORK_WIFI) -> {
                    netWorkType = "wifi"
                }
                (NetworkUtils.NetworkType.NETWORK_4G) -> {
                    netWorkType = "4g"
                }
                (NetworkUtils.NetworkType.NETWORK_3G) -> {
                    netWorkType = "3g"
                }
                (NetworkUtils.NetworkType.NETWORK_2G) -> {
                    netWorkType = "2g"
                }
                (NetworkUtils.NetworkType.NETWORK_UNKNOWN) -> {
                    netWorkType = "unknow"
                }
                (NetworkUtils.NetworkType.NETWORK_NO) -> {
                    netWorkType = "no"
                }
                else -> {
                    netWorkType = "unknow"
                }
            }
            jsonObject.put("network_type", netWorkType)
            var result : String? = HardWareUtils.getSimOperatorName(context)
            if (TextUtils.isEmpty(result)) {
                result = HardWareUtils.getSimOperator(  context)
            }
            jsonObject.put("network_operator_name", result)

            KudiCreditApp.instance?.let {
                val am : ActivityManager = it.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val outInfo = ActivityManager.MemoryInfo();
                am.getMemoryInfo(outInfo)
                val availMem = outInfo.availMem
                val totalMem = outInfo.totalMem
                jsonObject.put("ram_total_size", totalMem)
                //内存可用大小
                jsonObject.put("ram_usable_size", availMem)
            }

        } catch ( e : Exception) {

        }
        return jsonObject
    }


    @SuppressLint("MissingPermission")
    private fun getHardware(
        jsonObject: JSONObject, jsonStr : String?,  observer: Observer?) {
        logFile(" start upload hardware .")
        val startMillions = System.currentTimeMillis()
        var myreqbody: RequestBody? = null
        try {
            myreqbody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                JSONObject(java.lang.String.valueOf(jsonObject)).toString()
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (myreqbody == null) {
            return
        }
        val observable = NetManager.getApiService().hareware(myreqbody).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        observable.subscribeWith(object : NetObserver<Response<HardwareResponseBean>>() {
            override fun onNext(response: Response<HardwareResponseBean>) {
                super.onNext(response)
                if (response.isSuccess) {
                    observer?.success("")
                    if (Constant.IS_COLLECT) {
                        logFile(jsonStr)
                    }
                }
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                var errorMsg: String? = null
                try {
                    errorMsg = exception.toString()
                } catch (e: Exception) {

                }
                observer?.failure(errorMsg)
                logFile("start upload auth failure =  " + (System.currentTimeMillis() - startMillions) + errorMsg)

            }
        })

    }

    interface Observer {
        fun success(response: String)
        fun failure(response: String?)
    }

    fun logFile(str : String?){
        if (TextUtils.isEmpty(str)) {
            return
        }
        if (BuildConfig.DEBUG) {
            Log.e("Test", str!!)
        }
        LogSaver.logToFile("Collect hareware  $str")
    }


}