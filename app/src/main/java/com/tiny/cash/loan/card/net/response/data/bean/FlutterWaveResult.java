package com.tiny.cash.loan.card.net.response.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FlutterWaveResult implements Serializable {

    private String status;
    private String message;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String txRef;
        private String orderRef;
        private String flwRef;
        private String redirectUrl;
        private String device_fingerprint;
        private Object settlement_token;
        private String cycle;
        private int amount;
        private int charged_amount;
        private double appfee;
        private int merchantfee;
        private int merchantbearsfee;
        private String chargeResponseCode;
        private String raveRef;
        private String chargeResponseMessage;
        private String authModelUsed;
        private String currency;
        @SerializedName("IP")
        private String iP;
        private String narration;
        private String status;
        private String modalauditid;
        private String vbvrespmessage;
        private String authurl;
        private String vbvrespcode;
        private Object acctvalrespmsg;
        private String acctvalrespcode;
        private String paymentType;
        private Object paymentPlan;
        private Object paymentPage;
        private String paymentId;
        private String fraud_status;
        private String charge_type;
        private int is_live;
        private String createdAt;
        private String updatedAt;
        private Object deletedAt;
        private int customerId;
        @SerializedName("AccountId")
        private int accountId;
        @SerializedName("customer.id")
        private int _$CustomerId243;// FIXME check this code
        @SerializedName("customer.phone")
        private Object _$CustomerPhone131;// FIXME check this code
        @SerializedName("customer.fullName")
        private String _$CustomerFullName200;// FIXME check this code
        @SerializedName("customer.customertoken")
        private Object _$CustomerCustomertoken267;// FIXME check this code
        @SerializedName("customer.email")
        private String _$CustomerEmail308;// FIXME check this code
        @SerializedName("customer.createdAt")
        private String _$CustomerCreatedAt306;// FIXME check this code
        @SerializedName("customer.updatedAt")
        private String _$CustomerUpdatedAt242;// FIXME check this code
        @SerializedName("customer.deletedAt")
        private Object _$CustomerDeletedAt274;// FIXME check this code
        @SerializedName("customer.AccountId")
        private int _$CustomerAccountId242;// FIXME check this code
        private List<?> meta;
        private FlwMetaBean flwMeta;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTxRef() {
            return txRef;
        }

        public void setTxRef(String txRef) {
            this.txRef = txRef;
        }

        public String getOrderRef() {
            return orderRef;
        }

        public void setOrderRef(String orderRef) {
            this.orderRef = orderRef;
        }

        public String getFlwRef() {
            return flwRef;
        }

        public void setFlwRef(String flwRef) {
            this.flwRef = flwRef;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getDevice_fingerprint() {
            return device_fingerprint;
        }

        public void setDevice_fingerprint(String device_fingerprint) {
            this.device_fingerprint = device_fingerprint;
        }

        public Object getSettlement_token() {
            return settlement_token;
        }

        public void setSettlement_token(Object settlement_token) {
            this.settlement_token = settlement_token;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getCharged_amount() {
            return charged_amount;
        }

        public void setCharged_amount(int charged_amount) {
            this.charged_amount = charged_amount;
        }

        public double getAppfee() {
            return appfee;
        }

        public void setAppfee(double appfee) {
            this.appfee = appfee;
        }

        public int getMerchantfee() {
            return merchantfee;
        }

        public void setMerchantfee(int merchantfee) {
            this.merchantfee = merchantfee;
        }

        public int getMerchantbearsfee() {
            return merchantbearsfee;
        }

        public void setMerchantbearsfee(int merchantbearsfee) {
            this.merchantbearsfee = merchantbearsfee;
        }

        public String getChargeResponseCode() {
            return chargeResponseCode;
        }

        public void setChargeResponseCode(String chargeResponseCode) {
            this.chargeResponseCode = chargeResponseCode;
        }

        public String getRaveRef() {
            return raveRef;
        }

        public void setRaveRef(String raveRef) {
            this.raveRef = raveRef;
        }

        public String getChargeResponseMessage() {
            return chargeResponseMessage;
        }

        public void setChargeResponseMessage(String chargeResponseMessage) {
            this.chargeResponseMessage = chargeResponseMessage;
        }

        public String getAuthModelUsed() {
            return authModelUsed;
        }

        public void setAuthModelUsed(String authModelUsed) {
            this.authModelUsed = authModelUsed;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getIP() {
            return iP;
        }

        public void setIP(String iP) {
            this.iP = iP;
        }

        public String getNarration() {
            return narration;
        }

        public void setNarration(String narration) {
            this.narration = narration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getModalauditid() {
            return modalauditid;
        }

        public void setModalauditid(String modalauditid) {
            this.modalauditid = modalauditid;
        }

        public String getVbvrespmessage() {
            return vbvrespmessage;
        }

        public void setVbvrespmessage(String vbvrespmessage) {
            this.vbvrespmessage = vbvrespmessage;
        }

        public String getAuthurl() {
            return authurl;
        }

        public void setAuthurl(String authurl) {
            this.authurl = authurl;
        }

        public String getVbvrespcode() {
            return vbvrespcode;
        }

        public void setVbvrespcode(String vbvrespcode) {
            this.vbvrespcode = vbvrespcode;
        }

        public Object getAcctvalrespmsg() {
            return acctvalrespmsg;
        }

        public void setAcctvalrespmsg(Object acctvalrespmsg) {
            this.acctvalrespmsg = acctvalrespmsg;
        }

        public String getAcctvalrespcode() {
            return acctvalrespcode;
        }

        public void setAcctvalrespcode(String acctvalrespcode) {
            this.acctvalrespcode = acctvalrespcode;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public Object getPaymentPlan() {
            return paymentPlan;
        }

        public void setPaymentPlan(Object paymentPlan) {
            this.paymentPlan = paymentPlan;
        }

        public Object getPaymentPage() {
            return paymentPage;
        }

        public void setPaymentPage(Object paymentPage) {
            this.paymentPage = paymentPage;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getFraud_status() {
            return fraud_status;
        }

        public void setFraud_status(String fraud_status) {
            this.fraud_status = fraud_status;
        }

        public String getCharge_type() {
            return charge_type;
        }

        public void setCharge_type(String charge_type) {
            this.charge_type = charge_type;
        }

        public int getIs_live() {
            return is_live;
        }

        public void setIs_live(int is_live) {
            this.is_live = is_live;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public int get_$CustomerId243() {
            return _$CustomerId243;
        }

        public void set_$CustomerId243(int _$CustomerId243) {
            this._$CustomerId243 = _$CustomerId243;
        }

        public Object get_$CustomerPhone131() {
            return _$CustomerPhone131;
        }

        public void set_$CustomerPhone131(Object _$CustomerPhone131) {
            this._$CustomerPhone131 = _$CustomerPhone131;
        }

        public String get_$CustomerFullName200() {
            return _$CustomerFullName200;
        }

        public void set_$CustomerFullName200(String _$CustomerFullName200) {
            this._$CustomerFullName200 = _$CustomerFullName200;
        }

        public Object get_$CustomerCustomertoken267() {
            return _$CustomerCustomertoken267;
        }

        public void set_$CustomerCustomertoken267(Object _$CustomerCustomertoken267) {
            this._$CustomerCustomertoken267 = _$CustomerCustomertoken267;
        }

        public String get_$CustomerEmail308() {
            return _$CustomerEmail308;
        }

        public void set_$CustomerEmail308(String _$CustomerEmail308) {
            this._$CustomerEmail308 = _$CustomerEmail308;
        }

        public String get_$CustomerCreatedAt306() {
            return _$CustomerCreatedAt306;
        }

        public void set_$CustomerCreatedAt306(String _$CustomerCreatedAt306) {
            this._$CustomerCreatedAt306 = _$CustomerCreatedAt306;
        }

        public String get_$CustomerUpdatedAt242() {
            return _$CustomerUpdatedAt242;
        }

        public void set_$CustomerUpdatedAt242(String _$CustomerUpdatedAt242) {
            this._$CustomerUpdatedAt242 = _$CustomerUpdatedAt242;
        }

        public Object get_$CustomerDeletedAt274() {
            return _$CustomerDeletedAt274;
        }

        public void set_$CustomerDeletedAt274(Object _$CustomerDeletedAt274) {
            this._$CustomerDeletedAt274 = _$CustomerDeletedAt274;
        }

        public int get_$CustomerAccountId242() {
            return _$CustomerAccountId242;
        }

        public void set_$CustomerAccountId242(int _$CustomerAccountId242) {
            this._$CustomerAccountId242 = _$CustomerAccountId242;
        }

        public List<?> getMeta() {
            return meta;
        }

        public void setMeta(List<?> meta) {
            this.meta = meta;
        }

        public FlwMetaBean getFlwMeta() {
            return flwMeta;
        }

        public void setFlwMeta(FlwMetaBean flwMeta) {
            this.flwMeta = flwMeta;
        }

        public static class FlwMetaBean {
        }
    }
}
