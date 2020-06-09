package com.hybunion.yirongma.payment.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.CommonUtil;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.activity.ValueCardBillingDetailsAt;
import com.hybunion.yirongma.payment.utils.CommonMethodUtil;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity;
import com.hybunion.yirongma.valuecard.activity.LMFValueCardBalanceAT;
import com.hybunion.yirongma.valuecard.activity.VCManageActivity;
import com.hybunion.yirongma.valuecard.activity.ValueCardMakeActivity;
import com.hybunion.yirongma.valuecard.activity.ValueCardSummaryActivity;
import com.hybunion.yirongma.valuecard.activity.ValueCardsListActivity;

/**
 * @author SunBingbing
 * @date 2017/8/26
 * @email freemars@yeah.net
 * @description
 */

public class LMFStoredValueCard extends BaseFragment implements View.OnClickListener {
    private View root_view;
    private LinearLayout ll_lmf_cf, ll_lmf_report,ll_lmf_sc, ll_lmf_cd, ll_lmf_balance,ll_vc_consumption;
    private String permission,isJhMidBindTid,merchantName,areaType;
    private ImageView gallery;
    private TextView tv_head;
    String loginType,storeName;
    private CommonMethodUtil commonMethodUtil; // 工具方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.activity_czk, null, false);
            permission = SharedPreferencesUtil.getInstance(getActivity()).getKey("permission");
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        areaType =  SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.AREATYPE);
        merchantName= SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantName");
        loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        storeName = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeName");
        String aa = SharedPreferencesUtil.getInstance(getActivity()).getKey("newMerchantName");
        SharedPreferencesUtil.getInstance(getActivity()).putKey("aa",aa);
        LogUtil.d(merchantName+"商店名称");
        commonMethodUtil = new CommonMethodUtil();
        tv_head = (TextView) root_view.findViewById(R.id.tv_head);
        if("0".equals(loginType)){
            tv_head.setText(merchantName);
        }else {
            tv_head.setText(storeName);
        }
        ll_lmf_cf = (LinearLayout) root_view.findViewById(R.id.ll_lmf_cf);
        ll_lmf_report = (LinearLayout) root_view.findViewById(R.id.ll_lmf_report);
        ll_lmf_sc = (LinearLayout) root_view.findViewById(R.id.ll_lmf_sc);
        ll_lmf_cd= (LinearLayout) root_view.findViewById(R.id.ll_lmf_cd);
        ll_lmf_balance = (LinearLayout) root_view.findViewById(R.id.ll_lmf_balance);
        gallery = (ImageView) root_view.findViewById(R.id.gallery);
        ll_vc_consumption = (LinearLayout) root_view.findViewById(R.id.ll_vc_consumption);
        ll_vc_consumption.setOnClickListener(this);
        ll_lmf_cf.setOnClickListener(this);
        ll_lmf_report .setOnClickListener(this);
        ll_lmf_sc.setOnClickListener(this);
        ll_lmf_cd.setOnClickListener(this);
        ll_lmf_balance.setOnClickListener(this);
