package com.hybunion.yirongma.payment.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.YouHuiQuanDetailsDataBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 优惠券详情——券数据
 */

public class YouHuiQuanDetailsDataFragment extends BasicFragment {
    // 今日数据
    @Bind(R.id.tv_name_youhuiquan_data)
    TextView mTvName;
    @Bind(R.id.tv_lingqu_num_youhuiquan_data)
    TextView mTvLingQuNum;
    @Bind(R.id.tv_hexiao_num_youhuiquan_data)
    TextView mTvHeXiaoNum;
    @Bind(R.id.tv_hexiaolv_youhuiquan_data)
    TextView mTvHeXiaoLv;
    // 累计数据
    @Bind(R.id.tv_name1_youhuiquan_data)
    TextView mTvName1;
    @Bind(R.id.tv_lingqu1_num_youhuiquan_data)
    TextView mTvLingQuNum1;
    @Bind(R.id.tv_hexiao1_num_youhuiquan_data)
    TextView mTvHeXiaoNum1;
    @Bind(R.id.tv_hexiaolv1_youhuiquan_data)
    TextView mTvHeXiaoLv1;

    private String mCouponId; // 优惠券 id

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_youhuiquan_details_data;
    }

    @Override
    protected void load() {
        super.load();
        Bundle bundle = getArguments();
        mCouponId = bundle.getString("couponId");
        showProgressDialog("");
        getData(mCouponId);

    }

    // 请求券数据 将 优惠券id 传过来
    public void getData(String couponId){
        String url = NetUrl.YOUHUIQUAN_DETAILS_DATA;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("couponId", couponId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<YouHuiQuanDetailsDataBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(YouHuiQuanDetailsDataBean youHuiQuanDetailsDataBean) {
                mDataBean = youHuiQuanDetailsDataBean;
                    if (mDataBean!=null){
                        if (!"0".equals(mDataBean.getStatus())){
                            String msg = mDataBean.getMessage();
                            if (!TextUtils.isEmpty(msg))
                                ToastUtil.show(msg);
                            else ToastUtil.show("网络连接不佳");
                        }
                        setDatas();
                    }else{
                        ToastUtil.show("网络连接不佳");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return YouHuiQuanDetailsDataBean.class;
            }
        });


    }

    private YouHuiQuanDetailsDataBean mDataBean;


    private void setDatas(){
        YouHuiQuanDetailsDataBean.TodayDataBean todayDataBean = mDataBean.todayData;
        if (todayDataBean!=null){
            mTvName.setText(todayDataBean.couponName);
            mTvLingQuNum.setText(TextUtils.isEmpty(todayDataBean.receiveNumber)?"":(todayDataBean.receiveNumber+" 张"));
            mTvHeXiaoNum.setText(TextUtils.isEmpty(todayDataBean.cavNumber)?"":(todayDataBean.cavNumber+" 张"));
            mTvHeXiaoLv.setText(todayDataBean.cavRate);
        }

        YouHuiQuanDetailsDataBean.TotalDataBean totalDataBean = mDataBean.totalData;
        if (totalDataBean!=null){
            mTvName1.setText(totalDataBean.couponName);
            mTvLingQuNum1.setText(TextUtils.isEmpty(totalDataBean.receiveNumber)?"":(totalDataBean.receiveNumber+" 张"));
            mTvHeXiaoNum1.setText(TextUtils.isEmpty(totalDataBean.cavNumber)?"":(totalDataBean.cavNumber+" 张"));
            mTvHeXiaoLv1.setText(totalDataBean.cavRate);
        }
    }



}
