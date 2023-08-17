package com.tiny.cash.loan.card.feature.users;

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityContactBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;
import com.tiny.cash.loan.card.utils.Utils;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.Common;
import com.tiny.cash.loan.card.net.response.data.bean.UserContact;
import com.tiny.cash.loan.card.net.request.params.ContactInfoParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import co.paystack.android.utils.StringUtils;

import com.tiny.cash.loan.card.message.EventMessage;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * *作者: jyw
 * 日期：2020/10/12 20:50
 */
public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private ActivityContactBinding mBinding;
    private String Type = "0";

    private final int REQUEST_CODE = 1000;
    private final int CONTACT_CODE = 2000;
    private ContactInfoParams mRefereeInfoParams;
    private NetObserver<Response<UserContact>> mObserver;
    private List<String> mList;

    private ActivityResultLauncher mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_contact);
        initView();
        initData();
//        UpLoadUserInfo();

        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        Uri contactUri = result.getData().getData();
                        if (contactUri == null) {
                            ToastUtils.showShort("not select contact.");
                            return;
                        }
                        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                        Cursor cursor = getContentResolver().query(contactUri,
                                projection, null, null, null);
                        while (cursor != null && cursor.moveToFirst()) {
                            int NUMBER_INDEX = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int DISPLAY_NAME = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                            phoneNum  = cursor.getString(NUMBER_INDEX);
                            contactName = cursor.getString(DISPLAY_NAME);
                            cursor.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  把电话号码中的  -  符号 替换成空格
                    if (phoneNum != null) {
                        phoneNum = phoneNum.replaceAll("-", " ");
                        // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                        phoneNum = phoneNum.replaceAll(" ", "");
                    }
                    setResultView(contactName, phoneNum);
                });
    }

    private void initView() {
        mBinding.tvMoreContinue.setOnClickListener(this);
        mBinding.btnNext.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
        mBinding.ivFirstContactMobile.setOnClickListener(this);
        mBinding.ivSecondContactMobile.setOnClickListener(this);
        mBinding.ivThreeContactMobile.setOnClickListener(this);
        mBinding.ivFourContactMobile.setOnClickListener(this);
        mBinding.ivFirstContactMobileClear.setOnClickListener(this);
        mBinding.ivSecondContactMobileClear.setOnClickListener(this);
        mBinding.ivThreeContactMobileClear.setOnClickListener(this);
        mBinding.ivFourContactMobileClear.setOnClickListener(this);
    }

    boolean flag;

    private void OnFocusChangeListener(EditText editText, String type) {

        editText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            flag = hasFocus;
            int length = editText.getText().length();
            switch (type) {
                case Constants.ONE:
                    mBinding.ivFirstContactMobileClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
                    break;
                case Constants.TWO:
                    mBinding.ivSecondContactMobileClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
                    break;
                case Constants.THREE:
                    mBinding.ivThreeContactMobileClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
                    break;
                case Constants.FOUR:
                    mBinding.ivFourContactMobileClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
                    break;
            }
        });
    }

    private void addTextChangedListener(TextView editText, String type) {
        editText.setOnClickListener(v -> {
            startOpenContact(type);
        });
    }

    private void initData() {
        EventBus.getDefault().register(this);
        FirebaseLogUtils.Log("af_add_info2");
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
        queryCommonBo(Constants.RELATIONSHIP);
        OnSpinnerItemSelectedListener();
    }

    HashMap<String, String> keyMap = new HashMap<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RelationshipList(EventMessage event) {
        if (event.what == EventMessage.RELATIONSHIP) {
            Common common = (Common) event.obj;
            List<Common.DictMapBean.Relationship> relationshipList = common.getDictMap().getRelationship();
            for (Common.DictMapBean.Relationship item : relationshipList) {
                mList.add(item.getVal());
                keyMap.put(item.getVal(), item.getKey());
            }
            mBinding.spinnerFirstRalationship.attachDataSource(mList);
            mBinding.spinnerSecondRalationship.attachDataSource(mList);
            mBinding.spinnerThreeRalationship.attachDataSource(mList);
            mBinding.spinnerFourRalationship.attachDataSource(mList);
        }
    }


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
            case R.id.iv_back:
                finish();
                break;
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


        if (TextUtils.isEmpty(etFirstContactName)
                || TextUtils.isEmpty(etFirstContactMobile)
                || TextUtils.isEmpty(etSecondContactName)
                || TextUtils.isEmpty(etSecondContactMobile)
                || TextUtils.isEmpty(mRefereeInfoParams.getContact1Relationship())
                || TextUtils.isEmpty(mRefereeInfoParams.getContact2Relationship())) {

            ToastManager.show(this, "This not null");
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
                    KvStorage.put(LocalConfig.LC_TOKEN, response.getBody().getToken());
                    if (response.getBody().isHasProfile() && response.getBody().isHasContact() && !response.getBody().isHasOther()) {
                        startIntent(WorkInfoActivity.class);
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

    private void startOpenContact(String type) {
        Type = type;
        intentToContact();
    }

//    private void checkPermission(String type) {
//        Type = type;
//        //**版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取**
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //ContextCompat.checkSelfPermission() 方法 指定context和某个权限 返回PackageManager.PERMISSION_DENIED或者PackageManager.PERMISSION_GRANTED
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // 若不为GRANTED(即为DENIED)则要申请权限了
//                // 申请权限 第一个为context 第二个可以指定多个请求的权限 第三个参数为请求码
//                ActivityCompat.requestPermissions(this,
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
//                Toast.makeText(this, "Authorization is prohibited", Toast.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    private void intentToContact() {
        // 跳转到联系人界面
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        mActivityResultLauncher.launch(intent);
        if (intent.resolveActivity(getPackageManager()) != null) {

        }
    }

    String phoneNum = null;
    String contactName = null;

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    @Override
    protected void onDestroy() {
        CommonUtils.disposable(mObserver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
