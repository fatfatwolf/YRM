package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.model.JavaScriptInterface;


public class OpenAnimActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_head;
    private WebView webView;
    private LinearLayout ll_back;
    WebSettings mWebSettings;
    String linkUrl = "";
    private boolean flag = false;
    private boolean isLogin = false;
    String UID;
    @Override
    protected int getContentView() {
        return R.layout.activity_open_anim;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void initView() {
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setText("广告详情");
        webView = (WebView) findViewById(R.id.webview);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        linkUrl = getIntent().getStringExtra("linkUrl");
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        webView.setHapticFeedbackEnabled(false);
        initWebView();
        UID = SharedPreferencesUtil.getInstance(OpenAnimActivity.this).getKey(SharedPConstant.UID);
        isLogin = getIntent().getBooleanExtra("isLogin",false);
        if(linkUrl == ""){

        }else {
            webView.loadUrl(linkUrl);
        }
    }

    @Override
    public void initData() {

    }

    public void initWebView(){
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        final JavaScriptInterface myJavaScriptCallBacks = new JavaScriptInterface(new JavaScriptInterface.OnJsCallBack() {
            @Override
            public void onCallNative(String value) {
                //SharedPreferencesUtil.putKey(SharedPConstant.merchantID,value);
                //SharedPreferencesUtil.getInstance()
                LogUtil.d(value);
                //.deleteDatabase(“WebView.db”); 3.context.deleteDatabase(“WebViewCache.db”);
            }
        });
        //webView.getSettings().setAppCachePath();
        webView.addJavascriptInterface(myJavaScriptCallBacks, "nativeInterFaceHandler");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoading();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:nativeInterFaceHandler.callNative(document.getElementById('version').innerText);");
                hideLoading();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                flag = false;
                SharedPreferencesUtil.getInstance(OpenAnimActivity.this).putBooleanKey("isGoAnim",flag);
                showLoading();
                if(isLogin){
                    SharedPreferencesUtil.getInstance(OpenAnimActivity.this).putKey(SharedPConstant.FLOAT_IS_SHOW, 1); // 用户默认开启悬浮球功能
                    if(TextUtils.isEmpty(UID)){
                        Intent intent = new Intent(OpenAnimActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(OpenAnimActivity.this,LMFMainActivity.class);
                        intent.putExtra("intentType","2");//标志从哪里跳到得主页
                        startActivity(intent);
                    }

                }else {
                    Intent intent = new Intent(OpenAnimActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                hideLoading();
                OpenAnimActivity.this.finish();
                break;

            }
        }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            showLoading();
            if(isLogin){
                if(TextUtils.isEmpty(UID)){
                    Intent intent = new Intent(OpenAnimActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(OpenAnimActivity.this,LMFMainActivity.class);
                    intent.putExtra("intentType","2");//标志从哪里跳到得主页
                    startActivity(intent);
                }
            }else {
                Intent intent = new Intent(OpenAnimActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            hideLoading();
            OpenAnimActivity.this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
