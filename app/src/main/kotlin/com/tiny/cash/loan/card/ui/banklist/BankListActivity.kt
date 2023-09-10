package com.tiny.cash.loan.card.ui.banklist

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.bean.bank.BankResponseBean
import com.tiny.cash.loan.card.bean.bank.CardResponseBean
import com.tiny.cash.loan.card.event.BankListEvent
import com.tiny.cash.loan.card.global.ConfigMgr
import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.NetManager
import com.tiny.cash.loan.card.net.NetObserver
import com.tiny.cash.loan.card.net.ResponseException
import com.tiny.cash.loan.card.net.response.Response
import com.tiny.cash.loan.card.ui.base.BaseActivity2
import com.tiny.cash.loan.card.utils.CommonUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class BankListActivity : BaseActivity2() {
    private val TAG = "BankListActivity"

    private var rvBankList: RecyclerView? = null
    private var ivBack: ImageView? = null
    private val mBankList = ArrayList<BankResponseBean.Bank>()
    private var mAdapter: BankListAdapter? = null
    private var sideBar: WaveSideBar? = null
    private var flLoading: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.theme_color))
        BarUtils.setStatusBarLightMode(this, false)
        setContentView(R.layout.activity_bank_list)
        initializeView()
        if (ConfigMgr.mBankList.size == 0){
            getBankList()
        } else {
            mBankList.clear()
            mBankList.addAll(ConfigMgr.mBankList)
            updateList()
        }
    }

    private fun initializeView() {
        rvBankList = findViewById(R.id.rv_bank_list)
        ivBack = findViewById(R.id.iv_bank_list_back)
        sideBar = findViewById(R.id.sidebar_bank_list)
        flLoading = findViewById(R.id.fl_banklist_loading)
        ivBack?.setOnClickListener(View.OnClickListener { finish() })

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvBankList?.layoutManager = manager
        mAdapter = BankListAdapter(mBankList)
        mAdapter?.setOnItemClickListener(object : BankListAdapter.OnItemClickListener {
            override fun onItemClick(bankBean: BankResponseBean.Bank, pos: Int) {
                EventBus.getDefault().post(BankListEvent(bankBean))
                finish()
            }
        })
        rvBankList?.setAdapter(mAdapter)
        sideBar?.setOnSelectIndexItemListener(WaveSideBar.OnSelectIndexItemListener {
            Log.e(
                TAG,
                " test index .. = " + it
            )
        })
    }

    private var dict4Observer: NetObserver<Response<BankResponseBean>>? = null
    private fun getBankList() {
        flLoading?.visibility = View.VISIBLE
        val observable: Observable<Response<BankResponseBean>> =
            NetManager.getApiService2().queryBankList2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        CommonUtils.disposable(dict4Observer)
        dict4Observer = object : NetObserver<Response<BankResponseBean>>() {
            override fun onNext(response: Response<BankResponseBean>) {
                if (isFinishing || isDestroyed){
                    return
                }
                flLoading?.visibility = View.GONE
                val responseBean: BankResponseBean? = response.body
                if (responseBean == null || responseBean.banklist == null) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, " get bank list null .")
                    }
                    return
                }
                Collections.sort<BankResponseBean.Bank>(responseBean.banklist!!,
                    object : Comparator<BankResponseBean.Bank> {
                        override fun compare(
                            bank1: BankResponseBean.Bank,
                            bank2: BankResponseBean.Bank
                        ): Int {
                            if (TextUtils.isEmpty(bank1.bankName)) {
                                return -1
                            }
                            if (TextUtils.isEmpty(bank2.bankName)) {
                                return 1
                            }
                            val c1 : Char = bank1.bankName!![0]
                            val c2 : Char = bank2.bankName!![0]
                            return c1 - c2
                        }
                    })

                ConfigMgr.mBankList.clear()
                ConfigMgr.mBankList.addAll(responseBean.banklist!!)
                mBankList.clear()
                mBankList.addAll(responseBean.banklist!!)
                updateList()
            }

            override fun onException(netException: ResponseException) {
                flLoading?.visibility = View.GONE
                Log.e(TAG, "get bank list = " + netException.msg)
            }
        }
        observable.subscribeWith(dict4Observer)
    }

    fun updateList(){
        mAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        CommonUtils.disposable(dict4Observer)
        super.onDestroy()
    }
}