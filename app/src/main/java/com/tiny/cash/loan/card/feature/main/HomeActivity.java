package com.tiny.cash.loan.card.feature.main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.feature.account.LoginActivity;
import com.tiny.cash.loan.card.feature.account.RegisterActivity;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityHomeBinding;
import com.tiny.cash.loan.card.utils.DeviceInfo;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.NetUtil;
import com.tiny.cash.loan.card.utils.ui.ToastManager;
import com.tiny.cash.loan.card.utils.Utils;

/**
 * Created by WANG on 2020/10/10.
 */

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
        initHeardData();
    }

    /**
     * 初始化请求头里参数
     */
    private void initHeardData() {
        DeviceInfo.getInstance(this).getVerName();
        DeviceInfo.getInstance(this).getVersionCode();
        DeviceInfo.getInstance(this).getChannelName();
        DeviceInfo.getInstance(this).init();
    }


    private void initView() {
        mBinding.btnRegister.setOnClickListener(view ->
                startIntent(RegisterActivity.class));

        mBinding.btnSignIn.setOnClickListener(view -> startIntent(LoginActivity.class));
        if (!Utils.hasSimCard(this)) {
            ToastManager.show(this, getString(R.string.str_insert_sim_card));
            finish();
        }
        if (!NetUtil.isNetworkAvailable(this)) {
            ToastManager.show(this, getString(R.string.str_not_network));
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //修改背景为白色
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

}
