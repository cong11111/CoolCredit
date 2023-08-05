package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;

public class SendCodeBO implements Serializable {

    public String getDndPass() {
        return dndPass;
    }

    public void setDndPass(String dndPass) {
        this.dndPass = dndPass;
    }

    String dndPass;
}
