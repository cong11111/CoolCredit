package com.tiny.cash.loan.card.ui.dialog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.KeyboardUtils.OnSoftInputChangedListener
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.base.BaseActivity
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.ui.view.EmailAutoCompleteTextView
import java.util.regex.Pattern

class InputBasicInfoActivity : BaseActivity() {

    private var et: EmailAutoCompleteTextView? = null
    private var viewClear: View? = null
    private var viewCommit: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this@InputBasicInfoActivity, true)
        setContentView(R.layout.dialog_input_basic_info)
        et = findViewById<EmailAutoCompleteTextView>(R.id.et_personal_email)
        viewClear = findViewById<View>(R.id.iv_clear)
        et?.setOnEditorActionListener(object : OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var text = ""
                if (et != null) {
                    text = et!!.text.toString()
                }
                if (text != null && matches(text)) {
                    finishAndInput()
                } else {
                    notMatchToast()
                }
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
        viewCommit = findViewById<View>(R.id.tv_commit)
        viewCommit?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                var text = ""
                if (et != null) {
                    text = et!!.text.toString()
                }
                //这里正则写的有点粗暴:)
                if (text != null && matches(text)) {
                    finishAndInput()
                } else {
                    notMatchToast()
                }
            }

        })
        KeyboardUtils.registerSoftInputChangedListener(this@InputBasicInfoActivity, object : OnSoftInputChangedListener {
            override fun onSoftInputChanged(height: Int) {
               val layoutParams =  viewCommit?.layoutParams as MarginLayoutParams
                if (height == 0) {
                    layoutParams.bottomMargin = resources.getDimension(R.dimen.dp_15).toInt()
                } else {
                    layoutParams.bottomMargin = height + resources.getDimension(R.dimen.dp_2).toInt()
                }
                viewCommit?.layoutParams = layoutParams
            }

        })
    }

    private fun notMatchToast() {
        ToastUtils.showShort("This email format is incorrect,pls check it.")
    }

    private fun finishAndInput(needSetData : Boolean = true) {
        val intent = Intent()
        var data = ""
        if (et != null) {
            data = et!!.text.toString()
        }
        intent.putExtra("data", data)
        if (needSetData) {
            setResult(111, intent)
        } else {
            setResult(111)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (et != null) {
            KeyboardUtils.hideSoftInput(et!!)
        }
    }

    override fun onBackPressed() {
        finishAndInput(false)
    }
}

private fun matches(input : String): Boolean {
   return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", input)
}
