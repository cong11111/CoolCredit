package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;

public class QueryOrderId implements Serializable {

    /**
     * orderId : 2012010102000004535
     * hasProfile : true
     * hasContact : true
     * hasOther : true
     * bvnChecked : true
     * accountChecked : true
     * cardChecked : true
     */

    private String orderId; //“-1”，没有获取到正确的ID
    private boolean hasProfile;
    private boolean hasContact;
    private boolean hasOther;
    private boolean bvnChecked;
    private boolean accountChecked;
    private boolean cardChecked;

    private boolean hasInfoReviewCard;
    private boolean hasInfoReviewSelfie;
    private boolean infoReviewSwitch;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public boolean isHasContact() {
        return hasContact;
    }

    public void setHasContact(boolean hasContact) {
        this.hasContact = hasContact;
    }

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
    }

    public boolean isAccountChecked() {
        return accountChecked;
    }

    public void setAccountChecked(boolean accountChecked) {
        this.accountChecked = accountChecked;
    }

    public boolean isCardChecked() {
        return cardChecked;
    }

    public boolean isHasInfoReviewCard() {
        return hasInfoReviewCard;
    }

    public void setHasInfoReviewCard(boolean hasInfoReviewCard) {
        this.hasInfoReviewCard = hasInfoReviewCard;
    }

    public boolean isHasInfoReviewSelfie() {
        return hasInfoReviewSelfie;
    }

    public void setHasInfoReviewSelfie(boolean hasInfoReviewSelfie) {
        this.hasInfoReviewSelfie = hasInfoReviewSelfie;
    }

    public boolean isInfoReviewSwitch() {
        return infoReviewSwitch;
    }

    public void setInfoReviewSwitch(boolean infoReviewSwitch) {
        this.infoReviewSwitch = infoReviewSwitch;
    }

    public void setCardChecked(boolean cardChecked) {
        this.cardChecked = cardChecked;
    }
}
