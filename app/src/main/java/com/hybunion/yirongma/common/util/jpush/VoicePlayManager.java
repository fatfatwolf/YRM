package com.hybunion.yirongma.common.util.jpush;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by android on 2017/10/26.
 */

public class VoicePlayManager {

    private static SoundPool sp;
    private static HashMap<String, Integer> mSoundPoolMap;
    private static Context mContext;
    private static VoicePlayManager voiceActivity;
    private static Vector<Integer> mKillSoundQueue = new Vector<Integer>();
    private static AudioManager mAudioManager;
    private static Handler mHandler = new Handler();
    private AtomicBoolean mIsLoadComplete = new AtomicBoolean(false);
    private String headSet = "1";//1拔出耳机 2:插入耳机

    private String headSetBlue = "1";
    private int currentVolume; // 当前系统音乐音量
    private int maxVolume; // 系统最大音乐音量
    private boolean mOtherPlaying = false;

    private VoicePlayManager() {

    }

    public static VoicePlayManager getInstance(Context mContext) {
        VoicePlayManager.mContext = mContext;

        if (voiceActivity == null) {
            voiceActivity = new VoicePlayManager();
        }
        return voiceActivity;
    }

    //初始化声音
    public void initSound() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new HashMap();
        mSoundPoolMap.put("rechare", sp.load(mContext, R.raw.value_card_rechare, 1));
        mSoundPoolMap.put("consume", sp.load(mContext, R.raw.value_card_shoukuan, 1));
        mSoundPoolMap.put("start", sp.load(mContext, R.raw.shoukuan, 1));
        mSoundPoolMap.put("零", sp.load(mContext, R.raw.tts_0, 1));
        mSoundPoolMap.put("一", sp.load(mContext, R.raw.tts_1, 1));
        mSoundPoolMap.put("二", sp.load(mContext, R.raw.tts_2, 1));
        mSoundPoolMap.put("三", sp.load(mContext, R.raw.tts_3, 1));
        mSoundPoolMap.put("四", sp.load(mContext, R.raw.tts_4, 1));
        mSoundPoolMap.put("五", sp.load(mContext, R.raw.tts_5, 1));
        mSoundPoolMap.put("六", sp.load(mContext, R.raw.tts_6, 1));
        mSoundPoolMap.put("七", sp.load(mContext, R.raw.tts_7, 1));
        mSoundPoolMap.put("八", sp.load(mContext, R.raw.tts_8, 1));
        mSoundPoolMap.put("九", sp.load(mContext, R.raw.tts_9, 1));
        mSoundPoolMap.put("十", sp.load(mContext, R.raw.tts_ten, 1));
        mSoundPoolMap.put("百", sp.load(mContext, R.raw.tts_hundred, 1));
        mSoundPoolMap.put("千", sp.load(mContext, R.raw.tts_thousand, 1));
        mSoundPoolMap.put("万", sp.load(mContext, R.raw.tts_ten_thousand, 1));
        mSoundPoolMap.put("亿", sp.load(mContext, R.raw.tts_hundred_million, 1));
        mSoundPoolMap.put(".", sp.load(mContext, R.raw.tts_dot, 1));
        mSoundPoolMap.put("end", sp.load(mContext, R.raw.tts_yuan, 1));

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool sound, int sampleId, int status) {
                //多次完成加载，但是只回调一次成功
                mIsLoadComplete.set(true);
            }
        });


    }

    private  boolean mAudioFocus;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {


        @Override
        public void onAudioFocusChange(int focusChange) {

            switch(focusChange){

                case AudioManager.AUDIOFOCUS_GAIN:


                mAudioFocus = true;

                break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:


                mAudioFocus = true;

                break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:


                mAudioFocus = true;

                break;

                case AudioManager.AUDIOFOCUS_LOSS:


                mAudioFocus = false;

//                stop();

                break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:


                mAudioFocus = false;

//                stop();

                break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:


                mAudioFocus = false;

//                stop();

                break;

                default :


                    break;

            }

        }

    };

    private void requestAudioFocus(){


        if(!mAudioFocus){

            int result = mAudioManager.requestAudioFocus(afChangeListener,

                    AudioManager.STREAM_MUSIC,//识别音乐播放

                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);//音频焦点

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                mAudioFocus = true;

            }else{


            }

        }

    }

    private void abandonAudioFocus() {


        if (mAudioFocus) {

            mAudioManager.abandonAudioFocus(afChangeListener);

            mAudioFocus = false;

        }

    }



    int type = 0;
    public void playMutilSounds(String redMsg) throws InterruptedException {

        if (!mIsLoadComplete.get()) {
            while (true) {
                if (mIsLoadComplete.get()) {
                    break;
                }
            }
        }



        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (currentVolume!=maxVolume){   // 当前音量不是最大音量，将当前音量存起来，播报完成后恢复。
            SharedPreferencesUtil.getInstance(mContext).putKey(SharedPConstant.CUR_VOL,currentVolume);
        }
        Log.i("xjz--声音音量","当前音量"+currentVolume+"      最大音量"+maxVolume);
        headSet = SharedPreferencesUtil.getInstance(mContext).getKey(SharedPConstant.HEAD_SET);
        if(TextUtils.isEmpty(headSet)){
            headSet = "1";
        }
        headSetBlue = SharedPreferencesUtil.getInstance(mContext).getKey(SharedPConstant.HEAD_SET_BULETOOTH);
        if(TextUtils.isEmpty(headSetBlue)){
            headSetBlue = "1";
        }
        if("1".equals(headSet) && "1".equals(headSetBlue)){
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
        }else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
        }

        String soundMsg;
        if ("元".equals(redMsg.substring(redMsg.length() - 1, redMsg.length()))) {
            if (redMsg.contains("众维码")) {
                type = 0;
                soundMsg = redMsg.replace("众维码收款", "").replace("元", "");
            } else if(redMsg.contains("储值卡购买")){
                type = 1;
                soundMsg = redMsg.replace("储值卡购买", "").replace("元", "");
            }else if(redMsg.contains("储值卡消费")){
                type = 2;
                soundMsg = redMsg.replace("储值卡消费", "").replace("元", "");
            }else {
                type = 3;
                soundMsg = redMsg.replace("收款", "").replace("元", "");
            }
            if (!TextUtils.isEmpty(soundMsg)) {
                LogUtil.d("播报金额：" + soundMsg);
                //获取数字字符串每个字符的ID
                final List<String> soundChs = TTSUtils.getInstance().getChs(soundMsg);
                int size = soundChs.size();
                if (size > 1 && "一".equals(soundChs.get(0)) && "十".equals(soundChs.get(1))) {
                    soundChs.remove(0);
                }
                if (type == 1) {
                    //储值卡购买
                    soundChs.add(0, "rechare");
                } else if (type == 2) {
                    //储值卡消费
                    soundChs.add(0, "consume");
                } else {
                    soundChs.add(0, "start");
                }
                soundChs.add(soundChs.size(), "end");


                synchronized (this) {
                    String voiceSwitch = SharedPreferencesUtil.getInstance(mContext).getKey("VoiceSwitch");
                    if (!TextUtils.isEmpty(voiceSwitch) && "0".equals(voiceSwitch)) return;
                    requestAudioFocus();
                    for (String key : soundChs) {
                        if (mSoundPoolMap.containsKey(key)) {
                            int soundId = sp.play(mSoundPoolMap.get(key),
                                    1.0f,// 左声道音量
                                    1.0f,// 右声道音量
                                    1, // 优先级
                                    0,// 循环播放次数
                                    1.0f);// 回放速度，该值在0.5-2.0之间 1为正常速度
                            //sleep for a while for SoundPool play
                            if ("start".equals(key)) {
                                Thread.sleep(1450);
                            } else if ("rechare".equals(key) || "consume".equals(key)) {
                                Thread.sleep(1350);
                            } else {
                                Thread.sleep(450);
                            }
                            LogUtil.d("soundId=" + soundId + "," + key + "====");
                            mKillSoundQueue.add(soundId);
                        }

                        // schedule the current sound to stop after set milliseconds
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (!mKillSoundQueue.isEmpty()) {

                                    sp.stop(mKillSoundQueue.firstElement());
                                }
                            }
                        }, 300);

                    }
                    abandonAudioFocus();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
                }
            }
        }
    }



}
