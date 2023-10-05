package com.chocolate.moudle.scan

import android.annotation.SuppressLint
import android.app.Application
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter

@SuppressLint("StaticFieldLeak")
object CameraSdk {

    var mPresenter : BaseUploadFilePresenter? = null

    var mAppContext : Application? = null

//    // 前往自拍界面
//    const val TYPE_SELF_IE = 1112
//    //
//    const val TYPE_SCAN = 1114

    fun executeScanActivityNext(bitmapPath : String) {
        mObserver?.onScanActivityFinish( bitmapPath)
    }

    interface Observer {
        fun onScanActivityFinish( bitmapPath : String)
    }

    private var mObserver : Observer? = null
    fun setObserver(observer : Observer?) {
        mObserver = observer
    }
}