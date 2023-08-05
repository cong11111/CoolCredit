package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class UserProfile implements Serializable {

    /**
     * hasAddress : false
     * hasContact : false
     * hasProfile : true
     * nationalChecked : true
     * token : 52d7353408e7f416807cec1420111545
     */

    private boolean hasOther;
    private boolean hasContact;
    private boolean hasProfile;
    private boolean bvnChecked;
    private String token;

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
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


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
