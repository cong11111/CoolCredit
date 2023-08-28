package com.tiny.cash.loan.card.feature.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityRegisterBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.RegexUtils;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.CheckCode;
import com.tiny.cash.loan.card.net.response.data.bean.CheckMobile;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.response.Response;

import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.tiny.cash.loan.card.Constants.FIRST_PHONE;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TimeCount mTimeCount;//计时器
    private boolean countting;
    private ActivityRegisterBinding binding;
    private int mCount = 0;
    boolean startFlag = false;
    boolean flag = false;//记录 是否点击语言验证
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_register);
        initEvent();
//        FirebaseLogUtils.Log("af_registered_phone");
    }

    private void initEvent() {
        binding.rlPhone.setActivated(false);
        binding.ivBack.setOnClickListener(this);
        binding.ivPhoneClear.setOnClickListener(this);
        binding.btnValidate.setOnClickListener(this);
        binding.btnSendCode.setOnClickListener(this);
        binding.tvSendCode.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.tvSignIn.setOnClickListener(this);
        binding.tvCallSMSCode.setOnClickListener(this);
        String ussd = KvStorage.get(LocalConfig.LC_USSD, Constants.ZERO);
        if (Constants.ZERO.equals(ussd)) {
            binding.tvSignIn.setVisibility(View.GONE);
        } else {
            binding.tvSignIn.setVisibility(View.VISIBLE);
        }
        mCount = 0;
        binding.edtPhone.postDelayed(() -> {
            showSoftInput();
        }, 100);
        mTimeCount = new TimeCount(90000, 1000);
        binding.edtPhone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        binding.edtPhone.setOnEditorActionListener((v, actionId, event) -> {
            //以下方法防止两次发送请求
            if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String phone = binding.edtPhone.getText().toString().replaceAll(" ", "");
                if (StringUtils.isEmpty(phone) || phone.length() < 9) {
                    showToast(getString(R.string.str_login_phone_error));
                } else {
                    binding.edtValidate.requestFocus();
                    checkMobile();
                }
                hideSoftInput();
            }
            return false;
        });
        binding.edtValidate.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Next();
            }
            return false;
        }));
        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.rlPhone.setActivated(hasFocus);
                int length = binding.edtPhone.getText().length();
                binding.ivPhoneClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
            }
        });
        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    if (i != 4 && i != 9 && charSequence.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(charSequence.charAt(i));
                        if ((stringBuilder.length() == 5 || stringBuilder.length() == 10)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(charSequence.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    binding.edtPhone.setText(stringBuilder.toString());
                    binding.edtPhone.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString().replaceAll(" ", "");
                binding.ivPhoneClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if (s.length() == 10 || s.length() == 11) {//&& isValidNum()
                    binding.btnNext.setEnabled(true);
                }
                if (!countting) {
                    binding.btnValidate.setEnabled(true);
                }
            }
        });

        binding.edtValidate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {//&& isValidUser()
                    binding.btnNext.setEnabled(true);
                }
            }
        });

    }

    private boolean isValidNum() {
        return binding.edtValidate.getText() != null && RegexUtils.isValidNum(binding.edtValidate.getText().toString());
    }

    private boolean isValidUser() {
        return binding.edtPhone.getText() != null && RegexUtils.isValidPhone(FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", ""));
    }

    private void showSoftInput() {
        binding.edtPhone.requestFocus();
        binding.edtPhone.setSelection(binding.edtPhone.getText().length());
        InputMethodManager inputManager = (InputMethodManager) binding.edtPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(binding.edtPhone, 0);
        }

    }

    private void hideSoftInput() {
        InputMethodManager inputManager = (InputMethodManager) binding.edtPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && binding.edtPhone.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(binding.edtPhone.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(View view) {
        if (view == binding.tvSignIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (view == binding.btnValidate) {
            flag = false;
            if (intCount == 0){
                FirebaseUtils.logEvent("fireb_send_sms");
                intCount++;
            } else {
                FirebaseUtils.logEvent("fireb_resend_sms");
            }
            checkMobile();
        }else if (view == binding.tvCallSMSCode) {
            flag = true;
            checkMobile();
        } else if (view == binding.btnNext) {
            Next();
        } else if (view == binding.btnSendCode || view == binding.tvSendCode) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode("*347*8#");
            intent.setData(Uri.parse("tel:" + tel));
            startFlag = true;
            startActivity(intent);
        } else if (view == binding.ivPhoneClear) {
            binding.edtPhone.setText("");
        } else if (view == binding.ivBack) {
            finish();
        }
    }

    private void Next() {
        if (!binding.edtPhone.getText().toString().trim().equals("")) {
            if (isValidUser()) {
                if (!binding.edtValidate.getText().toString().trim().equals("")) {
                    checkCode();
                } else {
                    showToast(getString(R.string.str_validate_num_input_hint));
                }
            } else {
                showToast(getString(R.string.str_login_phone_error));
            }
        } else {
            showToast(getString(R.string.str_login_phone_error));
        }
    }
    private int intCount = 0;
    private void checkMobile() {
        String phoneStr = FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)) {
            ToastManager.show(RegisterActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        Observable observable = NetManager.getApiService().hasRegister(phoneStr)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(sendCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        checkMobileObserver = new NetObserver<Response<CheckMobile>>() {
            @Override
            public void onNext(Response<CheckMobile> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (!response.getBody().isHasRegisted()) {
                        if (flag){
                            callSMSCode();
                        }else {
                            mCount = mCount + 1;
                            if (binding.tvSendCode.getVisibility() == View.GONE) {
                                binding.tvSendCode.setVisibility(View.VISIBLE);
                                binding.btnSendCode.setVisibility(View.VISIBLE);
                            }
                            sendCode();
                        }

                    } else {
                        showToast("Dear customer, you have registered successfully, please log in.");
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
        observable.subscribeWith(checkMobileObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (startFlag){
            startFlag = false;
            ussdLogin();
        }
    }

    public void ussdLogin(){
        String phoneStr = FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)) {
            ToastManager.show(RegisterActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        Observable observable = NetManager.getApiService().USSDLogin(phoneStr)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mUssdObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        mUssdObserver = new NetObserver<Response<OrderStatus>>() {
            @Override
            public void onNext(Response<OrderStatus> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess() && Constants.ONE.equals(response.getBody().getVerify())) {
                    Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                    intent.putExtra("phone", phoneStr);
                    startActivity(intent); //页面跳转
                } else {
                    ToastManager.show(RegisterActivity.this, response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(mUssdObserver);
    }
    private NetObserver sendCodeObserver, checkCodeObserver, checkMobileObserver,mUssdObserver,callSMSCodeObserver;

    private void sendCode() {
        String phoneStr = FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)) {
            ToastManager.show(RegisterActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        Observable observable = NetManager.getApiService().sendSmsCode(phoneStr, Constants.ONE)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(sendCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        sendCodeObserver = new NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    mTimeCount.start();
                    showToast(getString(R.string.str_validate_has_recived));
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(sendCodeObserver);
    }

    private void callSMSCode() {
//        FirebaseLogUtils.Log("callSMSCod");
        String phoneStr = FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)) {
            ToastManager.show(RegisterActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        Observable observable = NetManager.getApiService().sendVoiceCode(phoneStr, Constants.ONE)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(callSMSCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        callSMSCodeObserver = new NetObserver<Response>() {
            @Override
            public void onNext(Response response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(callSMSCodeObserver);
    }
    private void checkCode() {
        String phoneStr = FIRST_PHONE + binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)) {
            ToastManager.show(RegisterActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        String validateStr = binding.edtValidate.getText().toString().replaceAll(" ", "");
        Observable observable = NetManager.getApiService().checkSmsCode(phoneStr, validateStr)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(checkCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        checkCodeObserver = new NetObserver<Response<CheckCode>>() {
            @Override
            public void onNext(Response<CheckCode> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody().isVerifyed()) {
                        Intent intent = new Intent(RegisterActivity.this, SetPasswordActivity.class);
                        intent.putExtra("phone", phoneStr);
                        startActivity(intent); //页面跳转
                    } else {
                        ToastManager.show(RegisterActivity.this, getString(R.string.str_Illegal_verification_code));
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
        observable.subscribeWith(checkCodeObserver);
    }

    @Override
    protected void onDestroy() {
        hideSoftInput();
        CommonUtils.disposable(sendCodeObserver);
        CommonUtils.disposable(checkCodeObserver);
        CommonUtils.disposable(callSMSCodeObserver);
        CommonUtils.disposable(mUssdObserver);
        mCount = 0;
        startFlag = false;
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {//&& isValidUser() && isValidNum()
            binding.btnNext.setEnabled(true);
        } else {
            binding.btnNext.setEnabled(false);
        }
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            binding.btnValidate.setEnabled(false);
            countting = true;
            binding.btnValidate.setText("send(" + l / 1000 + ")");
        }

        @Override
        public void onFinish() {
            binding.btnValidate.setEnabled(true);
            countting = false;
            binding.btnValidate.setText(getString(R.string.validateNum));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }
}