package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogPaymentStatusBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonDialogListener;

import androidx.fragment.app.FragmentManager;


public class PayStackStatusDialogFragment extends BaseDialogFragment<PayStackStatusDialogFragment.DialogBuilder> {
    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_payment_status;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use DownloadProgressDialogBuilder to construct this dialog");
        }
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                //去除系统自带的margin
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //设置dialog在界面中的属性
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Override
    protected void build(DialogBuilder builder) {
        DialogPaymentStatusBinding mBinding = builder.getBinding();
        String mStatus = builder.mStatus;
        if ("success".equals(mStatus)){
            mBinding.tvStatusTitle.setText("Payment Success");
            mBinding.tvDesc.setText("Your payment is complete, please check the notification");
        }else {
            mBinding.tvStatusTitle.setText("Payment Failed");
            mBinding.tvDesc.setText("Your payment is failed, try again");
        }
        mBinding.btnGoHome.setOnClickListener(v -> {
            builder.mNegativeButtonListener.onNegativeButtonClicked();
            dismiss();
        });

    }

    public static class DialogBuilder extends BaseDialogBuilder<PayStackStatusDialogFragment, DialogPaymentStatusBinding, DialogBuilder> {

        private INegativeButtonDialogListener mNegativeButtonListener;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private String mStatus;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, PayStackStatusDialogFragment.class);
        }
        public PayStackStatusDialogFragment.DialogBuilder setStatus(String status) {
            mStatus = status;
            return this;
        }
        @Override
        protected DialogBuilder self() {
            return this;
        }


        public PayStackStatusDialogFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public PayStackStatusDialogFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public PayStackStatusDialogFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public PayStackStatusDialogFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public PayStackStatusDialogFragment.DialogBuilder setNegativeListener(final INegativeButtonDialogListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }
    }
}
