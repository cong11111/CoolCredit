package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class AppConfigFile implements Serializable {

    /**
     * bank : Zenith Bank
     * whatsApp : +2348084890423
     * accountName : Bestfin Nigeria Limited
     * phone : 018887100
     * ussd : 1
     * accountNumber : 1130088382
     * email : support@goodicredit.com
     */

    private String bank;
    private String whatsApp;
    private String whatsApp1;

    public String getWhatsApp1() {
        return whatsApp1;
    }

    public void setWhatsApp1(String whatsApp1) {
        this.whatsApp1 = whatsApp1;
    }

    private String accountName;
    private String phone;
    private String ussd;
    private String accountNumber;
    private String email;
    private String auditMsg;
    private String fiveStarTitle;
    private String fiveStarContent;
    private String kochavaSwitch;// 0 关 1 开
    private String bankCardFailMessage; // 正常失败返回内容
    private String repayBankCardFailMessage; //从还款界面进来绑卡失败提示内容
    private String  bankCardRetryMessage ;//临近到期还款前绑卡失败提示
    private String bankCardSwitch; //银行卡验证开关 0 关 1 开
    private String flutterwavePayMethod; // 开关  内容 account,card,transfer,ussd


    private String paystackCardBind; //0关闭，1打开
    private String repayChannel; // 开关  内容paystack_card,paystack_h5,flutterwave_h5,monnify_account,redocly_h5

    public String getPaystackCardBind() {
        return paystackCardBind;
    }

    public void setPaystackCardBind(String paystackCardBind) {
        this.paystackCardBind = paystackCardBind;
    }

    public String getRepayChannel() {
        return repayChannel;
    }

    public void setRepayChannel(String repayChannel) {
        this.repayChannel = repayChannel;
    }


    public String getFlutterwavePayMethod() {
        return flutterwavePayMethod;
    }

    public void setFlutterwavePayMethod(String flutterwavePayMethod) {
        this.flutterwavePayMethod = flutterwavePayMethod;
    }

    public String getBankCardRetryMessage() {
        return bankCardRetryMessage;
    }

    public void setBankCardRetryMessage(String bankCardRetryMessage) {
        this.bankCardRetryMessage = bankCardRetryMessage;
    }

    public String getBankCardSwitch() {
        return bankCardSwitch;
    }

    public void setBankCardSwitch(String bankCardSwitch) {
        this.bankCardSwitch = bankCardSwitch;
    }

    public String getBankCardFailMessage() {
        return bankCardFailMessage;
    }

    public void setBankCardFailMessage(String bankCardFailMessage) {
        this.bankCardFailMessage = bankCardFailMessage;
    }

    public String getRepayBankCardFailMessage() {
        return repayBankCardFailMessage;
    }

    public void setRepayBankCardFailMessage(String repayBankCardFailMessage) {
        this.repayBankCardFailMessage = repayBankCardFailMessage;
    }

    public String getKochavaSwitch() {
        return kochavaSwitch;
    }

    public void setKochavaSwitch(String kochavaSwitch) {
        this.kochavaSwitch = kochavaSwitch;
    }

    public String getFiveStarTitle() {
        return fiveStarTitle;
    }

    public void setFiveStarTitle(String fiveStarTitle) {
        this.fiveStarTitle = fiveStarTitle;
    }

    public String getFiveStarContent() {
        return fiveStarContent;
    }

    public void setFiveStarContent(String fiveStarContent) {
        this.fiveStarContent = fiveStarContent;
    }

    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUssd() {
        return ussd;
    }

    public void setUssd(String ussd) {
        this.ussd = ussd;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
