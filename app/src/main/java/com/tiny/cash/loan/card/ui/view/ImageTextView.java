package com.tiny.cash.loan.card.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.tiny.cash.loan.card.kudicredit.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * *作者: jyw
 * 日期：2020/6/4 14:41
 */

public class ImageTextView extends AppCompatTextView {
    private TextPaint mBackGroundPaint;
    private Bitmap mBitmap;
    private Context mContext;
    private String mTxt;
    private int mRectWidth;
    private int mRectHeight;
    private int mWidthPixels;
    private TextPaint mTextPaint;
    private int paddingLeft;
    private int mTextLeft = 0;
    private Rect mTextBoundRect;
    private Rect mBackgroundRect;
    private String mText;
    private String mKey;
    private List<String> mIntegerList = new ArrayList<>();
    private String mString;
    private int mBitmapX;
    private int mBitmapY;
    private String mEllipsis = "...";
    private float wordSize;
    private int seleteColor, defaultColor,backgroundColor;
    private int mTalNum;
    private Paint mBitmapPaint;

    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        paddingLeft = mContext.getResources().getDimensionPixelSize(R.dimen.dp_10);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        seleteColor = typedArray.getInt(R.styleable.ImageTextView_selColor, -1);
        defaultColor = typedArray.getInt(R.styleable.ImageTextView_textColor, -1);
        backgroundColor = typedArray.getInt(R.styleable.ImageTextView_backgroundColor, -1);
        wordSize = typedArray.getDimension(R.styleable.ImageTextView_size, sp2px(8));
        typedArray.recycle();
        init();
    }

    private void init() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(wordSize);
        mTextPaint.setColor(defaultColor);
        mBackGroundPaint = new TextPaint();
        mBackGroundPaint.setAntiAlias(true);
        if (backgroundColor == -1){
            mBackGroundPaint.setColor(Color.TRANSPARENT);
        }else {
            mBackGroundPaint.setColor(backgroundColor);
        }
        mBackGroundPaint.setTextSize(wordSize);

        mBitmapPaint = new TextPaint();
        mBitmapPaint.setAntiAlias(true);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mWidthPixels = displayMetrics.widthPixels;
        mTextBoundRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mRectWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mRectWidth = mWidthPixels / 2;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mRectHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mRectHeight = 60;
        }
        setMeasuredDimension(mRectWidth, mRectHeight);
        //1.画显示背景的矩形框
        mBackgroundRect = new Rect(0, 0, mRectWidth, mRectHeight);
    }

    /**
     * 这个方法调用之前要先调用settext()
     * @param resId
     * @param newWidth
     * @param newHeight
     * @param y
     */
    public void setDrawLeftBitMap(int resId, int newWidth, int newHeight,int y) {

        if (resId == 0) {
            mBitmap = null;
        } else {
            mBitmapY = y;
            mBitmapPaint.setColor(Color.WHITE);
            mBitmap = changeBitmapSize(resId, newWidth, newHeight);
        }
        invalidate();
    }

    /**
     * 设置高亮字体展示
     * @param str
     * @param key
     */
    public void setKey(String str, String key) {
        setText(str);
        mKey = key;
        invalidate();
    }

    private Bitmap changeBitmapSize(int resId, int newWidth, int newHeight) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //计算压缩的比率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //获取新的bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float baseline = fm.descent - fm.ascent;
        float x;
        float y = baseline;  //由于系统基于字体的底部来绘制文本，所有需要加上字体的高度。
        int width = getWidth() - paddingLeft;
        mTxt = getText().toString();
        canvas.drawRect(mBackgroundRect, mBackGroundPaint);
        if (mBitmap != null) {
            mTxt = getText().toString();
            width = getWidth() - mBitmap.getWidth() - paddingLeft;
        }

        //文本自动换行
        String[] texts = autoSplit(mTxt, mTextPaint, width);
        if (texts.length <= 0) {
            return;
        }
        for (int i = 0; i < texts.length; i++) {
            mText = texts[i];
            float mMeasureTextWidth = 0;
            if (TextUtils.isEmpty(mText)) {
                continue;
            }
            //不同文字有不同的范围框，通过Rect.centerX()拿到文本矩形框的中间位置
            mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBoundRect);
            if (!TextUtils.isEmpty(mKey)) {//找出高亮关键字位置

                if (i == 1){
                    if (mIntegerList.size() != mKey.length()){
                        mTalNum = mIntegerList.size();
                        mIntegerList = matcherSearchTitle( mText, mKey);//"..."不能高亮显示，所以在这加了三目运算
                    }else {
                        mIntegerList.clear();
                    }
                }else {
                    mTalNum = 0;
                    mIntegerList = matcherSearchTitle(mText.contains(mEllipsis) ?
                            mText.substring(0, mText.length() - 3) : mText, mKey);//"..."不能高亮显示，所以在这加了三目运算
                }

            }
            if (mBitmap != null && i == 0) {
                mBitmapX =
                        mBackgroundRect.centerX() - mTextBoundRect.centerX() - 2 * mBitmap.getWidth() / 3;
                x = mBackgroundRect.centerX() - mTextBoundRect.centerX() + mBitmap.getWidth() / 2 + mTextLeft;
                canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mBitmapPaint);
            } else {
                x = mBackgroundRect.centerX() - mTextBoundRect.centerX();
            }

            if (TextUtils.isEmpty(mKey)) {
                canvas.drawText(mText, x + mMeasureTextWidth, y, mTextPaint);
            } else {
                for (int j = 0; j < mText.length(); j++) {
                    for (String item : mIntegerList) {
                        if (j == Integer.parseInt(item)) {//更改高亮字体颜色
                            mTextPaint.setColor(seleteColor);
                            break;
                        }
                    }
                    mString = String.valueOf(mText.toCharArray()[j]);
                    mMeasureTextWidth = mTextPaint.measureText(mText, 0, j);

                    canvas.drawText(mString, x + mMeasureTextWidth, y, mTextPaint);
                    mTextPaint.setColor(Color.BLACK);
                }
            }
            y += baseline + fm.leading; //添加字体行间距
        }


    }

    /**
     * 关键字高亮变色     *
     * * @param color 变化的色值
     * * @param text 文字
     * * @param keyword 文字中的关键字
     * * @
     * return 所有高亮字体位置集合
     */
    String mPinyinText;
    int mKeyStart;
    int mTextIndex;
    List<String> list = new ArrayList<>();
    Pattern pattern;

    public List<String> matcherSearchTitle(String text, String keyword) {
        list.clear();
        mTextIndex = 0;
        mKeyStart = 0;
        if (!TextUtils.isEmpty(keyword)) {
            String name = text.toLowerCase();
            String mKeyword = keyword.toLowerCase();
            int mKeyWordLenth = mKeyword.length();
//            if (Utils.isContainChinese(name)) {
//                for (Character item : name.toCharArray()) {
//                    if (list.size() + mTalNum == mKeyWordLenth) {
//                        return list;
//                    }
//                    if (mKeyStart >= mKeyWordLenth) {
//                        mKeyStart = mKeyWordLenth - 1;
//                    }
//
//                    if (Utils.isContainChinese(item.toString())) {//单个字符是汉字情况
//                        pattern = Pattern.compile(Pattern.quote(mKeyword));
//                        boolean mKeyContainChinese = Utils.isContainChinese(mKeyWordLenth > 1
//                                ? mKeyword.substring(mKeyStart, mKeyStart + 1) : mKeyword);
//                        if (mKeyContainChinese) {//搜索条件和 文件名字都是汉字
//                            mPinyinText = item.toString();
//                        } else {
//                            mPinyinText = Pinyin.toPinyin(item.toString(), "").toLowerCase();
//                        }
//
//                        if (!pattern.matcher(mPinyinText).find()) {
//                            int index = 0;
//                            for (char c : mKeyword.toCharArray()) {
////                                if (mPinyinText.indexOf(c) != 0) {//首字母开始匹配
////                                    continue;
////                                }
//                                boolean a = true;
//                                pattern = Pattern.compile(Pattern.quote(String.valueOf(c)));
//                                a = a && pattern.matcher(mPinyinText.substring(index)).find();
//                                index = mPinyinText.indexOf(String.valueOf(c));
//                                index++;
//                                if (a) {
//                                    list.add(String.valueOf(mTextIndex));
//                                    mKeyStart++;
//                                    break;
//                                }
//                            }
//                        } else {
////                            if (mPinyinText.indexOf(keyword) == 0) {//首字母开始匹配
//                                list.add(String.valueOf(mTextIndex));
//                                mKeyStart++;
////                            }
//
//                        }
//                    } else {//单个字符不是汉字情况
//                        for (char c : mKeyword.toCharArray()) {
//                            pattern = Pattern.compile(Pattern.quote(String.valueOf(c)));
//                            Matcher m = pattern.matcher(item.toString());
//                            if (m.find()) {
//                                list.add(String.valueOf(mTextIndex));
//                                mKeyStart++;
//                                break;
//                            }
//                        }
//                    }
//                    mTextIndex++;
//                }
//            } else {//不包含汉字
                for (char c : mKeyword.toCharArray()) {
                    pattern = Pattern.compile(Pattern.quote(String.valueOf(c)));
                    Matcher m = pattern.matcher(name);
                    while (m.find()) {
                        int start = m.start();
                        int end = m.end();
                        while (start < end) {
                            list.add(String.valueOf(start));
                            start++;
                        }
                    }
                }
            }
//        }
        return list;
    }


    /**
     * 自动测算显示字符宽度，并进行裁切
     *
     * @param string
     * @param p
     * @param width
     * @return
     */
    private String[] autoSplit(String string, Paint p, float width) {
        int length = string.length();
        float textWidth = p.measureText(string);
        if (textWidth <= width) {
            return new String[]{string};
        }

        int start = 0, end = 1, i = 0;
        int num = length - 1;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while (start < length) {//第一行
            if (p.measureText(string, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = (String) string.subSequence(start, end);
                start = end;
                break;
            }

            if (end == length) { //不足一行的文本
                lineTexts[i++] = (String) string.subSequence(start, end);
                break;
            }
            end += 1;
        }
        while (num > 0 && lines > 1) {//第二行
            if (num > start) {
                if (p.measureText(string, num, length) > width) { //文本宽度超出控件宽度时
                    lineTexts[i++] = (String) string.subSequence(num, length);
                    String firstString = lineTexts[0];
                    int firstStringLength = firstString.length();
                    for (int j = 1; j < 4; j++) {//动态算出省略号占用的字符长度，再去掉相应长度的字符
                        if (p.measureText(firstString, firstStringLength - j, firstStringLength) > p.measureText(mEllipsis, 0, mEllipsis.length())) {
                            lineTexts[0] =
                                    firstString.subSequence(0, firstStringLength - j) + mEllipsis;
                            break;
                        }
                    }
                    break;
                }
            }
            if (num == start) { //不足一行的文本
                lineTexts[i++] = (String) string.subSequence(num, length);
                break;
            }
            num -= 1;
        }
        return lineTexts;
    }
    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
