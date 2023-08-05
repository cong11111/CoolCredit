package com.tiny.cash.loan.card.feature.menu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentBasicinfoBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.UserProfileDetail;
import com.tiny.cash.loan.card.net.response.Response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BasicInfoFragment extends BaseFragment {
    private FragmentBasicinfoBinding mBinding;

    public static BasicInfoFragment newInstance() {
        Bundle args = new Bundle();
        BasicInfoFragment fragment = new BasicInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basicinfo, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData(){
        queryProfileDetail();
    }

    private void initView(UserProfileDetail profileDetailBO){
        mBinding.tvFirstName.setText(profileDetailBO.getFirstName());
        mBinding.tvMiddleName.setText(profileDetailBO.getMiddleName());
        mBinding.tvLastName.setText(profileDetailBO.getLastName());
        mBinding.tvGender.setText(profileDetailBO.getGender()==1 ? getString(R.string.str_male):getString(R.string.str_female));
        mBinding.tvDateTime.setText(profileDetailBO.getBirthday());
        mBinding.tvBvn.setText(profileDetailBO.getBvn());
        mBinding.tvPersonalEmail.setText(profileDetailBO.getEmail());
        mBinding.tvAddress.setText(profileDetailBO.getHomeStateLabel()+profileDetailBO.getHomeAreaLabel());
        mBinding.tvStreetNumber.setText(profileDetailBO.getHomeAddress());
    }

    NetObserver<Response<UserProfileDetail>> profileDetailObserver;
    private void queryProfileDetail() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID,"");
        Observable observable =
                NetManager.getApiService().profileDetail(accountId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(profileDetailObserver);
        showProgressDialogFragment(getString(R.string.str_loading),false);
        profileDetailObserver = new NetObserver<Response<UserProfileDetail>>() {
            @Override
            public void onNext(Response<UserProfileDetail> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody()!= null) {
                        initView(response.getBody());
                    }
                } else {
                    String msg = response.getStatus().getMsg();
                    if (!TextUtils.isEmpty(msg)) {
                        showToast(msg);
                    } else {
                        showToast("Error");
                    }
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(profileDetailObserver);
    }

    @Override
    public void onDestroy() {
        CommonUtils.disposable(profileDetailObserver);
        super.onDestroy();
    }
}
