package com.tiny.cash.loan.card.ui.pay2.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.loan.RepayLoanResponseBean
import com.tiny.cash.loan.card.bean.repay.MonifyResponseBean
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

class NorLoanPresenter : BasePresenter {
    private var bankNum : String? = null

    constructor(fragment: Fragment) : super(fragment) {

    }

    private var flutterwareObserver: NetObserver<Response<RepayLoanResponseBean>>? = null
    //订单ID ,申请金额
    override fun requestUrl(orderId: String?, amount: String?) {
//        val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//        try {
//            jsonObject.put("accountId", Constant.mAccountId)
//            jsonObject.put("orderId", orderId)
//            jsonObject.put("amount", amount.toString())
//            jsonObject.put("cardNumber", bankNum)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
        val observable: Observable<Response<RepayLoanResponseBean>> = NetManager.getApiService2().SubmitOrderRepay2(
            Constant.mAccountId, orderId, amount.toString(), bankNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        CommonUtils.disposable(flutterwareObserver)
        flutterwareObserver = object : NetObserver<Response<RepayLoanResponseBean>>() {
            override fun onNext(response: Response<RepayLoanResponseBean>) {
                if (isDestroy()) {
                    return
                }
                val loanResponse: RepayLoanResponseBean? = response.body
                if (loanResponse == null) {
                    mObserver?.repayFailure("null ", false, null)
                    return
                }
                if (!TextUtils.equals(loanResponse.status, "1")) {
                    mObserver?.repayFailure("status != 1", true, "nor loan repay status ! = 1")
                    return
                }
                mObserver?.repaySuccess()
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()) {
                    return
                }
                mObserver?.repayFailure(netException.msg, true, "nor repay loan error")
            }
        }
        observable.subscribeWith(flutterwareObserver)
    }

    override fun updateResult() {

    }

    fun getCurBankNum() : String?{
        return bankNum
    }

    fun setCurBankNum(bankNum: String?) {
        this.bankNum = bankNum
    }
}