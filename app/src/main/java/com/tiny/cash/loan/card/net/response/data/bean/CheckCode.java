package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class CheckCode implements Serializable {

    boolean verifyed;

    public boolean isVerifyed() {
        return verifyed;
    }

    public void setVerifyed(boolean verifyed) {
        this.verifyed = verifyed;
    }
}
