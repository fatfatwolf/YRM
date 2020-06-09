package com.hybunion.yirongma.payment.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;

public class HeadSetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if(BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //没链接上
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET_BULETOOTH,"1");//1为未连接蓝牙，2为连接蓝牙耳机状态
                Log.i("xjz--耳机状态","未连接上蓝牙");
            }else{
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET_BULETOOTH,"2");
                Log.i("xjz--耳机状态","连接上蓝牙");
            }
        }else if(intent.hasExtra("state")){
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if(BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //没链接上
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET_BULETOOTH,"1");
                Log.i("xjz--耳机状态","未连接上蓝牙");
            }else{
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET_BULETOOTH,"2");
                Log.i("xjz--耳机状态","连接上蓝牙");
            }
            if(intent.getIntExtra("state",2) == 0){
                //拔出
                Log.i("xjz--耳机状态","有线耳机拔出");
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET,"1");//1为拔出，2为插入耳机状态

            }else {
                //插入
                Log.i("xjz--耳机状态","有线耳机插入");
                SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HEAD_SET,"2");//1为拔出，2为插入耳机状态
            }
        }else {
            Log.i("xjz--耳机状态","未检测 action="+action);
        }
    }
}
