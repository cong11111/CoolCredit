package com.tiny.cash.loan.card.net.response;


import java.io.Serializable;

public class Response<T> extends BaseResponse implements Serializable {

    T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
