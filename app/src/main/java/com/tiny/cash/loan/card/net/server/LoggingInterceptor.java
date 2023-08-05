package com.tiny.cash.loan.card.net.server;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
//        LogHelper.writeLog(TAG, String.format("%.1fms%n%s",
//                (t2 - t1) / 1e6d, response.toString()));
        return response;
    }
}
