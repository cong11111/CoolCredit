package com.tiny.cash.loan.card.utils;

import android.content.Context;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import java.util.HashMap;
import java.util.Map;

public class GoogleReferrerHelper {
    private static GoogleReferrerHelper instance = null;
    InstallReferrerClient referrerClient;
    static Context mContext;

    public static GoogleReferrerHelper getIns(Context context) {
        if (instance == null) {
            instance = new GoogleReferrerHelper();
        }
        mContext = context;
        return instance;
    }


    public void start() {
        referrerClient = InstallReferrerClient.newBuilder(mContext).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established.
                        ReferrerDetails response = null;
                        try {
                            if (referrerClient != null) {
                                response = referrerClient.getInstallReferrer();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (response!=null) {
                            String referrerUrl = response.getInstallReferrer();
                            KvStorage.put(LocalConfig.LC_UTMSOURCE,referrerUrl);
                        }
//                        long referrerClickTime = response.getReferrerClickTimestampSeconds();
//                        long appInstallTime = response.getInstallBeginTimestampSeconds();
//                        boolean instantExperienceLaunched = response.getGooglePlayInstantParam();
//                        getSplitData(referrerUrl);
//                        KvStorage.put(LocalConfig.LC_UTMMEDIUM, map.get("utm_medium"));
                        referrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                start();
            }
        });
    }

    /**
     * 把格式：utm_source=testSource&utm_medium=testMedium&utm_term=testTerm&utm_content=11
     * 这种格式的数据切割成key,value的形式并put进JSONObject对象，用于上传
     *
     * @param referer
     * @return
     */
    private void getSplitData(String referer) {
        Map<String, String> map = new HashMap<>();
        if (referer.length() > 2 && referer.contains("&")) {
            for (String data : referer.split("&")) {
                if (data.length() > 2 && data.contains("=")) {
                    String[] split = data.split("=");
                    for (int i = 0; i < split.length; i++) {
                        map.put(split[0], split[1]);
                    }
                }
            }
        }
        KvStorage.put(LocalConfig.LC_UTMSOURCE, map.get("utm_source"));
        KvStorage.put(LocalConfig.LC_UTMMEDIUM, map.get("utm_medium"));
        //facebook 安装完成
        FirebaseLogUtils.Log("new_installComplete");
    }
}
