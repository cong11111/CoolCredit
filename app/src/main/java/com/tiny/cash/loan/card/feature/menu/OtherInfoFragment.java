package com.tiny.cash.loan.card.feature.menu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentOtherDetailsBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.data.bean.UserAddressDetail;
import com.tiny.cash.loan.card.net.response.data.bean.UserProfile;
import com.tiny.cash.loan.card.net.request.params.OtherInfoParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import co.paystack.android.utils.StringUtils;
import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OtherInfoFragment extends BaseFragment implements View.OnClickListener{
    private FragmentOtherDetailsBinding mBinding;
    int mCont = 0;
    private List<Common.DictMapBean.Marital> marital;
    public static OtherInfoFragment newInstance() {
        Bundle args = new Bundle();
        OtherInfoFragment fragment = new OtherInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_details, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initAddData();
    }
    private void initData() {
        initSwitch();
        queryAddressDetail();
    }


    private void initSwitch(){
        mBinding.sV.setChecked(false);
        mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
        mBinding.sV.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b)-> {
            //控制开关字体颜色
            if (b) {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_true);
                mBinding.llAddWorkmsg.setVisibility(View.VISIBLE);
                mBinding.llShowWorkMsg.setVisibility(View.GONE);
            } else {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
                mBinding.llAddWorkmsg.setVisibility(View.GONE);
                mBinding.llShowWorkMsg.setVisibility(View.VISIBLE);
            }
        });
    }
    private void initShowView(UserAddressDetail addressDetailBO) {
        mBinding.llShowWorkMsg.setVisibility(View.VISIBLE);
        mBinding.llAddWorkmsg.setVisibility(View.GONE);
        if (addressDetailBO.isEdit()){
            mBinding.rlSwitch.setVisibility(View.VISIBLE);
        }else {
            mBinding.rlSwitch.setVisibility(View.GONE);
        }
        mBinding.tvNumberOfSpouses.setText(addressDetailBO.getMaritalLabel());
        mBinding.tvForeignDebt.setText(addressDetailBO.getHasOutstandingLoan() == 1 ? "Yes" : "No");
        mBinding.tvGrossSalary.setText(addressDetailBO.getSalaryLabel());
        mBinding.tvCompanyName.setText(addressDetailBO.getCompanyName());
        mBinding.tvCompanyAddress.setText(addressDetailBO.getCompanyAddress());
        mBinding.tvWorkingStatus.setText(addressDetailBO.getWorkLabel());
        if (StringUtils.isEmpty(addressDetailBO.getEducation())) {
            mBinding.llEducation.setVisibility(View.GONE);
        } else {
            mBinding.llEducation.setVisibility(View.VISIBLE);
            mBinding.tvEducation.setText(addressDetailBO.getEducation());
        }
    }

    NetObserver<Response<UserAddressDetail>> addressDetailObserver;

    private void queryAddressDetail() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable =
                NetManager.getApiService().otherDetail(accountId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(addressDetailObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        addressDetailObserver = new NetObserver<Response<UserAddressDetail>>() {
            @Override
            public void onNext(Response<UserAddressDetail> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody() != null) {
                        initShowView(response.getBody());
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
        observable.subscribeWith(addressDetailObserver);
    }

    @Override
    public void onDestroy() {
        mBinding.sV.setChecked(false);
        mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
        CommonUtils.disposable(addressDetailObserver);
        if (EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initView() {
        if(!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        mBinding.btnSubmit.setOnClickListener(this);
        OnSpinnerItemSelectedListener();
    }

    private void initAddData() {
        List<String> dataset = new LinkedList<>(Arrays.asList("No", "Yes"));
        mBinding.spinnerForeignDebt.attachDataSource(dataset);
        queryCommonBo(Constants.MARITAL);
    }

    HashMap<String, String> maritalKeyMap = new HashMap<>();
    HashMap<String, String> salaryKeyMap = new HashMap<>();
    HashMap<String, String> workKeyMap = new HashMap<>();
    HashMap<String, String> educationKeyMap = new HashMap<>();
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CommonEventMessage(EventMessage event) {
        mCont = mCont + 1;
        if (mCont == 1) {
            queryCommonBo(Constants.SALARY);
        }
        if (mCont == 2) {
            queryCommonBo(Constants.WORK);
        }
        if (mCont == 3) {
            queryCommonBo(Constants.EDUCATION);
        }
        if (mCont == 4) {
            dismissProgressDialogFragment();
        }

        Common common = (Common) event.obj;
        if (event.what == EventMessage.MARITAL) {
            List<String> mMaritalList = new ArrayList<>();
            marital = common.getDictMap().getMarital();
            for (Common.DictMapBean.Marital item : marital) {
                mMaritalList.add(item.getVal());
                maritalKeyMap.put(item.getVal(), item.getKey());
            }
            mBinding.spinnerNumberOfSpouses.attachDataSource(mMaritalList);
        }
        if (event.what == EventMessage.SALARY) {
            List<String> mSalaryList = new ArrayList<>();
            List<Common.DictMapBean.Salary> salary = common.getDictMap().getSalary();
            for (Common.DictMapBean.Salary item : salary) {
                mSalaryList.add(item.getVal());
                salaryKeyMap.put(item.getVal(), item.getKey());
            }
            mBinding.spinnerGrossSalary.attachDataSource(mSalaryList);
        }
        if (event.what == EventMessage.WORK) {
            List<String> mWorkList = new ArrayList<>();
            List<Common.DictMapBean.Work> works = common.getDictMap().getWork();
            for (Common.DictMapBean.Work item : works) {
                mWorkList.add(item.getVal());
                workKeyMap.put(item.getVal(), item.getKey());
            }
            mBinding.spinnerWorkingStatus.attachDataSource(mWorkList);
        }

        if (event.what == EventMessage.EDU) {
            List<String> mEducationList = new ArrayList<>();
            List<Common.DictMapBean.Education> education = common.getDictMap().getEducation();
            for (Common.DictMapBean.Education item : education) {
                mEducationList.add(item.getVal());
                educationKeyMap.put(item.getVal(), item.getKey());
            }
            mBinding.spinnerEducation.attachDataSource(mEducationList);
        }
    }

    private void OnSpinnerItemSelectedListener() {
        mBinding.spinnerNumberOfSpouses.setOnSpinnerItemSelectedListener((NiceSpinner parent, View view, int position, long id) -> {
            String item = (String) parent.getItemAtPosition(position);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    NetObserver<Response<UserProfile>> mObserver;

    private void submit() {
        OtherInfoParams otherInfoParams = new OtherInfoParams();
        String mMarital = mBinding.spinnerNumberOfSpouses.getText().toString();
        String mEducation = mBinding.spinnerEducation.getText().toString();
        String mSalary = mBinding.spinnerGrossSalary.getText().toString();
        String mwork = mBinding.spinnerWorkingStatus.getText().toString();
        String mHasOutstandingLoan = mBinding.spinnerForeignDebt.getText().toString();
        String CompanyName = Utils.filter(mBinding.etCompanyName.getText().toString());
        String CompanyAddress = Utils.filter(mBinding.etCompanyAddress.getText().toString());

        if (TextUtils.isEmpty(mMarital)
                || TextUtils.isEmpty(mSalary)
                || TextUtils.isEmpty(mwork)
                || TextUtils.isEmpty(mHasOutstandingLoan)
                || TextUtils.isEmpty(CompanyName)
                || TextUtils.isEmpty(CompanyAddress)) {
            ToastManager.show(getContext(), "This not null");
            return;
        }

        if ("NO".equals(mHasOutstandingLoan)) {
            otherInfoParams.setHasOutstandingLoan("0");
        } else {
            otherInfoParams.setHasOutstandingLoan("1");
        }
        String accounId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        otherInfoParams.setAccounId(accounId);
        otherInfoParams.setEducation(educationKeyMap.get(mEducation));
        otherInfoParams.setMarital(maritalKeyMap.get(mMarital));
        otherInfoParams.setSalary(salaryKeyMap.get(mSalary));
        otherInfoParams.setWork(workKeyMap.get(mwork));
        otherInfoParams.setCompanyName(CompanyName);
        otherInfoParams.setCompanyAddress(CompanyAddress);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        Observable observable = NetManager.getApiService().UpAddressInfo(otherInfoParams)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);
        mObserver = new NetObserver<Response<UserProfile>>() {
            @Override
            public void onNext(Response<UserProfile> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    // TODO fireb_data
//                    FirebaseUtils.logEvent("fireb_data3");
                   queryAddressDetail();
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

}
