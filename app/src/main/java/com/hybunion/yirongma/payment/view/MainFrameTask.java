package com.hybunion.yirongma.payment.view;

import android.app.Activity;

/**
 * 创建自定义进度条的类
 */
public class MainFrameTask {
    private Activity mainFrame = null;
    private CustomProgressDialog progressDialog = null;

    public MainFrameTask(Activity mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void startProgressDialog(String values) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(mainFrame);
            progressDialog.setMessage(values);

        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}