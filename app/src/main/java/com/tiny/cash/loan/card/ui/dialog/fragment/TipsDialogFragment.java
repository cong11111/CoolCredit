package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogTipsBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.ICenterButtonDialogListener;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonDialogListener;
import com.tiny.cash.loan.card.ui.dialog.iface.IPositiveButtonDialogListener;

import androidx.fragment.app.FragmentManager;


public class TipsDialogFragment extends BaseDialogFragment<TipsDialogFragment.DialogBuilder> {

    DialogTipsBinding binding;

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    protected void build(DialogBuilder builder) {
        binding = builder.getBinding();
        CharSequence title = builder.mTitle;
        CharSequence message = builder.mMessage;
        CharSequence message2 = builder.mMessage2;
        CharSequence messageTitle = builder.mMessageTitle;
        CharSequence positiveTxt = builder.mPositiveButtonText;
        CharSequence negativeTxt = builder.mNegativeButtonText;
        CharSequence centerTxt = builder.mCenterButtonText;

        if (TextUtils.isEmpty(title)) {
            binding.layoutTitle.tvTitle.setText("");
        } else {
            binding.layoutTitle.tvTitle.setText(title);
        }

        if (TextUtils.isEmpty(message)) {
            binding.tvMessage.setVisibility(View.GONE);
        } else {
            binding.tvMessage.setGravity(builder.mMessageGravity);
            binding.tvMessage.setText(message);
            binding.tvMessage.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(message2)) {
            binding.tvMessage2.setVisibility(View.GONE);
        } else {
            binding.tvMessage2.setText(message2);
            binding.tvMessage2.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(messageTitle)) {
            binding.tvMessageTitle.setVisibility(View.GONE);
        } else {
            binding.tvMessageTitle.setGravity(builder.mMessageGravity);
            binding.tvMessageTitle.setText(messageTitle);
            binding.tvMessageTitle.setVisibility(View.VISIBLE);
        }

//        binding.layoutTitle.ivClose.setOnClickListener(v -> {
//            dismissAllowingStateLoss();
//            if (builder.cancelDialogListener != null) {
//                builder.cancelDialogListener.onCancel();
//            }
//        });

        if (TextUtils.isEmpty(positiveTxt) && TextUtils.isEmpty(negativeTxt) && TextUtils.isEmpty(centerTxt)) {
            binding.layoutBottom.layoutButtons.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(positiveTxt)) {
                binding.layoutBottom.tvPositive.setText(positiveTxt);
                binding.layoutBottom.tvPositive.setVisibility(View.VISIBLE);
                binding.layoutBottom.layoutButtons.setVisibility(View.VISIBLE);
                binding.layoutBottom.tvPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (builder.mPositiveButtonListener != null) {
                            builder.mPositiveButtonListener.onPositiveButtonClicked();
                        }
                        dismiss();
                    }
                });

            } else {
                binding.layoutBottom.tvPositive.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(negativeTxt)) {
                binding.layoutBottom.tvNegative.setText(negativeTxt);
                binding.layoutBottom.tvNegative.setVisibility(View.VISIBLE);
                binding.layoutBottom.layoutButtons.setVisibility(View.VISIBLE);
                binding.layoutBottom.tvNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (builder.mNegativeButtonListener != null) {
                            builder.mNegativeButtonListener.onNegativeButtonClicked();
                        }
                        dismiss();
                    }
                });
            } else {
                binding.layoutBottom.tvNegative.setVisibility(View.GONE);
            }
