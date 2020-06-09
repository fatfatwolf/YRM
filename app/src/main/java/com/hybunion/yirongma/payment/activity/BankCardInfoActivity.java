package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ModifyCardMessageBean;
import com.hybunion.yirongma.payment.bean.QueryMerInfoNoneBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.FormatUtil;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.SaveMerInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 银行卡信息
 * Created by lyf on 2017/5/20.
 */

public class BankCardInfoActivity extends BasicActivity {
    @Bind(R.id.iv_bank_icon)
    ImageView iv_bank_icon;//银行图标
    @Bind(R.id.tv_bank_name)
    TextView tv_bank_name;//银行名称
    @Bind(R.id.tv_bankCard_type)
    TextView mBankCardType;//银行卡类型
    @Bind(R.id.tv_bankCard_number)
    TextView tv_bankCard_number;//银行卡号码
    @Bind(R.id.tv_approve_status)
    TextView tv_approve_status;//审核状态
    @Bind(R.id.rl_bankCard_item)
    RelativeLayout mBankCardLayout;//银行卡条目的布局
    @Bind(R.id.titleBar_bank_card_info_activity)
    TitleBar mTitleBar;

    private String bankAccName;
    private String bankNumber;
    private Gson mGson;
    private String bindStatus; // 审核状态
    private String acctype; // 1-对公    2-对私

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_card_info;
    }

    @Override
    public void initView() {
        super.initView();

        mTitleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BankCardInfoActivity.this, BankCardChangeRecordActivity.class));
            }
        });
        mGson = new Gson();
        queryMerchantInfo();
    }



    //点击银行卡条目，显示原有卡片信息
    public void bankCardItem() {
        if ("Y".equals(bindStatus) || "C".equals(bindStatus) || TextUtils.isEmpty(bindStatus)) {
            if ("对公".equals(acctype)) {
                ToastUtil.showShortToast("对公账户修改，请在商户管理平台提交工单申请");
                return;
            }
            Intent intent = new Intent(BankCardInfoActivity.this, ModifyBankCardInfoActivity.class);
            intent.putExtra("bankAccName", bankAccName);
            intent.putExtra("legalNum", SharedUtil.getInstance(this).getString(Constants.LEGAL_NUM));
            intent.putExtra("accNum", SharedPreferencesUtil.getInstance(this).getKey(Constants.ACCNUM));
            intent.putExtra("bankAccNo", bankNumber);
            startActivity(intent);
        } else if ("W".equals(bindStatus)) {
            ToastUtil.showShortToast("入网资料审核中，暂不可变更结算卡信息");
        }
        else if ("K".equals(bindStatus)) {
            ToastUtil.showShortToast("开店审批被退回，请联系业务员");
        }
    }

    /**
     * 查询银行卡信息
     *
     * @param bankCardNumber
     */

    private void queryBankCardInfoRequest(String bankCardNumber) {
        String url = NetUrl.QUERY_BANK_CARD;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("cardBin", bankCardNumber);
            jsonRequest.put("agent_id", BankCardInfoActivity.this.getString(R.string.AGENT_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(BankCardInfoActivity.this, url, jsonRequest, new MyOkCallback<ModifyCardMessageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ModifyCardMessageBean modifyCardMessageBean) {
                String status = modifyCardMessageBean.getStatus();
                String paymentBank = modifyCardMessageBean.getPaymentBank();//银行名称
                String paymentBankImg = modifyCardMessageBean.getPaymentBankImg();//银行图标
                String cardType = modifyCardMessageBean.getCardType();//卡的类型
                String paymentLine = modifyCardMessageBean.getPaymentLine();//系统行号
                String msg = modifyCardMessageBean.getMessage();
                if ("0".equals(status)) {
                    showBankIcon(paymentBankImg, iv_bank_icon);//获取银行卡的小图标
                    if(!TextUtils.isEmpty(bankNumber)){
                        String bankCard = FormatUtil.formatIDCard(bankNumber);//格式化卡号
                        tv_bankCard_number.setText(bankCard);//设置银行卡号
                    }
                    tv_bank_name.setText(paymentBank);//设置银行名称
                    mBankCardType.setText(cardType);//设置银行卡类型
                }else{
                    if (!TextUtils.isEmpty(msg)){
                        ToastUtil.showShortToast(msg);
                    }
                }

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ModifyCardMessageBean.class;
            }
        });

    }

    /**
     * 显示银行卡图标
     *
     * @param url
     * @param mBankIcon
     */
    private void showBankIcon(String url, ImageView mBankIcon) {
        if (!isFinishing())
            Glide.with(this).load(url)//图片的url
//                .placeholder(R.drawable.my_message_bankcard)//默认图片
                    .error(R.drawable.bank_card_failed)//错误图片
                    .into(mBankIcon);//显示的位置
    }

    /**
     * 查询商户信息，进行本地保存
     */
    private void queryMerchantInfo() {
        showLoading();
        String url = NetUrl.QUERY_MERINFO;
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("UID", SharedPreferencesUtil.getInstance(BankCardInfoActivity.this).getKey("UID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(BankCardInfoActivity.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("find","返回数据："+response);
                try {
                    String status = response.getString("status");
                    Gson gson = new Gson();
                    QueryMerInfoNoneBean bean = gson.fromJson(response.toString(), QueryMerInfoNoneBean.class);
                    if (status.equals("0")) {
                        bindStatus = bean.getAPPROVESTATUS();
                        acctype = bean.getACCTYPE();
                        if (!TextUtils.isEmpty(bindStatus)) {
                            if ("Z".equals(bindStatus) || "W".equals(bindStatus)) {
                                tv_approve_status.setText("审核中");
                            } else {
                                tv_approve_status.setText("变更");
                            }
                        }
                        bankAccName = bean.getBankAccName();
                        bankNumber = bean.getBankAccNo();
                        // 根据卡号查询卡信息

                        queryBankCardInfoRequest(bankNumber);
                        // 保存用户数据
                        SaveMerInfoUtil.saveAll(BankCardInfoActivity.this,bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideLoading();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
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


    @OnClick(R.id.tv_approve_status)
    public void buttonClick(){
        bankCardItem();
    }


}
