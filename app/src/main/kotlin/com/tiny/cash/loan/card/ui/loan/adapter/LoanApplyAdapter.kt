package com.tiny.cash.loan.card.ui.loan.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tiny.cash.loan.card.bean.loan.ProductResponseBean
import com.tiny.cash.loan.card.bean.loan.TrialResponseBean
import com.tiny.cash.loan.card.kudicredit.R

class LoanApplyAdapter : RecyclerView.Adapter<LoanApplyHolder> {

    private var mList: ArrayList<TrialResponseBean.Trial>? = null

    private var mListener: OnItemClickListener? = null


    constructor(list: ArrayList<TrialResponseBean.Trial>){
        mList = list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanApplyHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_loan_apply_detail, parent, false)
        return LoanApplyHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: LoanApplyHolder, position: Int) {
        val trial: TrialResponseBean.Trial = mList!![position]

        holder.tvTitle?.text = getTitleByPos(position)

        if (!TextUtils.isEmpty(trial.disburseAmount)) {
            holder.tvOriginAmount?.text = trial.disburseAmount
        }
        if (!TextUtils.isEmpty(trial.fee)) {
            holder.tvOrigin?.text = trial.fee
        }
        if (!TextUtils.isEmpty(trial.interest)) {
            holder.tvInterest?.text = trial.interest
        }
        if (!TextUtils.isEmpty(trial.totalAmount)) {
            holder.tvAmount?.text = trial.totalAmount
        }
        if (!TextUtils.isEmpty(trial.repayDate)) {
            holder.tvPurpose?.text = trial.repayDate
        }
    }

    private fun getTitleByPos(pos : Int) : String{
        if (mList != null && mList!!.size == 1){
            return "Bills details"
        }
        if (pos == 0){
            return "1st Installment"
        } else {
            return "2st Installment"
        }
    }


    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    fun setItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onClick(product: ProductResponseBean.Product, pos: Int)
    }

}