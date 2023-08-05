package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.ProgressDialogBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;

import androidx.fragment.app.FragmentManager;


public class ProgressDialogFragment extends BaseDialogFragment<ProgressDialogFragment.DialogBuilder> {

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.progress_dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use DownloadProgressDialogBuilder to construct this dialog");
        }
    }

    @Override
    protected void build(DialogBuilder builder) {
        final ProgressDialogBinding binding = builder.getBinding();
        if (!TextUtils.isEmpty(builder.mMessage)) {
            binding.tvLoginDialog.setText(builder.mMessage);
        }

    }


    public static class DialogBuilder extends BaseDialogBuilder<ProgressDialogFragment, ProgressDialogBinding, DialogBuilder> {

        private CharSequence mMessage;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, ProgressDialogFragment.class);
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
    }
}
