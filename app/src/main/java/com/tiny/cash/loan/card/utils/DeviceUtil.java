package com.tiny.cash.loan.card.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * 创建时间: 2016/08/22 16:23 <br>
 * 作者: fujia xiezhe <br>
 * 描述: DeviceUtil只负责获得DeviceId以及和硬件相关内容
 */
public class DeviceUtil {
    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static final String UUID_STATICS = "uuid_statics";
    private static final String DEVICE_ID = "device_id";

    public static final String CONFIG = "config";
    private static Point sScreenSize = new Point(-1, -1);  //初始化默认值
    private static int sStatusBarHeight;

    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_BASE64IMEI = "BASE64_IMEI";
    private static String sImei;
    private static String sAndroidId;
    private static String sMacAddress;
    //读取 IMEI次数（频繁读写造成 ANR）
    private static int readedImeiCount = 0;
    private static int BUILDVERSIONQ = 29;

    /**
     * Don't let anyone instantiate this class.
     */
    private DeviceUtil() {
        throw new IllegalStateException("Do not need instantiate!");
    }

    /**
     * <p>获取设备唯一ID号。</p>
     * <p>策略为：</p>
     * <p>第一优先序列为移动设备的IMEI或者CDMA手机的MEID、ESN</p>
     * <p>第二优先序列为android系统提供的androidId</p>
     * <p>第三优先序列为android系统属性里存储的ro.serialno</p>
     * <p>第四优先序列为自动生成随机数</p>
     *
     * @param context add <uses-permission android:name="READ_PHONE_STATE" />
     */
    private static String sDeviceID = "";
    public static String getDeviceID(@NonNull Context context) {

        if (!TextUtils.isEmpty(sDeviceID) || context == null) {
            Log.d(TAG,"sDeviceID e:"+sDeviceID);
            return sDeviceID;
        }
        try {
            String deviceID = "";
            deviceID = KvStorage.get(LocalConfig.LC_IMEI, deviceID);;

            if (!TextUtils.isEmpty(deviceID)) {
                sDeviceID = deviceID;
                return deviceID;
            }

            deviceID = getDeviceIdByPhoneInfo(context);

            if (TextUtils.isEmpty(deviceID)) {
                deviceID = getDeviceIdByAndroidSystem(context);
            }

            if (TextUtils.isEmpty(deviceID)) {
                deviceID = getDeviceIdBySystemProperties(context);
            }

            if (TextUtils.isEmpty(deviceID)) {
                deviceID = MD5Util.StringInMd5(UUID.randomUUID().toString());
                Log.d(TAG, "MD5Util: "+deviceID);
            }

            // 保存到config文件中
            KvStorage.put(LocalConfig.LC_IMEI, deviceID);
            Log.d(TAG,"getDeviceID e:"+deviceID);
            sDeviceID = deviceID;
        } catch (Throwable e) {
        }
        return sDeviceID;
    }

    /**
     * 针对动态权限获取的情况
     * 重新按照规则获取设备ID，并且存到本地。
     *
     */
    public static String getDeviceIDEx(@NonNull Context context) {

        if (context == null) {
            return "";
        }

        String deviceId = getDeviceIdByPhoneInfo(context);

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getDeviceIdByAndroidSystem(context);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getDeviceIdBySystemProperties(context);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = MD5Util.StringInMd5(UUID.randomUUID().toString());
        }
        KvStorage.put(LocalConfig.LC_IMEI, deviceId);
        sDeviceID = deviceId;

