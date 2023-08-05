package com.tiny.cash.loan.card.feature.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanDeclinedBinding;

import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiny.cash.loan.card.message.EventMessage;

public class DeclinedFragment extends BaseFragment {

    private LayoutLoanDeclinedBinding mBinding;

    public static DeclinedFragment newInstance() {
        Bundle args = new Bundle();
        DeclinedFragment fragment = new DeclinedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanDeclinedBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_declined, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    private void initData() {
        LoanOrderDetail data = (LoanOrderDetail) getArguments().get("data");
        if (data != null) {
            String reason = data.getReason();
//            String limitday = data.getLimitday();
//            mBinding.tvReappaly.setText(getString(R.string.str_reapply,limitday));
            mBinding.tvReason.setText(reason);
            boolean canApply = data.isCanApply();
            if (canApply){
                mBinding.btnApplyAgain.setVisibility(View.VISIBLE);
            }else {
                mBinding.btnApplyAgain.setVisibility(View.GONE);
            }
        }
        mBinding.btnApplyAgain.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventMessage(EventMessage.LOANDECLINED));
        });
    }
}
