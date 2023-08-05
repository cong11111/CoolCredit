package com.tiny.cash.loan.card.net.request.params;

import java.io.Serializable;

public class SetPwdParams implements Serializable {

    String uniqueId;
    String pwd;
    String affirm;
    public static SetPwdParams createParams(String uniqueId, String pwd, String affirm) {
        SetPwdParams params = new SetPwdParams();
        params.uniqueId = uniqueId;
        params.pwd = pwd;
        params.affirm = affirm;
        return params;
    }
}
