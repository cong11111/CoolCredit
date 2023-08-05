package com.tiny.cash.loan.card.utils.ui;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.tiny.cash.loan.card.utils.LogUtil;

/**
 * @author lv
 * @time 2020/8/6 0015  14:35
 * @describe Toast显示公共类
 */
public class ToastManager {

    public static void show(Context context, int resId) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        show(context, context.getResources().getText(resId), Toast.LENGTH_LONG);
    }

    public static void show(Context context, int resId, int duration) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        Toast.makeText(context, text, duration).show();
    }

    public static void show(Context context, int resId, Object... args) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_LONG);
    }

    public static void show(Context context, String format, Object... args) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        show(context, String.format(format, args), Toast.LENGTH_LONG);
    }

    public static void showOnRunnable(final Context context, final String msg, Handler handler) {
//        if (!LogUtil.DEBUG) {
//            return;
//        }
        handler.post(new Runnable() {

            @Override
            public void run() {
                show(context, msg);
            }
        });
    }

    public static void showOnRunnable(final Context context, final String msg) {
        showOnRunnable(context, msg, new Handler(context.getMainLooper()));
    }

    public static void showOnRunnable(final Context context, final int resId, Handler handler) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                show(context, context.getResources().getString(resId));
            }
        });
    }

    public static void showOnRunnable(final Context context, final int resId) {
        showOnRunnable(context, resId, new Handler(context.getMainLooper()));
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

}
