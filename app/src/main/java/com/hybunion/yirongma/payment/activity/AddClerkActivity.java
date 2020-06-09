package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AddClerkBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.HashMap;
import java.util.Map;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lyj
 * 17/8/16
 * @email freemars@yeah.net
 * @description
 */

public class AddClerkActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_head)
    TextView tvHead;
    @Bind(R.id.et_clerk_name)
    EditText clerkName;
    @Bind(R.id.tv_shop_member)
    TextView tv_shop_member;
    @Bind(R.id.tv_shop_manage)
    TextView tv_shop_manage;
    @Bind(R.id.et_shop_phone)
    TextView et_shop_phone;
    private String clerktitle,merchantID, empId ,name,storeName,type,loginType,merId;
    private String employPhone; //二维码地址
    private IWXAPI api;
    private int flag = 2;
    private String storeId;
    Button bt_add_clerk_sure;
    private final String APP_ID="wx91edf936606e9712";
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
    /**
     * get contentView's id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_add_clerk;
    }
    @Override
    public void initView() {
        api = WXAPIFactory.createWXAPI(AddClerkActivity.this, APP_ID);
        api.registerApp(APP_ID);
        bt_add_clerk_sure = (Button) findViewById(R.id.bt_add_clerk_sure);
        bt_add_clerk_sure.setOnClickListener(this);
        tv_shop_member.setOnClickListener(this);
        tv_shop_manage.setOnClickListener(this);
        Intent intent=getIntent();
        type = getIntent().getStringExtra("type");
        loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        clerktitle = intent.getStringExtra("clerktitle");
        storeName = getIntent().getStringExtra("storeName");
        storeId = intent.getStringExtra("storeId");
        if ("1".equals(clerktitle)){
            tvHead.setText("修改员工");
             empId = intent.getStringExtra("empId");
             name = intent.getStringExtra("name");
             clerkName.setText(name);
        }else {
            tvHead.setText("添加员工");
        }
        if(type.equals("2")){
            tv_shop_manage.setVisibility(View.GONE);
        }
        merchantID= SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
    }
    @Override
    public void initData() {}

    @Override
    public void showInfo(Map map) {
        super.showInfo(map);
        if (map==null){
            return;
        }

    }

    @OnClick(R.id.ll_back)
    public void close() {
        finish();
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_clerk_sure:
                String employName = clerkName.getText().toString().trim();
                employPhone = et_shop_phone.getText().toString().trim();
                if(employPhone.equals("") || employPhone == null){
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if ("".equals(employName) || employName==null ) {
                    ToastUtil.show("请输入店员姓名");
                    return;
                }else{
                    if(loginType.equals("0")){
                        merId = merchantID;
                    }else {
                        merId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
                    }
                addClerk(storeId,employName,employPhone,flag,storeName,merId);
            }
                break;
            case R.id.tv_shop_manage:
                flag = 1;
                tv_shop_manage.setBackgroundResource(R.drawable.background_corner2);
                tv_shop_manage.setTextColor(getResources().getColor(R.color.btn_back_color1));
                tv_shop_member.setBackgroundResource(R.drawable.background_corner4);
                tv_shop_member.setTextColor(getResources().getColor(R.color.text_color2));
                break;
            case R.id.tv_shop_member:
                flag = 2;
                tv_shop_member.setTextColor(getResources().getColor(R.color.btn_back_color1));
                tv_shop_manage.setTextColor(getResources().getColor(R.color.text_color2));
                tv_shop_manage.setBackgroundResource(R.drawable.background_corner4);
                tv_shop_member.setBackgroundResource(R.drawable.background_corner2);
                break;
        }

    }

    public void addClerk(String storeId,String employName,String employPhone,int flag,String storeName,String merId){
        String url = NetUrl.YRM_ADD_CLERK_DETAIL;
        Map map = new HashMap();
        map.put("storeId",storeId);
        map.put("employName",employName);
        map.put("employPhone",employPhone);
        map.put("position",String.valueOf(flag));
        map.put("storeName",storeName);
        map.put("merId",merId);

        OkUtils.getInstance().postFormData(AddClerkActivity.this, url, map, new MyOkCallback<AddClerkBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(AddClerkBean addClerkBean) {
                String msg = addClerkBean.getMsg();
                if ("0".equals(addClerkBean.getStatus())){
                    String empId = addClerkBean.getMerId();
                    ToastUtil.show(msg);
                    LogUtil.d(empId+"店员ID");
                    hideLoading();
                    finish();
                }else {
                    ToastUtil.show(msg);
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
                return AddClerkBean.class;
            }
        });
    }

}
