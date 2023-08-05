package com.tiny.cash.loan.card.net.response;


import com.tiny.cash.loan.card.net.server.ApiServerImpl;

import java.io.Serializable;

public class BaseResponse implements Serializable {


    /**
     * head : {"code":"200","msg":"OK"}
     */

    private ResponseStatus head;

    public ResponseStatus getStatus() {
        return head;
    }

    public void setHead(ResponseStatus head) {
        this.head = head;
    }


    public boolean isSuccess() {
        return getResponseCode() == ApiServerImpl.OK;
    }

    public int getResponseCode() {
        return head.getCode();
    }
}
