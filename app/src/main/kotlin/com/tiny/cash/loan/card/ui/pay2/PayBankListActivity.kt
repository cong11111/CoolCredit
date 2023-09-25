package com.tiny.cash.loan.card.ui.pay2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.event.ChooseBankListEvent
import com.tiny.cash.loan.card.global.ConfigMgr
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.base.BaseActivity2
import com.tiny.cash.loan.card.ui.card.BindNewCardActivity
import com.tiny.cash.loan.card.ui.card.CardListAdapter2
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject

class PayBankListActivity : BaseActivity2() {

    companion object {
        const val TO_PAYBANK_LIST_RESULT = 117

        fun launchActivityForResult(context: Activity){
            val intent = Intent(context, PayBankListActivity::class.java)
            context.startActivityForResult(intent, TO_PAYBANK_LIST_RESULT)
        }
    }

    private val TAG = "PayBankListActivity"
    private var ivBack : ImageView? = null
    private var rvBankList : RecyclerView? = null

    private val mBankList = ArrayList<CardResponseBean.Bank>()
    private var mAdapter: CardListAdapter2? = null

    private var flLoading: FrameLayout? = null
    private var flEmpty: FrameLayout? = null
    private var flCommit: FrameLayout? = null
    private var llAddCard: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_banklist)
        ivBack = findViewById(R.id.iv_pay_banklist_back)
        ivBack?.setOnClickListener(object :OnClickListener{
            override fun onClick(v: View?) {
                 finish()
            }

        })
        rvBankList = findViewById(R.id.rv_pay_bank_list)
        flLoading = findViewById(R.id.fl_bank_card_loading)
        flEmpty = findViewById(R.id.fl_bank_card_empty)
        flCommit= findViewById(R.id.fl_pay_banklist_commit)
        llAddCard = findViewById(R.id.ll_pay_banklist_add_card)

        rvBankList?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = CardListAdapter2(mBankList)
        mAdapter?.setOnItemClickListener(object :CardListAdapter2.OnItemClickListener {
            override fun onItemClick(bank: CardResponseBean.Bank, pos: Int) {
                mAdapter?.curPos = pos
                mAdapter?.notifyDataSetChanged()
            }

        })
        rvBankList?.adapter = mAdapter
        if (Constant.bankList.isEmpty()){
            getBankList()
        } else {
            mBankList.clear()
            mBankList.addAll(Constant.bankList)
        }

        flCommit?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                var event = ChooseBankListEvent()
                event.bankNum = mAdapter?.getBankNum()
                EventBus.getDefault().post(event)
                setResult(11111)
                finish()
            }

        })
        llAddCard?.setOnClickListener(View.OnClickListener {
            BindNewCardActivity.launchAddBankCardForResult(this@PayBankListActivity)
        })
    }

    private var dict4Observer: NetObserver<Response<CardResponseBean>>? = null
    private fun getBankList() {
        flEmpty?.visibility = View.GONE
        flLoading?.visibility = View.VISIBLE

        val observable: Observable<Response<CardResponseBean>> =
            NetManager.getApiService2().queryBankCardList2(Constant.mAccountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(dict4Observer)
        dict4Observer = object : NetObserver<Response<CardResponseBean>>() {
            override fun onNext(response: Response<CardResponseBean>) {
                if (isFinishing || isDestroyed){
                    return
                }
                flLoading?.visibility = View.GONE
                val bankBean: CardResponseBean? = response.body
                if (bankBean == null || bankBean.cardlist == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " get bank list null.")
                    }
                    flEmpty?.visibility = View.VISIBLE
                    return
                }
                if (bankBean.cardlist!!.isEmpty()){
                    flEmpty?.visibility = View.VISIBLE
                } else {
                    flEmpty?.visibility = View.GONE
                    mBankList.clear()
                    mBankList.addAll(bankBean.cardlist!!)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onException(netException: ResponseException) {
                if (isFinishing || isDestroyed){
                    return
                }
                flLoading?.visibility = View.GONE
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, " get bank list error =  ." + netException.msg)
                }
            }
        }
        observable.subscribeWith(dict4Observer)
    }

    override fun onDestroy() {
        CommonUtils.disposable(dict4Observer)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BindNewCardActivity.BIND_BINK_CARD_FROM_PAY_BANK){
            setResult(TO_PAYBANK_LIST_RESULT)
            finish()
        }
    }
}