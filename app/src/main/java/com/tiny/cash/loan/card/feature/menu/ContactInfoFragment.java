package com.tiny.cash.loan.card.feature.menu;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentContactBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.data.bean.UserContact;
import com.tiny.cash.loan.card.net.response.data.bean.UserContactDetail;
import com.tiny.cash.loan.card.net.request.params.ContactInfoParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactInfoFragment extends BaseFragment implements View.OnClickListener {
    private FragmentContactBinding mBinding;
    private String Type = "0";

    private final int REQUEST_CODE = 1000;
    private final int CONTACT_CODE = 2000;
    private ContactInfoParams mRefereeInfoParams;
    private NetObserver<Response<UserContact>> mObserver;
    private List<String> mList;

    private boolean hasContact = false;

    public static ContactInfoFragment newInstance() {
        Bundle args = new Bundle();
        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initShowData();
    }

    private void initSwitch() {
        mBinding.sV.setChecked(false);
        mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
        mBinding.sV.setOnCheckedChangeListener((CompoundButton compoundButton, boolean b) -> {
            //控制开关字体颜色
            if (b) {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_true);
                mBinding.llAddDetail.setVisibility(View.VISIBLE);
                mBinding.llShowDetail.setVisibility(View.GONE);
            } else {
                mBinding.sV.setSwitchTextAppearance(getContext(), R.style.s_false);
                mBinding.llAddDetail.setVisibility(View.GONE);
                mBinding.llShowDetail.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initShowData() {
        initSwitch();
        queryRefereeDetail();
    }

    private void showView(UserContactDetail refereeDetailBO) {
        mBinding.llShowDetail.setVisibility(View.VISIBLE);
        mBinding.llAddDetail.setVisibility(View.GONE);
        if (refereeDetailBO.isEdit()) {
            mBinding.rlSwitch.setVisibility(View.VISIBLE);
        } else {
            mBinding.rlSwitch.setVisibility(View.GONE);
        }
        mBinding.tvFirstContactName.setText(refereeDetailBO.getContact1());
        mBinding.tvFirstContactMobile.setText(refereeDetailBO.getContact1Mobile());
        mBinding.tvFirstRalationship.setText(refereeDetailBO.getContact1RelationshipLabel());
        mBinding.tvSecondContactName.setText(refereeDetailBO.getContact2());
        mBinding.tvSecondContactMobile.setText(refereeDetailBO.getContact2Mobile());
        mBinding.tvSecondRalationship.setText(refereeDetailBO.getContact2RelationshipLabel());

        mBinding.tvThreeContactName.setText(refereeDetailBO.getContact3());
        mBinding.tvThreeContactMobile.setText(refereeDetailBO.getContact3Mobile());
        mBinding.tvThreeRalationship.setText(refereeDetailBO.getContact3RelationshipLabel());
        mBinding.tvFourContactName.setText(refereeDetailBO.getContact4());
        mBinding.tvFourContactMobile.setText(refereeDetailBO.getContact4Mobile());
        mBinding.tvFourRalationship.setText(refereeDetailBO.getContact1RelationshipLabel());
    }

    NetObserver<Response<UserContactDetail>> refereeDetailObserver;

    private void queryRefereeDetail() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().contactDetail(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(refereeDetailObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        refereeDetailObserver = new NetObserver<Response<UserContactDetail>>() {
            @Override
            public void onNext(Response<UserContactDetail> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody() != null) {
                        UserContactDetail body = response.getBody();
                        hasContact = !TextUtils.isEmpty(body.getContact1()) ||
                                !TextUtils.isEmpty(body.getContact2()) || !TextUtils.isEmpty(body.getContact3());
                        showView(response.getBody());
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
        observable.subscribeWith(refereeDetailObserver);
    }

    @Override
    public void onDestroy() {
        CommonUtils.disposable(refereeDetailObserver);
        CommonUtils.disposable(mCommonBoObserver);
        super.onDestroy();
    }

    private void initView() {
        mBinding.tvMoreContinue.setOnClickListener(this);
        mBinding.btnNext.setOnClickListener(this);
        mBinding.ivFirstContactMobile.setOnClickListener(this);
        mBinding.ivSecondContactMobile.setOnClickListener(this);
        mBinding.ivThreeContactMobile.setOnClickListener(this);
        mBinding.ivFourContactMobile.setOnClickListener(this);
        mBinding.ivFirstContactMobileClear.setOnClickListener(this);
        mBinding.ivSecondContactMobileClear.setOnClickListener(this);
        mBinding.ivThreeContactMobileClear.setOnClickListener(this);
        mBinding.ivFourContactMobileClear.setOnClickListener(this);
    }

    private void addTextChangedListener(TextView editText, String type) {
        editText.setOnClickListener(v -> {
            startOpenContact(type);
        });
    }

    NetObserver mCommonBoObserver;

    public void queryRelationShip(String type) {

        Observable observable = NetManager.getApiService().queryCommon(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mCommonBoObserver);
        mCommonBoObserver = new NetObserver<Response<Common>>() {
            @Override
            public void onNext(Response<Common> response) {
                if (response.isSuccess()) {
                    switch (type) {
                        case Constants.RELATIONSHIP:
                            Common common = response.getBody();
                            List<Common.DictMapBean.Relationship> relationshipList = common.getDictMap().getRelationship();
                            for (Common.DictMapBean.Relationship item : relationshipList) {
                                Log.d("AAAAAAAAAA", "onNext: " + item.getVal());
                                mList.add(item.getVal());
                                keyMap.put(item.getVal(), item.getKey());
                            }
                            mBinding.spinnerFirstRalationship.attachDataSource(mList);
                            mBinding.spinnerSecondRalationship.attachDataSource(mList);
                            mBinding.spinnerThreeRalationship.attachDataSource(mList);
                            mBinding.spinnerFourRalationship.attachDataSource(mList);
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
        observable.subscribeWith(mCommonBoObserver);
    }

    private void initData() {
        addTextChangedListener(mBinding.etFirstContactName, Constants.ONE);
        addTextChangedListener(mBinding.etSecondContactName, Constants.TWO);
        addTextChangedListener(mBinding.etThreeContactName, Constants.THREE);
        addTextChangedListener(mBinding.etFourContactName, Constants.FOUR);

        addTextChangedListener(mBinding.etFirstContactMobile, Constants.ONE);
        addTextChangedListener(mBinding.etSecondContactMobile, Constants.TWO);
        addTextChangedListener(mBinding.etThreeContactMobile, Constants.THREE);
        addTextChangedListener(mBinding.etFourContactMobile, Constants.FOUR);
        mRefereeInfoParams = new ContactInfoParams();
        mList = new ArrayList<>();
        queryRelationShip(Constants.RELATIONSHIP);
        OnSpinnerItemSelectedListener();
    }

    HashMap<String, String> keyMap = new HashMap<>();

    private void OnSpinnerItemSelectedListener() {

        mBinding.spinnerFirstRalationship.setOnSpinnerItemSelectedListener((NiceSpinner parent, View view, int position, long id) -> {
            String item = (String) parent.getItemAtPosition(position);
            mRefereeInfoParams.setContact1Relationship(keyMap.get(item));
        });

        mBinding.spinnerSecondRalationship.setOnSpinnerItemSelectedListener((NiceSpinner parent, View view, int position, long id) -> {
            String item = (String) parent.getItemAtPosition(position);
            mRefereeInfoParams.setContact2Relationship(keyMap.get(item));
        });

        mBinding.spinnerThreeRalationship.setOnSpinnerItemSelectedListener((NiceSpinner parent, View view, int position, long id) -> {
            String item = (String) parent.getItemAtPosition(position);
            mRefereeInfoParams.setContact3Relationship(keyMap.get(item));
        });

        mBinding.spinnerFourRalationship.setOnSpinnerItemSelectedListener((NiceSpinner parent, View view, int position, long id) -> {
            String item = (String) parent.getItemAtPosition(position);
            mRefereeInfoParams.setContact4Relationship(keyMap.get(item));
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_more_Continue:
                mBinding.llMoreContainer.setVisibility(mBinding.llMoreContainer.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_next:
                submitContact();
                break;
            case R.id.iv_first_contact_mobile:
                startOpenContact(Constants.ONE);
                break;
            case R.id.iv_second_contact_mobile:
                startOpenContact(Constants.TWO);
                break;
            case R.id.iv_three_contact_mobile:
                startOpenContact(Constants.THREE);
                break;
            case R.id.iv_four_contact_mobile:
                startOpenContact(Constants.FOUR);
                break;
            case R.id.iv_first_contact_mobile_clear:
                mBinding.etFirstContactMobile.setText("");
                break;
            case R.id.iv_second_contact_mobile_clear:
                mBinding.etSecondContactMobile.setText("");
                break;
            case R.id.iv_three_contact_mobile_clear:
                mBinding.etThreeContactMobile.setText("");
                break;
            case R.id.iv_four_contact_mobile_clear:
                mBinding.etFourContactMobile.setText("");
                break;
        }
    }

    private void submitContact() {

        String etFirstContactName = Utils.filter(mBinding.etFirstContactName.getText().toString());
        String etFirstContactMobile = Utils.filter(mBinding.etFirstContactMobile.getText().toString());
        String etSecondContactName = Utils.filter(mBinding.etSecondContactName.getText().toString());
        String etSecondContactMobile = Utils.filter(mBinding.etSecondContactMobile.getText().toString());

        String etThreeContactName = Utils.filter(mBinding.etThreeContactName.getText().toString());
        String etThreeContactMobile = Utils.filter(mBinding.etThreeContactMobile.getText().toString());
        String etFourContactName = Utils.filter(mBinding.etFourContactName.getText().toString());
        String etFourContactMobile = Utils.filter(mBinding.etFourContactMobile.getText().toString());
        mRefereeInfoParams.setContact1Relationship(keyMap.get(mBinding.spinnerFirstRalationship.getText().toString()));
        mRefereeInfoParams.setContact2Relationship(keyMap.get(mBinding.spinnerSecondRalationship.getText().toString()));
        mRefereeInfoParams.setContact3Relationship(keyMap.get(mBinding.spinnerThreeRalationship.getText().toString()));
        mRefereeInfoParams.setContact4Relationship(keyMap.get(mBinding.spinnerFourRalationship.getText().toString()));

        if (!checkContactAvailable()) {
            return;
        }
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        mRefereeInfoParams.setAccountId(accountId);
        mRefereeInfoParams.setContact1(etFirstContactName);
        mRefereeInfoParams.setContact1Mobile(etFirstContactMobile);
        mRefereeInfoParams.setContact2(etSecondContactName);
        mRefereeInfoParams.setContact2Mobile(etSecondContactMobile);

        mRefereeInfoParams.setContact3(etThreeContactName);
        mRefereeInfoParams.setContact3Mobile(etThreeContactMobile);
        mRefereeInfoParams.setContact4(etFourContactName);
        mRefereeInfoParams.setContact4Mobile(etFourContactMobile);

        Observable observable = NetManager.getApiService().upContactInfoBo(mRefereeInfoParams)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        mObserver = new NetObserver<Response<UserContact>>() {
            @Override
            public void onNext(Response<UserContact> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    // TODO fireb_data
                    if (response.getBody().isHasContact() && hasContact) {
                        FirebaseUtils.logEvent("fireb_data2");
                    }
                    KvStorage.put(LocalConfig.LC_TOKEN, response.getBody().getToken());
                    queryRefereeDetail();
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

    private void startOpenContact(String type) {
        Type = type;
        intentToContact();
    }

//    private void checkPermission(String type) {
//        Type = type;
//        //**版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取**
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //ContextCompat.checkSelfPermission() 方法 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
//            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // 若不为GRANTED(即为DENIED)则要申请权限了
//                // 申请权限 第一个为context 第二个可以指定多个请求的权限 第三个参数为请求码
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
//            } else {
//                //权限已经被授予，在这里直接写要执行的相应方法即可
//                intentToContact();
//            }
//        } else {
//            // 低于6.0的手机直接访问
//            intentToContact();
//        }
//    }

    // 用户权限 申请 的回调方法
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[], @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                intentToContact();
//            } else {
//                Toast.makeText(getContext(), "Authorization is prohibited", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, CONTACT_CODE);
    }

    String phoneNum = null;
    String contactName = null;
    Cursor cursor = null;

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTACT_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    // 创建内容解析者
                    ContentResolver contentResolver = getContext().getContentResolver();
                    if (uri != null) {
                        cursor = contentResolver.query(uri,
                                new String[]{"display_name", "data1"}, null, null, null);
                    }
                    if (cursor != null && cursor.getCount() >= 1) {
                        while (cursor.moveToNext()) {
                            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    }
                } catch (Exception exception) {

                }
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum = phoneNum.replaceAll(" ", "");
                }
                setResultView(contactName, phoneNum);
            }
        }

    }

    private void setResultView(String name, String mobile) {

        switch (Type) {
            case Constants.ONE:
                if (!StringUtils.isEmpty(name)) {
                    mRefereeInfoParams.setContact1(Utils.filter(name));
                }
                if (!StringUtils.isEmpty(mobile)) {
                    mRefereeInfoParams.setContact1Mobile(Utils.filter(mobile));
                }
                mBinding.etFirstContactName.setText(name);
                mBinding.etFirstContactMobile.setText(mobile);
                break;
            case Constants.TWO:
                if (!StringUtils.isEmpty(name)) {
                    mRefereeInfoParams.setContact2(Utils.filter(name));
                }
                if (!StringUtils.isEmpty(mobile)) {
                    mRefereeInfoParams.setContact2Mobile(Utils.filter(mobile));
                }
                mBinding.etSecondContactName.setText(name);
                mBinding.etSecondContactMobile.setText(mobile);
                break;
            case Constants.THREE:
                if (!StringUtils.isEmpty(name)) {
                    mRefereeInfoParams.setContact3(Utils.filter(name));
                }
                if (!StringUtils.isEmpty(mobile)) {
                    mRefereeInfoParams.setContact3Mobile(Utils.filter(mobile));
                }
                mBinding.etThreeContactName.setText(name);
                mBinding.etThreeContactMobile.setText(mobile);
                break;
            case Constants.FOUR:
                if (!StringUtils.isEmpty(name)) {
                    mRefereeInfoParams.setContact4(Utils.filter(name));
                }
                if (!StringUtils.isEmpty(mobile)) {
                    mRefereeInfoParams.setContact4Mobile(Utils.filter(mobile));
                }
                mBinding.etFourContactName.setText(name);
                mBinding.etFourContactMobile.setText(mobile);
                break;
        }
    }

    private boolean checkContactAvailable() {
        String leagalName1 = Utils.filter(mBinding.etFirstContactName.getText().toString());
        String mobile1 = Utils.filter(mBinding.etFirstContactMobile.getText().toString());
        String leagalName2 = Utils.filter(mBinding.etSecondContactName.getText().toString());
        String mobile2 = Utils.filter(mBinding.etSecondContactMobile.getText().toString());

        String leagalName3 = Utils.filter(mBinding.etThreeContactName.getText().toString());
        String mobile3 = Utils.filter(mBinding.etThreeContactMobile.getText().toString());
        String leagalName4 = Utils.filter(mBinding.etFourContactName.getText().toString());
        String mobile4 = Utils.filter(mBinding.etFourContactMobile.getText().toString());
        String relationShip1 = mRefereeInfoParams.getContact1Relationship();
        String relationShip2 = mRefereeInfoParams.getContact2Relationship();
        String relationShip3 = mRefereeInfoParams.getContact3Relationship();
        String relationShip4 = mRefereeInfoParams.getContact4Relationship();
        if (TextUtils.isEmpty(leagalName1)){
            ToastUtils.showShort("Please select contact1 name");
            return false;
        }
        if (TextUtils.isEmpty(mobile1)){
            ToastUtils.showShort("Please select contact1 mobile");
            return false;
        }
        if (relationShip1 == null){
            ToastUtils.showShort("Please select contact1 relationship");
            return false;
        }
        if (TextUtils.isEmpty(leagalName2)){
            ToastUtils.showShort("Please select contact2 name");
            return false;
        }
        if (TextUtils.isEmpty(mobile2)){
            ToastUtils.showShort("Please select contact2 mobile");
            return false;
        }
        if (relationShip2 == null){
            ToastUtils.showShort("Please select contact2 relationship");
            return false;
        }
        if (TextUtils.isEmpty(leagalName3)){
            ToastUtils.showShort("Please select contact3 name");
            return false;
        }
        if (TextUtils.isEmpty(mobile3)){
            ToastUtils.showShort("Please select contact3 mobile");
            return false;
        }
        if (relationShip3 == null){
            ToastUtils.showShort("Please select contact3 relationship");
            return false;
        }
        if (TextUtils.isEmpty(leagalName4)){
            ToastUtils.showShort("Please select contact4 name");
            return false;
        }
        if (TextUtils.isEmpty(mobile4)){
            ToastUtils.showShort("Please select contact4 mobile");
            return false;
        }
        if (relationShip4 == null){
            ToastUtils.showShort("Please select contact4 relationship");
            return false;
        }
        if (TextUtils.equals(mobile3, mobile1)){
//            ToastUtils.showShort("Phone number 3 and phone number 1 are the same")
            ToastUtils.showShort("Duplicate contact info.");
            return false;
        }
        if (TextUtils.equals(mobile3, mobile2)){
//            ToastUtils.showShort("Phone number 3 and phone number 2 are the same")
            ToastUtils.showShort("Duplicate contact info.");
            return false;
        }
        if (TextUtils.equals(mobile4, mobile1)){
//            ToastUtils.showShort("Phone number 4 and phone number 1 are the same")
            ToastUtils.showShort("Duplicate contact info.");
            return false;
        }
        if (TextUtils.equals(mobile4, mobile2)){
//            ToastUtils.showShort("Phone number 4 and phone number 2 are the same")
            ToastUtils.showShort("Duplicate contact info.");
            return false;
        }
        if (TextUtils.equals(mobile4, mobile3)){
//            ToastUtils.showShort("Phone number 4 and phone number 3 are the same")
            ToastUtils.showShort("Duplicate contact info.");
            return false;
        }
        return true;
    }
}
