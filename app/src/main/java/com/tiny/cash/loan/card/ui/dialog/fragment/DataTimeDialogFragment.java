package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogDatepickerBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonListener;
import com.tiny.cash.loan.card.ui.dialog.iface.IPositiveButtonDialogListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


public class DataTimeDialogFragment extends BaseDialogFragment<DataTimeDialogFragment.DialogBuilder> {
    private static int mType;
    private DialogDatepickerBinding mBinding;

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager,
                                              @TYPE int type) {
        mType = type;
        return new DialogBuilder(context, fragmentManager);
    }

    public static final int TYPE_DATE = 1;
    public static final int TYPE_TIME = 2;


    @IntDef({TYPE_DATE, TYPE_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_datepicker;
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params;
//        if (mType == TYPE_DATE) {
            params = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
//        } else {
//            params = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);
////            params.setMargins(10, 0, 10, 0);
//        }
        np.setLayoutParams(params);
    }

    /**
     * 调整FrameLayout大小
     *
     */
    private void resizePicker(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    private void setNumberPickerDivider(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {  //设置颜色
                pf.setAccessible(true);
                ColorDrawable colorDrawable =
                        new ColorDrawable(ContextCompat.getColor(getContext(),
                                R.color.color_ffd1d1d1)); //选择自己喜欢的颜色
                try {
                    pf.set(numberPicker, colorDrawable);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (pf.getName().equals("mSelectionDividerHeight")) {   //设置高度
                pf.setAccessible(true);
                try {
                    int result = 1;  //要设置的高度
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            picker.invalidate();
        }
    }

    /**
     * 得到viewGroup里面的组件
     *
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList();
        View child;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    setNumberPickerDivider((NumberPicker) child);
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * 获取日期选择的值
     */
    private String getDatePickerValue() {
        int mYear = mBinding.datePicker.getYear();
        int mMonth = mBinding.datePicker.getMonth() + 1;
        int mDay = mBinding.datePicker.getDayOfMonth();
        return mYear + "/" + mMonth + "/" + mDay;
    }


    /**
     * 获取时间选择的值
     */
    private String getTimePickerValue() {
        // api23这两个方法过时
        int mHour = mBinding.timePicker.getCurrentHour();// timePicker.getHour();
        int mMinute = mBinding.timePicker.getCurrentMinute();// timePicker.getMinute();
        return mHour + ":" + mMinute;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use DownloadProgressDialogBuilder to construct " + "this dialog");
        }
    }

    @Override
    protected void build(DialogBuilder builder) {
        mBinding = builder.getBinding();
        mBinding.layoutBottom.tvNegative.setText(builder.mNegativeButtonText);
        mBinding.layoutBottom.tvPositive.setText(builder.mPositiveButtonText);
        if (mType == TYPE_DATE) {
            mBinding.datePicker.setVisibility(View.VISIBLE);
            mBinding.timePicker.setVisibility(View.GONE);
            resizePicker(mBinding.datePicker);
        } else {
            resizePicker(mBinding.timePicker);
            mBinding.datePicker.setVisibility(View.GONE);
            mBinding.timePicker.setVisibility(View.VISIBLE);
            mBinding.timePicker.setIs24HourView(true);
            mBinding.timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
            //设置点击事件不弹键盘
            //去除默认分号
            ViewGroup view = (ViewGroup) mBinding.timePicker.getChildAt(0);
            ViewGroup view2 = (ViewGroup) view.getChildAt(1);
            view2.getChildAt(1).setVisibility(View.GONE);
        }
        initData(builder);
    }

    private void initData (DialogBuilder builder){
        mBinding.tvTitle.setText(builder.mMessage);
        mBinding.layoutBottom.tvNegative.setOnClickListener(v -> {
            if (mType == TYPE_DATE) {
                builder.mNegativeButtonListener.onNegativeButtonClicked(getDatePickerValue());
            } else {
                builder.mNegativeButtonListener.onNegativeButtonClicked(getTimePickerValue());
            }
            dismissAllowingStateLoss();
        });
        mBinding.layoutBottom.tvPositive.setOnClickListener(v -> dismissAllowingStateLoss());
        mBinding.ivClose.setOnClickListener(v -> dismissAllowingStateLoss());
    }
    @StyleRes
    public int resolveTheme() {
        return R.style.DateTimePicker_Dialog;
    }

    public static class DialogBuilder extends BaseDialogBuilder<DataTimeDialogFragment,
                    DialogDatepickerBinding, DialogBuilder> {
        private IPositiveButtonDialogListener mPositiveButtonListener;
        private INegativeButtonListener mNegativeButtonListener;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, DataTimeDialogFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }

        public DialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }

        public DialogBuilder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }
        public DataTimeDialogFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public DataTimeDialogFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public DataTimeDialogFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public DataTimeDialogFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }
        public DialogBuilder setPositiveListener(final IPositiveButtonDialogListener listener) {
            mPositiveButtonListener = listener;
            return this;
        }


        public DialogBuilder setNegativeListener(final INegativeButtonListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }

    }
}
