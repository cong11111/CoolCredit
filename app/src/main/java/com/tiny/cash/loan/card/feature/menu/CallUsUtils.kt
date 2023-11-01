package com.tiny.cash.loan.card.feature.menu

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import java.io.File

object CallUsUtils {
    fun getFileUri(context: Context, file: File, authority: String): Uri? {
        var useProvider = false
        var canAdd = false
        if (file.exists()) {
            useProvider = true
            canAdd = true
        }
        return if (canAdd) {
            if (useProvider) {
                FileProvider.getUriForFile(getDPContext(context), authority, file)
            } else {
                Uri.fromFile(file)
            }
        } else null
    }

    private fun getDPContext(context: Context): Context {
        var storageContext: Context = context
        if (Build.VERSION.SDK_INT >= 24) {
            if (!context.isDeviceProtectedStorage) {
                val deviceContext = context.createDeviceProtectedStorageContext()
                storageContext = deviceContext
            }
        }
        return storageContext
    }

    fun getAuthority(context: Context) =
        context.applicationInfo.packageName + ".fileprovider"

    fun startFeedBackEmail(traceFile: String?, context: Context, email : String) {
        try {
            val data = Intent(Intent.ACTION_SEND)
            data.data = Uri.parse(email)
            data.setType("text/plain")
            val addressEmail = arrayOf<String>(email!!)
            data.putExtra(Intent.EXTRA_EMAIL, addressEmail)
            if (!Constant.isAabBuild()){
                val addressCC = arrayOf<String>("wang867103701@gmail.com")
                data.putExtra(Intent.EXTRA_CC, addressCC)
            }
            data.putExtra(Intent.EXTRA_SUBJECT, "Icredit Feedback")
            data.putExtra(Intent.EXTRA_TEXT, "Hi:  ")
            if (!TextUtils.isEmpty(traceFile)) {
                data.putExtra(
                    Intent.EXTRA_STREAM,
                    getFileUri(context, File(traceFile!!), getAuthority(context))
                )
            }
            context?.startActivity(Intent.createChooser(data, "Icredit Feedback:"))
        } catch (e: Exception) {
            if ((e is ActivityNotFoundException)) {
                ToastUtils.showShort(" not exist email app")
            }
            if (BuildConfig.DEBUG) {
                throw e
            }
        }
    }
}