package com.tiny.cash.loan.card.feature.bank;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityBankcardBinding;
import com.tiny.cash.loan.card.ui.dialog.fragment.OfflinePaymentTransferFragment;
import com.tiny.cash.loan.card.ui.dialog.fragment.TipsDialogFragment;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.bean.BankResult;
import com.tiny.cash.loan.card.net.request.params.BankCardParams;
import com.tiny.cash.loan.card.net.response.Response;

import org.greenrobot.eventbus.EventBus;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.exceptions.ExpiredAccessCodeException;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;
import co.paystack.android.utils.StringUtils;
import com.tiny.cash.loan.card.message.EventMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by WANG on 2020/10/11.
 */

/**
 * 被废弃
 */
public class AddBankCardActivity extends BaseActivity {

    private ActivityBankcardBinding mBinding;
    private Charge mCharge;
    private NetObserver<Response<BankResult>> sendCodeObserver;
    private NetObserver<Response<BankResult>> verifyObserver;
    private String accountId;
    private String cardNum;
    private String cvc;
    private String date;
    String type;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getStringExtra("type");
        mBinding = ActivityBankcardBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_bankcard);

        mBinding.btnSubmit.setOnClickListener(v -> {
            startAFreshCharge();
        });
        mBinding.llTitle.ivBack.setOnClickListener(v -> {
            finish();
        });
        mBinding.etBankDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i == 3 && s.charAt(i) == '/') {
                        continue;
                    } else {
                        stringBuilder.append(s.charAt(i));
                        if ((stringBuilder.length() == 3 && stringBuilder.charAt(stringBuilder.length() - 1) != '/')) {
                            stringBuilder.insert(stringBuilder.length() - 1, '/');
                        }
                    }
                }


                if (!stringBuilder.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == '/') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mBinding.etBankDate.setText(stringBuilder.toString());
                    mBinding.etBankDate.setSelection(index);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void startAFreshCharge() {
        Card card = loadCard();
        if (card == null) {
            return;
        }
        mCharge = new Charge();
        mCharge.setCard(card);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        fetchAccessCode();
    }

    private void fetchAccessCode() {
        accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().fetchAccessCode(accountId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(sendCodeObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        sendCodeObserver = new NetObserver<Response<BankResult>>() {
            @Override
            public void onNext(Response<BankResult> response) {
                if (response.isSuccess()) {
                    Log.d("TAG", "onNext: " + response.getBody().getAccessCode());
                    mCharge.setAccessCode(response.getBody().getAccessCode());
                    chargeCard();
                } else {
                    dismissProgressDialogFragment();
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(sendCodeObserver);
    }


    private void verifyOnServer(String reference) {
        BankCardParams bankCardParams = new BankCardParams();
        bankCardParams.setReference(reference);
        bankCardParams.setAccountId(accountId);
        bankCardParams.setCardNumber(cardNum);
        bankCardParams.setCvv(cvc);
        bankCardParams.setExpireDate(date);
        Observable observable = NetManager.getApiService().verifyReference(bankCardParams)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(verifyObserver);
        verifyObserver = new NetObserver<Response<BankResult>>() {
            @Override
            public void onNext(Response<BankResult> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody().isHasUpload()) {
                        FirebaseUtils.logEvent("fireb_card_success");
//                        FirebaseLogUtils.Log("af_add_bankcard");
                        EventBus.getDefault().post(new EventMessage(EventMessage.BANKCARDSUCCESS));
                        finish();
                    } else {
                        showError();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(verifyObserver);
    }

    private void showError(){
        if ("repay".equals(type))
            showRepayBankCardDialog();
        else
            showBankCardDialog();
    }
    private void showBankCardDialog() {
        String message = KvStorage.get(LocalConfig.LC_BANKCARDFAILMESSAGE,"");
        if (StringUtils.isEmpty(message)){
            message = "Sorry, you have failed to bind your bank card, would you like to continue the loan application?";
        }
        TipsDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setMessage(message)
                .setPositiveButtonTxt("No")
                .setNegativeButtonTxt("Yes")
                .setNegativeListener(()->{
                    EventBus.getDefault().post(new EventMessage(EventMessage.BANKCARDSUCCESS));
                    finish();
                })
                .show();
    }

    private void showRepayBankCardDialog() {
        String message = KvStorage.get(LocalConfig.LC_REPAYBANKCARDFAILMESSAGE,"");
        if (StringUtils.isEmpty(message)){
            message = "Sorry, you have failed to bind your bank card, you can change another bank card or go for offline transfer to make repayment.";
        }
        TipsDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setMessage(message)
                .setPositiveButtonTxt("No")
                .setNegativeButtonTxt("Go for offline ")
                .setNegativeListener(() -> {
                    OfflinePaymentTransferFragment.createBuilder(this, getSupportFragmentManager())
                            .show();
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(verifyObserver);
        CommonUtils.disposable(sendCodeObserver);
    }

    private void chargeCard() {
        PaystackSdk.chargeCard(this, mCharge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                Log.d("TAG", "onSuccess:    " + transaction.getReference());
                verifyOnServer(transaction.getReference());
            }

            //此函数只在请求OTP保存引用之前调用，如果需要，可以解除otp验证
            @Override
            public void beforeValidate(Transaction transaction) {
                Log.d("TAG", "beforeValidate: " + transaction.getReference());
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                count++;
                Log.d("TAG", "onError: "+count);
                if (count == 1){
                    if (error instanceof ExpiredAccessCodeException) {
                        //再次获取 access code
                        startAFreshCharge();
                        chargeCard();
                        return;
                    }
                }else {
                    showError();
                }

                dismissProgressDialogFragment();
                ToastManager.show(AddBankCardActivity.this, error.getMessage());
//                if (transaction.getReference() != null) {
//                    ToastManager.show(AddBankCardActivity.this,  error.getMessage());
//                    verifyOnServer(transaction.getReference());
//                } else {
//                    ToastManager.show(AddBankCardActivity.this, error.getMessage());
//                }
            }


        });
    }

    /**
     * Method to validate the form, and set errors on the edittexts.
     */
    private Card loadCard() {
        Card card;

        cardNum = mBinding.etBankNumber.getText().toString().trim();

        card = new Card.Builder(cardNum, 0, 0, "").build();
        cvc = mBinding.etBankCvv.getText().toString().trim();
        //update the cvc field of the card
        card.setCvc(cvc);
        date = mBinding.etBankDate.getText().toString().trim();
        if (StringUtils.isEmpty(cardNum) || StringUtils.isEmpty(cvc) || StringUtils.isEmpty(date)) {
            ToastManager.show(AddBankCardActivity.this, "Required field must be filled");
            return null;
        }

        String[] split = date.split("/");
        if (split.length == 1) {
            ToastManager.show(AddBankCardActivity.this, "date error");
            return null;
        }
        int month = 0;
        try {
            month = Integer.parseInt(split[0]);
        } catch (Exception ignored) {
        }

        card.setExpiryMonth(month);
        int year = 0;
        try {
            year = Integer.parseInt(split[1]);
        } catch (Exception ignored) {
        }
        card.setExpiryYear(year);

        return card;
    }
}
