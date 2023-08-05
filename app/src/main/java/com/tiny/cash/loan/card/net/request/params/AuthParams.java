package com.tiny.cash.loan.card.net.request.params;

import java.io.Serializable;

public class AuthParams implements Serializable {

    private String sms;
    private String call;
    private String contacts;
    private String gps;
    private String appList;
    private String userIp;
    private String pubIp;
    private String imei;
    private String androidId;
    private String mac;
    private String deviceUniqId;
    private String brand;
    private String innerVersionCode;
    private String isRooted;
    private String isEmulator;
    private String accountId;
    private String orderId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getAppList() {
        return appList;
    }

    public void setAppList(String appList) {
        this.appList = appList;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getPubIp() {
        return pubIp;
    }

    public void setPubIp(String pubIp) {
        this.pubIp = pubIp;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceUniqId() {
        return deviceUniqId;
    }

    public void setDeviceUniqId(String deviceUniqId) {
        this.deviceUniqId = deviceUniqId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getInnerVersionCode() {
        return innerVersionCode;
    }

    public void setInnerVersionCode(String innerVersionCode) {
        this.innerVersionCode = innerVersionCode;
    }

    public String getIsRooted() {
        return isRooted;
    }

    public void setIsRooted(String isRooted) {
        this.isRooted = isRooted;
    }

    public String getIsEmulator() {
        return isEmulator;
    }

    public void setIsEmulator(String isEmulator) {
        this.isEmulator = isEmulator;
    }

}
