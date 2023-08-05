package com.tiny.cash.loan.card.ui.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * 重写DatePickerDialog
 * @author Administrator
 *		将不同系统月份英文修改为数字
 */
public class NewDatePicker extends DatePickerDialog{

    private static final String[] mDisplayMonths = {"January", "February", "March","April", "May", "June","July", "Aguest", "September","October", "November", "December"};

    /**
     * 构造方法
     * @param context
     * @param callBack
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    public NewDatePicker(Context context, OnDateSetListener callBack, int year,
                         int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        // TODO Auto-generated constructor stub
    }
    public NewDatePicker(Context context, int theme,
                         OnDateSetListener callBack, int year, int monthOfYear,
                         int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);
        // TODO Auto-generated constructor stub
    }

    /**
     * 重新排序 年月日（英文会出现 月 日 年的情况，所以需要排序）
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
        if (mSpinners != null) {
            NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
            NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
            NumberPicker mDaySpinner = (NumberPicker)findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
            mSpinners.removeAllViews();
            if (mYearSpinner != null) {
                mSpinners.addView(mYearSpinner);
            }
            if (mMonthSpinner != null) {
                mSpinners.addView(mMonthSpinner);
            }
            if (mDaySpinner != null) {
                mSpinners.addView(mDaySpinner);
            }
        }
    }

    /**
     * 有了年月日的正常顺序，下面的代码就不会出现数组越界异常
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        // TODO Auto-generated method stub
        super.onDateChanged(view, year, month, day);
        setTitle(year+"/ "+(month+1)+"/ "+day+"/");
        //关键行
        ((NumberPicker)((ViewGroup)((ViewGroup)view.getChildAt(0)).getChildAt(0)).getChildAt(1)).setDisplayedValues(mDisplayMonths);
    }
}
