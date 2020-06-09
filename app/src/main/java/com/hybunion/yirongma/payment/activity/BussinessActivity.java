package com.hybunion.yirongma.payment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

public class BussinessActivity extends BasicActivity {

    ExpandableListView expand_listView;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bussiness;
    }

    @Override
    public void initView() {
        super.initView();
        expand_listView = findViewById(R.id.expand_listView);
    }

}
