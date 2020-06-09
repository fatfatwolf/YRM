package com.hybunion.yirongma.payment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.activity.UserAgreementActivity;


/**
 * 功能描述：新版协议弹框
 * 编写人： xjz
 * 创建时间：201/2/25
 */
public class NoticeDialog2 extends Dialog {

    private TextView tvContent;

    private String mTitle;
    private String mContent;

    private DialogClick dialogClick;
    boolean isClick = true;
    private ImageView iv_agree;
    private TextView mTvRead;
    private Button bt_notice2_sure;
    private TextView tv_procotol;

    private TextView tv_notice_content2;


    public NoticeDialog2(Context context) {
        super(context, R.style.notice_dialog);
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
        setContentView(R.layout.dia_lmf_notice2);

        tvContent = (TextView) findViewById(R.id.tv_notice_content);
        iv_agree = (ImageView) findViewById(R.id.iv_agree);
        mTvRead = findViewById(R.id.tv_read);
        bt_notice2_sure = (Button) findViewById(R.id.bt_notice2_sure);
        tv_procotol = (TextView) findViewById(R.id.tv_procotol);
        tv_notice_content2 = findViewById(R.id.tv_notice_content2);

        tv_procotol.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_procotol.setMovementMethod(LinkMovementMethod.getInstance());
//        tv_procotol.setText(Html.fromHtml(mContent));
        String str = "         您好，我们已为您成功创建账户，为确保您的账户资金安全及商户信息准确性，请及时核实【结算银行卡】【签约信息】【基本资料】等信息，如有问题，请及时联系业务员，感谢合作！";
        SpannableString  spannableString = new SpannableString(str);
        StyleSpan styleSpan_B  = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan_B, 24, 43, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_notice_content2.setText(spannableString);
//        tv_notice_content2.setText(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));

        tv_procotol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //等待H5
                Intent intent = new Intent(getContext(),LMFRedRainActivity.class);
                intent.putExtra("webViewUrl","4");
                getContext().startActivity(intent);
            }
        });
        iv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick){
                    iv_agree.setImageResource(R.drawable.symbolsno);
                    isClick = false;
                }else {
                    iv_agree.setImageResource(R.drawable.symbolsyes);
                    isClick = true;
                }
            }
        });

        mTvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick){
                    iv_agree.setImageResource(R.drawable.symbolsno);
                    isClick = false;
                }else {
                    iv_agree.setImageResource(R.drawable.symbolsyes);
                    isClick = true;
                }
            }
        });


        bt_notice2_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClick){
                    if(clickListener!=null){
                        dismiss();
                        clickListener.onButtonClick();
                    }
                }else {
                    ToastUtil.show("请阅读并勾选协议");
                }
            }
        });


    }
    public OnButtonClickListener clickListener;
    public interface OnButtonClickListener{
        public void onButtonClick();
    }

    public void setButtonClickListener(OnButtonClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                ToastUtil.show("请阅读并同意协议后点击确定按钮");
            }
        return isClick;
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
