package com.tiny.cash.loan.card.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.TextView
import com.tiny.cash.loan.card.kudicredit.R

class RequestPermissionDialog constructor(context: Context): Dialog(context) {

    init {
        window?.decorView?.setPadding(0, 0, 0, 0)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val lp: WindowManager.LayoutParams? = window?.attributes
        if (lp != null) {
            lp.width = WindowManager.LayoutParams.MATCH_PARENT //设置宽度
            lp.height = WindowManager.LayoutParams.MATCH_PARENT //设置高度
            lp.horizontalMargin = 0f
            lp.verticalMargin = 0f
            window?.attributes = lp
        }
        setContentView(R.layout.dialog_request_permission)
        val tvCancel: TextView = findViewById<TextView>(R.id.tv_request_permission_cancel)
        val tvAgree: TextView = findViewById<TextView>(R.id.tv_request_permission_agree)
        tvAgree.setOnClickListener {
            if (mListener != null) {
                mListener!!.onClickAgree()
            }
            dismiss()
        }
        tvCancel.setOnClickListener {
            if (mListener != null) {
                mListener!!.onClickCancel()
            }
            dismiss()
        }
    }

    private var mListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    abstract class OnItemClickListener {
        abstract fun onClickAgree()
        fun onClickCancel() {}
    }
}