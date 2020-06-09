package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MemberManageBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;

public class MemberManageActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.rv_quality_member)
    RelativeLayout rv_quality_member;
    @Bind(R.id.iv_member_annoation)
    ImageView iv_member_annoation;
    @Bind(R.id.rv_discount_coupon)
    RelativeLayout rv_discount_coupon;
    @Bind(R.id.rv_value_card)
    RelativeLayout rv_value_card;
    @Bind(R.id.rv_send_message)
    RelativeLayout rv_send_message;
    @Bind(R.id.ll_member_details)
    LinearLayout ll_member_details;
    @Bind(R.id.tv_member_count)
    TextView tv_member_count;
    @Bind(R.id.tv_fans_count)
    TextView tv_fans_count;
    @Bind(R.id.rv_member_annoation)
    RelativeLayout rv_member_annoation;

    private String merId,storeId,loginType,couponAdmin;
    String vipCount = "0";
    private String vcSale;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_member_manage;
    }

    @Override
    public void initView() {
        super.initView();
        couponAdmin = SharedPreferencesUtil.getInstance(MemberManageActivity.this).getKey("couponAdmin");
        loginType = SharedPreferencesUtil.getInstance(MemberManageActivity.this).getKey("loginType");
        vcSale = SharedPreferencesUtil.getInstance(MemberManageActivity.this).getKey(SharedPConstant.VC_SALE);
        if (loginType.equals("0")) {
            merId =  SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
            storeId = "";
        } else {
            merId =  SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId");
            storeId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId");
        }
        rv_member_annoation.setOnClickListener(this);
        rv_discount_coupon.setOnClickListener(this);
        rv_value_card.setOnClickListener(this);
        rv_send_message.setOnClickListener(this);
        ll_member_details.setOnClickListener(this);
    }


    @Override
    protected void canDo() {
        super.canDo();
        setMemberCount(merId,loginType,storeId);
    }

    public void setMemberCount(String merId,String loginType, String storeId){
        String url = NetUrl.MEMBER_COUNT;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("merId",merId);
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("loginType",loginType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(MemberManageActivity.this, url, jsonRequest, new MyOkCallback<MemberManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MemberManageBean memberManageBean) {
                if(memberManageBean.getStatus().equals("0")){
                    vipCount = memberManageBean.yzVipCount;
                    tv_member_count.setText(memberManageBean.yzVipCount+"人");
                    tv_fans_count.setText(memberManageBean.fansCount+"人");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return MemberManageBean.class;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rv_member_annoation:
                showMyDialog2();
                break;
            case R.id.rv_discount_coupon:
                if(loginType.equals("0")){
                    Intent intent1 = new Intent(MemberManageActivity.this,MakeNoteActivity.class);
                    intent1.putExtra("memberType","1");
                    startActivity(intent1);
                }else if(loginType.equals("1") && "1".equals(couponAdmin)){
                    Intent intent1 = new Intent(MemberManageActivity.this,MakeNoteActivity.class);
                    intent1.putExtra("memberType","1");
                    startActivity(intent1);
                }else {
                    ToastUtil.show("暂无权限");
                }

                break;
            case R.id.rv_value_card:
                if("0".equals(loginType)){ //老板
                    if("2".equals(vcSale)){
                        Intent intent1 = new Intent(MemberManageActivity.this, HuiValueCardActivity.class);
                        startActivity(intent1);
                    }else {
                        Intent intent1 = new Intent(MemberManageActivity.this, HuiValueSuccessActivity.class);
                        startActivity(intent1);
                    }
                }else if("1".equals(loginType)){
                    if("2".equals(vcSale)){
                        ToastUtil.show("暂未加入商圈门店");
//                        Intent intent1 = new Intent(getActivity(), HuiValueCardActivity.class);
//                        startActivity(intent1);
                    }else {
                        Intent intent1 = new Intent(MemberManageActivity.this, HuiValueSuccessActivity.class);
                        startActivity(intent1);
                    }
                }else {
                    ToastUtil.show("暂无权限");
                }
                break;
            case R.id.rv_send_message:
                WechatAppletPushActivity.start(MemberManageActivity.this);
//                Intent intent2 = new Intent(MemberManageActivity.this,SendMsgResultActivity.class);
//                startActivity(intent2);
                break;
            case R.id.ll_member_details:
                Intent intent = new Intent(MemberManageActivity.this,MemberListActivity.class);
                intent.putExtra("vipCount",vipCount);
                startActivity(intent);
                break;
        }
    }

    private Dialog mMyDialog;
    private void showMyDialog2() {
        mMyDialog = new Dialog(this,R.style.MyDialogStyle);
        mMyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog.setContentView(R.layout.dialog_explanation);
        mMyDialog.setCanceledOnTouchOutside(false);
        mMyDialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDialog.dismiss();
            }
        });
        mMyDialog.show();
    }

}
