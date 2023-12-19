package com.tiny.cash.loan.card.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.tiny.cash.loan.card.base.BaseActivity
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.ui.view.EmailAutoCompleteTextView

class InputBasicInfoActivity : BaseActivity() {

    private var et: EmailAutoCompleteTextView? = null
    private var viewClear: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this@InputBasicInfoActivity, true)
        setContentView(R.layout.dialog_input_basic_info)
        et = findViewById<EmailAutoCompleteTextView>(R.id.et_personal_email)
        viewClear = findViewById<View>(R.id.iv_clear)
        et?.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                finishAndInput()

                return true
            }

        })
        et?.isFocusableInTouchMode = true
        et?.requestFocus()
        et?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null) {
                    return
                }
                try {
                    val str = s.toString().trim()
                    if (str.length > 0) {
                        viewClear?.visibility = View.VISIBLE
                    } else {
                        viewClear?.visibility = View.GONE
                    }
                } catch (e : java.lang.Exception) {
                    if (BuildConfig.DEBUG) {
                        throw e
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (isFinishing || isDestroyed) {
                return@Runnable
            }
            if (et != null) {
                KeyboardUtils.showSoftInput(et!!)
            }
        }, 300)
        viewClear?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                et?.setText("")
            }

        })
    }

    private fun finishAndInput() {

        val intent = Intent()
        var data = ""
        if (et != null) {
            data = et!!.text.toString()
        }
        intent.putExtra("data", data)
        setResult(111, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (et != null) {
            KeyboardUtils.hideSoftInput(et!!)
        }
    }

    override fun onBackPressed() {
        finishAndInput()
    }
}