package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogFragmentAgreementBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonDialogListener;
import com.tiny.cash.loan.card.ui.dialog.iface.IPositiveButtonDialogListener;

import androidx.fragment.app.FragmentManager;


public class AgreeTermsFragment extends BaseDialogFragment<AgreeTermsFragment.DialogBuilder> {

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_fragment_agreement;
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
        final DialogFragmentAgreementBinding binding = builder.getBinding();
        if (builder.mMessage.equals(Constants.ONE)){
            binding.llButton.setVisibility(View.VISIBLE);
        }else {
            binding.llButton.setVisibility(View.GONE);
        }

        binding.wvAgreementDisplay.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        binding.wvAgreementDisplay.loadUrl(Constants.ICREDIT_AGREE_URL);

        binding.btnCancel.setOnClickListener(v -> {
            dismissAllowingStateLoss();
            builder.mPositiveButtonListener.onPositiveButtonClicked();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            dismissAllowingStateLoss();
           builder.mNegativeButtonListener.onNegativeButtonClicked();
        });
    }


    public static class DialogBuilder extends BaseDialogBuilder<AgreeTermsFragment, DialogFragmentAgreementBinding, DialogBuilder> {

        private INegativeButtonDialogListener mNegativeButtonListener;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        IPositiveButtonDialogListener mPositiveButtonListener;
        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, AgreeTermsFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }

        public DialogBuilder setMessage(String messageResourceId) {
            mMessage = messageResourceId;
            return this;
        }

        public AgreeTermsFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public AgreeTermsFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public AgreeTermsFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public AgreeTermsFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public AgreeTermsFragment.DialogBuilder setNegativeListener(final INegativeButtonDialogListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }
        public AgreeTermsFragment.DialogBuilder setPositiveListener(final IPositiveButtonDialogListener listener) {
            mPositiveButtonListener = listener;
            return this;
        }
    }
}
