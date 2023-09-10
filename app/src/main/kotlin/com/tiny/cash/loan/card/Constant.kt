package com.tiny.cash.loan.card

import com.tiny.cash.loan.card.kudicredit.BuildConfig
import com.tiny.cash.loan.card.net.response.data.order.LoanOrderDetail

class Constant {

    companion object {

        fun isAabBuild() : Boolean {
            return if (BuildConfig.DEBUG) false else BuildConfig.IS_AAB_BUILD
        }

        const val IS_COLLECT= !BuildConfig.IS_AAB_BUILD

        var IS_FIRST_APPROVE = false

        var IS_FIRST_APPLY = false

        const val TEST_KEY_NOT_AUTO_LOGIN_EXECUTE = "test_key_not_auto_login_execute"

        const val KEY_FIREBASE_DATA = "key_firebase_data"

        var mToken : String? = null

        var mAccountId : String? = null

        var mMobile : String? = null

        var mLaunchOrderInfo: LoanOrderDetail? = null

        var imei : String? = null

        const val CUR_VERSION_CODE = 30005
        const val CUR_VERSION_NAME = "3.0.5"

//        const val KEY_ACCOUNT_ID = "key_sign_in_account_id"
//
//        const val KEY_TOKEN = "key_sign_in_token"
//
//        const val KEY_MOBILE = "key_sign_in_mobile"

        const val KEY_LOGIN_TIME = "key_sign_in_login_time"

    }
}