package com.chocolate.moudle.scan.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ConvertUtils;
import com.chocolate.moudle.scan.R;

public class CameraOverlay extends View {

    private final Paint mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int maskColor = 0;
    private Paint mBoardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int boardColor = 0;
    private RectF mDrawRectF = new RectF();

    private float radius = 0f;

    public CameraOverlay(Context context) {
        super(context);
        initPaint();
    }

    public CameraOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CameraOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mMaskPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        maskColor = ContextCompat.getColor(getContext(), R.color.color_881c1f3e);

        mMaskPaint.setColor(maskColor);
        PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        mMaskPaint.setXfermode(mode);

        boardColor = ContextCompat.getColor(getContext(), R.color.color_white);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setColor(boardColor);
        mBoardPaint.setStrokeWidth(2f);

        radius = ConvertUtils.dp2px(8);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        RectF rectF = createRectBg();
        mDrawRectF.set(rectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mDrawRectF, radius, radius, mMaskPaint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mMaskPaint);

        canvas.drawRoundRect(mDrawRectF, radius, radius, mBoardPaint);
    }

    private RectF createRectBg() {
         // 332, 237
        //667 , 375
        Pair<Integer, Integer> pair = getWidthAndHeight(getWidth());
        int innerW = pair.first;
        int innerH = pair.second;
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        int paddingHor = (int) ((getWidth() - innerW) / 2f);
        int paddingVer = (int) ((getHeight() - innerH) / 2f);
        rectF.inset(paddingHor, paddingVer);
        return rectF;
    }

    public static Pair<Integer, Integer> getWidthAndHeight(int width){
        int innerW = (int) (width / 667f * 332);
        int innerH = (int) (innerW / 332f * 237);
        return new Pair<Integer, Integer>(innerW, innerH);
    }
}
