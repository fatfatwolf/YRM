package com.hybunion.yirongma.payment.receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.LoginActivity;

// 首页注册的广播，用于通知 token 过期重新登录用
public class LoginReceiver extends BroadcastReceiver {
    private Dialog mDialog;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();

        if (OkUtils.getInstance().loginAction.equals(action)) {
            Activity activity = HRTApplication.getCurrentActivity();
            if (activity == null) {
                return;
            }
            mDialog = new Dialog(activity);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_dialog_relogin, null);
            TextView tvContent = view.findViewById(R.id.tv_msg);
            tvContent.setText("登录过期，请重新登录");
            view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context, LoginActivity.class);
                    context.startActivity(intent1);
                    HRTApplication.finishAllExceptActivity(LoginActivity.class);
                }
            });


//            View view = LayoutInflater.from(context).inflate(R.layout.dialog_jpush, null);
//            TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
//            TextView tvContent = view.findViewById(R.id.tvContent);
//            tvTitle.setText("提示");
//            tvContent.setText("登录过期，请重新登录！");
//            Button bt = view.findViewById(R.id.bt_sure);
//            bt.setText("重新登录");
//            bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent1 = new Intent(context, LoginActivity.class);
//                    context.startActivity(intent1);
//                    HRTApplication.finishAllExceptActivity(LoginActivity.class);
//                }
//            });
            mDialog.setContentView(view);
            mDialog.show();
        }


    }
}
