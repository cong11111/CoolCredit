package com.tiny.cash.loan.card.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.kudicredit.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lv
 * @time 2020/8/6 0015  14:32
 * @describe Log日志打印工具类
 */
public class LogUtil {
    private static LogUtil mInstance = null;
    public static boolean DEBUG = BuildConfig.DEBUG;
    public static boolean isRelease = !BuildConfig.DEBUG;
    private FileOutputStream mLog;

    public static LogUtil getInstance() {
        if (mInstance == null) {
            mInstance = new LogUtil();
        }
        return mInstance;
    }

    private LogUtil() {
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mosetting/log";
        if (FileUtil.mkdir(logPath)) {
            clearStaleLogs(logPath);
            logPath += ("/" + TimeUtil.curTimeToString() + ".txt");
            try {
                mLog = new FileOutputStream(new File(logPath));
            } catch (FileNotFoundException e) {
                mLog = null;
            }
        }
    }

    private void clearStaleLogs(String logDir) {
        List<String> logs = FileUtil.getSortedChilds(logDir);
        // 留最后10份日志
        if (logs != null && logs.size() >= 10) {
            for (int i = 0; i < logs.size() - 9; ++i) {
                File f = new File(logs.get(i));
                f.delete();
            }
        }
    }

    public static void i(String content) {
        if (isRelease) return;
        if (mInstance != null) {
            mInstance.log("info", content);
        }
        if (!DEBUG) {
            return;
        }
        StackTraceElement invoker = getInvoker();
        Log.i(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + content);
    }

    public static void w(String content) {
        if (isRelease) return;
        if (mInstance != null) {
            mInstance.log("warn", content);
        }
        if (!DEBUG) {
            return;
        }
        StackTraceElement invoker = getInvoker();
        Log.w(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + content);
    }

    public static void d(String content) {
        if (isRelease) return;
        if (mInstance != null) {
            mInstance.log("warn", content);
        }
        if (!DEBUG) {
            return;
        }
        StackTraceElement invoker = getInvoker();
        Log.d(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + content);
    }

    public static void e(String content) {
        if (isRelease) return;
        if (mInstance != null) {
            mInstance.log("err", content);
        }
        if (!DEBUG) {
            return;
        }
        StackTraceElement invoker = getInvoker();
        Log.e(invoker.getClassName(), "【" + invoker.getMethodName() + ":" + invoker.getLineNumber() + "】" + content);
    }

    private static StackTraceElement getInvoker() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private void log(String tag, String content) {
        if (isRelease) return;
        if (mLog != null) {
            StringBuilder sb = new StringBuilder("[");
            sb.append(TimeUtil.curTimeToString());
            sb.append("][");
            sb.append(tag);
            sb.append("]");
            sb.append(content);
            sb.append("\r\n");
            try {
                synchronized (this) {
                    mLog.write(sb.toString().getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logPolicy(String log) {
        if (isRelease) return;
        Log.i("policyLog", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/policyLog/policyLog" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
    }

    public static void logUpGps(String log) {
        if (isRelease) return;
        Log.i("logUpGps", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/gps/gpsUpLog" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
    }

    public static void logUpPic(String log) {
        if (isRelease) return;
        Log.i("logUpPic", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/pic/picUpLog" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
    }

    public static void logAlive(String log) {
        if (isRelease) return;
        Log.i("logAlive", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/alive/logAlive" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
    }

    public static void logDB(String log) {
        if (isRelease) return;
        Log.i("logDB", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/dblog/logDB" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
    }

    public static String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }

    public static String logGuide(String log) {
        Log.i("guideLog", log);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String currDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yunqilu/guideLog/guideLog" + currDay + ".txt";
        FileUtil.writeStr2File(logPath, time + "=======" + log + "\n");
        return logPath;
    }

    public static String collectDeviceInfo() {
        Map<String, String> infos = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        try {
            PackageManager pm = KudiCreditApp.getInstance().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(KudiCreditApp.getInstance().getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            }

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = sDateFormat.format(new Date());
            sb.append("\r\n" + date + "\n");
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
