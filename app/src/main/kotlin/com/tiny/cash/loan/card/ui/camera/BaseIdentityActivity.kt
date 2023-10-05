package com.tiny.cash.loan.card.ui.camera

import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import com.tiny.cash.loan.card.UploadFilePresenter
import com.tiny.cash.loan.card.ui.base.BaseActivity2
import java.io.File

open class BaseIdentityActivity : BaseActivity2() {

    var mPresenter : UploadFilePresenter = UploadFilePresenter()

    //1. MTN  2. Voter's Card  3. selfie
    fun startUploadProgress(imageType : String, file : File){
        mPresenter.startUpload(imageType, file, object : BaseUploadFilePresenter.UploadObserver {
            override fun onSuccess() {

            }

            override fun onProgress(progress: Int) {

            }

            override fun onFailure(errorDesc: String, errorMsg: String) {

            }

        })
    }


}