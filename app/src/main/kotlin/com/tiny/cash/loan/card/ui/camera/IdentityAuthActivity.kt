package com.tiny.cash.loan.card.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
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
        const val PATH_AUTH_PATH = "path_auth_path"
        const val TYPE_AUTH = 101

        fun launchActivity(context: Activity, bitmapPath: String) {
            var intent = Intent(context, IdentityAuthActivity::class.java)
            intent.putExtra(KEY_IDENTITY_AUTH_PATH, bitmapPath)
            context.startActivity(intent)
        }

        fun checkExistAndToSelfie(context: Activity) {
            val authPath = SPUtils.getInstance().getString(PATH_AUTH_PATH)
            if (!TextUtils.isEmpty(authPath) && File(authPath).exists()) {
                launchActivity(context, authPath)
            } else {
                ScanActivity.showMe(context)
            }
        }

    }

    private val mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            return false
        }

    })

    private var progressBar: ProgressBar? = null

    private var ivBack: AppCompatImageView? = null
    private var ivCenter: AppCompatImageView? = null
    private var flTap: FrameLayout? = null
    private var tvNext: AppCompatTextView? = null
    private var tvTap: AppCompatTextView? = null
    private var viewLoading: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.activity_identity_auth)
        selfiePath = intent.getStringExtra(KEY_IDENTITY_AUTH_PATH)
        val authPath = SPUtils.getInstance().getString(PATH_AUTH_PATH)
        if (!TextUtils.isEmpty(authPath)) {
            selfiePath = authPath
        }
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)

        ivCenter = findViewById<AppCompatImageView>(R.id.iv_center)
        flTap = findViewById<FrameLayout>(R.id.fl_identity_tap)
        tvNext = findViewById<AppCompatTextView>(R.id.tv_next)
        progressBar = findViewById<ProgressBar>(R.id.loading_progress)
        tvTap = findViewById<AppCompatTextView>(R.id.tv_auth_tap)
        viewLoading = findViewById<AppCompatTextView>(R.id.view_loading)
        progressBar?.max = 100
        ivBack?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }

        })
        tvTap?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                updateAuthTextView()
            }

        })
        ivCenter?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(selfiePath)) {
                    ScanActivity.showMeToSelfie(this@IdentityAuthActivity)
                }
            }

        })
        flTap?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ScanActivity.showMeToSelfie(this@IdentityAuthActivity)
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
        updateAuthTextView()
    }

    private var selfiePath: String? = null

    private fun updateAuthTextView() {
        if (TextUtils.isEmpty(selfiePath)) {
            tvTap?.text = getString(R.string.tap_to_shot)
        } else {
            tvTap?.text = getString(R.string.tap_retake)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanActivity.RESULT_SCAN_CODE) {
            val path = data?.getStringExtra(ScanActivity.KEY_RESULT_CAMERA_PATH)
            selfiePath = path
            SPUtils.getInstance().put(PATH_AUTH_PATH, selfiePath)
            updateAuthTextView()
        }
    }

    private var hasUploadSelfie = false
    private fun startUploadSelfieFile() {
        viewLoading?.visibility = View.VISIBLE
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
                    mHandler.post(Runnable {
                        if (isFinishing || isDestroyed) {
                            return@Runnable
                        }
                        viewLoading?.visibility = View.GONE
                        SPUtils.getInstance().put(PATH_AUTH_PATH, "")
                        ToastUtils.showShort("Identity auth upload success")
                        finish()
                    })
                }

                override fun onProgress(progress: Int) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    val progress = (progress / 2f + 50f).toInt()
                    mHandler.post(Runnable {
                        if (isFinishing || isDestroyed) {
                            return@Runnable
                        }
                        progressBar?.progress = progress
                    })
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
                    mHandler.post(Runnable {
                        if (isFinishing || isDestroyed) {
                            return@Runnable
                        }
                        viewLoading?.visibility = View.GONE
                        tvNext?.visibility = View.VISIBLE
                        progressBar?.visibility = View.GONE
                        if (!Constant.isAabBuild()) {
                            LogSaver.logToFile(" on file failure errorDesc = " + errorDesc
                                    + " errorMsg = " + errorMsg)
                        Toast.makeText(
                            this@IdentityAuthActivity,
                            "errorDesc = $errorDesc errorMsg = $errorMsg", Toast.LENGTH_SHORT
                        ).show()
                        }
                    })

                }
            })
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}