package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

/**
 * *作者: jyw
 * 日期：2020/10/14 9:43
 */
public class UserContact implements Serializable {
    /**
     * hasOther : false
     * hasProfile : true
     * hasReferee : true
     * token : 88245abed801a54e69d8e0239de05688
     */

    private boolean hasOther;
    private boolean hasProfile;
    private String token;
    private boolean hasContact;

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isHasContact() {
        return hasContact;
    }

    public void setHasContact(boolean hasContact) {
        this.hasContact = hasContact;
    }
}
