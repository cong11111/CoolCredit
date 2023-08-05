package com.tiny.cash.loan.card.feature.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.feature.main.MainActivity;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityPermissionBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.AgreeTermsFragment;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;


public class PermissionActivity extends BaseActivity implements View.OnClickListener {

    private ActivityPermissionBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_permission);
        initview();
    }

    private void initview() {
        mBinding.btnAllow.setOnClickListener(this);
        mBinding.btnDisagree.setOnClickListener(this);
        mBinding.tvPrivacy.setOnClickListener(v -> {
            AgreeTermsFragment.createBuilder(this, getSupportFragmentManager())
                    .setMessage(Constants.TWO)
                    .show();
        });
    }



    @Override
    public void onClick(View view) {
        if (view == mBinding.btnAllow) {
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_HASSHOWPERMISSION),true);
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (view == mBinding.btnDisagree) {
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_HASSHOWPERMISSION), false);
            finish();
        }
    }
}
