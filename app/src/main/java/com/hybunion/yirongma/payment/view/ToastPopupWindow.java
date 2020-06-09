package com.hybunion.yirongma.payment.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.hybunion.yirongma.R;

public class ToastPopupWindow extends PopupWindow {
    private Activity mContext;
    public ToastPopupWindow(Activity context,View view1,int view2) {
        super(context);
        mContext = context;
        initView(view1,view2);
    }


    public void initView(View view1,int view2){
        View view = LayoutInflater.from(mContext).inflate(view2, null);
        view.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        setContentView(view);
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mContext.getWindow().setAttributes(lp);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable(null,""));
        showAsDropDown(view1,-10,0);

    }
}
