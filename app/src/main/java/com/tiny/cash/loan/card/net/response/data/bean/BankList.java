package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;
import java.util.List;

public class BankList implements Serializable {
    private List<CardList> cardlist;

    public List<CardList> getCardlist() {
        return cardlist;
    }

    public void setCardlist(List<CardList> cardlist) {
        this.cardlist = cardlist;
    }

    public static class CardList implements Serializable {
        private String accountId;
        private String cardNumber;
        private String expireDate;
        private String cvv;
        private Boolean cardChecked;
        private String status;
        private String ctime;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

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

        public Boolean getCardChecked() {
            return cardChecked;
        }

        public void setCardChecked(Boolean cardChecked) {
            this.cardChecked = cardChecked;
        }
    }
}
