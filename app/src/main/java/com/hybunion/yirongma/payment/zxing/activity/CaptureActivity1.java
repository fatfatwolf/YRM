package com.hybunion.yirongma.payment.zxing.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
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
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.UserScanActivity;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.zxing.camera.CameraManager;
import com.hybunion.yirongma.payment.zxing.decoding.CaptureActivityHandler1;
import com.hybunion.yirongma.payment.zxing.decoding.FinishListener;
import com.hybunion.yirongma.payment.zxing.decoding.InactivityTimer;
import com.hybunion.yirongma.payment.zxing.view.ViewfinderView;


import java.io.IOException;
import java.util.Vector;

/**
 * 扫一扫
 * Created by lyf on 2017/3/27.
 */
public class CaptureActivity1 extends BasicActivity implements SurfaceHolder.Callback {
    private LinearLayout ll_back;
    private CaptureActivityHandler1 handler;
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
    private TextView mTvFlash;
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean mIsLampOpen; // 用来标识当前闪光灯是否打开
    String content;//tid

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.camera;
    }

    @Override
    public void initView() {
        super.initView();
        tv_title = (TextView) findViewById(R.id.tv_head);
        tv_title.setText("绑定收款设备");
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        flage = getIntent().getStringExtra("flage");
        bdType = getIntent().getStringExtra("bdType");//1为对私，其他为对公
        scanType = getIntent().getStringExtra("scanType");
        inactivityTimer = new InactivityTimer(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        mTvFlash = (TextView) findViewById(R.id.flashLamp_camera);
        mTvFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera == null){
                    mCamera = CameraManager.get().getCamera();
                }
                if (mCamera!=null){
                    mParams = mCamera.getParameters();
                    if (!mIsLampOpen){
                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(mParams);
                        mCamera.startPreview();
                        mIsLampOpen = true;
                        mTvFlash.setText("关闭照明灯");
//                        ToastUtil.show("手电筒打开");
                    }else{
                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCamera.setParameters(mParams);
                        mIsLampOpen = false;
                        mTvFlash.setText("打开照明灯");
//                        ToastUtil.show("手电筒关闭");
                    }

                }
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {//三种报单情况
            @Override
            public void onClick(View v) {
                HRTApplication.finishActivity(UserScanActivity.class);
                CaptureActivity1.this.finish();
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
            Toast.makeText(CaptureActivity1.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (!TextUtils.isEmpty(resultString)) {
                    if (!TextUtils.isEmpty(resultString)) {
                        int starIndex = resultString.lastIndexOf("qr");
                        if (starIndex > 0) {
                            int index = resultString.indexOf(".", starIndex + 3);
                            if(index!=-1){
                                content = resultString.substring(starIndex + 3, resultString.indexOf(".", starIndex + 3));
                                int lastIndex = resultString.lastIndexOf("sign=");
                                if (lastIndex > 0){
                                    signContents = resultString.substring(lastIndex + 5);
                                }else {
                                    signContents = "";
                                }
                            }else {
                                content = resultString.substring(starIndex + 3, resultString.length());
                                signContents = "";
                            }

                            com.hybunion.yirongma.payment.utils.LogUtil.d(content+"tid"+"\n"+signContents+"签名");
//                            startScanPaying(content,signContents); //请求数据
                            Intent intent = new Intent();
                            intent.putExtra("signCon",signContents);
                            intent.putExtra("content",content);
                            CaptureActivity1.this.setResult(11,intent);
                            CaptureActivity1.this.finish();

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
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
        if (handler == null) {
            handler = new CaptureActivityHandler1(CaptureActivity1.this, decodeFormats, characterSet);
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