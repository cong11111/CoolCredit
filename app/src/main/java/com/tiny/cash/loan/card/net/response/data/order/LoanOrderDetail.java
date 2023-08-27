package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;
import java.util.List;

public class LoanOrderDetail implements Serializable {

    /**
     * orderId : 2012120200000003455
     * stageList : [{"amount":2850,"disburseTime":"2020-12-04","repayDate":"2020-12-18","fee":540,"feePrepaid":540,"interest":36,"interestPrepaid":36,"stageNo":"1","totalAmount":2850},{"amount":150,"disburseTime":"2020-12-04","repayDate":"2021-03-04","fee":137,"feePrePaid":137,"interest":10,"interestPrePaid":10,"stageNo":"2","totalAmount":150}]
     * status : 1
     * token : abc3345b7f274d024s177e9cecd458d3
     * totalAmount : 3000
     */

    private String orderId;
    private String status;//"1":approving "2":active "3":paid "4":overdue "5":"declined"
    private String token;
    private String totalAmount;
    private String reason;
    private boolean canApply;
    private String reloan; //0 首贷  大于0复贷
    private String limitday;
    private List<StageListBean> stageList;

    //1是首次, 0是非首次
    private int firstApprove = 0;

    public int getFirstApprove() {
        return firstApprove;
    }

    public void setFirstApprove(int firstApprove) {
        this.firstApprove = firstApprove;
    }

    public String getReloan() {
        return reloan;
    }

    public void setReloan(String reloan) {
        this.reloan = reloan;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isCanApply() {
        return canApply;
    }

    public void setCanApply(boolean canApply) {
        this.canApply = canApply;
    }

    public String getLimitday() {
        return limitday;
    }

    public void setLimitday(String limitday) {
        this.limitday = limitday;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<StageListBean> getStageList() {
        return stageList;
    }

    public void setStageList(List<StageListBean> stageList) {
        this.stageList = stageList;
    }

    public static class StageListBean implements Serializable{

        /**
         * amount : 2850
         * disburseTime : 2020-12-04
         * repayDate : 2020-12-18
         * fee : 540
         * feePrepaid : 540
         * interest : 36
         * interestPrepaid : 36
         * stageNo : 1
         * totalAmount : 2850
         * feePrePaid : 137
         * interestPrePaid : 10
         */
        private String amount;  //应还本金
        private String disburseTime;//放款时间
        private String repayDate;//应还日期
        private String fee;// 服务费
        private String stageNo;// 当期期号
        private String totalAmount;// 当期应还总金额
        private String feePrePaid;//砍头服务费
        private String interest;//应还利息
        private String interestPrePaid;// 砍头利息
        private String status; //当期状态 "1":approving "2":active "3":paid "4":overdue "5":"declined"
        private String penalty; // 罚息
        private boolean payable;//是否可以还款

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPenalty() {
            return penalty;
        }

        public void setPenalty(String penalty) {
            this.penalty = penalty;
        }

        public boolean getPayable() {
            return payable;
        }

        public void setPayable(boolean payable) {
            this.payable = payable;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDisburseTime() {
            return disburseTime;
        }

        public void setDisburseTime(String disburseTime) {
            this.disburseTime = disburseTime;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
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

        public String getFeePrePaid() {
            return feePrePaid;
        }

        public void setFeePrePaid(String feePrePaid) {
            this.feePrePaid = feePrePaid;
        }
    }
}
