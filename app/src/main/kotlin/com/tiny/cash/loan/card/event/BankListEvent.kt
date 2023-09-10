package com.tiny.cash.loan.card.event

import com.tiny.cash.loan.card.bean.bank.BankResponseBean

class BankListEvent {

    var mData: BankResponseBean.Bank? = null

    constructor(data: BankResponseBean.Bank?) {
        mData = data
    }
}