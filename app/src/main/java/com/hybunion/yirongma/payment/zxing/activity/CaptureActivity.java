package com.hybunion.yirongma.payment.zxing.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.BindReceiptCodeBean;
import com.hybunion.yirongma.payment.bean.UplodeBandCordImageBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;

import com.hybunion.yirongma.payment.activity.UserScanActivity;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.TwoButtonDialog;
import com.hybunion.yirongma.payment.zxing.camera.CameraManager;
import com.hybunion.yirongma.payment.zxing.decoding.CaptureActivityHandler;
import com.hybunion.yirongma.payment.zxing.decoding.FinishListener;
import com.hybunion.yirongma.payment.zxing.decoding.InactivityTimer;
import com.hybunion.yirongma.payment.zxing.view.ViewfinderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 扫一扫
 * Created by lyf on 2017/3/27.
 */


public class CaptureActivity extends BasicActivity implements SurfaceHolder.Callback {
    private LinearLayout ll_back;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet,signContents;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String resultString;//扫面返回结果
    private TextView tv_title;
    private String  status, message,flage,bdType,scanType,shopName;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.camera;
    }

    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        switch (type) {
            case BIND_RECEIPT_CODE:

                break;
            case UPLODEBANDCARDIMG:

                break;
        }
    }

    private void personalPresenter(){
        Map<String, String> mMap = new HashMap<>();
        mMap.put("mid", SavedInfoUtil.getMid(this));
        mMap.put("rname",SharedPreferencesUtil.getInstance(this).getKey("business"));

        Map<String, File> path = new HashMap<>();
        String pic0 = SharedUtil.getInstance(this).getString("pic0");
        if (!TextUtils.isEmpty(pic0)) {
            path.put("registryUpLoadFile", new File(pic0));//店面门头照
        }
        String pic1 = SharedUtil.getInstance(this).getString("pic1");
        if (!TextUtils.isEmpty(pic1)) {
            path.put("photoUpLoadFile", new File(pic1));//店内经营照片
        }
        String pic2 = SharedUtil.getInstance(this).getString("pic2");
        if (!TextUtils.isEmpty(pic2)) {
            path.put("materialUpLoad7File", new File(pic2));//众维码桌牌的收银台照片
        }

        OkUtils.getInstance().postFile(this, NetUrl.UPLODEBANDCODEIMG, mMap, path, new MyOkCallback<UplodeBandCordImageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(UplodeBandCordImageBean uplodePicBean) {
                if (uplodePicBean.isSuccess()){
                    ToastUtil.showShortToast(uplodePicBean.getMsg());
                    finish();
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("success", "1");
                    setResult(RESULT_OK, intent);
                    ToastUtil.showShortToast(uplodePicBean.getMsg());
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return UplodeBandCordImageBean.class;
            }
        });



    }



    @Override
    public void initView() {
        super.initView();
        tv_title = (TextView) findViewById(R.id.tv_head);
        tv_title.setText("绑定收款码");
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        flage = getIntent().getStringExtra("flage");
        bdType = getIntent().getStringExtra("bdType");//1为对私，其他为对公
        scanType = getIntent().getStringExtra("scanType");
        inactivityTimer = new InactivityTimer(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {//三种报单情况
            @Override
            public void onClick(View v) {
                HRTApplication.finishActivity(UserScanActivity.class);
                CaptureActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);

        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
       CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }
    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        Log.e("PL", result.getText());
        playBeepSoundAndVibrate();//震动
        resultString = result.getText();//接收扫描返回结果
        com.hybunion.yirongma.payment.utils.LogUtil.d(resultString+"接收扫描返回结果");
        if ("".equals(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (!TextUtils.isEmpty(resultString)) {
                    if (!TextUtils.isEmpty(resultString)) {
                        int starIndex = resultString.indexOf("qr");
                        if (starIndex > 0) {
                            String content = resultString.substring(starIndex + 3, resultString.indexOf(".", starIndex + 3));
                            int lastIndex = resultString.lastIndexOf("sign=");
                            if (lastIndex > 0){
                                signContents = resultString.substring(lastIndex + 5);
                            }else {
                                signContents = "";
                            }
                            com.hybunion.yirongma.payment.utils.LogUtil.d(content+"tid"+"\n"+signContents+"签名");
                            startScanPaying(content,signContents); //请求数据
                        } else {
                            Toast.makeText(this.getApplicationContext(), "请扫描正确二维码", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startScanPaying(final String resultData ,final String signContents) {
        JSONObject bodyJson = new JSONObject();
        try {
            bodyJson.put("tid",resultData);
            bodyJson.put("merId",SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.CHECK_TID, bodyJson, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    status = response.getString("status");
                    message = response.getString("msg");
                    if ("0".equals(status)){
                        LogUtil.d(resultData+"===code");
                        SharedPreferencesUtil.getInstance(CaptureActivity.this).putKey("resultData", resultData);
                        SharedPreferencesUtil.getInstance(CaptureActivity.this).putKey("signContents", signContents);
                        if ("0".equals(flage)) {//从报单页进入，填写报单信息
                            finish();
                        }else {//已报单没绑码
                            bindReceiptCode();
                        }
                    }else {
                        showDialog(message);
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
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    private void bindReceiptCode(){
        String code = SharedPreferencesUtil.getInstance(CaptureActivity.this).getKey("resultData");
        String mid = SharedUtil.getInstance(CaptureActivity.this).getString(Constants.MID);
        shopName =SharedPreferencesUtil.getInstance(CaptureActivity.this).getKey("business");
        JSONObject jb = new JSONObject();
        Map map = new HashMap();
        map.put("mid",mid);
        map.put("qrtid", code);
        map.put("qrPwd",signContents);
        map.put("Shopname",shopName);

        OkUtils.getInstance().postFormData(this, NetUrl.BIND_RECEIPT_CODE, map, new MyOkCallback<BindReceiptCodeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BindReceiptCodeBean baseBean) {
                if (baseBean.isSuccess()) {
                    SharedPreferencesUtil.getInstance(CaptureActivity.this).putKey("isJhMidBindTid", "0");
                    if ("2".equals(scanType)) {
                        personalPresenter();
                    }else {
                        startActivity(new Intent(CaptureActivity.this, LMFMainActivity.class));
                        HRTToast.showToast(baseBean.getMsg(), CaptureActivity.this);
                        finish();
                    }
                } else {
                    SharedPreferencesUtil.getInstance(CaptureActivity.this).putKey("isJhMidBindTid", "1");
                    HRTToast.showToast(baseBean.getMsg(), CaptureActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return BindReceiptCodeBean.class;
            }
        });


    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
           CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats, characterSet);
        }
    }
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.app_name);
        builder.setTitle(title+"提示您");
        builder.setMessage("抱歉，相机可能需要一定权限，请到应用程序权限管理开启权限");
        builder.setPositiveButton("确定", new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

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

    private static final long VIBRATE_DURATION = 200L;

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
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    /**
     * 弹出的对话框
     */
    private void showDialog(String message) {
        TwoButtonDialog tipDialog = new TwoButtonDialog(CaptureActivity.this).builder()
                .setTitle(message)
                .setCancelable(false)
                .setRightButton("返回首页", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HRTApplication.finishActivity(UserScanActivity.class);
                        finish();
                    }
                });
        tipDialog.show();
    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
          //这里重写返回键
            HRTApplication.finishActivity(UserScanActivity.class);
            finish();
            return true;
        }
    return false;
    }
}