package com.tiny.cash.loan.card.utils;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiny.cash.loan.card.KudiCreditApp;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.multidex.BuildConfig;

public class FirebaseLogUtils {
    private static FirebaseAnalytics firebaseAnalytics;
    private static SimpleDateFormat format;
    public static void Log(String key){
        if(BuildConfig.DEBUG){
            return;
        }
        Bundle value = new Bundle();
        if(firebaseAnalytics == null){
            firebaseAnalytics = FirebaseAnalytics.getInstance(KudiCreditApp.getInstance());
            firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        }
        if(format == null){
            format = new SimpleDateFormat("yyyy/MM/dd-hh:mm", Locale.getDefault());
        }
        if(value == null){
            value = new Bundle();
        }
        String time = format.format(System.currentTimeMillis());
        value.putString("time",time);
        firebaseAnalytics.logEvent(key.replaceFirst("af","fire"), value);

//        String kochavaSwitch = KvStorage.get(LocalConfig.LC_KOCHAVASWITCH, "0");
//        if (MrConstant.ONE.equals(kochavaSwitch)) {
//            Tracker.sendEvent(key.replaceFirst("af", "kochava"), "");
//        }
//        AppEventsLogger logger = AppEventsLogger.newLogger(App.getInstance());
//        logger.logEvent(key.replaceFirst("af","fb") );

        Map<String,Object> eventValues = new HashMap<>();
        eventValues.put("time",time);
    }

}