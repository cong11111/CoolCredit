package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;

public class PayStackBean implements Serializable {
    public String getAuthorizationURL() {
        return authorizationURL;
    }

    public void setAuthorizationURL(String authorizationURL) {
        this.authorizationURL = authorizationURL;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    private String authorizationURL;
    private String accessCode;
    private String reference;
}
