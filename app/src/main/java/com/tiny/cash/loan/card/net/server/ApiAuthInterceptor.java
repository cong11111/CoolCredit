 package com.tiny.cash.loan.card.net.server;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiAuthInterceptor implements Interceptor {
    public ApiAuthInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String verCode = null;
        /**
         *应用平台    platform
         *用户token    token
         *设备型号    device
         *设备唯一标识    deviceId
         *语言    imei
         *手机品牌    brand
         *操作系统    os
         *是否为模拟器    isSimulator
         *语言    lang
         *经度    longitude
         *纬度    latitude
         *内部版本号    innerVersionCode
         *APP版本号    appVersion
         *安装包发布的渠道    channel
         *客户来源    utmSource
         *媒介    utmMedium
         */
        String platform = KvStorage.get(LocalConfig.LC_PLATFORM, "");
        String token = KvStorage.get(LocalConfig.LC_TOKEN, "");
        String device = KvStorage.get(LocalConfig.LC_DEVICE, "");
        String deviceId = KvStorage.get(LocalConfig.LC_DEVICEID, "");
        String imei = KvStorage.get(LocalConfig.LC_IMEI, "");
        String brand = KvStorage.get(LocalConfig.LC_BRAND, "");

        String isSimulator = KvStorage.get(LocalConfig.LC_ISSIMULATOR, "0");
        String lang = KvStorage.get(LocalConfig.LC_LANG, "");
        String longitude = KvStorage.get(LocalConfig.LC_LONGITUDE, "");
        String latitude = KvStorage.get(LocalConfig.LC_LATITUDE, "");

        String appVersion =null;
        String channel = KvStorage.get(LocalConfig.LC_CHANNEL, "");
        String utmSource = KvStorage.get(LocalConfig.LC_UTMSOURCE, "");
        String utmMedium = KvStorage.get(LocalConfig.LC_UTMMEDIUM, "");
        try {
            PackageInfo pInfo = KudiCreditApp.getInstance().getPackageManager().getPackageInfo(KudiCreditApp.getInstance().getPackageName(), 0);
             appVersion = pInfo.versionName;   //version name
             verCode = String.valueOf(pInfo.versionCode);      //version code
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    
        Request newRequest = request.newBuilder()
                .addHeader("platform", platform)
                .addHeader("token", token)
                .addHeader("device", device)
                .addHeader("deviceId", deviceId)
                .addHeader("imei", imei)
                .addHeader("brand", brand)
                .addHeader("os", "Android")
                .addHeader("isSimulator", isSimulator)
                .addHeader("lang", lang)
                .addHeader("longitude", longitude)
                .addHeader("latitude", latitude)
                .addHeader("innerVersionCode",verCode)
                .addHeader("appVersion", appVersion)
                .addHeader("channel", channel)
                .addHeader("utmSource", utmSource)
                .addHeader("utmMedium", utmMedium)
                .build();
        return chain.proceed(newRequest);
    }
}
