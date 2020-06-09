package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.VoiceSettingBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.FloatWindowView.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.TitleBar;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 收款语音设置 界面
 */

public class VoiceSettingActivity extends BasicActivity {
    @Bind(R.id.seekBar_voice_setting_activity)
    SeekBar mSeekBar;
    @Bind(R.id.titleBar_voice_setting_activity)
    TitleBar mTitleBar;
    @Bind(R.id.tv_mostSlow)
    TextView mTvMostSlow;
    @Bind(R.id.tv_moreSlow)
    TextView mTvMoreSlow;
    @Bind(R.id.tv_normal)
    TextView mTvNormal;
    @Bind(R.id.tv_moreFast)
    TextView mTvMoreFast;
    @Bind(R.id.tv_mostFast)
    TextView mTvMostFast;
    @Bind(R.id.switch_button)
    Switch mSwitch;
    @Bind(R.id.voice_check_switch_button)
    Switch mVoiceSwitch;  // 悬浮球开关
    @Bind(R.id.img_default)
    ImageView mImgDefault;
    @Bind(R.id.rv_voice_detection)
    RelativeLayout rv_voice_detection;
    @Bind(R.id.rv_broad_ball)
    RelativeLayout rv_broad_ball;
    @Bind(R.id.setting_help_parent)
    RelativeLayout setting_help_parent;
    @Bind(R.id.rv_plug_in)
    RelativeLayout rv_plug_in;
    @Bind(R.id.switch_chajian)
    Switch switch_chajian;

