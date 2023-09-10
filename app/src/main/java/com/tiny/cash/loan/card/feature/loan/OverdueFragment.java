package com.tiny.cash.loan.card.feature.loan;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiny.cash.loan.card.Constant;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanOverdueBinding;
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;
import com.tiny.cash.loan.card.ui.dialog.fragment.OfflinePaymentTransferFragment;
import com.tiny.cash.loan.card.ui.pay2.PayActivity2;
import com.tiny.cash.loan.card.utils.FirebaseUtils;

import java.util.List;

public class OverdueFragment extends BaseFragment {

    private LayoutLoanOverdueBinding mBinding;
    private LoanOrderDetail data;

    public static OverdueFragment newInstance() {
        Bundle args = new Bundle();
        OverdueFragment fragment = new OverdueFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanOverdueBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_overdue, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        mBinding.btnRepaymemt.setOnClickListener(v -> {
            queryBankStatus();
        });
        mBinding.btnOfflinePaymentTransfer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBinding.btnOfflinePaymentTransfer.setOnClickListener(v -> {
            OfflinePaymentTransferFragment.createBuilder(getContext(), getChildFragmentManager())
                    .show();
        });
    }

    private void initView() {
        mBinding.llLoanDetail.rlFirstOverdueFee.setVisibility(View.VISIBLE);
        mBinding.llLoanDetail.rlSecondOverdueFee.setVisibility(View.VISIBLE);
    }

    private void queryBankStatus() {
        LoanOrderHelp.getInstant().existBankCard(it -> {
            if (it == null)
                return;
//            Intent intent = new Intent();
//            intent.setClass(getActivity(),
//                    PaymentMethodActivity.class);
//            intent.putExtra("orderId",data.getOrderId());
//            intent.putExtra("totalAmount",data.getTotalAmount());
//            startActivity(intent);
            PayActivity2.Companion.launchPayActivity(getActivity(), data.getOrderId(), data.getTotalAmount());
//            startActivity(intent);
        });
    }
    public String split(String s){
        if (TextUtils.isEmpty(s)) {
            return "0";
        }
        if (s.length()>2) {
            return s.substring(0, s.length() - 2);
        }
        return "0";
    }
    private void initData() {
//        if (LocalConfig.isNewUser())
//            FirebaseLogUtils.Log("af_new_overdue");
//        else
//            FirebaseLogUtils.Log("af_old_overdue");
        data = (LoanOrderDetail) getArguments().get("data");
        if (data != null && checkNeedShowLog(data.getOrderId())) {
            if (Constant.Companion.getIS_FIRST_APPLY()) {
                FirebaseUtils.logEvent("fireb_overdue");
            }
            FirebaseUtils.logEvent("fireb_overdue_all");
        }
        List<LoanOrderDetail.StageListBean> stageList = data.getStageList();
        mBinding.tvAllAmount.setText(getString(R.string.str_money, split(data.getTotalAmount())));
        mBinding.llLoanDetail.rlTermsTwo.setVisibility(View.GONE);
        if (stageList != null) {
            for (LoanOrderDetail.StageListBean trialBean : stageList) {
                if (Constants.ONE.equals(trialBean.getStageNo())) {
                    mBinding.llLoanDetail.tvFirstInterest.setText(getString(R.string.str_money, split(trialBean.getInterest())));
                    mBinding.llLoanDetail.tvFirstFee.setText(getString(R.string.str_money, split(trialBean.getFee())));
                    mBinding.llLoanDetail.tvFirstDisburse.setText(getString(R.string.str_money, split(trialBean.getAmount())));
                    mBinding.llLoanDetail.tvFirstRepayment.setText(getString(R.string.str_money, split(trialBean.getTotalAmount())));
                    mBinding.llLoanDetail.tvFirstRepayDate.setText(trialBean.getRepayDate());
                    mBinding.llLoanDetail.tvFirstOverdueFee.setText(getString(R.string.str_money, split(trialBean.getPenalty())));
                    mBinding.llLoanDetail.btnFirstRepaymemt.setVisibility(trialBean.getPayable() ? View.VISIBLE : View.GONE);
                    mBinding.llLoanDetail.btnFirstRepaymemt.setVisibility(View.GONE);
                    mBinding.llLoanDetail.btnFirstRepaymemt.setOnClickListener(v -> {
                        queryBankStatus();
                    });
                } else {
                    mBinding.llLoanDetail.rlTermsTwo.setVisibility(View.VISIBLE);
                    mBinding.llLoanDetail.tvSecondInterest.setText(getString(R.string.str_money, split(trialBean.getInterest())));
                    mBinding.llLoanDetail.tvSecondFee.setText(getString(R.string.str_money, split(trialBean.getFee())));
                    mBinding.llLoanDetail.tvSecondDisburse.setText(getString(R.string.str_money, split(trialBean.getAmount())));
                    mBinding.llLoanDetail.tvSecondRepayment.setText(getString(R.string.str_money, split(trialBean.getTotalAmount())));
                    mBinding.llLoanDetail.tvSecondRepayDate.setText(trialBean.getRepayDate());
                    mBinding.llLoanDetail.tvSecondOverdueFee.setText(getString(R.string.str_money, split(trialBean.getPenalty())));
//                    mBinding.llLoanDetail.btnSecondRepaymemt.setVisibility(trialBean.getPayable() ? View.VISIBLE : View.GONE);
                    mBinding.llLoanDetail.btnSecondRepaymemt.setVisibility(View.GONE);
                    mBinding.llLoanDetail.btnSecondRepaymemt.setOnClickListener(v -> {
                        queryBankStatus();
                    });
                }
            }
        }
    }
}
