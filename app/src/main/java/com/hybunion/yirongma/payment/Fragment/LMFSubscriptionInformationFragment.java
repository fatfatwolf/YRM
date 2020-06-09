package com.hybunion.yirongma.payment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.CollectionCodeBean;
import com.hybunion.yirongma.payment.bean.SingInfoBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/9/14.
 */

public class LMFSubscriptionInformationFragment extends LMFMerchantInformationFragment implements View.OnClickListener{
    private View root_view;
    TextView info_1,info_2,info_3,info_4,info_5,info_6,info_7,tv_info_9;
    RelativeLayout rl_info_1,rl_info_2,rl_info_3,rl_info_4,rl_info_5,rl_info_6,rl_info_7,rl_info_8,rl_info_9;
    View view2,view3,view7,view8;
    private TextView tv_popmd_md, tv_popmd_t1, tv_popmd_qx;
    LinearLayout rel_settlement;
    SingInfoBean dataInfo;
    String cycle;
    private View view;
    private PopupWindow popup;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.activity_lmf_sing_info_payment, null);
            initView(root_view);
            getQRCodeList();
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        return root_view;
    }
    private void initView(View view){
        info_1 = (TextView) view.findViewById(R.id.info_1);
        info_2 = (TextView) view.findViewById(R.id.info_2);
        info_3 = (TextView) view.findViewById(R.id.info_3);
        info_4 = (TextView) view.findViewById(R.id.info_4);
        info_5 = (TextView) view.findViewById(R.id.info_5);
        info_6 = (TextView) view.findViewById(R.id.info_6);
        info_7 = (TextView) view.findViewById(R.id.info_7);
        tv_info_9 = view.findViewById(R.id.tv_info_9);
        rl_info_1 = (RelativeLayout) view.findViewById(R.id.rl_info_1);
        rl_info_2 = (RelativeLayout) view.findViewById(R.id.rl_info_2);
        rl_info_3 = (RelativeLayout) view.findViewById(R.id.rl_info_3);
        rl_info_4 = (RelativeLayout) view.findViewById(R.id.rl_info_4);
        rl_info_5 = (RelativeLayout) view.findViewById(R.id.rl_info_5);
        rl_info_6 = (RelativeLayout) view.findViewById(R.id.rl_info_6);
        rl_info_7 = (RelativeLayout) view.findViewById(R.id.rl_info_7);
        rl_info_8 = view.findViewById(R.id.rl_info_8);
        rl_info_9 = view.findViewById(R.id.rl_info_9);
        rel_settlement = (LinearLayout) view.findViewById(R.id.rel_settlement);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);
        view7 = view.findViewById(R.id.view7);
        view8 = view.findViewById(R.id.view8);
        rl_info_4.setOnClickListener(this);
        rl_info_8.setOnClickListener(this);
    }
    private void getSubscripInfor(){
        String url = NetUrl.QUERY_SING_INFO;
        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(getActivity()));
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.e("response==="+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("success");
                    if (success) {//{"msg":"查询成功","numberUnits":"","sessionExpire":false,"success":true}
                        Gson gson = new Gson();
                        if(jsonObject.has("obj")){
                            JSONObject obj = jsonObject.optJSONObject("obj");
                            String msg = obj.optString("msg");
                            if ("商户信息不存在".equals(msg)) {
                                ToastUtil.shortShow(getActivity(), msg);
                                return;
                            } else {
                                JSONObject data = obj.optJSONObject("data");
                                SingInfoBean.ObjBean.DataBean dataInfo = gson.fromJson(data.toString(), new TypeToken<SingInfoBean.ObjBean.DataBean>() {
                                }.getType());
                                try {
                                    rl_info_4.setVisibility(View.VISIBLE);
                                    rl_info_8.setVisibility(View.VISIBLE);
                                    String minInfo1 = dataInfo.getMinfo1();
                                    String minInfo2 = dataInfo.getMinfo2();
                                    String settMethod = dataInfo.getSettmethod();
                                    cycle = dataInfo.getCycle();
                                    String rate = dataInfo.getSecondRate();
                                    String time = dataInfo.getPreSetTime();
                                    String quotaAmt = dataInfo.getQuotaAmt();
                                    String scanRate = dataInfo.getScanRate();
                                    if(TextUtils.isEmpty(scanRate)){
                                        view8.setVisibility(View.GONE);
                                        rl_info_9.setVisibility(View.GONE);
                                    }else {
                                        view8.setVisibility(View.VISIBLE);
                                        DecimalFormat df = new DecimalFormat("0.00%");
                                        if(YrmUtils.isNumber(scanRate)){
                                            tv_info_9.setText(df.format(Double.parseDouble(scanRate)));
                                        }else {
                                            tv_info_9.setText(scanRate);
                                        }
                                        rl_info_9.setVisibility(View.VISIBLE);
                                    }
                                    if (!TextUtils.isEmpty(minInfo1)) {
                                        rl_info_1.setVisibility(View.VISIBLE);
                                        info_1.setText(minInfo1 + "元");
                                    }
                                    if (!TextUtils.isEmpty(minInfo2)) {
                                        rl_info_2.setVisibility(View.VISIBLE);
                                        view2.setVisibility(View.VISIBLE);
                                        info_2.setText(minInfo2 + "元");
                                    }
                                    if (!TextUtils.isEmpty(settMethod)) {

                                        switch (settMethod) {
                                            case "0":
                                                rl_info_3.setVisibility(View.VISIBLE);
                                                view3.setVisibility(View.VISIBLE);
                                                info_3.setText("钱包提现");
                                                break;
                                            case "1":
                                                rl_info_3.setVisibility(View.VISIBLE);
                                                view3.setVisibility(View.VISIBLE);
                                                info_3.setText("秒到");
                                                break;
                                            case "3":
                                                rl_info_3.setVisibility(View.VISIBLE);
                                                view3.setVisibility(View.VISIBLE);
                                                info_3.setText("定时结算");
                                                break;
                                            case "4":
                                                rl_info_3.setVisibility(View.VISIBLE);
                                                view3.setVisibility(View.VISIBLE);
                                                info_3.setText("按金额结算");
                                                break;
                                            case "100000":
                                                rl_info_3.setVisibility(View.VISIBLE);
                                                view3.setVisibility(View.VISIBLE);
                                                info_3.setText("秒到");
                                                break;
                                            default:
                                                rl_info_3.setVisibility(View.GONE);
                                                break;
                                        }
                                    }
                                    if (!TextUtils.isEmpty(cycle)) {
//                                        if ("T+1".equals(cycle)) {
//                                            rl_info_3.setVisibility(View.GONE);
//                                        }
                                        rl_info_4.setVisibility(View.VISIBLE);
                                        info_4.setText(cycle);
                                    }
                                    if (!TextUtils.isEmpty(rate)) {
                                        rl_info_5.setVisibility(View.GONE);
                                        info_5.setText(rate + "元");
                                    }
                                    if (!TextUtils.isEmpty(time)) {
                                        rl_info_6.setVisibility(View.VISIBLE);
                                        info_6.setText(time + "天");
                                    }
                                    if (!TextUtils.isEmpty(quotaAmt)) {
                                        rl_info_7.setVisibility(View.VISIBLE);
                                        info_7.setText(quotaAmt + "元");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        LogUtil.e("map"+map.toString());
        VolleySingleton.getInstance(getActivity()).addMap(listener, errorListener, map, url);
    }
    /**
     * 查询该商户下面是否绑定了收款二维码
     */
    private void getQRCodeList() {
        String url = NetUrl.QUERY_COLLECTION_CODE_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(getActivity()));
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new Gson();
                CollectionCodeBean collectionCodeBean = mGson.fromJson(response,
                        new TypeToken<CollectionCodeBean>(){}.getType());
                boolean success = collectionCodeBean.isSuccess();
                List<CollectionCodeBean.ObjBean> data = collectionCodeBean.getObj();
                if (success){
                    if (data == null || data.size() == 0){ // 没有收款码
//                        ToastUtil.show("未绑定收款码，无法查看签约信息");
                    }
                }else { // 没有收款码
//                    ToastUtil.show("未绑定收款码，无法查看签约信息");
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        VolleySingleton.getInstance(getActivity()).addMap(listener,errorListener,map,url);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_info_8:
                //showChoice(rel_settlement);
                Intent intent = new Intent(getActivity(), LMFRedRainActivity.class);
                intent.putExtra("webViewUrl","4");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSubscripInfor();
    }
}