    private boolean mIsDefault = true;
    private boolean isFirst = true;
    private String openStatus;
    private String yxBeiSaoBoBao;
    private boolean isKaiQi;
    private boolean isGetInterface = false;
    private String loginType;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_voice_setting_activity;
    }

    @Override
    public void initView() {
        super.initView();
        yxBeiSaoBoBao = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.YX_BSBB);//0开启  1关闭
        loginType = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGINTYPE);
        if("0".equals(loginType)){
            rv_plug_in.setVisibility(View.VISIBLE);
        }else {
            rv_plug_in.setVisibility(View.GONE);
        }
        String voiceSwitch = SharedPreferencesUtil.getInstance(this).getKey("VoiceSwitch");  // 是否开启语音播报
        int floatIsOpen = SharedPreferencesUtil.getInstance(this).getIntKey1(SharedPConstant.FLOAT_IS_SHOW); // 是否开启悬浮球
        if (TextUtils.isEmpty(voiceSwitch) || "1".equals(voiceSwitch)) { // 第一次进入app，默认播报
            mSwitch.setChecked(true);
            rv_voice_detection.setVisibility(View.VISIBLE);
            rv_broad_ball.setVisibility(View.VISIBLE);
            setting_help_parent.setVisibility(View.VISIBLE);
            mVoiceSwitch.setChecked(floatIsOpen == 1 ? true : false);

        } else {
            mSwitch.setChecked(false);
            rv_voice_detection.setVisibility(View.GONE);
            rv_broad_ball.setVisibility(View.GONE);
            setting_help_parent.setVisibility(View.GONE);
        }

        // 关闭语音播报后，保持悬浮球开关状态不变
        // 打开语音播报后，判断悬浮球开启关闭状态，设置悬浮球开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    ToastUtil.show("您开启了语音播报");
                    SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).putKey("VoiceSwitch", "1");
                    rv_voice_detection.setVisibility(View.VISIBLE);
                    rv_broad_ball.setVisibility(View.VISIBLE);
                    setting_help_parent.setVisibility(View.VISIBLE);
                    int floatIsOpen = SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).getIntKey1(SharedPConstant.FLOAT_IS_SHOW);
                    mVoiceSwitch.setChecked(floatIsOpen == 1 ? true : false);
                } else {
                    ToastUtil.show("您关闭了语音播报");
                    SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).putKey("VoiceSwitch", "2");
                    rv_voice_detection.setVisibility(View.GONE);
                    rv_broad_ball.setVisibility(View.GONE);
                    setting_help_parent.setVisibility(View.GONE);
                    // 关闭语音播报，也关闭悬浮球
                }
            }
        });
        // 语音检测 开关
        mVoiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).putKey(SharedPConstant.FLOAT_IS_SHOW, 1);
                } else {
                    SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).putKey(SharedPConstant.FLOAT_IS_SHOW, 0);
                }
            }
        });

        if("0".equals(yxBeiSaoBoBao)){
            isKaiQi = true;
            switch_chajian.setChecked(true);
        }else {
            isKaiQi = false;
            switch_chajian.setChecked(false);
        }
        switch_chajian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isGetInterface)  return;
                if(isChecked){
                    String openStatus = "0";
                    broadScan(openStatus);
                }else {
                    String openStatus = "1";
                    broadScan(openStatus);
                }
            }
        });

        float speed = SharedPreferencesUtil.getInstance(this).getFloatKey(SharedPConstant.VOICE_SPEED);
        if (speed != 0 && speed != 1) {
            mImgDefault.setImageResource(R.drawable.unselected_img);
            mIsDefault = false;
            handleSpeed(speed);
        }

        mSeekBar.getThumb().setColorFilter(Color.parseColor("#F94B35"), PorterDuff.Mode.SRC_ATOP);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                // 用户抬手的时候，当前 progress 和哪个点最接近，就将 progress 设置成那个点的进度。
                if (progress >= 0 && progress <= 12) {
                    handleTextView(0);
                } else if (progress > 12 && progress <= 37) {
                    handleTextView(25);
                } else if (progress > 37 && progress <= 62) {
                    handleTextView(50);
                } else if (progress > 62 && progress <= 87) {
                    handleTextView(75);
                } else handleTextView(100);
            }
        });

    }

    public void broadScan(final String openStatus){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("merId", SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).getKey(SharedPConstant.MERCHANT_ID));
            jsonRequest.put("openStatus",openStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = NetUrl.BEI_SAO_BOBAO;
        OkUtils.getInstance().post(VoiceSettingActivity.this, url, jsonRequest, new MyOkCallback<VoiceSettingBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(VoiceSettingBean voiceSettingBean) {
                if("0".equals(voiceSettingBean.getStatus())){
                    isGetInterface = false;
                    isKaiQi = !isKaiQi;
                    SharedPreferencesUtil.getInstance(VoiceSettingActivity.this).putKey(SharedPConstant.YX_BSBB,openStatus);
                    ToastUtil.show("设置成功");
                }else {
                    isGetInterface = true;
                    switch_chajian.setChecked(isKaiQi);
                    ToastUtil.show("设置失败");
                }
            }

            @Override
            public void onError(Exception e) {
                isGetInterface = true;
                switch_chajian.setChecked(isKaiQi);
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return VoiceSettingBean.class;
            }
        });
    }

    MediaPlayer mediaPlayer;
    private void handleTextView(int progress) {
        if (mSeekBar != null)
            mSeekBar.setProgress(progress);
        mTvMostSlow.setTextColor(Color.parseColor("#B6BDD0"));
        mTvMoreSlow.setTextColor(Color.parseColor("#B6BDD0"));
        mTvNormal.setTextColor(Color.parseColor("#B6BDD0"));
        mTvMoreFast.setTextColor(Color.parseColor("#B6BDD0"));
        mTvMostFast.setTextColor(Color.parseColor("#B6BDD0"));
        float speed = 0.6f;
        switch (progress) {
            case 0:
                mTvMostSlow.setTextColor(Color.parseColor("#F94B35"));
                speed = 0.7f;
                setVoiceSpeed(speed);
                mIsDefault = false;
                break;
            case 25:
                mTvMoreSlow.setTextColor(Color.parseColor("#F94B35"));
                speed = 0.8f;
                setVoiceSpeed(speed);
                mIsDefault = false;
                break;
            case 50:
                mTvNormal.setTextColor(Color.parseColor("#F94B35"));
                speed = 1;
                setVoiceSpeed(speed);
                mIsDefault = true;
                mImgDefault.setImageResource(R.drawable.selected_img);
                break;
            case 75:
                mTvMoreFast.setTextColor(Color.parseColor("#F94B35"));
                speed = 1.2f;
                setVoiceSpeed(speed);
                mIsDefault = false;
                break;
            case 100:
                mTvMostFast.setTextColor(Color.parseColor("#F94B35"));
                speed = 1.4f;
                setVoiceSpeed(speed);
                mIsDefault = false;
                break;
        }
        isFirst = false;
        mImgDefault.setImageResource(mIsDefault ? R.drawable.selected_img : R.drawable.unselected_img);
        SharedPreferencesUtil.getInstance(this).putKey(SharedPConstant.VOICE_SPEED, speed);
    }

    public void setVoiceSpeed(float speed){
        if(isFirst){
            return;
        }
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.audio_success);
        if (afd == null)
            return;
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            if (Build.VERSION.SDK_INT>=23){
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            }
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer.release();
        }
    }

    @OnClick(R.id.defaultParent)
    public void defaultParentClick() {
        mImgDefault.setImageResource(mIsDefault ? R.drawable.unselected_img : R.drawable.selected_img);
        if (!mIsDefault) {  // 当前不是默认值，点击后恢复默认值
            handleTextView(50);
        }
        mIsDefault = !mIsDefault;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
             mediaPlayer.release();
    }

    // 查看手机设置方法 help
    @OnClick(R.id.setting_help_parent)
    public void voiceDeltction() {
        Intent intent = new Intent(this, LMFRedRainActivity.class);
        intent.putExtra("webViewUrl", "3");
        this.startActivity(intent);
    }

    private void handleSpeed(float speed) {
        switch (speed + "") {
            case "0.6":
                handleTextView(0);
                break;
            case "0.8":
                handleTextView(25);
                break;
            case "1":
                handleTextView(50);
                break;
            case "1.2":
                handleTextView(75);
                break;
            case "1.4":
                handleTextView(100);
                break;


        }
    }

    private MyBottomDialog mDialog;
    // 立即检测按钮，和点击悬浮球效果相同。
    @OnClick(R.id.check_it_now)
    public void checkItNow(){
        mDialog = new MyBottomDialog(this);
        mDialog.setOwnerActivity(this);
        mDialog.showThisDialog();



    }

}
