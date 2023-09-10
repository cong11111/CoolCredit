package com.tiny.cash.loan.card.bean.bank

class BankResponseBean {

    var banklist : List<Bank>? = null

    class Bank {
        var bankCode: String? = null
        var bankName: String? = null
    }
}