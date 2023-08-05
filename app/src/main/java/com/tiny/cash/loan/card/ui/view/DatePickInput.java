package com.tiny.cash.loan.card.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.tiny.cash.loan.card.kudicredit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.widget.AppCompatTextView;

public class DatePickInput extends AppCompatTextView {
    //    <!--            <com.example.coolcredit.view.DatePickInput-->
    //<!--                android:layout_width="match_parent"-->
    //<!--                android:layout_height="match_parent"-->
    //<!--                android:layout_gravity="center_vertical|center_horizontal"-->
    //<!--                android:layout_marginRight="20dp"-->
    //<!--                android:background="@color/color_FFFFFF"-->
    //<!--                android:gravity="center_vertical"-->
    //<!--                android:hint="请选择日期"-->
    //<!--                android:layout_marginTop="@dimen/dp_53"-->
    //<!--                android:inputType="datetime"-->
    //<!--                android:focusable="false"-->
    //<!--                android:maxLength="50"-->
    //<!--                android:singleLine="true"/>-->
    /**
     * 日期框
     */
    private NewDatePicker mDialog;
    private Context context;
    /**
     * 月份的取值
     */

    private static final String[] mDisplayMonths = {"January", "February", "March", "April", "May", "June",
            "July", "Aguest", "September", "October", "November", "December"};

    public DatePickInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public DatePickInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DatePickInput(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        setFocusable(false);
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        final Calendar c = Calendar.getInstance(Locale.CHINA);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Date date = new Date();
                try {
                    date = dateFormat.parse(getText().toString());
                } catch (ParseException e) {
                }
                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                mDialog = new NewDatePicker(getContext(), AlertDialog.THEME_HOLO_LIGHT, null,
                        year, month, day);
                mDialog.setTitle(year + "Year " + (month + 1) + "Month " + day + "Day");
                mDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.str_done),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = mDialog.getDatePicker();
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        c.set(year, month, day);
                        Date date = c.getTime();

                        Date curDate = new Date(System.currentTimeMillis());
                        setText(dateFormat.format(c.getTime()));
                    }
                });
                mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.str_cancel),
                        new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        setText("");
                    }
                });
                mDialog.show();
                /**
                 * 初始设置月份
                 */
                DatePicker dp = mDialog.getDatePicker();
                if (dp != null) {
                    ((NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1)).setDisplayedValues(mDisplayMonths);
                }
            }
        });
    }
}
