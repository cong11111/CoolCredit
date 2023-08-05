package com.tiny.cash.loan.card.net.request.params;

import java.io.Serializable;

public class BankCardParams implements Serializable {


    /**
     * accountId : 201111130100000432
     * cardNumber : 634423410928310834
     * expireDate : 11/20
     * cvv : 366
     * reference : 8sjdf7sfsdf234345456
     */

    private String accountId;
    private String cardNumber;
    private String expireDate;
    private String cvv;
    private String reference;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
