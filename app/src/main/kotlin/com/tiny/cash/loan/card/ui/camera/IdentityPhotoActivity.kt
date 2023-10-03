package com.tiny.cash.loan.card.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.BarUtils
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.camera2.CameraActivity2
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.ui.base.BaseActivity2

class IdentityPhotoActivity : BaseActivity2() {

    companion object {
        private const val TAG = "IdentityPhotoActivity"

        const val TYPE_MIN = 101
        const val TYPE_VOTER_CARD = 102

        fun launchActivity(context: Activity) {
            var intent = Intent(context, IdentityPhotoActivity::class.java)
            context.startActivity(intent)
        }

    }

    private var isFront : Boolean = true
    private var mType : Int = TYPE_MIN

    private var ivBack : AppCompatImageView? = null
    private var tvNin : AppCompatTextView? = null
    private var tvVotorCard : AppCompatTextView? = null
    private var ivCenter : AppCompatImageView? = null
    private var flTap : FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        setContentView(R.layout.activity_identity_photo)
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)
        tvNin = findViewById<AppCompatTextView>(R.id.tv_identity_photo_nin)
        tvVotorCard = findViewById<AppCompatTextView>(R.id.tv_identity_photo_voter_card)
        ivCenter = findViewById<AppCompatImageView>(R.id.iv_center)
        flTap = findViewById<FrameLayout>(R.id.fl_identity_tap)
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
                CameraActivity2.showMe(this@IdentityPhotoActivity, mType)
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
}