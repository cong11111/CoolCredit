package com.tiny.cash.loan.card.net.header;

import java.util.HashMap;

public class TokenHeader extends HashMap<String, String> {


    private TokenHeader() {
    }


    public static TokenHeader createHeader(String token) {
        TokenHeader header = new TokenHeader();
        header.put("Authorization", token);
        return header;
    }

    public static TokenHeader createHeader(String contentType,String token) {
        TokenHeader header = new TokenHeader();
        header.put("Content-Type", contentType);

        header.put("Authorization", token);
        return header;
    }
}
