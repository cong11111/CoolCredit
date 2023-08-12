package com.tiny.cash.loan.card.feature.start;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tiny.cash.loan.card.Constants;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.base.BaseActivity;
import com.tiny.cash.loan.card.kudicredit.databinding.ActivityAgreementBinding;

import co.paystack.android.utils.StringUtils;

public class AgreementActivity extends BaseActivity {

    private ActivityAgreementBinding binding;
    public static final String TYPE = "type";
    private String mUrl, title, mOrderId, mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgreementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//DataBindingUtil.setContentView(this, R.layout.activity_agreement);
        String type = getIntent().getStringExtra(TYPE);
        binding.llTitle.layoutToolbar.setVisibility(View.VISIBLE);
        if (Constants.ONE.equals(type)) {
            title = getString(R.string.str_terms_of_service);
            mUrl = "https://www.kudicredit.com/terms.html";
        } else if (Constants.TWO.equals(type)) {
            title = getString(R.string.str_privacy_policy);
            mUrl = "https://www.kudicredit.com/privacy.html";
        } else  if (Constants.THREE.equals(type))  {
            WebSettings webSettings = binding.mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            title = "PayStack";
            mUrl = getIntent().getStringExtra("Url");
            mOrderId = getIntent().getStringExtra("orderId");
            mReference = getIntent().getStringExtra("reference");
        }else {
            binding.llTitle.layoutToolbar.setVisibility(View.GONE);
            WebSettings webSettings = binding.mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            title = "PayStack";
            mUrl = getIntent().getStringExtra("Url");
        }
        binding.llTitle.tvTitle.setText(title);
        binding.llTitle.ivBack.setOnClickListener(v -> {
            finish();
        });
        initView();
    }

    private void initView() {
        binding.mWebView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        if (mUrl != null) {
            binding.mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //使用WebView加载显示url
                    view.loadUrl(url);
                    //返回true
                    return true;
                }

                @Override

                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    super.onPageStarted(view, url, favicon);


                }

                @Override

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.onFinishTemporaryDetach();
                    if (!StringUtils.isEmpty(url) && url.contains("callback.icredit.ng")) {
                        setResult(2000);
                        onBackPressed();
                    }else {
                        setResult(3000);
//                        onBackPressed();
                    }
                }
            });
            binding.mWebView.loadUrl(mUrl);


        }
    }

}