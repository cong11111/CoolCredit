package com.tiny.cash.loan.card.identification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.tiny.cash.loan.card.kudicredit.R;

public class IdentityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启一个事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 创建要添加的 Fragment 实例
        BlankFragment fragment = new BlankFragment();


        // 将 Fragment 添加到容器中，并提交事务
        fragmentTransaction.add(R.id.fl_camera_container, fragment, "YourFragmentTag");
        fragmentTransaction.commit();
    }
}