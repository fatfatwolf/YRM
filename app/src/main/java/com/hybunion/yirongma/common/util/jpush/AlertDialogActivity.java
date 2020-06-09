package com.hybunion.yirongma.common.util.jpush;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.hybunion.yirongma.payment.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Iron Man on 2016/7/28.
 * 用来呈现 AlertDialog 的Activity
 */
public class AlertDialogActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Log.i("SUN","bundle="+bundle.toString());

        //形成一个 AlertDialog
        MyDialog.Builder builder = new MyDialog.Builder(this);
        LogUtil.d(bundle.getString(JPushInterface.EXTRA_ALERT)+"推送信息");
        builder.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE))
               .setContent(bundle.getString(JPushInterface.EXTRA_ALERT))
               .setCancelButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       AlertDialogActivity.this.finish();
                   }
               })
               .setConfirmButton("好的", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       AlertDialogActivity.this.finish();

                   }
               });
        MyDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
