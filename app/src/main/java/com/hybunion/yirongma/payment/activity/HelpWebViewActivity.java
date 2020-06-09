package com.hybunion.yirongma.payment.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;


/**
 * Created by liujia on 2016/3/1.
 */
public class HelpWebViewActivity extends BaseActivity implements View.OnClickListener {
    private WebView myWebView = null;
    private LinearLayout btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_help);
        findId();
    }
    private void findId() {
        // 打开网页
        btn_back=(LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        myWebView = (WebView) findViewById(R.id.webView1);
        myWebView.loadUrl("http://www.hybunion.com/lmfwallethelp.html");
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history

        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            // 返回键退回
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
