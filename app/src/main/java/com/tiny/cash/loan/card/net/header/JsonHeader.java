package com.tiny.cash.loan.card.net.header;

import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import java.util.HashMap;

public class JsonHeader extends HashMap<String, String> {

    protected JsonHeader() {
    }

    public static JsonHeader createHeader() {
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

        String appVersion = KvStorage.get(LocalConfig.LC_APPVERSION, "");
        String channel = KvStorage.get(LocalConfig.LC_CHANNEL, "");
        String utmSource = KvStorage.get(LocalConfig.LC_UTMSOURCE, "");
        String utmMedium = KvStorage.get(LocalConfig.LC_UTMMEDIUM, "");

        JsonHeader header = new JsonHeader();
        header.put("Content-Type", "application/json");
        header.put("platform", platform);
        header.put("token", token);
        header.put("device", device);
        header.put("deviceId", deviceId);
        header.put("imei", imei);
        header.put("brand", brand);
        header.put("os", "Android");
        header.put("isSimulator", isSimulator);
        header.put("lang", lang);
        header.put("longitude", longitude);
        header.put("latitude", latitude);
        header.put("innerVersionCode", AppUtils.getVersionCode(KudiCreditApp.getInstance()));
        header.put("appVersion", appVersion);
        header.put("channel", channel);
        header.put("utmSource", utmSource);
        header.put("utmMedium", utmMedium);
        return header;
    }

}
