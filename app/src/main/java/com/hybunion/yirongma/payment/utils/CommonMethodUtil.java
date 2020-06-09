package com.hybunion.yirongma.payment.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.UserScanActivity;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity;


/**
 * Created by lyf on 2017/5/22.
 */

public class CommonMethodUtil implements View.OnClickListener {
    private Context mContext;
    private Dialog mDialog;

    public void showPopWindow(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPopup = inflater.inflate(R.layout.select_shop, null);
        mDialog.setContentView(mPopup);
        mDialog.setCanceledOnTouchOutside(true);
        mPopup.findViewById(R.id.rl_popup_bg).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_company_user).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_business_user).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_personal_user).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_bind_pos).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_open_shop_person).setOnClickListener(this);
        mPopup.findViewById(R.id.btn_open_shop_company).setOnClickListener(this);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_company_user://企业商户
                Intent intent = new Intent(mContext, UserScanActivity.class);
                intent.putExtra("formType", "2");
                mContext.startActivity(intent);
                break;
//            case R.id.btn_business_user://个体工商用户
//                mContext.startActivity(new Intent(mContext, BusinessUserBaseActivity.class));
//                break;
            case R.id.btn_personal_user://个人
                Intent intent2 = new Intent(mContext, UserScanActivity.class);
                intent2.putExtra("formType", "1");
                mContext.startActivity(intent2);
                break;
            case R.id.btn_bind_pos://绑定pos
//                mContext.startActivity(new Intent(mContext, BindPosActivity.class));
                break;
            case R.id.btn_open_shop_person:
                Intent intent1 = new Intent(mContext, CaptureActivity.class);
                intent1.putExtra("bdType", "1");
                intent1.putExtra("flage", "0");
                intent1.putExtra("scanType", "0");//区分说明页扫码0/报单失败重新扫码1/主页补资料扫码2
                mContext.startActivity(intent1);
                break;
            case R.id.btn_open_shop_company:
                Intent intent3 = new Intent(mContext, CaptureActivity.class);
                intent3.putExtra("bdType", "2");
                intent3.putExtra("flage", "0");
                intent3.putExtra("scanType", "0");//区分说明页扫码0/报单失败重新扫码1/主页补资料扫码2
                mContext.startActivity(intent3);
                break;


            default:
                break;
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
