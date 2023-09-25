package com.tiny.cash.loan.card.bean.upload

class InstallRequest {

    //  firebase app_instance_id Y
    var instanceId : String? = null
    // 安装来源,  google play,  official site， ..., other Y
    var source : String? = null
    //    广告渠道，google,facebook.... N
    var channel : String? = null
}