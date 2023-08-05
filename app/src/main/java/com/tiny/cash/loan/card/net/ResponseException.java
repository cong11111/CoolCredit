package com.tiny.cash.loan.card.net;


import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * @author rainking
 */
public class ResponseException extends Exception {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    ResponseException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ResponseException(String msg) {
        super(msg);
        this.msg = msg;
    }


    public ResponseException(Throwable cause, int code, String msg) {
        super(cause);
        this.code = code;
        if (TextUtils.isEmpty(msg)) {
            this.msg = cause.getMessage();
        } else {
            this.msg = msg;
        }

    }

    public ResponseException(Throwable cause, String msg) {
        super(cause);
        if (TextUtils.isEmpty(msg)) {
            this.msg = cause.getMessage();
        } else {
            this.msg = msg;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @NonNull
    @Override
    public String toString() {
        return "NetException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }


}
