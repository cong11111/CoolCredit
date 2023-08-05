package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class BankDetail implements Serializable {


    /**
     * bankdetail : {"accountId":"201216170100000153","bankAccountChecked":true,"bankAccountNumber":"3333333333","bankCode":"044","bankName":"Access Bank","cardChecked":true,"cardNumber":"444444444444","ctime":"2020-12-16 19:28:16","cvv":"369","expireDate":"23/23","id":7,"utime":"2020-12-16 19:28:21"}
     */
    private boolean bankedit;

    public boolean isEdit() {
        return bankedit;
    }

    public void setEdit(boolean edit) {
        this.bankedit = edit;
    }
    private BankdetailBean bankdetail;

    public BankdetailBean getBankdetail() {
        return bankdetail;
    }

    public void setBankdetail(BankdetailBean bankdetail) {
        this.bankdetail = bankdetail;
    }

    public static class BankdetailBean implements Serializable {
        /**
         * accountId : 201216170100000153
         * bankAccountChecked : true
         * bankAccountNumber : 3333333333
         * bankCode : 044
         * bankName : Access Bank
         * cardChecked : true
         * cardNumber : 444444444444
         * ctime : 2020-12-16 19:28:16
         * cvv : 369
         * expireDate : 23/23
         * id : 7
         * utime : 2020-12-16 19:28:21
         *
         */

        private String accountId;
        private boolean bankAccountChecked;
        private String bankAccountNumber;
        private String bankCode;
        private String bankName;
        private boolean cardChecked;
        private String cardNumber;
        private String ctime;
        private String cvv;
        private String expireDate;
        private int id;
        private String utime;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public boolean isBankAccountChecked() {
            return bankAccountChecked;
        }

        public void setBankAccountChecked(boolean bankAccountChecked) {
            this.bankAccountChecked = bankAccountChecked;
        }

        public String getBankAccountNumber() {
            return bankAccountNumber;
        }

        public void setBankAccountNumber(String bankAccountNumber) {
            this.bankAccountNumber = bankAccountNumber;
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

        public boolean isCardChecked() {
            return cardChecked;
        }

        public void setCardChecked(boolean cardChecked) {
            this.cardChecked = cardChecked;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getCvv() {
            return cvv;
        }

        public void setCvv(String cvv) {
            this.cvv = cvv;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }
    }
}
