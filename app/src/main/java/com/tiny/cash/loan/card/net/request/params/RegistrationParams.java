package com.tiny.cash.loan.card.net.request.params;

import java.io.Serializable;

public class RegistrationParams implements Serializable {

    String tel;
    String code;
    String appKey;
    String unionid;
    public static RegistrationParams createParams(String tel, String code, String appKey,String unionid) {
        RegistrationParams params = new RegistrationParams();
        params.tel = tel;
        params.code = code;
        params.appKey = appKey;
        params.unionid = unionid;
        return params;
    }
}