//        gallery.setOnClickListener(this);
        return root_view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_lmf_cf:
                if (CommonUtil.regex("储值卡交易流水", permission) || "-".equals(permission)) {
                    if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                        commonMethodUtil.showPopWindow(getActivity());
                    }else if ("1".equals(isJhMidBindTid)) {
                        if ("8".equals(areaType)) {
                            //提示上传资料
                            DistinguishTypeDialog();
                        } else {
                            scanningCodeDialog();
                        }
                    } else {
                        Intent transFlow = new Intent(getActivity(), ValueCardBillingDetailsAt.class);
                        startActivity(transFlow);
                    }
                } else {
                    ToastUtil.shortShow(getActivity(), "您没有此权限");
                }

                break;
            case R.id.ll_lmf_sc:
                if (CommonUtil.regex("储值卡制卡", permission) || "-".equals(permission)) {
                    if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                        commonMethodUtil.showPopWindow(getActivity());
                    }else if ("1".equals(isJhMidBindTid)) {
                        if ("8".equals(areaType)) {
                            //提示上传资料
                            DistinguishTypeDialog();
                        } else {
                            scanningCodeDialog();
                        }
                    } else {
                        Intent setRightIntent4 = new Intent(getActivity(), ValueCardMakeActivity.class);
                        startActivity(setRightIntent4);
                    }
                } else if ("1".equals(isJhMidBindTid)) {
                    scanningCodeDialog();
                } else {
                    ToastUtil.shortShow(getActivity(), "您没有此权限");
                }
                break;
            case R.id.ll_lmf_cd://卡管理界面
                if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                    commonMethodUtil.showPopWindow(getActivity());
                }else if ("1".equals(isJhMidBindTid)) {
                    if ("8".equals(areaType)) {
                        //提示上传资料
                        DistinguishTypeDialog();
                    } else {
                        scanningCodeDialog();
                    }
                } else {
                    Intent setRightIntent4 = new Intent(getActivity(), VCManageActivity.class);
                    startActivity(setRightIntent4);
                }

                break;
            case R.id.ll_lmf_balance://会员余额
                if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                    commonMethodUtil.showPopWindow(getActivity());
                }else if ("1".equals(isJhMidBindTid)) {
                    if ("8".equals(areaType)) {
                        //提示上传资料
                        DistinguishTypeDialog();
                    } else {
                        scanningCodeDialog();
                    }
                } else {
                    Intent memberBalanceIntent = new Intent(getActivity(), LMFValueCardBalanceAT.class);
                    startActivity(memberBalanceIntent);
                }
                break;
            case R.id.ll_lmf_report://储值卡汇总报表
                if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                    commonMethodUtil.showPopWindow(getActivity());
                }else if ("1".equals(isJhMidBindTid)) {
                    if ("8".equals(areaType)) {
                        //提示上传资料
                        DistinguishTypeDialog();
                    } else {
                        scanningCodeDialog();
                    }
                } else {
                    Intent summaryIntent = new Intent(getActivity(), ValueCardSummaryActivity.class);
                    startActivity(summaryIntent);
                }
                break;
            case R.id.ll_vc_consumption:
                if (TextUtils.isEmpty(SavedInfoUtil.getMid(getActivity()))) {
//                    commonMethodUtil.showPopWindow(getActivity());
                }else if ("1".equals(isJhMidBindTid)) {
                    if ("8".equals(areaType)) {
                        //提示上传资料
                        DistinguishTypeDialog();
                    } else {
                        scanningCodeDialog();
                    }
                } else {
                    inputPhoneDialog();
                }
                break;
            case R.id.gallery:
                Intent helpIntent=new Intent(getActivity(),LMFRedRainActivity.class);
                helpIntent.putExtra("webViewUrl","4");
                startActivity(helpIntent);
                break;
            default:
                break;
        }
    }
    //绑定二维码提示框
    public void scanningCodeDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.re_scanning_code_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogs);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.bt_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                intent.putExtra("flage", "1");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void inputPhoneDialog(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.input_phone_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogs);
        dialog.setContentView(view);
        final EditText etPhone = (EditText) view.findViewById(R.id.metName);
        Button btn_ok = (Button) view.findViewById(R.id.submit_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.submit_no);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString().trim();
                if("".equals(phone)){
                    ToastUtil.shortShow(getActivity()," 请输入手机号码");
                    return;
                }
                Intent intent=new Intent(getActivity(), ValueCardsListActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        isJhMidBindTid = SharedPreferencesUtil.getInstance(getActivity()).getKey("isJhMidBindTid");
//        merchantName= SharedPreferencesUtil.getInstance(getActivity()).getKey("newMerchantName");
//        tv_head.setText(merchantName);
    }
    public void DistinguishTypeDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.distinguish_type_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogs);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.bt_confirm_information);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), UplodeBandCardImgActivity.class);
//                startActivity(intent);
//                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
