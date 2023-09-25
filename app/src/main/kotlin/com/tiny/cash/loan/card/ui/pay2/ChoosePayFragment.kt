package com.tiny.cash.loan.card.ui.pay2

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.ui.pay2.adapter.ChoosePayData
import com.tiny.cash.loan.card.ui.pay2.presenter.NorLoanPresenter
import com.loan.icreditapp.ui.pay.presenter.PayStackPresenter
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.TextInfoResponse
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.bean.repay.MonifyResponseBean
import com.tiny.cash.loan.card.event.ChooseBankListEvent
import com.tiny.cash.loan.card.event.UpdateLoanEvent
import com.tiny.cash.loan.card.global.ConfigMgr
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.bean.FlutterWaveResult
import com.tiny.cash.loan.card.ui.base.BaseFragment2
import com.tiny.cash.loan.card.ui.pay2.adapter.ChoosePayAdapter
import com.tiny.cash.loan.card.ui.pay2.presenter.BasePresenter
import com.tiny.cash.loan.card.ui.pay2.presenter.FlutterwarePresenter
import com.tiny.cash.loan.card.utils.CardNumUtils
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ChoosePayFragment : BaseFragment2() {
    companion object {
        const val TAG = "ChoosePayFragment"
    }

    private var rvPay : RecyclerView? = null
    private var flLoading : FrameLayout? = null
    private var mAdapter : ChoosePayAdapter? = null
    private var ivClose : AppCompatImageView? = null
    private var flBottom : FrameLayout? = null

    private var orderId: String? = null
    private var amount: String? = null
    private var curPresenter: BasePresenter? = null
    private var mBankList: ArrayList<CardResponseBean.Bank> = ArrayList()
    private var mChooseDataList: ArrayList<ChoosePayData> = ArrayList()

    private var norPresenter: NorLoanPresenter? = null
    private var payStackPresenter: PayStackPresenter? = null
    private var flutterwavePresenter: FlutterwarePresenter? = null
    private var needShow: Boolean = false
    private var cardNum: String? = null
    //    private var redoclyPresenter: RedoclyPresenter? = null
//    private var monifyPresenter: MonifyPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_pay, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPay = view.findViewById(R.id.rv_choose_pay)
        flLoading = view.findViewById(R.id.fl_pay_loading)
        ivClose = view.findViewById(R.id.iv_choose_pay_close)
        flBottom = view.findViewById(R.id.fl_choose_pay_bottom)

        initView()
        initData()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        startLoading()
        updateBankListInternal()
    }

    private fun initView() {
        rvPay?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mChooseDataList.clear()
        mChooseDataList.addAll(ChoosePayData.buildList(cardNum, needShow))

        mAdapter = ChoosePayAdapter(mChooseDataList)
        mAdapter?.setOnItemClickListener(object :ChoosePayAdapter.OnItemClickListener {

            override fun onItemClick(bank: ChoosePayData, pos: Int) {
                when(pos) {
                    (0) -> {
                        curPresenter = norPresenter
                        if (TextUtils.isEmpty(norPresenter?.getCurBankNum())) {
                            PayBankListActivity.launchActivityForResult(requireActivity())
                            return
                        }
                        startLoading()
                    }
                    (1) -> {

                        curPresenter = payStackPresenter
                        startLoading()
                    }
                    (2) -> {
                        curPresenter = flutterwavePresenter
                        startLoading()
                    }
                    (3) -> {
                        activity?.setResult(PayActivity2.RESULT_CODE_SELECT_BANK_TRANFER)
                        activity?.finish()
                    }
                }
            }

        })
        rvPay?.adapter = mAdapter

        ivClose?.setOnClickListener{
            activity?.finish()
        }
        flBottom?.setOnClickListener{
            activity?.finish()
        }
    }

    private var monifyObserver: NetObserver<Response<MonifyResponseBean>>? = null

    private fun updateBankListInternal(){
        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        val observable: Observable<Response<MonifyResponseBean>> = NetManager.getApiService2().queryMonify2(accountId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(monifyObserver)
        monifyObserver = object : NetObserver<Response<MonifyResponseBean>>() {
            override fun onNext(response: Response<MonifyResponseBean>) {
                if (isDestroy()) {
                    return
                }
                var monifyBean = response.body
                if (monifyBean != null) {
                    if (TextUtils.equals(monifyBean.reserved, "1")
                        && !TextUtils.isEmpty(monifyBean.bankName)
                        && !TextUtils.isEmpty(monifyBean.accountNumber)
                        && !TextUtils.isEmpty(monifyBean.bankName)) {
                        needShow = true
                    }
                }
                ConfigMgr.getBankList(object : ConfigMgr.CallBack4 {
                    override fun onGetData(bankList: ArrayList<CardResponseBean.Bank>) {
                        if (isDestroy()){
                            return
                        }
                        mBankList.clear()
                        mBankList.addAll(bankList)
                        if (mBankList.isNotEmpty()) {
                            var bank: CardResponseBean.Bank = mBankList[0]
                            norPresenter?.setCurBankNum(bank.cardNumber)

                            mChooseDataList.clear()
                            cardNum = CardNumUtils.getCardNumHide2(bank.cardNumber)
                        }
                        mChooseDataList.clear()
                        mChooseDataList.addAll(ChoosePayData.buildList(cardNum, needShow))
                        Log.e("Test", " test 2222 = " +cardNum + "  needShow = " + needShow)
                        mAdapter?.notifyDataSetChanged()
                    }

                })
            }

            override fun onException(netException: ResponseException) {
                if (isDestroy()) {
                    return
                }
            }
        }
        observable.subscribeWith(monifyObserver)

        ConfigMgr.getTextInfo(object : ConfigMgr.CallBack3 {
            override fun onGetData(textInfoResponse: TextInfoResponse?) {
                if (isDestroy()){
                    return
                }

//                if (!TextUtils.isEmpty(bean.bankCode)) {
//                    selectBankCode?.setEditTextAndSelection(bean.bankCode!!)
//                }

            }

        })
    }

    private fun initData() {
        if (norPresenter == null) {
            norPresenter = NorLoanPresenter(this)
            norPresenter?.setObserver(MyObserver())
            norPresenter?.setData(orderId, amount)
        }
        if (payStackPresenter == null) {
            payStackPresenter = PayStackPresenter(this)
            payStackPresenter?.setData(orderId, amount)
            payStackPresenter?.setObserver(MyObserver())
        }
        if (flutterwavePresenter == null) {
            flutterwavePresenter = FlutterwarePresenter(this)
            flutterwavePresenter?.setData(orderId, amount)
            flutterwavePresenter?.setObserver(MyObserver())
        }
//        if (redoclyPresenter == null) {
//            redoclyPresenter = RedoclyPresenter(this)
//            redoclyPresenter?.setData(orderId, amount)
//            redoclyPresenter?.setObserver(MyObserver())
//        }
//        if (monifyPresenter == null) {
//            monifyPresenter = MonifyPresenter(this)
//            monifyPresenter?.setData(orderId, amount)
//            monifyPresenter?.setObserver(MyObserver())
//        }
        updateBankListInternal()
    }

    private fun startLoading() {
        if (curPresenter == null) {
            return
        }
        curPresenter?.requestUrl(orderId, amount)
        flLoading?.visibility = View.VISIBLE
    }

    fun setData(orderId: String?, amount: String?) {
        this.orderId = orderId
        this.amount = amount
    }

    fun onWebViewEnd() {
        flLoading?.visibility = View.VISIBLE
        curPresenter?.updateResult()
    }

    fun onUpdateBindCard(){
//        Log.e("Test", "on update bind card.")
        Constant.bankList.clear()
        updateBankListInternal()
    }


    fun onFlutterWaveResult(isSuccess: Boolean, bean: FlutterWaveResult?) {
        var statusOk = false
        if (bean != null) {
            statusOk = TextUtils.equals(bean.status, "success")
        }
        if (!isSuccess || !statusOk) {
            var sb = StringBuffer()
            sb.append("Flutterwave result error")
            if (bean != null) {
                if (!isSuccess && bean.data != null) {
                    sb.append(bean!!.data!!.vbvrespmessage)
                } else {
                    sb.append(GsonUtils.toJson(bean))
                }
            }
            flLoading?.visibility = View.GONE
            ToastUtils.showShort(sb.toString())
            return
        }
        flLoading?.visibility = View.VISIBLE
        flutterwavePresenter?.setFlutterwareBean(bean!!)
        flutterwavePresenter?.updateResult()
    }

    private fun toWebViewInternal(url: String) {
        flLoading?.visibility = View.GONE
        if (activity is PayActivity2) {
            var payActivity = activity as PayActivity2
            payActivity.toWebView(url)
        }
    }

    private inner class MyObserver : BasePresenter.Observer {

        override fun toWebView(url: String) {
            toWebViewInternal(url)
        }

        override fun repaySuccess() {
            flLoading?.visibility = View.GONE
            activity?.finish()
            ToastUtils.showShort("repay success")
            EventBus.getDefault().post(UpdateLoanEvent())
        }

        override fun repayFailure(response: String, needTip: Boolean, desc: String?) {
            flLoading?.visibility = View.GONE
            if (needTip) {
                var responseStr = StringBuffer()
                if (!TextUtils.isEmpty(desc)) {
                    responseStr.append(desc)
                }
                if (response != null) {
                    val body = response
                    if (!TextUtils.isEmpty(body) && !Constant.isAabBuild()) {
                        responseStr.append(body.toString())
                    }
                    LogSaver.logToFile(
                        "repay failure = " + responseStr.toString() + " body = "
                                + body
                    )
                }
                if (!TextUtils.isEmpty(responseStr.toString())) {
                    ToastUtils.showShort(responseStr.toString())
                }
            }
        }

        override fun showMonifyPage(bean: MonifyResponseBean) {
            flLoading?.visibility = View.GONE
//            llMonifyResult?.visibility = View.VISIBLE
//            if (TextUtils.equals(bean.reserved, "0")) {
//                tvMonifyTitle?.setText("Offline Tranfer:")
//            } else {
//                tvMonifyTitle?.setText("Virtual Account:")
//            }
//            if (!TextUtils.isEmpty(bean.bankName)) {
//                selectBankName?.setEditTextAndSelection(bean.bankName!!)
//            }
//            if (!TextUtils.isEmpty(bean.bankCode)) {
//                selectBankCode?.setEditTextAndSelection(bean.bankCode!!)
//            }
//            if (!TextUtils.isEmpty(bean.accountName)) {
//                selectAccountName?.setEditTextAndSelection(bean.accountName!!)
//            }
//            if (!TextUtils.isEmpty(bean.accountNumber)) {
//                selectAccountNumber?.setEditTextAndSelection(bean.accountNumber!!)
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onEvent(event: ChooseBankListEvent) {
        if (!TextUtils.isEmpty(event.bankNum)) {
            norPresenter?.setCurBankNum(event.bankNum)
//            tvNorCardNum?.text = CardNumUtils.getCardNumHide2(event.bankNum)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        CommonUtils.disposable(monifyObserver)
        super.onDestroy()
    }

}