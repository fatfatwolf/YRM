package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.OpenAnimResult;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.service.VMForegroundService;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity {
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
            , Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE
    };
    private List<String> mPermissionList = new ArrayList<>();//权限的list
    boolean isFirstIn = true;//是否第一次进入APP，判断是否跳到导航页
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    public static final int GO_LOGIN = 1002;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private String imageStr;
    TextView tv_time;
    private ImageView iv_open_anim;
    private String imgUrl, linkUrl;
    String UID;
    /**
     * 实例
     */
    private boolean isGoAnim = false;//跳转到广告业标识，用于广告页面的处理
    private boolean isClick = false;//是否 点击上方跳过五秒得按钮
    private boolean isLogin;//是否开启了自动登录
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                    case GO_HOME:
                        if (isGoAnim) {

                        } else{
                            SharedPreferencesUtil.getInstance(SplashActivity.this).putKey(SharedPConstant.FLOAT_IS_SHOW, 1); // 用户默认开启悬浮球功能
                            Intent intent = new Intent(SplashActivity.this, LMFMainActivity.class);
                            intent.putExtra("intentType","2");//2为标志从哪里跳到得主页
                            SplashActivity.this.startActivity(intent);
                        }

                        break;
                    case GO_GUIDE:
                        if (isGoAnim) {

                        } else{
                            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                            SplashActivity.this.startActivity(intent);
                        }
                        break;
                    case GO_LOGIN:
                        if (isGoAnim) {

                        } else{
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            SplashActivity.this.startActivity(intent);
                        }
                        break;

                    case 0:
                        tv_time.setText("跳过 (" + msg.arg1 + ")秒");
                        tv_time.setClickable(true);
                        if (msg.arg1 == 1) {
                            if (!isFirstIn) {//不是第一次登录

                                if (!isClick){//没有点击跳过按钮
                                    if (isLogin){
                                        if (TextUtils.isEmpty(UID)){ // 本地没有保存登录得UID，跳到登录页
                                            mHandler.sendEmptyMessageDelayed(GO_LOGIN, 0); // 去登录
                                        }else {
                                            mHandler.sendEmptyMessageDelayed(GO_HOME, 0);
                                        }
                                    }else{
                                        mHandler.sendEmptyMessageDelayed(GO_LOGIN, 0); // 去登录
                                    }

                                }

                            } else {//是第一次登录，无广告链接
                                    tv_time.setVisibility(View.GONE);
                                    mHandler.sendEmptyMessageDelayed(GO_GUIDE, 0);
                            }
                        }
                        break;
                }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //设置回application主题
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent foregroundIntent = new Intent(getApplicationContext(), VMForegroundService.class);
        startService(foregroundIntent);
        setContentView(R.layout.splash);
