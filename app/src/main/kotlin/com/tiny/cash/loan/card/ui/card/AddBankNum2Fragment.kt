package com.tiny.cash.loan.card.ui.card

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.Constants
import com.tiny.cash.loan.card.bean.bank.AccessCodeResponseBean
import com.tiny.cash.loan.card.bean.bank.UploadCardResponseBean
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.log.LogSaver
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.request.params.BankCardParams
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.net.response.data.bean.BankBoundResult
import com.tiny.cash.loan.card.ui.base.BaseFragment2
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.FirebaseUtils
import com.tiny.cash.loan.card.utils.KvStorage
import com.tiny.cash.loan.card.utils.LocalConfig
import com.tiny.cash.loan.card.widget.BlankTextWatcher
import com.tiny.cash.loan.card.widget.EditTextContainer
import com.tiny.cash.loan.card.widget.ExpiryTextWatcher
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject


class AddBankNum2Fragment : BaseFragment2() {

    private val TAG = "AddBankNum2Fragment"

    private var editBankNum: EditTextContainer? = null
    private var etChooseDate: AppCompatEditText? = null
    private var etCvv: AppCompatEditText? = null

    private var flCommit: FrameLayout? = null
    private var flLoading: FrameLayout? = null

    private val TYPE_SHOW_LOADING = 111
    private val TYPE_HIDE_LOADING = 112
    private val TYPE_SHOW_TOAST = 113
    private var toastMsg: String? = null

    private val mHandler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            when (msg.what) {
                TYPE_SHOW_LOADING -> {
                    flLoading?.visibility = View.VISIBLE
                }

                TYPE_HIDE_LOADING -> {
                    flLoading?.visibility = View.GONE
                }

                TYPE_SHOW_TOAST -> {
                    if (!TextUtils.isEmpty(toastMsg)) {
                        ToastUtils.showShort(toastMsg)
                    }
                }
            }
            return false
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_banknum, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editBankNum = view.findViewById(R.id.edit_container_add_bank_card_banknum)
        etChooseDate = view.findViewById(R.id.et_add_bank_card_choose_date)
        etCvv = view.findViewById(R.id.et_add_bank_card_cvv)
        flCommit = view.findViewById(R.id.fl_add_bank_num_commit)
        flLoading = view.findViewById(R.id.fl_add_bank_num_loading)

        var expiryTextWatcher = ExpiryTextWatcher(etChooseDate)
        etChooseDate?.addTextChangedListener(expiryTextWatcher)

