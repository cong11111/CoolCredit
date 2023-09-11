package com.tiny.cash.loan.card.utils;

import com.tiny.cash.loan.card.Constants;

/**
 * 数据存储SP
 */
public class LocalConfig {
    public static final String LC_FBTOKEN = "fbtoken";
    public static final String LC_ACCOUNTID = "accountId";
    public static final String LC_TOKEN = "token";//TOKEN
    public static final String LC_MOBILE = "mobile";//Mobile
    public static final String LC_PASSWORD = "password";//password
    public static final String LC_UUID = "uuid";//UUID
    public static final String LC_ISAGREEMENT = "isAgreement";//是否同意协议

    public static final String LC_PLATFORM = "platform"; //应用平台
    public static final String LC_DEVICE = "device";    //设备型号
    public static final String LC_DEVICEID = "deviceId";//设备唯一标识
    public static final String LC_IMEI = "imei"; //IMEI
    public static final String LC_BRAND = "brand";//手机品牌
    public static final String LC_OS = "os";    //操作系统型号
    public static final String LC_ISSIMULATOR = "isSimulator";//是否为模拟器
    public static final String LC_LANG = "lang";//语言
    public static final String LC_LONGITUDE = "longitude";//经度
    public static final String LC_LATITUDE = "latitude";//纬度
    public static final String LC_APPVERSION = "appVersion";//APP版本号
    public static final String LC_CHANNEL = "channel";//安装包发布的渠道
    public static final String LC_UTMSOURCE = "utmSource";//客户来源
    public static final String LC_UTMMEDIUM = "utmMedium";//媒介
    public static final String LC_ANDROIDID = "androidId";//媒介

    public static final String LC_HASSHOWPERMISSION = "hasshowpermission";// false未展示，true已展示


    public static final String LC_BANK = "bank";
    public static final String LC_WHATSAPP = "whatsApp";
    public static final String LC_WHATSAPP1 = "whatsApp1";
    public static final String LC_ACCOUNTNAME = "accountName";
    public static final String LC_PHONE = "phone";
    public static final String LC_USSD = "ussd";//1开 0关
    public static final String LC_ACCOUNTNUMBER = "accountNumber";
    public static final String LC_EMAIL = "email";
    public static final String LC_AUDITMSG = "auditMsg";
    public static final String LC_FIVESTARTITLE = "fiveStarTitle";
    public static final String LC_FIVESTARCONTENT = "fiveStarContent ";

    public static final String LC_ORDERSUCESS = "ordersucess";
    public static final String LC_ORDERPROGRESS = "orderprogress";

    public static final String LC_SHOW_APP_RELOAN = "showappreloan";// 是否首贷
    public static final String LC_SHOW_APP_STARS2 = "showappstars2";
    public static final String LC_KOCHAVASWITCH = "kochavaSwitch ";
    public static final String LC_FIRSTORDER = "firstOrder ";
    public static final String LC_BANKCARDSWITCH = "bankCardSwitch ";

    public static final String LC_BANKCARDFAILMESSAGE = "bankCardFailMessage ";
    public static final String LC_REPAYBANKCARDFAILMESSAGE = "repayBankCardFailMessage ";
    public static final String LC_BANKCARDRETRYMESSAGE = "bankCardRetryMessage";

    public static final String LC_FLUTTERWAVEPAYMETHOD = "flutterwavePayMethod ";

    public static final String LC_REPAYCHANNEL = "repayChannel ";
    public static final String LC_PAYSTACKCARDBIND = "paystackCardBind ";

    public static final String LC_TXREF = "TxRef";

    public static final String LC_FIREBASEPUSH = "firebasepush";

    public static String getNewKey(String key) {
        String accountId = KvStorage.get(LC_ACCOUNTID, "");
        return accountId + key;
    }

    public static boolean isNewUser() {
        String state = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_FIRSTORDER), Constants.ZERO);
        return Constants.ZERO.equals(state);

    }
}
