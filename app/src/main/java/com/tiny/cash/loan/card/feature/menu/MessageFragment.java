package com.tiny.cash.loan.card.feature.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.adapter.MessageAdapter;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentMessageBinding;
import com.tiny.cash.loan.card.message.MessageDetailsActivity;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.MessageResult;
import com.tiny.cash.loan.card.net.response.Response;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageFragment extends BaseFragment {
    private FragmentMessageBinding mBinding;
    private List<MessageResult.ItemsBean> mList = new ArrayList<>();

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMessageBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private  void initData(){
        getListMessage();
    }
    NetObserver messageObserver;

    private void getListMessage() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().getListMessage(accountId,"1","5000")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(messageObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        messageObserver = new NetObserver<Response<MessageResult>>() {
            @Override
            public void onNext(Response<MessageResult> response) {
                dismissProgressDialogFragment();

                if (response.isSuccess()) {
                    mList = response.getBody().getItems();
                    if (mList.size() == 0){
                        mBinding.recyclerView.setVisibility(View.GONE);
                        mBinding.layoutEmpty.setVisibility(View.VISIBLE);
                    }else {
                        mBinding.recyclerView.setVisibility(View.VISIBLE);
                        mBinding.layoutEmpty.setVisibility(View.GONE);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    mBinding.recyclerView.setLayoutManager(layoutManager);
                    MessageAdapter adapter = new MessageAdapter(getContext());
                    adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
                        @Override
                        public void itemClick(MessageResult.ItemsBean item) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(),
                                    MessageDetailsActivity.class);
                            intent.putExtra("bean",item);
                            startActivity(intent);
                        }
                    });
                    adapter.setData(mList);
                    mBinding.recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(messageObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(messageObserver);
    }
}
