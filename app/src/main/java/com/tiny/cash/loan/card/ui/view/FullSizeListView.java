package com.tiny.cash.loan.card.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.tiny.cash.loan.card.utils.ui.DisplayUtil;

public class FullSizeListView extends ListView {
    public FullSizeListView(Context context) {
        super(context);
    }

    public FullSizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullSizeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                DisplayUtil.dp2px(getContext(),222));
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}