package com.hybunion.yirongma.valuecard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.view.ImIput;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 储值卡-激活-储值卡激活页面-点击激活
 * Created by lyf on 2016/9/21.
 */

public class ValueCardActivateActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private LinearLayout btn_back;
    private ImIput imiput_startNum, imiput_endNum, imiput_recharge, imiput_seller;
    private Button btn_activate;

    private String firstNum, lastNum, payAmount, rechargeAmount;
    private static final String TRANSTERTYPE = "2";
    private String merchantID, loginName;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuecard_activate);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("储值卡激活");

        //起始编号
        imiput_startNum = (ImIput) findViewById(R.id.imiput_startNum);
        //结束编号
        imiput_endNum = (ImIput) findViewById(R.id.imiput_endNum);
        //支付金额
        imiput_recharge = (ImIput) findViewById(R.id.imiput_recharge);
        imiput_recharge.addTextChangedListener(inputWatcher);
        //售卡金额
        imiput_seller = (ImIput) findViewById(R.id.imiput_seller);
        imiput_seller.addTextChangedListener(textWatcher);
        btn_activate = (Button) findViewById(R.id.btn_activate);
        btn_activate.setOnClickListener(this);
        merchantID = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        loginName = SharedPreferencesUtil.getInstance(this).getKey("loginNumber");

        Intent intent = getIntent();
        if ("VCActivateHomeAdapter".equals(intent.getStringExtra("flag"))) {
            if (!TextUtils.isEmpty(intent.getStringExtra("cardSeq"))) {
                imiput_startNum.setText(intent.getStringExtra("cardSeq"));
                imiput_startNum.setSelection(intent.getStringExtra("cardSeq").length());//定位光标的位置
                imiput_endNum.setText(intent.getStringExtra("cardSeq"));
            }
        } else {
            if (!TextUtils.isEmpty(intent.getStringExtra("startNum"))) {
                imiput_startNum.setText(intent.getStringExtra("startNum"));
                imiput_startNum.setSelection(intent.getStringExtra("startNum").length());
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("endNum"))) {
                imiput_endNum.setText(intent.getStringExtra("endNum"));
                imiput_endNum.setSelection(intent.getStringExtra("endNum").length());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
                break;
            case R.id.btn_activate:
                firstNum = imiput_startNum.getText().toString();
                lastNum = imiput_endNum.getText().toString();
                //充值金额
                rechargeAmount = imiput_recharge.getText().toString();
                //售卡金额
                payAmount = imiput_seller.getText().toString();
                // 判断起始编号
                if (TextUtils.isEmpty(firstNum)) {
                    ToastUtil.show("请输入起始编号");
                    // 判断充值金额
                } else if (TextUtils.isEmpty(rechargeAmount) || Double.valueOf(rechargeAmount) == 0) {
                    ToastUtil.show("请输入充值金额或次数");

                } else if (Double.valueOf(rechargeAmount) > 50000){
                    ToastUtil.show("充值金额或次数不能大于50000");

                } else {
                    // 结束编号可以不输入（默认和起始编号一致）
                    if (TextUtils.isEmpty(lastNum)) {
                        imiput_endNum.setText(firstNum);
                        lastNum = firstNum;
                        imiput_endNum.setSelection(imiput_endNum.getText().toString().length());
                    } else if (firstNum.compareTo(lastNum) > 0) {
                        Toast.makeText(this, "结束编号需大于等于起始编号", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 售卡金额可以不输入（默认和充值金额一致）
                    if (TextUtils.isEmpty(payAmount)) {
                        imiput_seller.setText(rechargeAmount);
                        payAmount = rechargeAmount;
                        imiput_seller.setSelection(imiput_seller.getText().toString().length());
                    }

                    showInput(imiput_seller, false);
                    activateValueCard(firstNum, lastNum, payAmount, rechargeAmount, merchantID, loginName);
                }
                break;
            default:
                break;
        }
    }

    //激活储值卡
    private void activateValueCard(final String firstNum2, final String lastNum2, String payAmount2, String rechargeAmount2, String merId, String merName) {
        showProgressDialog("");
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if ("0".equals(status)) {
                        Intent intent = getIntent();
                        intent.putExtra("startNum", firstNum2);
                        intent.putExtra("endNum", lastNum2);
                        setResult(RESULT_OK, intent);
                        ValueCardActivateActivity.this.finish();
                        Toast.makeText(ValueCardActivateActivity.this, message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ValueCardActivateActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(ValueCardActivateActivity.this, getResources().getString(R.string.poor_network), Toast.LENGTH_LONG)
                        .show();

            }
        };
        JSONObject jsonRequest;
        try {

            JSONObject body = new JSONObject();
            body.put("firstNum", firstNum2);
            body.put("lastNum", lastNum2);
            body.put("merId", merId);
            body.put("merName", merName);
            body.put("payAmount", payAmount2);
            body.put("rechargeAmount", rechargeAmount2);
            body.put("transTerType", TRANSTERTYPE);
            jsonRequest = new JSONObject();
            jsonRequest.put("body", body);
            VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorListener,
                    jsonRequest, NetUrl.BATCH_ACTIVATED_CARD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
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
            String temp = s.toString();
            if (temp.startsWith(".")) {
                imiput_seller.setText("");
            }
            if (temp.length() > 1 && temp.startsWith("0") && !temp.startsWith("0.")) {
                imiput_seller.setText(temp.substring(1));
                imiput_seller.setSelection(imiput_seller.length());
            }
            int posDot = temp.indexOf(".");
            if (posDot <= 0) return;

            if (temp.length() - posDot - 1 > 2) {
                s.delete(posDot + 3, posDot + 4);
            }
        }
    };

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (null == editable) {
                return;
            }

            String temp = editable.toString();

            // 如果结束编号没有的话，帮助填入默认值
            if (TextUtils.isEmpty(imiput_endNum.getText().toString())){
                imiput_endNum.setText(imiput_startNum.getText().toString());
            }

            if (temp.length() > 1 && temp.startsWith("0")){
                imiput_recharge.setText(temp.substring(1));
                imiput_recharge.setSelection(imiput_recharge.length());
            }

        }
    };

    /**
     * 控制软键盘弹出隐藏
     *
     * @param et   绑定软键盘的edittext
     * @param flag true弹起，false隐藏
     */
    private void showInput(EditText et, boolean flag) {
        InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
        if (flag) {
            im.showSoftInput(et, 0);
        } else {
            // im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            im.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }
}
