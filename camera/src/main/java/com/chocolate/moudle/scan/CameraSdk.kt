package com.chocolate.moudle.scan

import android.annotation.SuppressLint
import android.app.Application
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter

@SuppressLint("StaticFieldLeak")
object CameraSdk {

    var mPresenter : BaseUploadFilePresenter? = null

    var mAppContext : Application? = null

}