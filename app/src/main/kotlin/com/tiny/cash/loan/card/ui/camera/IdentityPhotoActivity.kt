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
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import com.chocolate.moudle.scan.camera2.CameraActivity2
import com.chocolate.moudle.scan.my.ScanActivity
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.utils.JumpPermissionUtils
import java.io.File

class IdentityPhotoActivity : BaseIdentityActivity() {

    companion object {
        private const val TAG = "IdentityPhotoActivity"
        private  const val KEY_NEED_SHOW_AUTH = "key_need_show_auth"
        private  const val KEY_NIN_PATH = "key_nin_path"
        private  const val KEY_VOTOR_CARD_PATH = "key_votor_card_path"

        const val TYPE_MIN = 101
        const val TYPE_VOTER_CARD = 102


        fun launchActivity(context: Activity, needShowAuth : Boolean) {
            var intent = Intent(context, IdentityPhotoActivity::class.java)
            intent.putExtra(KEY_NEED_SHOW_AUTH, needShowAuth)
            context.startActivity(intent)
        }

    }

    private val mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            return false
        }

    })

    private var isFront: Boolean = true
    private var mNeedShowAuth: Boolean = false
    private var mType: Int = TYPE_MIN

    private var ivBack: AppCompatImageView? = null
    private var tvNin: AppCompatTextView? = null
    private var tvVotorCard: AppCompatTextView? = null
    private var ivCenter: AppCompatImageView? = null
    private var flTap: FrameLayout? = null
    private var tvNext: AppCompatTextView? = null
    private var progressBar: ProgressBar? = null
    private var tvTap: AppCompatTextView? = null
    private var viewLoading: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.activity_identity_photo)
        mNeedShowAuth = intent.getBooleanExtra(KEY_NEED_SHOW_AUTH, false)
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)
        tvNin = findViewById<AppCompatTextView>(R.id.tv_identity_photo_nin)
        tvVotorCard = findViewById<AppCompatTextView>(R.id.tv_identity_photo_voter_card)
        ivCenter = findViewById<AppCompatImageView>(R.id.iv_center)
        flTap = findViewById<FrameLayout>(R.id.fl_identity_tap)
        tvNext = findViewById<AppCompatTextView>(R.id.tv_next)
        progressBar = findViewById<ProgressBar>(R.id.loading_progress)
        tvTap = findViewById<AppCompatTextView>(R.id.tv_identity_photo_tap)
        viewLoading = findViewById<AppCompatTextView>(R.id.view_loading)
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
        ivCenter?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
               if (isFront) {
                   if (TextUtils.isEmpty(minPath)) {
                       executeCamera()
                   }
               } else {
                   if (TextUtils.isEmpty(votorCardPath)) {
                       executeCamera()
                   }
               }
            }

        })
        flTap?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                executeCamera()
            }

        })
        tvNext?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                if (TextUtils.isEmpty(minPath) && TextUtils.isEmpty(votorCardPath)) {
                    return
                }
                tvNext?.visibility = View.GONE
                progressBar?.visibility = View.VISIBLE
                progressBar?.progress = 0
                if (isFront) {
                    if (TextUtils.isEmpty(minPath)) {
                        return
                    }
                    startUploadMtnFileOnly()
                } else {
                    if (TextUtils.isEmpty(votorCardPath)) {
                        return
                    }
                    startUploadVotorCardFileOnly()
                }
