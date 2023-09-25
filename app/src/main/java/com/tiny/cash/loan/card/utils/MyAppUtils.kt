package com.tiny.cash.loan.card.utils

import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.BuildConfig

object MyAppUtils {
    fun getAppVersionCode() : Int{
        var innerVersionCode = Constant.CUR_VERSION_CODE
        try {
            innerVersionCode = AppUtils.getAppVersionCode()
        } catch (e : Exception) {

        }
        if (innerVersionCode < 20000){
            innerVersionCode = Constant.CUR_VERSION_CODE
        }
        innerVersionCode = Constant.CUR_VERSION_CODE
        return innerVersionCode
    }

    fun getAppVersionName() : String{
        var appName : String = ""
        try {
            appName = AppUtils.getAppVersionName().replace("Version","").trim()
        } catch (e : Exception){
            if (BuildConfig.DEBUG){
                throw e
            }
        }
        if (TextUtils.isEmpty(appName)) {
            appName = Constant.CUR_VERSION_NAME
        }
        appName = Constant.CUR_VERSION_NAME
        return appName
    }
}