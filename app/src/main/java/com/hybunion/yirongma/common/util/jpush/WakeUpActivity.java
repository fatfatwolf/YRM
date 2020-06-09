package com.hybunion.yirongma.common.util.jpush;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Iron Man on 2016/7/28.
 * 使用 Activity 唤醒屏幕
 */
public class WakeUpActivity extends Activity {
    private MReceiver receiver;
    private PowerManager.WakeLock wakeLock;
    private PowerManager powerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON); //监听打开锁屏界面
        receiver = new MReceiver();
        registerReceiver(receiver, filter);

        //唤醒屏幕
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager.isScreenOn()) { //屏幕亮着
            finish();
            return;

        }
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "target");
        wakeLock.acquire();//强制亮屏


    }

    //使用广播接收者来结束 Activity
    private class MReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_SCREEN_ON:
                    Log.i("SUN", "屏幕状态：解锁了");
                    if (wakeLock != null)
                        wakeLock.release();//释放亮屏
                    WakeUpActivity.this.finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除广播注册
        unregisterReceiver(receiver);
    }
}
