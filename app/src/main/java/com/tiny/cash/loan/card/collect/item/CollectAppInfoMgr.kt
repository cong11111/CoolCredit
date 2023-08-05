package com.tiny.cash.loan.card.collect.item

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.tiny.cash.loan.card.collect.BaseCollectDataMgr
import com.tiny.cash.loan.card.collect.EncodeUtils
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.ui.bean.InstallPackageInfo

class CollectAppInfoMgr {
    companion object {
        private const val TAG = "CollectAppInfoMgr"

        val sInstance by lazy(LazyThreadSafetyMode.NONE) {
            CollectAppInfoMgr()
        }
    }

    init {
        getAppInfoAesStr()
    }

    var aesAppInfoStr :String? = null

    fun getAppInfoAesStr() : String{
        if (aesAppInfoStr == null){
            getAppInfoAesStrInternal()
            if (BuildConfig.DEBUG) {
                Log.e("Test", "cache appinfo success = " + aesAppInfoStr?.length)
            }
        }
        return aesAppInfoStr!!
    }

     fun getAppInfoAesStrInternal(){
        val originAppInfo = GsonUtils.toJson(readAllAppInfo())
        val tempAppInfo = EncodeUtils.encryptAES(originAppInfo)
        aesAppInfoStr = if (TextUtils.isEmpty(tempAppInfo)) "" else tempAppInfo
    }

    private fun readAllAppInfo(): ArrayList<InstallPackageInfo> {
        val list: HashMap<String, InstallPackageInfo> = HashMap<String, InstallPackageInfo>()
        val tempList: ArrayList<InstallPackageInfo> = ArrayList<InstallPackageInfo>()
        try {
            try {
                val appList = getLaunchAllApp()
                list.putAll(appList)
            } catch(e : Exception) {

            }
            val pm = Utils.getApp().packageManager ?: return tempList
            val installedPackages = pm.getInstalledPackages(0)
            for (i in installedPackages.indices) {
                val packageInfo = installedPackages[i]
                val appInfoRequest = InstallPackageInfo()
                appInfoRequest.pkgname = BaseCollectDataMgr.encodeData(packageInfo.packageName)
                appInfoRequest.installtime = packageInfo.firstInstallTime.toString()
                appInfoRequest.installtime_utc =
                    BaseCollectDataMgr.local2UTC(packageInfo.firstInstallTime)
                val ai = packageInfo.applicationInfo
                if (ai != null) {
                    val isSystem = ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
                    appInfoRequest.type = if (isSystem) "0" else "1"
                    try {
                        appInfoRequest.appname =
                            BaseCollectDataMgr.encodeData1(ai.loadLabel(pm).toString())
                    } catch (e: Exception) {
                    }
                }
                list.put(packageInfo.packageName, appInfoRequest)
            }
        }catch (e : Exception ){
            if (BuildConfig.DEBUG){
                throw e
            }
        }
        tempList.addAll(list.values)
        return tempList
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun getLaunchAllApp() : HashMap<String, InstallPackageInfo> {
        val list: HashMap<String, InstallPackageInfo> = HashMap<String, InstallPackageInfo>()

        val pManager: PackageManager = Utils.getApp().getPackageManager()
        val packlist = pManager.getInstalledApplications(0)

        for (index in 0 until packlist.size) {
            val appInfo = packlist[index]
            val appInfoRequest = InstallPackageInfo()
            appInfoRequest.pkgname = BaseCollectDataMgr.encodeData(appInfo.packageName)
            appInfoRequest.appname = appInfo.name
            appInfoRequest.type = if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) <= 0) "1" else "0"
            list.put(appInfo.packageName, appInfoRequest)
        }
        return list
    }
}