package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/12.
 */

public class UserScanActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.has_know)
    Button has_know;
    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_titlebar_back_title;
    @Bind(R.id.img_form_bg)
    ImageView img_form_bg;
    String formType;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.personal_user_scan_at;
    }

    @Override
    public void initView() {
        tv_titlebar_back_title.setText("说明");
        has_know.setOnClickListener(this);
        formType = getIntent().getStringExtra("formType");
        if ("1".equals(formType)){
            img_form_bg.setBackgroundResource(R.drawable.img_personal);//个人
        }else {
            img_form_bg.setBackgroundResource(R.drawable.img_company);//企业
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.has_know:
                Intent intent=new Intent(UserScanActivity.this, CaptureActivity.class);
                if ("1".equals(formType)){
                    intent.putExtra("bdType","1");
                }else {
                    intent.putExtra("bdType","2");
                }
                intent.putExtra("flage","0");
                intent.putExtra("scanType","0");//区分说明页扫码0/报单失败重新扫码1/主页补资料扫码2
                startActivity(intent);
                finish();
                break;
        }
    }

    @OnClick(R.id.ll_titlebar_back)
    public void goBack() {
        finish();
    }
}
