package com.tiny.cash.loan.card.net.request.params;


import java.io.Serializable;

public class ApplyParams implements Serializable {

    public String accountId;
    public String orderId;
    public String prodId;
    public String prodName;
    public String amount;
    public String period;

    public static ApplyParams createParams(String accountId, String orderId, String prodId,
                                           String prodName, String amount, String period) {
        ApplyParams params = new ApplyParams();
        params.accountId = accountId;
        params.orderId = orderId;
        params.prodId = prodId;
        params.prodName = prodName;
        params.amount = amount;
        params.period = period;
        return params;
    }
}
