package com.tiny.cash.loan.card

import androidx.multidex.MultiDexApplication
import co.paystack.android.PaystackSdk
import com.google.firebase.FirebaseApp
import com.tiny.cash.loan.card.collect.LocationMgr
import com.tiny.cash.loan.card.log.LogSaver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KudiCreditApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        PaystackSdk.initialize(applicationContext)
        FirebaseApp.initializeApp(this)
        LocationMgr.getInstance().init(this)
        LogSaver.init(applicationContext)
    }

    companion object {
        @JvmStatic
        var instance: KudiCreditApp? = null
        private const val AF_DEV_KEY = "Reyr9DiAhRaKenX5omk7wj"
        private const val APP_GUID_KEY = "koicredit-nk4lczjxw"
    }
}