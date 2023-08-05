package com.tiny.cash.loan.card.feature.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanProcessingBinding;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProcessingFragment extends BaseFragment{

    private LayoutLoanProcessingBinding mBinding;

    public static ProcessingFragment newInstance() {
        Bundle args = new Bundle();
        ProcessingFragment fragment = new ProcessingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanProcessingBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_processing, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    public String split(String s){
        if (s.length()>2) {
            return s.substring(0, s.length() - 2);
        }
        return "0";
    }

    private void initData() {
//        boolean showFlag =  KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS),true);
//        if (showFlag){
//            AppStarsDialogFragment.createBuilder(getContext(), getChildFragmentManager())
//                    .setNegativeListener(li->{
//                        KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS),false);
//                        AppUtils.launchAppDetail(App.getInstance());
//                    }).show();
//        }

        boolean orderProgress = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_ORDERPROGRESS), false);
        if (!orderProgress) {
            if (LocalConfig.isNewUser())
                FirebaseLogUtils.Log("af_new_progress");
            else
                FirebaseLogUtils.Log("af_old_progress");
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_ORDERPROGRESS), true);
        }

        LoanOrderDetail data = (LoanOrderDetail) getArguments().get("data");
        mBinding.tvAllAmount.setText(getString(R.string.str_money,split(data.getTotalAmount())));
        String msg = KvStorage.get(LocalConfig.LC_AUDITMSG, "");
        mBinding.tvShowMsg.setText(msg);
    }

}
