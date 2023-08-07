package com.tiny.cash.loan.card.feature.repayment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flutterwave.raveandroid.BuildConfig;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RaveUiManager;
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants;
import com.google.gson.Gson;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.feature.start.AgreementActivity;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityPaywayBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.PayStackStatusDialogFragment;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseLogUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankList;
import com.tiny.cash.loan.card.net.response.data.bean.FlutterWaveResult;
import com.tiny.cash.loan.card.net.response.data.bean.MonifyResult;
import com.tiny.cash.loan.card.net.response.data.bean.RedoclyBean;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.response.data.order.PayStackBean;
import com.tiny.cash.loan.card.net.response.data.order.PayStackResult;
import com.tiny.cash.loan.card.net.response.data.order.FlutterWaveBean;
import com.tiny.cash.loan.card.net.response.data.order.VerifyFlutterBean;
import com.tiny.cash.loan.card.net.request.params.FlutterWaveParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPaywayBinding mBinding;
    String mOrderId, mTotalAmount;
    String mTxRef;
    String mJsonString;
    static String mReference1;
    boolean gotoBankCardFlag = false;
    private boolean paystack_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPaywayBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_payway);
        initData();
        initPayAccount();
        queryMonify();
        if (paystack_card) {
            mBinding.llQuickRepayment.setVisibility(View.VISIBLE);
            QueryBankDetail();
        }else {
            mBinding.llQuickRepayment.setVisibility(View.GONE);
        }
    }

    NetObserver monifyObserver;

    public void queryMonify() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().queryMonify(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(monifyObserver);
        monifyObserver = new NetObserver<Response<MonifyResult>>() {
            @Override
            public void onNext(Response<MonifyResult> response) {
                if (response.isSuccess()) {
                    MonifyResult body = response.getBody();
                    mBinding.tvAccountName.setText(body.getAccountName());
                    mBinding.tvAccountNumber.setText(body.getAccountNumber());
                    mBinding.tvAccountBank.setText(body.getBankName());
                } else {
                    String mBank = KvStorage.get(LocalConfig.LC_BANK, "");
                    String mName = KvStorage.get(LocalConfig.LC_ACCOUNTNAME, "");
                    String mNumber = KvStorage.get(LocalConfig.LC_ACCOUNTNUMBER, "");
                    mBinding.tvAccountName.setText(mName);
                    mBinding.tvAccountNumber.setText(mNumber);
                    mBinding.tvAccountBank.setText(mBank);
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(monifyObserver);
    }

    private void initData() {
        String str = KvStorage.get(LocalConfig.LC_REPAYCHANNEL, "");
        boolean paystack_h5 = str.contains("paystack_h5") ? true : false;
        boolean flutterwave_h5 = str.contains("flutterwave_h5") ? true : false;
        boolean monnify_account = str.contains("monnify_account") ? true : false;
        boolean redocly_h5 = str.contains("redocly_h5") ? true : false;
        paystack_card = str.contains("paystack_card") ? true : false;

        if (paystack_h5)
            mBinding.btnPaystack.setVisibility(View.VISIBLE);
        else
            mBinding.btnPaystack.setVisibility(View.GONE);

        if (flutterwave_h5)
            mBinding.btnFlutterwave.setVisibility(View.VISIBLE);
        else
            mBinding.btnFlutterwave.setVisibility(View.GONE);

        if (monnify_account)
            mBinding.llMonify.setVisibility(View.VISIBLE);
        else
            mBinding.llMonify.setVisibility(View.GONE);

        if (redocly_h5)
            // TODO
            mBinding.btnRedoclyH5.setVisibility(View.GONE);
        else
            mBinding.btnRedoclyH5.setVisibility(View.GONE);

        mTotalAmount = getIntent().getStringExtra("totalAmount");
        mOrderId = getIntent().getStringExtra("orderId");
        mBinding.btnPaystack.setOnClickListener(this);
        mBinding.btnFlutterwave.setOnClickListener(this);
        mBinding.btnRedoclyH5.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
        mBinding.tvOk.setOnClickListener(this);
        mBinding.tvBankNum.setOnClickListener(this);
    }

    public void initPayAccount() {
        mBinding.tvCopy.setOnClickListener(v -> {
            ToastManager.show(this, "Copied");
            AppUtils.copy(mBinding.tvAccountNumber.getText().toString(), this);
        });

        mBinding.tvCopy1.setOnClickListener(v -> {
            ToastManager.show(this, "Copied");
            AppUtils.copy(mBinding.tvAccountNumber1.getText().toString(), this);
        });
        mBinding.tvAccountName1.setText("Bestfin Nigeria Limited");
        mBinding.tvAccountNumber1.setText("0089596848");
        mBinding.tvAccountBank1.setText("Sterling Bank");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gotoBankCardFlag && paystack_card) {
            QueryBankDetail();
            gotoBankCardFlag = false;
        }
    }

    String cardNumber;
    List<BankList.CardList> cardList;

    public void QueryBankDetail() {
        showProgressDialogFragment(getString(R.string.str_loading), false);
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().queryBankCardList(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(bankObserver);
        bankObserver = new NetObserver<Response<BankList>>() {
            @Override
            public void onNext(Response<BankList> response) {
                if (response.isSuccess()) {
                    dismissProgressDialogFragment();
                    cardList = response.getBody().getCardlist();
                    if (cardList != null && cardList.size() > 0) {
                        mBinding.tvOk.setClickable(true);
                        cardNumber = cardList.get(cardList.size() - 1).getCardNumber();
                        if (cardNumber.length() > 8) {
                            mBinding.tvBankNum.setText(cardNumber.substring(0, cardNumber.length() - 8) + "****" + cardNumber.substring(cardNumber.length() - 4));
                        }
                        mBinding.tvOk.setBackground(getResources().getDrawable(R.drawable.bg_bule_radius_10));
                    } else {
                        mBinding.tvOk.setClickable(false);
                        mBinding.tvBankNum.setText(getString(R.string.please_bind_your_bank_card));
                        mBinding.tvOk.setBackground(getResources().getDrawable(R.drawable.bg_gray_solid_radius));
                        mBinding.tvOk.setTextColor(getResources().getColor(R.color.color_FFFFFF));
                    }
                } else {
                    ToastManager.show(PaymentMethodActivity.this, response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(bankObserver);
    }

    NetObserver Observer;
    private boolean doubleClick = true;

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

    NetObserver redoclyObserver,payStackObserver, mFlutterWaveObserver, verifyObserver, bankObserver;

    public void queryRedoclyUrl() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().queryRedocly(accountId, mOrderId,mTotalAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(redoclyObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        redoclyObserver = new NetObserver<Response<RedoclyBean>>() {
            @Override
            public void onNext(Response<RedoclyBean> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    Intent intent = new Intent(PaymentMethodActivity.this, AgreementActivity.class);
                    intent.putExtra("type", Constants.FOUR);
                    intent.putExtra("Url", response.getBody().getPageURL());
                    startActivityForResult(intent, 1001);
                } else {
                    ToastManager.show(PaymentMethodActivity.this, response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(redoclyObserver);
    }
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
                    mReference1 = response.getBody().getReference();

                    Intent intent = new Intent(PaymentMethodActivity.this, AgreementActivity.class);
                    intent.putExtra("type", Constants.THREE);
                    intent.putExtra("orderId", mOrderId);
                    intent.putExtra("reference", mReference1);
                    intent.putExtra("Url", response.getBody().getAuthorizationURL());
                    startActivityForResult(intent, 1000);
                } else {
                    ToastManager.show(PaymentMethodActivity.this, response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(payStackObserver);
    }

    private void getFlutterWaveTxRef(String orderId, String chargeType) {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String token = KvStorage.get(LocalConfig.LC_TOKEN, "");

        Observable observable = NetManager.getApiService().getFlutterwaveTxRef(accountId, token, orderId, chargeType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        showProgressDialogFragment(getString(R.string.str_loading), false);
        CommonUtils.disposable(mFlutterWaveObserver);

        mFlutterWaveObserver = new NetObserver<Response<FlutterWaveBean>>() {
            @Override
            public void onNext(Response<FlutterWaveBean> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    flutterWave(response.getBody());
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(mFlutterWaveObserver);
    }

    private void uploadVerifyFlutterJson() {
        showProgressDialogFragment(getString(R.string.str_loading), false);
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        FlutterWaveParams params = new FlutterWaveParams();
        params.setAccountId(accountId);
        params.setOrderId(mOrderId);
        params.setChargeType("2");
        params.setTxRef(mTxRef);
        params.setJsonStr(mJsonString);
        Observable observable = NetManager.getApiService().uploadJson(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(verifyObserver);

        verifyObserver = new NetObserver<Response<VerifyFlutterBean>>() {
            @Override
            public void onNext(Response<VerifyFlutterBean> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    String status = response.getBody().getStatus();
                    if (status.equals("pending") || status.equals("success")) {
                        if (status.equals("pending")) {
                            KvStorage.put(LocalConfig.LC_TXREF, mTxRef);
                        } else {
                            KvStorage.put(LocalConfig.LC_TXREF, "");
                        }
                        EventBus.getDefault().post(new EventMessage(EventMessage.UPLOADACTIVITY));
                        finish();
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
        observable.subscribeWith(verifyObserver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Paystack:
                QueryPayStackUrl();
                break;
            case R.id.btn_Flutterwave:
                getFlutterWaveTxRef(mOrderId, "2");
                break;
            case R.id.btn_redocly_h5:
                queryRedoclyUrl();
                break;
            case R.id.tv_ok:
                if (!doubleClick)
                    return;
                doubleClick = false;
                SubmitOrderRepay(mOrderId, mTotalAmount, cardNumber);
                break;

            case R.id.tv_bankNum:
                gotoBankCardFlag = true;
                Intent intent = new Intent(PaymentMethodActivity.this, PaymentActivity.class);
                intent.putExtra("data", (Serializable) cardList);
                intent.putExtra("orderId", mOrderId);
                intent.putExtra("totalAmount", mTotalAmount);
                startActivity(intent);
                break;
            case R.id.btn_Cancel:
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(payStackObserver);
        CommonUtils.disposable(verifyObserver);
        CommonUtils.disposable(mFlutterWaveObserver);
        CommonUtils.disposable(bankObserver);
        CommonUtils.disposable(mObserver);
        CommonUtils.disposable(Observer);
        CommonUtils.disposable(monifyObserver);
        CommonUtils.disposable(redoclyObserver);
    }


    public void flutterWave(FlutterWaveBean bean) {
        mTxRef = bean.getTxRef();
        String str = KvStorage.get(LocalConfig.LC_FLUTTERWAVEPAYMETHOD, "");
        boolean card = true;
        if (!TextUtils.isEmpty(str)) {
            card = str.contains("card") ? true : false;
        }
        boolean account = str.contains("account") ? true : false;
        boolean transfer = str.contains("transfer") ? true : false;
        boolean ussd = str.contains("ussd") ? true : false;
        new RaveUiManager(this)
                .acceptAccountPayments(account)
                .acceptCardPayments(card)
                .acceptBankTransferPayments(transfer)
                .acceptUssdPayments(ussd)
                .setAmount(Double.parseDouble(bean.getAmount()))
                .setCurrency(bean.getCurrency())
                .setfName(bean.getFirstName())
                .setlName(bean.getLastName())
                .setEmail(bean.getEmail())
                .setPublicKey(bean.getPublicKey())
                .setEncryptionKey(bean.getEncryptionKey())
                .setTxRef(mTxRef)
                .onStagingEnv(false)
                .initialize();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            mJsonString = data.getStringExtra("response");
            if(BuildConfig.DEBUG) {
                Log.d("Repayment", "mJsonString = " + mJsonString);
            }
            FlutterWaveResult bean = new Gson().fromJson(mJsonString, FlutterWaveResult.class);
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {

            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + bean.getData().getVbvrespmessage(), Toast.LENGTH_SHORT).show();
            }
            if (bean != null && "success".equals(bean.getStatus())) {
                uploadVerifyFlutterJson();
            }

        } else if (resultCode == 2000) {
            getPayStackResult();
        } else if (resultCode == 3000) {
            finish();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    NetObserver mObserver;

    private void getPayStackResult() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().payStackResult(accountId, mOrderId, mReference1)
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
                                createBuilder(PaymentMethodActivity.this, getSupportFragmentManager())
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

}
