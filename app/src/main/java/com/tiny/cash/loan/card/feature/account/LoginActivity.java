package com.tiny.cash.loan.card.feature.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.feature.main.MainActivity;
import com.tiny.cash.loan.card.feature.start.PermissionActivity;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityLoginBinding;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.MD5Util;
import com.tiny.cash.loan.card.utils.RegexUtils;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.DeviceCheck;
import com.tiny.cash.loan.card.net.response.data.bean.UserInfo;
import com.tiny.cash.loan.card.net.response.Response;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.tiny.cash.loan.card.Constants.FIRST_PHONE;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private String pwd,phone,authCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        binding.rlPhone.setActivated(true);
        binding.edtPhone.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftInput();
            }
        }, 100);

        binding.ivBack.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
        binding.ivPhoneClear.setOnClickListener(this);
        binding.ivPwdClear.setOnClickListener(this);
        binding.ivPwdStyle.setOnClickListener(this);
        binding.tvStrForgotPwd.setOnClickListener(this);
        binding.tvRegistered.setOnClickListener(this);

        binding.edtPhone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        binding.edtPhone.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                setPwdFocus();
            }
            return false;
        }));

        binding.edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.rlPassword.setActivated(!hasFocus);
                binding.rlPhone.setActivated(hasFocus);
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
                        if ((stringBuilder.length() == 5 || stringBuilder.length() == 10) && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
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
                if (s.length() == 10 ||s.length() == 11 && isValidPwd()) {
                    binding.btnLogin.setEnabled(true);
                }
            }
        });

        binding.edtPwd.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE  ) {
                login();
            }
            return false;
        }));

        binding.edtPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.rlPhone.setActivated(!hasFocus);
                binding.rlPassword.setActivated(hasFocus);
                int length = binding.edtPwd.getText().length();
                binding.ivPwdClear.setVisibility(hasFocus && length > 0? View.VISIBLE : View.GONE);
            }
        });

        binding.edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.ivPwdClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                if (s.length() == 4 && isValidUser()) {
                    binding.btnLogin.setEnabled(true);
                }
            }
        });
    }

    private boolean isValidPwd() {
        return binding.edtPwd.getText() != null && RegexUtils.isValidNum(binding.edtPwd.getText().toString());
    }

    private boolean isValidUser() {
        return binding.edtPhone.getText() != null && RegexUtils.isValidPhone(binding.edtPhone.getText().toString().replaceAll(" ", ""));
    }

    private void setPwdFocus() {
        binding.edtPwd.requestFocus();
        Editable text = binding.edtPwd.getText();
        if (TextUtils.isEmpty(text)) {
            binding.edtPwd.setSelection(0);
        } else {
            binding.edtPwd.setSelection(text.length());
        }
    }

    private void showSoftInput() {
        binding.edtPhone.requestFocus();
        binding.edtPhone.setSelection(binding.edtPhone.getText().length());
        InputMethodManager inputManager =
                (InputMethodManager) binding.edtPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(binding.edtPhone, 0);
        }

    }

    private void hideSoftInput() {
        InputMethodManager inputManager =
                (InputMethodManager) binding.edtPhone.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && binding.edtPhone.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(binding.edtPhone.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == binding.ivBack) {
            finish();
        }
        if (v == binding.tvRegistered) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent); //页面跳转
        }
        if (v == binding.tvStrForgotPwd) {
            Intent intent = new Intent(this, ForgotPwdActivity.class);
            startActivity(intent); //页面跳转
        }
        if (v == binding.btnLogin) {
            login();
        } else if (v == binding.ivPhoneClear) {
            binding.edtPhone.setText("");
        } else if (v == binding.ivPwdStyle) {
            boolean select = v.isSelected();
            if (!select) {
                //如果选中，显示密码
                binding.edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.ivPwdStyle.setImageResource(R.mipmap.btn_password_show);
                binding.edtPwd.setSelection(binding.edtPwd.getText().length());
            } else {
                //否则隐藏密码
                binding.edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.ivPwdStyle.setImageResource(R.mipmap.btn_password_hide);
                binding.edtPwd.setSelection(binding.edtPwd.getText().length());
            }
            v.setSelected(!select);
        } else if (v == binding.ivPwdClear) {
            binding.edtPwd.setText("");
        }
    }


    private NetObserver loginObserver,deviceCheckObserver,deviceCaptchaObserver;
    private void deviceCheck() {
        Observable observable = NetManager.getApiService().deviceCheck(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(deviceCheckObserver);
        showProgressDialogFragment(getString(R.string.str_loading),false);
        deviceCheckObserver = new NetObserver<Response<DeviceCheck>>() {
            @Override
            public void onNext(Response<DeviceCheck> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                 if (response.getBody().getVerify().equals(Constants.ZERO)){
                    binding.llValidate.setVisibility(View.VISIBLE);
                    deviceCaptcha();
                 }else {
                     binding.llValidate.setVisibility(View.GONE);
                     login(phone, pwd);
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
        observable.subscribeWith(deviceCheckObserver);
    }

    private void deviceCaptcha() {
        Observable observable = NetManager.getApiService().deviceCaptcha(phone, Constants.THREE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(deviceCaptchaObserver);
        showProgressDialogFragment(getString(R.string.str_loading),false);
        deviceCaptchaObserver = new NetObserver<Response<DeviceCheck>>() {
            @Override
            public void onNext(Response<DeviceCheck> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(deviceCaptchaObserver);
    }


    private void login() {
        phone = FIRST_PHONE+binding.edtPhone.getText().toString().replaceAll(" ", "");
        String passWord = binding.edtPwd.getText().toString();
        if (!RegexUtils.isValidPhone(phone)) {
            ToastManager.show(LoginActivity.this, getString(R.string.str_correct_phone_number));
            return;
        }
        if (binding.llValidate.getVisibility() == View.VISIBLE){
            authCode = binding.edtValidate.getText().toString();
            if (TextUtils.isEmpty(authCode) || authCode.length() != 6){
                ToastManager.show(LoginActivity.this, getString(R.string.str_validate));
                return;
            }
        }
        pwd = MD5Util.encryption(passWord);
        if (binding.llValidate.getVisibility() == View.VISIBLE){
            login(phone,pwd);
        }else {
            deviceCheck();
        }
    }

    private void login(String phone, String passWord) {
        Observable observable =
                NetManager.getApiService().login(phone, passWord,authCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(loginObserver);
        showProgressDialogFragment(getString(R.string.str_loading),false);
        loginObserver = new NetObserver<Response<UserInfo>>() {
            @Override
            public void onNext(Response<UserInfo> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    FirebaseLogUtils.Log("af_log_in");
                    KvStorage.put(LocalConfig.LC_ISLOGIN, true);
                    KvStorage.put(LocalConfig.LC_PASSWORD,
                            pwd);
                    KvStorage.put(LocalConfig.LC_TOKEN,
                            response.getBody().getToken());
                    KvStorage.put(LocalConfig.LC_MOBILE,
                            response.getBody().getMobile());
                    KvStorage.put(LocalConfig.LC_ACCOUNTID,
                            response.getBody().getAccountId());
                    Boolean mShowPermission = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_HASSHOWPERMISSION), false);
                    Intent intent;
                    if (mShowPermission){
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }else {
                        intent = new Intent(LoginActivity.this, PermissionActivity.class);
                    }
                    startActivity(intent);
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
        observable.subscribeWith(loginObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoftInput();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideSoftInput();
        CommonUtils.disposable(loginObserver);
        CommonUtils.disposable(deviceCheckObserver);
        CommonUtils.disposable(deviceCaptchaObserver);
    }
}