package com.tiny.cash.loan.card.feature.bank;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityBankAccountBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankNameList;
import com.tiny.cash.loan.card.net.response.data.bean.BankResult;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.server.ApiServerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by WANG on 2020/10/11.
 */

public class BankCardAccountActivity extends BaseActivity {

    private ActivityBankAccountBinding mBinding;
    HashMap<String, String> keyMap = new HashMap<>();
    List<String> valueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBankAccountBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_bank_account);
        initData();
//        UpLoadUserInfo();
    }

    private void initData() {
        mBinding.llTitle.tvTitle.setText("Bank Account");
        mBinding.llTitle.ivBack.setOnClickListener(v -> {
            finish();
        });
        queryBankList();
        mBinding.btnSubmit.setOnClickListener(view -> {
            submitBankAccount();
        });
    }

    private void initView(List<BankNameList.BankList> bodyBeanList) {
        for (BankNameList.BankList item : bodyBeanList) {
            valueList.add(item.getBankName());
            keyMap.put(item.getBankName(), item.getBankCode());
        }
        mBinding.niceSpinner.attachDataSource(valueList);
    }

    private NetObserver bankListObserver, bankAccountObserver;

    private void submitBankAccount() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String bankName = mBinding.niceSpinner.getText().toString();
        String bankCode = keyMap.get(bankName);
        String mBvn = mBinding.etBvn.getText().toString();
        String bankAcountNumber = mBinding.edtBankAcountNumber.getText().toString();
        if (TextUtils.isEmpty(bankAcountNumber)) {
            ToastManager.show(this, "This not null");
            return;
        }
        if (mBinding.etBvn.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mBvn)) {
                ToastManager.show(this, "This not null");
                return;
            }
            if (mBvn.length() != 11) {
                ToastManager.show(this, " please enter your correct bvn number");
                return;
            }
        }
        Observable observable = NetManager.getApiService().checkBankaccount(accountId, bankCode, bankName, bankAcountNumber, mBvn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bankAccountObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        bankAccountObserver = new NetObserver<Response<BankResult>>() {
            @Override
            public void onNext(Response<BankResult> response) {
                dismissProgressDialogFragment();
                int code = response.getStatus().getCode();
                if (code == ApiServerImpl.FAILE) {
                    if (response.getBody() != null && !response.getBody().isBvnChecked()) {
                        mBinding.etBvn.setVisibility(View.VISIBLE);
                        showToast(response.getStatus().getMsg());
                    }
                } else if (code == ApiServerImpl.OK) {
                    success(response);
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(bankAccountObserver);

    }

    private void success(Response<BankResult> response) {
        if (response.getBody().isBankAccountChecked()) {
            FirebaseLogUtils.Log("af_add_bankaccount");
            startIntent(AddMoreBankCardActivity.class);
            finish();
        } else {
            if (StringUtils.isEmpty(response.getBody().getAccountMessage())) {
                showToast("Verification failed, please enter the correct Account information");
            } else {
                showToast(response.getStatus().getMsg());
            }
        }
    }

    private void queryBankList() {
        Observable observable = NetManager.getApiService().queryBankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bankListObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        bankListObserver = new NetObserver<Response<BankNameList>>() {
            @Override
            public void onNext(Response<BankNameList> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    initView(response.getBody().getBankList());
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(bankListObserver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(bankListObserver);
        CommonUtils.disposable(bankAccountObserver);
    }
}
