package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogPaymentTransferBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonDialogListener;
import com.tiny.cash.loan.card.ui.dialog.iface.IPositiveButtonDialogListener;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import androidx.fragment.app.FragmentManager;


public class OfflinePaymentTransferFragment extends BaseDialogFragment<OfflinePaymentTransferFragment.DialogBuilder> {

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_payment_transfer;
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
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void build(DialogBuilder builder) {
        final DialogPaymentTransferBinding binding = builder.getBinding();

        String mBank = KvStorage.get(LocalConfig.LC_BANK, "");

        String mName = KvStorage.get(LocalConfig.LC_ACCOUNTNAME, "");
        String mNumber = KvStorage.get(LocalConfig.LC_ACCOUNTNUMBER, "");

        binding.tvAccountName.setText(mName);
        binding.tvAccountNumber.setText(mNumber);
        binding.tvAccountBank.setText(mBank);
        binding.ivShutdown.setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });

        binding.tvCopy.setOnClickListener(v->{
            ToastManager.show(requireContext(), "Copied");
            AppUtils.copy(mNumber,getContext());
        });
    }


    public static class DialogBuilder extends BaseDialogBuilder<OfflinePaymentTransferFragment, DialogPaymentTransferBinding, DialogBuilder> {

        private INegativeButtonDialogListener mNegativeButtonListener;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        IPositiveButtonDialogListener mPositiveButtonListener;
        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, OfflinePaymentTransferFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }

        public DialogBuilder setMessage(String messageResourceId) {
            mMessage = messageResourceId;
            return this;
        }

        public OfflinePaymentTransferFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public OfflinePaymentTransferFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public OfflinePaymentTransferFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public OfflinePaymentTransferFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public OfflinePaymentTransferFragment.DialogBuilder setNegativeListener(final INegativeButtonDialogListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }
        public OfflinePaymentTransferFragment.DialogBuilder setPositiveListener(final IPositiveButtonDialogListener listener) {
            mPositiveButtonListener = listener;
            return this;
        }
    }
}
