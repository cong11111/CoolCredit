package com.tiny.cash.loan.card.ui.login2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.KeyboardUtils.OnSoftInputChangedListener
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.Constants
import com.tiny.cash.loan.card.bean.login2.RegLoginBean
import com.tiny.cash.loan.card.bean.login2.UssdBean
import com.tiny.cash.loan.card.bean.login2.VerifySmsCodeBean
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.mgr.ReadSmsMgr
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.base.BaseFragment2
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.FirebaseUtils
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import com.tiny.cash.loan.card.widget.InputVerifyCodeView2
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginOtpFragment : BaseFragment2(){
    private var mPhoneNum : String? = null
    private var mPrex : String? = null

    private var ivBack : AppCompatImageView? = null
    private var tvCommit : AppCompatTextView? = null
    private var tvDesc1 : AppCompatTextView? = null
    private var tvUssd : AppCompatTextView? = null
    private var verifyCodeView : InputVerifyCodeView2? = null
    private var flLoading : FrameLayout? = null
    private var viewBottom : View? = null
    private var icIcon : AppCompatImageView? = null
    private var clBottom : View? = null

    private var mHandler: Handler? = null
    private var mAuthCode : String? = null

    private val MAX_TIME = 60
    private var mCurTime: Int = MAX_TIME

    private var isResend = false

    companion object {
        private const val TYPE_TIME_REDUCE = 1111

        const val TAG = "LoginOtpFragment"
    }

    init {
        mHandler = Handler(Looper.getMainLooper()) { message ->
            when (message.what) {
                TYPE_TIME_REDUCE -> {
                    mCurTime--
                    if (tvCommit != null) {
                        if (mCurTime == MAX_TIME) {
                            tvCommit?.text = StringUtils.getString(R.string.resend)
                            tvCommit?.isEnabled = true
//                            tvCommit?.setTextColor(Color.parseColor("#0EC6A2"))
                            mHandler?.sendEmptyMessageDelayed(TYPE_TIME_REDUCE, 1000)
                        } else if (mCurTime == 0) {
                            mHandler?.removeMessages(TYPE_TIME_REDUCE)
                            tvCommit?.text = StringUtils.getString(R.string.resend)
                            tvCommit?.isEnabled = true
                            isResend = true
//                            tvCommit?.setTextColor(Color.parseColor("#0EC6A2"))
                            mCurTime = MAX_TIME
                        } else {
                            val text = ( mCurTime.toString() + "s")
                            tvCommit?.isEnabled = false
                            tvCommit?.text = text
                            mHandler?.sendEmptyMessageDelayed(TYPE_TIME_REDUCE, 1000)
                            if (mCurTime <= 30) {
                                if (tvUssd?.visibility != View.VISIBLE) {
                                    tvUssd?.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
            false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack = view.findViewById<AppCompatImageView>(R.id.iv_title_back)
        tvCommit = view.findViewById<AppCompatTextView>(R.id.tv_otp_login_commit)
        verifyCodeView = view.findViewById<InputVerifyCodeView2>(R.id.input_verify_code_otp)
        flLoading = view.findViewById<FrameLayout>(R.id.fl_otp_login_loading)
        tvDesc1 = view.findViewById<AppCompatTextView>(R.id.tv_login_otp_desc1)
        tvUssd = view.findViewById<AppCompatTextView>(R.id.tv_can_not_recevie)
        viewBottom = view.findViewById<View>(R.id.view_bottom_otp)
        icIcon = view.findViewById<AppCompatImageView>(R.id.iv_login2_icon)
        clBottom = view.findViewById<View>(R.id.cl_otp_bottom)

        verifyCodeView?.setObserver(object : InputVerifyCodeView2.Observer {
            override fun onEnd() {
                if (isDestroy()) {
                    return
                }
                if (verifyCodeView != null) {
                    val verifyCode: String? = verifyCodeView?.getVerifyCode()
                    if (TextUtils.isEmpty(verifyCode)) {
                        ToastUtils.showShort("verify code = null. please input again.")
                        return
                    }
                    checkVerifySmsCode(false)
                }
            }

            override fun onFocusChange(hasFocus: Boolean) {
                if (isDestroy()) {
                    return
                }
                var colorRes: Int
                if (hasFocus) {
                    colorRes = R.color.verify_sms_select
                } else {
                    colorRes = R.color.signin_unselect
                }
//                tvVerifyCode?.setTextColor(resources.getColor(colorRes))
            }
        })

        ivBack?.setOnClickListener{
            if (activity is Login2Activity) {
                (activity as Login2Activity).toLoginFragment()
                if (KeyboardUtils.isSoftInputVisible(requireActivity())){
                    KeyboardUtils.hideSoftInput(requireActivity())
                }
            }
        }
        tvCommit?.setOnClickListener{
            if (TextUtils.isEmpty(getFinalPhoneNum())){
                ToastUtils.showShort("phone num is null")
            }
            requestSendSms(getFinalPhoneNum())
            mHandler?.sendEmptyMessage(TYPE_TIME_REDUCE)
        }
        tvCommit?.isEnabled = false
        mHandler?.sendEmptyMessage(TYPE_TIME_REDUCE)

        verifyCodeView?.post(Runnable {
            if (isDestroy() || verifyCodeView == null){
                return@Runnable
            }
            verifyCodeView!!.getEditText()?.let {
                KeyboardUtils.showSoftInput(it)
            }
        })

        tvUssd?.setOnClickListener{
            toSendCodeActivity()
        }

        val text = resources.getString(R.string.login_otp)
        val textDesc = String.format(text, getFinalPhoneNum())
        tvDesc1?.text = textDesc

        KeyboardUtils.registerSoftInputChangedListener(requireActivity(), object : OnSoftInputChangedListener {
            override fun onSoftInputChanged(height: Int) {
                if (icIcon == null || clBottom == null){
                    return
                }
                if (height == 0){
                    icIcon?.visibility = View.VISIBLE
                } else {
                    icIcon?.visibility = View.GONE
                }
                val layoutParams = clBottom!!.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomMargin = (height)
                clBottom!!.layoutParams = layoutParams

            }

        })
        ReadSmsMgr.setObserver(object : ReadSmsMgr.Observer {
            override fun onReceiveAuthCode(authCode: String) {
                if (TextUtils.equals(mAuthCode, authCode)) {
                    return
                }
                mAuthCode = authCode
                if (TextUtils.isEmpty(mAuthCode)) {
                    return
                }
                mHandler?.postDelayed(Runnable {
                    if (isDestroy()){
                        return@Runnable
                    }
                    verifyCodeView?.setVerifyCode(mAuthCode!!)
                }, 100)

            }

        })

    }
    fun setPhoneNum(prex : String , phoneNum: String){
        mPrex = prex
        mPhoneNum = phoneNum
    }

    private fun checkVerifySmsCode(needTip : Boolean){
        if (TextUtils.isEmpty(getFinalPhoneNum())) {
            if (BuildConfig.DEBUG) {
                throw java.lang.IllegalArgumentException("checkVerifySmsCode")
            }
            return
        }
        var verifyCode = verifyCodeView?.getVerifyCode()
        if (TextUtils.isEmpty(verifyCode)) {
            if (needTip) {
                ToastUtils.showShort(" verify code is null")
            }
//            verifyCodeView?.clearAll()
            return
        }
//        if (checkClickFast()){
//            return
//        }
//        FirebaseUtils.logEvent("fireb_register_start")
        requestVerifySmsCode(verifyCode)
    }

    private var checkCodeObserver: NetObserver<Response<VerifySmsCodeBean>>? = null
    private fun requestVerifySmsCode(verifyCode: String?) {
        flLoading?.visibility = View.VISIBLE
        val finalPhoneNum = getFinalPhoneNum()
        val observable: Observable<Response<VerifySmsCodeBean>> =
            NetManager.getApiService().checkSmsCodeV2(finalPhoneNum, verifyCode) //“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(checkCodeObserver)
        checkCodeObserver = object : NetObserver<Response<VerifySmsCodeBean>>() {
            override fun onNext(response: Response<VerifySmsCodeBean>) {
                if (isDestroy()){
                    return
                }
                flLoading?.visibility = View.GONE
                val baseResponseBean: VerifySmsCodeBean? = response.body
                if (baseResponseBean == null) {
                    ToastUtils.showShort(resources.getString(R.string.str_Illegal_verification_code))
                    return
                }
                if (!baseResponseBean.verifyed) {
                    ToastUtils.showShort(resources.getString(R.string.check_sms_code_verify_failure))
                    return
                }
                regOrLogin()
            }

            override fun onException(netException: ResponseException) {
                flLoading?.visibility = View.GONE
                Log.e(TAG, "request send sms error")
                ToastUtils.showShort("verify sms code error ...")
            }
        }
        observable.subscribeWith(checkCodeObserver)
    }

    private var regOrLoginObserver: NetObserver<Response<RegLoginBean>>? = null
    private fun regOrLogin(){
        flLoading?.visibility = View.VISIBLE
        KvStorage.put(LocalConfig.LC_TOKEN, "")
        val finalPhoneNum = getFinalPhoneNum()
        val observable: Observable<Response<RegLoginBean>> =
            NetManager.getApiService().regOrLogin(finalPhoneNum) //“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(regOrLoginObserver)
        regOrLoginObserver = object : NetObserver<Response<RegLoginBean>>() {
            override fun onNext(response: Response<RegLoginBean>) {
                if (isDestroy()){
                    return
                }
                flLoading?.visibility = View.GONE
                val regLoginBean: RegLoginBean? = response.body
                if (regLoginBean == null) {
                    ToastUtils.showShort(response.status.msg)
                    return
                }
                if (TextUtils.isEmpty(regLoginBean.token)){
                    return
                }
                toHomePage(regLoginBean)

            }

            override fun onException(netException: ResponseException) {
                flLoading?.visibility = View.GONE
                Log.e(TAG, "sign in error")
                var errorStr = ""
                if (netException != null){
                    try {
                        errorStr = netException.msg
                    } catch (e : Exception) {

                    }
                }
                ToastUtils.showShort("sign in error..$errorStr")
            }
        }
        observable.subscribeWith(regOrLoginObserver)

    }

    private var startFlag = false

    override fun onResume() {
        super.onResume()
        ReadSmsMgr.onResume()
        if (startFlag) {
            startFlag = false
            ussdLogin()
        }
    }

    override fun onDestroy() {
        CommonUtils.disposable(checkCodeObserver)
        CommonUtils.disposable(regOrLoginObserver)
        CommonUtils.disposable(mUssdObserver)
        CommonUtils.disposable(sendCodeObserver)
        super.onDestroy()
        ReadSmsMgr.onDestroy()
    }

    private fun toSendCodeActivity(){
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        val tel = Uri.encode("*347*8#")
        intent.data = Uri.parse("tel:$tel")
        startActivity(intent)
        startFlag = true
    }

    private var sendCodeObserver: NetObserver<Response<*>?>? = null
    private fun requestSendSms(phoneNum : String) {
        flLoading?.visibility = View.VISIBLE
        tvCommit?.isEnabled = false
        val observable: Observable<Response<*>> =
            NetManager.getApiService().sendSmsCode(phoneNum, Constants.ONE) //“1”:注册，“2”：修改密码
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(sendCodeObserver)
        sendCodeObserver = object : NetObserver<Response<*>?>() {
            override fun onNext(response: Response<*>) {
                flLoading?.visibility = View.GONE

                if (response == null) {
                    ToastUtils.showShort(" send sms failure.")
                    tvCommit?.isEnabled = true
                    return
                }
                if (response.isSuccess != true) {
                    ToastUtils.showShort("" + response.status)
                    tvCommit?.isEnabled = true
                    return
                }
                tvCommit?.isEnabled = false
                ToastUtils.showShort("send sms success")
                if (isResend) {
                    FirebaseUtils.logEvent("fireb_resend_sms")
                } else {
                    FirebaseUtils.logEvent("fireb_send_sms")
                }
            }

            override fun onException(netException: ResponseException) {
                flLoading?.visibility = View.GONE
                tvCommit?.isEnabled = true
                Log.e(TAG, "request send sms error")
                ToastUtils.showShort("request send sms error ...")
            }
        }
        observable.subscribeWith(sendCodeObserver)
    }

    private var mUssdObserver: NetObserver<Response<UssdBean>>? = null
    private fun ussdLogin(){
        flLoading?.visibility = View.VISIBLE
        val phoneNum = getFinalPhoneNum()
        val observable: Observable<Response<UssdBean>> = NetManager.getApiService().ussdLogin2(phoneNum)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(mUssdObserver)
        mUssdObserver = object : NetObserver<Response<UssdBean>>() {
            override fun onNext(response: Response<UssdBean>) {
                flLoading?.visibility = View.GONE
                val ussdBean: UssdBean? = response.body
                if (ussdBean == null) {
                    ToastUtils.showShort("ussd login response null")
                    return
                }
                try {
                    LogSaver.logToFile("ussd login response = " + GsonUtils.toJson(ussdBean)
                            + "   mobile = " + phoneNum)
                } catch (e : Exception) {
                    if (BuildConfig.DEBUG) {
                        throw e
                    }
                }
                if (TextUtils.equals(ussdBean.verify, "1")){
                    regOrLogin()
                } else {
                    ToastUtils.showShort(resources.getString(R.string.ussd_login_failure))
                }
            }

            override fun onException(netException: ResponseException) {
                flLoading?.visibility = View.GONE
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "ussd login error")
                }
                ToastUtils.showShort(resources.getString(R.string.ussd_login_error))
            }
        }
        observable.subscribeWith(mUssdObserver)
    }

    private fun toHomePage(bean: RegLoginBean){
        Constant.mAccountId = bean.accountId
        Constant.mToken = bean.token
        Constant.mMobile = bean.mobile

        KvStorage.put(LocalConfig.LC_TOKEN, bean.token)
        KvStorage.put(LocalConfig.LC_MOBILE, bean.mobile)
        KvStorage.put(LocalConfig.LC_ACCOUNTID, bean.accountId)
        if (TextUtils.equals(bean.active, "1")){
            FirebaseUtils.logEvent("fireb_click_register")
        } else if (TextUtils.equals(bean.active, "2")) {
            FirebaseUtils.logEvent("fireb_click_sign")
        }
        SPUtils.getInstance().put(Login2Fragment.KEY_PHONE_NUM_2, mPhoneNum)
        if (KeyboardUtils.isSoftInputVisible(requireActivity())){
            KeyboardUtils.hideSoftInput(requireActivity())
        }
        if (activity is Login2Activity) {
            var signIn : Login2Activity = activity as Login2Activity
            signIn.toHomePage()
        }
    }

    private fun getFinalPhoneNum() : String{
        return mPrex + mPhoneNum
    }
}