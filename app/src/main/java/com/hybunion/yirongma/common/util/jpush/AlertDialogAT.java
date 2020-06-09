package com.hybunion.yirongma.common.util.jpush;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.hybunion.yirongma.payment.activity.NewFeedBackActivity;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2018/4/2.
 */

public class AlertDialogAT extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        //形成一个 AlertDialog
        MyDialog.Builder builder = new MyDialog.Builder(this);
        LogUtil.d(bundle.getString(JPushInterface.EXTRA_ALERT)+"推送信息");
        builder.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE))
                .setContent(bundle.getString(JPushInterface.EXTRA_ALERT))
               /* .setCancelButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesUtil.getInstance(AlertDialogAT.this).putKey("flag","2");
                        dialogInterface.dismiss();
                        AlertDialogAT.this.finish();
                    }
                })*/
                .setConfirmButton("去申诉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent1=new Intent(AlertDialogAT.this, NewFeedBackActivity.class);
                        intent1.putExtra("flag","1");
                        AlertDialogAT.this.startActivity(intent1);
                        SharedPreferencesUtil.getInstance(AlertDialogAT.this).putKey("flag","2");
                        dialogInterface.dismiss();
                        AlertDialogAT.this.finish();

                    }
                });
        MyDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
