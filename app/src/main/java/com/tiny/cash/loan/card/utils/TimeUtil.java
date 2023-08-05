package com.tiny.cash.loan.card.utils;

import android.annotation.SuppressLint;
import android.net.ParseException;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author lv
 * @time 2020/8/6 0015  14:32
 * @describe 时间转换获取工具类
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    private static final String YMDHMS_FORMAT = "HH:mm, MMMM dd, yyyy";
    public static String curTimeToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public static String curYMDToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public static String curHMSToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public static String getSystemTime(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String format(long Date) {
        try {
            return new SimpleDateFormat(YMDHMS_FORMAT).format(new Date(Date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMDHMS_FORMAT);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMDHMS_FORMAT);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /**
     * local ---> UTC
     *
     * @return
     */
    public static String Local2UTC(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String gmtTime = sdf.format(new Date(timeStamp));
        return gmtTime;
    }


    /**
     * UTC --->local
     *
     * @param utcTime UTC
     * @return
     */
    public static String utc2Local(String utcTime) {
        try {
            if (TextUtils.isEmpty(utcTime)) {
                return "";
            }
            SimpleDateFormat utcFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date gpsUTCDate = null;
            try {
                gpsUTCDate = utcFormater.parse(utcTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat localFormater = new SimpleDateFormat(YMDHMS_FORMAT);
            localFormater.setTimeZone(TimeZone.getDefault());
            String localTime = localFormater.format(gpsUTCDate.getTime());
            return localTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
