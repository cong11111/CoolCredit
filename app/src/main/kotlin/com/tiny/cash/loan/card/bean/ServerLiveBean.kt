package com.tiny.cash.loan.card.bean

class ServerLiveBean {

    var status :String? = null

    fun isServerLive(): Boolean {
        return status == "live"
    }

}