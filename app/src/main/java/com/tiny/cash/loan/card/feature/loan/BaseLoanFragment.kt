package com.tiny.cash.loan.card.feature.loan

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.tiny.cash.loan.card.Constant
import com.tiny.cash.loan.card.data.FirebaseData
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail
import com.tiny.cash.loan.card.ui.base.BaseFragment2
import com.tiny.cash.loan.card.ui.pay2.PayActivity2

abstract class BaseLoanFragment : BaseFragment2() {

    private val TAG = "base_loan_fragment"

    var mOrderInfo : LoanOrderDetail? = null

    private var tvDisburseDate: AppCompatTextView? = null
    private var tvDueDate: AppCompatTextView? = null
    private var tvLoanAMount: AppCompatTextView? = null
    private var tvOriginFee: AppCompatTextView? = null
    private var tvRolloverFee: AppCompatTextView? = null
    private var tvOverdueFee: AppCompatTextView? = null
    private var tvTotalAmountDue: AppCompatTextView? = null
    private var llOverdueFee: LinearLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            mOrderInfo = requireArguments().getSerializable("data") as LoanOrderDetail?
        }
        super.onViewCreated(view, savedInstanceState)
        // TODO
//        boolean orderSucess = KvStorage.get(LocalConfig.getNewKey(LocalConfig.LC_ORDERSUCESS), false);
//        if (!orderSucess) {
//            if (LocalConfig.isNewUser())
//                FirebaseLogUtils.Log("af_new_active");
//            else
//                FirebaseLogUtils.Log("af_old_active");
//            KvStorage.put(LocalConfig.getNewKey(LocalConfig.LC_ORDERSUCESS), true);
//        }
        tvDisburseDate = view.findViewById(R.id.tv_loan_process_disburse_date)
        tvDueDate = view.findViewById(R.id.tv_loan_process_due_date)
        tvLoanAMount = view.findViewById(R.id.tv_loan_process_loan_amount)
        tvOriginFee = view.findViewById(R.id.tv_loan_process_loan_origin_fee)
        tvRolloverFee = view.findViewById(R.id.tv_loan_process_loan_fee)
        tvOverdueFee = view.findViewById(R.id.tv_loan_process_loan_overdue_fee)
        llOverdueFee = view.findViewById(R.id.ll_loan_process_loan_overdue_fee)
        tvTotalAmountDue = view.findViewById(R.id.tv_loan_process_loan_total_amount_due)
        bindDataInternal()
    }

    private fun bindDataInternal() {
        if (mOrderInfo == null) {
            return
        }
        if (mOrderInfo?.stageList != null) {
            var stage: LoanOrderDetail.StageListBean? = mOrderInfo?.stageList!![0]
            if (stage != null){
                tvDisburseDate?.text = stage.disburseTime.toString()
                tvDueDate?.text =  stage.repayDate.toString()

                tvLoanAMount?.text = "₦ " + stage.amount.toString()
                tvOriginFee?.text = "₦ " + stage.fee.toString()
                tvRolloverFee?.text = "₦ " + stage.interest.toString()
                if (stage.penalty == null){
                    llOverdueFee?.visibility = View.GONE
                } else {
                    llOverdueFee?.visibility = View.VISIBLE
                    if (tvOverdueFee != null ) {
                        tvOverdueFee?.text = "₦ " + stage.penalty.toString()
                    }
                }
            }
        }
        tvTotalAmountDue?.text = "₦ " + mOrderInfo?.totalAmount.toString()
    }

    //点击还款
    fun clickRepayLoad(){
        if (checkClickFast()){
            return
        }
        if (mOrderInfo == null){
            ToastUtils.showShort("repay load failure .")
            return
        }
        if (TextUtils.isEmpty(mOrderInfo?.orderId)){
            ToastUtils.showShort("repay load orderId == null .")
            return
        }
        if (mOrderInfo?.totalAmount == null){
            ToastUtils.showShort("repay load total amount == null .")
            return
        }
        var amount:String? = null
        if (mOrderInfo?.stageList != null) {
            var stage: LoanOrderDetail.StageListBean? = mOrderInfo?.stageList!![0]
            if (stage != null) {
                amount = stage.amount.toString()
            }
        }
        if (activity == null || requireActivity().isDestroyed || requireActivity().isFinishing) {
            return
        }
        PayActivity2.launchPayActivity(requireActivity(), mOrderInfo?.orderId!!, amount!!)
    }

    fun checkNeedShowLog() : Boolean{
        if (mOrderInfo == null){
            return false
        }
        val orderId : String? = mOrderInfo!!.orderId
        val dataStr = SPUtils.getInstance().getString(Constant.KEY_FIREBASE_DATA)
        if (!TextUtils.isEmpty(dataStr)){
            val firebaseData = GsonUtils.fromJson(dataStr, FirebaseData::class.java)
            if (firebaseData != null){
                if (TextUtils.equals(firebaseData.orderId, orderId)) {
                    if (firebaseData.status == 1){
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}