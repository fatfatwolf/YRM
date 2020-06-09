package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.BankCardApproveStatusBean;
import com.hybunion.yirongma.payment.bean.ModifyCardMessageBean;
import com.hybunion.yirongma.payment.bean.base.BaseBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包中的 提现
 */

public class TiXianNewActivity extends BasicActivity {
    //    @Bind(R.id.walletKeyboard_tixian_activity)
//    WalletKeyboard mKeyboard;
    @Bind(R.id.tixianMoney_tixian_activity)
    EditText mEdtAmt;
    @Bind(R.id.delete_tixian_activity)
    ImageView mImgX;
    @Bind(R.id.tv_ketixian_tixian_activity)
    TextView mTvJinE; // 钱包余额
    @Bind(R.id.addBankParent_tixian_new_activity)
    RelativeLayout mAddBankCardParent;
    @Bind(R.id.bankParent_tixian_activity)
    RelativeLayout mBankParent;
    @Bind(R.id.img_bank_tixian_activity)
    ImageView mImgBank;
    @Bind(R.id.tv_bankName_tixian_activity)
    TextView mTvBankName;
    @Bind(R.id.tv_bankDetail_tixian_activity)
    TextView mTvWeiHao;
    @Bind(R.id.kaitong_tixian_new_layout)
    LinearLayout mKaiTongParent;
    @Bind(R.id.selectState_tixian_new_layout)
    ImageView mImgSelectShiShi;  //实时到账选择图标
    @Bind(R.id.selectState1_tixian_new_layout)
    ImageView mImgSelectCiRi;  // 次日到账 选择图标
    @Bind(R.id.tv_tixian_new_layout)
    TextView mTvTiXianStr;
    @Bind(R.id.tv_shenhezhong_tixian_new_layout)
    TextView mTvSheHeZhong;


    private double mAmt; // 可提现金额（从 MyWalletActivity 中传过来）
    private double mBalance; // 钱包余额   可提现金额 = 钱包余额 - 手续费
    private String mMId;
    private String mBankAccNo, mBankAccName, mPayBankId, mBankName;
    private boolean mIsKaiTongShiShi; // 是否开通实时到账
    private boolean mIsSelectedShiShi; // 是否选中了实时到账。 true-选中实时到账  false-选中次日到账，没有哪个都不选的情况。
    private double mTiXianAmt, mFuWuFei; // 提现金额，服务费
    private DecimalFormat mDf;
    private String mCashStatus;
    private String tixianType = "1";

    private String 实时到账说明未开通 = "开通实时到账商户需满足以下条件：\n" +
            "1、商户开通时间≥7天\n" +
            "2、商户近7天每日交易笔数≥30笔\n" +
            "3、商户资质齐全\n" +
            "4、商户交易符合行业状态（交易金额、笔数、交易时间）";

    private String 开通实时到账 = "开通实时到账提现需对商户资质进行审核，审核结果可在通知栏中查看。\n\n" +
            "      服务时间：每日09:00~18:00";

    private String 实时到账说明已开通 = "1、实时到账提现每次收取服务费为总提现金额的0.1%+3元/笔，提现金额最高限额50万元；\n" +
            "2、提现手续费将从账户余额中额外扣除。";

    private String 次日到账说明 = "1、次日到账免提现服务费；\n"+"2、发起次日到账提现申请后，提现金额将在次日12点前到账，具体到账时间以开户行为准。";


    // amt 余额
    public static void start(Context from, double amt, double balance) {
        Intent intent = new Intent(from, TiXianNewActivity.class);
        intent.putExtra("amt", amt);  // 可提现金额  可提现金额 = 钱包余额-手续费
        intent.putExtra("balance", balance);  // 钱包余额
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_new_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mDf = new DecimalFormat("0.00");
        mMId = SharedPreferencesUtil.getInstance(this).getKey(Constants.MID);
        mAmt = getIntent().getDoubleExtra("amt", 0);  // 可提现金额
        mBalance = getIntent().getDoubleExtra("balance", 0);
        DecimalFormat df = new DecimalFormat("0.00");
        mTvJinE.setText(df.format(mBalance) + "  元");
        mCashStatus = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.IS_SHISHI);    // 钱包余额接口返回的状态。
        mIsKaiTongShiShi = "0".equals(mCashStatus);
        // 默认选中次日到账
        initState(mCashStatus);

