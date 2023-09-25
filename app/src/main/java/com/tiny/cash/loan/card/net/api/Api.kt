package com.tiny.cash.loan.card.net.api

import com.tiny.cash.loan.card.kudicredit.BuildConfig


class Api {

    companion object {
        private val USE_TEST_HOST_FLAG : Boolean = true

//        private val HOST = if (BuildConfig.DEBUG || USE_TEST_HOST_FLAG) "http://srv.chucard.com" else  "https://srv.creditng.com"

        val GET_POLICY: String = "https://www.kudicredit.com/privacy.html"

        val GET_TERMS: String = "https://www.kudicredit.com/terms.html"

        val GET_ALL: String = "https://www.kudicredit.com/all.html"

    }

}