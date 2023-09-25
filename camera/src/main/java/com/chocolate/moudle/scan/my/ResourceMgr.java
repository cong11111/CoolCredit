package com.chocolate.moudle.scan.my;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.chocolate.moudle.scan.R;

public class ResourceMgr {
    private Context mContext;
    private Drawable drawableHigh = null;
    private Drawable drawableNone = null;

    public ResourceMgr(Context context){
        mContext = context;
    }


    public Drawable getLeftIconRes(@DetectResult int result) {
        if (mContext == null){
            return null;
        }
        switch (result) {
            case DetectResult.NONE:
                if (drawableNone == null) {
                    drawableNone = ContextCompat.getDrawable(mContext, R.drawable.ic_scanning_failure);
                    drawableNone.setBounds(0, 0, drawableNone.getMinimumWidth(), drawableNone.getMinimumHeight());
                }
                return drawableNone;
        }

        if (drawableHigh == null) {
            drawableHigh = ContextCompat.getDrawable(mContext, R.drawable.ic_scanning_comfire);
            drawableHigh.setBounds(0, 0, drawableHigh.getMinimumWidth(), drawableHigh.getMinimumHeight());
        }
        return drawableHigh;
    }

//    private var facescanHigh: Drawable? = null
//    private var facescanMedium: Drawable? = null
//    private var facescanNone : Drawable? = null
//
//    fun getBgRes(@DetectResult result: Int): Drawable?  {
//        if (mContext == null){
//            return null
//        }
//        when (result) {
//            DetectResult.HIGH -> {
//                if (facescanHigh == null) {
//                    facescanHigh = ContextCompat.getDrawable(mContext!!,R.drawable.bg_face_scan_high)
//                }
//                return facescanHigh
//            }
//            DetectResult.MEDIUM -> {
//                if (facescanMedium == null) {
//                    facescanMedium = ContextCompat.getDrawable(mContext!!,R.drawable.bg_face_scan_medium)
//                }
//                return facescanMedium
//            }
////            DetectResult.NONE -> {
////                return R.drawable.bg_face_scan_none
////            }
//        }
//        if (facescanNone == null) {
//            facescanNone = ContextCompat.getDrawable(mContext!!,R.drawable.bg_face_scan_none)
//        }
//        return facescanNone
//    }

    public void onDestroy(){
        drawableHigh = null;
        drawableNone = null;
//        facescanHigh = null
//        facescanMedium = null
//        facescanNone = null
    }
}