        return deviceId;
    }

    /**
     * 获取移动设备的IMEI或者CDMA手机的MEID、ESN
     *
     * @param context 上下文对象
     * @return 移动设备的IMEI或者CDMA手机的MEID、ESN，或者为空（如果没有{@link Manifest.permission#READ_PHONE_STATE}权限）
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceIdByPhoneInfo(Context context) {
        return getIMEI(context);
    }

    /**
     * 获取android系统提供的androidId
     *
     * @param context 上下文对象
     * @return android系统提供的androidId`
     */
    public static String getDeviceIdByAndroidSystem(Context context) {
        return getAndroidID(context);
    }

    /**
     * 获取android系统属性里存储的ro.serialno
     *
     * @return android系统属性里存储的ro.serialno
     */
    private static String sDeviceIdBySystemPropertiesSerialno = "";
    public static String getDeviceIdBySystemProperties() {
        if (!TextUtils.isEmpty(sDeviceIdBySystemPropertiesSerialno)) {
            return sDeviceIdBySystemPropertiesSerialno;
        }
        String deviceId = null;
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", new Class[] { String.class, String.class });
            deviceId = (String) get.invoke(c, new Object[] { "ro.serialno", "" });
        } catch (Exception t) {
            //ignore
        }
        sDeviceIdBySystemPropertiesSerialno = deviceId;
        return deviceId;
    }

    private static String sDeviceIdBySystemProperties = "";
    @SuppressLint("HardwareIds")
    public static String getDeviceIdBySystemProperties(@NonNull Context context) {
        if (!TextUtils.isEmpty(sDeviceIdBySystemProperties)) {
            return sDeviceIdBySystemProperties;
        }
        String deviceId = null;
        try {
            deviceId = getDeviceIdBySystemProperties();
            if (TextUtils.isEmpty(deviceId)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                                == PackageManager.PERMISSION_GRANTED) {
                    deviceId = "";//Build.getSerial();
                } else {
                    deviceId = Build.SERIAL;
                }
            }
        } catch (Exception t) {
            //ignore
        }
        if(Build.UNKNOWN.equals(deviceId)){
            deviceId = "";
        }
        if (!TextUtils.isEmpty(deviceId)) {
            sDeviceIdBySystemProperties = deviceId;
        }
        Log.d(TAG, "getDeviceIdBySystemProperties: "+deviceId);
        return deviceId;
    }

    /**
     * To determine whether it contains a gyroscope
     *
     * @return boolean
     */
    public static boolean hasGravity(@NonNull Context context) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (manager == null) {
            return false;
        }
        return true;
    }


    /**
     * 获取屏幕高度x宽度
     */
    public static String getScreenSize(@NonNull Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            return dm2.widthPixels + "x" + dm2.heightPixels;
        } else {
            return dm2.heightPixels + "x" + dm2.widthPixels;
        }
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     */
    public static Point getScreenMetrics(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

    /**
     * 获取屏幕长宽比
     */
    public static float getScreenRate(@NonNull Context context) {
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H / W);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(@NonNull Context context) {

        if (sScreenSize.x <= 0 || sScreenSize.y <= 0) {
            WindowManager wm =
                    (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            //getSize方法里面直接对Point的x,y赋值,没有检测和创建Point。如果Point等于空,会有NPE。
            sScreenSize = new Point();
            display.getSize(sScreenSize);
        }

        return sScreenSize.x;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(@NonNull Context context) {

        if (sScreenSize.x <= 0 || sScreenSize.y <= 0) {
            WindowManager wm =
                    (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            //getSize方法里面直接对Point的x,y赋值,没有检测和创建Point。如果Point等于空,会有NPE。
            sScreenSize = new Point();
            display.getSize(sScreenSize);
        }

        return sScreenSize.y;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(@NonNull Context context) {

        if (sStatusBarHeight == 0) {
            int resourceId = context.getApplicationContext()
                    .getResources()
                    .getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight =
                        context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }

    //每次安装生成新的,生命周期对应一次安装
    //注意只在统计的时候使用
    public static String getUUID(@NonNull Context context) {
        String uuid = readUUID(context);
        if (TextUtils.isEmpty(uuid)) {
            uuid = generateUUID(context);
        }
        return uuid;
    }

    private static String readUUID(@NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        String uuid = sp.getString(UUID_STATICS, "").trim();
        return uuid;
    }

    private static synchronized String generateUUID(@NonNull Context context) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        String uuid = sp.getString(UUID_STATICS, "").trim();
        if (!TextUtils.isEmpty(uuid)) {
            return uuid;
        }
        Editor editor = sp.edit();
        uuid = UUID.randomUUID().toString();
        String uuidString = uuid.toString();
        // 保存到config文件中
        editor.putString(UUID_STATICS, uuidString);
        editor.commit();
        return uuidString;
    }



    /**
     * 获取IMEI
     *
     * @param context Context
     * @return IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(sImei)) {
            return sImei;
        }
        if(readedImeiCount >= 3 && Build.VERSION.SDK_INT >= BUILDVERSIONQ){
            return sImei;
        }
        readedImeiCount++;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String imei ;
        imei = sp.getString(KEY_BASE64IMEI, "");
        String packageName = "";
        if(context != null){
            packageName =   context.getPackageName();
        }
        //兼容几个版本后下掉
        if(!TextUtils.isEmpty(packageName)){
            if(!TextUtils.isEmpty(imei)){
                imei = new String(Base64.decode(imei, Base64.DEFAULT));
            }else{
                imei = sp.getString(KEY_IMEI, "");
            }
            //为空的时候就没必要在存储了
            if(!TextUtils.isEmpty(imei)){
                sImei = imei;
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY_BASE64IMEI, new String(Base64.encode(imei.trim().getBytes(), Base64.DEFAULT)));
                editor.apply();
                return imei;
            }
        }else {
            if (!TextUtils.isEmpty(imei)) {
                imei = new String(Base64.decode(imei, Base64.DEFAULT));
                sImei = imei;
                return imei;
            }
        }
        try {
            if (!PermissionUtil.IsPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                return null;
            }
            TelephonyManager tm =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                imei = tm.getDeviceId();
            }
            if (TextUtils.isEmpty(imei)) {
                return null;
            }
            //判断deviceId是否全为0
            String backId = imei;
            backId = backId.replace("0", "");
            if (TextUtils.isEmpty(backId)) {
                imei = "";
                return imei;
            }
        } catch (Exception e) {
            Log.w(TAG, "getIMEI e:" + e.toString());
            return null;
        }
        //为空的时候就没必要在存储了
        if(!TextUtils.isEmpty(imei)){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY_BASE64IMEI, new String(Base64.encode(imei.trim().getBytes(), Base64.DEFAULT)));
            editor.apply();
        }
        sImei = imei;
        return imei;
    }

    /**
     * 获取android系统提供的androidId
     *
     * @param context 上下文对象
     * @return android系统提供的androidId
     */
    public static String getAndroidID(Context context) {
        if (!TextUtils.isEmpty(sAndroidId)) {
            return sAndroidId;
        }
        String androidId = "";
        ContentResolver resolver = context.getContentResolver();
        if (resolver != null) {
            try {
                androidId =
                        android.provider.Settings.System.getString(resolver, Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                //ignore
            }
        }
        Log.d(TAG, "getAndroidID2: "+androidId);
        sAndroidId = androidId;
        return androidId;
    }

    /**
     * 获取手机的MAC地址
     *
     * @return mac address
     */
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";

    public static String getMacAddress(Context context) {

        if (!TextUtils.isEmpty(sMacAddress)) {
            return sMacAddress;
        }

        try {
            if (!PermissionUtil.IsPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                return "";
            }
            WifiManager wifiMan =
                    (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMan.getConnectionInfo();

            String macAddress = "";//wifiInfo.getMacAddress();
            if (wifiInfo != null && marshmallowMacAddress.equals(macAddress)) {
                String result = null;
                try {
                    result = getMacAddressByInterface(context);
                    if (result != null) {
                        sMacAddress = result;
                        return result;
                    }
                } catch (Throwable e) {
                    //ignore
                }
            } else {
                if (wifiInfo != null && macAddress != null) {
                    sMacAddress = macAddress;
                    return sMacAddress;
                } else {
                    return "";
                }
            }
            sMacAddress = marshmallowMacAddress;
            return marshmallowMacAddress;
        } catch (Exception e) {
            //ignore
        }
        return "";
    }

    private static String getMacAddressByInterface(Context context) {
        if (context == null) {
            return "";
        }

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            //ignore
        }
        return null;
    }
}
