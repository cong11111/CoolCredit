package com.tiny.cash.loan.card.ui.base

import androidx.annotation.IdRes
import com.tiny.cash.loan.card.base.BaseActivity

open class BaseActivity2 : BaseActivity() {

    fun toFragment(fragment: BaseFragment2?) {
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction() // 开启一个事务
            transaction.replace(getFragmentContainerRes(), fragment)
            transaction.commitAllowingStateLoss()
        }
    }

    @IdRes
    protected open fun getFragmentContainerRes(): Int {
//        return R.id.fl_sign_up_container
        return -1
    }
}