//        initPermissions();
        HRTApplication.getInstance().addActivity(this);
        isFirstIn = SharedPreferencesUtil.getInstance(this).getBooleanKey(SharedPConstant.IS_FIRST_IN);
        UID = SharedPreferencesUtil.getInstance(SplashActivity.this).getKey(SharedPConstant.UID);
        isLogin = SharedPreferencesUtil.getInstance(SplashActivity.this).getBooleanKey(SharedPConstant.ISAUTO_LOGIN,false);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_open_anim = (ImageView) findViewById(R.id.iv_open_anim);
        /*防止首次安装点击home键重新实例化*/
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        Constant.changed = 1;

        if (!YrmUtils.isFastDoubleClick(R.id.iv_open_anim)) {
            iv_open_anim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //执行点击事件
                    if (imageStr.equals("")) {
                        return;
                    }
                    isGoAnim = true;
                    Log.i("xjz", "跳转到广告页面");
                    Intent intent = new Intent(SplashActivity.this, OpenAnimActivity.class);
                    intent.putExtra("linkUrl", linkUrl);
                    intent.putExtra("isLogin", isLogin);
                    startActivity(intent);
                    SplashActivity.this.finish();


                }
            });
        }
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClick = true;
                if(isLogin){//勾选了自动登录
                    if (TextUtils.isEmpty(UID)){ // 本地没有保存登录得UID，跳到登录页
                        mHandler.sendEmptyMessageDelayed(GO_LOGIN, 0); // 去登录
                    }else {
                        mHandler.sendEmptyMessageDelayed(GO_HOME, 0);
                    }
                }else {
                    mHandler.sendEmptyMessageDelayed(GO_LOGIN, 0); // 去登录
                }

            }
        });

        // 判断是否是第一次进入APP
        // 第一次进入，弹出用户协议、隐私政策和权限功能提示框
        if (isFirstIn) {

            View view = LayoutInflater.from(this).inflate(R.layout.dialog_agreement_permission, null);
            final Dialog dialog = new Dialog(this, R.style.notice_dialog);
            final View container = view.findViewById(R.id.dialog_layout);
            final TextView tvMessage = view.findViewById(R.id.tv_message);
            final TextView tvConfirm = view.findViewById(R.id.tv_confirm);
            final TextView tvCancel = view.findViewById(R.id.tv_cancel);

            StringBuilder agreementSb = new StringBuilder();
            String startStr = "欢迎您使用众维码APP，请您充分阅读并理解";
            String userStr = "《众维码客户端用户服务协议》";
            String privateStr = "《隐私政策》";
            String endStr = "。\n" +
                    "根据相关法律法规的规定，为了保证您正常使用APP相关功能，本APP需获取以下权限：\n" +
                    "存储权限：用于图片等信息的保存，缓存信息降低流量的消耗；\n" +
                    "电话权限：用于拨打客服电话，识别手机设备信息，保证问题及时解决；\n" +
                    "位置权限：根据位置信息，获取商户位置，确保商户信息更加准确；\n" +
                    "相机权限：用于拍摄照片，提交认证信息，确保过程更加流畅。";
            agreementSb.append(startStr);
            agreementSb.append(userStr);
            agreementSb.append("和");
            agreementSb.append(privateStr);
            agreementSb.append(endStr);
            SpannableString spannableInfo = new SpannableString(agreementSb.toString());
            ClickableSpan cs = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    WebViewActivity.start(SplashActivity.this, "用户服务协议", NetUrl.AGREEMENT);
                }
            };
            spannableInfo.setSpan(cs, agreementSb.indexOf(userStr), agreementSb.indexOf(userStr) + userStr.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan cs1 = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
//                    ToastUtil.showShortToast("隐私协议");
                    WebViewActivity.start(SplashActivity.this, "隐私政策",NetUrl.PERSONALMENT);

                }
            };
            spannableInfo.setSpan(cs1, agreementSb.indexOf(privateStr), agreementSb.indexOf(privateStr) + privateStr.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvMessage.setText(spannableInfo);
            tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
            tvMessage.setHighlightColor(getResources().getColor(android.R.color.transparent));

            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getAnimImage();

                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    HRTApplication.getInstance().finishAllActivities();
                }
            });

            container.setLayoutParams(new FrameLayout.LayoutParams(
                    (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.8f),
                    FrameLayout.LayoutParams.WRAP_CONTENT));

            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }else{
            getAnimImage();
        }

    }
    public void initPermissions(){
        mPermissionList.clear();
        for (int i = 0; i < PERMISSIONS_STORAGE.length; i++) {
            if (ContextCompat.checkSelfPermission(this, PERMISSIONS_STORAGE[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(PERMISSIONS_STORAGE[i]);
            }
        }

        if (!YrmUtils.isEmptyList(mPermissionList) && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {//需要动态申请权限
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, 101);
        }
    }


    public void getAnimImage() {
        imageStr = SharedPreferencesUtil.getInstance(SplashActivity.this).getKey(SharedPConstant.JSON_LIST);
        Gson gson = new Gson();
        if (TextUtils.isEmpty(imageStr) || imageStr.equals("null")) {
            getOpenAnimation();
            if (!isFirstIn) {
                // 判断本地是否保存 UID
                Log.i("xjz", "不是第一次登录");
				if (TextUtils.isEmpty(UID)){ // 本地没有保存登录得UID，跳到登录页
                     mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS); // 去登录
				}else {
                    mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
                }

            } else {
                Log.i("xjz", "是第一次登录");
                tv_time.setVisibility(View.GONE);
                mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
            }
        } else {
            List<OpenAnimResult.OpenImage> imageList = gson.fromJson(imageStr, new TypeToken<List<OpenAnimResult.OpenImage>>() {
            }.getType());
            int index = (int) (0 + Math.random() * imageList.size());//将json字符串转换成List集合
            imgUrl = imageList.get(index).getAd_img_url();
            linkUrl = imageList.get(index).getAd_link_url();
            Glide.with(SplashActivity.this)
                    .load(imgUrl)
                    .placeholder(R.drawable.bootpage)//默认图片
                    .into(new GlideDrawableImageViewTarget(iv_open_anim) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            tv_time.setVisibility(View.VISIBLE);
                            getOpenAnimation();
                            if (!isClick) {
                                //在这里添加一些图片加载完成的操作
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 5; i > 0; i--) {
                                            Message msg = mHandler.obtainMessage();
                                            msg.arg1 = i;
                                            mHandler.sendMessage(msg);
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }
                                }).start();
                            }

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            Log.i("xjz--失败", "图片加载失败" + "  imgUrl" + imgUrl);
                            tv_time.setVisibility(View.GONE);
                            getOpenAnimation();
                            if (isFirstIn) {
                                mHandler.sendEmptyMessageDelayed(GO_GUIDE, 3000);
                            } else {
                                mHandler.sendEmptyMessageDelayed(GO_HOME, 3000);
                            }

                        }
                    });
        }


    }

    public void getOpenAnimation() {

        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("type", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = NetUrl.OPEN_ANIMATION;
        OkUtils.getInstance().post(null, url, jsonRequest, new MyOkCallback<OpenAnimResult>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(OpenAnimResult result) {
                    if (result != null) {
                        if (result.getStatus().equals("0")) {
                            Gson gson1 = new Gson();
                            if (result.getData() != null && result.getData().size() > 0) {
                                String jsonList = gson1.toJson(result.getData());
                                if (imageStr.equals(jsonList)) {
                                    Log.i("xjz", "图片一致，无须重新缓存");
                                } else {
                                    if (("").equals(imageStr)) {
                                        int index = (int) (0 + Math.random() * result.getData().size());//将json字符串转换成List集合
                                        imgUrl = result.getData().get(index).getAd_img_url();
                                        linkUrl = result.getData().get(index).getAd_link_url();

                                    }
                                    SharedPreferencesUtil.getInstance(SplashActivity.this).putKey(SharedPConstant.JSON_LIST, jsonList);
                                }
                            }
                        }
                    }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return OpenAnimResult.class;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//未申请权限
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissions[i]);
                    if (showRequestPermission) {//已申请

                    } else {//未申请
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }




}