        SpannableString ss = new SpannableString("请输入提现金额");
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mEdtAmt.setHint(new SpannedString(ss));
        mEdtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double tixianD;
                if (!TextUtils.isEmpty(s)) {
                    if (".".equals(s.toString())) {
                        mEdtAmt.setText("");
                        return;
                    }
                    if (s.toString().matches("[0][0-9]")) {
                        mEdtAmt.setText("0");
                        mEdtAmt.setSelection(mEdtAmt.getText().toString().length());
                        return;
                    }
                    if (s.toString().matches("[0-9]+[.][0-9]{3}")) {
                        String sn = s.toString().substring(0, s.toString().length() - 1);
                        mEdtAmt.setText(sn);
                        mEdtAmt.setSelection(mEdtAmt.getText().toString().length());
                        return;
                    }
                    tixianD = Double.parseDouble(s.toString());
                    if (tixianD < 10) {
                        mTvTiXianStr.setText("单笔最低提现金额 10.00 元");
                        mTvTiXianStr.setTextColor(Color.parseColor("#f94b35"));
                        mTvJinE.setText("");
                    } else if (tixianD > mAmt) {
                        mTvTiXianStr.setText("超出可提现金额，最多可提现 " + mDf.format(mAmt) + " 元");
                        mTvTiXianStr.setTextColor(Color.parseColor("#f94b35"));
                        mTvJinE.setText("");
                    } else if (tixianD > 500000) {
                        mTvTiXianStr.setText("单笔最高提现金额 500000.00 元");
                        mTvTiXianStr.setTextColor(Color.parseColor("#f94b35"));
                        mTvJinE.setText("");
                    } else {
                        mTvTiXianStr.setText("服务费  ");
                        mTvTiXianStr.setTextColor(Color.parseColor("#515E81"));
                        if (mIsSelectedShiShi) {
                            double fuwufei = tixianD / 1000 + 3;
                            BigDecimal b = new BigDecimal(fuwufei);
                            mFuWuFei = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            mTvJinE.setText(mFuWuFei + "  元");
                        } else {
                            mTvJinE.setText("0.00" + "  元");
                        }

                    }
                } else {
                    mTvTiXianStr.setText("钱包余额  ");
                    mTvTiXianStr.setTextColor(Color.parseColor("#515E81"));
                    mTvJinE.setText(mBalance + "  元");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void load() {
        super.load();
        queryBankCardStatus(); // 查询银行卡状态
    }

    // 查询银行卡状态
    private void queryBankCardStatus(){
        String mid = SharedUtil.getInstance(this).getString(Constants.MID);
        JSONObject jb = new JSONObject();
        try {
            jb.put("mid",mid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(this, NetUrl.BANKCARD_APPROVE_STATUS, jb, new MyOkCallback<BankCardApproveStatusBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BankCardApproveStatusBean statusBean) {
                boolean success = statusBean.getSuccess();
                String msg = statusBean.getMsg();
                if (success) {
                    if (statusBean.getObj().getRows() != null && statusBean.getObj().getRows().size() > 0) {
                        mBankAccName = statusBean.getObj().getRows().get(0).getBANKACCNAME();//姓名
                        mBankAccNo = statusBean.getObj().getRows().get(0).getBANKACCNO();//卡号
                        if (!TextUtils.isEmpty(mBankAccNo)) {
                            // 根据卡号，请求卡信息
                            queryBankCardInfo(mBankAccNo);
                        }

                    } else {
                        ToastUtil.showShortToast(msg);
                    }
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
                return BankCardApproveStatusBean.class;
            }
        });
    }

    // 根据卡号，请求卡信息
    private void queryBankCardInfo(String bankNo){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("cardBin", bankNo);
            jsonRequest.put("agent_id", getString(R.string.AGENT_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.QUERY_BANK_CARD, jsonRequest, new MyOkCallback<ModifyCardMessageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ModifyCardMessageBean baseBean) {
                String status = baseBean.getStatus();
                String msg = baseBean.getMessage();
                mBankName = baseBean.getPaymentBank();//银行名称
                String bankImg = baseBean.getPaymentBankImg();//银行图标
                String cardType = baseBean.getCardType();//卡的类型
                mPayBankId = baseBean.getPaymentLine();//系统行号

                if ("0".equals(status)) {
                    mAddBankCardParent.setVisibility(View.GONE);
                    mBankParent.setVisibility(View.VISIBLE);
                    Glide.with(TiXianNewActivity.this).load(bankImg).error(R.drawable.bank_card_failed).into(mImgBank);
                    mTvBankName.setText(mBankName);
                    String bankLast4 = "";
                    if (!TextUtils.isEmpty(mBankAccNo) && mBankAccNo.length() > 4) {
                        bankLast4 = mBankAccNo.substring(mBankAccNo.length() - 4);
                    }
                    mTvWeiHao.setText("尾号 " + bankLast4 + " " + cardType);

                } else {
                    ToastUtil.showShortToast(TextUtils.isEmpty(msg) ? "网络连接不佳" : msg);
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
                return ModifyCardMessageBean.class;
            }
        });
    }






    // 根据是否开通实时到站，设置按钮隐藏显示
    private void initState(String state) {
        mImgSelectShiShi.setVisibility(View.GONE);
        mKaiTongParent.setVisibility(View.GONE);
        mTvSheHeZhong.setVisibility(View.GONE);
        switch (state) {
            case "0":   // 审核成功，显示勾选图
                mImgSelectShiShi.setVisibility(View.VISIBLE);
                break;
            case "1":   // 审核失败或者没有开通，显示 立即开通
                mKaiTongParent.setVisibility(View.VISIBLE);
                break;
            case "2":    // 审核中  显示 审核中
                mTvSheHeZhong.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 开通实时到账
    @OnClick(R.id.kaitong_tixian_new_layout)
    public void kaitong() {
        showMyDialog(true, "开通实时到账", 开通实时到账, "立即开通", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBalanceDialog != null)
                    mBalanceDialog.dismiss();
                kaiTongShiShi();
            }
        });
    }

    /**
     * 开通实时到账
     */
    private void kaiTongShiShi(){
        JSONObject jb = new JSONObject();
        String merId = SharedPreferencesUtil.getInstance(this).getKey(Constants.MERCHANTID);
        try {
            jb.put("merId", merId);
            jb.put("type", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.KAITONG_SHISHI, jb, new MyOkCallback<BaseBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BaseBean result) {
                String status = result.getStatus();
                String msg = result.getMessage();
                if ("0".equals(status)) {
                    // 状态改成审核中
                    initState("2");
                } else {
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.showShortToast(msg);
                    else
                        ToastUtil.showShortToast("开通失败");
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
                return BaseBean.class;
            }
        });



    }


    // 实时到账问号 监听
    @OnClick(R.id.img_wenhao_tixian_new_layout)
    public void wenhao() {
        String content = "", title;
        if (mIsKaiTongShiShi) {
            title = "实时到账说明";
            content = 实时到账说明已开通;
        } else {
            title = "开通实时到账说明";
            content = 实时到账说明未开通;
        }
        showMyDialog(false, title, content, "知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBalanceDialog != null)
                    mBalanceDialog.dismiss();
            }
        });
    }

    // 次日到账问号 监听
    @OnClick(R.id.img_wenhao1_tixian_new_layout)
    public void wenhao1(){ //次日到账说明
        showMyDialog(false, "次日到账说明", 次日到账说明, "知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBalanceDialog != null)
                    mBalanceDialog.dismiss();
            }
        });
    }


    // 提现 按钮 监听
    @OnClick(R.id.tixian_new_activity)
    public void tixian() {
        String amt = mEdtAmt.getText().toString().trim();
        if (TextUtils.isEmpty(amt)) {
            ToastUtil.showShortToast("请输入提现金额");
            return;
        }
        if (TextUtils.isEmpty(mBankName) || TextUtils.isEmpty(mBankAccNo) || TextUtils.isEmpty(mBankAccName) ||
                TextUtils.isEmpty(mPayBankId)) {
            ToastUtil.showShortToast("请重新获取结算卡");
            return;
        }
        mTiXianAmt = Double.parseDouble(amt);
        if (mTiXianAmt < 10) {
            ToastUtil.showShortToast("单笔最低提现金额 10.00 元");
            return;
        }
        if (mTiXianAmt > mAmt) {
            ToastUtil.showShortToast("超出可提现金额，最多可提现 " + mDf.format(mAmt) + " 元");
            return;
        }
        if (mTiXianAmt > 500000) {
            ToastUtil.showShortToast("单笔最高提现金额 500000.00 元");
            return;
        }
//        presenter.tiXianNew(mMId, mTiXianAmt + "", mBankAccName, mBankAccNo, mPayBankId, mBankName,tixianType);
        tixian();
    }


    private void tiXian(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", mMId);
            jsonObject.put("money", mTiXianAmt+"");
            jsonObject.put("bankAccName", mBankAccName);
            jsonObject.put("bankAccNo", mBankAccNo);
            jsonObject.put("payBankId", mPayBankId);
            jsonObject.put("bankBranch", mBankName);
            jsonObject.put("cashtype",tixianType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.TIXIAN_NEW, jsonObject, new MyOkCallback<BaseBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BaseBean result) {
                hideLoading();
                if (result != null) {
                    String status = result.getStatus();
                    String msg = result.getMessage();
                    if ("0".equals(status)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String format = sdf.format(new Date());

                        TiXianResultActivity.start(TiXianNewActivity.this, mDf.format(mTiXianAmt) + "", mFuWuFei + "", mIsSelectedShiShi?"实时到账":"次日到账", mBankName, format);

                        TiXianNewActivity.this.finish();
                    } else {
                        if (!TextUtils.isEmpty(msg))
                            ToastUtil.showShortToast(msg);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return BaseBean.class;
            }
        });


    }


    // 实时到账 选择图标
    @OnClick(R.id.selectState_tixian_new_layout)
    public void selectShiShi() {
        if (mIsSelectedShiShi) return;
        tixianType = "0";
        select(true);
    }

    // 次日到账 选择图标
    @OnClick(R.id.selectState1_tixian_new_layout)
    public void ciRiSelect() {
        if (!mIsSelectedShiShi) return;
        tixianType = "1";
        select(false);
    }

    private void select(boolean isClickShiShi) {
        mIsSelectedShiShi = !mIsSelectedShiShi;
        mImgSelectShiShi.setImageResource(mIsSelectedShiShi ? R.drawable.selected_img : R.drawable.unselected_img);
        mImgSelectCiRi.setImageResource(mIsSelectedShiShi ? R.drawable.unselected_img : R.drawable.selected_img);
        mEdtAmt.setText(mEdtAmt.getText().toString());
        mEdtAmt.setSelection(mEdtAmt.getText().toString().length());

    }


    private Dialog mBalanceDialog;
    private TextView tvAccount = null, tvContent = null;
    private ImageView imgX = null;

    private void showMyDialog(boolean needCloseBt, String title, String content, String buttonContent, View.OnClickListener listener) {
        if (mBalanceDialog == null) {
            mBalanceDialog = new Dialog(this);
            mBalanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBalanceDialog.setContentView(R.layout.dialog_my_wallet_fragment);
            tvAccount = mBalanceDialog.findViewById(R.id.tv_account_my_wallet_dialog);
            tvContent = mBalanceDialog.findViewById(R.id.tv_content_my_wallet_dialog);
            imgX = mBalanceDialog.findViewById(R.id.img_close_my_wallet_dialog);
        }
        if (needCloseBt) {
            imgX.setVisibility(View.VISIBLE);
            imgX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBalanceDialog != null)
                        mBalanceDialog.dismiss();
                }
            });
        } else {
            imgX.setVisibility(View.GONE);
        }
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView okButton = mBalanceDialog.findViewById(R.id.ok_button_my_wallet_dialog);
        okButton.setText(buttonContent);
        okButton.setOnClickListener(listener);
        tvAccount.setText(title);
        tvContent.setText(content);
        mBalanceDialog.show();
    }

    // 重新调用结算卡接口
    @OnClick({R.id.addBankParent_tixian_new_activity, R.id.bankParent_tixian_activity})
    public void bankCardClick() {
        if (TextUtils.isEmpty(mBankName) || TextUtils.isEmpty(mBankAccNo) || TextUtils.isEmpty(mBankAccName) ||
                TextUtils.isEmpty(mPayBankId)) {
            queryBankCardStatus(); // 查询银行卡状态
        } else {  // 跳转结算卡详情页
            startActivity(new Intent(TiXianNewActivity.this, BankCardInfoActivity.class));
        }

    }

    // 全部提现 按钮
    @OnClick(R.id.tv_tixian_all_tixian_activity)
    public void tixianAll() {
        mEdtAmt.setText(mAmt + "");

    }

    @OnClick(R.id.delete_tixian_activity)
    public void clearAmt() {
//        mKeyboard.clear();
        mEdtAmt.setText("");
    }

    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        switch (type) {
            case BANK_CARD_APPROVE_STATUS:  // 查询卡状态获取卡号
//                if (map != null) {
//                    boolean successStatus = (boolean) map.get("success");
//                    String msg = (String) map.get("msg");
//                    if (successStatus) {
//                        mBankAccNo = (String) map.get("bankNumber");//卡号
//                        mBankAccName = (String) map.get("bankAccName");  // 用户名
//                        if (!TextUtils.isEmpty(mBankAccNo)) {
//                            presenter.queryBankCardInfo(mBankAccNo);
//                        }
//                    } else {
//                        ToastUtil.showShortToast(msg);
//                        return;
//                    }
//                } else {
//                    ToastUtil.showShortToast("网络不佳");
//                }
                break;

            case GET_BANKCARD_DETAIL:  // 查询卡信息
                if (map != null) {
//                    String status = (String) map.get("status");
//                    String msg = (String) map.get("msg");
//                    mBankName = (String) map.get("paymentBank");
//                    String bankImg = (String) map.get("paymentBankImg");
//                    String cardType = (String) map.get("cardType");
//                    mPayBankId = (String) map.get("paymentLine");
//                    if ("0".equals(status)) {
//                        mAddBankCardParent.setVisibility(View.GONE);
//                        mBankParent.setVisibility(View.VISIBLE);
//                        Glide.with(this).load(bankImg).error(R.drawable.bank_card_failed).into(mImgBank);
//                        mTvBankName.setText(mBankName);
//                        String bankLast4 = "";
//                        if (!TextUtils.isEmpty(mBankAccNo) && mBankAccNo.length() > 4) {
//                            bankLast4 = mBankAccNo.substring(mBankAccNo.length() - 4);
//                        }
//                        mTvWeiHao.setText("尾号 " + bankLast4 + " " + cardType);
//
//                    } else {
//                        ToastUtil.showShortToast(TextUtils.isEmpty(msg) ? "网络不佳" : msg);
//                    }
                } else {
                    ToastUtil.showShortToast("网络不佳");
                }
                break;
            case TIXIAN_NEW:
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                String format = sdf.format(new Date());
//
//                TiXianResultActivity.start(TiXianNewActivity.this, mDf.format(mTiXianAmt) + "", mFuWuFei + "", mIsSelectedShiShi?"实时到账":"次日到账", mBankName, format);
//
//                this.finish();
                break;

            case KAITONG_SHISHI:   // 开通实时到账
                if (map != null) {
                    String status = (String) map.get("status");
                    String msg = (String) map.get("msg");
//                    if ("0".equals(status)) {
//                        // 状态改成审核中
//                        initState("2");
//                    } else {
//                        if (!TextUtils.isEmpty(msg))
//                            ToastUtil.showShortToast(msg);
//                        else
//                            ToastUtil.showShortToast("开通失败");
//                    }

                }

                break;


        }
    }
}
