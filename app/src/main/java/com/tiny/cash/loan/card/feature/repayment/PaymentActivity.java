package com.tiny.cash.loan.card.feature.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.adapter.PayMentAdapter;
import com.tiny.cash.loan.card.feature.start.AgreementActivity;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityPaymentBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.PayStackStatusDialogFragment;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankList;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.response.data.order.PayStackBean;
import com.tiny.cash.loan.card.net.response.data.order.PayStackResult;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.Nullable;

import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PaymentActivity extends BaseActivity {
    private PayMentAdapter adapter;
    String mOrderId, mTotalAmount;
    private ActivityPaymentBinding mBinding;
    private boolean doubleClick = true;
    List<BankList.CardList> cardList;
    static String mReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_payment);

        View foodView = LayoutInflater.from(this).inflate(R.layout.paystack_foodview, null);
        mBinding.recyclerView.addFooterView(foodView);

        mTotalAmount = getIntent().getStringExtra("totalAmount");
        mOrderId = getIntent().getStringExtra("orderId");

        adapter = new PayMentAdapter(this);
        cardList = (List<BankList.CardList>) getIntent().getSerializableExtra("data");
        adapter.setData(cardList);
        mBinding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        LinearLayout layout = foodView.findViewById(R.id.add_newCard);

        layout.setOnClickListener(view1 -> {
            QueryPayStackUrl();
        });

        mBinding.btnApplyAgain.setOnClickListener(v -> {
            if (!doubleClick)
                return;
            if (adapter.getSelectPosition() == -1) {
                ToastManager.show(this, "This not null");
                return;
            }
            doubleClick = false;
            String cardNumber = adapter.getSelectData();
            SubmitOrderRepay(mOrderId, mTotalAmount, cardNumber);
        });
        mBinding.llTitle.ivBack.setOnClickListener(v -> {
            finish();
        });
        mBinding.llTitle.tvTitle.setText(getString(R.string.str_payment));

    }

    NetObserver payStackObserver;

    public void QueryPayStackUrl() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().getPayStackUrl(accountId, mOrderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(payStackObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        payStackObserver = new NetObserver<Response<PayStackBean>>() {
            @Override
            public void onNext(Response<PayStackBean> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    mReference = response.getBody().getReference();
                    Log.d("TAG1111", "mReference: "+mReference);
                    Intent intent = new Intent(PaymentActivity.this, AgreementActivity.class);
                    intent.putExtra("type", Constants.THREE);
                    intent.putExtra("orderId", mOrderId);
                    intent.putExtra("reference", mReference);
                    intent.putExtra("Url", response.getBody().getAuthorizationURL());
                    startActivityForResult(intent,1000);
                } else {
                    ToastManager.show(PaymentActivity.this, response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(payStackObserver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000){
            getPayStackResult();
        }
    }


    NetObserver mObserver;

    private void getPayStackResult() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().payStackResult(accountId, mOrderId, mReference)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(mObserver);

        mObserver = new NetObserver<Response<PayStackResult>>() {
            @Override
            public void onNext(Response<PayStackResult> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    String status = response.getBody().getStatus();

                    if ("pending".equals(status)) {
                        EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
                        finish();
                    } else {
                        PayStackStatusDialogFragment.
                                createBuilder(PaymentActivity.this, getSupportFragmentManager())
                                .setStatus(status)
                                .setNegativeListener(() -> {
                                    EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
                                    finish();
                                }).show();
                    }
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(payStackObserver);
        CommonUtils.disposable(Observer);
        CommonUtils.disposable(mObserver);
    }


    NetObserver Observer;


    private void SubmitOrderRepay(String orderId, String amount, String card_number) {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        showProgressDialogFragment(getString(R.string.str_loading), false);
        Observable observable = NetManager.getApiService().SubmitOrderRepay(accountId, orderId, amount, card_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(Observer);

        Observer = new NetObserver<Response<OrderStatus>>() {
            @Override
            public void onNext(Response<OrderStatus> response) {
                dismissProgressDialogFragment();
                doubleClick = true;
                if (response.isSuccess()) {
                    if (Constants.ONE.equals(response.getBody().getStatus())) {
                        FirebaseLogUtils.Log("PayMent");
                        EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
                        finish();
                    } else {
                        showToast(response.getStatus().getMsg());
                    }
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                doubleClick = true;
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(Observer);
    }


}
