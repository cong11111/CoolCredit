package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {


    /**
     * accountId : 201111130100000432
     * hasAddress : false
     * hasContact : false
     * hasProfile : false
     * mobile : 234888888888
     * token : f373d049aa3584692fb0215fba8d5852
     */

    private String accountId;
    private boolean hasOther;//是否完善其他信息
    private boolean hasContact;//是否完善联系人信息
    private boolean hasProfile;//是否完善个人信息
    private String mobile;
    private String token;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

    public boolean isHasContact() {
        return hasContact;
    }

    public void setHasContact(boolean hasContact) {
        this.hasContact = hasContact;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

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
}
