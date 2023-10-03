package com.chocolate.moudle.scan.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.CameraSdk
import com.chocolate.moudle.scan.R
import com.chocolate.moudle.scan.camera2.CameraActivity2
import com.chocolate.moudle.scan.camera2.BaseUploadFilePresenter
import java.io.File

class UploadFileFragment : Fragment() {

    companion object {
        const val TAG = "UploadFileFragment"
    }

    private var ivResult : AppCompatImageView? = null
    private var ivClose: AppCompatImageView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload_file, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivResult = view.findViewById<AppCompatImageView>(R.id.upload_file_result)
        ivClose = view.findViewById<AppCompatImageView>(R.id.upload_file_result_back)
        progressBar = view.findViewById<ProgressBar>(R.id.upload_file_loading_progress)
        initView()
    }

    private fun initView() {
        if (activity is CameraActivity2) {
            val imagePath = (activity as CameraActivity2).getPath()
            if (context != null && ivResult != null) {
                Log.e("Test", " image path = $imagePath")
                Glide.with(requireContext()).load(imagePath).into(ivResult!!)
            }
            startUploadFile(File(imagePath))
            ivClose?.setOnClickListener(object : OnClickListener {
                override fun onClick(v: View?) {

                }

            })
        }
    }

    //1. MTN  2. Voter's Card  3. selfie
    private fun startUploadFile(file : File) {
        CameraSdk.mPresenter?.startUpload("MTN", file)
    }
}