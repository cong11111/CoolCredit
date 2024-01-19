package com.tiny.cash.loan.card.feature.loan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chocolate.moudle.scan.my.ScanActivity;
import com.tiny.cash.loan.card.Constant;
import com.tiny.cash.loan.card.KudiCreditApp;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.BuildConfig;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.ui.adapter.AmountAdapter;
import com.tiny.cash.loan.card.ui.adapter.TermAdapter;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.LayoutLoanProductBinding;
import com.tiny.cash.loan.card.ui.camera.IdentityAuthActivity;
import com.tiny.cash.loan.card.ui.camera.IdentityPhotoActivity;
import com.tiny.cash.loan.card.ui.card.BindNewCardActivity;
import com.tiny.cash.loan.card.ui.dialog.fragment.ConfirmLoanDialogFragment;
import com.tiny.cash.loan.card.ui.dialog.fragment.TipsDialogFragment;
import com.tiny.cash.loan.card.feature.users.ContactsActivity;
import com.tiny.cash.loan.card.feature.users.BasicInfoActivity;
import com.tiny.cash.loan.card.feature.users.WorkInfoActivity;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.GPSUtils;
import com.tiny.cash.loan.card.utils.JumpPermissionUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.PermissionUtil;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.response.data.order.ProductList;
import com.tiny.cash.loan.card.net.response.data.order.QueryOrderId;
import com.tiny.cash.loan.card.net.response.data.order.LoanTrial;
import com.tiny.cash.loan.card.net.request.params.ApplyParams;
import com.tiny.cash.loan.card.net.response.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductFragment extends BaseFragment implements View.OnClickListener {
    private LayoutLoanProductBinding mBinding;
    private List<ProductList.ProductsBean> productsBeanList;
    private LoanTrial body;
    private String amount;
    private String mLastTerm;
    private ProductList.ProductsBean mCurProductBean;
    private String mType;
    private String[] permissions = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
//            , Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
           Manifest.permission.READ_SMS};
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1000;
    public static final int REQUEST_PERMISSIONS = 50;
    private boolean paystackCardBind;

    public static ProductFragment newInstance() {
        Bundle args = new Bundle();
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LayoutLoanProductBinding.inflate(inflater, container, false);//DataBindingUtil.inflate(inflater, R.layout.layout_loan_product, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mBinding.tvMoreContinue.setOnClickListener(this);
        mBinding.btnApplyNow.setOnClickListener(this);
    }


    private void initData() {
        String str = KvStorage.get(LocalConfig.LC_PAYSTACKCARDBIND, "");
        paystackCardBind = Constants.ONE.equals(str)? true : false;
        requestLoanProduct();
    }

    AmountAdapter amountAdapter = null;

    private void initAmountAdapter() {
        amountAdapter = new AmountAdapter(getContext());
        if (mAmountList != null && mAmountList.size() > 0) {
            amountAdapter.selectItem(mAmountList.get(mAmountList.size() - 1));
        }
        mBinding.spinnerLoanAmount.setAdapter(amountAdapter);
        amountAdapter.setData(mAmountList);
        amountAdapter.notifyDataSetChanged();

        mBinding.spinnerLoanAmount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = mAmountList.get(position);
                amountAdapter.selectItem(s);
                if (mAmountList != null && mAmountList.size() > 0) {
                    amount = mAmountList.get(position);
                    body = null;
                    TermView(amount, mTermMap.get(amount));
                }
            }
        });
    }


    TermAdapter adapter = null;

    private void initTermAdapter(String selectStr) {
        List<String> mTermList = mTermMap.get(amount);
        if (mTermList == null || mTermList.size() == 0) {
            return;
        }
        Collections.sort(mTermList, (String o1, String o2) -> {
            return Integer.parseInt(o1) - Integer.parseInt(o2);
        });
        adapter = new TermAdapter(getContext());
        if (mTermList.size() == 1){
            adapter.selectItem(mTermList.get(0));
        } else {
            if (TextUtils.isEmpty(selectStr)) {
                selectStr =  mTermList.get(mTermList.size() - 1);
            }
            adapter.selectItem(selectStr);
        }
        mBinding.recyclerView.setAdapter(adapter);
        adapter.setData(mTermList);

        mBinding.recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = mTermList.get(position);
                ProductList.ProductsBean productsBean = mDataMap.get(amount + s);
                adapter.selectItem(s);
                mLastTerm = s;
                List<String> list = mTermMap.get(amount);
                if (list != null && list.size() > 0) {
                    mCurProductBean = productsBean;
                    requestLoanTrial();
                }
            }
        });
    }


    private void TermView(String amount, List<String> mTermList) {
        String period = null;
        if (mTermList.size() == 1){
            period = mTermList.get(0);
            ProductList.ProductsBean productsBean = mDataMap.get(amount + period);
            mCurProductBean = productsBean;
            requestLoanTrial();
        }else {
//            mBinding.llNoTerm.setVisibility(View.GONE);
            mBinding.llAll.setVisibility(View.GONE);
            mBinding.llFirst1.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(mLastTerm)) {
                for (int i =0; i < mTermList.size(); i++) {
                    String temp = mTermList.get(i);
                    if (TextUtils.equals(mLastTerm, temp)) {
                        period = mLastTerm;
                        break;
                    }
                }
            }
            if (TextUtils.isEmpty(period)) {
                period = mTermList.get(mTermList.size() - 1);
            }
            mCurProductBean = mDataMap.get(amount + period);
            requestLoanTrial();
        }
        mLastTerm = mCurProductBean.getPeriod();
        initTermAdapter(period);
    }

    NetObserver LoanProductObserver, loanTrialObserver, OrderStatusObserver;
    List<String> mAmountList = new ArrayList<>();
    List<String> mRepayTermList = new ArrayList<>();
    HashMap<String, List<String>> mTermMap = new HashMap<>();
    HashMap<String, ProductList.ProductsBean> mDataMap = new HashMap<>();

    private void requestLoanProduct() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().productList(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(LoanProductObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        LoanProductObserver = new NetObserver<Response<ProductList>>() {
            @Override
            public void onNext(Response<ProductList> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody() == null || response.getBody().getProducts() == null || response.getBody().getProducts().size() == 0)
                        return;
                    productsBeanList = response.getBody().getProducts();
                    if (productsBeanList != null) {
                        Collections.sort(productsBeanList, new Comparator<ProductList.ProductsBean>() {
                            @Override
                            public int compare(ProductList.ProductsBean o1, ProductList.ProductsBean o2) {
                                try {
                                    if (o1 != null && o2 != null) {
                                        Double amount1  = Double.parseDouble(o1.getAmount());
                                        Double amount2 =  Double.parseDouble(o2.getAmount());
                                        if (amount1 != null && amount2 != null){
                                            return (int) (amount2 - amount1);
                                        }
                                    }
                                }catch (Exception e){
                                    Log.e("ProductFragment", "parse exception ", e);
                                }
                                return 0;
                            }
                        });
                    }
                    mType = response.getBody().getType();
                    for (ProductList.ProductsBean bean : productsBeanList) {
                        String amountStr = getAmountStr(bean.getAmount());
                        mRepayTermList = mTermMap.get(amountStr);
                        if (mRepayTermList == null) {
                            mRepayTermList = new ArrayList<>();
                        }
                        if (mAmountList != null && !TextUtils.isEmpty(bean.getAmount())) {
                            if (!mAmountList.contains(amountStr)) {
                                mAmountList.add(amountStr);
                            }
                        }
                        if (!mRepayTermList.contains(bean.getPeriod())) {
                            mRepayTermList.add(bean.getPeriod());
                        }
                        mDataMap.put(amountStr + bean.getPeriod(), bean);
                        mTermMap.put(amountStr, mRepayTermList);
                    }
                    Collections.sort(mAmountList, (String o1, String o2) -> {
                        return Integer.parseInt(o1) - Integer.parseInt(o2);
                    });
                    if (mAmountList.size() > 0) {
                        amount = mAmountList.get(mAmountList.size() - 1);
                    }
                    initAmountAdapter();
                    mLastTerm = null;
                    TermView(amount, mTermMap.get(amount));
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(LoanProductObserver);
    }

    public String getAmountStr(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return "0";
        }
        float amountFlt = Float.parseFloat(amount);
        String amountStr = ((int)amountFlt) + "";
        return amountStr;
    }

    private void initTrial(LoanTrial trial) {
        if (getContext() == null)
            return;
        if (trial == null) {
            mBinding.llNoTerm.setVisibility(View.VISIBLE);
        } else {
            mBinding.llNoTerm.setVisibility(View.GONE);
        }
        if (trial == null) {
            mBinding.tvFirstInterest.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvFirstFee.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvFirstDisburse.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvFirstRepayment.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvFirstRepayDate.setText("");

            mBinding.tvSecondInterest.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvSecondFee.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvSecondDisburse.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvSecondRepayment.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvSecondRepayDate.setText("");
            mBinding.tvTotalRepayDate.setText("");

            mBinding.tvTotalInterest.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvTotalFee.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvTotalDisburse.setText(getString(R.string.str_money, Constants.ZERO));
            mBinding.tvTotalRepayment.setText(getString(R.string.str_money, Constants.ZERO));

        } else {

            mBinding.tvTotalInterest.setText(getString(R.string.str_money, getAmountStr(trial.getTotalInterestAmount())));
            mBinding.tvTotalFee.setText(getString(R.string.str_money, getAmountStr(trial.getTotalFeeAmount())));
            mBinding.tvTotalDisburse.setText(getString(R.string.str_money, getAmountStr(trial.getTotalDisburseAmount())));
            mBinding.tvTotalRepayment.setText(getString(R.string.str_money, getAmountStr(trial.getTotalRepaymentAmount())));

            List<LoanTrial.TrialBean> trialBeanList = trial.getTrial();
            if (trialBeanList.size() == 1) {
                mBinding.llFirst1.setVisibility(View.VISIBLE);
                mBinding.llAll.setVisibility(View.GONE);
                for (LoanTrial.TrialBean trialBean : trialBeanList) {
                    if (Constants.ONE.equals(trialBean.getStageNo())) {
                        mBinding.tvFirstInterest1.setText(getString(R.string.str_money, getAmountStr(trialBean.getInterest())));
                        mBinding.tvFirstFee1.setText(getString(R.string.str_money, getAmountStr(trialBean.getFee())));
                        mBinding.tvFirstDisburse1.setText(getString(R.string.str_money, getAmountStr(trialBean.getDisburseAmount())));
                        mBinding.tvFirstRepayment1.setText(getString(R.string.str_money, getAmountStr(trialBean.getTotalAmount())));
                        mBinding.tvFirstRepayDate1.setText(trialBean.getRepayDate());
                    }
                }

            } else {
                mBinding.llFirst1.setVisibility(View.GONE);
                mBinding.llAll.setVisibility(View.VISIBLE);
                for (LoanTrial.TrialBean trialBean : trialBeanList) {
                    if (Constants.ONE.equals(trialBean.getStageNo())) {
                        mBinding.tvFirstInterest.setText(getString(R.string.str_money, getAmountStr(trialBean.getInterest())));
                        mBinding.tvFirstFee.setText(getString(R.string.str_money, getAmountStr(trialBean.getFee())));
                        mBinding.tvFirstDisburse.setText(getString(R.string.str_money, getAmountStr(trialBean.getDisburseAmount())));
                        mBinding.tvFirstRepayment.setText(getString(R.string.str_money, getAmountStr(trialBean.getTotalAmount())));
                        mBinding.tvFirstRepayDate.setText(trialBean.getRepayDate());
                    } else if (Constants.TWO.equals(trialBean.getStageNo())) {
                        mBinding.tvSecondInterest.setText(getString(R.string.str_money, getAmountStr(trialBean.getInterest())));
                        mBinding.tvSecondFee.setText(getString(R.string.str_money, getAmountStr(trialBean.getFee())));
                        mBinding.tvSecondDisburse.setText(getString(R.string.str_money, getAmountStr(trialBean.getDisburseAmount())));
                        mBinding.tvSecondRepayment.setText(getString(R.string.str_money, getAmountStr(trialBean.getTotalAmount())));
                        mBinding.tvSecondRepayDate.setText(trialBean.getRepayDate());
                        mBinding.tvTotalRepayDate.setText(trialBean.getRepayDate());
                    }
                }
            }

        }

    }

    private void requestLoanTrial() {
        if (mCurProductBean == null) {
            return;
        }
        String prodId = mCurProductBean.getProdId();
        String loanAmount = mCurProductBean.getAmount();
        String period = mCurProductBean.getPeriod();
        if (BuildConfig.DEBUG) {
            Log.e("Test", " cur amount = " + loanAmount + " period = " + period + " prodId = " + prodId);
        }
        Observable observable = NetManager.getApiService().loanTrial(prodId, loanAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(loanTrialObserver);
//        showProgressDialogFragment(getString(R.string.str_loading), false);
        loanTrialObserver = new NetObserver<Response<LoanTrial>>() {
            @Override
            public void onNext(Response<LoanTrial> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (response.getBody() != null) {
                        body = response.getBody();
                        initTrial(body);
                    }
                } else {
                    body = null;
                    showToast(response.getStatus().getMsg());
                    initTrial(null);
                }
            }

            @Override
            public void onException(ResponseException netException) {
                dismissProgressDialogFragment();
                mBinding.llNoTerm.setVisibility(View.GONE);
            }
        };
        observable.subscribeWith(loanTrialObserver);
    }

    private void QueryOrderId() {
        Observable observable = NetManager.getApiService().QueryOrderId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(OrderStatusObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        OrderStatusObserver = new NetObserver<Response<QueryOrderId>>() {
            @Override
            public void onNext(Response<QueryOrderId> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    if (productsBeanList == null || productsBeanList.size() == 0) {
                        return;
                    }
                    String orderId = response.getBody().getOrderId();
                    if (Constant.Companion.getIS_FIRST_APPROVE()) {
                        FirebaseUtils.logEvent("fireb_apply");
                    }
                    FirebaseUtils.logEvent("fireb_apply_all");
                    if ("-1".equals(orderId)) {
//                        FirebaseLogUtils.Log("af_apply_null");
                        if (!response.getBody().isHasProfile() || !response.getBody().isBvnChecked()) {
                            startIntent(BasicInfoActivity.class);
                            return;
                        }
                        if (!response.getBody().isHasContact()) {
                            startIntent(ContactsActivity.class);
                            return;
                        }
                        if (!response.getBody().isHasOther()) {
                            startIntent(WorkInfoActivity.class);
                            return;
                        }
                        if (response.getBody().isInfoReviewSwitch()) {
                            if (!response.getBody().isHasInfoReviewCard()) {
                                IdentityPhotoActivity.Companion.launchActivity(getActivity(), !response.getBody().isHasInfoReviewSelfie());
                                return;
                            }
                            if (!response.getBody().isHasInfoReviewSelfie()) {
                                boolean isGranted = PermissionUtils.isGranted(Manifest.permission.CAMERA);
                                if (isGranted) {
                                    IdentityAuthActivity.Companion.checkExistAndToSelfie(getActivity());
                                } else {
                                    PermissionUtils.permission(Manifest.permission.CAMERA).callback(new PermissionUtils.SimpleCallback() {
                                        @Override
                                        public void onGranted() {
                                            IdentityAuthActivity.Companion.checkExistAndToSelfie(getActivity());
                                        }

                                        @Override
                                        public void onDenied() {
                                            ToastUtils.showShort("please allow permission.");
                                            JumpPermissionUtils.goToSetting(getActivity());
                                        }
                                    }).request();
                                }

                                return;
                            }
                        }
                        if (!response.getBody().isAccountChecked()) {
                            BindNewCardActivity.Companion.launchAddBankAccount(getContext());
                            return;
                        }
                        if (paystackCardBind &&!response.getBody().isCardChecked()) {
//                            startIntent(AddMoreBankCardActivity.class);
                            BindNewCardActivity.Companion.launchAddBankCard(getContext());
                            return;
                        }
                        if (TextUtils.isEmpty(orderId) || TextUtils.equals(orderId, "-1")) {
                            ToastUtils.showShort("need correct loan apply orderId " + orderId);
                            return;
                        }
                    } else {
                        if (!response.getBody().isCardChecked()) {
                            showBankCardDialog(orderId);
                            return;
                        }
                        submitConfig(orderId);
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
        observable.subscribeWith(OrderStatusObserver);
    }

    private void submitConfig(String orderId) {
//        if (LocalConfig.isNewUser())
//            FirebaseLogUtils.Log("af_new_apply");
//        else
//            FirebaseLogUtils.Log("af_old_apply");
        if (mCurProductBean == null){
            return;
        }
        String prodId = mCurProductBean.getProdId();
        String prodName = mCurProductBean.getProdName();
        String period = mCurProductBean.getPeriod();
        String acconutId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        ApplyParams applyParams = ApplyParams.createParams(acconutId, orderId, prodId, prodName, amount, period);
        ConfirmLoanDialogFragment.createBuilder(getContext(), getChildFragmentManager())
                .setBody(body)
                .setMessage(applyParams)
                .setNegativeButtonTxt(getString(R.string.str_done))
                .setPositiveButtonTxt(getString(R.string.str_cancel))
                .setNegativeListener(() -> {
                    getActivity().onBackPressed();
                })
                .show();
    }

    private void showBankCardDialog(String orderId) {
        String message = KvStorage.get(LocalConfig.LC_BANKCARDFAILMESSAGE, "");
        if (StringUtils.isEmpty(message)) {
            message = "Sorry, you have failed to bind your bank card, would you like to continue the loan application?";
        }
        TipsDialogFragment.createBuilder(getContext(), getChildFragmentManager())
                .setMessage(message)
                .setPositiveButtonTxt("No")
                .setPositiveListener(() -> {
                    BindNewCardActivity.Companion.launchAddBankCard(getContext());
//                    startIntent(AddMoreBankCardActivity.class);
                })
                .setNegativeButtonTxt("Yes")
                .setNegativeListener(() -> {
                    submitConfig(orderId);
                }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.disposable(LoanProductObserver);
        CommonUtils.disposable(loanTrialObserver);
        CommonUtils.disposable(OrderStatusObserver);
    }

    private void requestPermissions() {
        if (PermissionUtil.hasPermission(getAppCompatActivity(), permissions)) {
            boolean locationProviderEnabled = GPSUtils.getInstance(getContext()).isLocationProviderEnabled();
            if (!locationProviderEnabled)
                gotoSysLocationSettingsPage();
            else
                QueryOrderId();
        } else {
            //检查勾选了不在提醒
            for (String item : permissions) {
                boolean flag = shouldShowRequestPermissionRationale(item);
                if (!flag) {
                    showWaringDialog();
                    return;
                }
            }
            requestPermissions(permissions, REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    QueryOrderId();
                } else {
                    showWaringDialog();
                }
                return;
            }
        }
    }

    private void showWaringDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("warning！")
                .setMessage("Please go to setup -> application-> permissiondemo-> to open the relevant permissions, otherwise the function will not work properly!")
                .setPositiveButton(getString(R.string.str_done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", KudiCreditApp.getInstance().getPackageName(), null);
                        intent.setData(uri);
                        if (isAdded()) {
                            startActivityForResult(intent, 1000);
                        }
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            QueryOrderId();
        }
        if (requestCode == 1000) {
            requestPermissions();
        }
    }

    private void gotoSysLocationSettingsPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_LOCATION_PERMISSION_CODE);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.btnApplyNow) {
            if (mCurProductBean == null || mCurProductBean.getUsable().equals("false")) {
                ToastManager.show(getContext(), getString(R.string.str_keep_a_good_credit_history));
                 return;
            }
            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_FIRSTORDER), Constants.ZERO);
            if (body == null) {
                return;
            }
            requestPermissions();
        }
        if (view == mBinding.tvMoreContinue) {
            mBinding.llFirst.setVisibility(mBinding.llFirst.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE);
            mBinding.llSecond.setVisibility(mBinding.llSecond.getVisibility()
                    == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }

}
