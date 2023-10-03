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

        fun showMe(activity: Activity) {
            val intent = Intent(activity, CameraActivity2::class.java)
            activity.startActivity(intent)
//            activity.overridePendingTransition(R.anim.td_slide_in_right, R.anim.td_slide_out_left)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarColor(this@CameraActivity2, android.graphics.Color.TRANSPARENT)
        setContentView(R.layout.activity_camera2)
        toCameraFragment()
    }

    private fun toCameraFragment() {
        var fragment = supportFragmentManager.findFragmentByTag(Camera2Fragment.TAG)
        if (fragment == null) {
            fragment = Camera2Fragment()
        }
        val bundle = Bundle()
        fragment.arguments = bundle
        toFragment(fragment, Camera2Fragment.TAG)
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

    fun getPath() : String? {
        return mPath
    }

    private fun toFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction() // 开启一个事务
        transaction.setCustomAnimations(
            R.anim.td_slide_in_right,
            R.anim.td_slide_out_left,
            R.anim.td_slide_in_left,
            R.anim.td_slide_out_right
        )
        transaction.replace(R.id.fl_camera_2, fragment, tag)
        transaction.commitAllowingStateLoss()
    }

}