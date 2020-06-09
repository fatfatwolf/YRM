package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseFragmentActivity;
import com.hybunion.yirongma.payment.Fragment.LMFEssentialInformationFragment;


/**
 * Created by admin on 2017/9/14.
 */

public class LMFMerchantInformationActivity extends BaseFragmentActivity{
//    private Button hrt_essential_information;
//    private ImageView ll_titlebar_back;
    private FrameLayout hrt_my_information;
    private int current_click = 1;
    private InputMethodManager manager;
    private static final int HRT_QUERYMEM_BT_ESSENTIAL_INFOR__IS_CLICK = 1;
    private static final int HRT_QUERYMEM_BT_SUBSCRIPTION_INFOR_IS_CLICK = 2;
    private static final int HRT_QUERYMEM_BT_MORE_IS_CLICK = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmf_merchant_infor);
//        hrt_essential_information = (Button) findViewById(R.id.hrt_essential_information);
        hrt_my_information = (FrameLayout) findViewById(R.id.hrt_my_information);
//        hrt_essential_information.setOnClickListener(this);
//        ll_titlebar_back = (ImageView) findViewById(R.id.ll_titlebar_back) ;
//        ll_titlebar_back.setOnClickListener(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.hrt_my_information, new LMFEssentialInformationFragment());
        transaction.commit();
    }


}
