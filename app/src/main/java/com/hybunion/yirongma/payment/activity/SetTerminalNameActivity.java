package com.hybunion.yirongma.payment.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import butterknife.Bind;

public class SetTerminalNameActivity extends BasicActivity {
    @Bind(R.id.tv_storeName)
    TextView tv_storeName;
    @Bind(R.id.bt_save)
    Button bt_save;
    @Bind(R.id.et_codeName)
    EditText et_codeName;

    private String storeName,codeName;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_terminal_name;
    }

    @Override
    public void initView() {
        super.initView();
        storeName = getIntent().getStringExtra("storeName");
        if(!TextUtils.isEmpty(storeName)){
            tv_storeName.setText(storeName);
        }
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeName = et_codeName.getText().toString();
                if(codeName.equals("")){
                    ToastUtil.show("请输入收款码名称");
                    return;
                }
            }
        });
    }
}
