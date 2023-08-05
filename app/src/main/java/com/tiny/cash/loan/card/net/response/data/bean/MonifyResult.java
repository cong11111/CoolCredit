package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class MonifyResult implements Serializable {

    private String reserved;//reserved 是否是虚拟账号 0 咱们原来银行信息 1
    private String bankCode;
    private String bankName;
    private String accountName;
    private String accountNumber;

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
