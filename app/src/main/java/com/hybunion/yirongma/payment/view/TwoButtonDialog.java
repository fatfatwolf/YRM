package com.hybunion.yirongma.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;


/**
 * Created by M on 2016/11/15.
 */

public class TwoButtonDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showRightBtn = false;
    private boolean showLeftBtn = false;

    private View mView;

    public TwoButtonDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public TwoButtonDialog builder() {
        
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_twobutton, null);

        // 根布局  
        lLayout_bg = (LinearLayout) view.findViewById(R.id.dialog_layout);
        // 标题  
        txt_title = (TextView) view.findViewById(R.id.tv_title);
        txt_title.setVisibility(View.GONE);
        // 消息  
        txt_msg = (TextView) view.findViewById(R.id.tv_message);
        txt_msg.setVisibility(View.GONE);
        // 左按钮  
        btn_neg = (Button) view.findViewById(R.id.tv_ok_left);
        btn_neg.setVisibility(View.GONE);
        // 右按钮  
        btn_pos = (Button) view.findViewById(R.id.tv_ok_right);
        btn_pos.setVisibility(View.GONE);

        mView = view.findViewById(R.id.view_line);
        mView.setVisibility(View.GONE);

        dialog = new Dialog(context, R.style.MyDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小 0.8宽  
        // 使用FrameLayout.LayoutParams设置参数  
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.8), FrameLayout.LayoutParams.WRAP_CONTENT));

        return this;
    }

    /**
     * 标题 
     *
     * @param title
     * @return
     */
    public TwoButtonDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("温馨提示");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    /**
     * 内容 
     * @param msg
     * @return
     */
    public TwoButtonDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("网路连接不佳");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    /**
     * 内容 
     * @param id
     * @return
     */
    public TwoButtonDialog setMsg(int id) {
        showMsg = true;
        if (id == 0) {
            txt_msg.setText("网路连接不佳");
        } else {
            txt_msg.setText(context.getResources().getString(id));
        }
        return this;
    }

    /**
     * 取消窗口 
     *
     * @param cancel
     * @return
     */
    public TwoButtonDialog setCancelable(boolean cancel) {

        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 确认按钮 
     *
     * @param text
     * @param listener
     * @return
     */
    public TwoButtonDialog setRightButton(String text,
                                          final View.OnClickListener listener) {
        showRightBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onClick(v);

            }
        });
        return this;
    }

    /**
     * 取消按钮 
     *
     * @param text
     * @param listener
     * @return
     */
    public TwoButtonDialog setLeftButton(String text,
                                         final View.OnClickListener listener) {
        showLeftBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 设置布局文件 
     */
    private void setLayout() {

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }
        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if (showRightBtn  ) {
            btn_pos.setVisibility(View.VISIBLE);

        }
        if(showLeftBtn){
            btn_neg.setVisibility(View.VISIBLE);
        }

        if(showLeftBtn && showRightBtn){
            mView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 显示dialog视图 
     */
    public void show() {
        setLayout();
        dialog.show();
    }
}  

