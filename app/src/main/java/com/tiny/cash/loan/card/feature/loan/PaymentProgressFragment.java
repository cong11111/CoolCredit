package com.tiny.cash.loan.card.feature.loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LoanPaymentProgressBinding;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiny.cash.loan.card.message.EventMessage;

public class PaymentProgressFragment extends BaseFragment{

    private LoanPaymentProgressBinding mBinding;

    public static PaymentProgressFragment newInstance() {
        Bundle args = new Bundle();
        PaymentProgressFragment fragment = new PaymentProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LoanPaymentProgressBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.loan_payment_progress, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.btnRefresh.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
        });
    }

}
