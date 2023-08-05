package com.tiny.cash.loan.card.net.request.params;


import java.io.Serializable;

public class LoginParams implements Serializable {

    String tel;
    String pwd;
    String slide;
    public static LoginParams createParams(String tel, String pwd,String slide) {
        LoginParams params = new LoginParams();
        params.tel = tel;
        params.pwd = pwd;
        params.slide = slide;
        return params;
    }
}
