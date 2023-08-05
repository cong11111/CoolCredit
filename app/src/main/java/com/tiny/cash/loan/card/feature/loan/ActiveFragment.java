package com.tiny.cash.loan.card.feature.loan;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanActiveBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.AppStarsDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.fragment.OfflinePaymentTransferFragment;
import com.tiny.cash.loan.card.feature.repayment.PaymentMethodActivity;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActiveFragment extends BaseFragment {

    private LayoutLoanActiveBinding mBinding;
    private LoanOrderDetail data;
    public static ActiveFragment newInstance() {
        Bundle args = new Bundle();
        ActiveFragment fragment = new ActiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanActiveBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_active, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    private void queryBankStatus() {
        LoanOrderHelp.getInstant().existBankCard(it -> {
            if (it == null)
                return;
//            if (MrConstant.ZERO.equals(it.getCardExist())) {//未绑定银行卡
//                TipsDialogFragment.createBuilder(getContext(), getChildFragmentManager())
//                        .setMessage(it.getCardNotExistMessage())
//                        .setPositiveButtonTxt("Ignore")
//                        .setNegativeButtonTxt("Go binding card")
//                        .setNegativeListener(() -> {
//                            Intent intent = new Intent(getContext(), AddMoreBankCardActivity.class);
//                            intent.putExtra("type", "repay");
//                            getContext().startActivity(intent);
//                        }).show();
//            }else {
//                PaymentDialogFragment.createBuilder(getContext(), getChildFragmentManager())
//                        .setMessage(data.getOrderId(), data.getTotalAmount())
//                        .show();
//            }

            Intent intent = new Intent();
            intent.setClass(getActivity(),
                    PaymentMethodActivity.class);
            intent.putExtra("orderId",data.getOrderId());
            intent.putExtra("totalAmount",data.getTotalAmount());
            startActivity(intent);
        });
    }
    public String split(String s){
        if (s.length()>2) {
            return s.substring(0, s.length() - 2);
        }
        return "0";
    }
    private void initData() {
      boolean showReloan = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_RELOAN), false);
        if (showReloan) {
            boolean showFlag = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS2), true);
            if (showFlag) {
                AppStarsDialogFragment.createBuilder(getContext(), getChildFragmentManager())
                        .setNegativeListener(li -> {
                            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS2), false);
                            AppUtils.launchAppDetail(KudiCreditApp.getInstance());
                        }).show();
            }
        }
        boolean orderSucess = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_ORDERSUCESS), false);
        if (!orderSucess) {
            if (LocalConfig.isNewUser())
                FirebaseLogUtils.Log("af_new_active");
            else
                FirebaseLogUtils.Log("af_old_active");
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_ORDERSUCESS), true);
        }
        data = (LoanOrderDetail) getArguments().get("data");
        List<LoanOrderDetail.StageListBean> stageList = data.getStageList();
        mBinding.tvAllAmount.setText(getString(R.string.str_money, split(data.getTotalAmount())));
        mBinding.llLoanDetail.rlTermsTwo.setVisibility(View.GONE);

        for (LoanOrderDetail.StageListBean trialBean : stageList) {
            if (Constants.ONE.equals(trialBean.getStageNo())) {
                mBinding.llLoanDetail.tvFirstInterest.setText(getString(R.string.str_money, split(trialBean.getInterest())));
                mBinding.llLoanDetail.tvFirstFee.setText(getString(R.string.str_money, split(trialBean.getFee())));
                mBinding.llLoanDetail.tvFirstDisburse.setText(getString(R.string.str_money, split(trialBean.getAmount())));
                mBinding.llLoanDetail.tvFirstRepayment.setText(getString(R.string.str_money, split(trialBean.getTotalAmount())));
                mBinding.llLoanDetail.tvFirstRepayDate.setText(trialBean.getRepayDate());
                mBinding.llLoanDetail.btnFirstRepaymemt.setVisibility(trialBean.getPayable() ? View.VISIBLE : View.GONE);

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
                mBinding.llLoanDetail.btnSecondRepaymemt.setVisibility(trialBean.getPayable() ? View.VISIBLE : View.GONE);

                mBinding.llLoanDetail.btnSecondRepaymemt.setOnClickListener(v -> {
                    queryBankStatus();
                });
            }
        }
    }
}
