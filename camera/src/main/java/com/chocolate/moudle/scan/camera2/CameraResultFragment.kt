package com.chocolate.moudle.scan.camera2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chocolate.moudle.scan.R

class CameraResultFragment : Fragment() {

    companion object {
        const val TAG = "CameraPreviewFragment"
    }

    private var ivPreview: AppCompatImageView? = null
    private var ivComfirm: AppCompatImageView? = null
    private var ivCancel: AppCompatImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera_preview, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivPreview = view.findViewById<AppCompatImageView>(R.id.camera_preview_result)
        ivComfirm = view.findViewById<AppCompatImageView>(R.id.iv_camera_preview_comfire)
        ivCancel = view.findViewById<AppCompatImageView>(R.id.iv_camera_preview_cancel)

        context?.let {
            if (activity is CameraActivity2) {
                val imagePath = (activity as CameraActivity2).getPath()
                if (context != null && ivPreview != null) {
                    Log.e("Test", " image path = $imagePath")
                    Glide.with(requireContext()).load(imagePath).into(ivPreview!!)
                }
            }
        }
        ivComfirm?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                if (activity is CameraActivity2) {
                    (activity as CameraActivity2).finishResult()
                }
            }

        })
        ivCancel?.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                if (activity is CameraActivity2) {
                    (activity as CameraActivity2).restorePic(null)
                    (activity as CameraActivity2).toCameraFragment(false)
                }
            }

        })
    }
}