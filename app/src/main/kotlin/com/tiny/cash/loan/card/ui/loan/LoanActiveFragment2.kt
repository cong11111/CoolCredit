package com.tiny.cash.loan.card.ui.loan

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.KudiCreditApp.Companion.instance
import com.tiny.cash.loan.card.bean.loan.DiscountAmountBean
import com.tiny.cash.loan.card.bean.loan.DiscountRequest
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.base.BaseLoanFragment
import com.tiny.cash.loan.card.ui.dialog.fragment.AppStarsDialogFragment
import com.tiny.cash.loan.card.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoanActiveFragment2 : BaseLoanFragment() {

    private val TAG = "LoanActiveFragment2"

    private var tvLoanRepay: AppCompatTextView? = null
    private var llBtnTop: LinearLayout? = null
    private var viewBottom: View? = null
    private var tvLoanPay: AppCompatTextView? = null
    private var tvTotalAmount: AppCompatTextView? = null

    private var flCommit: FrameLayout? = null

    companion object {
        fun newInstance(): LoanActiveFragment2? {
            val args = Bundle()
            val fragment = LoanActiveFragment2()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_loan_active_2, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLoanRepay = view.findViewById(R.id.tv_discount_loan_repay)
        llBtnTop = view.findViewById(R.id.ll_loan_repay)
        viewBottom = view.findViewById(R.id.view_loan_repay)
        tvLoanPay = view.findViewById(R.id.tv_discount_loan_repay)
        flCommit = view.findViewById(R.id.fl_loan_paid_commit)
        tvTotalAmount = view.findViewById(R.id.tv_loan_paid_total_amount)

        tvTotalAmount?.text = mOrderInfo?.totalAmount.toString()
        if (checkNeedShowLog()) {
            if (Constant.IS_FIRST_APPLY) {
                FirebaseUtils.logEvent("fireb_activity")
            }
            FirebaseUtils.logEvent("fireb_activity_all")
//            EventBus.getDefault().post(RateUsEvent())
        }

        flCommit?.setOnClickListener {
            clickRepayLoad()
        }

        discountAmount()

        val hasPermission = PermissionUtils.isGranted(PermissionConstants.SMS)
        val hasPermissionCoarseLocation = PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (hasPermissionCoarseLocation && hasPermission) {
            showAppStarDialog()
        }
    }

    fun showAppStarDialog() {
        val showReloan = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_RELOAN), false)
        if (showReloan) {
            val showFlag =
                KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS2), true)
            if (showFlag) {
                AppStarsDialogFragment.createBuilder(context, childFragmentManager)
                    .setNegativeListener { li: String? ->
                        KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_SHOW_APP_STARS2), false)
                        AppUtils.launchAppDetail(instance)
                    }.show()
            }
        }
    }

    //    token	String	Y	header中的token必须传入
//    innerVersionCode	String	Y	header中的innerVersionCode必须传入
//    accountId	String	Y	客户ID
//    orderId	String	Y	订单ID
    private var mDiscountAmount: NetObserver<Response<DiscountAmountBean>>? = null
    private fun discountAmount() {
        if (mOrderInfo == null) {
            return
        }
        val orderId = mOrderInfo!!.orderId
        if (TextUtils.isEmpty(orderId)) {
            return
        }
        val request = DiscountRequest()
        request.token = Constant.mToken
        request.innerVersionCode = MyAppUtils.getAppVersionCode()
        request.accountId = Constant.mAccountId
        request.orderId = orderId

        val observable: Observable<Response<DiscountAmountBean>> =
            NetManager.getApiService().discountAmount(request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(mDiscountAmount)
        mDiscountAmount = object : NetObserver<Response<DiscountAmountBean>>() {
            override fun onNext(response: Response<DiscountAmountBean>) {
                if (isDestroy()) {
                    return
                }
                val discountAmountBean: DiscountAmountBean? = response.body
                updateLoanRepaying(discountAmountBean)
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()) {
                    return
                }
                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                    return
                }
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "order info failure = " + netException.msg)
                }
                ToastUtils.showShort("order info failure")
            }
        }
        observable.subscribeWith(mDiscountAmount)
    }

    private fun updateLoanRepaying(bean: DiscountAmountBean?) {
        if (bean == null || TextUtils.isEmpty(bean.discountSwitch) || TextUtils.equals(bean.discountSwitch, "0")
            || TextUtils.isEmpty(bean.discountAmount)) {
            //关闭
            llBtnTop?.visibility = View.GONE
            viewBottom?.visibility = View.GONE
        } else {
            llBtnTop?.visibility = View.VISIBLE
            viewBottom?.visibility = View.VISIBLE

            val discountAccountStr = resources.getString(R.string.discount_account)
            val discountAmount = String.format(discountAccountStr, bean.discountAmount)
            tvLoanPay?.text = discountAmount
        }
    }

    override fun onDestroy() {
        CommonUtils.disposable(mDiscountAmount)
        super.onDestroy()
    }
}