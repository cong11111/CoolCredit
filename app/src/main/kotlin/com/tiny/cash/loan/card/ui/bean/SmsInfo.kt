package com.tiny.cash.loan.card.ui.bean

/**
 * *作者: jyw
 * 日期：2020/10/22 10:02
 */
class SmsInfo {
    /*
    * sms主要结构：
    *  _id：短信序号，如100
    *  thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的
    *  address：发件人地址，即手机号，如+8613811810000
    *  person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null
    *  date：日期，long型，如1256539465022，可以对日期显示格式进行设置
    *  protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信
    *  read：是否阅读0未读，1已读
    *  status：短信状态-1接收，0complete,64pending,128failed
    *  type：短信类型1是接收到的，2是已发出
    *  body：短信具体内容
    */
    /**
     * addr : 106907500362118
     * body : 【每日优鲜便利购】你的身体和你一样，需要运动！法国达能，碧悠新品谷物酸奶，尝鲜价5.9元，快去扫码抢购哟，退订回TD
     * time : 1516691107
     * read : 1
     * status : -1
     */
    var addr: String? = ""
    var body: String? = ""
    var time: String? = ""
    var read: String? = ""
    var status: String? = ""
    var type: String? = ""
}