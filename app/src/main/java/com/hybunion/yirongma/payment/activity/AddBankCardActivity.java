package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AddBankCardBean;
import com.hybunion.yirongma.payment.bean.BankInfoBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;
import butterknife.OnClick;

/**
 *  钱包模块 添加 结算卡
 */

public class AddBankCardActivity extends BasicActivity {
    @Bind(R.id.edt_name_add_bank_card_activity)
    EditText mEdtAccName;
    @Bind(R.id.edt_cardNum_add_bank_card_activity)
    EditText mEdtBankNum;
    @Bind(R.id.imgBank_add_bank_card_activity)
    ImageView mImgBank;
    @Bind(R.id.tvBankName_add_bank_card_activity)
    TextView mTvBankName;
    private String mLoginType;
    private String mMerId;
    private String mMid;
    private String bankName, paymentLine,bankImg;
    private String mTiXianName;
    // tixianName 提现人姓名，如果传过来有值，就固定显示此名字，不能修改；如果传过来没有值，就可以输入
    public static void start(Context from, String tixianName){
        Intent intent = new Intent(from, AddBankCardActivity.class);
        intent.putExtra("tixianName",tixianName);
        from.startActivity(intent);
    }


    @Override
    protected BasePresenter getPresenter() {
        return null ;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_bank_card_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {  // 老板
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
        mMid = SharedPreferencesUtil.getInstance(this).getKey(Constants.MID);
        mEdtBankNum.addTextChangedListener(watcher);
        mTiXianName = getIntent().getStringExtra("tixianName");
        if (!TextUtils.isEmpty(mTiXianName)){
            mEdtAccName.setText(mTiXianName);
            mEdtAccName.setFocusable(false);
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null == s) {
                return;
            }
            String bankCardNumber = mEdtBankNum.getText().toString().trim();
            int length = bankCardNumber.length();
            if ((length >= 7 && length <= 10 || length == 19)) {
                //查询银行信息
                getBank(bankCardNumber);
            }
        }
    };

    public void getBank(String cardbin) {
        String url = NetUrl.QUERYBANKNAME;
        JSONObject bodyObject = new JSONObject();
        try {
            bodyObject.put("cardBin", cardbin);
            bodyObject.put("agent_id", SharedPreferencesUtil.getInstance(AddBankCardActivity.this).getKey("agentId"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(AddBankCardActivity.this, url, bodyObject, new MyOkCallback<BankInfoBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(BankInfoBean bean2) {
                String status = bean2.getStatus();
                if ("0".equals(status)) {
                    mTvBankName.setText(bean2.getPaymentBank());
                    bankName = bean2.getPaymentBank();
                    paymentLine = bean2.getPaymentLine();
                    bankImg = bean2.getPaymentBankImg();
                    mImgBank.setVisibility(View.VISIBLE);
                    mPayBankId = bean2.getPaymentLine();
                    Glide.with(AddBankCardActivity.this)
                            .load(bankImg)
                            .into(mImgBank);
                    SharedPreferencesUtil.getInstance(context()).putKey("PersonalAccountBranchBank", paymentLine);//系统行号
                    SharedPreferencesUtil.getInstance(context()).putKey("PersonalAccountBankImage", bankImg);//图片地址
                } else {
                    ToastUtil.shortShow(AddBankCardActivity.this, "不支持该银行卡，请更换入账银行卡");
                    mTvBankName.setText("");
                    bankName = "";
                    mImgBank.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return BankInfoBean.class;
            }
        });

    }



    // 确认添加 按钮监听
    @OnClick(R.id.add_add_bank_card_activity)
    public void addCard(){
        String accName = mEdtAccName.getText().toString().trim();
        String bankNum = mEdtBankNum.getText().toString().trim();
        String bankName = mTvBankName.getText().toString().trim();
        if (TextUtils.isEmpty(accName)){
            ToastUtil.show("请输入插卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(bankNum)){
            ToastUtil.show("请输入银行卡号");
            return;
        }
        if (TextUtils.isEmpty(bankName)){
            ToastUtil.show("请选择开户行");
            return;
        }

        addBankCard(mMerId, mMid, bankName,bankNum,accName,mPayBankId);

    }


    // 添加结算卡
    public void addBankCard(String merId, String mid, String bankName, String cardNum, String bankAccName, String paybankId) {
        String url = NetUrl.ADD_BANK_CARD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
            jsonObject.put("jhMid", mid);
            jsonObject.put("bankName", bankName);
            jsonObject.put("bankAccNo", cardNum);
            jsonObject.put("bankAccName", bankAccName);
            jsonObject.put("paybankId", paybankId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(AddBankCardActivity.this, url, jsonObject, new MyOkCallback<AddBankCardBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(AddBankCardBean baseBean) {
                String msg = baseBean.getMessage();
                if ("0".equals(baseBean.getStatus())) {
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtil.show(msg);
                    } else {
                        ToastUtil.show("添加成功");
                    }
                } else {
                    if (!TextUtils.isEmpty(msg))
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
                return AddBankCardBean.class;
            }
        });

    }

    // 开户行 点击跳转 监听
    @OnClick(R.id.kaihuhangParent_add_bank_card_activity)
    public void kaihuhang(){
        startActivityForResult(new Intent(this,AllBankListActivity.class),112);
    }


    private String mBankName, mPayBankId;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data!=null){
            String imgUrl = data.getStringExtra("bankImg");
            mBankName = data.getStringExtra("bankName");
            mPayBankId = data.getStringExtra("payBankId");
            mImgBank.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(imgUrl)
                    .into(mImgBank);
            mTvBankName.setText(mBankName);
        }
    }


}
