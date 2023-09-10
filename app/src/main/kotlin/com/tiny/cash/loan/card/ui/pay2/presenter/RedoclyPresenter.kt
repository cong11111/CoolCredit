package com.tiny.cash.loan.card.ui.pay2.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment

import com.tiny.cash.loan.card.ui.pay2.presenter.BasePresenter
import org.json.JSONException
import org.json.JSONObject

//class RedoclyPresenter : BasePresenter {
//
//    constructor(payFragment: Fragment) : super(payFragment) {
//
//    }
//
//    override fun requestUrl(orderId: String?, amount: String?) {
//        val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//        try {
//            jsonObject.put("accountId", Constant.mAccountId)
//            jsonObject.put("orderId", orderId)
//            jsonObject.put("amount", amount)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        OkGo.post<String>(Api.REDOCLY_REPAY_PAGE).tag(PayFragment.TAG)
//            .upJson(jsonObject)
//            .execute(object : StringCallback() {
//                override fun onSuccess(response: Response<String>) {
//                    if (isDestroy()) {
//                        return
//                    }
//                    var redoclueBean = CheckResponseUtils.checkResponseSuccess(
//                        response,
//                        RedocluResponseBean::class.java
//                    )
//                    if (redoclueBean == null) {
//                        mObserver?.repayFailure(response, false, null)
//                        return
//                    }
//                    if (TextUtils.isEmpty(redoclueBean!!.pageURL)) {
//                        mObserver?.repayFailure(response, true, " redocly pageUrl = null")
//                        return
//                    }
//                    mObserver?.toWebView(redoclueBean!!.pageURL!!)
//                }
//
//                override fun onError(response: Response<String>) {
//                    super.onError(response)
//                    if (isDestroy()) {
//                        return
//                    }
//                    mObserver?.repayFailure(response, true, "request redocly error")
//                }
//            })
//    }
//
//    override fun updateResult() {
//        mObserver?.repaySuccess()
//    }
//
//
//}