package com.tiny.cash.loan.card.ui.pay2.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.GsonUtils
import com.flutterwave.raveandroid.RaveUiManager
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.repay.FlutterwareResponse1Bean
import com.tiny.cash.loan.card.bean.repay.FlutterwareResponse2Bean
import com.tiny.cash.loan.card.bean.repay.FlutterwareResultBean
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.request.params.FlutterWaveParams
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.bean.FlutterWaveResult
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FlutterwarePresenter : BasePresenter {

    private var mTxRef : String? = null
    private var mBean : FlutterWaveResult? = null

    constructor(payFragment: Fragment) : super(payFragment) {

    }


    private var txtRefObserver: NetObserver<Response<FlutterwareResponse1Bean>>? = null
    override fun requestUrl(orderId: String?, amount: String?) {
        val observable: Observable<Response<FlutterwareResponse1Bean>> = NetManager.getApiService().getFlutterwaveTxRef2(
            Constant.mAccountId, Constant.mToken, orderId, "2")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(txtRefObserver)

        txtRefObserver = object : NetObserver<Response<FlutterwareResponse1Bean>>() {
            override fun onNext(response: Response<FlutterwareResponse1Bean>) {
                if (isDestroy()){
                    return
                }
                var flutterware1Bean = response.body
                if (flutterware1Bean == null){
                    mObserver?.repayFailure("null", false, null)
                    return
                }
                if (TextUtils.isEmpty(flutterware1Bean.txRef)){
                    mObserver?.repayFailure("txRef null", true, "request getTextRef = null")
                    return
                }
                var card = flutterware1Bean.card
                if (card == null){
                    card = true
                }
                var account = flutterware1Bean.account
                if (account == null){
                    account = false
                }
                var transfer = flutterware1Bean.transfer
                if (transfer == null){
                    transfer = false
                }
                var ussd = flutterware1Bean.ussd
                if (ussd == null){
                    ussd = false
                }
                mTxRef = flutterware1Bean.txRef
                RaveUiManager(mPayFragment!!.activity).acceptAccountPayments(account).acceptCardPayments(card)
                    .acceptBankTransferPayments(transfer).acceptUssdPayments(ussd)
                    .setAmount(flutterware1Bean.amount!!.toDouble()).setCurrency(flutterware1Bean.currency).setfName(flutterware1Bean.firstName)
                    .setlName(flutterware1Bean.lastName).setEmail(flutterware1Bean.email).setPublicKey(flutterware1Bean.publicKey)
                    .setEncryptionKey(flutterware1Bean.encryptionKey).setTxRef(flutterware1Bean.txRef).onStagingEnv(false).
                    withTheme(R.style.MyCustomTheme).initialize()
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()){
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "request getTextRef failure")
            }
        }
        observable.subscribeWith(txtRefObserver)
    }


    fun setFlutterwareBean(bean: FlutterWaveResult) {
        mBean = bean
        uploadJson()
    }

    private var verifyObserver: NetObserver<Response<FlutterwareResponse2Bean>>? = null
    private fun uploadJson(){
        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        val params = FlutterWaveParams()
        params.accountId = accountId
        params.orderId = orderId
        params.chargeType = "2"
        params.txRef = mTxRef
        params.jsonStr = GsonUtils.toJson(mBean)
        val observable: Observable<Response<FlutterwareResponse2Bean>> = NetManager.getApiService().uploadJson2(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(verifyObserver)
        verifyObserver = object : NetObserver<Response<FlutterwareResponse2Bean>>() {
            override fun onNext(response: Response<FlutterwareResponse2Bean>) {
                if (isDestroy()){
                    return
                }
                var flutterware2Bean = response.body
                if (flutterware2Bean == null){
                    mObserver?.repayFailure("null", false, null)
                    return
                }
                val isSuccess = TextUtils.equals(flutterware2Bean.status, "1") || TextUtils.equals(flutterware2Bean.status, "success")
                if (isSuccess){
                    updateResult()
                } else {
                    mObserver?.repayFailure(GsonUtils.toJson(response), true, "flutterwave uploadJson status != 1")
                }
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()){
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "flutter uploadJson error")
            }
        }
        observable.subscribeWith(verifyObserver)
    }

    private var flutterwareObserver: NetObserver<Response<FlutterwareResultBean>>? = null
    override fun updateResult() {
        val observable: Observable<Response<FlutterwareResultBean>> = NetManager.getApiService().getFlutterStatus2(
            Constant.mAccountId, orderId, mTxRef)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(flutterwareObserver)
        flutterwareObserver = object : NetObserver<Response<FlutterwareResultBean>>() {
            override fun onNext(response: Response<FlutterwareResultBean>) {
                if (isDestroy()){
                    return
                }
                var flutterwareResult = response.body
                if (flutterwareResult == null){
                    mObserver?.repayFailure("flutter status null", false, null)
                    return
                }
                if (TextUtils.equals(flutterwareResult.status, "1")){
                    mObserver?.repaySuccess()
                } else {
                    mObserver?.repayFailure(GsonUtils.toJson(flutterwareResult), true, "flutterwave status != 1")
                }
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()){
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "request flutterwave error")
            }
        }
        observable.subscribeWith(flutterwareObserver)
    }
}