package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ChooseBankCardBean;
import com.hybunion.yirongma.payment.bean.base.BaseBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.WalletKeyboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 代理“商”计划 中的体现
 */

public class TiXianActivity extends BasicActivity {
    @Bind(R.id.walletKeyboard_tixian_activity)
    WalletKeyboard mKeyboard;
    @Bind(R.id.tixianMoney_tixian_activity)
    TextView mTvAmt;
    @Bind(R.id.delete_tixian_activity)
    ImageView mImgX;
    @Bind(R.id.tv_ketixian_tixian_activity)
    TextView mTvKeTiXian; // 可提现金额
    @Bind(R.id.addBankParent_tixian_activity)
    RelativeLayout mAddBankCardParent;
    @Bind(R.id.bankParent_tixian_activity)
    RelativeLayout mBankParent;
    @Bind(R.id.img_bank_tixian_activity)
    ImageView mImgBank;
    @Bind(R.id.tv_bankName_tixian_activity)
    TextView mTvBankName;
    @Bind(R.id.tv_bankDetail_tixian_activity)
    TextView mTvWeiHao;

    private double mAmt; // 可提现金额（从 MyWalletActivity 中传过来）
    private String mLoginType;
    private String mMerId;
    private String mMId;

    public static void start(Context from, double amt){
        Intent intent = new Intent(from, TiXianActivity.class);
        intent.putExtra("amt",amt);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_layout;
    }

    @Override
    public void initView() {
        super.initView();
        // 键盘监听
        mKeyboard.setKeyboardListener(new WalletKeyboard.KeyboardListener() {
            @Override
            public void amtChanged(String amt) {  // 按键盘的监听
                mTvAmt.setText(amt);
            }

            @Override
            public void tixian() {  // 确认提现 按钮监听
                String amt = mTvAmt.getText().toString().trim();
                double amtD = Double.parseDouble(amt);
                if (amtD == 0){
                    ToastUtil.show("请输入提现金额");
                    return;
                }
                if (TextUtils.isEmpty(mBankName) || TextUtils.isEmpty(mBankAccNo) || TextUtils.isEmpty(mBankAccName) ||
                        TextUtils.isEmpty(mPayBankId)){
                    ToastUtil.show("请选择银行卡");
                    return;
                }
                tiXian(mMerId, mMId,mBankName,mBankAccNo,mBankAccName,mPayBankId,amt);
            }
        });
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {  // 老板
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
        mMId = SharedPreferencesUtil.getInstance(this).getKey(Constants.MID);
        mAmt = getIntent().getDoubleExtra("amt",0);
        DecimalFormat df = new DecimalFormat("0.00");
        mTvKeTiXian.setText(df.format(mAmt)+"  元");
    }

    public void tiXian(String merId, String mId, String bankName, String bankAccNo, String bankAccName, String payBankId, String cashAmt){
        String url = NetUrl.TIXIAN;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId",merId);
            jsonObject.put("jhMid",mId);
            jsonObject.put("bankName",bankName);
            jsonObject.put("bankAccNo",bankAccNo);
            jsonObject.put("bankAccName",bankAccName);
            jsonObject.put("paybankId",payBankId);
            jsonObject.put("cashAmt",cashAmt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(TiXianActivity.this, url, jsonObject, new MyOkCallback<BaseBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BaseBean baseBean) {
                String msg = baseBean.getMessage();
                if ("0".equals(baseBean.getStatus())){
                    Intent intent = new Intent(TiXianActivity.this,AgentTiXianResultActivity.class);
                    startActivity(intent);
                    hideLoading();
                    TiXianActivity.this.finish();
                }else{
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
                return BaseBean.class;
            }
        });



    }

    @OnClick({R.id.addBankParent_tixian_activity, R.id.bankParent_tixian_activity})
    public void bankCardClick(RelativeLayout layout){
        switch(layout.getId()){
            case R.id.addBankParent_tixian_activity:   // 添加结算卡，跳转结算卡列表
                startActivityForResult(new Intent(TiXianActivity.this,ChooseBankCardActivity.class),133);
                break;

            case R.id.bankParent_tixian_activity:   // 添加结算卡，跳转结算卡列表
                startActivityForResult(new Intent(TiXianActivity.this,ChooseBankCardActivity.class),144);
                break;

        }
    }

    @Override
    protected void load() {
        super.load();
        getBankList(mMerId);

    }

    public void getBankList(String merId){
        String url = NetUrl.CHOOSE_BANK_CARD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId",merId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(TiXianActivity.this, url, jsonObject, new MyOkCallback<ChooseBankCardBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ChooseBankCardBean chooseBankCardBean) {
                List<ChooseBankCardBean.DataBean> data = chooseBankCardBean.getData();
                if (data!=null && data.size()!=0){
                    mAddBankCardParent.setVisibility(View.GONE);
                    mBankParent.setVisibility(View.VISIBLE);
                    Glide.with(TiXianActivity.this).load(data.get(data.size()-1).bankImg).into(mImgBank);
                    mTvBankName.setText(data.get(data.size()-1).bankName);
                    mTvWeiHao.setText("尾号 "+data.get(data.size()-1).accNo+" "+data.get(data.size()-1).cardType);

                    mBankName = data.get(data.size()-1).bankName;
                    mBankAccName = data.get(data.size()-1).bankAccName;
                    mBankAccNo = data.get(data.size()-1).bankAccNo;
                    mPayBankId = data.get(data.size()-1).paybankId;

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
                return ChooseBankCardBean.class;
            }
        });

    }

    // 全部提现 按钮
    @OnClick(R.id.tv_tixian_all_tixian_activity)
    public void tixianAll(){
        mTvAmt.setText(mAmt+"");
    }

    @OnClick(R.id.delete_tixian_activity)
    public void clearAmt(){
        mKeyboard.clear();
    }
    private String mBankName, mBankAccNo, mBankAccName, mPayBankId;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 132 && data!=null){  // 选择结算银行卡完成
            ChooseBankCardBean.DataBean dataBean = (ChooseBankCardBean.DataBean) data.getSerializableExtra("data");
            if (dataBean!=null){
                mAddBankCardParent.setVisibility(View.GONE);
                mBankParent.setVisibility(View.VISIBLE);
                Glide.with(this).load(dataBean.bankImg).into(mImgBank);
                mTvBankName.setText(dataBean.bankName);
                mTvWeiHao.setText("尾号 "+dataBean.accNo+" "+dataBean.cardType);
                mBankName = dataBean.bankName;
                mBankAccNo = dataBean.bankAccNo;
                mBankAccName = dataBean.bankAccName;
                mPayBankId = dataBean.paybankId;
            }

        }
    }
}
