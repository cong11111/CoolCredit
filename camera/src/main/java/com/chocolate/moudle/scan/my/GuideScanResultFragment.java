package com.chocolate.moudle.scan.my;

import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.chocolate.moudle.scan.base.FaceSaveState;
import com.chocolate.moudle.scan.my.ScanActivity;
import com.chocolate.moudle.scan.R;
public class GuideScanResultFragment extends Fragment {

    public static final String TAG = "GuideScanResultFragment";
    private ImageView ivResult;
    private TextView tvUpload;
    private TextView tvError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivResult = view.findViewById(R.id.scan_result_view);
        tvUpload = view.findViewById(R.id.tv_upload_pic);
        tvError = view.findViewById(R.id.tv_face_scan_error_tips);
        if (getActivity() instanceof ScanActivity) {
            Pair<Bitmap, FaceSaveState> pair = ((ScanActivity) getActivity()).getBitmap();
            if (pair != null && pair.first != null && !pair.first.isRecycled()) {
                tvError.setVisibility(View.GONE);
                ivResult.setImageBitmap(pair.first);
                tvUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO upload pic
                    }
                });
            } else {
                tvError.setVisibility(View.VISIBLE);
            }
        }
    }
}
