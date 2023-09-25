package com.tiny.cash.loan.card.feature.users;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityWorkDetailsBinding;
import com.tiny.cash.loan.card.ui.card.BindNewCardActivity;
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

import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * *作者: jyw
 * 日期：2020/10/12 20:50
 */
public class WorkInfoActivity extends BaseActivity implements View.OnClickListener {
    private ActivityWorkDetailsBinding mBinding;
    int mCont = 0;
    private List<Common.DictMapBean.Marital> marital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityWorkDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_work_details);
        initView();
        initData();
//        UpLoadUserInfo();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
        OnSpinnerItemSelectedListener();
    }

    private void initData() {
//        FirebaseLogUtils.Log("af_add_info3");
        showProgressDialogFragment(getString(R.string.str_loading), false);
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
            case R.id.iv_back:
                finish();
                break;
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
            ToastManager.show(this, "This not null");
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
                    FirebaseUtils.logEvent("fireb_data3");
                    BindNewCardActivity.Companion.launchAddBankAccount(WorkInfoActivity.this);
                    finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
