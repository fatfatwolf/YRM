package com.hybunion.yirongma.payment.zxing.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.payment.bean.PosPayBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.zxing.camera.CameraManager;
import com.hybunion.yirongma.payment.zxing.decoding.CaptureActivityHandler2;
import com.hybunion.yirongma.payment.utils.MD5Util;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.activity.SuccessfulReceipt;
import com.hybunion.yirongma.payment.zxing.decoding.FinishListener;
import com.hybunion.yirongma.payment.zxing.decoding.InactivityTimer;
import com.hybunion.yirongma.payment.zxing.view.ViewfinderView;
import com.hybunion.yirongma.payment.utils.YrmUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 扫一扫
 * Created by lyf on 2017/3/27.
 */


public class CaptureActivity2 extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private ImageView hui_head_back;//返回按键
    private TextView tv_title, tv_cancel;//标题  取消按钮
    private LinearLayout ll_pay_layout; //支付结果界面
    private RelativeLayout fl_camera; //扫一扫界面
    private Button btn_complete;//完成  重新扫描  查询支付状态
    private int clickType = 0;//跳转哪个页面标示
    private ImageView iv_pay_icon;//图标
    private TextView tv_pay_result, tv_cause_description;//结果 原因
    private ViewfinderView mViewfinderView;
    private SurfaceView mSurfaceView;
    private CaptureActivityHandler2 handler;
    private boolean hasSurface;
    private MediaPlayer mediaPlayer;
    private InactivityTimer mInactivityTimer;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean playBeep;
    private boolean vibrate;
    private String resultData, SCANType;//扫面返回结果
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private String payableAmount; //应付金额
    private String noDisAmount; //不打折金额
    private String memDiscount; //会员折扣
    private String payAmount; //实付金额
    private String orderNo;  //订单号
    private int payChannel, sign, finalFlag;
    private String amt, status, message, city, address, reStr, loginType;
    private double longitude, latitude;
    private Button bt_code;
    public String val = "";
    private TextView mTvLamp; // 打开照明灯
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean mIsLampOpen;
    private String mStoreId, merName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        mStoreId = getIntent().getStringExtra("storeId");
        merName = getIntent().getStringExtra("merName");
        setContentView(R.layout.activity_qrcode_scan);
        CameraManager.init(this);
        loginType = SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("loginType");
        hui_head_back = (ImageView) findViewById(R.id.hui_head_back);
        tv_title = (TextView) findViewById(R.id.hui_head_text);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_pay_result = (TextView) findViewById(R.id.tv_pay_result);
        tv_cause_description = (TextView) findViewById(R.id.tv_cause_description);
        iv_pay_icon = (ImageView) findViewById(R.id.iv_pay_icon);
        ll_pay_layout = (LinearLayout) findViewById(R.id.ll_pay_layout);
        fl_camera = (RelativeLayout) findViewById(R.id.fl_camera);
        mTvLamp = (TextView) findViewById(R.id.flashLamp_qrcode_scan_activity);
        mTvLamp.setOnClickListener(this);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        bt_code = (Button) findViewById(R.id.bt_code);
        bt_code.setOnClickListener(this);
        hasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
        hui_head_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        sign = intent.getExtras().getInt("flag");
        bt_code.setVisibility(View.GONE);
        finalFlag = intent.getExtras().getInt("finalFlag");
        amt = getIntent().getStringExtra("amt");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        longitude = intent.getExtras().getDouble("longitude");
        latitude = intent.getExtras().getDouble("latitude");
        if (finalFlag == 20) {
            tv_title.setText("扫一扫");
        }
    }

    private void directToScan(String name) {
        if ("微信条码".equals(name)) {
            tv_title.setText("微信支付");
            payChannel = 55;
        } else if ("支付宝条码".equals(name)) {
            tv_title.setText("支付宝");
            payChannel = 56;
        } else if ("QQ条码".equals(name)) {
            tv_title.setText("QQ钱包");
            payChannel = 59;
        } else if ("百度条码".equals(name)) {
            tv_title.setText("百度钱包");
            payChannel = 57;
        } else if ("京东条码".equals(name)) {
            tv_title.setText("京东钱包");
            payChannel = 58;
        } else if ("银联条码".equals(name)) {
            tv_title.setText("银联支付");
            payChannel = 60;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hui_head_back:
                finish();
                break;
            case R.id.btn_complete:
                if (clickType == 0) {
                    queryPayStatus();//查询支付状态
                } else {
                    fl_camera.setVisibility(View.VISIBLE);
                    ll_pay_layout.setVisibility(View.GONE);
                    tv_cancel.setVisibility(View.GONE);
                    continuePreview();//重新扫码
                }
                break;
            case R.id.tv_cancel: //取消
                finish();
                break;
            case R.id.bt_code:
//                Intent qrCodeIntent = new Intent(CaptureActivity2.this, UnionQRCodeActivity.class);
//                qrCodeIntent.putExtra("payableAmount", payableAmount);
//                qrCodeIntent.putExtra("noDisAmount", noDisAmount);
//                qrCodeIntent.putExtra("memDiscount", memDiscount);
//                qrCodeIntent.putExtra("payAmount", payAmount);
//                startActivity(qrCodeIntent);
//                this.finish();
                break;
            case R.id.flashLamp_qrcode_scan_activity: // 照明灯
                if (mCamera == null) {
                    mCamera = CameraManager.get().getCamera();
                    mParams = mCamera.getParameters();
                }
                if (mCamera != null) {
                    if (!mIsLampOpen) {
                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(mParams);
                        mCamera.startPreview();
                        mIsLampOpen = true;
                        mTvLamp.setText("关闭照明灯");
//                        ToastUtil.show("手电筒打开");
                    } else {
                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(mParams);
                        mIsLampOpen = false;
                        mTvLamp.setText("打开照明灯");
//                        ToastUtil.show("手电筒关闭");
                    }
                }
                break;

        }
    }

    //查询支付状态
    private void queryPayStatus() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("orderNo", orderNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(this, NetUrl.YINLIAN_QUERY, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    String status = response.getString("status");
                    String message = response.getString("msg");
                    String exchangeTime = response.getString("exchangeTime");
                    String orderStatus = response.getString("orderStatus");//订单状态，0未支付，1支付成功，2订单取消
                    ll_pay_layout.setVisibility(View.VISIBLE);
                    fl_camera.setVisibility(View.GONE);
                    tv_cancel.setVisibility(View.VISIBLE);
                    clickType = 0;
                    if (status.equals("0")) {
                        if (orderStatus.equals("0")) {
                            tv_pay_result.setText("支付失败");
                            tv_cause_description.setText(message);
                            tv_cancel.setText("取消");
                            iv_pay_icon.setImageResource(R.drawable.union_fail);
                            btn_complete.setText("查询支付状态");
                        } else if (orderStatus.equals("1")) {
                            tv_pay_result.setText("支付成功");
                            tv_cause_description.setText(message);
                            tv_cancel.setText("完成");
                            iv_pay_icon.setImageResource(R.drawable.union_success);
                            btn_complete.setText("查询支付状态");
                        } else {
                            tv_pay_result.setText("支付失败");
                            tv_cause_description.setText(message);
                            tv_cancel.setText("取消");
                            iv_pay_icon.setImageResource(R.drawable.union_fail);
                            btn_complete.setText("查询支付状态");
                        }

                    } else if (status.equals("1")) {
                        tv_cancel.setText("取消");
                        tv_pay_result.setText("支付失败");
                        tv_cause_description.setText(message);
                        iv_pay_icon.setImageResource(R.drawable.union_fail);
                        btn_complete.setText("查询支付状态");
                    } else if (status.equals("3")) {
                        tv_cancel.setText("取消");
                        tv_pay_result.setText("支付中...");
                        tv_cause_description.setText(message);
                        iv_pay_icon.setImageResource(R.drawable.union_paying);
                        btn_complete.setText("查询支付状态");
                    } else {
                        ToastUtil.showShortToast(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    /**
     * 扫描成功后的处理
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        playBeepSoundAndVibrate();//震动
        resultData = result.getText();//接收扫描的结果
        if ("".equals(resultData) || null == resultData) {
            continuePreview();
            Toast.makeText(CaptureActivity2.this, "扫码失败", Toast.LENGTH_SHORT).show();
        } else {
            if (YrmUtils.isNumber(resultData)) {
                payChannel = YrmUtils.getPayChannel(resultData);
                if (payChannel == 0) {
                    ToastUtil.showShortToast("请扫正确的二维码");
                } else {
                    startScanPaying(resultData); //请求数据
                }
            } else {
                ToastUtil.showShortToast("请扫正确的二维码");
            }

            mInactivityTimer.onActivity();//停止扫描
        }
    }

    private void startScanPaying(String resultData) {
        JSONObject jsonRequest = new JSONObject();
        try {
            getNumLargeSmallLetter(32);
            List<PosPayBean> list = new ArrayList<PosPayBean>();
            StringBuilder sdb = new StringBuilder();
            list.add(new PosPayBean("payableAmount", amt));
            list.add(new PosPayBean("authcode", resultData));
            if (loginType.equals("0")) {
                list.add(new PosPayBean("merId", GetApplicationInfoUtil.getMerchantId()));
                list.add(new PosPayBean("storeId", mStoreId));
            } else {
                list.add(new PosPayBean("merId", SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("shopId")));
                list.add(new PosPayBean("storeId", mStoreId));
            }
            list.add(new PosPayBean("payChannel", payChannel + ""));
            list.add(new PosPayBean("merName", merName));
            list.add(new PosPayBean("tranCity", city));
            list.add(new PosPayBean("longitude", longitude + ""));
            list.add(new PosPayBean("latitude", latitude + ""));
            list.add(new PosPayBean("nonceStr", val));
            list.add(new PosPayBean("address", address));
            Collections.sort(list, new Comparator<PosPayBean>() {
                @Override
                public int compare(PosPayBean text, PosPayBean t1) {
                    return text.getKey().compareTo(t1.getKey());
                }
            });
            for (PosPayBean lists : list) {
                if (sdb.toString().equals("")) {
                    if (lists.getVaule() == null || lists.getVaule() == "") {
                        sdb.append("");
                    } else {
                        sdb.append(lists.getKey() + "=" + lists.getVaule());
                    }
                } else {
                    if (lists.getVaule() == null || lists.getVaule() == "") {
                        sdb.append("");
                    } else {
                        sdb.append("&" + lists.getKey() + "=" + lists.getVaule());
                    }
                }
            }
            LogUtil.d(sdb.toString() + "&key=" + GetApplicationInfoUtil.getMerchantId() + "排序");
            String name = SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANT_NAME);
            jsonRequest.put("authcode", resultData);
            jsonRequest.put("merName", merName);
            jsonRequest.put("payChannel", payChannel);
            jsonRequest.put("payableAmount", amt);
            jsonRequest.put("tranCity", city);
            jsonRequest.put("longitude", longitude + "");
            jsonRequest.put("latitude", latitude + "");
            jsonRequest.put("address", address);
            jsonRequest.put("nonceStr", val);

            String aa = sdb.toString() + "&key=" + GetApplicationInfoUtil.getMerchantId();
            if (("0").equals(loginType)) {
                jsonRequest.put("merId", GetApplicationInfoUtil.getMerchantId());
                jsonRequest.put("sign", MD5Util.md5(sdb.toString() + "&key=" + GetApplicationInfoUtil.getMerchantId()));
                jsonRequest.put("storeId", mStoreId);
            } else {
                String id = SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("shopId");
                Log.i("xjz111--id", id);
                Log.i("xjz111--sign", (sdb.toString() + "&key=" + SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("shopId")));
                jsonRequest.put("merId", SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("shopId"));
                jsonRequest.put("sign", MD5Util.md5(sdb.toString() + "&key=" + SharedPreferencesUtil.getInstance(CaptureActivity2.this).getKey("shopId")));
                jsonRequest.put("storeId", mStoreId);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(this, NetUrl.SCAN_PAY, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    status = response.getString("status");
                    message = response.getString("msg");
                    if ("1".equals(status)) {
                        ToastUtil.showShortToast(message);
                        CaptureActivity2.this.finish();
                    } else {
                        JSONObject orderInfo = response.optJSONObject("orderInfo");
                        String payName = orderInfo.optString("payName");
                        String orderNo = orderInfo.optString("orderNo");
                        String tradeStatus = orderInfo.optString("orderStatus");
                        String merName = orderInfo.optString("merName");
                        String orderDate = orderInfo.optString("orderDate");
                        Intent intent = new Intent(CaptureActivity2.this, SuccessfulReceipt.class);
                        intent.putExtra("payName", payName);
                        intent.putExtra("orderNo", orderNo);
                        intent.putExtra("tradeStatus", tradeStatus);//0代付款，1已付款，2订单被取消
                        intent.putExtra("merName", merName);
                        intent.putExtra("orderDate", orderDate);
                        intent.putExtra("payableAmount", amt);
                        intent.putExtra("status", status);
                        intent.putExtra("message", message);
                        intent.putExtra("city", city);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("address", address);
                        startActivity(intent);
                        CaptureActivity2.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
                hideProgressDialog();
            }
            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public void drawViewfinder() {
        mViewfinderView.drawViewfinder();
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView = (SurfaceView) findViewById(R.id.sv_preview);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            displayFrameworkBugMessageAndExit();
        }

        if (handler == null) {
            handler = new CaptureActivityHandler2(this, decodeFormats, characterSet);
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.app_name);
        builder.setTitle(title + "提示您");
        builder.setMessage("抱歉，相机需要一定权限，请到应用程序权限管理开启权限");
        builder.setPositiveButton("确定", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 数字与大小写字母混编字符串
     *
     * @param size
     * @return
     */
    public void getNumLargeSmallLetter(int size) {
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < size; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        Log.d("随机生成", val);
    }

    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description: 32位大写MD5
     */
    public String parseStrToMd5U32(String str) {
        reStr = parseStrToMd5L32(str);

        Log.d("加密", reStr);
        return reStr;
    }

    /**
     * @param str
     * @return
     * @Date: 2013-9-6
     * @Author: lulei
     * @Description: 32位小写MD5
     */
    public static String parseStrToMd5L32(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    /**
     * 继续扫描
     */
    private void continuePreview() {
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        super.onDestroy();
    }
}