        val editText: AppCompatEditText? = editBankNum?.getEditText()
        if (editText != null) {
            var blankTextWatcher = BlankTextWatcher(editText)
            editText.addTextChangedListener(blankTextWatcher)
        }
        editBankNum?.setInputNum()
//
        flCommit?.setOnClickListener(OnClickListener {
            if (checkBankNum()) {
                val cardNum = editBankNum?.getText()?.replace(" ", "")
                val expireDate = etChooseDate?.text
                val cvv = etCvv?.text
                val expireList = expireDate?.split("/")
                if (expireList?.size == 2) {
                    var first: Int? = null
                    var second: Int? = null
                    try {
                        first = Integer.parseInt(expireList[0])
                        second = Integer.parseInt(expireList[1])
                    } catch (e: Exception) {

                    }
                    if (first == null || second == null) {
                        ToastUtils.showShort("expiry date is not correct")
                        return@OnClickListener
                    }
                    FirebaseUtils.logEvent("fireb_card_submit")
                    Log.e(
                        TAG, " first = " + first + " second = " + second
                                + " cardNum = " + cardNum
                    )
                    checkNeedBindCard(cardNum.toString(), cvv.toString(), first, second)
                }
            }
        })
    }

    private var bankBoundObserver: NetObserver<Response<BankBoundResult>>? = null
    private fun checkNeedBindCard(
        cardNum: String, cvc: String,
        mouth: Int, year: Int
    ) {
        mHandler?.sendEmptyMessage(TYPE_SHOW_LOADING)
        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        val observable: Observable<Response<BankBoundResult>> =
            NetManager.getApiService().queryCardBound(accountId, cardNum)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(bankBoundObserver)
        bankBoundObserver = object : NetObserver<Response<BankBoundResult>>() {
            override fun onNext(response: Response<BankBoundResult>) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                val body = response.body
                if (Constants.ZERO == body.cardBound) {
                    val card: Card = Card.Builder(cardNum, mouth, year, cvc).build()
                    Log.e(TAG, " is valid = " + card.isValid)
                    val charge = Charge()
                    charge.card = card
                    getAccessCode(charge, cardNum, cvc, mouth, year)
                } else {
                    ToastUtils.showShort(body.cardBoundMessage)
                }
            }

            override fun onException(netException: ResponseException) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
            }
        }
        observable.subscribeWith(bankBoundObserver)
    }

    private fun checkBankNum(): Boolean {
        if (editBankNum == null || TextUtils.isEmpty(editBankNum?.getText())) {
            ToastUtils.showShort("card num is empty")
            return false
        }
        if (etChooseDate == null || TextUtils.isEmpty(etChooseDate?.getText())) {
            ToastUtils.showShort("bank num is empty")
            return false
        }
        if (etCvv == null || TextUtils.isEmpty(etCvv?.text)) {
            ToastUtils.showShort("bank cvv is empty")
            return false
        }

//        expiryMonth : Int, expiryYear : Int
        return true
    }

    private var accessObserver: NetObserver<Response<AccessCodeResponseBean>>? = null
    fun getAccessCode(
        charge: Charge, cardNum: String, cvc: String,
        mouth: Int, year: Int
    ) {
        mHandler?.sendEmptyMessage(TYPE_SHOW_LOADING)
        var jsonObject: JSONObject = JSONObject()
        try {
            jsonObject.put("accountId", Constant.mAccountId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        val observable: Observable<Response<AccessCodeResponseBean>> =
            NetManager.getApiService2().fetchAccessCode2(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(accessObserver)
        accessObserver = object : NetObserver<Response<AccessCodeResponseBean>>() {
            override fun onNext(response: Response<AccessCodeResponseBean>) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                val responseBean: AccessCodeResponseBean? = response.body
                if (responseBean == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " get access code ." + response)
                    }
                    errorMsg("get access code failure", "null", jsonObject)
                    return
                }
                charge.accessCode = responseBean.accessCode
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "access code = " + charge.accessCode)
                }
                chargeCard(charge, cardNum, cvc, mouth, year)
            }

            override fun onException(netException: ResponseException) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                errorMsg("get access code error", netException.msg, jsonObject)
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "get bank list = " + netException.msg)
                }
            }
        }
        observable.subscribeWith(accessObserver)
    }

    private fun chargeCard(
        charge: Charge, cardNum: String, cvc: String,
        first: Int, second: Int
    ) {
//        PaystackSdk.setPublicKey()
        mHandler?.sendEmptyMessage(TYPE_SHOW_LOADING)
        PaystackSdk.chargeCard(requireActivity(), charge, object : Paystack.TransactionCallback {
            override fun onSuccess(transaction: Transaction) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                Log.e(TAG, "onSuccess:    " + transaction.getReference())
//                verifyOnServer(transaction.getReference())
                uploadCard(transaction.getReference(), cardNum, cvc, first, second)
            }

            //此函数只在请求OTP保存引用之前调用，如果需要，可以解除otp验证
            override fun beforeValidate(transaction: Transaction) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "beforeValidate: " + transaction.getReference())
                }
            }

            override fun onError(error: Throwable, transaction: Transaction?) {
                // If an access code has expired, simply ask your server for a new one
                // and restart the charge instead of displaying error
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                errorMsg("bind card failure pay stack error " + error.message, error.message)
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, " = " + transaction.toString(), error)
                }
            }
        })
    }

    private var cardObserver: NetObserver<Response<UploadCardResponseBean>>? = null
    private fun uploadCard(
        reference: String, cardNum: String, cvc: String,
        expiryMonth: Int, expiryYear: Int
    ) {
//                expireDate	String	Y	过期日	格式 MM/YY
//                	String	Y	CVV-卡片背面后三位数字
//        reference	String	Y	交易号
        mHandler?.sendEmptyMessage(TYPE_SHOW_LOADING)
        var jsonObject: JSONObject = JSONObject()
        try {
            jsonObject.put("accountId", Constant.mAccountId)
            jsonObject.put("cardNumber", cardNum)
            //卡号
            jsonObject.put("expireDate", expiryMonth.toString() + "/" + expiryYear)
            jsonObject.put("cvv", cvc)
            jsonObject.put("reference", reference)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val backParams = BankCardParams()
        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
        backParams.accountId = accountId
        backParams.cardNumber = cardNum
        backParams.expireDate = expiryMonth.toString() + "/" + expiryYear
        backParams.cvv = cvc
        backParams.reference = reference

        val observable: Observable<Response<UploadCardResponseBean>> =
            NetManager.getApiService().verifyReference(backParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(accessObserver)
        cardObserver = object : NetObserver<Response<UploadCardResponseBean>>() {
            override fun onNext(response: Response<UploadCardResponseBean>) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                val responseBean: UploadCardResponseBean? = response.body
                if (responseBean == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " get access code null .")
                    }
                    errorMsg("upload bank card failure ", " get access code null .", jsonObject)
                    return
                }
                if (responseBean.hasUpload != true) {
                    ToastUtils.showShort("verify bank card not correct")
                    errorMsg(
                        "verify bank card not correct ",
                        "verify bank card not correct",
                        jsonObject
                    )
                    return
                }
                ToastUtils.showShort("bind card success")
                FirebaseUtils.logEvent("fireb_card_success")
                if (activity is BindNewCardActivity) {
                    var bindNewCardActivity: BindNewCardActivity = activity as BindNewCardActivity
                    bindNewCardActivity.toStep(BindNewCardActivity.BIND_BINK_CARD_SUCCESS)
                }
            }

            override fun onException(netException: ResponseException) {
                mHandler?.sendEmptyMessage(TYPE_HIDE_LOADING)
                errorMsg("upload bank card error ", netException.msg, jsonObject)
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "get bank list = " + netException.msg)
                }
            }
        }
        observable.subscribeWith(cardObserver)
    }

    private fun errorMsg(errorMsg: String, response: String?, requestJson: JSONObject) {
        toastMsg = errorMsg;
        mHandler.removeMessages(TYPE_SHOW_TOAST)
        mHandler.sendEmptyMessage(TYPE_SHOW_TOAST)
        val result = StringBuffer()
        if (response != null) {
            result.append(response)
        }
        result.append(requestJson.toString())
        LogSaver.logToFile("errorMsg = " + errorMsg + " data = " + result.toString())
    }

    private fun errorMsg(errorMsg: String, error: String?) {
        toastMsg = errorMsg;
        mHandler.removeMessages(TYPE_SHOW_TOAST)
        mHandler.sendEmptyMessage(TYPE_SHOW_TOAST)
        val result = StringBuffer()
        if (!TextUtils.isEmpty(error)) {
            result.append(error)
        }
        LogSaver.logToFile("errorMsg = " + errorMsg + " data = " + result.toString())
    }

    override fun onDestroy() {
        CommonUtils.disposable(bankBoundObserver)
        CommonUtils.disposable(accessObserver)
        CommonUtils.disposable(accessObserver)
        mHandler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}