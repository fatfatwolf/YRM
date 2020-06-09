package com.hybunion.yirongma.payment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.bean.BindEquipBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.MyBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 绑定云喇叭
 */

public class BindCloudLaBaActivity extends BasicActivity {

    @Bind(R.id.bind_receipt_code_btn_scan)
    Button btnScan;
    @Bind(R.id.tv_brand_bind_cloud_laba_activity)
    TextView mTvBrand;
    @Bind(R.id.et_terminal_number)
    EditText et_terminal_number;
    @Bind(R.id.bind_receipt_code_text_store)
    TextView storeOwned;
    @Bind(R.id.tv_name)
    TextView tv_name;

    String content;
    String storeId, storeName;
    private String yunType,type;//1

    public static void start(Activity context, String storeName, String storeId, String type) {
        Intent intent = new Intent(context, BindCloudLaBaActivity.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("storeId", storeId);
        intent.putExtra("type", type);
        context.startActivityForResult(intent,122);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_cloud_laba;
    }

    @Override
    public void initView() {
        storeId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        type = getIntent().getStringExtra("type");
        if("1".equals(type)){
            tv_name.setText("门店名称");
        }else {
            tv_name.setText("款台名称");
        }
        storeOwned.setText(storeName);
        yunType = "0";
    }


    @OnClick(R.id.bind_receipt_code_btn_scan)
    public void onClick() {
        String yunId = et_terminal_number.getText().toString();
        if (TextUtils.isEmpty(yunId)) {
            HRTToast.showToast("终端编号不能为空", this);
            return;
        }

        bindCloudYun(yunId,storeId,yunType);
    }
    public void bindCloudYun(String yunId, String mStoreId,String type){
        String url = NetUrl.NEW_BIND;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", SharedPreferencesUtil.getInstance(BindCloudLaBaActivity.this).getKey(Constants.MERCHANTID));
            jsonObject.put("storeId",mStoreId);
            jsonObject.put("yunId",yunId);
            jsonObject.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(BindCloudLaBaActivity.this, url, jsonObject, new MyOkCallback<BindEquipBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BindEquipBean bindEquipBean) {
                String status = bindEquipBean.getStatus();
                String message = bindEquipBean.getMessage();
                if("0".equals(status)){
                    ToastUtil.show(message);
                    setResult(122);
                    finish();
                }else {
                    ToastUtil.show(message);
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
                return BindEquipBean.class;
            }
        });
    }



    @OnClick(R.id.choose_parent_bind_cloud_laba_activity)
    public void choosePinPai() {
        showDilaog();
    }

    private MyBottomDialog mDialog;

    public void showDilaog() {
        final List<String> strList = new ArrayList<>();
        strList.add("智联博众");
        strList.add("新联付");
        strList.add("波普");
        strList.add("华智融");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog("云喇叭品牌", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {

                switch (position) {
                    case 0:
                        mTvBrand.setText("智联博众");
                        yunType = "0";
                        break;
                    case 1:
                        mTvBrand.setText("新联付");
                        yunType = "2";
                        break;
                    case 2:
                        mTvBrand.setText("波普");
                        yunType = "1";
                        break;
                    case 3:
                        mTvBrand.setText("华智融");
                        yunType = "3";
                        break;
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });
    }

}
