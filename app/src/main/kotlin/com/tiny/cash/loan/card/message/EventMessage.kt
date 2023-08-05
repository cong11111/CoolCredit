package com.tiny.cash.loan.card.message

class EventMessage {
    @JvmField
    var what: Int
    var arg1 = 0
    var arg2 = 0
    @JvmField
    var obj: Any? = null

    constructor(what: Int) {
        this.what = what
    }

    constructor(what: Int, obj: Any?) {
        this.what = what
        this.obj = obj
    }

    constructor(what: Int, arg1: Int, obj: Any?) {
        this.what = what
        this.arg1 = arg1
        this.obj = obj
    }

    constructor(what: Int, arg1: Int, arg2: Int, obj: Any?) {
        this.what = what
        this.arg1 = arg1
        this.arg2 = arg2
        this.obj = obj
    }

    constructor(what: Int, arg1: Int) {
        this.what = what
        this.arg1 = arg1
    }

    constructor(what: Int, arg1: Int, arg2: Int) {
        this.what = what
        this.arg1 = arg1
        this.arg2 = arg2
    }

    companion object {
        const val EDU = 10000
        const val SALARY = 10001
        const val MARITAL = 10002
        const val RELATIONSHIP = 10003
        const val WORK = 10004
        const val STATEAREA = 10005
        const val UPLOADACTIVITY = 20000
        const val BANKCARDSUCCESS = 20001
        const val LOANDECLINED = 20002
        const val PAYSTACK = 20003
    }
}