package com.chocolate.moudle.scan.my;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.chocolate.moudle.scan.R;

public class GameActivity extends AppCompatActivity {

    private int curFragmentIndex = 0;

    private ImageView ivBack;
    private TextView tvRight;
    private TextView tvTitle;

    public static void showMe(Activity activity) {
        Intent intent = new Intent(activity, GameActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.td_slide_in_right, R.anim.td_slide_out_left);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            curFragmentIndex = savedInstanceState.getInt("index");
        }
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        setContentView(R.layout.activity_game);
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
//
//    var overlayTop: Int = 0
//    var overlayHeight: Float = -1f


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", curFragmentIndex);
    }

    private void loadFragment() {
        if (curFragmentIndex > 1) {
            finish();

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
//                toCameraPrepareFragment()
                break;
        }
    }

//    private fun toCameraPrepareFragment() {
//        ivBack.visibility = View.VISIBLE
//        title_tv.visibility = View.VISIBLE
//        right_tv.visibility = View.VISIBLE
//        right_tv.text = getString(R.string.skip)
//        var fragment = supportFragmentManager.findFragmentByTag(GuidePrepareFragment.TAG)
//        if (fragment == null) {
//            fragment = GuidePrepareFragment()
//        }
//        val bundle = Bundle()
//        bundle.putBoolean("showNext", true)
//        bundle.putInt("currentPosition", curFragmentIndex)
//        fragment.arguments = bundle
//        toFragment(fragment, GuidePrepareFragment.TAG)
//    }

    private void toGuideScanningFragment() {
        ivBack.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvRight.setVisibility(View.GONE);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CameraScanningFragment.TAG);
        if (fragment == null) {
            fragment = new CameraScanningFragment2();
        }
        Bundle bundle = new Bundle();
//        bundle.putBoolean("showInfo", true);
        fragment.setArguments(bundle);
        toFragment(fragment, CameraScanningFragment2.TAG);
    }

    private void toScanResultFragment() {
//        back_iv.visibility = View.GONE
//        title_tv.visibility = View.GONE
//        right_tv.visibility = View.GONE
//        var fragment = supportFragmentManager.findFragmentByTag(GuideScanResultFragment.TAG)
//        if (fragment == null) {
//            fragment = GuideScanResultFragment()
//        } else if (fragment is GuideScanResultFragment) {
//            fragment.resetData()
//        }
//        val bundle = Bundle()
//        bundle.putInt("overlayTop", overlayTop)
//        bundle.putFloat("overlayHeight", overlayHeight)
//        fragment.arguments = bundle
//        toFragment(fragment, GuideScanResultFragment.TAG)
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
    public void onBackPressed() {
        finish();
    }


}
