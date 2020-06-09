package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hybunion.yirongma.BuildConfig;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.model.JavaScriptInterface;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.hybunion.yirongma.R.id.webview;

/**
 * @author xjz
 * @date 2018/8/30
 * @email freemars@yeah.net
 * @description
 */

public class  LMFRedRainActivity extends BaseActivity {
//    @Bind(R.id.tv_head)
//    TextView tv_head;
    @Bind(R.id.titleBar_webview)
    TitleBar mTitleBar;
    @Bind(R.id.webview)
    WebView webView;
    WebSettings mWebSettings;
    private String webViewUrl;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private final static int VIDEO_REQUEST = 120;
    private ProgressBar progressBar;//显示网页加载进度的进度条
    private Uri imageUri;
    private Handler maniHandler = new Handler(Looper.getMainLooper());
    String type;

    /**
     * get contentView's id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_lmf_webview;
    }

    /**
     * load data from sever
     */
    @Override
    protected void loadData() {

    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView() {
        mTitleBar.setTitleBarBackVisible(true);
        webView = (WebView) findViewById(webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent = getIntent();
        webViewUrl = intent.getStringExtra("webViewUrl");
        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLightTouchEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setSupportZoom(true);
        webView.setHapticFeedbackEnabled(false);
        if ("1".equals(webViewUrl)) {
            mTitleBar.setTv_titlebar_back_titleText("活动");
            String url1 = getIntent().getStringExtra("url");
//            webView.addJavascriptInterface(new YYCNewJavaScriptInterface(LMFRedRainActivity.this), "Android");
            String mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MID);
            if (mid == null) {
                mid = "";
            }
            String url111 = url1 + "?merId=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID) + "&mid=" + mid;
            webView.loadUrl(url1 + "?merId=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID) + "&mid=" + mid);
        } else if ("2".equals(webViewUrl)) {
            LogUtil.d(NetUrl.green_plan_URL + "?merchantID=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID) + "=====商户ID");
            String url = NetUrl.green_plan_URL + "?merchantID=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
            webView.loadUrl(NetUrl.green_plan_URL + "?merchantID=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
        } else if ("3".equals(webViewUrl)) {
            String agentIdDiff = String.valueOf(Constant.AGENT_ID_DIFF);
            if(agentIdDiff.equals("0")){//易融码
                webView.loadUrl(NetUrl.use_help_yrm_URL);
            }else if(agentIdDiff.equals("1")){//立码富
                webView.loadUrl(NetUrl.use_help_lmf_URL);
            }

        }else if("4".equals(webViewUrl)){//商户活动协议书
            webView.loadUrl(NetUrl.Ty_Protocol_URL);
        }else if("5".equals(webViewUrl)){//贷款
            mTitleBar.setTv_titlebar_back_titleText("贷款");
            String totalAmount = getIntent().getStringExtra("totalAmount");
            String mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MID);
            String url = BuildConfig.LOAN_URL + "product_Hmd.html?merId=" + mid + "&amount=" + totalAmount;
            webView.loadUrl(url);
        }else if("6".equals(webViewUrl)){
            String mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MID);
            String url = getResources().getString(R.string.bussiness_act_url)+ "?merId=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID) + "&mid=" + mid;
            webView.loadUrl(url);
        } else if("7".equals(webViewUrl)){
            String mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MID);
//            String url = getResources().getString(R.string.bussiness_act_url)+ "?merId=" + SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID) + "&mid=" + mid;
            String url = getResources().getString(R.string.merchant_activety_url);
            webView.loadUrl(url);
        } else if("8".equals(webViewUrl)){
            String url = "https://download.hybunion.com/h5/SelfMarketingAgreement.html";

            webView.loadUrl(url);
        }else if("9".equals(webViewUrl)){
            String url = "https://download.hybunion.com/h5/RechargeProtocol.html";
            webView.loadUrl(url);
        }else if("10".equals(webViewUrl)){
            String mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MID);
            String agentId = this.getString(R.string.AGENT_ID);
            String url;
            if("0".equals(agentId)){
                 url = "https://wechattest.hrtfin.com/jhdWeb/product_jhd.html?channel="+"21"+"&mid="+mid;
//                 url = "http://10.51.130.15:8020/jhdWeb/faceRecognition.html?__hbt=1571894512125#";
//                url = "https://wechat.hrtfin.com/jhdWeb/product_jhd.html?channel=" + "21" + "&mid=" + mid;
            }else {
//                url = "https://wechattest.hrtfin.com/jhdWeb/product_jhd.html?channel="+"20"+"&mid="+mid;
                url = "https://wechat.hrtfin.com/jhdWeb/product_jhd.html?channel=" + "20" + "&mid=" + mid;
            }
            webView.loadUrl(url);
        } else {
            webView.loadUrl(NetUrl.voice_help_URL);
        }
        initWebView();
    }

    public void initWebView() {

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
                if(!TextUtils.isEmpty(url)){
                    webView.loadUrl(url);
//                    videoFlag = url.contains("vedio");
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!"3".equals(webViewUrl)){
                    if (!isFinishing())
                        showLoading();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isFinishing())
                    hideLoading();
                if (!"1".equals(webViewUrl) && !"5".equals(webViewUrl)){
                    mTitleBar.setTv_titlebar_back_titleText(view.getTitle());
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                if (!YrmUtils.isHavePermission(LMFRedRainActivity.this, Manifest.permission.CAMERA, YrmUtils.REQUEST_PERMISSION_CAMERA))
                    return  false;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    type = fileChooserParams.getAcceptTypes()[0];
                }
                if (type.contains("video")) {
                    recordVideo();
                } else {
                    take();
                }
                return true;
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                    take();
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                    take();
            }
            //4.1-5.0系统
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                if (acceptType.contains("video")) {
                    recordVideo();
                } else {
                    take();
                }
            }
        });
    }


    /**
     * 录像
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //限制时长
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        //开启摄像机
        startActivityForResult(intent, VIDEO_REQUEST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result != null) {
                    String path = getPath(getApplicationContext(),
                            result);
                    Uri uri = Uri.fromFile(new File(path));
                    mUploadMessage
                            .onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri);
                }
                mUploadMessage = null;


            }
        }else if (requestCode == VIDEO_REQUEST) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;

            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                if (resultCode == RESULT_OK) {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
                    mUploadCallbackAboveL = null;
                } else {
                    mUploadCallbackAboveL.onReceiveValue(new Uri[]{});
                    mUploadCallbackAboveL = null;
                }

            } else if (mUploadMessage != null) {
                if (resultCode == RESULT_OK) {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(Uri.EMPTY);
                    mUploadMessage = null;
                }

            }
        }
    }


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;

        if (resultCode == RESULT_OK) {

            if (data == null) {

                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void initData() {
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
}
