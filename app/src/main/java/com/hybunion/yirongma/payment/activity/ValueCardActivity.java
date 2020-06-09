package com.hybunion.yirongma.payment.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.payment.Fragment.LMFStoredValueCard;
import com.hybunion.yirongma.payment.Fragment.LMFSubscriptionInformationFragment;

public class ValueCardActivity extends BaseActivity {
    private FrameLayout hrt_my_information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_card);
        hrt_my_information = (FrameLayout) findViewById(R.id.hrt_my_information);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.hrt_my_information, new LMFStoredValueCard());
        transaction.commit();
    }
}
