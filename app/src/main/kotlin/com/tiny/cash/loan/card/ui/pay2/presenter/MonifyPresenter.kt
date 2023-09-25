package com.loan.icreditapp.ui.pay.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.GsonUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.bean.repay.FlutterwareResultBean
import com.tiny.cash.loan.card.bean.repay.MonifyResponseBean
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.pay2.presenter.BasePresenter
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject

class MonifyPresenter : BasePresenter {

    private var mMonifyBean : MonifyResponseBean? = null

    constructor(payFragment: Fragment) : super(payFragment) {

    }

    private var flutterwareObserver: NetObserver<Response<MonifyResponseBean>>? = null

    override fun requestUrl(orderId: String?, amount: String?) {
        val observable: Observable<Response<MonifyResponseBean>> = NetManager.getApiService2().queryMonify2(Constant.mAccountId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(flutterwareObserver)
        flutterwareObserver = object : NetObserver<Response<MonifyResponseBean>>() {
            override fun onNext(response: Response<MonifyResponseBean>) {
                if (isDestroy()) {
                    return
                }
                var monifyBean = response.body
                if (monifyBean == null) {
                    mObserver?.repayFailure("monity null", false, null)
                    return
                }
//                    if (TextUtils.equals(monifyBean.reserved, "1")) {
                mObserver?.showMonifyPage(monifyBean)
                mMonifyBean = monifyBean
//                    }
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()) {
                    return
                }
                mObserver?.repayFailure(netException.msg, false, "monify error")
            }
        }
        observable.subscribeWith(flutterwareObserver)

    }

    fun getCLipBoardText() : String?{
        if (mMonifyBean == null){
            return null
        }
        var sb = StringBuffer()
        if (!TextUtils.isEmpty(mMonifyBean!!.accountNumber)){
            sb.append(mMonifyBean!!.accountNumber)
        }
       return sb.toString()
    }

    override fun updateResult() {

    }


}