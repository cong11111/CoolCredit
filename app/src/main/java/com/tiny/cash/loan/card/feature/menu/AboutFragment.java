package com.tiny.cash.loan.card.feature.menu;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentAboutBinding;
import com.tiny.cash.loan.card.feature.start.AgreementActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AboutFragment extends BaseFragment implements View.OnClickListener{
    private FragmentAboutBinding mBinding;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAboutBinding.inflate(inflater, container, false);
        //DataBindingUtil.inflate(inflater, R.layout.fragment_about, container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }
    Intent intent;
    private void initView(){
        String appVersion = null;
        try {
            appVersion = getContext().getPackageManager().
                     getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mBinding.tvVersionCode.setText(appVersion);
        mBinding.rlPrivacyPolicy.setOnClickListener(this);
        mBinding.rlTermsOfService.setOnClickListener(this);
         intent = new Intent(getContext(),AgreementActivity.class);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rl_Terms_of_Service:
                intent.putExtra("type", Constants.ONE);
               startActivity(intent);
                break;
            case R.id.rl_Privacy_Policy:
                intent.putExtra("type", Constants.TWO);
                startActivity(intent);
                break;
            case R.id.rl_Version_Update:
                break;
        }
    }
}
