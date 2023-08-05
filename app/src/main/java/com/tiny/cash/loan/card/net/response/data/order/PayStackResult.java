package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;

public class PayStackResult implements Serializable {
    private String accountId;
    private String orderId;
    private String status; // status的值有三个 success 成功 failed 失败 penging 入帐中

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
}
