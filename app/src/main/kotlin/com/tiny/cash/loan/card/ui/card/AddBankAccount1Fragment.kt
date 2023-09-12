package com.tiny.cash.loan.card.ui.card

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.bank.BankAccountResponseBean
import com.tiny.cash.loan.card.bean.bank.BankResponseBean
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.event.BankListEvent
import com.tiny.cash.loan.card.event.UpdateLoanEvent
import com.tiny.cash.loan.card.global.ConfigMgr
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.banklist.BankListActivity
import com.tiny.cash.loan.card.ui.base.BaseFragment2
import com.tiny.cash.loan.card.utils.CommonUtils
import com.tiny.cash.loan.card.utils.FirebaseUtils
import com.tiny.cash.loan.card.widget.EditTextContainer
import com.tiny.cash.loan.card.widget.SelectContainer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONException
import org.json.JSONObject

class AddBankAccount1Fragment : BaseFragment2() {

    private val TAG = "AddBankAccount1Fragment"

    private var selectBankList: SelectContainer? = null
    private var editBankNum: EditTextContainer? = null
    private var editBvn: EditTextContainer? = null
    private var flCommit: FrameLayout? = null
    private var viewDivide3: View? = null

    private var mBankData: BankResponseBean.Bank? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_bank_account, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectBankList = view.findViewById(R.id.select_container_add_bank_account_bank_list)
        editBankNum = view.findViewById(R.id.edit_container_add_bank_account_banknum)
        editBvn = view.findViewById(R.id.edit_container_add_bank_account_bvn)
        flCommit = view.findViewById(R.id.fl_add_bank_account_commit)
        viewDivide3 = view.findViewById(R.id.view_divide_3)

        selectBankList?.setOnClickListener(View.OnClickListener {
            var intent: Intent = Intent(activity, BankListActivity::class.java)
            startActivity(intent)
        })

        flCommit?.setOnClickListener(OnClickListener {
            if (checkClickFast()) {
                return@OnClickListener
            }
            if (checkBankAccountAvailable()) {
                uploadBankAccount()
            }
        })
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onEvent(event: BankListEvent) {
        mBankData = event.mData
        selectBankList?.setData(event.mData?.bankName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        CommonUtils.disposable(dict4Observer)
    }

    private fun checkBankAccountAvailable() : Boolean{
        if (mBankData == null){
            ToastUtils.showShort("unselect bank")
            return false
        }
        if (editBankNum == null || editBankNum!!.isEmptyText()){
            ToastUtils.showShort("bank account num == null")
            return false
        }
        return true
    }

    private var dict4Observer: NetObserver<Response<BankAccountResponseBean>>? = null
    private fun uploadBankAccount() {
//        bank account  1408518986
//        Access bank Idise Betty   2284463522
//         Zenith bank 李俊杰  2284462518
//                 Zenith bank 刘正
        var bvn = ""
        if (editBvn?.visibility == View.VISIBLE) {
            val temp = editBvn?.getText()
            if (TextUtils.isEmpty(temp) || temp?.length != 11) {
                ToastUtils.showShort("please enter your correct bvn number");
                return;
            }
            if (!TextUtils.isEmpty(temp)) {
                bvn = temp!!
            }
        }
        val jsonObject: JSONObject = JSONObject()
        try {
            //客户ID
            jsonObject.put("accountId", Constant.mAccountId)
            //银行简码
            jsonObject.put("bankCode", mBankData?.bankCode)
            //银行名称
            jsonObject.put("bankName", mBankData?.bankName)
            //客户填写的银行账号
            jsonObject.put("bankAccountNumber", editBankNum?.getText())
            jsonObject.put("bvn", bvn)
//            if (BuildConfig.DEBUG) {
//                jsonObject.put("bankAccountNumber", "2284462518")
//            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        var bankCode : String = ""
        if (!TextUtils.isEmpty(mBankData?.bankCode)) {
            bankCode = mBankData!!.bankCode!!
        }
        var bankName : String = ""
        if (!TextUtils.isEmpty(mBankData?.bankName)) {
            bankName = mBankData!!.bankName!!
        }
        var editBankNumStr : String = ""
        if (!TextUtils.isEmpty(editBankNum?.getText())) {
            editBankNumStr = editBankNum!!.getText()!!
        }

        val observable: Observable<Response<BankAccountResponseBean>> =
            NetManager.getApiService2().checkBankaccount2(Constant.mAccountId, bankCode ,
                bankName, editBankNumStr, bvn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(dict4Observer)
        dict4Observer = object : NetObserver<Response<BankAccountResponseBean>>() {
            override fun onNext(response: Response<BankAccountResponseBean>) {
                val responseBean: BankAccountResponseBean? = response.body
                if (responseBean == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " upload bank account null" )
                    }
                    return
                }
                if (responseBean.bvnChecked != true) {
                    viewDivide3?.visibility = View.VISIBLE
                    editBvn?.visibility = View.VISIBLE
                    return
                }
                if (responseBean.bankAccountChecked != true) {
                    ToastUtils.showShort("check bank account failure ")
                    return
                }
                FirebaseUtils.logEvent("fireb_bank")
                if (activity is BindNewCardActivity) {
                    var bindNewCardActivity : BindNewCardActivity = activity as BindNewCardActivity
                    bindNewCardActivity.toStep(BindNewCardActivity.SUCCESS)
                    EventBus.getDefault().post(UpdateLoanEvent())
                }
            }

            override fun onException(netException: ResponseException) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "get bank list = " + netException.msg)
                }
            }
        }
        observable.subscribeWith(dict4Observer)
    }

    override fun onDetach() {
        super.onDetach()
    }

}