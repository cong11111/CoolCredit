package com.tiny.cash.loan.card.utils;


import com.blankj.utilcode.util.SPUtils;

public class KvStorage {
//    private static MMKV kv;
//
//    static {
//        if (Build.VERSION.SDK_INT == 19) {
//            //一些 Android设备（API level 19）在安装/更新APK 时可能出错, 导致 libmmkv.so 找不到。
//            String dir = KudiCreditApp.getInstance().getFilesDir().getAbsolutePath() + "/mmkv";
//            MMKV.initialize(dir, new MMKV.LibLoader() {
//                @Override
//                public void loadLibrary(String libName) {
//                    //开源库[ReLinker](https://github.com/KeepSafe/ReLinker) 专门解决这个问题
//                    ReLinker.loadLibrary(KudiCreditApp.getInstance(), libName);
//                }
//            });
//        } else {
//            //👇初始化代码，数据默认存储在：
//            //context.getFilesDir().getAbsolutePath() + "/mmkv"
//            MMKV.initialize(KudiCreditApp.getInstance());
//        }
////        MMKV.initialize(App.getInstance());
//        kv = MMKV.defaultMMKV();
//    }

    public static String get(String key, String defaultVal) {
        return SPUtils.getInstance().getString(key, defaultVal);
    }

    public static Boolean get(String key, Boolean defaultVal) {
        return SPUtils.getInstance().getBoolean(key, defaultVal);
    }

    public static Float get(String key, Float defaultVal) {
        return SPUtils.getInstance().getFloat(key, defaultVal);
    }

    public static Integer get(String key, Integer defaultVal) {
        return SPUtils.getInstance().getInt(key, defaultVal);
    }

    public static Long get(String key, Long defaultVal) {
        return SPUtils.getInstance().getLong(key, defaultVal);
    }

    public static void put(String key, Boolean val) {
        SPUtils.getInstance().put(key, val);
    }

    public static void put(String key, Float val) {
        SPUtils.getInstance().put(key, val);
    }

    public static void put(String key, Integer val) {
        SPUtils.getInstance().put(key, val);
    }

    public static void put(String key, Long val) {
        SPUtils.getInstance().put(key, val);
    }

    public static void put(String key, String val) {
        SPUtils.getInstance().put(key, val);
    }

}
