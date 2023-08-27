package com.tiny.cash.loan.card.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.data.FirebaseData;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.dialog.fragment.ProgressDialogFragment;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tiny.cash.loan.card.message.EventMessage;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseFragment extends Fragment {


    private AppCompatActivity mActivity;

    public AppCompatActivity getAppCompatActivity() {
        return mActivity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof AppCompatActivity)) {
            throw new IllegalStateException(getClass().getSimpleName() + " must be attached to a AppCompatActivity.");
        }
        mActivity = (AppCompatActivity) activity;

        super.onAttach(activity);
    }

    public void startIntent(Class c) {
        Intent intent = new Intent(mActivity, c);
        startActivity(intent);
    }

    //    @Nullable
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if (enter) {
//            return AnimationUtils.loadAnimation(mActivity, R.anim.fragment_slide_left_enter);
//        } else {
//            return super.onCreateAnimation(transit,false,nextAnim);
//        }
//    }
    NetObserver mObserver;

    public void queryCommonBo(String type) {
        List<String> mList = new ArrayList<>();

        Observable observable = NetManager.getApiService().queryCommon(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);
        mObserver = new NetObserver<Response<Common>>() {
            @Override
            public void onNext(Response<Common> response) {
                if (response.isSuccess()) {
                    switch (type) {
                        case Constants.EDUCATION:
                            List<Common.DictMapBean.Education> eduList = response.getBody().getDictMap().getEducation();
                            for (Common.DictMapBean.Education item : eduList) {
                                mList.add(item.getVal());
                            }
                            EventBus.getDefault().post(new EventMessage(EventMessage.EDU, response.getBody()));
                            break;
                        case Constants.SALARY:

                            EventBus.getDefault().post(new EventMessage(EventMessage.SALARY, response.getBody()));
                            break;

                        case Constants.MARITAL:
                            EventBus.getDefault().post(new EventMessage(EventMessage.MARITAL, response.getBody()));
                            break;

                        case Constants.RELATIONSHIP:
                            EventBus.getDefault().post(new EventMessage(EventMessage.RELATIONSHIP, response.getBody()));
                            break;

                        case Constants.WORK:
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK, response.getBody()));
                            break;

                        case Constants.STATE:
                            List<Common.DictMapBean.State> stateList = response.getBody().getDictMap().getState();
                            for (Common.DictMapBean.State item : stateList) {
                                mList.add(item.getVal());
                            }
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK, mList));
                            break;

                        case Constants.AREA:
                            List<Common.DictMapBean.Area> areaList = response.getBody().getDictMap().getArea();
                            for (Common.DictMapBean.Area item : areaList) {
                                mList.add(item.getVal());
                            }
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK, mList));
                            break;
                    }
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(mObserver);
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
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    boolean registerRoot = false;

    protected void registerEventBus() {
        if (!registerRoot) {
            registerRoot = true;
            EventBus.getDefault().register(this);
        }
    }

    protected void unRegisterEventBus() {
        if (registerRoot) {
            registerRoot = false;
            EventBus.getDefault().unregister(this);
        }
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

    public OnBackListener onBackListener;

    public void setOnBackListener(OnBackListener listener) {
        this.onBackListener = listener;
    }

    public interface OnBackListener {
        void onBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setOnBackListener(null);

    }

    public static String KEY_FIREBASE_DATA = "key_firebase_data";

    public boolean checkNeedShowLog(String orderId) {
        if (orderId == null) {
            return false;
        }
        String dataStr = SPUtils.getInstance().getString(KEY_FIREBASE_DATA);
        if (!TextUtils.isEmpty(dataStr)) {
            FirebaseData firebaseData = GsonUtils.fromJson(dataStr, FirebaseData.class);
            if (firebaseData != null) {
                if (TextUtils.equals(firebaseData.getOrderId(), orderId)) {
                    if (firebaseData.getStatus() == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
