package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;

public class MessageBean implements Serializable {
    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    private String unreadCount;
}
