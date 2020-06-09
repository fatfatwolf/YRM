package com.hybunion.yirongma.payment.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.AutonymCertificationInfoBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.FormatUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.view.MaxLenghtInputFilter;
import com.hybunion.yirongma.payment.view.NameInputFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/9/14.
 */

public class LMFEssentialInformationFragment extends LMFMerchantInformationFragment {
    TextView tv_1;
    TextView tv_2;
    TextView tv_3;
    TextView tv_4;
    TextView tv_5;
    TextView tv_6;
    TextView tv_7;
    TextView tv_8;
    TextView tv_9;
    TextView tv_10;
    private View root_view;
    private String msg, appStatus;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.activity_essential_information, null);
            initView(root_view);
            //queryMerchantStatusRequest();
            queryMerchantInfo();
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        return root_view;
    }

    private void initView(View view) {
        tv_1 = (TextView) view.findViewById(R.id.info_1);
        tv_2 = (TextView) view.findViewById(R.id.info_2);
        tv_2.setFilters(new InputFilter[]{new NameInputFilter(), new MaxLenghtInputFilter(30)});
        tv_3 = (TextView) view.findViewById(R.id.info_3);
        tv_4 = (TextView) view.findViewById(R.id.info_4);
        tv_5 = (TextView) view.findViewById(R.id.info_5);
        tv_6 = (TextView) view.findViewById(R.id.info_6);
        tv_7 = (TextView) view.findViewById(R.id.info_7);
        tv_8 = (TextView) view.findViewById(R.id.info_8);
        tv_9 = (TextView) view.findViewById(R.id.info_9);
        tv_10 = view.findViewById(R.id.info_10);
    }


    private void isNull( AutonymCertificationInfoBean.RowModel rowModel){
        if(rowModel.getMid() == null){
            rowModel.setMid("");
        }
        if(rowModel.getRname() == null){
            rowModel.setRname("");
        }
        if(rowModel.getRaddr() == null){
            rowModel.setRname("");
        }
        if(rowModel.getBno() == null){
            rowModel.setBno("");
        }
        if(rowModel.getBankBranch() == null){
            rowModel.setBankBranch("");
        }
    }
    /**
     * 查询商户信息，进行本地保存
     */
    private void queryMerchantInfo() {
        showProgressDialog("正在加载中");
        String url = NetUrl.QUERY_MERCHENT_INFO;

        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(getActivity()));
        OkUtils.getInstance().postFormData(getActivity(), url, map, new MyOkCallback<AutonymCertificationInfoBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(AutonymCertificationInfoBean bean) {
                if (!bean.isSuccess()){
                    ToastUtil.showShortToast("网络连接不佳");
                    return;
                }
                AutonymCertificationInfoBean.RowModel rowModel = bean.getObj().getRows();
                isNull(rowModel);
                tv_1.setText(rowModel.getMid());
                tv_2.setText(rowModel.getRname());
                tv_3.setText(rowModel.getRaddr());
                tv_4.setText(rowModel.getBno());
                tv_5.setText(FormatUtil.formatName(rowModel.getBankAccName()));
                tv_6.setText(FormatUtil.formatBankCard(rowModel.getBankAccNo()));
                tv_7.setText(rowModel.getBankBranch());
                if (TextUtils.isEmpty(rowModel.getAccNum())){
                    if (isLegalId(rowModel.getLegalNum())) {
                        tv_8.setText(FormatUtil.formatIDCard(rowModel.getLegalNum()));
                    } else {
                        tv_8.setText("");
                    }
                }else {
                    if (isLegalId(rowModel.getAccNum())) {
                        tv_8.setText(FormatUtil.formatIDCard(rowModel.getAccNum()));
                    } else {
                        tv_8.setText("");
                    }
                }
                tv_9.setText(FormatUtil.formatPhone(rowModel.getContactPhone()));
                if(null!=rowModel.getContactPerson()){
                    tv_10.setText(rowModel.getContactPerson());
                }
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.Name, rowModel.getBankAccName());  //账户名称
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.BANK_ACCNO, rowModel.getBankAccNo());  //结算账户
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.BANK_BRANCH, rowModel.getBankBranch());  //结算账户银行
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.LEGAL_NUM, rowModel.getLegalNum());  //身份证号
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.MERCHANT_NAME, rowModel.getRname());  //注册店名
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.CONTACT_PHONE, rowModel.getContactPhone());  //联系电话
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.ADDR, rowModel.getRaddr());  //注册地址
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.BNO, rowModel.getBno());  //营业执照号
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return AutonymCertificationInfoBean.class;
            }
        });

    }

    public static boolean isLegalId(String id) {
        return id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)");
    }





    public void telServoceDialog() {
        @SuppressLint("RestrictedApi") LayoutInflater inflater = this.getLayoutInflater(getArguments());
        View view = inflater.inflate(R.layout.tel_service_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogs);
        dialog.setContentView(view);
        TextView bt_sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView bt_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-010-5670"));
                startActivity(intent);

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
