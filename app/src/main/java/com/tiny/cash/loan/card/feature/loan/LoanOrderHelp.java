package com.tiny.cash.loan.card.feature.loan;

import com.tiny.cash.loan.card.ui.dialog.iface.ICompleteListener;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankResult;
import com.tiny.cash.loan.card.net.response.Response;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoanOrderHelp {
    static LoanOrderHelp mInstant = null;

    public static LoanOrderHelp getInstant() {
        if (mInstant == null) {
            mInstant = new LoanOrderHelp();
        }
        return mInstant;
    }

    NetObserver Observer, bindCardObserver;

    public void bindCardNotify(ICompleteListener<BankResult> listener) {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String mToken = KvStorage.get(LocalConfig.LC_TOKEN, "");
        Observable observable = NetManager.getApiService().bindCardNotify(accountId, mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bindCardObserver);
        bindCardObserver = new NetObserver<Response<BankResult>>() {
            @Override
            public void onNext(Response<BankResult> response) {
                if (response.isSuccess()) {
                    listener.onComplete(response.getBody());
                }
            }

            @Override
            public void onException(ResponseException netException) {

            }
        };
        observable.subscribeWith(bindCardObserver);
    }

    public void existBankCard(ICompleteListener<BankResult> listener) {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String mToken = KvStorage.get(LocalConfig.LC_TOKEN, "");
        Observable observable = NetManager.getApiService().existBankCard(accountId, mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(Observer);
        Observer = new NetObserver<Response<BankResult>>() {
            @Override
            public void onNext(Response<BankResult> response) {
                if (response.isSuccess()) {
                    listener.onComplete(response.getBody());
                }
            }

            @Override
            public void onException(ResponseException netException) {

            }
        };
        observable.subscribeWith(Observer);
    }

    public void onDestroy() {
        CommonUtils.disposable(Observer);
        CommonUtils.disposable(bindCardObserver);
    }

}
