package com.chocolate.moudle.scan.base;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FaceSaveState implements Parcelable {

    private List<PointF> facePoints;
    private List<PointF> leftCheekPoints;
    private List<PointF> rightCheekPoints;
    private List<PointF> noseBridgePoints;
    private List<PointF> upperLipTopPoints;
    private List<PointF> lowerLipBottomPoints;
    private List<PointF> leftEyePoints;
    private List<PointF> rightEyePoint;
    private Rect rect;
    // 前置摄像头情况下, 拍摄出来的照片是一个反转过的, 而给analysis逻辑的是一个正的图片
    private boolean isImageFlipped = false;

    public FaceSaveState(){

    }

    protected FaceSaveState(Parcel in) {
        facePoints = in.createTypedArrayList(PointF.CREATOR);
        leftCheekPoints = in.createTypedArrayList(PointF.CREATOR);
        rightCheekPoints = in.createTypedArrayList(PointF.CREATOR);
        noseBridgePoints = in.createTypedArrayList(PointF.CREATOR);
        upperLipTopPoints = in.createTypedArrayList(PointF.CREATOR);
        lowerLipBottomPoints = in.createTypedArrayList(PointF.CREATOR);
        leftEyePoints = in.createTypedArrayList(PointF.CREATOR);
        rightEyePoint = in.createTypedArrayList(PointF.CREATOR);
        rect = in.readParcelable(Rect.class.getClassLoader());
        isImageFlipped = in.readByte() != 0;
    }

    public static final Creator<FaceSaveState> CREATOR = new Creator<FaceSaveState>() {
        @Override
        public FaceSaveState createFromParcel(Parcel in) {
            return new FaceSaveState(in);
        }

        @Override
        public FaceSaveState[] newArray(int size) {
            return new FaceSaveState[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(facePoints);
        dest.writeTypedList(leftCheekPoints);
        dest.writeTypedList(rightCheekPoints);
        dest.writeTypedList(noseBridgePoints);
        dest.writeTypedList(upperLipTopPoints);
        dest.writeTypedList(lowerLipBottomPoints);
        dest.writeTypedList(leftEyePoints);
        dest.writeTypedList(rightEyePoint);
        dest.writeParcelable(rect, flags);
        dest.writeByte((byte) (isImageFlipped ? 1 : 0));
    }


    public List<PointF> getFacePoints() {
        return facePoints;
    }

    public void setFacePoints(List<PointF> facePoints) {
        this.facePoints = facePoints;
    }

    public List<PointF> getLeftCheekPoints() {
        return leftCheekPoints;
    }

    public void setLeftCheekPoints(List<PointF> leftCheekPoints) {
        this.leftCheekPoints = leftCheekPoints;
    }

    public List<PointF> getRightCheekPoints() {
        return rightCheekPoints;
    }

    public void setRightCheekPoints(List<PointF> rightCheekPoints) {
        this.rightCheekPoints = rightCheekPoints;
    }

    public List<PointF> getNoseBridgePoints() {
        return noseBridgePoints;
    }

    public void setNoseBridgePoints(List<PointF> noseBridgePoints) {
        this.noseBridgePoints = noseBridgePoints;
    }

    public List<PointF> getUpperLipTopPoints() {
        return upperLipTopPoints;
    }

    public void setUpperLipTopPoints(List<PointF> upperLipTopPoints) {
        this.upperLipTopPoints = upperLipTopPoints;
    }

    public List<PointF> getLowerLipBottomPoints() {
        return lowerLipBottomPoints;
    }

    public void setLowerLipBottomPoints(List<PointF> lowerLipBottomPoints) {
        this.lowerLipBottomPoints = lowerLipBottomPoints;
    }

    public List<PointF> getLeftEyePoints() {
        return leftEyePoints;
    }

    public void setLeftEyePoints(List<PointF> leftEyePoints) {
        this.leftEyePoints = leftEyePoints;
    }

    public List<PointF> getRightEyePoint() {
        return rightEyePoint;
    }

    public void setRightEyePoint(List<PointF> rightEyePoint) {
        this.rightEyePoint = rightEyePoint;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public boolean isImageFlipped() {
        return isImageFlipped;
    }

    public void setImageFlipped(boolean imageFlipped) {
        isImageFlipped = imageFlipped;
    }
}