//                if (!TextUtils.isEmpty(minPath) && TextUtils.isEmpty(votorCardPath)) {
//                    startUploadMtnFileOnly()
//                } else if (TextUtils.isEmpty(minPath) && !TextUtils.isEmpty(votorCardPath)) {
//                    startUploadVotorCardFileOnly()
//                } else if (!TextUtils.isEmpty(minPath) && !TextUtils.isEmpty(votorCardPath)) {
//                    startUploadMtnFile()
//                }
            }

        })
        ivBack?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }

        })
        minPath = SPUtils.getInstance().getString(KEY_NIN_PATH)
        votorCardPath = SPUtils.getInstance().getString(KEY_VOTOR_CARD_PATH)
        updateNinAndVotorCard()
        updateMinAndVoterCardPreview()
    }

    private var minPath: String? = null
    private var votorCardPath: String? = null

    private fun executeCamera() {
        val isGranted = PermissionUtils.isGranted(Manifest.permission.CAMERA)
        if (isGranted) {
            CameraActivity2.showMe(this@IdentityPhotoActivity, mType)
        } else {
            PermissionUtils.permission(Manifest.permission.CAMERA)
                .callback(object : SimpleCallback {
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

    private fun updateNinAndVotorCard() {
        tvNin?.isSelected = isFront
        tvVotorCard?.isSelected = !isFront
    }

    private fun updateMinAndVoterCardPreview() {
        var showNextFlag: Boolean = false
        if (isFront) {
            if (!TextUtils.isEmpty(minPath)) {
                showNextFlag = true
            }
        } else {
            if (!TextUtils.isEmpty(votorCardPath)) {
                showNextFlag = true
            }
        }
        if (mType == TYPE_MIN) {
            if (TextUtils.isEmpty(minPath)) {
                ivCenter?.setImageResource(R.drawable.identity_1)
                tvTap?.text = getString(R.string.tap_to_shot)
            } else {
                Glide.with(this).load(minPath).into(ivCenter!!)
                tvTap?.text = getString(R.string.tap_retake)
            }
        } else {
            if (TextUtils.isEmpty(votorCardPath)) {
                ivCenter?.setImageResource(R.drawable.identity_bvn_2)
                tvTap?.text = getString(R.string.tap_to_shot)
            } else {
                Glide.with(this).load(votorCardPath).into(ivCenter!!)
                tvTap?.text = getString(R.string.tap_retake)
            }
        }
        tvNext?.isSelected = showNextFlag
        tvNext?.isEnabled = showNextFlag
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CameraActivity2.RESULT_CAMERA_CODE) {
            val path = data?.getStringExtra(CameraActivity2.KEY_RESULT_CAMERA_PATH)
            val type = data?.getIntExtra(CameraActivity2.KEY_RESULT_CAMERA_TYPE, TYPE_MIN)
            if (type == TYPE_MIN) {
                minPath = path
                SPUtils.getInstance().put(KEY_NIN_PATH, minPath)
            } else if (type == TYPE_VOTER_CARD) {
                votorCardPath = path
                SPUtils.getInstance().put(KEY_VOTOR_CARD_PATH, votorCardPath)
            }
            updateMinAndVoterCardPreview()
        }
    }

    private var hasUploadMtn = false
    private var hasUploadVotorCard = false

    private fun startUploadMtnFileOnly() {
        viewLoading?.visibility = View.VISIBLE
        mPresenter.startUpload("1", File(minPath), object : BaseUploadFilePresenter.UploadObserver {
            override fun onSuccess() {
                if (isFinishing || isDestroyed) {
                    return
                }
                hasUploadMtn = true
                Log.i("Okhttp", " on file mtn success 1 = ")
                onUploadSuccess()
            }

            override fun onProgress(progress: Int) {
                if (isFinishing || isDestroyed) {
                    return
                }
                onProgressChange(progress)
            }

            override fun onFailure(errorDesc: String, errorMsg: String) {
                onStartUploadError(errorDesc, errorMsg)
            }

        })
    }

    private fun startUploadVotorCardFileOnly() {
        viewLoading?.visibility = View.VISIBLE
        mPresenter.onDestroy()
        mPresenter.startUpload(
            "2",
            File(votorCardPath),
            object : BaseUploadFilePresenter.UploadObserver {
                override fun onSuccess() {
                    hasUploadVotorCard = true
                    Log.i("Okhttp", " on file votor card success ")
                    onUploadSuccess()
                }

                override fun onProgress(progress: Int) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    onProgressChange(progress)
                }

                override fun onFailure(errorDesc: String, errorMsg: String) {
                    onStartUploadError(errorDesc, errorMsg)
                }

            })
    }

    private fun startUploadMtnFile() {
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
                val temp = (progress / 2f).toInt()
                onProgressChange(temp)
            }

            override fun onFailure(errorDesc: String, errorMsg: String) {
                onStartUploadError(errorDesc, errorMsg)
            }

        })
    }

    private fun startUploadVotorCardFile() {
        mPresenter.onDestroy()
        mPresenter.startUpload(
            "2",
            File(votorCardPath),
            object : BaseUploadFilePresenter.UploadObserver {
                override fun onSuccess() {
                    hasUploadVotorCard = true
                    Log.i("Okhttp", " on file success 2 = ")
                    onUploadSuccess()
                }

                override fun onProgress(progress: Int) {
                    if (isFinishing || isDestroyed) {
                        return
                    }
                    val temp = (progress / 2f + 50f).toInt()
                    onProgressChange(temp)
                }

                override fun onFailure(errorDesc: String, errorMsg: String) {
                    onStartUploadError(errorDesc, errorMsg)
                }

            })
    }

    private fun onUploadSuccess() {
        mHandler?.post {
            if (isFinishing || isDestroyed) {
                return@post
            }
            viewLoading?.visibility = View.GONE
            ToastUtils.showShort("Identity photo upload success")
            if (mNeedShowAuth) {
                ScanActivity.showMe(this)
            }
            SPUtils.getInstance().put(KEY_NIN_PATH, "")
            SPUtils.getInstance().put(KEY_VOTOR_CARD_PATH, "")
            finish()
        }
    }

    private fun onProgressChange(progress: Int) {
        mHandler?.post(Runnable {
            if (isFinishing || isDestroyed) {
                return@Runnable
            }
            progressBar?.progress = progress
        })
        Log.i("Okhttp", " on progress 2 = " + progress)
    }

    private fun onStartUploadError(errorDesc: String, errorMsg: String) {
        if (isFinishing || isDestroyed) {
            return
        }
        Log.v(
            "Okhttp", " on file failure 1 errorDesc = " + errorDesc
                    + " errorMsg = " + errorMsg
        )
        mHandler.post(Runnable {
            if (isFinishing || isDestroyed) {
                return@Runnable
            }
            tvNext?.visibility = View.VISIBLE
            progressBar?.visibility = View.GONE
            viewLoading?.visibility = View.GONE
            if (!Constant.isAabBuild()) {
                LogSaver.logToFile(
                    " on file failure 1 errorDesc = " + errorDesc
                            + " errorMsg = " + errorMsg
                )
                Toast.makeText(
                    this@IdentityPhotoActivity,
                    " on file failure  errorDesc = " + errorDesc
                            + " errorMsg = " + errorMsg,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
        mPresenter.onDestroy()
    }

}