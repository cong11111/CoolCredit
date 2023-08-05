package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class BankResult implements Serializable {

    /**
     * accessCode : 9E64A2R25983F
     */

    private String accessCode;
    private boolean bankAccountChecked;
    private boolean hasUpload;
    private String accountMessage;
    private String cardMessage;
    private boolean bvnChecked;

    private String cardExist;//0不存在 1存在
    private String cardNotExistMessage;//不存在返回的信息


    private String cardNotBindNotify;// 0不通知 1通知
    private String cardNotBindMessage;//通知的内容

    public String getCardNotBindNotify() {
        return cardNotBindNotify;
    }

    public void setCardNotBindNotify(String cardNotBindNotify) {
        this.cardNotBindNotify = cardNotBindNotify;
    }

    public String getCardNotBindMessage() {
        return cardNotBindMessage;
    }

    public void setCardNotBindMessage(String cardNotBindMessage) {
        this.cardNotBindMessage = cardNotBindMessage;
    }

    public String getCardExist() {
        return cardExist;
    }

    public void setCardExist(String cardExist) {
        this.cardExist = cardExist;
    }

    public String getCardNotExistMessage() {
        return cardNotExistMessage;
    }

    public void setCardNotExistMessage(String cardNotExistMessage) {
        this.cardNotExistMessage = cardNotExistMessage;
    }

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
    }

    public String getAccountMessage() {
        return accountMessage;
    }

    public void setAccountMessage(String accountMessage) {
        this.accountMessage = accountMessage;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public void setCardMessage(String cardMessage) {
        this.cardMessage = cardMessage;
    }

    public boolean isHasUpload() {
        return hasUpload;
    }

    public void setHasUpload(boolean hasUpload) {
        this.hasUpload = hasUpload;
    }

    public boolean isBankAccountChecked() {
        return bankAccountChecked;
    }

    public void setBankAccountChecked(boolean bankAccountChecked) {
        this.bankAccountChecked = bankAccountChecked;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
