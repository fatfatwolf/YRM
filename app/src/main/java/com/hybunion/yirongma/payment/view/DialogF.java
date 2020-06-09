package com.hybunion.yirongma.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xiao on 2016/6/20.
 */
public class DialogF extends Dialog {

    private static int default_width = 160; //默认宽度
    private static int default_height = 120;//默认高度

    public DialogF(Context context, View layout, int style ,int flag) {
        this(context, default_width, default_height, layout, style , flag);
    }

    public DialogF(Context context, int width, int height, View layout, int style , int flag) {
        super(context, style);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layout);
        Window window = getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams params = window.getAttributes();
        if (flag==0){
            params.gravity = Gravity.CENTER;
        }else if (flag==1){
            params.gravity = Gravity.TOP;
        }

        window.setAttributes(params);
    }


}
