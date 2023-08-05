package com.tiny.cash.loan.card.feature.menu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentBankAccountBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankDetail;
import com.tiny.cash.loan.card.net.response.data.bean.BankNameList;
import com.tiny.cash.loan.card.net.response.data.bean.BankResult;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.server.ApiServerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BankAccountFragment extends BaseFragment {
    private FragmentBankAccountBinding mBinding;
    HashMap<String, String> keyMap = new HashMap<>();
    List<String> valueList = new ArrayList<>();

    public static BankAccountFragment newInstance() {
        Bundle args = new Bundle();
        BankAccountFragment fragment = new BankAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentBankAccountBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.fragment_bank_account, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initSwitch() {
        mBinding.sV.setChecked(false);
        mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
        mBinding.sV.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            //控制开关字体颜色
            if (b) {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_true);
                mBinding.llAddBankAccount.setVisibility(View.VISIBLE);
                mBinding.llBankAccountResult.setVisibility(View.GONE);
            } else {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
                mBinding.llAddBankAccount.setVisibility(View.GONE);
                mBinding.llBankAccountResult.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        initSwitch();
        queryBankList();
        QueryBankDetail();
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
            ToastManager.show(getContext(), "This not null");
            return;
        }
        if (mBinding.etBvn.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(mBvn)) {
                ToastManager.show(getContext(), "This not null");
                return;
            }
            if (mBvn.length() != 11) {
                ToastManager.show(getContext(), " please enter your correct bvn number");
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
            QueryBankDetail();
        } else {
            if (StringUtils.isEmpty(response.getBody().getAccountMessage())) {
                showToast("Verification failed, please enter the correct account information");
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
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(bankListObserver);
        CommonUtils.disposable(bankAccountObserver);
        CommonUtils.disposable(bankObserver);
    }

    NetObserver bankObserver;

    private void QueryBankDetail() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().queryBankDetail(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bankObserver);
        bankObserver = new NetObserver<Response<BankDetail>>() {
            @Override
            public void onNext(Response<BankDetail> bankDetailResponse) {
                if (bankDetailResponse.getBody() != null) {
                    if (bankDetailResponse.getBody().isEdit()) {
                        mBinding.rlSwitch.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.rlSwitch.setVisibility(View.GONE);
                    }
                    BankDetail.BankdetailBean bankdetail = bankDetailResponse.getBody().getBankdetail();
                    if (bankdetail != null) {
                        mBinding.llAddBankAccount.setVisibility(View.GONE);
                        mBinding.llBankAccountResult.setVisibility(View.VISIBLE);
                        mBinding.tvBankName.setText(bankdetail.getBankName());
                        mBinding.tvBankAcountNumber.setText(bankdetail.getBankAccountNumber());
//                    mBinding.tvPhoneNumber.setText(bankdetail.getBankName());
                    }
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(bankObserver);
    }


}
