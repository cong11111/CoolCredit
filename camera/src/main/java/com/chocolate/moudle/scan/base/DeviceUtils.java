package com.chocolate.moudle.scan.base;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceUtils {
    /**
     * Return whether device is emulator.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmulator(Context context) {
        boolean checkProperty = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) return true;

        String operatorName = "";
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
                operatorName = name;
            }
        }
        boolean checkOperatorName = operatorName.toLowerCase().equals("android");
        if (checkOperatorName) return true;

        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        boolean checkDial = intent.resolveActivity(context.getPackageManager()) == null;
        if (checkDial) return true;
        if (isEmulatorByCpu()) return true;

//        boolean checkDebuggerConnected = Debug.isDebuggerConnected();
//        if (checkDebuggerConnected) return true;

        return false;
    }

    /**
     * Returns whether is emulator by check cpu info.
     * by function of {@link #readCpuInfo}, obtain the device cpu information.
     * then compare whether it is intel or amd (because intel and amd are generally not mobile phone cpu), to determine whether it is a real mobile phone
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    private static boolean isEmulatorByCpu() {
        String cpuInfo = readCpuInfo();
        return cpuInfo.contains("intel") || cpuInfo.contains("amd");
    }

    /**
     * Return Cpu information
     *
     * @return Cpu info
     */
    private static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            StringBuilder sb = new StringBuilder();
            String readLine;
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ignored) {
        }
        return result;
    }
}
