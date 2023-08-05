/*
 * Copyright 2013 Inmite s.r.o. (www.inmite.eu).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tiny.cash.loan.card.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.dialog.fragment.ProgressDialogFragment;
import com.tiny.cash.loan.card.ui.listener.WindowTraceCallback;
import com.tiny.cash.loan.card.utils.Utils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Base dialog fragment for all your dialogs, styleable and same design on Android 2.2+.
 *
 * @author David Vávra (david@inmite.eu)
 */
public abstract class BaseDialogFragment<B extends BaseDialogBuilder> extends DialogFragment {

    B builder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int theme = resolveTheme();
        Dialog dialog = new Dialog(getActivity(), theme);

        Bundle args = getArguments();
        if (args != null) {
            dialog.setCanceledOnTouchOutside(
                    args.getBoolean(BaseDialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE));
        }
        return dialog;
    }

    ProgressDialogFragment progressDialogFragment;

    public void showProgressDialogFragment(String message, boolean cancelable) {
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.str_loading);
        }
        dismissProgressDialogFragment();
        progressDialogFragment = ProgressDialogFragment.createBuilder(getContext(),
                getFragmentManager()).setCancelable(cancelable).setMessage(message).show();

    }


    public void dismissProgressDialogFragment() {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (builder != null) {
            builder.bindView(inflater, container, getDialogLayout());
            build(builder);
            return builder.getBinding().getRoot();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowTraceCallback traceCallback = new WindowTraceCallback(window.getCallback());
                window.setCallback(traceCallback);
            }
        }
    }

    public B getBuilder() {
        return builder;
    }

    public void setBuilder(B baseDialogBuilder) {
        builder = baseDialogBuilder;
    }

    protected abstract void build(B builder);

    @LayoutRes
    protected abstract int getDialogLayout();

    @Override
    public void onDestroyView() {
        // bug in the compatibility library
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (builder != null && builder.cancelDialogListener != null) {
            builder.cancelDialogListener.onCancel();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (builder != null) {
            builder.setCancelDialogListener(null);
        }
    }

    /**
     * Resolves the theme to be used for the dialog.
     *
     * @return The theme.
     */
    @StyleRes
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    protected void showToast(@StringRes int strId) {
        showToast(getString(strId));
    }

    protected void showToast(@StringRes int strId, int showTime) {
        showToast(getString(strId), showTime);
    }


    protected void showToast(String msg) {
        showToast(msg, 500);
    }

    protected void showToast(String msg, int showTime) {
        Activity activity = getActivity();
        if (activity != null) {
            Utils.showToast(activity, msg, showTime);
        }
    }


}