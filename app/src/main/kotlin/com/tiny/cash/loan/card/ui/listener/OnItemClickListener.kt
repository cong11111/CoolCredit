package com.tiny.cash.loan.card.ui.listener

interface OnItemClickListener<DATA, BINDING> {
    fun onItemClick(position: Int, data: DATA, binding: BINDING)
}