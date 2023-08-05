package com.tiny.cash.loan.card.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tiny.cash.loan.card.KudiCreditApp;

import java.util.HashSet;
import java.util.Set;


public final class PreferenceUtils {

    private static Context getContext() {
        return KudiCreditApp.getInstance();
    }

    private static String KEY_LAST_CLOUD_ACCOUNT = "last_user_account";
    private static String KEY_LAST_CLOUD_UID = "last_user_uid";

    public static void reset(final Context context) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.clear();
        edit.apply();
    }

    public static void setLastUserAccount(String account) {
        commitString(KEY_LAST_CLOUD_ACCOUNT, account);
    }

    public static String getLastUserAccount() {
        return getString(KEY_LAST_CLOUD_ACCOUNT, "");
    }

    public static void setLastUserId(String account) {
        commitString(KEY_LAST_CLOUD_UID, account);
    }

    public static String getLastUserId() {
        return getString(KEY_LAST_CLOUD_UID, "");
    }


    public static String getString(String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat(key, defValue);
    }

    public static void put(String key, String value) {
        putString(key, value);
    }

    public static void put(String key, int value) {
        putInt(key, value);
    }

    public static void put(String key, float value) {
        putFloat(key, value);
    }

    public static void put(String key, boolean value) {
        putBoolean(key, value);
    }

    private static void putFloat(String key, float value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    public static int getInt(String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key, defValue);
    }


    public static boolean hasString(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.contains(key);
    }

    private static void putString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void commitString(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static void commitBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static void commitInt(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static void commitLong(String key, long value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void remove(String... keys) {
        if (keys != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                    getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (String key : keys) {
                editor.remove(key);
            }
            editor.apply();
        }
    }


    public static Boolean getBooleanPrefs(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, false);
    }

    public static Boolean getBooleanPrefs(Context ctx, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(key, defaultValue);
    }

    public static Boolean getBooleanPrefs(String key) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getBoolean(key, false);
    }

    public static Boolean getBooleanPrefs(String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getBoolean(key, defaultValue);
    }

    public static String getStringPrefs(String key) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getString(key, "");
    }

    public static String getStringPrefs(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, "");
    }

    public static String getStringPrefs(Context ctx, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(key, defaultValue);
    }

    public static int getIntegerPrefs(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, 0);
    }

    public static int getIntegerPrefs(String key) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getInt(key, 0);
    }

    public static int getIntegerPrefs(Context ctx, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getInt(key, defaultValue);
    }

    public static long getLongPrefs(Context ctx, String key) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, 0);
    }

    public static long getLongPrefs(Context ctx, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(ctx).getLong(key, defaultValue);
    }

    public static Long getLongPrefs(String key) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getLong(key, 0);
    }

    public static long getLongPrefs(String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getLong(key, defaultValue);
    }

    public static Set<String> getStrinSetPrefs(String key) {
        return PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()).getStringSet(key, new HashSet<String>());
    }

    public static void set(final String key, final Object value) {
        set(PreferenceManager.getDefaultSharedPreferences(KudiCreditApp.getInstance().getBaseContext()), key, value);
    }

    public static void set(final Context context, final String key, final Object value) {
        set(PreferenceManager.getDefaultSharedPreferences(context), key, value);
    }

    public static void set(final SharedPreferences preferences, final String key, final Object value) {
        SharedPreferences.Editor sharedPreferenceEditor = preferences.edit();
        if (value instanceof String) {
            sharedPreferenceEditor.putString(key, (String) value);
        } else if (value instanceof Long) {
            sharedPreferenceEditor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            sharedPreferenceEditor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            sharedPreferenceEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Set) {
            sharedPreferenceEditor.putStringSet(key, (Set<String>) value);
        }
        sharedPreferenceEditor.apply();
    }

    public static void clear(final Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }
}
