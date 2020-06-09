package com.hybunion.yirongma.payment.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.model.JavaScriptInterface;

import butterknife.Bind;

// 收款码/和卡结算界面
public class SettlementFragment extends BasicFragment {
    @Bind(R.id.webview_settlement_fragment)
    WebView mWebView;

    private WebSettings mWebSettings;
    private String mUrl = "";

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_settlement_layout;
    }

    @Override
    protected void initView() {
        super.initView();

        Bundle arguments = getArguments();
        if (arguments != null)
            mUrl = (String) arguments.get("url");
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebView.setHapticFeedbackEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        final JavaScriptInterface myJavaScriptCallBacks = new JavaScriptInterface(new JavaScriptInterface.OnJsCallBack() {
            @Override
            public void onCallNative(String value) {
            }
        });
        mWebView.addJavascriptInterface(myJavaScriptCallBacks, "nativeInterFaceHandler");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog("");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mWebView.loadUrl("javascript:nativeInterFaceHandler.callNative(document.getElementById('version').innerText);");
                hideProgressDialog();
            }
        });
    }

    public void loadNewUrl(String url) {
        mWebView.loadUrl(url);
    }

}
