package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.Fragment.TerminalListFragment;
import com.hybunion.yirongma.payment.Fragment.YunCloudFragment;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

public class TerminalManageActivity extends BasicActivity implements View.OnClickListener{
    View view1;
    View view2;
    TextView tv_terminal_list;
    TextView tv_cloud_list;
    FrameLayout fl_manage;
    LinearLayout ll_back;
    TextView tv_head;
    String storeId,storeName,type;
    private int current_click = 1;
    TerminalListFragment terminalListFragment;
    YunCloudFragment yunCloudFragment;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_terminal_manage;
    }


    @Override
    public void initView() {
        super.initView();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setText("终端管理");
        fl_manage = (FrameLayout) findViewById(R.id.fl_manage);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        tv_terminal_list = (TextView) findViewById(R.id.tv_terminal_list);
        tv_cloud_list = (TextView) findViewById(R.id.tv_cloud_list);
        ll_back.setOnClickListener(this);
        tv_terminal_list.setOnClickListener(this);
        tv_cloud_list.setOnClickListener(this);
        terminalListFragment= new TerminalListFragment();
        storeId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        type = getIntent().getStringExtra("type");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("storeId",storeId);
        bundle.putString("storeName",storeName);
        bundle.putString("type",type);
        terminalListFragment.setArguments(bundle);
        transaction.add(R.id.fl_manage, terminalListFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_terminal_list:
                tv_terminal_list.setTextColor(getResources().getColor(R.color.lmf_main_color));
                tv_terminal_list.setTextSize(17);
                tv_cloud_list.setTextColor(getResources().getColor(R.color.text_color2));
                tv_cloud_list.setTextSize(14);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                if(current_click != 1){
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    if(terminalListFragment == null){
                        transaction1.add(R.id.fl_manage,terminalListFragment);
                    }else {
                        transaction1.show(terminalListFragment);
                    }
                    if(yunCloudFragment!=null){
                        transaction1.hide(yunCloudFragment);
                    }
                    transaction1.commit();
                    current_click = 1;
                }
                break;
            case R.id.tv_cloud_list:
                tv_cloud_list.setTextColor(
                        getResources().getColor(R.color.lmf_main_color));
                tv_cloud_list.setTextSize(17);
                tv_terminal_list.setTextColor(getResources().getColor(R.color.text_color2));
                tv_terminal_list.setTextSize(14);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                if(current_click != 2){
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    if(terminalListFragment !=null){
                        transaction1.hide(terminalListFragment);
                    }

                    if(yunCloudFragment == null){
                        yunCloudFragment = new YunCloudFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("storeId",storeId);
                        bundle1.putString("storeName",storeName);
                        bundle1.putString("type",type);
                        yunCloudFragment.setArguments(bundle1);
                        transaction1.add(R.id.fl_manage,yunCloudFragment);
                    }else {
                        transaction1.show(yunCloudFragment);
                    }
                    transaction1.commit();
                    current_click = 2;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //绑定终端，刷新页面回调
        if(resultCode == 123){
            if(terminalListFragment!=null){
                terminalListFragment.queryTerminalList(0);
            }
        }

        //绑定云喇叭，刷新页面回调
        if(resultCode == 122){
            if(yunCloudFragment!=null)
                yunCloudFragment.queryCollectCode(0);
        }
    }
}
