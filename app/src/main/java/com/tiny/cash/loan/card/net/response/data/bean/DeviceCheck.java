package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class DeviceCheck implements Serializable {

    String verify;

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
