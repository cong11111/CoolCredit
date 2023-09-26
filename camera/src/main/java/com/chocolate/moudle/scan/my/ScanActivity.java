package com.chocolate.moudle.scan.my;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chocolate.moudle.scan.BuildConfig;
import com.chocolate.moudle.scan.R;
import com.chocolate.moudle.scan.base.FaceSaveState;

import java.io.File;

public class ScanActivity extends AppCompatActivity {

    private static final String KEY_SAVE_PATH = "key_save_bitmap_path";
    private static final String KEY_SAVE_FACE = "key_save_face";

    private int curFragmentIndex = 0;
    private Bitmap bitmap;
    private FaceSaveState mFaceState;
    private String bitmapPath;

    private ImageView ivBack;
    private TextView tvRight;
    private TextView tvTitle;

    public static void showMe(Activity activity) {
        Intent intent = new Intent(activity, ScanActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.td_slide_in_right, R.anim.td_slide_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            curFragmentIndex = savedInstanceState.getInt("index");
            bitmapPath = savedInstanceState.getString(KEY_SAVE_PATH);
            mFaceState = savedInstanceState.getParcelable(KEY_SAVE_FACE);
        }
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_scan);
        ivBack = findViewById(R.id.back_iv);
        tvRight = findViewById(R.id.right_tv);
        tvTitle = findViewById(R.id.title_tv);

        int statusBarH =  BarUtils.getStatusBarHeight();
        ConstraintLayout.LayoutParams backParam = (ConstraintLayout.LayoutParams) ivBack.getLayoutParams();
        if (statusBarH > 0) {
            backParam.topMargin =  statusBarH;
        } else {
            backParam.topMargin = ConvertUtils.dp2px(44);

        }
        ivBack.setLayoutParams(backParam);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                back();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curFragmentIndex++;
                loadFragment();
            }
        });
        loadFragment();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", curFragmentIndex);
        outState.putString(KEY_SAVE_PATH, bitmapPath);
        outState.putParcelable(KEY_SAVE_FACE, mFaceState);
    }

    private void loadFragment() {
        if (curFragmentIndex > 1) {
            finish();
            if (!TextUtils.isEmpty(bitmapPath) && !BuildConfig.DEBUG) {
                FileUtils.delete(new File(bitmapPath));
            }
            return;
        }
        switch (curFragmentIndex){
            case 0:
//                updateFragmentTopPadding((ivBack.getHeight() + ((ConstraintLayout.LayoutParams)ivBack.getLayoutParams()).topMargin));
                toGuideScanningFragment();
                break;
            case 1:
                toScanResultFragment();
                break;
            default:
                break;
        }
    }

    private void toGuideScanningFragment() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CameraScanningFragment.TAG);
        if (fragment == null) {
            fragment = new CameraScanningFragment();
        }
        Bundle bundle = new Bundle();
//        bundle.putBoolean("showInfo", true);
        fragment.setArguments(bundle);
        toFragment(fragment, CameraScanningFragment.TAG);
    }

   public void toScanResultFragment() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(GuideScanResultFragment.TAG);
        if (fragment == null) {
            fragment = new GuideScanResultFragment();
        }
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        toFragment(fragment, GuideScanResultFragment.TAG);
    }

    public void setBitmap(Bitmap bitmap, FaceSaveState face) {
        this.bitmap = bitmap;
        this.mFaceState = face;
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<File>() {
            @Override
            public File doInBackground() throws Throwable {
                if (getCacheDir() == null){
                    return null;
                }
                File parentFile = new File(getCacheDir(), "scan/temp");
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                File saveFile = new File(parentFile, System.currentTimeMillis() + ".png");
                boolean isSuccess = ImageUtils.save(bitmap, saveFile, Bitmap.CompressFormat.PNG, 80, false);
                if (isSuccess) {
                    if (BuildConfig.DEBUG) {
                        Log.e(CameraScanningFragment.TAG, "ScanActivity save bitmap success = " + isSuccess);
                    }
                    saveFile.deleteOnExit();
                    return saveFile;
                }
                return null;
            }

            @Override
            public void onSuccess(File result) {
                if (result != null && result.exists()){
                    bitmapPath = result.getAbsolutePath();
                }
            }
        });
    }

    public Pair<Bitmap, FaceSaveState> getBitmap()  {
        if (bitmap == null || bitmap.isRecycled()) {
            if (!TextUtils.isEmpty(bitmapPath) && FileUtils.isFileExists(bitmapPath)) {
                if (BuildConfig.DEBUG) {
                    Log.e("Test", "ScanActivity load bitmap from save data.");
                }
                return new Pair(BitmapFactory.decodeFile(bitmapPath), mFaceState);
            }
        }
        return new Pair(bitmap, mFaceState);
    }

    private void next() {
//        if (curFragmentIndex == 0) {
//            val fragment = getSupportFragmentManager().findFragmentByTag(Cam.TAG)
//            if (fragment is GuidePrepareFragment) {
//                if (fragment.getPosition() < 3) {
//                    fragment.nextStep()
//                } else {
//                    curFragmentIndex++
//                    loadFragment()
//                }
//            }
//        } else {
            curFragmentIndex++;
            loadFragment();
//        }
    }

    private void backToFirst() {
        finish();
    }

    private void toFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction(); // 开启一个事务
        transaction.setCustomAnimations(
                R.anim.td_slide_in_right,
                R.anim.td_slide_out_left,
                R.anim.td_slide_in_left,
                R.anim.td_slide_out_right
        );

        transaction.addToBackStack(null);
        transaction.replace(R.id.fl_container, fragment, tag);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        finish();
//        if (curFragmentIndex == 0) {
//            val fragment = supportFragmentManager.findFragmentByTag(GuidePrepareFragment.TAG)
//            if (fragment is GuidePrepareFragment) {
//                val position = fragment.getPosition()
//                if (position > 0) {
//                    fragment.previousPosition()
//                } else {
//                    curFragmentIndex--
//                    if (curFragmentIndex < 0) {
//                        finish()
//                        return
//                    }
//                }
//            }
//            return
//        }
//        if (curFragmentIndex == 2) {
//            val fragment = supportFragmentManager.findFragmentByTag(GuideScanResultFragment.TAG)
//            if (fragment is GuideScanResultFragment) {
//                fragment.cancelAnalysis()
//                //跟产品确认，除新用户流程，结果页面可以物理返回，且返回拍照页面
//            }
//        }
//        if (curFragmentIndex == 1) {
//            backToFirst()
//            //拍照页面返回同点击"x"
//            return
//        }
//        curFragmentIndex--
//        if (curFragmentIndex < 0) {
//            finish()
//            return
//        }
//        if (curFragmentIndex == 0) {
//            back_iv.visibility = View.VISIBLE
//            title_tv.visibility = View.VISIBLE
//            right_tv.visibility = View.VISIBLE
//        } else {
//            back_iv.visibility = View.GONE
//            title_tv.visibility = View.GONE
//            right_tv.visibility = View.GONE
//        }
//        FragmentationMagician.popBackStackAllowingStateLoss(supportFragmentManager)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
    }
}
