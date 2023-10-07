package com.tiny.cash.loan.card.ui.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.SimpleCallback
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import com.chocolate.moudle.scan.camera2.CameraActivity2
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.utils.JumpPermissionUtils
import java.io.File

class IdentityPhotoActivity : BaseIdentityActivity() {

    companion object {
        private const val TAG = "IdentityPhotoActivity"

        const val TYPE_MIN = 101
        const val TYPE_VOTER_CARD = 102
        fun launchActivity(context: Activity) {
            var intent = Intent(context, IdentityPhotoActivity::class.java)
            context.startActivity(intent)
        }

    }

    private val mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            return false
        }

    })

    private var isFront : Boolean = true
    private var mType : Int = TYPE_MIN

    private var ivBack : AppCompatImageView? = null
    private var tvNin : AppCompatTextView? = null
    private var tvVotorCard : AppCompatTextView? = null
    private var ivCenter : AppCompatImageView? = null
    private var flTap : FrameLayout? = null
    private var tvNext : AppCompatTextView? = null
    private var progressBar : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.activity_identity_photo)
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)
        tvNin = findViewById<AppCompatTextView>(R.id.tv_identity_photo_nin)
        tvVotorCard = findViewById<AppCompatTextView>(R.id.tv_identity_photo_voter_card)
        ivCenter = findViewById<AppCompatImageView>(R.id.iv_center)
        flTap = findViewById<FrameLayout>(R.id.fl_identity_tap)
        tvNext = findViewById<AppCompatTextView>(R.id.tv_next)
        progressBar = findViewById<ProgressBar>(R.id.loading_progress)
        progressBar?.max = 100
        tvNin?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                isFront = true
                mType = TYPE_MIN
                updateNinAndVotorCard()
                updateMinAndVoterCardPreview()
            }

        })
        tvVotorCard?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                isFront = false
                mType = TYPE_VOTER_CARD
                updateNinAndVotorCard()
                updateMinAndVoterCardPreview()
            }

        })
        flTap?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val isGranted = PermissionUtils.isGranted(Manifest.permission.CAMERA)
                if (isGranted) {
                    CameraActivity2.showMe(this@IdentityPhotoActivity, mType)
                } else {
                    PermissionUtils.permission(Manifest.permission.CAMERA).callback(object : SimpleCallback {
                        override fun onGranted() {
                            CameraActivity2.showMe(this@IdentityPhotoActivity, mType)
                        }

                        override fun onDenied() {
                            ToastUtils.showShort("please allow permission.")
                            JumpPermissionUtils.goToSetting(this@IdentityPhotoActivity)
                        }

                    }).request()
                }
            }

        })
        tvNext?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                tvNext?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
                progressBar?.progress = 0
                startUploadMtnFile()
            }

        })
        ivBack?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }

        })
        updateNinAndVotorCard()
        updateMinAndVoterCardPreview()
    }

    private var minPath : String? = null
    private var votorCardPath : String? = null

    private fun updateNinAndVotorCard() {
        tvNin?.isSelected = isFront
        tvVotorCard?.isSelected = !isFront
    }

    private fun updateMinAndVoterCardPreview() {
        var showNextFlag : Boolean = true
        if (TextUtils.isEmpty(minPath) || TextUtils.isEmpty(votorCardPath)){
            showNextFlag = false
        }
        if (mType ==  TYPE_MIN) {
            if (TextUtils.isEmpty(minPath)){
                ivCenter?.setImageResource(R.drawable.identity_1)
            } else {
                Glide.with(this).load(minPath).into(ivCenter!!)
            }
        } else {
            if (TextUtils.isEmpty(votorCardPath)){
                ivCenter?.setImageResource(R.drawable.identity_bvn_2)
            } else {
                Glide.with(this).load(votorCardPath).into(ivCenter!!)
            }
        }
        if (showNextFlag) {
            tvNext?.visibility = View.VISIBLE
        } else {
            tvNext?.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraActivity2.RESULT_CAMERA_CODE) {
            val path = data?.getStringExtra(CameraActivity2.KEY_RESULT_CAMERA_PATH)
            val type = data?.getIntExtra(CameraActivity2.KEY_RESULT_CAMERA_TYPE, TYPE_MIN)
            if (type == TYPE_MIN) {
                minPath = path
            } else if (type == TYPE_VOTER_CARD) {
                votorCardPath = path
            }
            updateMinAndVoterCardPreview()
        }
    }

    private var hasUploadMtn = false
    private var hasUploadVotorCard = false
    private fun startUploadMtnFile(){
        mPresenter.startUpload("1", File(minPath), object : BaseUploadFilePresenter.UploadObserver {
            override fun onSuccess() {
                if (isFinishing || isDestroyed) {
                    return
                }
                hasUploadMtn = true
                Log.i("Okhttp", " on file success 1 = ")
                mHandler.postDelayed(Runnable {
                    startUploadVotorCardFile()
                }, 1000)
            }

            override fun onProgress(progress: Int) {
                if (isFinishing || isDestroyed) {
                    return
                }
                val progress = (progress / 2f).toInt()
                progressBar?.progress = progress
                Log.i("Okhttp", " on progress 1 = " + progress)
            }

            override fun onFailure(errorDesc: String, errorMsg: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                Log.e("Okhttp", " on file failure 1 errorDesc = " + errorDesc
                        + " errorMsg = " + errorMsg)
                if (!Constant.isAabBuild()) {
                    LogSaver.logToFile(" on file failure 1 errorDesc = " + errorDesc
                            + " errorMsg = " + errorMsg)
                    Toast.makeText(
                        this@IdentityPhotoActivity, " on file failure 1 errorDesc = " + errorDesc
                                + " errorMsg = " + errorMsg, Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    private fun startUploadVotorCardFile(){
        mPresenter.onDestroy()
        mPresenter.startUpload("2", File(votorCardPath), object : BaseUploadFilePresenter.UploadObserver {
            override fun onSuccess() {
                hasUploadVotorCard = true
                Log.i("Okhttp", " on file success 2 = ")
                mHandler?.post {
                    if (isFinishing || isDestroyed) {
                        return@post
                    }
                    ToastUtils.showShort("Identity photo upload success")
                    finish()
                }
            }

            override fun onProgress(progress: Int) {
                if (isFinishing || isDestroyed) {
                    return
                }
                val progress = (progress / 2f + 50f).toInt()
                progressBar?.progress = progress
                Log.i("Okhttp", " on progress 2 = " + progress)
            }

            override fun onFailure(errorDesc: String, errorMsg: String) {
                if (isFinishing || isDestroyed) {
                    return
                }
                Log.v("Okhttp", " on file failure 2 errorDesc = " + errorDesc
                        + " errorMsg = " + errorMsg)
                if (!Constant.isAabBuild()) {
                    LogSaver.logToFile(" on file failure 2 errorDesc = " + errorDesc
                            + " errorMsg = " + errorMsg)
                    Toast.makeText(
                        this@IdentityPhotoActivity, " on file failure 2 errorDesc = " + errorDesc
                                + " errorMsg = " + errorMsg, Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
        mPresenter.onDestroy()
    }
}