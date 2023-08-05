package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogAppstarsBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonListener;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import androidx.fragment.app.FragmentManager;


public class AppStarsDialogFragment extends BaseDialogFragment<AppStarsDialogFragment.DialogBuilder> {
    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_appstars;
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
        DialogAppstarsBinding mBinding = builder.getBinding();
        String fiveStarTitle = KvStorage.get(LocalConfig.LC_FIVESTARTITLE, "");
        String fiveStarContent = KvStorage.get(LocalConfig.LC_FIVESTARCONTENT, "");
//        mBinding.tvTitle.setText(fiveStarTitle);
        mBinding.tvContent.setText(fiveStarContent);
        mBinding.btnCancel.setOnClickListener(v->{
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS2),false);
            dismissAllowingStateLoss();
        });
        mBinding.btnSubmit.setOnClickListener(v -> {
            builder.mNegativeButtonListener.onNegativeButtonClicked(Constants.ONE);
        });
    }

    public static class DialogBuilder extends BaseDialogBuilder<AppStarsDialogFragment, DialogAppstarsBinding, DialogBuilder> {

        private INegativeButtonListener mNegativeButtonListener;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, AppStarsDialogFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }


        public AppStarsDialogFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public AppStarsDialogFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public AppStarsDialogFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public AppStarsDialogFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public AppStarsDialogFragment.DialogBuilder setNegativeListener(final INegativeButtonListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }

    }
}
