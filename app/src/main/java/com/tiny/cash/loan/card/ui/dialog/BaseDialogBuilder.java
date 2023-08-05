package com.tiny.cash.loan.card.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.tiny.cash.loan.card.ui.listener.ICancelDialogListener;


/**
 * Internal base builder that holds common values for all dialog fragment builders.
 *
 * @author Tomas Vondracek
 */
public abstract class BaseDialogBuilder<F extends BaseDialogFragment, Binding extends ViewDataBinding, T extends BaseDialogBuilder<F, Binding, T>> {

    public final static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
    public final static String DEFAULT_TAG = "simple_dialog";
    protected final Context mContext;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;
    public ICancelDialogListener cancelDialogListener;
    private String mTag = DEFAULT_TAG;
    private boolean mCancelable = true;
    private boolean mCancelableOnTouchOutside = true;
    private Binding binding;

    public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        mFragmentManager = fragmentManager;
        mContext = context.getApplicationContext();
        mClass = clazz;
    }

    public void bindView(LayoutInflater inflater, ViewGroup container, @LayoutRes int layoutID) {
        binding = DataBindingUtil.inflate(inflater, layoutID, container, false);
    }

    public Binding getBinding() {
        return binding;
    }

    protected abstract T self();


    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return self();
    }

    public T setCancelDialogListener(ICancelDialogListener listener) {
        cancelDialogListener = listener;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            mCancelable = cancelable;
        }
        return self();
    }

    public T setTag(String tag) {
        mTag = tag;
        return self();
    }

    private F create() {
        final Bundle args = prepareArguments();
        args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);
        final F fragment = (F) Fragment.instantiate(mContext, mClass.getName(), args);
        fragment.setBuilder(this);

        fragment.setCancelable(mCancelable);
        return fragment;
    }

    protected Bundle prepareArguments() {
        return new Bundle();
    }

    public F show() {
        F fragment = create();
        try {
            fragment.showAllowingStateLoss(mFragmentManager, mTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

    /**
     * Like show() but allows the commit to be executed after an activity's state is saved. This
     * is dangerous because the commit can be lost if the activity needs to later be restored from
     * its state, so this should only be used for cases where it is okay for the UI state to change
     * unexpectedly on the user.
     */
    public F showAllowingStateLoss() {
        F fragment = create();
        try {
            fragment.showAllowingStateLoss(mFragmentManager, mTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
