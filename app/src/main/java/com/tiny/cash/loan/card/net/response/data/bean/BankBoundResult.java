package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class BankBoundResult implements Serializable {
    //cardBound 是否已经绑定，0 未绑定 1绑定 。 cardBoundMessage 绑定的时候有消息，未绑定没有内容。
    String cardBound;
    String cardBoundMessage;

    public String getCardBound() {
        return cardBound;
    }

    public void setCardBound(String cardBound) {
        this.cardBound = cardBound;
    }

    public String getCardBoundMessage() {
        return cardBoundMessage;
    }

    public void setCardBoundMessage(String cardBoundMessage) {
        this.cardBoundMessage = cardBoundMessage;
    }
}
