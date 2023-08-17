package com.tiny.cash.loan.card.feature.users;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityBasicInfoBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.StateAreaMap;
import com.tiny.cash.loan.card.net.response.data.bean.UserProfile;
import com.tiny.cash.loan.card.net.request.params.UserInfoParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * *作者: jyw
 * 日期：2020/10/12 20:50
 */
public class BasicInfoActivity extends BaseActivity implements View.OnClickListener {

    private ActivityBasicInfoBinding mBinding;
    private String mGender;
    private List<StateAreaMap.DictMapBean.State> stateList;
    private String stateValue;
    private String areaValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBasicInfoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_userinformation);
        initView();
        initData();
//        UpLoadUserInfo();
    }

    private void initView() {
        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
        mBinding.rlDate.setOnClickListener(this);
    }

    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    HashMap<String, String> keyMap = new HashMap<>();
    HashMap<String, String> stateMap = new HashMap<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StateAreaList(EventMessage event) {
        if (event.what == EventMessage.STATEAREA) {
            StateAreaMap stateAreaMap = (StateAreaMap) event.obj;
            DataWork(stateAreaMap);
        }
    }

    private void DataWork(StateAreaMap stateAreaMap) {
        stateList = stateAreaMap.getDictMap().getState();
        for (StateAreaMap.DictMapBean.State state : stateList) {
            options1Items.add(state.getVal());
            stateMap.put(state.getVal(), state.getKey());
            switch (state.getKey()) {
                case "100":
                    loadData(stateAreaMap.getDictMap().getArea().get_$100());
                    break;
                case "101":
                    loadData(stateAreaMap.getDictMap().getArea().get_$101());
                    break;
                case "102":
                    loadData(stateAreaMap.getDictMap().getArea().get_$102());
                    break;
                case "103":
                    loadData(stateAreaMap.getDictMap().getArea().get_$103());
                    break;
                case "104":
                    loadData(stateAreaMap.getDictMap().getArea().get_$104());
                    break;
                case "105":
                    loadData(stateAreaMap.getDictMap().getArea().get_$105());
                    break;
                case "106":
                    loadData(stateAreaMap.getDictMap().getArea().get_$106());
                    break;
                case "107":
                    loadData(stateAreaMap.getDictMap().getArea().get_$107());
                    break;
                case "108":
                    loadData(stateAreaMap.getDictMap().getArea().get_$108());
                    break;
                case "109":
                    loadData(stateAreaMap.getDictMap().getArea().get_$109());
                    break;

                case "110":
                    loadData(stateAreaMap.getDictMap().getArea().get_$110());
                    break;
                case "111":
                    loadData(stateAreaMap.getDictMap().getArea().get_$111());
                    break;
                case "112":
                    loadData(stateAreaMap.getDictMap().getArea().get_$112());
                    break;
                case "113":
                    loadData(stateAreaMap.getDictMap().getArea().get_$113());
                    break;
                case "114":
                    loadData(stateAreaMap.getDictMap().getArea().get_$114());
                    break;
                case "115":
                    loadData(stateAreaMap.getDictMap().getArea().get_$115());
                    break;
                case "116":
                    loadData(stateAreaMap.getDictMap().getArea().get_$116());
                    break;
                case "117":
                    loadData(stateAreaMap.getDictMap().getArea().get_$117());
                    break;
                case "118":
                    loadData(stateAreaMap.getDictMap().getArea().get_$118());
                    break;
                case "119":
                    loadData(stateAreaMap.getDictMap().getArea().get_$119());
                    break;

                case "120":
                    loadData(stateAreaMap.getDictMap().getArea().get_$120());
                    break;
                case "121":
                    loadData(stateAreaMap.getDictMap().getArea().get_$121());
                    break;
                case "122":
                    loadData(stateAreaMap.getDictMap().getArea().get_$122());
                    break;
                case "123":
                    loadData(stateAreaMap.getDictMap().getArea().get_$123());
                    break;
                case "124":
                    loadData(stateAreaMap.getDictMap().getArea().get_$124());
                    break;
                case "125":
                    loadData(stateAreaMap.getDictMap().getArea().get_$125());
                    break;
                case "126":
                    loadData(stateAreaMap.getDictMap().getArea().get_$126());
                    break;
                case "127":
                    loadData(stateAreaMap.getDictMap().getArea().get_$127());
                    break;
                case "128":
                    loadData(stateAreaMap.getDictMap().getArea().get_$128());
                    break;
                case "129":
                    loadData(stateAreaMap.getDictMap().getArea().get_$129());
                    break;

                case "130":
                    loadData(stateAreaMap.getDictMap().getArea().get_$130());
                    break;
                case "131":
                    loadData(stateAreaMap.getDictMap().getArea().get_$131());
                    break;
                case "132":
                    loadData(stateAreaMap.getDictMap().getArea().get_$132());
                    break;
                case "133":
                    loadData(stateAreaMap.getDictMap().getArea().get_$133());
                    break;
                case "134":
                    loadData(stateAreaMap.getDictMap().getArea().get_$134());
                    break;
                case "135":
                    loadData(stateAreaMap.getDictMap().getArea().get_$135());
                    break;
                case "136":
                    loadData(stateAreaMap.getDictMap().getArea().get_$136());
                    break;
            }
        }
    }

    private void loadData(List<StateAreaMap.DictMapBean.AreaBean.keyVaule> beans) {
        List<String> mList = new ArrayList<>();
        for (StateAreaMap.DictMapBean.AreaBean.keyVaule bean : beans) {
            mList.add(bean.getVal());
            keyMap.put(bean.getVal(), bean.getKey());
        }
        options2Items.add(mList);
    }

    private void showPopWindow() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = options1Items.get(options1) + " / "
                    + options2Items.get(options1).get(options2);
            stateValue = options1Items.get(options1);
            areaValue = options2Items.get(options1).get(options2);
            mBinding.textAddress.setText(tx);
        }).build();
        if (options1Items.size() > 0 && options2Items.size() > 0) {
            pvOptions.setPicker(options1Items, options2Items, null);
            pvOptions.show();
        }
    }

    private void initData() {
        EventBus.getDefault().register(this);
        FirebaseLogUtils.Log("af_add_info1");
        queryStateArea();
        mGender = Constants.ONE;
        mBinding.radioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            RadioButton checkView = group.findViewById(checkedId);
            if (checkView != null && !checkView.isPressed()) {
                return;
            }
            if (checkView.getText().equals(getString(R.string.str_male))) {
                mGender = Constants.ONE;
            } else {
                mGender = Constants.TWO;
            }
        });
        mBinding.ivAddress.setOnClickListener(v -> {
            showPopWindow();
        });
        mBinding.etStreetNumber.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit();
            }
            return false;
        }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_date:
