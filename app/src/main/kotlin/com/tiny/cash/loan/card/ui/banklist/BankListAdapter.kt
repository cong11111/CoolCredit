package com.tiny.cash.loan.card.ui.banklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tiny.cash.loan.card.bean.bank.BankResponseBean
import com.tiny.cash.loan.card.kudicredit.R

class BankListAdapter : RecyclerView.Adapter<SelectDataHolder> {

    private var mLists: ArrayList<BankResponseBean.Bank>? = null
    private var mItemClickListener: OnItemClickListener? = null

    constructor(lists: ArrayList<BankResponseBean.Bank>?) {
        mLists = lists
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectDataHolder {
        return SelectDataHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_select_data, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SelectDataHolder, position: Int) {
        val bankBean = mLists!![position]
        holder.tvSelectData?.text = bankBean.bankName
        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(bankBean!!, position)
        }
    }

    override fun getItemCount(): Int {
        return if (mLists == null) 0 else mLists!!.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(bankBean: BankResponseBean.Bank, pos: Int)
    }
}