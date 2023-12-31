package com.tiny.cash.loan.card.utils;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tiny.cash.loan.card.Constant;
import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.log.LogSaver;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseUtils {
    /**
     * 注册 fireb_register,
     * 资料1 fireb_data1 ,
     * 资料2 fireb_data2 ,
     * 资料3 fireb_data3 ,
     * 银行账号 fireb_bank,
     * <p>
     * fireb_click_sign 点击登录按钮,
     * fireb_send_sms   请求发送短信,
     * fireb_resend_sms 再次发送短信,
     * fireb_register   注册成功 ,
     * fireb_apply 首次申请,
     * fireb_apply_confirm   首次申请确认成功,
     * fireb_activity   首次放款 ,
     * fireb_overdue    首次逾期 ,
     * fireb_apply_all  申请,
     * fireb_apply_confirm_all    申请确认成功,
     * fireb_activity_all    放款,
     * fireb_overdue_all     逾期,
     * fireb_band_card 进入绑卡 ,
     * fireb_card_submit 提交绑卡 ,
     * fireb_card_success 绑卡成功 ,
     * fireb_upload_auth_duration 上传短信耗时,
     */

    private static final String TAG = "KudiCreditFirebase";

    public static void setUserId(Context context, String userId) {
        FirebaseAnalytics.getInstance(context).setUserId(userId);
        if (!Constant.Companion.isAabBuild()) {
            Toast.makeText(context, "setUserId = " + userId, Toast.LENGTH_SHORT).show();
            LogSaver.logToFile("setUserId = " + userId);
        }
    }

    public static void logEvent(String event) {
        if (KudiCreditApp.Companion.getInstance() == null) {
            return;
        }
        Context context = KudiCreditApp.Companion.getInstance();
        Bundle params = new Bundle();
        if (!Constant.Companion.isAabBuild()) {
            Log.e(TAG, " log event = " + event);
            Toast.makeText(context, "埋点 = " + event, Toast.LENGTH_SHORT).show();
            LogSaver.logToFile("log event = " + event);
        }
        FirebaseAnalytics.getInstance(context).logEvent(event, params);
    }

    public static void logEvent(String event, String paramsKey, String paramsValue, String pKey2, String pValue2) {
        if (KudiCreditApp.Companion.getInstance() == null) {
            return;
        }
        Context context = KudiCreditApp.Companion.getInstance();
        Bundle params = new Bundle();
        params.putString(paramsKey, paramsValue);
        params.putString(pKey2, pValue2);

        if (!Constant.Companion.isAabBuild()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(paramsKey, paramsValue);
                jsonObject.put(pKey2, pValue2);
            } catch (JSONException e) {

            }
            String result =  event + " " + jsonObject.toString();
            Log.e(TAG, " log event = " + result);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Toast.makeText(context, event, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }
        FirebaseAnalytics.getInstance(context).logEvent(event, params);
    }
}
