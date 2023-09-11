package com.tiny.cash.loan.card

import android.text.TextUtils
import androidx.appcompat.app.AppCompatDelegate
import android.util.Log
import androidx.multidex.MultiDexApplication
import co.paystack.android.PaystackSdk
import com.blankj.utilcode.util.LanguageUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.tiny.cash.loan.card.collect.LocationMgr
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.utils.GooglePlaySdk
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class KudiCreditApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        PaystackSdk.initialize(applicationContext)
        FirebaseApp.initializeApp(this)
        LocationMgr.getInstance().init(this)
        LogSaver.init(applicationContext)
        LanguageUtils.applyLanguage(Locale.ENGLISH, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val s = KvStorage.get(LocalConfig.LC_UTMSOURCE, "")
        val s1 = KvStorage.get(LocalConfig.LC_UTMMEDIUM, "")
        if (TextUtils.isEmpty(s) || TextUtils.isEmpty(s1)) {
            GooglePlaySdk.getInstance(this)?.start()
        }
        initTestLog()
    }

    private fun initTestLog() {
        FirebaseAnalytics.getInstance(this).appInstanceId.addOnCompleteListener(
            object :
                OnCompleteListener<String> {
                override fun onComplete(p0: Task<String>) {
                    Log.e("Test", " task 222222 = " + p0.result)
                    LogSaver.logToFile("firebase analytic id =  " +  p0.result)
                }

            }
        )
    }

    companion object {
        @JvmStatic
        var instance: KudiCreditApp? = null
        private const val AF_DEV_KEY = "Reyr9DiAhRaKenX5omk7wj"
        private const val APP_GUID_KEY = "koicredit-nk4lczjxw"
    }
}