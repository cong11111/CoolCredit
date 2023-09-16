package com.tiny.cash.loan.card.feature.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tiny.cash.loan.card.Constant;
import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.utils.SendFileUtils;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.collect.BaseCollectDataMgr;
import com.tiny.cash.loan.card.collect.CollectDataMgr;
import com.tiny.cash.loan.card.collect.CollectHardwareMgr;
import com.tiny.cash.loan.card.collect.LocationMgr;
import com.tiny.cash.loan.card.collect.item.CollectSmsMgr;
import com.tiny.cash.loan.card.dialog.RequestPermissionDialog;
import com.tiny.cash.loan.card.ui.loan.LoanActiveFragment2;
import com.tiny.cash.loan.card.feature.loan.LoanOrderHelp;
import com.tiny.cash.loan.card.feature.loan.OverdueFragment;
import com.tiny.cash.loan.card.feature.loan.PaymentProgressFragment;
import com.tiny.cash.loan.card.feature.loan.ProcessingFragment;
import com.tiny.cash.loan.card.feature.loan.ProductFragment;
import com.tiny.cash.loan.card.feature.menu.AboutFragment;
import com.tiny.cash.loan.card.feature.menu.BankAccountFragment;
import com.tiny.cash.loan.card.feature.menu.CallUsFragment;
import com.tiny.cash.loan.card.feature.menu.CardFragment;
import com.tiny.cash.loan.card.feature.menu.HelpFragment;
import com.tiny.cash.loan.card.feature.menu.MessageFragment;
import com.tiny.cash.loan.card.feature.menu.MyProfileFragment;
import com.tiny.cash.loan.card.feature.menu.OfflinePaymentFragment;
import com.tiny.cash.loan.card.kudicredit.BuildConfig;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.message.EventMessage;
import com.tiny.cash.loan.card.net.NetManager;
import com.tiny.cash.loan.card.net.NetObserver;
import com.tiny.cash.loan.card.net.ResponseException;
import com.tiny.cash.loan.card.net.response.BaseResponse;
import com.tiny.cash.loan.card.net.response.Response;
import com.tiny.cash.loan.card.net.response.data.bean.AppConfigFile;
import com.tiny.cash.loan.card.net.response.data.bean.AuthResult;
import com.tiny.cash.loan.card.net.response.data.bean.UpLoadAPPVersion;
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail;
import com.tiny.cash.loan.card.net.response.data.order.MessageBean;
import com.tiny.cash.loan.card.net.response.data.order.OrderStatus;
import com.tiny.cash.loan.card.net.server.ApiServerImpl;
import com.tiny.cash.loan.card.ui.adapter.DrawerAdapter;
import com.tiny.cash.loan.card.ui.card.BindNewCardActivity;
import com.tiny.cash.loan.card.ui.dialog.fragment.TipsDialogFragment;
import com.tiny.cash.loan.card.ui.loan.LoanDeclinedFragment;
import com.tiny.cash.loan.card.ui.login2.Login2Activity;
import com.tiny.cash.loan.card.ui.menu.VirtualAccountFragment;
import com.tiny.cash.loan.card.ui.pay2.PayActivity2;
import com.tiny.cash.loan.card.utils.AppUtils;
import com.tiny.cash.loan.card.utils.CommonUtils;
import com.tiny.cash.loan.card.utils.FirebaseUtils;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import co.paystack.android.utils.StringUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private TextView mTv_title;
    private DrawerLayout mDrawer;
    private Fragment mCurrentFragment;
    private ImageView mImageView;//, android.Manifest.permission.CAMERA  , android.Manifest.permission.RECORD_AUDIO
    private NetObserver loutObserver, UpVersionObserver, orderObserver, flutterObserver;
    private TextView mRefresh;
    DrawerAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        if (!TextUtils.isEmpty(accountId)) {
            FirebaseUtils.setUserId(this, accountId);
        }
        initView();
        initDraw();
        viewClick();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            push = intent.getStringExtra(LocalConfig.LC_FIREBASEPUSH);
            pushUI(push);
        }
        initData();
        init();
        SPUtils.getInstance().put(Constant.KEY_LOGIN_TIME, System.currentTimeMillis());
    }

    public void init() {
        EventBus.getDefault().register(this);
        requestPermissions();

        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                KvStorage.put(LocalConfig.LC_FBTOKEN, token);
                upLoadFcmtoken();
            });
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }

