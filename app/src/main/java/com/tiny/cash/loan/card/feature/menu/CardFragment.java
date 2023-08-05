package com.tiny.cash.loan.card.feature.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.adapter.BankCardAdapter;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentBankcardBinding;
import com.tiny.cash.loan.card.feature.bank.AddMoreBankCardActivity;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankList;
import com.tiny.cash.loan.card.net.response.Response;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CardFragment extends BaseFragment {
    private FragmentBankcardBinding mBinding;
    private BankCardAdapter adapter;

    public static CardFragment newInstance() {
        Bundle args = new Bundle();
        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bankcard, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View foodView = LayoutInflater.from(getActivity()).inflate(R.layout.card_foodview, null);
        mBinding.listView.addFooterView(foodView);
        LinearLayout layout = foodView.findViewById(R.id.add_newCard);
        adapter = new BankCardAdapter(getContext());
        mBinding.listView.setAdapter(adapter);
        String str = KvStorage.get(LocalConfig.LC_REPAYCHANNEL, "");
        boolean paystack_card = str.contains("paystack_card") ? true : false;

        if (paystack_card)
            layout.setVisibility(View.VISIBLE);
        else
            layout.setVisibility(View.GONE);

        layout.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddMoreBankCardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        QueryBankDetail();
    }

    NetObserver bankObserver;

    private void QueryBankDetail() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().queryBankCardList(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bankObserver);
        bankObserver = new NetObserver<Response<BankList>>() {
            @Override
            public void onNext(Response<BankList> response) {
                if (response.isSuccess()) {
                    List<BankList.CardList> cardlist = response.getBody().getCardlist();
                    adapter.setData(cardlist);
                } else {
                    ToastManager.show(getContext(), response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(bankObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(bankObserver);
    }
}
