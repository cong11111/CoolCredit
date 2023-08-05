package com.tiny.cash.loan.card.ui.listener

interface OnItemLongClickListener<DATA, BINDING> {
    fun onItemLongClick(position: Int, data: DATA, binding: BINDING): Boolean
}