package com.hybunion.yirongma.valuecard.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.adapter.ValueCardMakeAdapter;
import com.hybunion.yirongma.valuecard.model.CardRuleBean;
import com.hybunion.yirongma.valuecard.timepicker.DiaBirthHelper;
import com.hybunion.yirongma.valuecard.view.DialogValueCardMake;
import com.hybunion.yirongma.valuecard.view.ExpandList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述：制卡
 * 编写人： myy
 * 创建时间：2017/3/6
 */
public class ValueCardMakeActivity extends BaseActivity implements View.OnClickListener, ValueCardMakeAdapter.onMinusClickLisener {

    private EditText cardName;//卡名称
    private TextView cardKind;//卡种类
    private TextView cardType;//卡类型
    private EditText cardNumber;//卡数量
    private TextView cardTime;//截止日期
    private ImageView openPass;//密码设置
    private ImageView imgv_plus;//卡规则加号按钮
    private Button submit;//提交
    private ExpandList list_rule;//卡规则列表
    private EditText et_rechargeMoney;//充值
    private EditText et_getMoney;//送，得，享
    private TextView tv_song;//送，得，享
    private LinearLayout ll_back, lin_edittext;
    private Button btn_head_right;
    private TextView tv_head;
    private RelativeLayout rl_type;
    private List<CardRuleBean> mdataList = new ArrayList<>();//卡规则
    private int type;//卡规则类型,用于adapter里面显示相应的送，得，享信息
    private ValueCardMakeAdapter adapter;
    //卡名称,卡数量，卡种类（0：实体卡，1：电子卡）,卡类型（0：次卡，1：充送卡，2：折扣卡），密码（0：不需要，1：需要）给个默认值，用于dialog显示背景
    private String mCardName, mCardNumber, mCardKind = "3", mCardType = "", mCardType2 = "4", mPass = "0", merId;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean onPassFlag = false;//密码
    private String strPattern = "^[A-Za-z0-9\\u4e00-\\u9fa5]+$";
    private Pattern mPattern = Pattern.compile(strPattern);
    private TextView tv_cardAny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_card_make);
        initView2();
        initData2();
    }

    private void initView2() {
        cardName = (EditText) findViewById(R.id.et_CardName);
        cardNumber = (EditText) findViewById(R.id.et_CardNumber);
        cardKind = (TextView) findViewById(R.id.tv_CardKind);
        cardType = (TextView) findViewById(R.id.tv_CardType);
        cardTime = (TextView) findViewById(R.id.tv_CardTime);
        openPass = (ImageView) findViewById(R.id.Btn_openPass);
        submit = (Button) findViewById(R.id.btn_head_right);
        imgv_plus = (ImageView) findViewById(R.id.imgv_plus);
        list_rule = (ExpandList) findViewById(R.id.list_rule);
        et_rechargeMoney = (EditText) findViewById(R.id.et_rechargeMoney);
        et_getMoney = (EditText) findViewById(R.id.et_getMoney);
        tv_song = (TextView) findViewById(R.id.tv_song);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lin_edittext = (LinearLayout) findViewById(R.id.lin_edittext);
        btn_head_right = (Button) findViewById(R.id.btn_head_right);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_cardAny = (TextView) findViewById(R.id.card_any);
        cardKind.setOnClickListener(this);
        cardType.setOnClickListener(this);
        cardTime.setOnClickListener(this);
        openPass.setOnClickListener(this);
        imgv_plus.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        btn_head_right.setOnClickListener(this);
        et_rechargeMoney.setOnClickListener(this);
        et_getMoney.setOnClickListener(this);
        btn_head_right.setVisibility(View.VISIBLE);
        et_getMoney.setTextIsSelectable(false);
        cardName.setTextIsSelectable(false);
        cardNumber.setTextIsSelectable(false);
        et_getMoney.setLongClickable(false);
//        cardName.setLongClickable(false);
//        cardNumber.setLongClickable(false);
        et_getMoney.addTextChangedListener(textWatcherPay);
        cardName.addTextChangedListener(textWatcherName);
        cardNumber.addTextChangedListener(textWatcherNumber);
        et_rechargeMoney.addTextChangedListener(textWatcherPay2);

    }

    private void initData2() {
        btn_head_right.setText("提交");
        tv_head.setText("制卡");
        adapter = new ValueCardMakeAdapter(this, this);
        merId = SharedPreferencesUtil.getInstance(getApplicationContext()).getKey("merchantID");
    }


    @Override
    public void onMinusClick(int position) {
        adapter.removeList(position);
        List<CardRuleBean> alist = adapter.getDataList();
        mdataList.clear();
        if (null != alist) {
            mdataList.addAll(alist);
            if (mdataList.size() < 10) {
                lin_edittext.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Btn_openPass:
                if (onPassFlag) {
                    onPassFlag = false;
                    mPass = "0";
                    openPass.setImageResource(R.drawable.valuecardmake_off);
                } else {
                    onPassFlag = true;
                    mPass = "1";
                    openPass.setImageResource(R.drawable.valuecardmake_open);
                }
                break;

            case R.id.btn_head_right:
                showInput(et_getMoney, false);
                JSONArray arrayRule = new JSONArray();//存放交易规则
                List<CardRuleBean> tempList = new ArrayList<>();
                String cardnumber = cardNumber.getText().toString();
                String cardname = cardName.getText().toString().trim();

                if (" ".equals(cardname) || "".equals(cardname)) {
                    showToast("请输入卡名称");
                    return;
                } else if ("null".equalsIgnoreCase(cardname)) {
                    showToast("卡名称不能为null（无论大小写）");
                    return;
                } else {
                    mCardName = cardname;
                }
//                if ((!"0".equals(mCardKind)) && (!"1".equals(mCardKind))) {
//                    showToast("请选择卡种类");
//                    return;
//                }
                if ((!"0".equals(mCardType)) && (!"1".equals(mCardType))
                        && (!"2".equals(mCardType)) && (!"3".equals(mCardType))) {
                    showToast("请选择卡类型");
                    return;
                }
                if ("".equals(cardnumber) || "0".equals(cardnumber)) {
                    showToast("请输入卡数量");
                    return;
                } else if (Integer.parseInt(cardnumber) > 1000) {
                    showToast("最高限制1000张");
                    return;
                } else {
                    mCardNumber = cardnumber;
                }
                if ("".equals(cardTime.getText().toString())) {
                    showToast("请设置卡到期时间");
                    return;
                }

                if (null != mdataList) {
                    tempList.addAll(mdataList);
                }

                if ((!"".equals(et_rechargeMoney.getText().toString())) && (!"".equals(et_getMoney.getText().toString()))) {
                    CardRuleBean bean = new CardRuleBean();
                    bean.setChong(et_rechargeMoney.getText().toString());
                    bean.setSong(et_getMoney.getText().toString());
                    tempList.add(bean);
                }

                if ("3".equals(mCardType)){ // 任意金额卡不参与卡规则

                    onSubmit("1", mCardName, mCardNumber, new JSONArray().toString(),
                            mCardType, cardTime.getText().toString(), mPass, merId);

                }else {
                    if (null != tempList && tempList.size() > 10) {
                        showToast("储值卡规则最多十条");
                        return;
                    } else if (null != tempList && tempList.size() > 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(tempList.get(i).getChong());
                            jsonArray.put(tempList.get(i).getSong());
                            arrayRule.put(jsonArray);
                        }
                    } else {
                        showToast("请输入交易规则");
                        return;
                    }
                    onSubmit("1", mCardName, mCardNumber, arrayRule.toString(), mCardType,
                            cardTime.getText().toString(), mPass, merId);
                }


                break;
            case R.id.imgv_plus:
                if ((!"0".equals(mCardType)) && (!"1".equals(mCardType)) && (!"2".equals(mCardType))) {
                    showToast("请先选择储值卡类型");
                    et_rechargeMoney.setText("");
                    et_getMoney.setText("");
                    return;
                }
                if (mdataList.size() == 10 || mdataList.size() > 10) {
                    showToast("储值卡规则最多十条");
                    return;
                }
                CardRuleBean temp = new CardRuleBean();
                if ("".equals(et_rechargeMoney.getText().toString()) || "0".equals(et_rechargeMoney.getText().toString())) {
                    showToast("请输入充值金额");
                    return;
                } else if (Integer.parseInt(et_rechargeMoney.getText().toString()) > 50000 || Integer.parseInt(et_rechargeMoney.getText().toString()) < 0) {
                    showToast("充值金额范围为：（0,50000）");
                    return;
                } else {
                    temp.setChong(et_rechargeMoney.getText().toString());
                }
                if ("".equals(et_getMoney.getText().toString())) {
                    if ("0".equals(mCardType)) {
                        showToast("请输入次数");
                    } else if ("1".equals(mCardType)) {
                        showToast("请输入金额");
                    } else {
                        showToast("请输入折扣");
                    }
                    return;
                } else {
                    if ("2".equals(mCardType)) {
                        if (Float.parseFloat(et_getMoney.getText().toString()) > 10 || Float.parseFloat(et_getMoney.getText().toString()) < 1
                                || Float.parseFloat(et_getMoney.getText().toString()) == 1) {
                            showToast("折扣范围为：（1,10）");
                            return;
                        }
                    }
//                    if ("0".equals(mCardType) || "1".equals(mCardType)) {
//                        if (Integer.parseInt(et_getMoney.getText().toString()) == 0) {
//                            showToast("输入金额不可为0");
//                            return;
//                        }
//                    }
                    temp.setSong(et_getMoney.getText().toString());
                }

                mdataList.add(temp);
                if (mdataList.size() == 10) {
                    lin_edittext.setVisibility(View.GONE);
                    showInput(et_getMoney, false);
                }

                et_rechargeMoney.setText("");
                et_getMoney.setText("");
                et_rechargeMoney.setFocusable(true);
//                et_rechargeMoney.setFocusableInTouchMode(false);
                et_rechargeMoney.requestFocus();

                adapter.setList(mdataList);
                if (null == list_rule.getAdapter()) {
                    list_rule.setAdapter(adapter);
                }

                break;
            case R.id.tv_CardKind:
                DialogValueCardMake.Builder builder = new DialogValueCardMake.Builder(this, 2);
                builder.setTitle("选择卡种类");
                builder.setClick(Integer.parseInt(mCardKind));
                builder.onButtonClick1("实体卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == DialogValueCardMake.BUTTONONE) {
                            mCardKind = "0";
                            cardKind.setText("实体卡");
                        }
                        dialog.dismiss();
                    }
                });

                builder.onButtonClick2("电子卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogValueCardMake.BUTTONTWO) {
                            mCardKind = "1";
                            cardKind.setText("电子卡");
                        }
                        dialog.dismiss();
                    }
                });

                builder.onDeleteClick(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.tv_CardType:
                final DialogValueCardMake.Builder builderType = new DialogValueCardMake.Builder(this, 4);
                builderType.setTitle("选择卡类型");
                builderType.setClick(Integer.parseInt(mCardType2));
                builderType.onButtonClick1("充送卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogValueCardMake.BUTTONONE) {
                            et_getMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
                            type = ValueCardMakeAdapter.SONG;
                            clearList(type);
                            adapter.setType(type);
                            mCardType = "1";
                            mCardType2 = "0";
                            cardType.setText("充送卡");
                            tv_song.setText("送（元）");
                            et_getMoney.setText("");
                            et_rechargeMoney.setText("");
                            et_rechargeMoney.setHint("充值金额");
                            et_getMoney.setHint("赠送金额");
                        }
                        dialog.dismiss();
                        tv_cardAny.setVisibility(View.GONE);
                        lin_edittext.setVisibility(View.VISIBLE);
                    }
                });

                builderType.onButtonClick2("打折卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogValueCardMake.BUTTONTWO) {
                            et_getMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            type = ValueCardMakeAdapter.XIANG;
                            clearList(type);
                            adapter.setType(type);
                            mCardType = "2";
                            mCardType2 = "1";
                            cardType.setText("打折卡");
                            tv_song.setText("享（折）");
                            et_getMoney.setText("");
                            et_rechargeMoney.setText("");
                            et_rechargeMoney.setHint("充值金额");
                            et_getMoney.setHint("折扣");
                        }
                        dialog.dismiss();
                        tv_cardAny.setVisibility(View.GONE);
                        lin_edittext.setVisibility(View.VISIBLE);
                    }
                });

                builderType.onButtonClick3("次卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == DialogValueCardMake.BUTTONTHREE) {
                            et_getMoney.setInputType(InputType.TYPE_CLASS_NUMBER);
                            type = ValueCardMakeAdapter.DE;
                            clearList(type);
                            adapter.setType(type);
                            mCardType = "0";
                            mCardType2 = "2";
                            cardType.setText("次卡");
                            tv_song.setText("得（次）");
                            et_getMoney.setText("");
                            et_rechargeMoney.setText("");
                            et_rechargeMoney.setHint("充值金额");
                            et_getMoney.setHint("总次数");
                        }
                        dialog.dismiss();
                        tv_cardAny.setVisibility(View.GONE);
                        lin_edittext.setVisibility(View.VISIBLE);
                    }
                });

                builderType.onButtonClick4("任意金额卡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogValueCardMake.BUTTONFOUR) {
//                            et_getMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            type = ValueCardMakeAdapter.ANY;
                            clearList(type);
                            adapter.setType(type);
                            mCardType = "3";
                            mCardType2 = "3";
                            cardType.setText("任意金额卡");
//                            tv_song.setText("享（折）");
//                            et_getMoney.setText("");
//                            et_rechargeMoney.setText("");
//                            et_rechargeMoney.setHint("充值金额");
//                            et_getMoney.setHint("折扣");
                        }
                        dialog.dismiss();
                        tv_cardAny.setVisibility(View.VISIBLE);
                        lin_edittext.setVisibility(View.GONE);
                    }
                });
                builderType.onDeleteClick(new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderType.create().show();

                break;

            case R.id.tv_CardTime:
                DiaBirthHelper.Builder builderTime = new DiaBirthHelper.Builder(this);
                builderTime.setTitle("设置卡到期时间");
                builderTime.onButtonClick1(new DiaBirthHelper.onButtonClickLisener() {
                    @Override
                    public void onSubmit(DialogValueCardMake dialog, String time) {
                        String curTime = dateFormat.format(new Date(System.currentTimeMillis()));//获取当前时间
                        try {
                            if (dateFormat.parse(time).after(dateFormat.parse(curTime))) {
                                cardTime.setText(time);
                                dialog.dismiss();
                            } else {
                                showToast("截止日期必须大于当前日期");
                                return;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builderTime.onDeleteClick(
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builderTime.create().show();
                break;
            case R.id.ll_back:
                if ((!"".equals(cardName.getText().toString())) || (!"".equals(cardType.getText().toString())) || (!"".equals(cardNumber.getText().toString()))
                        || (!"".equals(cardTime.getText().toString())) || (!"".equals(et_rechargeMoney.getText().toString())) || (!"".equals(et_getMoney.getText().toString()))) {
                    showDialog();
                } else {
                    finish();
                }
                break;

            case R.id.et_rechargeMoney:
                et_rechargeMoney.setFocusable(true);
                et_rechargeMoney.setFocusableInTouchMode(true);
                et_rechargeMoney.requestFocus();
                showInput(et_rechargeMoney, true);
                break;
            case R.id.et_getMoney:
                et_getMoney.setFocusable(true);
                et_getMoney.setFocusableInTouchMode(true);
                et_getMoney.requestFocus();
                showInput(et_getMoney, true);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!"".equals(cardName.getText().toString())) || (!"".equals(cardType.getText().toString())) || (!"".equals(cardNumber.getText().toString()))
                    || (!"".equals(cardTime.getText().toString())) || (!"".equals(et_rechargeMoney.getText().toString())) || (!"".equals(et_getMoney.getText().toString()))) {
                showDialog();
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher textWatcherPay = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null == s) {
                return;
            }
            String temp = s.toString();
            if (TextUtils.isEmpty(temp)) {
                return;
            }

            if ("".equals(mCardType)) {
                if (temp.startsWith("0") && temp.length() > 1) {
                    et_getMoney.setText(temp.substring(1));
                    et_getMoney.setSelection(temp.length() - 1);
                }
            }

            if ("0".equals(mCardType)) {
                if (temp.startsWith("0")) {
                    et_getMoney.setText("");
                    showToast("总次数不能为0");
                }
                if (Integer.parseInt(temp) > 50000) {
                    showToast("总次数范围为：（0,50000）");
                    et_getMoney.setText(temp.substring(0, temp.length() - 1));
                    et_getMoney.setSelection(temp.length() - 1);
                }
            }

            if ("1".equals(mCardType)) {
                if (temp.startsWith("0") && temp.length() > 1) {
                    et_getMoney.setText(temp.substring(1));
                    et_getMoney.setSelection(temp.length() - 1);
                }
                if (Integer.parseInt(temp) > 50000) {
                    showToast("赠送金额范围为：（0,50000）");
                    et_getMoney.setText(temp.substring(0, temp.length() - 1));
                    et_getMoney.setSelection(temp.length() - 1);
                }
            }
            if ("2".equals(mCardType)) {

                if (temp.startsWith(".") || temp.startsWith("0")) {
                    et_getMoney.setText("");
                    return;
                }
                if (temp.length() > 1 && temp.contains(".")) {
                    int posDot = temp.indexOf(".");
                    if (posDot <= 0) return;
                    if (temp.length() - posDot - 1 > 2) {
                        s.delete(posDot + 3, posDot + 4);
                    }
                }
                if (Float.parseFloat(temp) > 10.0) {
                    showToast("折扣范围为：（1,10）");
                    et_getMoney.setText(temp.substring(0, temp.length() - 1));
                    et_getMoney.setSelection(temp.length() - 1);
                }
            }
        }
    };

    private TextWatcher textWatcherName = new TextWatcher() {

        String mOldStr = "";

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
            String tmp = s.toString();
            if (TextUtils.isEmpty(tmp)) {
                mOldStr = "";
                return;
            }
            Matcher aMatcher = mPattern.matcher(tmp);

            if (!aMatcher.matches()) {
                showToast("卡名称只允许输入汉字,字母,数字");
                cardName.setText(mOldStr);
                cardName.setSelection(mOldStr.length());
            } else {
                mOldStr = tmp;
            }
            if (mOldStr.toString().getBytes().length > 30) {
                cardName.setText(mOldStr.substring(0, mOldStr.length() - 1));
                cardName.setSelection(mOldStr.length());
                showToast("卡名称为10个汉字或20个字母或数字");
            }
            if (mOldStr.toString().length() > 20) {
                cardName.setText(mOldStr.substring(0, mOldStr.length() - 1));
                cardName.setSelection(mOldStr.length());
                showToast("卡名称为10个汉字或20个字母或数字");
            }
        }
    };

    private TextWatcher textWatcherPay2 = new TextWatcher() {


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
            String tmp = s.toString();
            if (TextUtils.isEmpty(tmp)) {
                return;
            }

            if (tmp.startsWith("0")) {
                et_rechargeMoney.setText("");
                showToast("充值金额不能为0");
            }
            if (Integer.parseInt(tmp) > 50000) {
                showToast("充值金额范围为：（0,50000）");
                et_rechargeMoney.setText(tmp.substring(0, tmp.length() - 1));
                et_rechargeMoney.setSelection(tmp.length() - 1);
            }
        }
    };

    private TextWatcher textWatcherNumber = new TextWatcher() {


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
            String tmp = s.toString();
            if (TextUtils.isEmpty(tmp)) {
                return;
            }

            if (tmp.startsWith("0")) {
                cardNumber.setText("");
            }
            if (Integer.parseInt(tmp) > 1000) {
                showToast("最高限制1000张");
                cardNumber.setText(tmp.substring(0, tmp.length() - 1));
                cardNumber.setSelection(cardNumber.length());
                return;
            }

        }
    };

    private void onSubmit(String cardKind, String cardName, String cardNumber, String cardRechargeRule, String cardType, String expireDate, String isNeedPwd, String merId) {
        showProgressDialog("");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if ("0".equals(status)) {
                        ValueCardMakeActivity.this.finish();
                    }
                    LogUtils.d("myy", "message=" + message);
                    showToast(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorlistener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "请稍后重试",
                        Toast.LENGTH_SHORT).show();
            }
        };

        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("cardKind", cardKind);
            dataParam.put("cardName", cardName);
            dataParam.put("cardNumber", cardNumber);
            dataParam.put("cardRechargeRule", cardRechargeRule);
            dataParam.put("cardType", cardType);
            dataParam.put("expireDate", expireDate);
            dataParam.put("isNeedPwd", isNeedPwd);
            dataParam.put("merId", merId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(this)
                .addRequestWithHeader(listener,errorlistener,dataParam, NetUrl.NEWMAKECARD);
    }

    private void showToast(String message) {
        Toast.makeText(ValueCardMakeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearList(int type) {
        if (adapter.getType() != type) {
            if (null != adapter.getDataList() && adapter.getDataList().size() > 0) {
                adapter.getDataList().clear();
                adapter.notifyDataSetChanged();
            }
            if (null != mdataList && mdataList.size() > 0) {
                mdataList.clear();
            }
        }
    }

    /**
     * 弹起隐藏软键盘
     *
     * @param et   要绑定软键盘的edittext
     * @param flag true为弹起，false为隐藏
     */
    private void showInput(EditText et, boolean flag) {
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        if (flag) {
            im.showSoftInput(et, 0);
        } else {
            // im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            im.hideSoftInputFromWindow(et.getWindowToken(), 0);
            et_getMoney.clearFocus();
            et_rechargeMoney.clearFocus();
        }
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText("退出将不保存填写内容");
        btn_cancel.setText("取消");
        btn_ok.setText("确定");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ValueCardMakeActivity.this.finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