//        boolean b = NotificationManagerCompat.from(this).areNotificationsEnabled();
//        if (!b) {
//            goToSetting();
//        }
    }

    NetObserver configObserver;

    private void QueryAppConfig() {
        Observable observable = NetManager.getApiService().QueryAppConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(configObserver);
        configObserver = new NetObserver<Response<AppConfigFile>>() {
            @Override
            public void onNext(Response<AppConfigFile> response) {
                AppConfigFile body = response.getBody();
                KvStorage.put(LocalConfig.LC_BANK, body.getBank());
                KvStorage.put(LocalConfig.LC_WHATSAPP, body.getWhatsApp());
                KvStorage.put(LocalConfig.LC_WHATSAPP1, body.getWhatsApp1());
                KvStorage.put(LocalConfig.LC_ACCOUNTNAME, body.getAccountName());
                KvStorage.put(LocalConfig.LC_PHONE, body.getPhone());
                KvStorage.put(LocalConfig.LC_USSD, body.getUssd());
                KvStorage.put(LocalConfig.LC_ACCOUNTNUMBER, body.getAccountNumber());
                KvStorage.put(LocalConfig.LC_EMAIL, body.getEmail());
                KvStorage.put(LocalConfig.LC_AUDITMSG, body.getAuditMsg());
                KvStorage.put(LocalConfig.LC_FIVESTARTITLE, body.getFiveStarTitle());
                KvStorage.put(LocalConfig.LC_FIVESTARCONTENT, body.getFiveStarContent());
                KvStorage.put(LocalConfig.LC_KOCHAVASWITCH, body.getKochavaSwitch());
                KvStorage.put(LocalConfig.LC_BANKCARDFAILMESSAGE, body.getBankCardFailMessage());
                KvStorage.put(LocalConfig.LC_REPAYBANKCARDFAILMESSAGE, body.getRepayBankCardFailMessage());
                KvStorage.put(LocalConfig.LC_BANKCARDSWITCH, body.getBankCardSwitch());
                KvStorage.put(LocalConfig.LC_BANKCARDRETRYMESSAGE, body.getBankCardRetryMessage());
                KvStorage.put(LocalConfig.LC_FLUTTERWAVEPAYMETHOD, body.getFlutterwavePayMethod());
                KvStorage.put(LocalConfig.LC_REPAYCHANNEL, body.getRepayChannel());
                KvStorage.put(LocalConfig.LC_PAYSTACKCARDBIND, body.getPaystackCardBind());

                String str = body.getPaystackCardBind();
                if (!StringUtils.isEmpty(str)) {
                    boolean paystackCardBind = Constants.ONE.equals(str) ? true : false;

                    if (paystackCardBind) {
                        showBankNotify();
                    }
                }
            }

            @Override
            public void onException(ResponseException netException) {
            }
        };
        observable.subscribeWith(configObserver);
    }

    /**
     * drawer item 点击事件
     */
    public class MyOnItemClickListener implements DrawerAdapter.OnItemClickListener {

        @Override
        public void itemClick(DrawerAdapter.DrawerItemNormal drawerItemNormal) {
            switch (drawerItemNormal.titleRes) {
                case R.string.menu_My_loan:
                    showLoanView();
                    break;
                case R.string.menu_My_Profile:
                    showMyProfileFragment();
                    mTv_title.setText(getString(R.string.menu_My_Profile));
                    break;
                case R.string.menu_Card:
                    showCardFragment();
                    mTv_title.setText(getString(R.string.menu_Card));
                    break;
                case R.string.menu_Bank_Account:
                    showBankAccountFragment();
                    mTv_title.setText(getString(R.string.menu_Bank_Account));
                    break;
                case R.string.menu_off_payment:
                    mTv_title.setText(getString(R.string.offline_payment_transfer));
                    showOffPaymentFragment();
                    break;
                case R.string.menu_Message:
                    mTv_title.setText(getString(R.string.menu_Message));
                    showMessageFragment();
                    break;

                case R.string.menu_ContactUs:
                    showCallUsFragment();
                    mTv_title.setText(getString(R.string.menu_ContactUs));
                    break;
                case R.string.menu_About:
                    showAboutFragment();
                    mTv_title.setText(getString(R.string.menu_About));
                    break;
                case R.string.menu_Log_out:
                    logOut();
                    break;
                case R.string.menu_test1:
                    test1();
                    break;
                case R.string.menu_share_file:
                    SendFileUtils.INSTANCE.startFeedBackEmail(MainActivity.this);
                    break;
            }
            if (drawerItemNormal.titleRes == R.string.menu_My_loan) {
                mRefresh.setVisibility(View.VISIBLE);
            } else {
                mRefresh.setVisibility(View.GONE);
            }
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    private void initDraw() {
        drawerAdapter = new DrawerAdapter();
        drawerAdapter.setOnItemClickListener(this, new MyOnItemClickListener());
        rvDrawer.setLayoutManager(new LinearLayoutManager(this));
        rvDrawer.setAdapter(drawerAdapter);
    }

    private void requestPermissions() {
        boolean hasPermission = PermissionUtils.isGranted(
//            PermissionConstants.LOCATION,
//            PermissionConstants.CAMERA,
                PermissionConstants.SMS
//            PermissionConstants.CONTACTS,
//            PermissionConstants.STORAGE,
        );
        boolean hasPermissionCoarseLocation = PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
//        val hasPermissionCallLog = PermissionUtils.isGranted(Manifest.permission.READ_CALL_LOG)
//        val hasPermissionReadPhoneState =
//            PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE)
//                if (false && hasPermission) {
        if (hasPermissionCoarseLocation && hasPermission) {
            executeCache();
        } else {
            requestPermissionInternal();
        }

    }

    private void requestPermissionInternal() {
        RequestPermissionDialog dialog = new RequestPermissionDialog(this);
        dialog.setOnItemClickListener(new RequestPermissionDialog.OnItemClickListener() {
            @Override
            public void onClickAgree() {
                PermissionUtils utils;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    utils = PermissionUtils.permission(Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    utils = PermissionUtils.permission(Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                utils.callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        executeCache();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.showShort("please allow permission.");
                    }
                }).request();
            }
        });
        dialog.show();
    }

    private void RequestUpVersion() {
        Observable observable = NetManager.getApiService().checkUpVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(UpVersionObserver);
        UpVersionObserver = new NetObserver<Response<UpLoadAPPVersion>>() {
            @Override
            public void onNext(Response<UpLoadAPPVersion> response) {
                if (response.isSuccess()) {
                    if (response.getBody() == null)
                        return;
                    String type = response.getBody().getUpdateType();
                    if (TextUtils.isEmpty(type) || Integer.parseInt(type) == 0)
                        return;
                    String appName = response.getBody().getUpdateTitle();
                    String updateUrl = response.getBody().getUpdateUrl();
                    String updateContent = response.getBody().getUpdateContent();
                    boolean flag = Integer.parseInt(type) == 1 ? true : false;
                    if (TextUtils.isEmpty(updateUrl)) {
                        forceUpdate(appName, updateContent);
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
        observable.subscribeWith(UpVersionObserver);
    }

    private void forceUpdate(String appName, String updateinfo) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
        mDialog.setTitle(appName);
        mDialog.setMessage(updateinfo);
        mDialog.setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setPositiveButton(getString(R.string.str_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.launchAppDetail(MainActivity.this);
            }
        }).setCancelable(true).create().show();
    }

    private void logOut() {
        String userId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().logout(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(loutObserver);
        loutObserver = new NetObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse response) {
                if (response.isSuccess()) {

                    KvStorage.put(LocalConfig.LC_ACCOUNTID, "");
                    FirebaseMessaging.getInstance().deleteToken();
                    startIntent(Login2Activity.class);
                    finish();
                } else {
                    showToast(response.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
            }
        };
        observable.subscribeWith(loutObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        CommonUtils.disposable(loutObserver);
        CommonUtils.disposable(UpVersionObserver);
        CommonUtils.disposable(configObserver);
        LoanOrderHelp.getInstant().onDestroy();
    }

    long requestTime = 0;

    @Override
    protected void onResume() {
        super.onResume();

        if (mTv_title.getText().equals(getString(R.string.menu_Card)) || mTv_title.getText().equals(getString(R.string.menu_ContactUs)) || mTv_title.getText().equals(getString(R.string.menu_My_Profile))
                || mTv_title.getText().equals(getString(R.string.menu_About)) || mTv_title.getText().equals(getString(R.string.menu_Message))) {
            return;
        }

        if (mCurrentFragment != null) {
            mCurrentFragment.setUserVisibleHint(true);
        }

        long timeMillis = System.currentTimeMillis();
        if (timeMillis - requestTime > 2000 && !orderRequestFlag) {
            mLoanTitle = getString(R.string.menu_My_loan);
            QueryOrderDetail();
            requestTime = timeMillis;
        }

    }

    private RecyclerView rvDrawer;

    private void initView() {
        mImageView = findViewById(R.id.imageView);
        mRefresh = findViewById(R.id.Refresh);

        mTv_title = findViewById(R.id.tv_title);
        mDrawer = findViewById(R.id.drawer_layout);
        rvDrawer = findViewById(R.id.rv_drawer);

        String mMobile = KvStorage.get(LocalConfig.LC_MOBILE, "");
        TextView userMobile = findViewById(R.id.tv_user_mobile);
        userMobile.setText(mMobile);
    }

    private void viewClick() {
        mRefresh.setOnClickListener(v -> {
            String mTxRef = KvStorage.get(LocalConfig.LC_TXREF, "");
            if (TextUtils.isEmpty(mTxRef))
                QueryOrderDetail();
            else
                QueryFlutterStatus(mTxRef);
        });
    }

    private void QueryFlutterStatus(String mTxRef) {
        showProgressDialogFragment(getString(R.string.str_loading), false);
        orderRequestFlag = true;
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        String orderId = mLoanDetail.getOrderId();

        Observable observable = NetManager.getApiService().getFlutterStatus(accountId, orderId, mTxRef)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(flutterObserver);
        flutterObserver = new NetObserver<Response<OrderStatus>>() {
            @Override
            public void onNext(Response<OrderStatus> bean) {
                orderRequestFlag = false;
                if (!bean.getBody().getStatus().equals("pending")) {
                    KvStorage.put(LocalConfig.LC_TXREF, "");
                    QueryOrderDetail();
                }
            }

            @Override
            public void onException(ResponseException netException) {
                orderRequestFlag = false;
                showLoanView();
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(flutterObserver);
    }

    @SuppressLint("WrongConstant")
    private void initData() {
        mImageView.setOnClickListener(view -> mDrawer.openDrawer(Gravity.START));
        RequestUpVersion();
        QueryAppConfig();

        if (TextUtils.isEmpty(push)) {
            getListMessage();
        }

    }


    NetObserver messageObserver;

    private void getListMessage() {
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");
        Observable observable = NetManager.getApiService().getMessageUnread(accountId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(messageObserver);
        showProgressDialogFragment(getString(R.string.str_loading), false);
        messageObserver = new NetObserver<Response<MessageBean>>() {
            @Override
            public void onNext(Response<MessageBean> response) {
                dismissProgressDialogFragment();
                if (response.isSuccess()) {
                    int count = Integer.valueOf(response.getBody().getUnreadCount());
                    if (count > 0) {
                        drawerAdapter.setMessageRead(count);
                        drawerAdapter.notifyDataSetChanged();
                        goToMessageUI(count);
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
        observable.subscribeWith(messageObserver);
    }

    private void goToMessageUI(int count) {
        TipsDialogFragment.createBuilder(this, getSupportFragmentManager())
                .setMessage2("You have " + count + " new messages.")
                .setPositiveButtonTxt("Cancel")
                .setNegativeButtonTxt("Read")
                .setNegativeListener(() -> {
                    pushUI("push");
                })
                .show();
    }

    public void showBankNotify() {
        LoanOrderHelp.getInstant().bindCardNotify(it -> {
            if (it == null)
                return;
            if (Constants.ONE.equals(it.getCardNotBindNotify())) {
                TipsDialogFragment.createBuilder(MainActivity.this, getSupportFragmentManager())
                        .setMessage(it.getCardNotBindMessage())
                        .setPositiveButtonTxt("Ignore")
                        .setNegativeButtonTxt("Go binding card")
                        .setNegativeListener(() -> {
                            BindNewCardActivity.Companion.launchAddBankCard(this);
//                            Intent intent = new Intent(this, AddMoreBankCardActivity.class);
//                            intent.putExtra("type", "main");
//                            startActivity(intent);
                        }).show();
            }
        });
    }

    String push = "";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            push = intent.getStringExtra(LocalConfig.LC_FIREBASEPUSH);
            pushUI(push);
        }
    }

    private void pushUI(String push1) {
        if ("push".equals(push1)) {
            mLoanTitle = getString(R.string.menu_Message);
            showMessageFragment();
            KvStorage.put(LocalConfig.LC_FIREBASEPUSH, "");
            push = "";
            drawerAdapter.selectPosition(5);
            drawerAdapter.setMessageRead(0);
            drawerAdapter.notifyDataSetChanged();
            mTv_title.setText(mLoanTitle);
            mRefresh.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RelationshipList(EventMessage event) {
        if (event.what == EventMessage.UPLOADACTIVITY || event.what == EventMessage.BANKCARDSUCCESS) {
            QueryOrderDetail();
        } else if (event.what == EventMessage.LOANDECLINED) {
            mLoanDetail = null;
            showLoanView();
        }

    }

    BaseFragment fragment;
    String mLoanTitle;
    String mOrderStatus;

    private void showLoanView() {
        if ("push".equals(push)) {
            pushUI(push);
            return;
        }
        if (mLoanDetail == null) {
            mLoanTitle = getString(R.string.menu_My_loan);
            fragment = ProductFragment.newInstance();
        } else {
            String orderId = mLoanDetail.getOrderId();
            mOrderStatus = mLoanDetail.getStatus();
            if (TextUtils.isEmpty(orderId) || TextUtils.isEmpty(mOrderStatus)) {
                mOrderStatus = "3";
            }
            switch (mOrderStatus) {
                case Constants.ONE:
                    mLoanTitle = getString(R.string.str_process);
                    fragment = ProcessingFragment.newInstance();
                    break;
                case Constants.TWO:
                    mLoanTitle = getString(R.string.str_active);
//                    fragment = ActiveFragment.newInstance();
                    fragment = LoanActiveFragment2.Companion.newInstance();
                    break;
                case Constants.THREE:
                    mLoanTitle = getString(R.string.menu_My_loan);
                    fragment = ProductFragment.newInstance();
                    break;
                case Constants.FOUR:
                    mLoanTitle = getString(R.string.str_overdue);
                    fragment = OverdueFragment.newInstance();
                    break;
                case Constants.FIVE:
                    mLoanTitle = getString(R.string.my_loan_title_declined);
                    fragment = LoanDeclinedFragment.Companion.newInstance();
                    break;
                case Constants.SIX:
                    mLoanTitle = getString(R.string.str_repayment);
                    fragment = PaymentProgressFragment.newInstance();
                    break;
            }
        }
        mTv_title.setText(mLoanTitle);
        if (mLoanDetail != null) {
            fragment.getArguments().putSerializable("data", mLoanDetail);
        }
        showFragment(fragment);
    }

    private LoanOrderDetail mLoanDetail;
    private boolean orderRequestFlag = false;

    private void QueryOrderDetail() {
        showProgressDialogFragment(getString(R.string.str_loading), false);
        orderRequestFlag = true;
        String accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "");

        Observable observable = NetManager.getApiService().QueryOrderDetail(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        CommonUtils.disposable(orderObserver);
        orderObserver = new NetObserver<Response<LoanOrderDetail>>() {
            @Override
            public void onNext(Response<LoanOrderDetail> loanOrderDetail) {
                orderRequestFlag = false;
//                if (mCont == 1) {
                dismissProgressDialogFragment();
//                } else {
//                    mCont = 1;
//                }
                if (loanOrderDetail.getStatus().getCode() == ApiServerImpl.OK) {
                    mLoanDetail = loanOrderDetail.getBody();
                    Constant.Companion.setIS_FIRST_APPROVE(mLoanDetail.getFirstApprove() == 1);
                    Constant.Companion.setIS_FIRST_APPLY(mLoanDetail.isFirstLoan());
                    if (mLoanDetail == null || Constants.ZERO.equals(mLoanDetail.getReloan())) {
                        KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_FIRSTORDER), Constants.ZERO);
                    } else {
                        KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_FIRSTORDER), mLoanDetail.getReloan());
                    }
                    Boolean flag = Constants.ZERO.equals(mLoanDetail.getReloan());
                    KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_RELOAN), flag);
                    showLoanView();
                } else {
                    showToast(loanOrderDetail.getStatus().getMsg());
                }
            }

            @Override
            public void onException(ResponseException netException) {
                orderRequestFlag = false;
                showLoanView();
                dismissProgressDialogFragment();
            }
        };
        observable.subscribeWith(orderObserver);
    }

    private void showFragment(Fragment fragment) {
        mCurrentFragment = fragment;
        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment, fragment.getClass().getName());
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayActivity2.REQUEST_CODE_TO_PAY) {
            if (resultCode == PayActivity2.RESULT_CODE_SELECT_BANK_TRANFER) {
                VirtualAccountFragment homeFragment = new VirtualAccountFragment();
                showFragment(homeFragment);
                mTv_title.setText(getText(R.string.setting_virtual_account));
                if (drawerAdapter != null) {
                    drawerAdapter.selectPosition(-1);
                }
                requestTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * 第三种方法
     */
    long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "Press again to exit the program", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    private void showMyLoanFragment() {
        ProductFragment homeFragment = ProductFragment.newInstance();
        showFragment(homeFragment);
    }

    private void showMyProfileFragment() {
        MyProfileFragment profileFragment = MyProfileFragment.newInstance();
        showFragment(profileFragment);
    }

    private void showCardFragment() {
        CardFragment cardFragment = CardFragment.newInstance();
        showFragment(cardFragment);
    }

    private void showBankAccountFragment() {
        BankAccountFragment bankAccountFragment = BankAccountFragment.newInstance();
        showFragment(bankAccountFragment);
    }

    private void showOffPaymentFragment() {
        OfflinePaymentFragment offlinePaymentFragment = OfflinePaymentFragment.newInstance();
        showFragment(offlinePaymentFragment);
    }

    private void showMessageFragment() {
        MessageFragment messageFragment = MessageFragment.newInstance();
        showFragment(messageFragment);
    }

    private void showHelpFragment() {
        HelpFragment helpFragment = HelpFragment.newInstance();
        showFragment(helpFragment);
    }

    private void showAboutFragment() {
        AboutFragment aboutFragment = AboutFragment.newInstance();
        showFragment(aboutFragment);
    }

    private void showCallUsFragment() {
        CallUsFragment aboutFragment = CallUsFragment.newInstance();
        showFragment(aboutFragment);
    }

    private void test1() {
        BindNewCardActivity.Companion.launchAddBankAccount(this);
//        PayActivity2.Companion.launchPayActivity(this, "11111","111111");
        if (true) {
            return;
        }
        CollectDataMgr.Companion.getSInstance().collectAuthData("11111111", new BaseCollectDataMgr.Observer() {
            @Override
            public void success(@Nullable AuthResult response) {
                CollectHardwareMgr.Companion.getSInstance().collectHardware(MainActivity.this, null);
                ToastUtils.showLong("upload success");
                Log.e("Test", " collect data success = " + JSONObject.toJSONString(response));
            }

            @Override
            public void failure(@Nullable String response) {
                Log.e("Test", " collect data failure = " + response);
            }
        });
    }

    private void executeCache() {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                try {
                    LocationMgr.getInstance().getLocation();
                    CollectSmsMgr.Companion.getSInstance().tryCacheSms();
                } catch (Exception e) {
                    if (BuildConfig.DEBUG) {
                        throw e;
                    }
                }
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });

    }
}