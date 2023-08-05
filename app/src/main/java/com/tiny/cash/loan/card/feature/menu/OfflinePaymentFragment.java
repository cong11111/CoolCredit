package com.tiny.cash.loan.card.feature.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentOfflinePaymentBinding;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OfflinePaymentFragment extends BaseFragment {
    private FragmentOfflinePaymentBinding mBinding;

    public static OfflinePaymentFragment newInstance() {
        Bundle args = new Bundle();
        OfflinePaymentFragment fragment = new OfflinePaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentOfflinePaymentBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.fragment_offline_payment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String mBank = KvStorage.get(LocalConfig.LC_BANK, "");

        String mName = KvStorage.get(LocalConfig.LC_ACCOUNTNAME, "");
        String mNumber = KvStorage.get(LocalConfig.LC_ACCOUNTNUMBER, "");

        mBinding.tvAccountName.setText(mName);
        mBinding.tvAccountNumber.setText(mNumber);
        mBinding.tvAccountBank.setText(mBank);

        mBinding.tvCopy.setOnClickListener(v->{
            ToastManager.show(requireContext(), "Copied");
            AppUtils.copy(mNumber,getContext());
        });
    }


}
