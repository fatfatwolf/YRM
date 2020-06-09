package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtil;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by king on 2016/8/15.
 */
public class Keyboard extends LinearLayout{

    TextView btn0;

    TextView btn1;

    TextView btn2;

    TextView btn3;

    TextView btn4;

    TextView btn5;

    TextView btn6;

    TextView btn7;

    TextView btn8;

    TextView btn9;

    TextView btnac;

    ImageView btnDel;

    LinearLayout btnConfirm;

    EditText amt;

    private ConfirmListener listener;

    private boolean changed = false;

    private DecimalFormat df = new DecimalFormat("0.00");

    public Keyboard(Context context) {
        super(context);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.input_amt, this, true);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView(){
       // amt = (EditText) findViewById(R.id.tv_amt);
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
       /* btnac = (TextView) findViewById(R.id.btn_clear);
        btnDel = (ImageView) findViewById(R.id.btn_del);
        //btnDot = (TextView) findViewById(R.id.btn_dot);
        btnConfirm = (LinearLayout) findViewById(R.id.btn_confirm);*/


    }

    @OnClick(R.id.btn_0)
    public void onClickBtn0(){
        LogUtil.d("run here");
        setAmt("0");
    }

    @OnClick(R.id.btn_1)
    public void onClickBtn1(){
        setAmt("1");
    }

    @OnClick(R.id.btn_2)
    public void onClickBtn2(){
        setAmt("2");
    }

    @OnClick(R.id.btn_3)
    public void onClickBtn3(){
        setAmt("3");
    }

    @OnClick(R.id.btn_4)
    public void onClickBtn4(){
        setAmt("4");
    }

    @OnClick(R.id.btn_5)
    public void onClickBtn5(){
        setAmt("5");
    }

    @OnClick(R.id.btn_6)
    public void onClickBtn6(){
        setAmt("6");
    }

    @OnClick(R.id.btn_7)
    public void onClickBtn7(){
        setAmt("7");
    }

    @OnClick(R.id.btn_8)
    public void onClickBtn8(){
        setAmt("8");
    }

    @OnClick(R.id.btn_9)
    public void onClickBtn9(){
        setAmt("9");
    }

    /*@OnClick(R.id.btn_dot)
    public void onClickBtnDot(){
        setAmt(".");
    }*/

  /*  @OnClick(R.id.btn_clear)
    public void onClickBtnAc(){
        amt.setText("0.00");
    }*/

    @OnClick(R.id.btn_del)
    public void onClickBtnDel(){
        clearAmt();
    }

    @OnClick(R.id.btn_confirm)
    public void onClickBtnConfirm(){
        if ((amt.getText().toString()).matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")){
            listener.onClick();
        }
    }

    private void setAmt(String num) {
        if (amt.getText().toString().length() > 9)
            return;
        amt.setText(amt.getText().toString() + num);
    }

    private void clearAmt() {
        int len = amt.getText().toString().length();
        if(len != 0)
            amt.setText(amt.getText().toString().substring(0, len - 1));
    }

    private void initData(){
        amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (changed) {
                    changed = !changed;
                    return;
                }
                changed = !changed;
                String string = (s.toString()).replace(".", "");
                int amtInt = Integer.parseInt(string);
                amt.setText(df.format(amtInt / 100.0));
                amt.setSelection(amt.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((amt.getText().toString()).matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")) {
                    btnConfirm.setEnabled(true);
                }else{
                    btnConfirm.setEnabled(false);
                }
            }
        });
    }

    public void setConfirmListener(ConfirmListener listener){
        this.listener = listener;
    }

    public String getAmount(){
        return amt.getText().toString().trim();
    }

    public interface ConfirmListener {
        void onClick();
    }




}
