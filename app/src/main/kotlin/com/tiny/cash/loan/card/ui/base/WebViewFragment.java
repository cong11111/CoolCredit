package com.tiny.cash.loan.card.ui.base;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiny.cash.loan.card.Constant;
import com.tiny.cash.loan.card.kudicredit.R;
import com.tiny.cash.loan.card.log.LogSaver;

import org.jetbrains.annotations.NotNull;

public class WebViewFragment extends BaseFragment2 {

    private WebView webView;

    private String mUrl;
    private ProgressBar pbLoading;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        initializeView(view);
        initializeData();
        return view;
    }

    private void initializeView(View view) {
        pbLoading = view.findViewById(R.id.pb_webview_loading);
        webView = view.findViewById(R.id.web_view);
//        startLoading();
    }

    private void initializeData() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 100 && pbLoading == null) {
                   return;
                }
                if (newProgress == 100){
                    if (pbLoading.getVisibility() == View.VISIBLE){
                        pbLoading.setVisibility(View.GONE);
                    }
                } else {
                    if (pbLoading.getVisibility() != View.VISIBLE){
                        pbLoading.setVisibility(View.VISIBLE);
                    }
                    pbLoading.setProgress(newProgress);
                }
//                Log.e("Test", " on progress changed = " + newProgress);
            }
        });
        webView.setWebViewClient(webViewClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);//设置是否支持插件
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (!TextUtils.isEmpty(mUrl)) {
            webView.loadUrl(mUrl);
            if (Constant.IS_COLLECT) {
                LogSaver.logToFile("webview url = " + mUrl);
            }
            Log.e("Test", " url 1 = " + mUrl);
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (!TextUtils.isEmpty(url) && url.contains(getCallBackMethod())){
                if (getActivity().isFinishing() || getActivity().isDestroyed()){
                    return;
                }
                // TODO
//                if (getActivity() instanceof PayActivity){
//                    PayActivity payActivity = (PayActivity) getActivity();
//                    payActivity.payStackSuccess();
//                } else if (getActivity() instanceof PayActivity2){
//                    PayActivity2 payActivity = (PayActivity2) getActivity();
//                    payActivity.payStackSuccess();
//                }
            }
            if (Constant.IS_COLLECT) {
                LogSaver.logToFile("webview url = " + url);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (interceptUrl(url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        private boolean interceptUrl(String url) {
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("tel:")) {
                    startCall(url);
                    return true;
                } else if (url.startsWith("mailto") || url.startsWith("intent")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    LogSaver.logToFile("webview onReceivedError = " + error.getDescription() + " getErrorCode = " + error.getErrorCode());
                } else {
                    LogSaver.logToFile("webview onReceivedError  22= " );
                }
                LogSaver.logToFile("webview onReceivedError  = " + request.getUrl() );
                Log.e("Test", " onReceivedError = " + request.getUrl()  );
            } catch (Exception e) {

            }

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            try {
                LogSaver.logToFile("webview onReceivedHttpError = " + request.getUrl());
                LogSaver.logToFile("webview errorResponse = " + errorResponse.getStatusCode());
            } catch (Exception e) {

            }

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            LogSaver.logToFile("webview onReceivedSslError = " + error.toString());
        }

    };

    private String getCallBackMethod(){
        // TODO
//        TextInfoResponse response = Constant.Companion.getTextInfoResponse();
        String resultUrl = null;
//        if (response != null && !TextUtils.isEmpty(response.getPaystackCallbackAddress())){
//            resultUrl = response.getPaystackCallbackAddress();
//        }
        if (TextUtils.isEmpty(resultUrl)){
            resultUrl = "callback.icredit.ng";
        }
        return resultUrl;
    }

    private void startCall(String telUrl) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telUrl));
        startActivity(intent);
    }

    public void setUrl(String url) {
        mUrl = url;
        if (webView != null) {
            webView.loadUrl(mUrl);
            Log.e("Test", " url = " + mUrl);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUrl = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }

    private void startLoading() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 100; i++) {
                    SystemClock.sleep(500);
                    final int index = i;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pbLoading != null) {
                                pbLoading.setProgress(index);
                            }
                        }
                    });
                }

            }
        }.start();
    }
}
