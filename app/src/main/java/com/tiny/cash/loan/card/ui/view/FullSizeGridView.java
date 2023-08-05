package com.tiny.cash.loan.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class FullSizeGridView extends GridView {

    public FullSizeGridView(Context context) {
        super(context);
    }

    public FullSizeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullSizeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                    , MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
