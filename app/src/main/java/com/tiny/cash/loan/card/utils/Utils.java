/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tiny.cash.loan.card.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;
import android.widget.Toast;

import com.tiny.cash.loan.card.kudicredit.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import co.paystack.android.utils.StringUtils;
import io.reactivex.disposables.Disposable;

import static android.service.quicksettings.TileService.ACTION_QS_TILE_PREFERENCES;

public class Utils {

    private static final StringBuilder sBuilder = new StringBuilder(50);
    private static final java.util.Formatter sFormatter = new java.util.Formatter(
            sBuilder, Locale.getDefault());

    public static String formatDateRange(Context context, long start, long end) {
        final int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH;

        synchronized (sBuilder) {
            sBuilder.setLength(0);
            return DateUtils.formatDateRange(context, sFormatter, start, end,
                    flags, null).toString();
        }
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasNougatMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasOreoMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1;
    }

    public static boolean hasPie() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean hasMoreHeap() {
        return Runtime.getRuntime().maxMemory() > 20971520;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isLowRamDevice(Context context) {
        if (Utils.hasKitKat()) {
            final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            return am.isLowRamDevice();
        }
        return !hasMoreHeap();
    }


    public static String encodeData(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String s1 = s.replaceAll("%", "").replaceAll("\\+", "").replaceAll("\"", "").replaceAll("'", "").replaceAll("\\\\", "");
        try {
            return URLEncoder.encode(s1, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String filter(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        String s1 = s.replaceAll("%", "").replaceAll("\\+", "");
        return s1;

    }

    public static String formatTime(Context context, long when) {
        // TODO: DateUtils should make this easier
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int flags = DateUtils.FORMAT_NO_NOON | DateUtils.FORMAT_NO_MIDNIGHT | DateUtils.FORMAT_ABBREV_ALL;

        if (then.year != now.year) {
            flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        return DateUtils.formatDateTime(context, when, flags);
    }

    public static long getDirectorySize(File dir) {
        long result = 0L;
        if (dir.listFiles() != null && dir.listFiles().length > 0) {
            for (File eachFile : dir.listFiles()) {
                result += eachFile.isDirectory() && eachFile.canRead() ? getDirectorySize(eachFile) : eachFile.length();
            }
        } else if (!dir.isDirectory()) {
            result = dir.length();
        }
        return result;
    }

    public static boolean hasSoftNavBar(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        return !hasMenuKey && !hasBackKey;

    }

    private static final int BRIGHTNESS_THRESHOLD = 150;

    public static boolean isColorDark(int color) {
        return ((30 * Color.red(color) +
                59 * Color.green(color) +
                11 * Color.blue(color)) / 100) <= BRIGHTNESS_THRESHOLD;
    }

    public static final int PRESSED_COLOR_LIGHTUP = 255 / 25;

    public static int getLightColor(int color, int amount) {
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }

    public static int getLightColor(int color) {
        int amount = PRESSED_COLOR_LIGHTUP;
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }

    public static int getStatusBarColor(int color1) {
        int color2 = Color.parseColor("#000000");
        return blendColors(color1, color2, 0.9f);
    }

    public static int getActionButtonColor(int color1) {
        int color2 = Color.parseColor("#ffffff");
        return blendColors(color1, color2, 0.9f);
    }

    public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    public static int getComplementaryColor(int colorToInvert) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(colorToInvert), Color.green(colorToInvert),
                Color.blue(colorToInvert), hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }


    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isActivityAlive(Activity activity) {
        return !(null == activity || (Utils.hasJellyBeanMR1() ? activity.isDestroyed() : activity.isFinishing()));
    }


    public static boolean hasFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    public static boolean hasLeanback(Context context) {
        return hasFeature(context, PackageManager.FEATURE_LEANBACK);
    }

    public static boolean hasWiFi(Context context) {
        return hasFeature(context, PackageManager.FEATURE_WIFI);
    }


    public static Bitmap getVector2Bitmap(Context context, int id) {
        VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_MEDIUM);
        return Math.round(px);
    }


    public static boolean isRTL() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
                == androidx.core.view.ViewCompat.LAYOUT_DIRECTION_RTL;
    }


    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static ColorStateList getColorStateList(Context context, int colorRes) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int color = ContextCompat.getColor(context, colorRes);

        int[] colors = new int[]{color, color, color, color};
        return new ColorStateList(states, colors);
    }

    public static boolean isQSTile(Intent intent) {
        if (null != intent.getAction()) {
            String action = intent.getAction();
            return ACTION_QS_TILE_PREFERENCES.equals(action);
        }
        return false;
    }


    public static Spanned fromHtml(String text) {
        if (Utils.hasNougat()) {
            return Html.fromHtml(text, 0);
        } else {
            return Html.fromHtml(text);
        }
    }

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Visibility {
    }

    @Visibility
    public static int getVisibility(boolean show) {
        return show ? View.VISIBLE : View.GONE;
    }

    public static boolean checkUSBDevices() {
        return true;
    }

    static Toast sToast = null;

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getApplicationContext().getResources().getString(resId), duration);
    }

    public static void showToast(Context context, String info, int duration) {
        if (sToast == null) {
            View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.toast_popup_layout, null, false);
            sToast = new Toast(context.getApplicationContext());
            sToast.setGravity(Gravity.CENTER, 0, 0);
            sToast.setView(v);
        }
        ((TextView) sToast.getView().findViewById(R.id.txt_toast_popup_info)).setText(info);
        sToast.setDuration(duration);
        sToast.show();
    }

    public static void disposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public static void dismissDialogFragment(DialogFragment dialogFragment) {
        if (dialogFragment != null) {
            dialogFragment.dismissAllowingStateLoss();
            dialogFragment = null;
        }
    }

    /**
     * bitmap转化base64
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                try {
                    baos.flush();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        return result;
    }

    /**
     * 判断是否包含非法字符
     *
     * @param fileName
     * @return
     */
    public static boolean isValidFileName(String fileName) {
        return fileName.matches("([^\\s\\\\./:\\*\\?\\\"<>\\|](\\x20|[^\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|]$)");
    }
}