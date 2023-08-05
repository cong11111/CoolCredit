package com.tiny.cash.loan.card.net.server

import com.tiny.cash.loan.card.Constants
import okhttp3.Interceptor

class ApiServerImpl : BaseApiServer {
    private constructor() : super() {}
    private constructor(useRxJava: Boolean) : super(useRxJava) {}

    override fun getBaseUrl(): String {
        return Constants.BASE_URL
    }

    override fun getInterceptor(): Interceptor {
        return ApiAuthInterceptor()
    }

    companion object {
        const val OK = 200 //成功调用
        const val PHONE_HAS_REG = 402 //手机已注册
        const val PHONE_HASNOT_REG = 403 //手机未注册
        const val PHONE_OTHER = 406 //换设备登录
        const val FAILE = 999
        private var mInstance: ApiServerImpl? = null
        private var mSyncInstance: ApiServerImpl? = null
        @JvmStatic
        val instance: ApiServerImpl?
            get() {
                if (mInstance == null) {
                    synchronized(ApiServerImpl::class.java) {
                        if (mInstance == null) {
                            mInstance = ApiServerImpl()
                        }
                    }
                }
                return mInstance
            }
        val syncInstance: ApiServerImpl?
            get() {
                if (mSyncInstance == null) {
                    synchronized(ApiServerImpl::class.java) {
                        if (mSyncInstance == null) {
                            mSyncInstance = ApiServerImpl(false)
                        }
                    }
                }
                return mSyncInstance
            }
    }
}