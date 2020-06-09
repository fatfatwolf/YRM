package com.hybunion.yirongma.payment.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;

import com.hybunion.yirongma.payment.utils.MD5Util;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SaveImageToAlbum;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author LYJ
 */
public class SweepOrderActivity extends BaseActivity {
    String mid,merchantName,loginName,body;
//    LinearLayout btn_back;
    String url,publicKey,md5Key;
    WebView webView;
    WebSettings mWebSettings;
    private String cookieStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_order);
        loginName =SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGIN_NUMBER);
        mid = SharedPreferencesUtil.getInstance(SweepOrderActivity.this).getKey("mid");
        merchantName= SharedPreferencesUtil.getInstance(SweepOrderActivity.this).getKey("merchantName");
        publicKey = YrmUtils.getTimeStamp();
        md5Key = MD5Util.smallMd5(publicKey+"yunqisecret");
        url = "http://store.yunqixinxi.com/login#/allw/auth/phone="+loginName+"/linkName="+mid+"/storeName="+merchantName+"/publicKey="+publicKey+"/md5Key="+md5Key;


        webView = (WebView) findViewById(R.id.wv_about_us);
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setSupportZoom(true);
        webView.setHapticFeedbackEnabled(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.supportMultipleWindows();
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebSettings.setAllowFileAccess(true);  //设置可以访问文件
        mWebSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        mWebSettings.setSavePassword(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (100 == newProgress) {
//                    findViewById(R.id.footer).setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.clearCache(true); // 清除资源缓存。请注意，缓存是每个应用程序的，所以这将清除所有使用的WebViews的缓存。false的话，只会清除RAM上的缓存。
                webView.clearHistory(); // 清除历史记录
                webView.clearFormData(); // //从当前关注的表单字段中移除自动填充弹出窗口
                webView.clearSslPreferences(); // 清除存储的SSL首选项表
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                if (!isFinishing())
//                    showProgressDialog("");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (!isFinishing())
//                    hideProgressDialog();

                CookieManager cookieManager = CookieManager.getInstance();
                cookieStr = cookieManager.getCookie(url); // 获取到cookie字符串值
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String fileName = contentDisposition.substring(contentDisposition.lastIndexOf("="));
                DownloadTask task = new DownloadTask();
                task.execute(url, fileName);
//                DonwloadSaveImg.donwloadImg(SweepOrderActivity.this,url);

            }
        });
    }


    public void url2bitmap(){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
        if (bitmap != null) {
            SaveImageToAlbum.saveFile(SweepOrderActivity.this,bitmap);
        }

//        URL iconUrl = null;
//        try {
////            iconUrl = new URL(url);
//            URLConnection conn = iconUrl.openConnection();
//            HttpURLConnection http = (HttpURLConnection) conn;
//            int length = http.getContentLength();
//            conn.connect();
//            // 获得图像的字符流
//            InputStream is = conn.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is, length);
//            bitmap = BitmapFactory.decodeFile(fileName);
//            bis.close();
//            is.close();
//            if (bitmap != null) {
//                SaveImageToAlbum.saveFile(SweepOrderActivity.this,bitmap);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearSslPreferences();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (webView.canGoBack()) {
            webView.goBack();//返回上一页面
        } else {
            this.finish();
        }
    }
    String fileName;
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = params[0]; // 要访问的链接
            fileName = params[1]; // 文件名

            if (TextUtils.isEmpty(url)) {
                return null;
            }

            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, fileName);
//            if (file.exists()) {
//                return fileName;
//            }

            // 下载文件
            try {
                URL myFileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) myFileUrl
                        .openConnection();
                // 携带cookie请求，要写在请求开始之前
                conn.addRequestProperty("Cookie", cookieStr);
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                // 我们把文件保存在sd卡根目录
                FileOutputStream out = new
                        FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = is.read(buffer)) > 0) {
                    out.write(buffer, 0, r);
                    out.flush();
                }
                is.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            Log.i("xjz--fileName",fileName);
            return fileName;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 这里可以对下载好的文件进行打开等操作
            url2bitmap();
//            Intent mediaScanIntent = new Intent(
//                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File file = new File(fileName);
//            Uri contentUri = Uri.fromFile(file);
//            mediaScanIntent.setData(contentUri);
//            sendBroadcast(mediaScanIntent);
        }
    }
}

