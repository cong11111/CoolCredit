package com.tiny.cash.loan.card.feature.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseFragment;
import com.tiny.cash.loan.card.kudicredit.databinding.FragmentCallUsBinding;
import com.tiny.cash.loan.card.utils.KvStorage;
import com.tiny.cash.loan.card.utils.LocalConfig;
import com.tiny.cash.loan.card.utils.ui.ToastManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class CallUsFragment extends BaseFragment {
    private FragmentCallUsBinding mBinding;
    private String mEmail;

    public static CallUsFragment newInstance() {
        Bundle args = new Bundle();
        CallUsFragment fragment = new CallUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_us, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        String mPhone = KvStorage.get(LocalConfig.LC_PHONE, "");
        String mWhatApp = KvStorage.get(LocalConfig.LC_WHATSAPP, "");
        String mWhatApp1 = KvStorage.get(LocalConfig.LC_WHATSAPP1, "");
        Log.d("TAG", "initView: "+mPhone);
        mEmail = KvStorage.get(LocalConfig.LC_EMAIL, "");
        String[] split = mPhone.split("\n");

            mBinding.tvCallUs.setText(split[0]);

        if (split.length == 2){
            mBinding.llCallUs1.setVisibility(View.VISIBLE);
            mBinding.tvCallUs1.setText(split[1]);
        }
        if (split.length == 3){
            mBinding.llCallUs1.setVisibility(View.VISIBLE);
            mBinding.llCallUs2.setVisibility(View.VISIBLE);
            mBinding.tvCallUs1.setText(split[1]);
            mBinding.tvCallUs2.setText(split[2]);
        }
        if (split.length == 4){
            mBinding.llCallUs1.setVisibility(View.VISIBLE);
            mBinding.llCallUs2.setVisibility(View.VISIBLE);
            mBinding.llCallUs3.setVisibility(View.VISIBLE);
            mBinding.tvCallUs1.setText(split[1]);
            mBinding.tvCallUs2.setText(split[2]);
            mBinding.tvCallUs3.setText(split[3]);
        }
        if (split.length == 5){
            mBinding.llCallUs1.setVisibility(View.VISIBLE);
            mBinding.llCallUs2.setVisibility(View.VISIBLE);
            mBinding.llCallUs3.setVisibility(View.VISIBLE);
            mBinding.llCallUs4.setVisibility(View.VISIBLE);
            mBinding.tvCallUs1.setText(split[1]);
            mBinding.tvCallUs2.setText(split[2]);
            mBinding.tvCallUs3.setText(split[3]);
            mBinding.tvCallUs4.setText(split[4]);
        }
        if (split.length == 6){
            mBinding.llCallUs1.setVisibility(View.VISIBLE);
            mBinding.llCallUs2.setVisibility(View.VISIBLE);
            mBinding.llCallUs3.setVisibility(View.VISIBLE);
            mBinding.llCallUs4.setVisibility(View.VISIBLE);
            mBinding.llCallUs5.setVisibility(View.VISIBLE);
            mBinding.tvCallUs1.setText(split[1]);
            mBinding.tvCallUs2.setText(split[2]);
            mBinding.tvCallUs3.setText(split[3]);
            mBinding.tvCallUs4.setText(split[4]);
            mBinding.tvCallUs5.setText(split[5]);
        }
        mBinding.tvLetsChat.setText(mWhatApp);
        mBinding.tvSendEmail.setText(mEmail);
        mBinding.tvLetsChat1.setText(mWhatApp1);
        mBinding.rlCallUs.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[0]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlCallUs1.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[1]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlCallUs2.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[2]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlCallUs3.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[3]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlCallUs4.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[4]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlCallUs5.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            String tel = Uri.encode(split[5]);
            intent.setData(Uri.parse("tel:" + tel));
            startActivity(intent);
        });
        mBinding.rlLetsChat.setOnClickListener(v -> {
            if (isAppInstall(getActivity(), "com.whatsapp")) { //判断是否装了whatsAPP
                chatInWhatsApp(getContext(),mWhatApp);
            }else {
                ToastManager.show(getActivity(),"not install whatsAPP");
            }
        });
        mBinding.rlLetsChat1.setOnClickListener(v -> {
            if (isAppInstall(getActivity(), "com.whatsapp")) { //判断是否装了whatsAPP
                chatInWhatsApp(getContext(),mWhatApp1);
            }else {
                ToastManager.show(getActivity(),"not install whatsAPP");
            }
        });
        mBinding.rlSendEmail.setOnClickListener(v -> {
            openEmail();
        });
    }

    public static void chatInWhatsApp(Context mContext, String mobileNum) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobileNum));
            intent.setPackage("com.whatsapp");
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppInstall(Context context, String appName) {
        PackageInfo packageInfo = null ;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(appName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo==null?false:true;
    }
    /**
     * 打开邮箱客户端
     */
    private void openEmail() {
        Uri uri = Uri.parse("mailto:" + mEmail);
        List<ResolveInfo> packageInfos = getContext().getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_SENDTO, uri), 0);
        List<String> tempPkgNameList = new ArrayList<>();
        List<Intent> emailIntents = new ArrayList<>();
        for (ResolveInfo info : packageInfos) {
            String pkgName = info.activityInfo.packageName;
            if (!tempPkgNameList.contains(pkgName)) {
                tempPkgNameList.add(pkgName);
                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(pkgName);
                emailIntents.add(intent);
            }
        }
        if (!emailIntents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(emailIntents.remove(0), "Select your email");
            if (chooserIntent != null) {
                // 设置对方邮件地址
                chooserIntent.putExtra(Intent.EXTRA_EMAIL, uri);
                chooserIntent.setAction(Intent.ACTION_SENDTO);
                chooserIntent.setData(uri);
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(getActivity(), "No available mail client was found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No available mail client was found", Toast.LENGTH_SHORT).show();
        }
    }

}
