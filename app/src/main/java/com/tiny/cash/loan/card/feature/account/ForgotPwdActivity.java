package com.tiny.cash.loan.card.feature.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityForgotPwdBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.RegexUtils;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.CheckCode;
import com.tiny.cash.loan.card.net.response.data.bean.SendCodeBO;
import com.tiny.cash.loan.card.net.response.Response;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.tiny.cash.loan.card.Constants.FIRST_PHONE;

/**
 * *作者: jyw
 * 日期：2020/10/15 14:23
 */
public class ForgotPwdActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ActivityForgotPwdBinding binding;
    private TimeCount mTimeCount;//计时器
    private boolean countting;
    private int mCount = 0;
    boolean startFlag = false;
    boolean flag = false;//记录 是否点击语言验证
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPwdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_forgot_psw);
        initEvent();
    }

    private void initEvent() {
        binding.rlPhone.setActivated(false);
        binding.ivBack.setOnClickListener(this);
        binding.ivPhoneClear.setOnClickListener(this);
        binding.btnValidate.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
        binding.btnSendCode.setOnClickListener(this);
        binding.tvSendCode.setOnClickListener(this);
        mCount = 0;
        binding.edtPhone.postDelayed(()-> { showSoftInput();}, 100);
        mTimeCount = new TimeCount(90000, 1000);
        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.rlPhone.setActivated(hasFocus);
                int length = binding.edtPhone.getText().length();
                binding.ivPhoneClear.setVisibility(hasFocus && length > 0? View.VISIBLE : View.GONE);
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
                if (s.length() == 10 ||s.length() == 11 && isValidNum()) {
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
                if (s.length() == 6 && isValidUser()) {
                    binding.btnNext.setEnabled(true);
                }
            }
        });

    }

    private boolean isValidNum() {
        return binding.edtValidate.getText() != null && RegexUtils.isValidNum(binding.edtValidate.getText().toString());
    }

    private boolean isValidUser() {
        return binding.edtPhone.getText() != null && RegexUtils.isValidPhone(FIRST_PHONE+binding.edtPhone.getText().toString().replaceAll(" ", ""));
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
        if (view == binding.btnValidate) {
            sendCode();
        } else if (view == binding.btnNext) {
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
        }  else if (view == binding.btnSendCode || view == binding.tvSendCode) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode("*347*8#");
            intent.setData(Uri.parse("tel:" + tel));
            startFlag = true;
            startActivity(intent);
        } else if (view == binding.ivPhoneClear) {
            binding.edtPhone.setText("");
        }else if (view == binding.ivBack) {
            finish();
        }
    }




    private NetObserver sendCodeObserver,checkCodeObserver;

    private void sendCode() {
        String phoneStr = FIRST_PHONE+binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)){
            ToastManager.show(ForgotPwdActivity.this,getString(R.string.str_correct_phone_number));
            return;
        }
        Observable observable = NetManager.getApiService().sendSmsCode(phoneStr, Constants.ONE)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(sendCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading),false);
        sendCodeObserver = new NetObserver<Response<SendCodeBO>>() {
            @Override
            public void onNext(Response<SendCodeBO> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    mTimeCount.start();
                    showToast(getString(R.string.str_validate_has_recived));
                    mCount = mCount + 1;
                    if (mCount == 2 && binding.tvSendCode.getVisibility() == View.GONE) {
                        binding.tvSendCode.setVisibility(View.VISIBLE);
                        binding.btnSendCode.setVisibility(View.VISIBLE);
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
        observable.subscribeWith(sendCodeObserver);
    }

    private void checkCode() {
        String phoneStr = FIRST_PHONE+binding.edtPhone.getText().toString().replaceAll(" ", "");
        if (!RegexUtils.isValidPhone(phoneStr)){
            ToastManager.show(ForgotPwdActivity.this,getString(R.string.str_correct_phone_number));
            return;
        }
        String validateStr = binding.edtValidate.getText().toString().replaceAll(" ", "");
        Observable observable = NetManager.getApiService().checkSmsCode(phoneStr, validateStr)//“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(checkCodeObserver);
        checkCodeObserver = new NetObserver<Response<CheckCode>>() {
            @Override
            public void onNext(Response<CheckCode> response) {
                if (response.isSuccess()) {
                    if (response.getBody().isVerifyed()){
                        Intent intent = new Intent(ForgotPwdActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("phone",phoneStr);
                        startActivity(intent); //页面跳转
                    }else {
                        ToastManager.show(ForgotPwdActivity.this,getString(R.string.str_Illegal_verification_code));
                    }

                } else {
                    showToast(response.getStatus().getMsg());
                }
            }
            @Override
            public void onException(ResponseException netException) {
            }
        };
        observable.subscribeWith(checkCodeObserver);
    }

    @Override
    protected void onDestroy() {
        hideSoftInput();
        mCount = 0;
        CommonUtils.disposable(sendCodeObserver);
        CommonUtils.disposable(checkCodeObserver);
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && isValidUser() && isValidNum()) {
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
