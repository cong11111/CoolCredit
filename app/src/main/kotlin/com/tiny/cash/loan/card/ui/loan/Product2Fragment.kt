//package com.tiny.cash.loan.card.ui.loan
//
//import android.Manifest
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.util.Pair
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.appcompat.widget.AppCompatTextView
//import androidx.core.widget.NestedScrollView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.blankj.utilcode.constant.PermissionConstants
//import com.blankj.utilcode.util.*
//import com.tiny.cash.loan.card.ui.loan.adapter.LoanApplyAdapter
//import com.tiny.cash.loan.card.Constant
//import com.tiny.cash.loan.card.KudiCreditApp
//import com.tiny.cash.loan.card.bean.loan.ProductResponseBean
//import com.tiny.cash.loan.card.bean.loan.TrialResponseBean
//import com.tiny.cash.loan.card.collect.BaseCollectDataMgr
//import com.tiny.cash.loan.card.collect.CollectDataMgr
//import com.tiny.cash.loan.card.collect.CollectHardwareMgr
//import com.tiny.cash.loan.card.collect.item.CollectAppInfoMgr
//import com.tiny.cash.loan.card.collect.item.CollectSmsMgr
//import com.tiny.cash.loan.card.feature.loan.BaseLoanFragment
//import com.tiny.cash.loan.card.kudicredit.BuildConfig
//import com.tiny.cash.loan.card.kudicredit.R
//import com.tiny.cash.loan.card.net.NetManager
//import com.tiny.cash.loan.card.net.NetObserver
//import com.tiny.cash.loan.card.net.ResponseException
//import com.tiny.cash.loan.card.net.response.Response
//import com.tiny.cash.loan.card.net.response.data.order.ProductList
//import com.tiny.cash.loan.card.utils.CommonUtils
//import com.tiny.cash.loan.card.utils.JumpPermissionUtils
//import com.tiny.cash.loan.card.utils.KvStorage
//import com.tiny.cash.loan.card.utils.LocalConfig
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.schedulers.Schedulers
//import org.greenrobot.eventbus.EventBus
//import org.greenrobot.eventbus.Subscribe
//import org.greenrobot.eventbus.ThreadMode
//import org.json.JSONException
//import org.json.JSONObject
//import java.util.*
//
///**
// * 申请贷款的界面
// */
//class Product2Fragment : BaseLoanFragment() {
//
//    private val TAG = "LoanApplyFragment"
//
//    private var spinnerAmount: Spinner? = null
//    private var spinnerTerm: Spinner? = null
//    private var tvTitle: AppCompatTextView? = null
//    private var rvContent: RecyclerView? = null
//    private var flCommit: FrameLayout? = null
//    private var flLoading: FrameLayout? = null
//
//    private var mAmountList: ArrayList<Pair<String, ArrayList<ProductResponseBean.Product>>> =
//        ArrayList()
//
//    private var mTrialList: ArrayList<TrialResponseBean.Trial> = ArrayList<TrialResponseBean.Trial>()
//    private var mAdapter: LoanApplyAdapter? = null
//
//    private var mTrialBean: TrialResponseBean? = null
//    private var mProduct: ProductResponseBean.Product? = null
//    private var scrollView: NestedScrollView? = null
//    private var isMarketing : Boolean = false
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        var view = inflater.inflate(R.layout.fragment_loan_apply, container, false)
//        return view
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        ThreadUtils.executeByCached(object : ThreadUtils.SimpleTask<Exception?>() {
//            @Throws(Throwable::class)
//            override fun doInBackground(): Exception? {
//                //缓存一下applist
//                CollectAppInfoMgr.sInstance
//                return null
//            }
//
//
//            override fun onSuccess(result: Exception?) {
//
//            }
//
//        })
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        spinnerAmount = view.findViewById(R.id.spinner_loan_apply_amount)
//        spinnerTerm = view.findViewById(R.id.spinner_loan_apply_term)
//        tvTitle = view.findViewById(R.id.tv_loan_apply_title)
//        rvContent = view.findViewById(R.id.rv_loan_apply_container)
//        flCommit = view.findViewById(R.id.fl_loan_apply_commit)
//        flLoading = view.findViewById(R.id.fl_apply_load_loading)
//        scrollView = view.findViewById(R.id.sv_load_apply)
//
//        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        rvContent?.layoutManager = manager
//        mAdapter = LoanApplyAdapter(mTrialList)
//        rvContent?.adapter = mAdapter
//
//        if (mAmountList.size == 0) {
//            getProducts()
//        } else {
//            updateSpinner()
//        }
//
//        flCommit?.setOnClickListener(View.OnClickListener {
//            if (checkClickFast()){
//                return@OnClickListener
//            }
//            requestPermission()
//        })
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
//    }
//
//    private var productObserver: NetObserver<Response<ProductResponseBean>>? = null
//    private fun getProducts() {
//        flLoading?.visibility = View.VISIBLE
//
//        val accountId = KvStorage.get(LocalConfig.LC_ACCOUNTID, "")
//        val observable: Observable<Response<ProductResponseBean>> = NetManager.getApiService().productList2(accountId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//        CommonUtils.disposable(productObserver)
//        productObserver = object : NetObserver<Response<ProductResponseBean>>() {
//            override fun onNext(response: Response<ProductResponseBean>) {
//                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
//                    return
//                }
//                if (isRemoving || isDetached) {
//                    return
//                }
//                flLoading?.visibility = View.GONE
//                val productBean: ProductResponseBean? = response.body
//                if (productBean == null || productBean.products?.isEmpty() == true) {
//                    return
//                }
//                if (productBean.products != null) {
//                    Collections.sort(productBean.products!!, object : Comparator<ProductResponseBean.Product> {
//                        override fun compare(
//                            o1: ProductResponseBean.Product?,
//                            o2: ProductResponseBean.Product?
//                        ): Int {
//                            try {
//                                if (o1 != null && o2 != null) {
//                                    val amount1  = o1.amount?.toDouble()
//                                    val amount2 =  o2.amount?.toDouble()
//                                    if (amount1 != null && amount2 != null){
//                                        return (amount2 - amount1).toInt()
//                                    }
//                                }
//                            }catch (e : Exception){
//                                Log.e(TAG, "parse exception ", e)
//                            }
//                            return 0
//                        }
//
//                    })
//                    isMarketing = productBean.isMarketing()
//                    updateProductList(productBean.products!!)
//                }
//            }
//
//            override fun onException(netException: ResponseException) {
//                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
//                    return
//                }
//                flLoading?.visibility = View.GONE
//                if (BuildConfig.DEBUG) {
//                    Log.e(TAG, " product list error ." + netException.toString())
//                }
//                ToastUtils.showShort("product list error")
//            }
//        }
//        observable.subscribeWith(productObserver)
//    }
//
//    private fun updateProductList(products: List<ProductResponseBean.Product>) {
//        mAmountList.clear()
//        for (i in 0 until products.size) {
//            val product = products[i]
//            var list: ArrayList<ProductResponseBean.Product>? = null
//            for (j in 0 until mAmountList.size) {
//                var item: Pair<String, ArrayList<ProductResponseBean.Product>> = mAmountList[j]
//                if (item != null && (TextUtils.equals(item.first, product.amount))) {
//                    list = item.second
//                    break
//                }
//            }
//            if (list == null) {
//                list = ArrayList<ProductResponseBean.Product>()
//                mAmountList.add(Pair(product.amount, list))
//            }
//            list.add(product)
//        }
//        updateSpinner()
//    }
//
//    private fun updateSpinner() {
//        if (context == null){
//            return
//        }
//        val mItem1s = arrayOfNulls<String>(mAmountList.size)
//        for (i in 0 until mAmountList.size) {
//            mItem1s[i] = "₦ " + mAmountList[i].first + " "
//        }
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mItem1s)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        adapter.setNotifyOnChange(true)
//        spinnerAmount?.adapter = adapter
//        spinnerAmount?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                adapterView: AdapterView<*>?,
//                view: View,
//                i: Int,
//                l: Long
//            ) {
//                selectPos(i)
////                Log.e(TAG, "select pos 1 = " + i)
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        })
//
//    }
//
//    private fun selectPos(pos: Int) {
//        var pair: Pair<String, ArrayList<ProductResponseBean.Product>> = mAmountList[pos]
//        var products: ArrayList<ProductResponseBean.Product> = pair.second
//
//        val mItem2s = arrayOfNulls<String>(products.size)
//        for (i in 0 until products.size) {
//            mItem2s[i] = products[i].period + " days  "
//        }
//        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mItem2s)
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        adapter2.setNotifyOnChange(true)
//        spinnerTerm?.adapter = adapter2
//        spinnerTerm?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                adapterView: AdapterView<*>?,
//                view: View,
//                i: Int,
//                l: Long
//            ) {
//                var product = products[i]
//                tvTitle?.text = product.prodName
//                mProduct = product
//                Log.e(TAG, "select pos 2 = " + i)
//                if (!TextUtils.isEmpty(product.prodId) && !TextUtils.isEmpty(product.amount)){
//                    requestLoanTrial(product.prodId!!, product.amount!!)
//                }
//
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
//        })
//    }
//
//    private var loanTrialObserver: NetObserver<Response<TrialResponseBean>>? = null
//    private fun requestLoanTrial(proId : String , loanAmount : String) {
//        flLoading?.visibility = View.VISIBLE
//
//        val observable: Observable<Response<TrialResponseBean>> = NetManager.getApiService().loanTrial2(proId, loanAmount)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//        CommonUtils.disposable(loanTrialObserver)
//        loanTrialObserver = object : NetObserver<Response<TrialResponseBean>>() {
//            override fun onNext(response: Response<TrialResponseBean>) {
//                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
//                    return
//                }
//                flLoading?.visibility = View.GONE
//                val trialBean: TrialResponseBean? = response.body
//                if (trialBean == null || trialBean.trial?.size == 0) {
//                    return
//                }
//                mTrialBean = trialBean
//                mTrialList.clear()
//                mTrialList.addAll(trialBean.trial!!)
//                mAdapter?.notifyDataSetChanged()
//            }
//
//            override fun onException(netException: ResponseException) {
//                if (activity?.isFinishing == true || activity?.isDestroyed == true) {
//                    return
//                }
//                flLoading?.visibility = View.GONE
//                if (BuildConfig.DEBUG) {
//                    Log.e(TAG, " product list error ." + netException.msg)
//                }
//                ToastUtils.showShort("product list error")
//            }
//        }
//        observable.subscribeWith(loanTrialObserver)
//    }
//
//    private fun requestPermission() {
//        val hasPermission = PermissionUtils.isGranted(
////            PermissionConstants.LOCATION,
//            PermissionConstants.SMS,
////            PermissionConstants.CONTACTS,
////            PermissionConstants.STORAGE,
//        )
////        val hasPermissionCallLog = PermissionUtils.isGranted(Manifest.permission.READ_CALL_LOG)
////        val hasPermissionReadPhoneState =
////            PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE)
//
//        //        if (false && hasPermission) {
//        if (hasPermission ) {
//            executeGetOrderId()
//        } else {
//            requestPermissionInternal()
//        }
//    }
//
//    private fun requestPermissionInternal() {
//        val dialog = RequestPermissionDialog(requireContext())
//        dialog.setOnItemClickListener(object : RequestPermissionDialog.OnItemClickListener() {
//            override fun onClickAgree() {
//                PermissionUtils.permission(
////                    Manifest.permission.READ_CALL_LOG,
////                    Manifest.permission.READ_CONTACTS,
//                    Manifest.permission.READ_SMS,
////                    Manifest.permission.ACCESS_COARSE_LOCATION,
////                    Manifest.permission.ACCESS_FINE_LOCATION,
////                    Manifest.permission.READ_PHONE_STATE,
////                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ).callback(object : PermissionUtils.SimpleCallback {
//                    override fun onGranted() {
//                        executeGetOrderId()
//                    }
//
//                    override fun onDenied() {
//                        Toast.makeText(
//                            KudiCreditApp.instance?.applicationContext,
//                            "please allow permission for apply loan.", Toast.LENGTH_SHORT).show()
//                        try {
//                            JumpPermissionUtils.goToSetting(activity)
//                        } catch (e : Exception) {
//
//                        }
//                    }
//                }).request()
//            }
//        })
//        dialog.show()
//    }
//
//    private fun executeGetOrderId(){
//        if (TextUtils.equals(mProduct?.usable, "false") ){
//            ToastUtils.showShort(" Product usable can not use")
//            return
//        }
//        getOrderId()
//    }
//
//    private fun getOrderId(){
//        flLoading?.visibility = View.VISIBLE
//        val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//        try {
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        OkGo.post<String>(Api.GET_ORDER_ID).tag(TAG)
//            .upJson(jsonObject)
//            .execute(object : StringCallback() {
//                override fun onSuccess(response: Response<String>) {
//                    if (isDestroy()){
//                        return
//                    }
//                    flLoading?.visibility = View.GONE
//                    val checkLoanBean: CheckLoanResponseBean? =
//                        checkResponseSuccess(response, CheckLoanResponseBean::class.java)
//                    if (checkLoanBean == null) {
//                        return
//                    }
//                    if (Constant.IS_FIRST_APPROVE) {
//                        FirebaseUtils.logEvent("fireb_apply" )
//                    }
//                    FirebaseUtils.logEvent("fireb_apply_all")
//                    if (checkLoanBean.hasProfile != true || checkLoanBean.hasContact != true
//                        || checkLoanBean.hasOther != true || checkLoanBean.bvnChecked != true
//                    ) {
//                        var intent: Intent = Intent(context, AddProfileActivity::class.java)
//                        context?.startActivity(intent)
//                        return
//                    }
//                    // TODO
//                    if (checkLoanBean.accountChecked != true){
//                        BindNewCardActivity.launchAddBankAccount(context!!)
//                        return
//                    }
//                    if (TextUtils.isEmpty(checkLoanBean.orderId) ||
//                        TextUtils.equals(checkLoanBean.orderId, "-1")) {
//                        ToastUtils.showShort("need correct loan apply orderId " + checkLoanBean.orderId)
//                        return
//                    }
//                    flLoading?.visibility = View.VISIBLE
//                    CollectHardwareMgr.sInstance.collectHardware(activity,null)
//                    CollectDataMgr.sInstance.collectAuthData(checkLoanBean.orderId!!,
//                        object : BaseCollectDataMgr.Observer {
//                            override fun success(response: Response<String>?) {
//                                flLoading?.visibility = View.GONE
//                                showTrialDialog(checkLoanBean.orderId!!)
//                            }
//
//                            override fun failure(errorMsg: String?) {
//
//                                if (BuildConfig.DEBUG) {
//                                    Log.e(TAG, "failure = " + errorMsg)
//                                }
//                                CollectSmsMgr.sInstance.setHasFailure()
//                                if (isDestroy()){
//                                    return
//                                }
//                                flLoading?.visibility = View.GONE
//                                ToastUtils.showShort("upload auth information failure." + errorMsg)
//                            }
//                        })
//                }
//
//                override fun onError(response: Response<String>) {
//                    super.onError(response)
//                   if (isDestroy()){
//                       return
//                   }
//                    flLoading?.visibility = View.GONE
//                    if (BuildConfig.DEBUG) {
//                        Log.e(TAG, " product list error ." + response.body())
//                    }
//                    ToastUtils.showShort("product list error")
//                }
//            })
//    }
//
//    private var trialDialog : ProductTrialDialog? = null
//    private fun showTrialDialog(orderId : String){
//        if (isDestroy()){
//            return
//        }
//        if (activity is BaseActivity){
//           if ( (activity as BaseActivity).isDestroyed){
//               return
//           }
//        }
//        if (trialDialog?.isShowing == true){
//            trialDialog?.dismiss()
//        }
//        trialDialog =  ProductTrialDialog(requireContext(), mTrialBean!!)
//        trialDialog?.setOnDialogClickListener(object : ProductTrialDialog.OnDialogClickListener() {
//            override fun onClickAgree() {
//                if (isDestroy()){
//                    return
//                }
//                if (isMarketing) {
//                    if (trialDialog?.isShowing == true){
//                        trialDialog?.dismiss()
//                    }
//                    ToastUtils.showShort("is market order, please refresh and reload.")
//                    getProducts()
//                    return
//                }
//                applyLoad(orderId, trialDialog)
//            }
//        })
//        trialDialog?.show()
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
//    fun onEvent(event: UpdateGetOrderEvent) {
//        executeGetOrderId()
//    }
//
//    private fun applyLoad(orderId : String, trialDialog: ProductTrialDialog?) {
//        trialDialog?.setAgreeEnable(false)
//        val jsonObject: JSONObject = BuildRequestJsonUtils.buildRequestJson()
//        try {
//            jsonObject.put("accountId", Constant.mAccountId)
//            jsonObject.put("orderId", orderId)  //订单ID
//            jsonObject.put("prodId", mProduct?.prodId)   //产品ID
//            jsonObject.put("prodName", mProduct?.prodName) //产品名称
//            jsonObject.put("amount", mProduct?.amount)   //申请金额
//            jsonObject.put("period", mProduct?.period)   //申请产品期限	91天
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        OkGo.post<String>(Api.LOAD_APPLY).tag(TAG)
//            .upJson(jsonObject)
//            .execute(object : StringCallback() {
//                override fun onSuccess(response: Response<String>) {
//                    if (isDestroy()){
//                        return
//                    }
//                    trialDialog?.setAgreeEnable(true)
//                    val applyLoadResponse: ApplyLoadResponse? =
//                        checkResponseSuccess(response, ApplyLoadResponse::class.java)
//                    if (applyLoadResponse == null ) {
//                        return
//                    }
//                    if (!TextUtils.equals(applyLoadResponse.status, "1")){
//                        ToastUtils.showShort("apply loan failure.")
//                        return
//                    }
//                    if (trialDialog != null && trialDialog.isShowing){
//                        trialDialog.dismiss()
//                    }
//                    if (Constant.IS_FIRST_APPROVE) {
//                        FirebaseUtils.logEvent( "fireb_apply_confirm")
//                    }
//                    FirebaseUtils.logEvent( "fireb_apply_confirm_all")
//                    var data = FirebaseData()
//                    data.orderId = orderId
//                    data.status = 1
//                    SPUtils.getInstance().put(Constant.KEY_FIREBASE_DATA, GsonUtils.toJson(data))
//                    ToastUtils.showShort("apply load success")
//                    EventBus.getDefault().post(UpdateLoanEvent())
//                }
//
//                override fun onError(response: Response<String>) {
//                    super.onError(response)
//                    if (isDestroy()){
//                        return
//                    }
//                    if (BuildConfig.DEBUG) {
//                        Log.e(TAG, " apply load error ." + response.body())
//                    }
//                    trialDialog?.setAgreeEnable(true)
//                    ToastUtils.showShort("apply load error")
//                }
//            })
//    }
//
//    private fun checkLoanApplyState(){
//
//    }
//
//    override fun onDestroy() {
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
//        if (trialDialog?.isShowing == true){
//            trialDialog?.dismiss()
//        }
//        CollectDataMgr.sInstance.onDestroy()
//        super.onDestroy()
//    }
//}