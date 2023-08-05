package com.tiny.cash.loan.card

import com.tiny.cash.loan.card.kudicredit.BuildConfig

class Constant {

    companion object {

        fun isAabBuild() : Boolean {
            return if (BuildConfig.DEBUG) false else BuildConfig.IS_AAB_BUILD
        }

        const val CUR_VERSION_CODE = 20089
        const val CUR_VERSION_NAME = "2.2.9"

        const val IS_COLLECT= true
    }
}