//            if (!TextUtils.isEmpty(centerTxt)) {
//                binding.layoutBottom.tvCenterSave.setText(centerTxt);
//                binding.layoutBottom.tvCenterSave.setVisibility(View.VISIBLE);
//                binding.layoutBottom.layoutButtons.setVisibility(View.VISIBLE);
//                binding.layoutBottom.tvCenterSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (builder.mCenterButtonListener != null) {
//                            builder.mCenterButtonListener.onCenterButtonClicked();
//                        }
//                        dismiss();
//                    }
//                });
//            } else {
//                binding.layoutBottom.tvCenterSave.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_tips;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use MusicListDialogBuilder to construct this dialog");
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        DialogBuilder builder = getBuilder();
        if (builder != null) {
            builder.setPositiveListener(null).setNegativeListener(null).setCancelDialogListener(null);

        }
    }

    public static class DialogBuilder extends BaseDialogBuilder<TipsDialogFragment, DialogTipsBinding, DialogBuilder> {

        private CharSequence mTitle;
        private CharSequence mMessage,mMessage2;
        private CharSequence mMessageTitle;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private CharSequence mCenterButtonText;

        private int mMessageGravity = Gravity.LEFT;

        private IPositiveButtonDialogListener mPositiveButtonListener;
        private INegativeButtonDialogListener mNegativeButtonListener;
        private ICenterButtonDialogListener mCenterButtonListener;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, TipsDialogFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }

        public DialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public DialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public DialogBuilder setMessageTitle(CharSequence message) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMessageTitle = Html.fromHtml(Html.toHtml(
                        new SpannedString(message), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
                        , Html.FROM_HTML_MODE_LEGACY);
            } else {
                mMessageTitle = Html.fromHtml(Html.toHtml(new SpannedString(message)));
            }
            return this;
        }

        public DialogBuilder setMessageTitle(int messageResourceId) {
            mMessageTitle = mContext.getString(messageResourceId);
            return this;
        }

        public DialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }
        public DialogBuilder setMessage2(CharSequence message) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMessage2 = Html.fromHtml(Html.toHtml(
                        new SpannedString(message), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
                        , Html.FROM_HTML_MODE_LEGACY);
            } else {
                mMessage2 = Html.fromHtml(Html.toHtml(new SpannedString(message)));
            }
            return this;
        }

        /**
         * Allow to set resource string with HTML formatting and bind %s,%i.
         * This is workaround for https://code.google.com/p/android/issues/detail?id=2923
         */
        public DialogBuilder setMessage(CharSequence message, Object... formatArgs) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMessage = Html.fromHtml(String.format(Html.toHtml(
                        new SpannedString(message), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE), formatArgs)
                        , Html.FROM_HTML_MODE_LEGACY);
            } else {
                mMessage = Html.fromHtml(String.format(Html.toHtml(new SpannedString(message)), formatArgs));
            }

            return this;
        }

        public DialogBuilder setMessage(CharSequence message) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mMessage = Html.fromHtml(Html.toHtml(
                        new SpannedString(message), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
                        , Html.FROM_HTML_MODE_LEGACY);
            } else {
                mMessage = Html.fromHtml(Html.toHtml(new SpannedString(message)));
            }
            return this;
        }

        public DialogBuilder setMessageGravity(int gravity) {
            mMessageGravity = gravity;
            return this;
        }

        public DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public DialogBuilder setPositiveButton(int textId, final IPositiveButtonDialogListener listener) {
            return setPositiveButton(mContext.getText(textId), listener);
        }

        public DialogBuilder setPositiveButton(CharSequence text, final IPositiveButtonDialogListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public DialogBuilder setPositiveListener(final IPositiveButtonDialogListener listener) {
            mPositiveButtonListener = listener;
            return this;
        }

        public DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public DialogBuilder setNegativeButton(int textId, final INegativeButtonDialogListener listener) {
            return setNegativeButton(mContext.getText(textId), listener);
        }

        public DialogBuilder setNegativeButton(CharSequence text, final INegativeButtonDialogListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public DialogBuilder setNegativeListener(final INegativeButtonDialogListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }

        public DialogBuilder setCenterButtonTxt(int textId) {
            return setCenterButtonTxt(mContext.getText(textId));
        }

        public DialogBuilder setCenterButtonTxt(CharSequence text) {
            mCenterButtonText = text;
            return this;
        }

        public DialogBuilder setCenterButton(int textId, final ICenterButtonDialogListener listener) {
            return setCenterButton(mContext.getText(textId), listener);
        }

        public DialogBuilder setCenterButton(CharSequence text, final ICenterButtonDialogListener listener) {
            mCenterButtonText = text;
            mCenterButtonListener = listener;
            return this;
        }

        public DialogBuilder setCenterListener(final ICenterButtonDialogListener listener) {
            mCenterButtonListener = listener;
            return this;
        }


    }
}
