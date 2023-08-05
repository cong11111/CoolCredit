package com.tiny.cash.loan.card.net.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public final class StringConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}

