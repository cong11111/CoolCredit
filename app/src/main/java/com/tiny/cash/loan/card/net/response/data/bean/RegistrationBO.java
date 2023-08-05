package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class RegistrationBO implements Serializable {


    /**
     * mobile : 254712328097
     * token : db1746cbb75ab9076cae2b7efbc2c3e4
     * userId : 452838440405303296
     */


    private String mobile;
    private String token;
    private String accountId;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
