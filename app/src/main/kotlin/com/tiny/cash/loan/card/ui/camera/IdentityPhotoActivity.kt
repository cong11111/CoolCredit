package com.tiny.cash.loan.card.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.BarUtils
import com.chocolate.moudle.scan.camera2.CameraActivity2
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.ui.base.BaseActivity2

class IdentityPhotoActivity : BaseActivity2() {

    companion object {
        private const val TAG = "IdentityPhotoActivity"

        fun launchActivity(context: Activity) {
            var intent = Intent(context, IdentityPhotoActivity::class.java)
            context.startActivity(intent)
        }

    }

    private var isFront : Boolean = true

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
                updateNinAndVotorCard()
            }

        })
        tvVotorCard?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                isFront = false
                updateNinAndVotorCard()
            }

        })
        flTap?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                CameraActivity2.showMe(this@IdentityPhotoActivity)
            }

        })
        updateNinAndVotorCard()
    }

    private fun updateNinAndVotorCard() {
        tvNin?.isSelected = isFront
        tvVotorCard?.isSelected = !isFront
    }
}