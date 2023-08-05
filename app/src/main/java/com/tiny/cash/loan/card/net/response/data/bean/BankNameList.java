package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;
import java.util.List;

public class BankNameList implements Serializable {


    private List<BankList> banklist;

    public List<BankList> getBankList() {
        return banklist;
    }

    public void setBankList(List<BankList> bankList) {
        this.banklist = bankList;
    }

    public static class BankList implements Serializable{
        /**
         * bankCode : ZENB
         * bankName : Zenith Bank
         */

        private String bankCode;
        private String bankName;

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
    }
}
