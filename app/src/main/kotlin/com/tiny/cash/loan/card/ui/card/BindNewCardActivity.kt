package com.tiny.cash.loan.card.ui.card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.BarUtils
import com.tiny.cash.loan.card.event.UpdateBindCardResult
import com.tiny.cash.loan.card.kudicredit.R
import com.tiny.cash.loan.card.ui.base.BaseActivity2
import com.tiny.cash.loan.card.utils.FirebaseUtils
import org.greenrobot.eventbus.EventBus

class BindNewCardActivity : BaseActivity2() {

    companion object {
        const val EXTRA_ADD_BANK_CARD = "extra_add_bank_card"

        const val EXTRA_HAS_RESULT = "extra_add_bank_has_result"
        //添加银行账号
        const val ADD_BANK_ACCOUNT = 111
        //增加银行卡号
        const val ADD_BANK_CARD_NUM = 112

        const val SUCCESS = 113

        const val BIND_BINK_CARD_SUCCESS = 114

        fun launchAddBankAccount(context: Context){
            val intent = Intent(context, BindNewCardActivity::class.java)
            intent.putExtra(EXTRA_ADD_BANK_CARD, ADD_BANK_ACCOUNT)
            context.startActivity(intent)
        }

        fun launchAddBankCard(context: Context){
            val intent = Intent(context, BindNewCardActivity::class.java)
            intent.putExtra(EXTRA_ADD_BANK_CARD, ADD_BANK_CARD_NUM)
            context.startActivity(intent)
        }

        const val BIND_BINK_CARD_FROM_PAY_BANK = 114
        fun launchAddBankCardForResult(context: Activity){
            val intent = Intent(context, BindNewCardActivity::class.java)
            intent.putExtra(EXTRA_ADD_BANK_CARD, ADD_BANK_CARD_NUM)
            intent.putExtra(EXTRA_HAS_RESULT, true)
            context.startActivityForResult(intent, BIND_BINK_CARD_FROM_PAY_BANK)
        }
    }

    private var ivBack :ImageView? = null
    private var tvTitle :TextView? = null
    private var mType : Int = ADD_BANK_CARD_NUM
    private var mHasResult : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.theme_color))
        BarUtils.setStatusBarLightMode(this, false)
        mType = intent.getIntExtra(EXTRA_ADD_BANK_CARD, ADD_BANK_CARD_NUM)
        mHasResult = intent.getBooleanExtra(EXTRA_HAS_RESULT, false)

        setContentView(R.layout.activity_bind_new_card)
        ivBack = findViewById(R.id.iv_bind_new_card_back)
        tvTitle = findViewById(R.id.tv_bind_new_card_title)

        ivBack?.setOnClickListener(View.OnClickListener {
            finish()
        })
        toStepInternal(if (mType == ADD_BANK_CARD_NUM ) ADD_BANK_CARD_NUM
            else ADD_BANK_ACCOUNT)
        if (mType == ADD_BANK_CARD_NUM) {
            FirebaseUtils.logEvent("fireb_band_card")
        }
    }

    fun toStep(step: Int){
        toStepInternal(step)
    }

    private fun toStepInternal(step: Int) {
        if (step == ADD_BANK_ACCOUNT) {
            var addBankFragment = AddBankAccount1Fragment()
            toFragment(addBankFragment)
            tvTitle?.text = resources.getString(R.string.bind_new_card_title1)
        } else if (step == ADD_BANK_CARD_NUM) {
            var addBankNum2Fragment = AddBankNum2Fragment()
            toFragment(addBankNum2Fragment)
            tvTitle?.text = resources.getString(R.string.bind_new_card_title2)
        } else if (step == SUCCESS) {
//            EventBus.getDefault().post(UpdateGetOrderEvent())
            finish()
        } else if (step == BIND_BINK_CARD_SUCCESS){
            EventBus.getDefault().post(UpdateBindCardResult())
            setResult(BIND_BINK_CARD_FROM_PAY_BANK)
            finish()
        }
    }

    override fun getFragmentContainerRes(): Int {
        return R.id.fl_bind_new_card_container
    }
}