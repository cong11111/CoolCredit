package com.tiny.cash.loan.card.net.response;

import java.io.Serializable;

public class ResponseStatus implements Serializable {
    /**
     * code : 200
     * msg : OK
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}