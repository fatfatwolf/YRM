package com.hybunion.yirongma.payment.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.BindChaJianBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.NameFilter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.MaxLenghtInputFilter;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 绑定收银插件
 */

public class BindChaJianActivity extends BasicActivity {

    @Bind(R.id.bind_receipt_code_btn_scan)
    Button btnScan;
    @Bind(R.id.bind_receipt_code_edit_text_name)
    EditText mText;
    @Bind(R.id.bind_receipt_code_text_store)
    TextView storeOwned;
    String content;
    String storeId, storeName;
    private String merId;

    public static void start(Activity context, String storeName, String storeId) {
        Intent intent = new Intent(context, BindChaJianActivity.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("storeId", storeId);
        context.startActivityForResult(intent,123);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_chajian;
    }

    @Override
    public void initView() {
        mText.setFilters(new InputFilter[]{new NameFilter(), new MaxLenghtInputFilter(30)});
        String loginType = SharedPreferencesUtil.getInstance(BindChaJianActivity.this).getKey("loginType");
        storeId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        if(loginType.equals("0")){
            merId = SharedPreferencesUtil.getInstance(BindChaJianActivity.this).getKey("merchantID");
        }else {
            merId = SharedPreferencesUtil.getInstance(BindChaJianActivity.this).getKey("shopId");
        }
        storeOwned.setText(storeName);
    }


    @Override
    protected void load() {

    }

    @OnClick(R.id.bind_receipt_code_edit_text_name)
    public void editText() {
        String inputContent = mText.getText().toString().trim();
        if (!TextUtils.isEmpty(inputContent)) {
            mText.setSelection(inputContent.length());
        }

    }

    @OnClick(R.id.bind_receipt_code_btn_scan)
    public void onClick() {
        String tidName = mText.getText().toString().trim();
        if (TextUtils.isEmpty(tidName)) {
            HRTToast.showToast("名称不能为空", this);
            return;
        }
        addShouYim(merId,storeId,"",tidName);
    }


    public void addShouYim(String merId, String storeId, String tid, String tidName){
        String url = NetUrl.ADD_KUANTAI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId",merId);
            jsonObject.put("storeId",storeId);
            jsonObject.put("tid",tid);
            jsonObject.put("tidName",tidName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(BindChaJianActivity.this, url, jsonObject, new MyOkCallback<BindChaJianBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BindChaJianBean bindChaJianBean) {
                String status = bindChaJianBean.getStatus();
                String message = bindChaJianBean.getMessage();
                if(status.equals("0")){
                    ToastUtil.show(message);
                    setResult(123);
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
                return BindChaJianBean.class;
            }
        });
    }

}
