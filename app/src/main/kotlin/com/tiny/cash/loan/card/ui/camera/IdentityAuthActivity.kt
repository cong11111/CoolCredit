package com.tiny.cash.loan.card.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import com.chocolate.moudle.scan.my.ScanActivity
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import java.io.File

class IdentityAuthActivity : BaseIdentityActivity() {

    companion object {
        private const val TAG = "IdentityAuthActivity"
        private const val KEY_IDENTITY_AUTH_PATH = "key_identity_auth_path"

        const val TYPE_AUTH = 101

        fun launchActivity(context: Activity, bitmapPath: String) {
            var intent = Intent(context, IdentityAuthActivity::class.java)
            intent.putExtra(KEY_IDENTITY_AUTH_PATH, bitmapPath)
            context.startActivity(intent)
        }

    }

    private var mType: Int = TYPE_AUTH
    private var progressBar: ProgressBar? = null

    private var ivBack: AppCompatImageView? = null
    private var ivCenter: AppCompatImageView? = null
    private var flTap: FrameLayout? = null
    private var tvNext: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.activity_identity_auth)
        selfiePath = intent.getStringExtra(KEY_IDENTITY_AUTH_PATH)
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)

        ivCenter = findViewById<AppCompatImageView>(R.id.iv_center)
        flTap = findViewById<FrameLayout>(R.id.fl_identity_tap)
        tvNext = findViewById<AppCompatTextView>(R.id.tv_next)
        progressBar = findViewById<ProgressBar>(R.id.loading_progress)
        progressBar?.max = 100
        flTap?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ScanActivity.showMe(this@IdentityAuthActivity)
                finish()
            }

        })
        tvNext?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                tvNext?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
                progressBar?.progress = 0
                startUploadSelfieFile()
            }

        })
        if (ivCenter != null && !TextUtils.isEmpty(selfiePath)) {
            Glide.with(this).load(selfiePath).into(ivCenter!!)
        }
    }

    private var selfiePath: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanActivity.RESULT_SCAN_CODE) {
            val path = data?.getStringExtra(ScanActivity.KEY_RESULT_CAMERA_PATH)
            selfiePath = path
        }
    }

    private var hasUploadSelfie = false
    private fun startUploadSelfieFile() {
        mPresenter?.startUpload(
            "3",
            File(selfiePath),
            object : BaseUploadFilePresenter.UploadObserver {
                override fun onSuccess() {
                    hasUploadSelfie = true
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    if (BuildConfig.DEBUG) {
                        Log.i("Okhttp", " on file success 3 = ")
                    }
                    ToastUtils.showShort("Identity auth upload success")
                    finish()
                }

                override fun onProgress(progress: Int) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    val progress = (progress / 2f + 50f).toInt()
                    progressBar?.progress = progress
                    if (BuildConfig.DEBUG) {
                        Log.i("Okhttp", " on progress 2 = " + progress)
                    }
                }

                override fun onFailure(errorDesc: String, errorMsg: String) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    if (BuildConfig.DEBUG) {
                        Log.e(
                            "Okhttp", " on file failure errorDesc = " + errorDesc
                                    + " errorMsg = " + errorMsg
                        )
                    }
                    if (!Constant.isAabBuild()) {
                        LogSaver.logToFile(" on file failure errorDesc = " + errorDesc
                                + " errorMsg = " + errorMsg)
                        Toast.makeText(
                            this@IdentityAuthActivity,
                            "errorDesc = $errorDesc errorMsg = $errorMsg", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}