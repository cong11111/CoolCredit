package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;
import java.util.List;

public class LoanTrial implements Serializable {

    /**
     * totalFeeAmount : 564
     * repayDate : 2020-12-23 18:43:03
     * totalInterestAmount : 46
     * totalDisburseAmount : 2390
     * totalRepaymentAmount : 3000
     * trial : [{"disburseAmount":"2364","disburseTime":"2020-12-09 18:43:03","fee":"450","feePrePaid":"450","interest":"36","interestPrePaid":"36","loanAmount":"2850","repayDate":"2020-12-23 18:43:03","stageNo":"1","totalAmount":"2850"},{"disburseAmount":"26","disburseTime":"2020-12-09 18:43:03","fee":"114","feePrePaid":"114","interest":"10","interestPrePaid":"10","loanAmount":"150","repayDate":"2021-03-10 18:43:03","stageNo":"2","totalAmount":"150"}]
     */

    private String totalFeeAmount;
    private String repayDate;
    private String totalInterestAmount;
    private String totalDisburseAmount;
    private String totalRepaymentAmount;
    private List<TrialBean> trial;

    public String getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(String totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getTotalInterestAmount() {
        return totalInterestAmount;
    }

    public void setTotalInterestAmount(String totalInterestAmount) {
        this.totalInterestAmount = totalInterestAmount;
    }

    public String getTotalDisburseAmount() {
        return totalDisburseAmount;
    }

    public void setTotalDisburseAmount(String totalDisburseAmount) {
        this.totalDisburseAmount = totalDisburseAmount;
    }

    public String getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(String totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public List<TrialBean> getTrial() {
        return trial;
    }

    public void setTrial(List<TrialBean> trial) {
        this.trial = trial;
    }

    public static class TrialBean implements Serializable {
        /**
         * disburseAmount : 2364
         * disburseTime : 2020-12-09 18:43:03
         * fee : 450
         * feePrePaid : 450
         * interest : 36
         * interestPrePaid : 36
         * loanAmount : 2850
         * repayDate : 2020-12-23 18:43:03
         * stageNo : 1
         * totalAmount : 2850
         */

        private String disburseAmount;
        private String disburseTime;
        private String fee;
        private String feePrePaid;
        private String interest;
        private String interestPrePaid;
        private String loanAmount;
        private String repayDate;
        private String stageNo;
        private String totalAmount;

        public String getDisburseAmount() {
            return disburseAmount;
        }

        public void setDisburseAmount(String disburseAmount) {
            this.disburseAmount = disburseAmount;
        }

        public String getDisburseTime() {
            return disburseTime;
        }

        public void setDisburseTime(String disburseTime) {
            this.disburseTime = disburseTime;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFeePrePaid() {
            return feePrePaid;
        }

        public void setFeePrePaid(String feePrePaid) {
            this.feePrePaid = feePrePaid;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getInterestPrePaid() {
            return interestPrePaid;
        }

        public void setInterestPrePaid(String interestPrePaid) {
            this.interestPrePaid = interestPrePaid;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getStageNo() {
            return stageNo;
        }

        public void setStageNo(String stageNo) {
            this.stageNo = stageNo;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}
