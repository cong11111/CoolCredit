package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class AuthResult implements Serializable {

    /**
     * hasUpload : true
     */

    private boolean hasUpload;
    private String failReason;

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public boolean isHasUpload() {
        return hasUpload;
    }

    public void setHasUpload(boolean hasUpload) {
        this.hasUpload = hasUpload;
    }
}
