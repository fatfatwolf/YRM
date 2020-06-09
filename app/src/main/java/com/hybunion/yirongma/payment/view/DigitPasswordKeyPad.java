package com.hybunion.yirongma.payment.view;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.CommonMethod;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZWL on 2015/1/26.
 */
public class DigitPasswordKeyPad extends View {
    private Context ctx = null;
    private View v;
    private static String digitnum = "";
    private int length = 8;

    private Button digitkeypad_1;
    private Button digitkeypad_2;
    private Button digitkeypad_3;
    private Button digitkeypad_4;
    private Button digitkeypad_5;
    private Button digitkeypad_6;
    private Button digitkeypad_7;
    private Button digitkeypad_8;
    private Button digitkeypad_9;
    private Button digitkeypad_0;
    private Button digitkeypad_c;
    private Button digitkeypad_ok;
    private EditText digitkeypad_edittext;
    private EditText et_result;


    private boolean isPwd;
    private boolean flag = true;
    List<Integer> randomNum = new ArrayList<Integer>();
    private Button digitkeypad_cancel;

    public DigitPasswordKeyPad(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    public void setEditTextIsPwd(boolean ispwd) {
        if (ispwd) {
            digitkeypad_edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            digitkeypad_edittext.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        this.isPwd = ispwd;
    }

    CommonMethod.ConfirmListener confirmListener;

    public View setup(EditText view, CommonMethod.ConfirmListener confirmListener) {
        et_result = view;
        this.confirmListener = confirmListener;
        LayoutInflater lif = LayoutInflater.from(ctx);
        v = lif.inflate(R.layout.control_digitpasswordkeypad, null);

        // 初始化 对象
        digitkeypad_1 = (Button) v.findViewById(R.id.digitkeypad_1);
        digitkeypad_2 = (Button) v.findViewById(R.id.digitkeypad_2);
        digitkeypad_3 = (Button) v.findViewById(R.id.digitkeypad_3);
        digitkeypad_4 = (Button) v.findViewById(R.id.digitkeypad_4);
        digitkeypad_5 = (Button) v.findViewById(R.id.digitkeypad_5);
        digitkeypad_6 = (Button) v.findViewById(R.id.digitkeypad_6);
        digitkeypad_7 = (Button) v.findViewById(R.id.digitkeypad_7);
        digitkeypad_8 = (Button) v.findViewById(R.id.digitkeypad_8);
        digitkeypad_9 = (Button) v.findViewById(R.id.digitkeypad_9);
        digitkeypad_0 = (Button) v.findViewById(R.id.digitkeypad_0);
        digitkeypad_c = (Button) v.findViewById(R.id.digitkeypad_c);
        digitkeypad_ok = (Button) v.findViewById(R.id.digitkeypad_ok);
        digitkeypad_cancel = (Button) v.findViewById(R.id.digitkeypad_cancel);
        digitkeypad_edittext = (EditText) v.findViewById(R.id.digitpadedittext);

        // 添加点击事件
        DigitPasswordKeypadOnClickListener dkol = new DigitPasswordKeypadOnClickListener();
        digitkeypad_1.setOnClickListener(dkol);
        digitkeypad_2.setOnClickListener(dkol);
        digitkeypad_3.setOnClickListener(dkol);
        digitkeypad_4.setOnClickListener(dkol);
        digitkeypad_5.setOnClickListener(dkol);
        digitkeypad_6.setOnClickListener(dkol);
        digitkeypad_7.setOnClickListener(dkol);
        digitkeypad_8.setOnClickListener(dkol);
        digitkeypad_9.setOnClickListener(dkol);
        digitkeypad_0.setOnClickListener(dkol);
        digitkeypad_c.setOnClickListener(dkol);
        digitkeypad_ok.setOnClickListener(new DigitPasswordKeypadFinshOnClikcListener());
        digitkeypad_cancel.setOnClickListener(new DigitPasswordKeypadCancleOnClickListener());
        while (flag) {
            int random = (int) (Math.random() * 10);
            if (!randomNum.contains(random)) {
                randomNum.add(random);
                if (randomNum.size() == 10) {
                    flag = false;
                }
            }
        }
        digitkeypad_0.setText(randomNum.get(0) + "");
        digitkeypad_1.setText(randomNum.get(1) + "");
        digitkeypad_2.setText(randomNum.get(2) + "");
        digitkeypad_3.setText(randomNum.get(3) + "");
        digitkeypad_4.setText(randomNum.get(4) + "");
        digitkeypad_5.setText(randomNum.get(5) + "");
        digitkeypad_6.setText(randomNum.get(6) + "");
        digitkeypad_7.setText(randomNum.get(7) + "");
        digitkeypad_8.setText(randomNum.get(8) + "");
        digitkeypad_9.setText(randomNum.get(9) + "");

        return v;
    }


    private class DigitPasswordKeypadCancleOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            digitnum = "";
            confirmListener.cancel();
            CommonMethod.hidePassWdPadView();

        }
    }


    private class DigitPasswordKeypadFinshOnClikcListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.digitkeypad_ok) {
                Activity activity = null;
                try {
                    activity = (Activity) ctx;
                } catch (Exception e) {

                }
                if (!TextUtils.isEmpty(digitnum)&&digitnum.length()!=6){
                    if (activity != null && !activity.isFinishing())//页面加载完毕
                    {
                        digitnum="";
                        ToastUtil.shortShow(ctx, "密码格式输入错误");
                        return;
                    }
                }
                LogUtils.d("DigitPasswordKeypadFinshOnClikcListener");
                et_result.setText(digitnum);
                et_result.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                et_result.setSelection(digitnum.length());
                et_result.setFocusable(false);
                confirmListener.confirm(digitnum);
                digitnum = "";
                CommonMethod.hidePassWdPadView();
            }
        }
    }

    public void initInputLable(String str, int length) {
        str = str.trim();
        digitnum = str;
        this.length = length;
        digitkeypad_edittext.setText(digitnum);
        digitkeypad_edittext.setSelection(digitnum.length());
    }

    private class DigitPasswordKeypadOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int viewId = v.getId();

            switch (viewId) {
                case R.id.digitkeypad_1:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_1.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_2:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_2.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_3:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_3.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_4:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_4.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_5:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_5.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_6:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_6.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_7:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_7.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_8:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_8.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_9:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_9.getText().toString();
                    }
                    break;
                case R.id.digitkeypad_0:
                    if (digitnum.length() == length) {
                        return;
                    } else {
                        digitnum += digitkeypad_0.getText().toString();
                    }

                    break;
                case R.id.digitkeypad_c:// 后退
                    if (digitnum.length() > 0) {
                        digitnum = digitnum.substring(0, digitnum.length() - 1);
                    }
                    break;
            }
            // 格式化 数据
            digitkeypad_edittext.setText(digitnum);
            digitkeypad_edittext.setSelection(null != digitnum ? digitnum.length() : 0);
        }

    }


}
