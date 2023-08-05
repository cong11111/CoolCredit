package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class CheckMobile implements Serializable {

    boolean hasRegisted;

    public boolean isHasRegisted() {
        return hasRegisted;
    }

    public void setHasRegisted(boolean hasRegisted) {
        this.hasRegisted = hasRegisted;
    }
}
