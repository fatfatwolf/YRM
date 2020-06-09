package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;


public class DialogMsg {

    private Context context;
    private View loadingView;
    private Dialog loading;
    private TextView tishi_msg;

    public DialogMsg(Context context) {
        super();
        this.context = context;
        loadingView = LayoutInflater.from(context).inflate(R.layout.code_view, null);
        InitData();
    }

    private void InitData() {
        loading = new Dialog(context, R.style.Dialog_image);
        loading.setContentView(loadingView);
        loading.setCancelable(true);
        loading.setCanceledOnTouchOutside(true);
        loading.getWindow().setGravity(Gravity.CENTER_VERTICAL);
       /* LinearLayout mLayout = (LinearLayout) loadingView.findViewById(R.id.ll_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loading != null) {
                    loading.dismiss();
                }
            }
        });*/
    }

    public void Set_Msg(String msg) {
        tishi_msg = (TextView) loadingView.findViewById(R.id.textView2);
        tishi_msg.setText(msg + "");
    }

    public void Set_NumMsg(String msg) {
        tishi_msg = (TextView) loadingView.findViewById(R.id.textView4);
        tishi_msg.setText(msg + "");
    }

    public ImageView onImageCode() {
        return (ImageView) loadingView.findViewById(R.id.imageView);
    }

    public RelativeLayout onImage() {
        return (RelativeLayout) loadingView.findViewById(R.id.relativeLayout);
    }

    public Button Send_code() {
        return (Button) loadingView.findViewById(R.id.bt_send_code);
    }

    public Dialog getLoading(){
        return loading;
    }
    public View getLoadView() {
        return loadingView;
    }

    public void Show() {
        if (loading != null) {
            loading.show();
        }
    }

    public void dimss() {
        if (loading != null) {
            loading.dismiss();
        }
    }

}
