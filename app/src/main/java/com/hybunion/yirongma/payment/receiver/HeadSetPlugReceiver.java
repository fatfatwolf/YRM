package com.hybunion.yirongma.payment.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadSetPlugReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)){
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if(BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //没链接上
                Log.i("xjz--蓝牙耳机连接状态","未连接上蓝牙");
            }else{
                //连接上
                Log.i("xjz--蓝牙耳机连接状态","连接上蓝牙");
            }

        }
    }
}
