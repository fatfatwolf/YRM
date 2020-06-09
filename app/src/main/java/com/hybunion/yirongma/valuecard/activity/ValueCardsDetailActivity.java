package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.CheckVerificationCodeBean;
import com.hybunion.yirongma.payment.bean.ValueCardsDetailBean;
import com.hybunion.yirongma.payment.bean.VerifyingCode;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.OCJDesUtil;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.CashierInputFilter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.valuecard.view.CountDownTask;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/10.
 */

public class ValueCardsDetailActivity extends BasicActivity implements View.OnClickListener {
    private DecimalFormat df = new DecimalFormat("######0.00");
    @Bind(R.id.tv_titlebar_back_title)
    TextView title;
    @Bind(R.id.tv_card_type)
    TextView tv_card_type;
    @Bind(R.id.tv_card_money)
    TextView tv_card_money;
    @Bind(R.id.tv_card_type_company)
    TextView tv_card_type_company;
    @Bind(R.id.tv_card_number)
    TextView tv_card_number;
    @Bind(R.id.tv_card_valid)
    TextView tv_card_valid;
    @Bind(R.id.ll_value_card)
    LinearLayout ll_value_card;
    @Bind(R.id.et_put_money)
    EditText et_put_money;
    @Bind(R.id.tv_amount_of_payment)
    TextView amountOfPayment;
    @Bind(R.id.tv_discountRate)
    TextView tv_discountRate;
    @Bind(R.id.validation_code)
    Button validationCode;
    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.bt_submit)
    Button submit;
    public String code, cardNo, cardName, cardBalace, cardType, cardExpireDate, cardNumber, phone, discountRate, amountPayment, strsub, memId, putMoney;
    public static ValueCardsDetailActivity instance = null;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.value_cards_detail_ac;
    }

    @Override
    public void initView() {
        instance = this;
        cardNo = getIntent().getStringExtra("cardNo");
        cardName = getIntent().getStringExtra("cardName");
        cardBalace = getIntent().getStringExtra("cardBalace");
        cardType = getIntent().getStringExtra("cardType");
        cardExpireDate = getIntent().getStringExtra("cardExpireDate");
        phone = getIntent().getStringExtra("phone");
        discountRate = getIntent().getStringExtra("discountRate");
        memId = getIntent().getStringExtra("memId");
        title.setText(phone);
        tv_card_type.setText(cardName);
        tv_card_money.setText(cardBalace);
        formattingMethod(cardNo);
        tv_card_number.setText(cardNumber);
        tv_card_valid.setText("截止日期:" + cardExpireDate);
        if ("0".equals(cardType)) {
            tv_card_type_company.setText("次");
            et_put_money.setText("1");
            et_put_money.setFocusable(false);
            putMoney = et_put_money.getText().toString().trim();
            amountOfPayment.setText("实付次数：" + putMoney+"次");
            tv_card_type_company.setTextColor(getResources().getColor(R.color.ci_card_num));
            tv_card_money.setTextColor(getResources().getColor(R.color.ci_card_num));
            tv_card_type.setTextColor(getResources().getColor(R.color.ci_card));
            ll_value_card.setBackgroundResource(R.drawable.img_silvery_consumption_card);
            tv_card_number.setTextColor(getResources().getColor(R.color.ci_card_num));
            tv_card_valid.setTextColor(getResources().getColor(R.color.ci_card_num));
            et_put_money.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            et_put_money.setHint("请输入消费金额");
            tv_card_type_company.setText("元");
            if ("1".equals(cardType)) {
                tv_card_type.setTextColor(getResources().getColor(R.color.white));
                ll_value_card.setBackgroundResource(R.drawable.img_red_consumption_card);
                tv_card_number.setTextColor(getResources().getColor(R.color.white));
                tv_card_valid.setTextColor(getResources().getColor(R.color.white));
            } else if ("2".equals(cardType)) {
                tv_card_type.setTextColor(getResources().getColor(R.color.white));
                ll_value_card.setBackgroundResource(R.drawable.img_balck_consumption_card);
                tv_card_number.setTextColor(getResources().getColor(R.color.textColor_9));
                tv_card_valid.setTextColor(getResources().getColor(R.color.textColor_9));
            } else if ("3".equals(cardType)) {
                if (!TextUtils.isEmpty(discountRate)) {
                    tv_discountRate.setVisibility(View.VISIBLE);
                    tv_discountRate.setText("(" + discountRate + "折)");
                    tv_discountRate.setTextColor(getResources().getColor(R.color.zhekou_card_num));
                }
                tv_card_type.setTextColor(getResources().getColor(R.color.zhekou_card));
                ll_value_card.setBackgroundResource(R.drawable.img_golden_consumption_card);
                tv_card_money.setTextColor(getResources().getColor(R.color.zhekou_card_num));
                tv_card_type_company.setTextColor(getResources().getColor(R.color.zhekou_card_num));
                tv_card_number.setTextColor(getResources().getColor(R.color.zhekou_card_num));
                tv_card_valid.setTextColor(getResources().getColor(R.color.zhekou_card_num));
            } else if ("4".equals(cardType)) {
                tv_card_type.setTextColor(getResources().getColor(R.color.white));
                ll_value_card.setBackgroundResource(R.drawable.img_red_consumption_card);
                tv_card_number.setTextColor(getResources().getColor(R.color.white));
                tv_card_valid.setTextColor(getResources().getColor(R.color.white));
            }
            CashierInputFilter.setPricePoint(et_put_money);
        }
        et_put_money.addTextChangedListener(new MyTextChangeListener());
        validationCode.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    private class MyTextChangeListener implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null == s) {
                return;
            }
            putMoney = et_put_money.getText().toString().trim();
            if (TextUtils.isEmpty(putMoney)) {
                amountOfPayment.setText("实付金额：0元");
            } else {
                    //分为折扣卡 和 金额卡（任意金额卡）
                    BigDecimal b1 = new BigDecimal(putMoney);
                    if ("3".equals(cardType)) {
                        if (!"0".equals(discountRate)) {
                            if ("10".equals(discountRate)) {
                                amountPayment = et_put_money.getText().toString().trim();
                            } else {
                                BigDecimal b2 = new BigDecimal(discountRate);
                                BigDecimal b3 = new BigDecimal(10);
                                amountPayment = df.format(b1.multiply(b2.divide(b3)).doubleValue());//折扣卡实付金额
                                LogUtil.d(amountPayment + "=======折扣卡实付金额");
                                amountOfPayment.setText("实付金额：" + amountPayment + "元");
                            }
                        } else {
                            amountPayment = et_put_money.getText().toString().trim();
                            amountOfPayment.setText("实付金额：" + amountPayment + "元");
                        }
                    } else {
                        amountPayment = et_put_money.getText().toString().trim();
                        amountOfPayment.setText("实付金额：" + amountPayment + "元");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.validation_code:
                if ("0".equals(cardType)) {
                    code = validationCode.getText().toString().trim();
                    LogUtil.d(code + "=====");
                    if ("重新发送".equals(code) || "点击获取".equals(code)) {
                        et_put_money.setFocusable(false);
                        CountDownTask countDownTask = new CountDownTask(new CountDownTask.OnTimingChangeListener() {
                            @Override
                            public void onTimingChange(long milliseconds) {
                                validationCode.setText(String.valueOf(milliseconds / 1000) + "s");
                                if (milliseconds == 0) {
                                    validationCode.setText("重新发送");
                                }
                            }
                        });
                        countDownTask.setmMaxTryAgainTime(60000);
                        countDownTask.startCountdown();
                        if (cardNo.length() >= 4) {
                            strsub = cardNo.substring(cardNo.length() - 4);
                        }
                        repInfo(strsub, phone, "1次");
                    }
                } else {
                    LogUtil.d(et_put_money.getText().toString().trim() + "实付金额");
                    if (TextUtils.isEmpty(et_put_money.getText().toString().trim())) {
                        ToastUtil.show("请输入应付金额");
                        return;
                    } else if (Double.valueOf(amountPayment) < 0.01) {
                        ToastUtil.show("实付金额不能小于0.01元");
                    } else if (Double.parseDouble(amountPayment) > Double.parseDouble(df.format(Double.parseDouble(cardBalace)))) {
                        ToastUtil.show("实付金额大于储值卡余额");
                    } else {
                        code = validationCode.getText().toString().trim();
                        if ("重新发送".equals(code) || "点击获取".equals(code)) {
                            et_put_money.setFocusable(false);
                            CountDownTask countDownTask = new CountDownTask(new CountDownTask.OnTimingChangeListener() {
                                @Override
                                public void onTimingChange(long milliseconds) {
                                    validationCode.setText(String.valueOf(milliseconds / 1000) + "s");
                                    if (milliseconds == 0) {
                                        validationCode.setText("重新发送");
                                    }
                                }
                            });
                            countDownTask.setmMaxTryAgainTime(60000);
                            countDownTask.startCountdown();
                            if (cardNo.length() >= 4) {
                                strsub = cardNo.substring(cardNo.length() - 4);
                            }
                            repInfo(strsub, phone, putMoney+"元");
                        }
                    }
                    return;
                }
                break;
            case R.id.bt_submit:
                if (!"0".equals(cardType)) {
                    if (TextUtils.isEmpty(et_put_money.getText().toString().trim())) {
                        ToastUtil.show("请输入应付金额" );
                        return;
                    } else if (Double.valueOf(amountPayment) < 0.01) {
                        ToastUtil.show("实付金额不能小于0.01元" );
                    } else if (Double.parseDouble(amountPayment) > Double.parseDouble(df.format(Double.parseDouble(cardBalace)))) {
                        ToastUtil.show("输入金额大于储值卡余额" );
                    } else if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                        ToastUtil.show("请输入验证码" );
                    } else {
                        String etCode = et_code.getText().toString().trim();
                        repCheckInfo(phone, etCode);
                    }
                } else {
                    if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                        ToastUtil.show("请输入验证码" );
                    } else {
                        String etCode = et_code.getText().toString().trim();
                        repCheckInfo(phone, etCode);
                    }
                }
                break;
        }
    }

    public void repInfo(String strsub,String phone,String amountPayment) {
        String url = NetUrl.QUERY_CODE;
        JSONObject object = new JSONObject();
        try {
            object.put("agentId", GetApplicationInfoUtil.getAgentId());
            object.put("amount", amountPayment);
            object.put("cardLastFourNum", strsub);
            object.put("loginName", OCJDesUtil.encryptThreeDESECB(phone));
            object.put("sendMsgType", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ValueCardsDetailActivity.this, url, object, new MyOkCallback<VerifyingCode>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(VerifyingCode baseBean) {
                String stuta = baseBean.getStatus();
                if ("0".equals(stuta)){
                    String msg = baseBean.getMessage();
                    if(!TextUtils.isEmpty(msg))
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
                return VerifyingCode.class;
            }
        });
    }


    public void repCheckInfo(String phone,String code) {
        String url = NetUrl.QUERY_CHECK_CODE;

        JSONObject object = new JSONObject();
        try {
            object.put("loginName", phone);
            object.put("validateCode", code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ValueCardsDetailActivity.this, url, object, new MyOkCallback<CheckVerificationCodeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(CheckVerificationCodeBean bean) {
                String status = bean.getStatus();
                String message = bean.getMessage();
                if ("1".equals(status)) {
                    showLoading();
                    getConsumption(memId, cardNo, putMoney);
                } else {
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
                return CheckVerificationCodeBean.class;
            }
        });
    }



    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        if (type.equals(RequestIndex.VALUECARDCONSUMPTION)) {
            hideLoading();
            ValueCardsDetailBean bean = (ValueCardsDetailBean) map.get("bean");
            String status = bean.getStatus();
            if ("0".equals(status)) {
                Intent intent = new Intent(ValueCardsDetailActivity.this, VCConsumptionSuccessAT.class);
                intent.putExtra("phone", phone);
                intent.putExtra("orderNo", bean.getOrderNo());
                intent.putExtra("exchangeTim", bean.getExchangeTime());
                intent.putExtra("cardType", cardType);
                intent.putExtra("cardNo", cardNo);
                intent.putExtra("putMoney", et_put_money.getText().toString().trim());
                intent.putExtra("amountPayment", amountPayment);
                intent.putExtra("discountRate", discountRate);
                startActivity(intent);
                ValueCardsListActivity.instance.finish();
                this.finish();
            } else {
                ToastUtil.show("消费失败，请重试" );
            }
        } else if (type.equals(RequestIndex.CHECKCODE)) {
            CheckVerificationCodeBean bean = (CheckVerificationCodeBean) map.get("bean");
            String status = bean.getStatus();
            String message = bean.getMessage();
            if ("1".equals(status)) {
                showLoading();
                getConsumption(memId, cardNo, putMoney);
            } else {
                ToastUtil.show(message);
            }
        }
    }

    public void getConsumption(String memId, final String cardNo, final String amountPayment) {
        String url = NetUrl.VALUE_CARD_CONSUMPTION;
        JSONObject object = new JSONObject();
        try {
            object.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            object.put("payableAmount", amountPayment);
            object.put("memId", memId);
            object.put("cardNo", cardNo);
            object.put("vcOrderType", "3");
            object.put("payChannel","50");
            object.put("tradeType","APP");
            object.put("payAction", "vcardPayment.do");
        } catch (Exception e) {
            e.printStackTrace();
        }
       OkUtils.getInstance().post(ValueCardsDetailActivity.this, url, object, new MyOkCallback<ValueCardsDetailBean>() {
           @Override
           public void onStart() {
               showLoading();
           }

           @Override
           public void onSuccess(ValueCardsDetailBean bean) {
               String status = bean.getStatus();
               if ("0".equals(status)) {
                   Intent intent = new Intent(ValueCardsDetailActivity.this, VCConsumptionSuccessAT.class);
                   intent.putExtra("phone", phone);
                   intent.putExtra("orderNo", bean.getOrderNo());
                   intent.putExtra("exchangeTim", bean.getExchangeTime());
                   intent.putExtra("cardType", cardType);
                   intent.putExtra("cardNo", cardNo);
                   intent.putExtra("putMoney", et_put_money.getText().toString().trim());
                   intent.putExtra("amountPayment", amountPayment);
                   intent.putExtra("discountRate", discountRate);
                   startActivity(intent);
                   hideLoading();
                   ValueCardsListActivity.instance.finish();
                   ValueCardsDetailActivity.this.finish();
               } else {
                   ToastUtil.show("消费失败，请重试" );
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
               return null;
           }
       });
    }



    private void formattingMethod(String cardNo) {
        char[] array = cardNo.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if ((i + 1) % 4 == 0 && i != array.length - 1) {
                sb.append(" ");
            }
        }
        cardNumber = sb.toString();
    }

    @OnClick(R.id.ll_titlebar_back)
    public void goBack() {
        finish();
    }
}
