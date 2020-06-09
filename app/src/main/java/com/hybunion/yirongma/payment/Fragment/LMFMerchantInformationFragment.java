package com.hybunion.yirongma.payment.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.view.MyDialogView;

/**
 * Created by admin on 2017/9/14.
 */

public class LMFMerchantInformationFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    protected RelativeLayout mProgressDialog;
    private View activity_dialog;
    private TextView tv_loading_title;
    private MyDialogView myDialogView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li = LayoutInflater.from(getActivity());
        mProgressDialog = (RelativeLayout) li.inflate(R.layout.dialog_progressbar, null);
        tv_loading_title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
        activity_dialog = getActivity().getLayoutInflater().inflate(R.layout.activity_dialog_icon, null);
        myDialogView = (MyDialogView) activity_dialog.findViewById(R.id.myDialog);

    }
    public static LMFMerchantInformationFragment newInstance(int position) {
        LMFMerchantInformationFragment f=null;
        switch (position) {
            case 0:
//                f = new LMFMerchantInforMoreFragment();
                break;
            case 1:
                f = new LMFSubscriptionInformationFragment();
                break;
            case 2:
                f = new LMFEssentialInformationFragment();
                break;
        }
        Bundle b=new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }
    public void showProgressDialog(String title) {
        if (title != null && !"".equals(title)) {
            tv_loading_title.setText(title);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
                ViewGroup top = (ViewGroup) getActivity().getWindow().getDecorView();
                top.addView(activity_dialog);
                myDialogView.startAnim();
            }
        });
    }

    public void hideProgressDialog() {
        Context context = getActivity();
        if (context == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
            }
        });
    }
}
