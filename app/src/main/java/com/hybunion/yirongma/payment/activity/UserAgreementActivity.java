package com.hybunion.yirongma.payment.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;

/**
 * 关于我们
 *
 * @author LYJ
 */
public class UserAgreementActivity extends BaseActivity {
    String mid,merchantName,loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_order);
        loginName =SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGIN_NUMBER);
        mid = SharedPreferencesUtil.getInstance(UserAgreementActivity.this).getKey("mid");
        merchantName= SharedPreferencesUtil.getInstance(UserAgreementActivity.this).getKey("merchantName");
        String url ="http://www.xiucan.com/admin/?tpshopid="+mid+"&partner=hyb&openid=tel_+"+loginName+"&shopname="+merchantName+
                "&tel="+loginName+"&city=北京&address=海淀区";
        WebView webView = (WebView) findViewById(R.id.wv_about_us);

        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (100 == newProgress) {
//                    findViewById(R.id.footer).setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webSettings.setDomStorageEnabled(true); // 避免 Android 不支持 H5 的某些标签
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


}

