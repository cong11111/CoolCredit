package com.tiny.cash.loan.card.ui.bean

/**
 * *作者: jyw
 * 日期：2020/10/22 10:02
 */
class CallInfo {
    /**
     * 通话时长
     */
    var duration = 0

    /**
     * 通话记录的电话号码
     */
    var number: String? = ""

    /**
     * 通话记录的日期
     */
    var date: String? = ""

    /**
     * 通话记录的联系人
     */
    var name: String? = ""

    /**
     * 通话类型 1.呼入2.呼出3.未接
     */
    var type = 0
}