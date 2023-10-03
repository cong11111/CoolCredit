package com.tiny.cash.loan.card.ui.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_photo)
        ivBack = findViewById<AppCompatImageView>(R.id.iv_identity_photo_back)
        tvNin = findViewById<AppCompatTextView>(R.id.tv_identity_photo_nin)
        tvVotorCard = findViewById<AppCompatTextView>(R.id.tv_identity_photo_voter_card)
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
        updateNinAndVotorCard()
    }

    private fun updateNinAndVotorCard() {
        tvNin?.isSelected = isFront
        tvVotorCard?.isSelected = !isFront
    }
}