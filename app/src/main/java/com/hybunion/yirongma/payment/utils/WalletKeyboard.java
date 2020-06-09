package com.hybunion.yirongma.payment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by king on 2016/8/15.
 */
public class WalletKeyboard extends LinearLayout {

    private TextView btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn_clear,btnDot,ok;
    LinearLayout btnDel;
    private String lastAmt = "";
    private KeyboardListener mListener;

    public WalletKeyboard(Context context) {
        super(context);
    }

    public WalletKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.wallet_keyboard, this, true);
        ButterKnife.bind(this);
        initView();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        btn0 = (TextView) findViewById(R.id.btn_0);
        btn1 = (TextView) findViewById(R.id.btn_1);
        btn2 = (TextView) findViewById(R.id.btn_2);
        btn3 = (TextView) findViewById(R.id.btn_3);
        btn4 = (TextView) findViewById(R.id.btn_4);
        btn5 = (TextView) findViewById(R.id.btn_5);
        btn6 = (TextView) findViewById(R.id.btn_6);
        btn7 = (TextView) findViewById(R.id.btn_7);
        btn8 = (TextView) findViewById(R.id.btn_8);
        btn9 = (TextView) findViewById(R.id.btn_9);
        btn_clear = (TextView) findViewById(R.id.btn_clear);
        ok = (TextView) findViewById(R.id.ok_wallet_keyboard);
        btnDel = (LinearLayout) findViewById(R.id.btn_del);
        btnDot = (TextView) findViewById(R.id.btn_dot);
    }

    @OnClick(R.id.btn_0)
    public void onClickBtn0() {
        LogUtil.d("run here");
        setAmt("0");
    }

    @OnClick(R.id.btn_1)
    public void onClickBtn1() {
        setAmt("1");
    }

    @OnClick(R.id.btn_2)
    public void onClickBtn2() {
        setAmt("2");
    }

    @OnClick(R.id.btn_3)
    public void onClickBtn3() {
        setAmt("3");
    }

    @OnClick(R.id.btn_4)
    public void onClickBtn4() {
        setAmt("4");
    }

    @OnClick(R.id.btn_5)
    public void onClickBtn5() {
        setAmt("5");
    }

    @OnClick(R.id.btn_6)
    public void onClickBtn6() {
        setAmt("6");
    }

    @OnClick(R.id.btn_7)
    public void onClickBtn7() {
        setAmt("7");
    }

    @OnClick(R.id.btn_8)
    public void onClickBtn8() {
        setAmt("8");
    }

    @OnClick(R.id.btn_9)
    public void onClickBtn9() {
        setAmt("9");
    }

    @OnClick(R.id.btn_dot)
    public void onClickBtnDot() {
        setAmt(".");
    }

    @OnClick(R.id.btn_clear)
    public void onClickClear(){
        changeAmt("0.00");
        lastAmt = "";
    }
    @OnClick(R.id.btn_del)
    public void onClickBtnDel() {
        clearAmt();
    }
    @OnClick(R.id.ok_wallet_keyboard)
    public void ok(){   // 确认支付
        if (mListener!=null)
            mListener.tixian();
    }

    private void setAmt(String num) {
        if (lastAmt.replace(".", "").length() >= 9) {
            return;
        }
        String afterAmt = lastAmt + num;
        setTextOnScreen(afterAmt);
    }

    private void setTextOnScreen(String s) {
        if ("".equals(s)){
            changeAmt("0.00");
//            amt.setTextColor(getResources().getColor(R.color.textColorCar));
            lastAmt = s;
            return;
        }
        if (s.startsWith(".")) {
            s = "0.";
        }
        if (s.startsWith("0") && s.length() > 1 && !s.startsWith("0.")) {
            s = s.substring(1);
        }
        if (s.contains(".")) {
            btnDot.setEnabled(false);
            int dotIndex = s.indexOf(".");
            if (s.length() > dotIndex + 3) {
                s = s.substring(0, dotIndex + 3);
            }
        } else {
            btnDot.setEnabled(true);
        }
        changeAmt(s);
//        amt.setTextColor(getResources().getColor(R.color.black));
        lastAmt = s;
    }

    private void clearAmt() {

        if (lastAmt.equals("0.00")) {
            return;
        }

        if (lastAmt.length()>1){
            String afterDelAmt = lastAmt.substring(0, lastAmt.length() - 1);
            setTextOnScreen(afterDelAmt);
        }else {
            setTextOnScreen("");
        }
    }

    public void setKeyboardListener(KeyboardListener listener){
        mListener = listener;
    }

    // 金额更改、确认提现 监听
    public interface KeyboardListener{
        void amtChanged(String amt);
        void tixian();
    }

    private void changeAmt(String amt){
        if (mListener!=null)
            mListener.amtChanged(amt);
    }

    // 外面调的 清除金额 的方法
    public  void clear(){
        changeAmt("0.00");
        lastAmt = "";
    }


}



