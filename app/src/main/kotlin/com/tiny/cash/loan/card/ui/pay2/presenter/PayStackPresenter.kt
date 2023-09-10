package com.loan.icreditapp.ui.pay.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.loan.PayStackResponseBean
import com.tiny.cash.loan.card.bean.loan.RepayLoanResponseBean
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.order.PayStackResult
import com.tiny.cash.loan.card.ui.pay2.presenter.BasePresenter
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

class PayStackPresenter : BasePresenter {

    private var mPayStackBean: PayStackResponseBean? = null

    constructor(payFragment: Fragment) : super(payFragment) {

    }

    private var observer1: NetObserver<Response<PayStackResponseBean>>? = null
    override fun requestUrl(orderId: String?, amount: String?) {
        val observable: Observable<Response<PayStackResponseBean>> = NetManager.getApiService2().getPayStackUrl2(
            Constant.mAccountId, orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        CommonUtils.disposable(observer1)
        observer1 = object : NetObserver<Response<PayStackResponseBean>>() {
            override fun onNext(response: Response<PayStackResponseBean>) {
                if (isDestroy()){
                    return
                }
                var payStackBean = response.body
                if (payStackBean == null){
                    mObserver?.repayFailure("null", false, null)
                    return
                }
                mPayStackBean = payStackBean
                if (!TextUtils.isEmpty(mPayStackBean!!.authorizationURL)){
                    mObserver?.toWebView(mPayStackBean!!.authorizationURL!!)
                } else {
                    mObserver?.repayFailure(" request null", true, "request payStack failure")
                }
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()){
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "request payStack error")
            }
        }
        observable.subscribeWith(observer1)
    }

    private var observer2: NetObserver<Response<PayStackResult>>? = null
    override fun updateResult() {
        val jsonObject: JSONObject = JSONObject()
        try {
            jsonObject.put("accountId", Constant.mAccountId)
            jsonObject.put("orderId", orderId)
            if (mPayStackBean != null) {
                jsonObject.put("reference", mPayStackBean!!.reference)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val observable: Observable<Response<PayStackResult>> = NetManager.getApiService2().payStackResult(
            Constant.mAccountId, orderId, mPayStackBean!!.reference)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        CommonUtils.disposable(observer2)
        observer2 = object : NetObserver<Response<PayStackResult>>() {
            override fun onNext(response: Response<PayStackResult>) {
                if (isDestroy()){
                    return
                }
                var payStackResult = response.body
                if (payStackResult == null){
                    mObserver?.repayFailure("null", false, null)
                    return
                }
                if (TextUtils.isEmpty(payStackResult.status)){
                    mObserver?.repayFailure("pay stack null", true, "update payStack result failure" )
                    LogSaver.logToFile(" pay stack error = " + jsonObject.toString())
                    return
                }
                val isSuccess = TextUtils.equals(payStackResult.status, "1") || TextUtils.equals(payStackResult.status, "success")
                if (!isSuccess){
                    mObserver?.repayFailure(" pay stack not correct = " + jsonObject.toString(), true, "update payStack result status not correct, try again.")
                    LogSaver.logToFile(" pay stack not correct = " + jsonObject.toString())
                    return
                }
                mObserver?.repaySuccess()
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()){
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "update payStack result error")
            }
        }
        observable.subscribeWith(observer2)
    }


}