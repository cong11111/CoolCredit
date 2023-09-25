package com.tiny.cash.loan.card.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.FileProvider
import androidx.databinding.library.BuildConfig
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.ZipUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.log.LogSaver
import java.io.File

object SendFileUtils {

    fun startFeedBackEmail(context: Activity) {
        ThreadUtils.executeByCached(object : ThreadUtils.SimpleTask<String?>() {
            @Throws(Throwable::class)
            override fun doInBackground(): String? {
                var logFoldPath = File(LogSaver.getLogFileFolder())
                if (logFoldPath.listFiles().isNotEmpty()) {
                    val srcFile = logFoldPath.listFiles()[0]
                    val traceFile =
                        File(context.filesDir.absolutePath + "/log/", "trace")
                    FileUtils.createFileByDeleteOldFile(traceFile)
                    val success = ZipUtils.zipFile(srcFile, traceFile)
                    if (success) {
                        return traceFile.absolutePath
                    }
                }
                return null
            }

            override fun onSuccess(result: String?) {
                startFeedBackEmail(result, context)
            }
        })
    }

    fun startFeedBackEmail(traceFile: String?, context: Activity) {
        try {
            val data = Intent(Intent.ACTION_SEND)
            data.data = Uri.parse("wang867103701@gmail.com")
            data.type = "text/plain"
            val addressEmail = arrayOf<String>("wang867103701@gmail.com")
            data.putExtra(Intent.EXTRA_EMAIL, addressEmail)
            if (!Constant.isAabBuild()) {
                val addressCC = arrayOf<String>("wang867103701@gmail.com")
                data.putExtra(Intent.EXTRA_CC, addressCC)
            }
            data.putExtra(Intent.EXTRA_SUBJECT, "iCredit Feedback")
//            val mobile = SPUtils.getInstance().getString(Constant.KEY_MOBILE)
            data.putExtra(Intent.EXTRA_TEXT, "Hi:  num "  + ",")
            if (!TextUtils.isEmpty(traceFile)) {
                data.putExtra(
                    Intent.EXTRA_STREAM,
                    getFileUri(context, File(traceFile!!), getAuthority(context))
                )
            }
            context.startActivity(Intent.createChooser(data, "Icredit Feedback:"))
        } catch (e: Exception) {
            if ((e is ActivityNotFoundException)) {
                ToastUtils.showShort(" not exist email app")
            }
            if (BuildConfig.DEBUG) {
                throw e
            }
        }
    }

    private fun getFileUri(context: Context, file: File, authority: String): Uri? {
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

    private fun getAuthority(context: Context) =
        context.applicationInfo.packageName + ".fileprovider"
}