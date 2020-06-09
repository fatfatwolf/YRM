package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.utils.NameFilter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.MaxLenghtInputFilter;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 绑定收银插件
 */

public class BindQR65Activity extends BasicActivity {

    @Bind(R.id.bind_receipt_code_btn_scan)
    Button btnScan;
    @Bind(R.id.bind_receipt_code_edit_text_name)
    EditText mText;
    @Bind(R.id.bind_receipt_code_text_store)
    TextView storeOwned;
    String content;
    String storeId,storeName;

    public static void start(Context context, String storeName, String storeId){
        Intent intent = new Intent(context,BindQR65Activity.class);
        intent.putExtra("storeName",storeName);
        intent.putExtra("storeId",storeId);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_qr65;
    }

    @Override
    public void initView() {
        mText.setFilters(new InputFilter[]{new NameFilter(), new MaxLenghtInputFilter(30)});
        storeId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        storeOwned.setText(storeName);
    }


    @OnClick(R.id.bind_receipt_code_edit_text_name)
    public void editText() {
        String inputContent = mText.getText().toString().trim();
        if (!TextUtils.isEmpty(inputContent)) {
            mText.setSelection(inputContent.length());
        }
    }

    @OnClick(R.id.bind_receipt_code_btn_scan)
    public void onClick() {
        if (TextUtils.isEmpty(mText.getText().toString())) {
            HRTToast.showToast("名称不能为空", this);
            return;
        }
    }

}
