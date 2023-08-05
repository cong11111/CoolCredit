package com.tiny.cash.loan.card.net.server;

import okhttp3.OkHttpClient;

public interface INetServer {
    String getBaseUrl();

    OkHttpClient getClient();

    <T> T createApi(Class<T> clazz);

}
