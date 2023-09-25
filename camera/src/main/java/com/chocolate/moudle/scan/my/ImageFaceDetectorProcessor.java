package com.chocolate.moudle.scan.my;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chocolate.moudle.scan.base.FaceDetectorProcessor;
import com.chocolate.moudle.scan.base.GraphicOverlay;
import com.google.mlkit.vision.face.Face;

import java.util.List;

public class ImageFaceDetectorProcessor extends FaceDetectorProcessor {
    private Context mContext;
    public ImageFaceDetectorProcessor(Context context) {
        super(context);
        mContext = context;
    }

    private boolean mIsShowPoint = false;

    public void setShowPoint(boolean isShowPoint){
        mIsShowPoint =  isShowPoint;
    }

    @Override
    protected void onSuccess(@NonNull List<Face> faces, @NonNull GraphicOverlay graphicOverlay) {
        if (faces.size() != 1){
            Log.e("Test", " size != 1");
            return;
        }
        if (mIsShowPoint) {
            super.onSuccess(faces, graphicOverlay);
        }
        Face face1 = faces.get(0);
        if (mObserver != null){
            mObserver.onSuccess(face1);
        }
    }

    private Observer mObserver;

    public void setObserver(Observer observer){
        mObserver = observer;
    }

    public interface Observer {
        void onSuccess(Face face);

        void onFailure(@NonNull Exception e);
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        super.onFailure(e);
        if (mObserver != null){
            mObserver.onFailure(e);
        }
    }
}
