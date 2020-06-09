package com.hybunion.yirongma.payment.activity;

import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.HuiListBean2;
import com.hybunion.yirongma.payment.bean.ModifyRulerBossBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.adapter.GridViewModifyAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.ToastPopupWindow;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ModifyRulersShopActivity extends BasicActivity {

    @Bind(R.id.tv_customer)
    TextView tv_customer;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.rv_others_bollow)
    RelativeLayout rv_others_bollow;
    @Bind(R.id.tv_ruler_count)
    TextView tv_ruler_count;
    @Bind(R.id.ll_can_borrow)
    LinearLayout ll_can_borrow;
    @Bind(R.id.view)
    View view;
    List<HuiListBean2> dataList;
    private String merId;
    private String popFlag = "0";
    GridViewModifyAdapter  gridViewModifyAdapter;
    private String isShare,shareNum;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_rulers_shop;
    }

    @Override
    public void initView() {
        super.initView();
        dataList = new ArrayList<>();
        merId = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID);
        gridViewModifyAdapter = new GridViewModifyAdapter(this,dataList);
        gridView.setAdapter(gridViewModifyAdapter);
        rv_others_bollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastPopupWindow popupWindow = new ToastPopupWindow(ModifyRulersShopActivity.this,rv_others_bollow,R.layout.layout_toast_other_popupwindow);
            }
        });
    }

    @Override
    protected void load() {
        super.load();
        queryMerCardInfo(merId);
    }


    public void queryMerCardInfo(String merId){
        String url = NetUrl.QUERY_MER_CARD_INFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(ModifyRulersShopActivity.this, url, jsonObject, new MyOkCallback<ModifyRulerBossBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ModifyRulerBossBean modifyRulerBossBean) {
                if(null!=modifyRulerBossBean){
                    List<HuiListBean2> cardRules = modifyRulerBossBean.getData().get(0).cardRules;
                    if(!YrmUtils.isEmptyList(cardRules)){
                        dataList.clear();
                        dataList.addAll(cardRules);
                        gridViewModifyAdapter.updataList(dataList);
                    }
                    popFlag = modifyRulerBossBean.getData().get(0).popFlag;
                    isShare = modifyRulerBossBean.getData().get(0).isShare;
                    shareNum = modifyRulerBossBean.getData().get(0).shareNum;

                    if("0".equals(isShare)){
                        ll_can_borrow.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        if("0".equals(shareNum)){
                            tv_ruler_count.setText("不限次");
                        }else {
                            tv_ruler_count.setText("每张卡可借用10次");
                        }
                    }else {
                        ll_can_borrow.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    }



                    if("1".equals(popFlag)){
                        tv_customer.setText("所有顾客弹窗指引");
                    }else if("2".equals(popFlag)){
                        tv_customer.setText("到店消费两次以上顾客弹窗指引");
                    }else {
                        tv_customer.setText("返回有误，请重新选择");
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ModifyRulerBossBean.class;
            }
        });
    }


}
