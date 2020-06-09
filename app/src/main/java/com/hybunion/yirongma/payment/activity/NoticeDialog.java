package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.Fragment.LMFPaymentFragment;


/**
 * 功能描述：公告弹框
 * 展示 Html 文本，用 WebView 不用 TextView
 */
public class NoticeDialog extends Dialog {

    private ImageView imgvDismiss;
    private TextView tvTitle;
//    private TextView tvContent;
    private CheckBox not_hint;
    private WebView mWebView;

    private String mTitle;
    private String mContent;

    private DialogClick dialogClick;
    private Context mContext;


    public NoticeDialog(Context context) {
        super(context, R.style.notice_dialog);
    }

    public NoticeDialog(Context context, String title, String content) {
        super(context, R.style.notice_dialog);
        this.mContext = context;
        this.mTitle = title;
        this.mContent = content;
    }

    public void setDialogClick(DialogClick dialogClick) {
        this.dialogClick = dialogClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dia_lmf_notice);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        setContentView(R.layout.dia_lmf_notice);

        mWebView = findViewById(R.id.webView_dia_lmf_notice);
        mWebView.getSettings().setSupportZoom(false);
//        tvContent = (TextView) findViewById(R.id.tv_notice_content);
        tvTitle = (TextView) findViewById(R.id.tv_notice_title);
        imgvDismiss = (ImageView) findViewById(R.id.imgv_dismiss);
        not_hint = (CheckBox) findViewById(R.id.cb_not_hint);

        if (TextUtils.isEmpty(mTitle)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitle);
        }
        if (TextUtils.isEmpty(mContent)) {
//            tvContent.setText("暂无公告内容");
            mWebView.loadDataWithBaseURL(null,"暂无公告内容","text/html","UTF-8",null);
        } else {
            mWebView.loadDataWithBaseURL(null,mContent,"text/html","UTF-8",null);
//            tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
//            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
//            tvContent.setText(Html.fromHtml(mContent));
        }

        imgvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (not_hint.isChecked()) {
                    SharedPreferencesUtil.getInstance(mContext).putBooleanKey("is_lmf_hint", true);
                }
                Intent intent = new Intent();
                intent.setAction(LMFPaymentFragment.KEY);
                intent.putExtra("key", "1");
                mContext.sendBroadcast(intent);
                dismiss();
            }
        });

    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface DialogClick {
        void onClick();
    }

}
