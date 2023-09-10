package com.tiny.cash.loan.card.net.server;


import com.tiny.cash.loan.card.kudicredit.BuildConfig;
import com.tiny.cash.loan.card.net.converter.TextConverterFactory;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseApiServer {
    private Retrofit mRetrofit;

    protected BaseApiServer() {
        this(true);
    }


    protected BaseApiServer(boolean useRxJava) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(TextConverterFactory.newInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient());
        if (useRxJava) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        mRetrofit = builder.build();
    }

    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public abstract String getBaseUrl();

    public abstract Interceptor getInterceptor();

    public OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
//                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//                Boolean result = hv.verify("*.icredit.ng", session);
//                return result;
                return true;
            }
        };

        SSLSocketFactory sslSocketFactory = null;
        X509TrustManager x509TrustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                if(chain == null) {
                    throw new IllegalArgumentException("check Server X509Certificate is null");
                }

                if (chain.length < 0) {
                    throw new IllegalArgumentException("check Server X509Certificate is empty");
                }

                for (X509Certificate cert :chain) {
                    cert.checkValidity();
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
        try {

            // Create a trust manager that does not validate certificate chains

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    x509TrustManager
            };
//            SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
//            if (sslContext != null) {
//                sslSocketFactory = sslContext.getSocketFactory();
//            }
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }

        return new OkHttpClient.Builder()
                .addInterceptor(getInterceptor())
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .retryOnConnectionFailure(true)
                .hostnameVerifier(hostnameVerifier)
                .build();

    }
}
