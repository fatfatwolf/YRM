package com.hybunion.yirongma.valuecard.view;

import android.os.Handler;

/**
 * Created by admin on 2017/9/29.
 * lyj
 */
public class CountDownTask {
    private Handler mHandler = new Handler();

    private OnTimingChangeListener mListener;

    /** 最大等待时间 */
    private long mMaxTryAgainTime = 6000;

    /** 倒计时间隔 */
    private long mCountDownInterval = 1000;

    private long mMilliseconds = mMaxTryAgainTime; // 剩余秒数

    private boolean mIsTryAgain = true; // 是否能够重新发送

    public CountDownTask(OnTimingChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 获取是否计时完毕
     * @return
     */
    public boolean isIsTryAgain() {
        return mIsTryAgain;
    }

    public long getMilliSeconds() {
        return mMilliseconds;
    }

    /**
     * 开始倒计时
     */
    public void startCountdown() {
        mMilliseconds = 0;
        mIsTryAgain = false;
        mHandler.post(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mListener != null) {
                if(mMaxTryAgainTime - mMilliseconds > 0) {  //如果减法大于0 那么说明下一次还有秒数
                    mListener.onTimingChange(mMaxTryAgainTime - mMilliseconds);
                    mHandler.postDelayed(this, mCountDownInterval);
                } else {
                    mListener.onTimingChange(0);
                }
            }
            mMilliseconds += mCountDownInterval; //递增时间
        }
    };

    /**
     * 取消定时
     */
    public void cancel() {
        mIsTryAgain = true;
        mHandler.removeCallbacks(runnable);
    }

    /**
     * 设置最大等待时间
     * @param time
     */
    public void setmMaxTryAgainTime(long time) {
        mMaxTryAgainTime = time;
    }

    /**
     * 设置倒计时间隔
     * @param countDownInterval
     */
    public void setCountDownInterval(int countDownInterval) {
        this.mCountDownInterval = countDownInterval;
    }

    public interface OnTimingChangeListener {

        void onTimingChange(long milliseconds);
    }
}
