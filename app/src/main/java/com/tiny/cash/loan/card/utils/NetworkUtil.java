package com.tiny.cash.loan.card.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtil {


    private WifiManager mWifiMan;

    /**
     * 获取网络类型
     *
     * @return
     */
    private String getNetType(ConnectivityManager mConMan) {
        NetworkInfo netInfo = mConMan.getActiveNetworkInfo();
        if (netInfo == null) {
            return "none";
        }
        int type = netInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            return "mobile";
        }
        return "none";
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public  String getIpAddress(Context mContext) {
        mWifiMan = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager mConMan = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (!"wifi".equals(getNetType(mConMan))) {
            return getLocalIpAddress();
        }
        WifiInfo wifiInfo = mWifiMan.getConnectionInfo();
        if (wifiInfo == null) {
            return getLocalIpAddress();
        }
        int ip = wifiInfo.getIpAddress();
        if (ip == 0) {
            return getLocalIpAddress();
        }
        return String.format("%d.%d.%d.%d", ip & 0xff, ip >> 8 & 0xff,
                ip >> 16 & 0xff, ip >> 24 & 0xff);
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        byte[] addr = inetAddress.getAddress();
                        if (addr.length == 4) {
                            int ip = ((addr[3] & 0xff) << 24) | ((addr[2] & 0xff) << 16) | ((addr[1] & 0xff) << 8) | (addr[0] & 0xff);
                            return String.format("%d.%d.%d.%d", ip & 0xff, ip >> 8 & 0xff, ip >> 16 & 0xff, ip >> 24 & 0xff);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Error==:", ex.toString());
        }
        return "";
    }

}