//                DataTimeDialogFragment.createBuilder(this, getSupportFragmentManager(),
////                        DataTimeDialogFragment.TYPE_DATE)
////                        .setMessage("")
////                        .setNegativeButtonTxt(getString(R.string.str_done))
////                        .setPositiveButtonTxt(getString(R.string.str_cancel))
////                        .setNegativeListener((String date) -> {
////                            mBinding.tvDateTime.setText(date);
////
////                }).show();
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(BasicInfoActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        mBinding.tvDateTime.setText(getTime(date));
//                        Toast.makeText(UserInformationActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                    }
                }).build();
                pvTime.show();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    private NetObserver mObserver;

    private void submit() {
        String mFirstName = Utils.filter(mBinding.etFirstName.getText().toString());
        String mMiddleName = Utils.filter(mBinding.etMiddleName.getText().toString());
        String mLastName = Utils.filter(mBinding.etLastName.getText().toString());
        String mBvn = mBinding.etBvn.getText().toString();
        String mPersonalEmail = mBinding.etPersonalEmail.getText().toString().trim();
        String mAddress = Utils.filter(mBinding.etStreetNumber.getText().toString());
        String mDateTime = mBinding.tvDateTime.getText().toString();
        if (!mPersonalEmail.contains("@")) {
            ToastManager.show(this, getString(R.string.str_not_email));
            return;
        }
        if (mBvn.length() != 11) {
            ToastManager.show(this, " please enter your correct bvn number");
            return;
        }
        String statekey = stateMap.get(stateValue);
        String areakey = keyMap.get(areaValue);
        if (TextUtils.isEmpty(mFirstName)
                || TextUtils.isEmpty(mLastName)
                || TextUtils.isEmpty(mBvn)
                || TextUtils.isEmpty(mPersonalEmail)
                || TextUtils.isEmpty(mAddress)
                || TextUtils.isEmpty(mDateTime)
                || TextUtils.isEmpty(statekey)
                || TextUtils.isEmpty(areakey)) {
            ToastManager.show(this, "This not null");
            return;
        }

        showProgressDialogFragment(getString(R.string.str_loading), false);
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        UserInfoParams params = UserInfoParams.createParams(accountId, mFirstName, mMiddleName,
                mLastName, mBvn, mDateTime, mGender, mPersonalEmail, statekey, areakey, mAddress);
        Observable observable = NetManager.getApiService().uploadUserInfo(params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);
        mObserver = new NetObserver<Response<UserProfile>>() {
            @Override
            public void onNext(Response<UserProfile> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody().isHasProfile() && !response.getBody().isHasContact()) {
                        startIntent(ContactsActivity.class);
                        finish();
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

    @Override
    protected void onDestroy() {
        CommonUtils.disposable(mObserver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
