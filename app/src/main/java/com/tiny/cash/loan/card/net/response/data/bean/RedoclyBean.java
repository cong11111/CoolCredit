package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class RedoclyBean implements Serializable {
    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    String pageURL;
}
