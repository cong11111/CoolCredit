package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;

public class OrderStatus implements Serializable {

    String status; //“1” 成功  “0” 失败
    String verify; //“1” 成功  “0” 失败

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
