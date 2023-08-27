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
import com.tiny.cash.loan.card.kudicredit.databinding.ActivitySetPwdBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.AgreeTermsFragment;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.MD5Util;
import com.tiny.cash.loan.card.utils.RegexUtils;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.RegistrationBO;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.server.ApiServerImpl;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private String phone;
    private ActivitySetPwdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPwdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_set_pwd);
        phone = getIntent().getStringExtra("phone");
        initEvent();
    }

    private void initEvent() {
        binding.cbAgree.setSelected(true);
        binding.edtPwd.setText("");
        binding.rlPassword.setActivated(true);
        binding.edtPwd.postDelayed(() -> {
            showSoftInput();
        }, 100);
        binding.btnSignUp.setOnClickListener(this);
        binding.ivBackReg.setOnClickListener(this);
        binding.ivPwdClear.setOnClickListener(this);
        binding.ivPwdStyle.setOnClickListener(this);
        binding.ivPwdConfirmClear.setOnClickListener(this);
        binding.ivPwdConfirmStyle.setOnClickListener(this);
        binding.tvPrivacy.setOnClickListener(this);
        binding.cbAgree.setOnClickListener(this);

        binding.edtPwd.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                setPwdConfirmFocus();
            }
            return false;
        }));

        binding.edtPwd.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            binding.rlPasswordConfirm.setActivated(!hasFocus);
            binding.rlPassword.setActivated(hasFocus);
            int length = binding.edtPwd.getText().length();
            binding.ivPwdClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
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
                if (s.length() == 4 && isValidPwdConfirm() && cbAgree()) {
                    binding.btnSignUp.setEnabled(true);
                }
            }
        });
        binding.edtPwdConfirm.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        binding.edtPwdConfirm.setOnEditorActionListener(((v, actionId, event) -> {
            return false;
        }));

        binding.edtPwdConfirm.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            binding.rlPassword.setActivated(!hasFocus);
            binding.rlPasswordConfirm.setActivated(hasFocus);
            int length = binding.edtPwdConfirm.getText().length();
            binding.ivPwdConfirmClear.setVisibility(hasFocus && length > 0 ? View.VISIBLE : View.GONE);
        });

        binding.edtPwdConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.ivPwdConfirmClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                if (s.length() == 4 && isValidPwd() && isValidPwdConfirm() && cbAgree()) {
                    binding.btnSignUp.setEnabled(true);
                }
            }
        });

    }

    private boolean cbAgree() {
        return binding.cbAgree.isSelected();
    }

    private void setPwdConfirmFocus() {
        binding.edtPwdConfirm.requestFocus();
        Editable text = binding.edtPwdConfirm.getText();
        if (TextUtils.isEmpty(text)) {
            binding.edtPwdConfirm.setSelection(0);
        } else {
            binding.edtPwdConfirm.setSelection(text.length());
        }
    }

    private boolean isValidPwdConfirm() {
        return binding.edtPwdConfirm.getText() != null && RegexUtils.isPwd(binding.edtPwdConfirm.getText().toString()) && binding.edtPwd.getText().toString().equals(binding.edtPwdConfirm.getText().toString());
    }

    private boolean isValidPwd() {
        return binding.edtPwd.getText() != null && RegexUtils.isPwd(binding.edtPwd.getText().toString())&& binding.edtPwd.getText().toString().equals(binding.edtPwdConfirm.getText().toString());
    }

    private void showSoftInput() {
        binding.edtPwd.requestFocus();
        binding.edtPwd.setSelection(binding.edtPwd.getText().length());
        InputMethodManager inputManager = (InputMethodManager) binding.edtPwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(binding.edtPwd, 0);
        }

    }

    private void hideSoftInput() {
        InputMethodManager inputManager = (InputMethodManager) binding.edtPwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && binding.edtPwd.getWindowToken() != null) {
            inputManager.hideSoftInputFromWindow(binding.edtPwd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    protected void onDestroy() {
        hideSoftInput();
        CommonUtils.disposable(quickRegistrationObserver);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == binding.cbAgree) {
            boolean selected = v.isSelected();
            v.setSelected(!selected);
            binding.btnSignUp.setEnabled(!selected && isValidPwd() && isValidPwdConfirm());
        } else if (v == binding.btnSignUp) {
            FirebaseUtils.logEvent("fireb_click_register");
            quickRegistration();
        } else if (v == binding.ivBackReg) {
            finish();
        } else if (v == binding.ivPwdClear) {
            binding.edtPwd.setText("");
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
        } else if (v == binding.ivPwdConfirmClear) {
            binding.edtPwdConfirm.setText("");
        } else if (v == binding.ivPwdConfirmStyle) {
            boolean select = v.isSelected();
            if (!select) {
                //如果选中，显示密码
                binding.edtPwdConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.ivPwdConfirmStyle.setImageResource(R.mipmap.btn_password_show);
                binding.edtPwdConfirm.setSelection(binding.edtPwdConfirm.getText().length());
            } else {
                //否则隐藏密码
                binding.edtPwdConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.ivPwdConfirmStyle.setImageResource(R.mipmap.btn_password_hide);
                binding.edtPwdConfirm.setSelection(binding.edtPwdConfirm.getText().length());
            }
            v.setSelected(!select);
        } else  if (v == binding.tvPrivacy) {
            AgreeTermsFragment.createBuilder(this, getSupportFragmentManager())
                    .setMessage(Constants.TWO)
                    .show();
        }
    }

    private NetObserver quickRegistrationObserver;

    private void quickRegistration() {
        String pwd = MD5Util.encryption(binding.edtPwd.getText().toString());
        String pwdConfirm = MD5Util.encryption(binding.edtPwdConfirm.getText().toString());

        if (!pwd.equals(pwdConfirm)) {
            ToastManager.show(SetPasswordActivity.this, getString(R.string.str_Inconsistent_passwords));
        }
        Observable observable = NetManager.getApiService().registration(pwd, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(quickRegistrationObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        quickRegistrationObserver = new NetObserver<Response<RegistrationBO>>() {
            @Override
            public void onNext(Response<RegistrationBO> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    FirebaseUtils.logEvent("fireb_register");
//                    FirebaseLogUtils.Log("af_registered_successful");
                    KvStorage.put(LocalConfig.LC_ISLOGIN, true);
                    KvStorage.put(LocalConfig.LC_TOKEN, response.getBody().getToken());
                    KvStorage.put(LocalConfig.LC_ACCOUNTID, response.getBody().getAccountId());
                    KvStorage.put(LocalConfig.LC_MOBILE, response.getBody().getMobile());
                    Boolean mShowPermission = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_HASSHOWPERMISSION), false);
                    KvStorage.put(LocalConfig.LC_PASSWORD, pwd);
                    Intent intent;
                    if (mShowPermission){
                        intent = new Intent(SetPasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }else {
                        intent = new Intent(SetPasswordActivity.this, PermissionActivity.class);
                    }
                    startActivity(intent);
                } else if (response.getStatus().getCode() == ApiServerImpl.PHONE_HAS_REG) {
                    showToast(response.getStatus().getMsg());
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(quickRegistrationObserver);

    }

}