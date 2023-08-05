package com.tiny.cash.loan.card.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.UUID;

import co.paystack.android.utils.StringUtils;

import static com.tiny.cash.loan.card.utils.LocalConfig.LC_DEVICEID;

import com.tiny.cash.loan.card.kudicredit.BuildConfig;
import com.tiny.cash.loan.card.Constants;


@SuppressLint("DefaultLocale")
@SuppressWarnings("deprecation")
public class DeviceInfo {
    private static DeviceInfo mInstance = null;
    private static Context mContext = null;

    private DeviceInfo(Context context) {
        this.mContext = context;
    }

    public static DeviceInfo getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new DeviceInfo(context);
        }
        return mInstance;
    }

    public void init() {
        getAndroidId();
        getDeviceId();
        DeviceUtil.getDeviceID(mContext);
        getSystemLanguage();
        getSystemModel();
        getDeviceBrand();
        getOsVersion();
        getDeviceBrand();
        siSimulator();
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        String lang = Locale.getDefault().getLanguage();
        KvStorage.put(LocalConfig.LC_LANG, lang);
        return lang;
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.DISPLAY;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        KvStorage.put(LocalConfig.LC_DEVICE, Build.MODEL);
        return Build.MODEL;
    }

    /**
     * 获取设备名称
     *
     * @return 设备名称
     */
    public static String getDeviceName() {
        return Build.DEVICE;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        KvStorage.put(LocalConfig.LC_BRAND, Build.BRAND);
        return Build.BRAND;
    }


    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 9.0 +
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) { // 8.0 +
                serial = Build.SERIAL;
            } else { // 8.0 -
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取安卓系统版本
     *
     * @return
     */
    public static String getOsVersion() {
        String version = Build.VERSION.RELEASE;
        if (!version.startsWith("android")) {
            version = "android" + Build.VERSION.RELEASE;
            KvStorage.put(LocalConfig.LC_OS, version);
        }
        return version;
    }

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getCurrentVersion() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }


    /**
     * 获取可用内存大小RAM
     *
     * @return String GB
     */
    public String getAvailMemory() {
        return Formatter.formatFileSize(mContext, getAvailMemoryByte());
    }

    /**
     * 获取android当前可用内存大小
     *
     * @return
     */
    public long getAvailMemoryByte() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return mi.availMem;
    }

    /**
     * 获取手机内存大小RAM
     *
     * @return String GB
     */
    public String getTotalMemory() {
        return Formatter.formatFileSize(mContext, getTotalMemoryByte());
    }

    /**
     * 获取手机内存大小RAM
     *
     * @return long 单位byte
     */
    public long getTotalMemoryByte() {
        String fileName = "/proc/meminfo";// 系统内存信息文件
        String fileText;
        String[] arrayOfString;
        long totalMemory = 0;
        FileReader localFileReader = null;
        try {
            localFileReader = new FileReader(fileName);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            fileText = localBufferedReader.readLine();
            arrayOfString = fileText.split("\\s+");
            totalMemory = Long.valueOf(arrayOfString[1]) * 1024;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (localFileReader != null) {
                try {
                    localFileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return totalMemory;
    }

    /**
     * 获取已用存储
     *
     * @return
     */
    @SuppressLint("NewApi")
    public String getUsedMemory() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(mContext, mi.totalMem - mi.availMem);
    }

    /**
     * 获取外部存储
     *
     * @return
     */
    public String getExternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, totalBlocks * blockSize);
    }

    /**
     * 获取手机内存大小RAM
     *
     * @return String GB
     */
    public String getTotalEnviMemory(long size) {
        return Formatter.formatFileSize(mContext, size);
    }

    /**
     * 获取手机存储空间以及可用空间
     */
    public long getBlocMemory(String strType) {
        File root = Environment.getDataDirectory();
        //设置为1是为了避免计算百分比报错
        long mBlockSize = 1;
        try {
            StatFs statfs = new StatFs(root.getPath());
            // 获取SDCard上BLOCK总数
            long nTotalBlocks = statfs.getBlockCount();
            // 获取SDCard上每个block的SIZE
            long nBlocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();
            // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
            long nFreeBlock = statfs.getFreeBlocks();
            // 计算SDCard 总容量大小MB
            long total = nTotalBlocks * nBlocSize;
            // 计算 SDCard 剩余大小MB
            long free = nAvailaBlock * nBlocSize;
            if ("total".equals(strType)) {
                mBlockSize = total;
            } else if ("free".equals(strType)) {
                mBlockSize = free;
            }
        } catch (IllegalArgumentException e) {
            Log.e("LOG_TAG", e.toString());
        }
        return mBlockSize;
    }

    /**
     * 获取UUid
     * @return
     */
    public String getUUID() {
        String serial;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = "";//Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 获取设备IMEI
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getDeviceIMEI() {
        String imei = getUUID();
        Log.d("TAG", "getDeviceIMEI: "+imei);
//        KvStorage.put(LocalConfig.LC_IMEI, imei);
        return imei;
    }

    /**
     * 获取 AndroidID
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getAndroidId() {
        String ANDROID_ID = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
        KvStorage.put(LocalConfig.LC_ANDROIDID, ANDROID_ID);
        Log.d("TAG", "ANDROID_ID: "+ANDROID_ID);
        return ANDROID_ID;
    }

    /**
     * 获取是否开启开发者选项
     *
     * @return
     */
    @SuppressLint({"NewApi", "InlinedApi"})
    public String IsUsbAdb() {
        int value = 0;
        //4.2以下
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            value = Settings.System.getInt(mContext.getContentResolver(), Settings.System.ADB_ENABLED, 0);
        } else {
            //4.2或4.2以上
            value = Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        }
        return (value > 0) ? "1" : "0";
    }

    /**
     * 获取屏幕分辨率
     *
     * @param
     */
    @SuppressLint("NewApi")
    public String getScreenSize() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return width + "x" + height;
    }

    /**
     * 获取屏幕尺寸
     *
     * @param
     */
    @SuppressLint({"NewApi", "DefaultLocale"})
    public String getScreenSizeOfDevice() {
        Point point = new Point();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getRealSize(point);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        double x = Math.pow(point.x, 2);
        double y = Math.pow(point.y, 2);
        double screenInches = Math.sqrt(x + y) / dm.densityDpi;
        double ddd = Math.round(screenInches * 10) * 0.1d;
        String screenSize = String.format("%.1f", ddd);
        return screenSize;
    }

    /**
     * 获取CPU名字
     *
     * @param
     */
    public String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            @SuppressWarnings("resource") BufferedReader br = new BufferedReader(fr);
            for (; ; ) {
                String text = br.readLine();
                if (!text.contains("Hardware")) {
                    continue;
                }
                String[] array = text.split(":\\s+", 2);
                for (int j = 0; j < array.length; j++) {
                    Log.d("getCpuName", "");
                }
                return array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取电话号码
     *
     * @param
     */
    @SuppressLint("MissingPermission")
    public String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getLine1Number();
        }
        return null;
    }

    /**
     * 获取电话IMSI
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getPhoneIMSI() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSubscriberId();
        }
        return null;
    }

    /**
     * 获取电话IMSI
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getPhoneIccid() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getSimSerialNumber();
        }
        return null;
    }

    /**
     * 获取屏幕分辨率
     */
    public String getScreenRatio() {
        DisplayMetrics dm2 = mContext.getResources().getDisplayMetrics();
        String strOpt = dm2.heightPixels + " x " + dm2.widthPixels;
        return strOpt;
    }


    /**
     * 判断是否支持双卡
     *
     * @param
     * @return
     */
    public boolean isDualSIM() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass = TelephonyManager.class;
        boolean ret = false;
        try {
            Method getSubscriberId = telephonyClass.getMethod("isMultiSimEnabled");
            Object o = getSubscriberId.invoke(telephonyManager);
            ret = (Boolean) o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取渠道名
     *
     * @return 渠道名
     */
    public String getChannelName() {
        String channelName = null;
        PackageManager packageManager = mContext.getPackageManager();
        if (packageManager != null) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                channelName = String.valueOf(applicationInfo.metaData.get(Constants.KEY_CHANNEL));
                KvStorage.put(LocalConfig.LC_CHANNEL, channelName);
            } catch (PackageManager.NameNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        return channelName;
    }

    /**
     * app版本号
     *
     * @return
     */
    public int getVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
            KvStorage.put(LocalConfig.LC_APPVERSION, verName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取 MacAddress
     *
     * @return
     */
    public String getMacAddress() {

        String macAddress = null;
        WifiManager wifiManager =
                (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());

        if (!wifiManager.isWifiEnabled()) {
            //必须先打开，才能获取到MAC地址
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        }
        if (null != info) {
            macAddress = "";// info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * true 是模拟器
     *
     * @return
     */
    public boolean siSimulator() {
        if (notHasLightSensorManager()
                || isFeatures()
                || checkIsNotRealPhone()
                || checkPipes()) {
            KvStorage.put(LocalConfig.LC_ISSIMULATOR, "1");
            return true;
        } else {
            KvStorage.put(LocalConfig.LC_ISSIMULATOR, "0");
            return false;
        }
    }

    /*
     *用途:判断蓝牙是否有效来判断是否为模拟器
     *返回:true 为模拟器
     */
    public boolean notHasBlueTooth() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            return true;
        } else {
            // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
            String name = "";//ba.getName();
            if (TextUtils.isEmpty(name)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /*
     *用途:依据是否存在光传感器来判断是否为模拟器
     *返回:true 为模拟器
     */
    public Boolean notHasLightSensorManager() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
        if (null == sensor8) {
            return true;
        } else {
            return false;
        }
    }

    /*
     *用途:根据部分特征参数设备信息来判断是否为模拟器
     *返回:true 为模拟器
     */
    public boolean isFeatures() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    /*
     *用途:根据CPU是否为电脑来判断是否为模拟器
     *返回:true 为模拟器
     */
    public boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
            return true;
        }
        return false;
    }

    /*
     *用途:根据CPU是否为电脑来判断是否为模拟器(子方法)
     *返回:String
     */
    public String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
        }
        return result;
    }

    /*
     *用途:检测模拟器的特有文件
     *返回:true 为模拟器
     */
    private String[] known_pipes = {"/dev/socket/qemud", "/dev/qemu_pipe"};

    public boolean checkPipes() {
        for (int i = 0; i < known_pipes.length; i++) {
            String pipes = known_pipes[i];
            File qemu_socket = new File(pipes);
            if (qemu_socket.exists()) {
                Log.v("Result:", "Find pipes!");
                return true;
            }
        }
        return false;
    }

    private boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }


    /**
     * 得到全局唯一UUID
     */
    public String getDeviceId() {
        String uuid = KvStorage.get(LC_DEVICEID, "");
        if (isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            KvStorage.put(LC_DEVICEID, uuid);
        }
        return uuid;
    }
}
