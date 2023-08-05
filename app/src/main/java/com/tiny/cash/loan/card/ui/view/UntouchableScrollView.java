package com.tiny.cash.loan.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class UntouchableScrollView extends ScrollView {
    public UntouchableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 不拦截，继续分发下去
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
