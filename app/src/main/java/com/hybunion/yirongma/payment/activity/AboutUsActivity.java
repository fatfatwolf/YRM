package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.Constants;

/**
 * 关于我们
 *
 * @author LYJ
 */
public class AboutUsActivity extends BaseActivity implements OnClickListener {
    private TextView tv_version, customerservice_phone, collection_phone;
    private String version;
    private RelativeLayout ll_customerservice_phone, ll_collection_phone, rl_microblog, rl_wechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
//        findViewById(R.id.btn_back).setOnClickListener(this);
        ll_customerservice_phone = (RelativeLayout) findViewById(R.id.ll_customerservice_phone);
        ll_customerservice_phone.setOnClickListener(this);
        ll_collection_phone = (RelativeLayout) findViewById(R.id.ll_collection_phone);
        ll_collection_phone.setOnClickListener(this);
        if (Constant.AGENT_ID == 0) {
            ll_customerservice_phone.setVisibility(View.VISIBLE);
            ll_collection_phone.setVisibility(View.VISIBLE);
        }

        tv_version = (TextView) findViewById(R.id.tv_version);
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText("版本号：" + version + Constants.TEST_CODE);
        customerservice_phone = (TextView) findViewById(R.id.customerservice_phone);
        collection_phone = (TextView) findViewById(R.id.collection_phone);
        rl_microblog = (RelativeLayout) findViewById(R.id.rl_microblog);
        rl_wechat = (RelativeLayout) findViewById(R.id.rl_wechat);

        rl_microblog.setOnClickListener(this);
        rl_wechat.setOnClickListener(this);

        WebView webView = (WebView) findViewById(R.id.wv_about_us);
        webView.loadUrl(getResources().getString(R.string.about_us_url));
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不使用缓存
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (100 == newProgress) {
                    findViewById(R.id.footer).setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_back:
//                finish();
//                break;
            case R.id.ll_customerservice_phone:
                String customerServicePhone = customerservice_phone.getText().toString().trim();
                Log.i("lyj", customerServicePhone + "");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customerServicePhone));
                startActivity(intent);
                break;
            case R.id.ll_collection_phone:
                String collectionPhone = collection_phone.getText().toString().trim();
                Intent Phone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + collectionPhone));
                startActivity(Phone);
                break;
            case R.id.rl_microblog:
//                Intent microblogIntent = new Intent(AboutUsActivity.this, ActivityWebView.class);
//                microblogIntent.putExtra("title", "关于我们");
//                microblogIntent.putExtra("url", "http://weibo.com/hybunion");
//                startActivity(microblogIntent);
                break;
            case R.id.rl_wechat:
                Toast.makeText(AboutUsActivity.this, "正在开发中,敬请期待...", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }
}

