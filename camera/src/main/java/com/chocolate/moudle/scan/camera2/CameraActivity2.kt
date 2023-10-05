package com.chocolate.moudle.scan.camera2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.chocolate.moudle.scan.R
import com.chocolate.moudle.scan.my.UploadFileFragment

class CameraActivity2 : AppCompatActivity() {

    companion object {
        const val TYPE_MIN = 101
        const val TYPE_VOTER_CARD = 102

        const val RESULT_CAMERA_CODE = 1112
        const val KEY_RESULT_CAMERA_PATH = "key_result_camera_path"
        const val KEY_RESULT_CAMERA_TYPE = "key_result_camera_type"

        fun showMe(activity: Activity, type : Int) {
            val intent = Intent(activity, CameraActivity2::class.java)
            intent.putExtra(KEY_RESULT_CAMERA_TYPE, type)
            activity.startActivityForResult(intent, RESULT_CAMERA_CODE)
//            activity.overridePendingTransition(R.anim.td_slide_in_right, R.anim.td_slide_out_left)
        }

    }

    private var mType : Int = TYPE_MIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this@CameraActivity2, android.graphics.Color.TRANSPARENT)
        setContentView(R.layout.activity_camera2)
        mType = intent.getIntExtra(KEY_RESULT_CAMERA_TYPE, TYPE_MIN)
        toCameraFragment()
    }

    fun toCameraFragment(needAnim: Boolean = true) {
        var fragment = supportFragmentManager.findFragmentByTag(Camera2Fragment.TAG)
        if (fragment == null) {
            fragment = Camera2Fragment()
        }
        val bundle = Bundle()
        fragment.arguments = bundle
        toFragment(fragment, Camera2Fragment.TAG, needAnim)
    }

    private var mPath : String? = null
    fun restorePic(path : String?) {
        mPath = path
    }
    fun toUploadPicFragment() {
        var fragment = supportFragmentManager.findFragmentByTag(UploadFileFragment.TAG)
        if (fragment == null) {
            fragment = UploadFileFragment()
        }
        val bundle = Bundle()
        fragment.arguments = bundle
        toFragment(fragment, UploadFileFragment.TAG)
    }

    fun toCameraResultFragment(){
        var fragment = supportFragmentManager.findFragmentByTag(CameraResultFragment.TAG)
        if (fragment == null) {
            fragment = CameraResultFragment()
        }
        val bundle = Bundle()
        fragment.arguments = bundle
        toFragment(fragment, CameraResultFragment.TAG)
    }

    fun getPath() : String? {
        return mPath
    }

    fun finishResult() {
        val intent = Intent()
        intent.putExtra(KEY_RESULT_CAMERA_PATH, mPath)
        intent.putExtra(KEY_RESULT_CAMERA_TYPE, mType)
        setResult(RESULT_CAMERA_CODE, intent)
        finish()
    }

    private fun toFragment(fragment: Fragment, tag: String, needAnim : Boolean = true) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction() // 开启一个事务
        if (needAnim) {
            transaction.setCustomAnimations(
                R.anim.td_slide_in_right,
                R.anim.td_slide_out_left,
                R.anim.td_slide_in_left,
                R.anim.td_slide_out_right
            )
        }
        transaction.replace(R.id.fl_camera_2, fragment, tag)
        transaction.commitAllowingStateLoss()
    }

}