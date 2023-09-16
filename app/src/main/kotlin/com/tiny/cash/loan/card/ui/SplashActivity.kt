package com.tiny.cash.loan.card.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.base.BaseActivity
import com.tiny.cash.loan.card.feature.main.MainActivity
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail
import com.tiny.cash.loan.card.ui.login2.Login2Activity
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.DeviceInfo
import com.tiny.cash.loan.card.utils.LocalConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    private val TAG = "SplashActivity"

    private val TO_WELCOME_PAGE = 111

    private val TO_MAIN_PAGE = 112

    private var requestTime: Long = 0

    private var mHandler: Handler? = null

    init {
        mHandler = Handler(
            Looper.getMainLooper()
        ) { message ->
            when (message.what) {
                TO_WELCOME_PAGE -> {
                    mHandler?.removeCallbacksAndMessages(null)
//                    OkGo.getInstance().cancelTag(TAG)
//                    val login2Intent = Intent(this@LauncherActivity, WelcomeActivity::class.java)
                    val login2Intent = Intent(this@SplashActivity, Login2Activity::class.java)
                    startActivity(login2Intent)
                    finish()
                }
                TO_MAIN_PAGE -> {
                    mHandler?.removeCallbacksAndMessages(null)
//                    OkGo.getInstance().cancelTag(TAG)
                    val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.theme_color))
        BarUtils.setStatusBarLightMode(this, false)
        setContentView(R.layout.activity_splash)
        //        boolean openGuide = SPUtils.getInstance().getBoolean(KEY_GUIDE, true);
//        boolean openGuide = false;
//        if (openGuide) {
//            SPUtils.getInstance().put(KEY_GUIDE, false);
//        }
        val accountId = SPUtils.getInstance().getString(LocalConfig.LC_ACCOUNTID)
        val token = SPUtils.getInstance().getString(LocalConfig.LC_TOKEN)
        val mobile = SPUtils.getInstance().getString(LocalConfig.LC_MOBILE)
        val lastLoginTime = SPUtils.getInstance().getLong(Constant.KEY_LOGIN_TIME, 0L)
        var canUseToken = true
        if (lastLoginTime > 0 && System.currentTimeMillis() >= lastLoginTime) {
            val deltaTime = System.currentTimeMillis() - lastLoginTime
            val TIME : Long = 30L * 24 * 60 * 60 * 1000
            if (deltaTime >= TIME) {
                canUseToken = false
            }
        }
        Constant.mToken = token
//        Log.e(TAG, " token = " + token)

        if (TextUtils.isEmpty(accountId) || TextUtils.isEmpty(token)
            || TextUtils.isEmpty(mobile) || !canUseToken) {
//        if (BuildConfig.DEBUG) {
            if (!canUseToken && BuildConfig.DEBUG) {
                ToastUtils.showShort("token desire")
            }
            LogSaver.logToFile("auto login token has desire last login time = $lastLoginTime" + " curTime = " + System.currentTimeMillis())
            mHandler?.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 1000)
        } else {
            requestDetail(accountId!!, token!!, mobile!!)
            mHandler?.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 3000)
        }
        initHeardData()
    }

    private var orderObserver: NetObserver<Response<LoanOrderDetail>>? = null

    private fun requestDetail(accountId: String, token: String, mobile : String) {
        requestTime = System.currentTimeMillis()
        if (BuildConfig.DEBUG) {
            Log.i(TAG, " launcher activity ... = $accountId")
        }
        val observable: Observable<Response<LoanOrderDetail>> = NetManager.getApiService().QueryOrderDetail(accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(orderObserver)
        orderObserver = object : NetObserver<Response<LoanOrderDetail>>() {
           override fun onNext(response: Response<LoanOrderDetail>) {
               if (isFinishing || isDestroyed) {
                   return
               }
               var successEnter = false
               val orderDetail = response.body
               if (orderDetail != null && !TextUtils.isEmpty(orderDetail.token)) {
                   Constant.mLaunchOrderInfo = orderDetail
                   successEnter = true
                   Constant.mAccountId = accountId
                   Constant.mToken = token
                   Constant.mMobile = mobile
               }
               mHandler?.sendEmptyMessageDelayed(if (successEnter) TO_MAIN_PAGE else TO_WELCOME_PAGE,100)
            }

            override fun onException(netException: ResponseException) {
                if (isFinishing || isDestroyed) {
                    return
                }
                mHandler?.sendEmptyMessage(TO_WELCOME_PAGE)
            }
        }
        observable.subscribeWith(orderObserver)
    }

    override fun onDestroy() {
        mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    private fun initHeardData() {
        DeviceInfo.getInstance(this).verName
        DeviceInfo.getInstance(this).versionCode
        DeviceInfo.getInstance(this).channelName
        DeviceInfo.getInstance(this).init()
    }
}