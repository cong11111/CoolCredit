package com.tiny.cash.loan.card.ui.banklist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tiny.cash.loan.card.kudicredit.R

class SelectDataHolder : RecyclerView.ViewHolder {

    var tvSelectData: TextView? = null

    constructor(itemView: View) : super(itemView) {
        tvSelectData = itemView.findViewById(R.id.tv_select_data)
    }

}