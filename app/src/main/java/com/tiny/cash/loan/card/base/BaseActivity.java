package com.tiny.cash.loan.card.base;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.githang.statusbar.StatusBarCompat;
import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.collect.EncodeUtils;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.bean.ContactInfo;
import com.tiny.cash.loan.card.ui.bean.InstallPackageInfo;
import com.tiny.cash.loan.card.ui.bean.SmsInfo;
import com.tiny.cash.loan.card.ui.dialog.fragment.ProgressDialogFragment;
import com.tiny.cash.loan.card.utils.ActivityCollector;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.DeviceInfo;
import com.tiny.cash.loan.card.utils.GPSUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.NetworkUtil;
import com.tiny.cash.loan.card.utils.PhoneInfoContent;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.AuthResult;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.data.bean.FcmTokenResult;
import com.tiny.cash.loan.card.net.response.data.bean.StateAreaMap;
import com.tiny.cash.loan.card.net.request.params.AuthParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class BaseActivity extends AppCompatActivity {
    boolean isUpLoadUserInfo = false;
    boolean isUpLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.hasLollipop()) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.color_ff3865cb));
        }
        ActivityCollector.addActivity(this);
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(fcmtokenObserver);
        CommonUtils.disposable(mStateObserver);
        CommonUtils.disposable(mObserver);
        ActivityCollector.removeActivity(this);
    }

    public void startIntent(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    /**
     * 谷歌FCM上报token
     */
    public void upLoadFcmtoken(){
        String mFcmToken = KvStorage.get(LocalConfig.LC_FBTOKEN, "");
        Observable observable = NetManager.getApiService().uploadFcmToken(mFcmToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(fcmtokenObserver);
        fcmtokenObserver = new NetObserver<Response<FcmTokenResult>>() {
            @Override
            public void onNext(Response<FcmTokenResult> response) {
                if (response.isSuccess()) {
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(fcmtokenObserver);
    }
    NetObserver mObserver,fcmtokenObserver,mStateObserver;

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
                            EventBus.getDefault().post(new EventMessage(EventMessage.EDU,response.getBody()));
                            break;
                        case Constants.SALARY:

                            EventBus.getDefault().post(new EventMessage(EventMessage.SALARY,response.getBody()));
                            break;

                        case Constants.MARITAL:
                            EventBus.getDefault().post(new EventMessage(EventMessage.MARITAL,response.getBody()));
                            break;

                        case Constants.RELATIONSHIP:
                            EventBus.getDefault().post(new EventMessage(EventMessage.RELATIONSHIP,response.getBody()));
                            break;

                        case Constants.WORK:
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK,response.getBody()));
                            break;

                        case Constants.STATE:
                            List<Common.DictMapBean.State> stateList = response.getBody().getDictMap().getState();
                            for (Common.DictMapBean.State item : stateList) {
                                mList.add(item.getVal());
                            }
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK,mList));
                            break;

                        case Constants.AREA:
                            List<Common.DictMapBean.Area> areaList = response.getBody().getDictMap().getArea();
                            for (Common.DictMapBean.Area item : areaList) {
                                mList.add(item.getVal());
                            }
                            EventBus.getDefault().post(new EventMessage(EventMessage.WORK,mList));
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


    public void queryStateArea() {
        Observable observable = NetManager.getApiService().queryStateArea(Constants.STATEAREA)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);
        mStateObserver = new NetObserver<Response<StateAreaMap>>() {
            @Override
            public void onNext(Response<StateAreaMap> response) {
                if (response.isSuccess()) {
                    EventBus.getDefault().post(new EventMessage(EventMessage.STATEAREA,response.getBody()));
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {

            }
        };
        observable.subscribeWith(mStateObserver);
    }
    ProgressDialogFragment progressDialogFragment;

    public void showProgressDialogFragment(String message, boolean cancelable) {
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.str_loading);
        }
        dismissProgressDialogFragment();
        progressDialogFragment = ProgressDialogFragment.createBuilder(this,
                getSupportFragmentManager()).setCancelable(cancelable).setMessage(message).show();

    }


    public void dismissProgressDialogFragment() {
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
        }
    }


    protected Toast sToast = null;

    protected void showToast(@LayoutRes int toastLayoutID, int duration) {
        View v = LayoutInflater.from(getApplicationContext()).inflate(toastLayoutID, null, false);
        sToast = new Toast(getApplicationContext());
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.setView(v);

        sToast.setDuration(duration);
        sToast.show();
    }

    protected void showToast(String msg) {
        showToast(msg, 500);
    }

    protected void showToast(String info, int duration) {
        View v =
                LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_validate_recive_layout, null, false);
        sToast = new Toast(getApplicationContext());
        sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.setView(v);

        ((TextView) sToast.getView().findViewById(R.id.tv_login_dialog)).setText(info);
        sToast.setDuration(duration);
        sToast.show();
    }

}