package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.SingInfoBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class BussinessInformationActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.ll_learing_bank_card)
    RelativeLayout ll_learing_bank_card;
    @Bind(R.id.ll_contract_information)
    LinearLayout ll_contract_information;
    @Bind(R.id.ll_basic_information)
    LinearLayout ll_basic_information;
    @Bind(R.id.titleBar_store_manage_activity)
    TitleBar mTitleBar;
//    @Bind(R.id.tv_head)
//    TextView tv_head;
//    @Bind(R.id.ll_back)
//    LinearLayout ll_back;
    @Bind(R.id.tv_all_money)
    TextView tv_all_money;
    @Bind(R.id.tv_accountNo)
    TextView tv_accountNo;
    private String merchantName;
    private String accountNo;
    private String bankBranch;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_store_manage;
    }

    @Override
    public void initView() {
        super.initView();
        merchantName = SharedPreferencesUtil.getInstance(this).getKey("merchantName");
        accountNo = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.BANK_ACCNO);
        bankBranch = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.BANK_BRANCH);
//        tv_accountNo = findViewById(R.id.tv_accountNo);
        String accNu = "";
        // 设置银行名称+银行卡号后四位
        if (!TextUtils.isEmpty(accountNo)){
            if (accountNo.length()>4){
                accNu = accountNo.substring(accountNo.length()-4, accountNo.length());
            }else{
                accNu = accountNo;
            }
        }
        if (!TextUtils.isEmpty(bankBranch)){
            tv_accountNo.setText(bankBranch+"  "+accNu);
        }else if (!TextUtils.isEmpty(accNu)){
            tv_accountNo.setText(accNu);
        }
        mTitleBar.setTv_titlebar_back_titleText(merchantName);
        ll_learing_bank_card.setOnClickListener(this);
        ll_contract_information.setOnClickListener(this);
        ll_basic_information.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_learing_bank_card:
                startActivity(new Intent(BussinessInformationActivity.this, BankCardInfoActivity.class));
                break;
            case R.id.ll_contract_information:
                Intent intent = new Intent(BussinessInformationActivity.this,LMFMerchantInformationActivity2.class);
                startActivity(intent);
                break;
            case R.id.ll_basic_information:
                Intent intent2 = new Intent(BussinessInformationActivity.this,LMFMerchantInformationActivity.class);
                startActivity(intent2);
                break;

        }
    }

    private void getSubscripInfor(){
        this.showLoading();
        String url = NetUrl.QUERY_SING_INFO;
        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(this));

        OkUtils.getInstance().postFormData(BussinessInformationActivity.this, url, map, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("success");
                    if (success) {//{"msg":"查询成功","numberUnits":"","sessionExpire":false,"success":true}
                        Gson gson = new Gson();
                        if(jsonObject.has("obj")){
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            String msg = obj.optString("msg");
                            if ("商户信息不存在".equals(msg)) {
                                ToastUtil.show(msg);
                                return;
                            } else {
                                JSONObject data = obj.optJSONObject("data");
                                SingInfoBean.ObjBean.DataBean dataInfo = gson.fromJson(data.toString(), new TypeToken<SingInfoBean.ObjBean.DataBean>() {
                                }.getType());
                                try {
                                    String minInfo2 = dataInfo.getMinfo2();
                                    tv_all_money.setText(YrmUtils.ThousandTwoPoints(YrmUtils.thousandSperate(minInfo2)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return String.class;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubscripInfor();

    }



}
