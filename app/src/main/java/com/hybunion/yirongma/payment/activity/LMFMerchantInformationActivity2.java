package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseFragmentActivity;
import com.hybunion.yirongma.payment.Fragment.LMFSubscriptionInformationFragment;


/**
 * 签约信息
 */

public class LMFMerchantInformationActivity2 extends BaseFragmentActivity{
    private FrameLayout hrt_my_information;
    private InputMethodManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmf_merchant_infor2);
        hrt_my_information = (FrameLayout) findViewById(R.id.hrt_my_information);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.hrt_my_information, new LMFSubscriptionInformationFragment());
        transaction.commit();
    }

}
