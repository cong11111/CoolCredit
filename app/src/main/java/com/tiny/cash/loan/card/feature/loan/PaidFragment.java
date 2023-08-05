package com.tiny.cash.loan.card.feature.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanPaidBinding;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PaidFragment extends BaseFragment{

    private LayoutLoanPaidBinding mBinding;

    public static PaidFragment newInstance() {
        Bundle args = new Bundle();
        PaidFragment fragment = new PaidFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanPaidBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_paid, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    private void initData() {
        if (LocalConfig.isNewUser())
            FirebaseLogUtils.Log("af_new_repay");
        else
            FirebaseLogUtils.Log("af_old_repay");
        LoanOrderDetail data = (LoanOrderDetail) getArguments().get("data");
        List<LoanOrderDetail.StageListBean> stageList = data.getStageList();
        mBinding.tvAllAmount.setText(getString(R.string.str_money,data.getTotalAmount()));
        mBinding.llLoanDetail.rlTermsTwo.setVisibility(View.GONE);

        for (LoanOrderDetail.StageListBean trialBean : stageList) {
            if (Constants.ONE.equals(trialBean.getStageNo())) {
                mBinding.llLoanDetail.tvFirstInterest.setText(getString(R.string.str_money,trialBean.getInterest()));
                mBinding.llLoanDetail.tvFirstFee.setText(getString(R.string.str_money,trialBean.getFee()));
                mBinding.llLoanDetail.tvFirstDisburse.setText(getString(R.string.str_money,trialBean.getAmount()));
                mBinding.llLoanDetail.tvFirstRepayment.setText(getString(R.string.str_money,trialBean.getTotalAmount()));
                mBinding.llLoanDetail.tvFirstRepayDate.setText(trialBean.getRepayDate());
            } else {
                mBinding.llLoanDetail.rlTermsTwo.setVisibility(View.VISIBLE);

                mBinding.llLoanDetail.tvSecondInterest.setText(getString(R.string.str_money,trialBean.getInterest()));
                mBinding.llLoanDetail.tvSecondFee.setText(getString(R.string.str_money,trialBean.getFee()));
                mBinding.llLoanDetail.tvSecondDisburse.setText(getString(R.string.str_money,trialBean.getAmount()));
                mBinding.llLoanDetail.tvSecondRepayment.setText(getString(R.string.str_money,trialBean.getTotalAmount()));
                mBinding.llLoanDetail.tvSecondRepayDate.setText(trialBean.getRepayDate());
            }
        }
    }
}
