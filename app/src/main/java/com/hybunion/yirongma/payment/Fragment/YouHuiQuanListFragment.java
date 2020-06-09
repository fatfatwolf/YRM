package com.hybunion.yirongma.payment.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MakeNoteBean;
import com.hybunion.yirongma.payment.bean.YouHuiQuanListBean;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.activity.YouHuiQuanDetailsActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 优惠券列表 Fragment
 */

public class YouHuiQuanListFragment extends BasicFragment {
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.listView_youhuiquan_fragment)
    ListView mLv;
    @Bind(R.id.null_parent_youhuiquan_list_fragment)
    RelativeLayout mNullParent;

    private String mType;  // 根据不同类型，请求不同数据
    private CommonAdapter1 mAdapter;
    private boolean mIsRefresh = true;
    private int mPage;
    private int mDataLength; // 本次请求返回了多少条数据
    private List<YouHuiQuanListBean.DataBean> mDataList = new ArrayList<>();
    public static final int LIMIT = 20;  // 每页条数
    private boolean mIsCanLoad;  // 当前是否可以加载数据
    private boolean mIsLoaded; // 是否已经加载过数据。
    private String loginType;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_youhuiquan_list;
    }

    @Override
    protected void initView() {
        super.initView();

        Bundle bundle = getArguments();
        mType = (String) bundle.get("type");
        loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        Log.d("223311", "当前的 type 是：" + mType);
        mLv.setAdapter(mAdapter = new CommonAdapter1<YouHuiQuanListBean.DataBean>(getActivity(), mDataList, R.layout.item_youhuiquan_list) {
            @Override
            public void convert(ViewHolder holder, YouHuiQuanListBean.DataBean item, final int position) {
                TextView tvTitle = holder.findView(R.id.tv_title_item_youhuiquan);
                tvTitle.setText(item.couponName);
                ImageView img_status = holder.findView(R.id.img_status_item_youhuiquan);
                TextView tv_status = holder.findView(R.id.tv_quan_status);
                if (!TextUtils.isEmpty(item.couponStatus)) {
                    switch (item.couponStatus) {
                        case "0":  // 0未上线  1已上线  2已下线  3已过期
                            img_status.setImageResource(R.drawable.img_not_online);
                            tv_status.setVisibility(View.VISIBLE);
                            tv_status.setText("上线");
                            tv_status.setTextColor(getResources().getColor(R.color.blue_4E69E2));
                            tv_status.setBackgroundResource(R.drawable.radius18_blue_textview);
                            tv_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if("0".equals(loginType)){
                                        setCoupon(mDataList.get(position).couponId,"1");
                                    }else {
                                        ToastUtil.show("暂无权限");
                                    }
                                }
                            });
                            break;
                        case "1":
                            tv_status.setVisibility(View.VISIBLE);
                            img_status.setImageResource(R.drawable.img_online);
                            tv_status.setText("下线");
                            tv_status.setTextColor(getResources().getColor(R.color.btn_back_color1));
                            tv_status.setBackgroundResource(R.drawable.radius18_red_textview);
                            tv_status.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if("0".equals(loginType)){
                                        setCoupon(mDataList.get(position).couponId,"2");
                                    }else {
                                        ToastUtil.show("暂无权限");
                                    }
                                }
                            });
                            break;
                        case "2":
                            tv_status.setVisibility(View.GONE);
                            img_status.setImageResource(R.drawable.img_offline);
                            break;
                        case "3":
                            tv_status.setVisibility(View.GONE);
                            img_status.setImageResource(R.drawable.img_overtime);
                            break;
                    }
                }
                TextView tvHaveNum = holder.findView(R.id.tv_have_num_item_youhuiquan);  // 优惠券领取数量
                tvHaveNum.setText(item.receiveNumber);
                TextView tvSumNum = holder.findView(R.id.tv_sum_num_item_youhuiquan);   // 优惠券总数
                tvSumNum.setText(item.couponNumber);
                TextView tvTime = holder.findView(R.id.tv_time_item_youhuiquan);  // 优惠券有效期
                tvTime.setText(item.validStartDate + " ~ " + item.validEndtDate);
                TextView tvRules = holder.findView(R.id.tv_rules_item_youhuiquan);  // 优惠券 规则
                tvRules.setText(item.couponRules);


            }
        });

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YouHuiQuanDetailsActivity.start(getActivity(), mDataList.get(position).couponId,mDataList.get(position).couponStatus);
            }
        });

        handleList();
    }

    public void setCoupon(String couponId,String type){
        String url = NetUrl.UPDOWN_COUPON;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("couponId",couponId);
            jsonObject.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<MakeNoteBean>() {
          @Override
          public void onStart() {
              showProgressDialog("");
          }

          @Override
          public void onSuccess(MakeNoteBean result) {
              String status = result.getStatus();
              String message = result.getMessage();
              if (!TextUtils.isEmpty(status) && "0".equals(status)){
                  loadData(mType);
                  if(!TextUtils.isEmpty(message))
                      ToastUtil.show(message);
              }else {
                  if(!TextUtils.isEmpty(message))
                      ToastUtil.show(message);
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
              return MakeNoteBean.class;
          }
      });

    }



    @Override
    protected void load() {
        super.load();
        if ("".equals(mType)){
            setData(mType, mPage,true);
        }
        mIsCanLoad = true;
    }

    public void  loadData(String type){
        mPage = 0;
        setData(type, mPage,true);

        mIsCanLoad = true;
    }


    public void setData(String status, int page, final boolean isShow) {
        String url = NetUrl.YOUHUIQUAN_LIST;
        String merId = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.MERCHANT_ID);
        String loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.LOGINTYPE);
        String storeId = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.STORE_ID);
        JSONObject jsonObject = new JSONObject();
        try {
            if("0".equals(loginType)){//老板
                jsonObject.put("merId", merId);
                jsonObject.put("isMer","0");
            }else {
                jsonObject.put("merId", storeId);
                jsonObject.put("isMer","1");
            }
            jsonObject.put("status", status);
            jsonObject.put("page", page + "");
            jsonObject.put("limit", YouHuiQuanListFragment.LIMIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<YouHuiQuanListBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                if(isShow)
                    showProgressDialog("");
            }

            @Override
            public void onSuccess(YouHuiQuanListBean mBean) {
                    if (mBean != null) {

                        if (!"0".equals(mBean.getStatus())) {
                            String msg = mBean.getMessage();
                            if (!TextUtils.isEmpty(msg))
                                ToastUtil.show(msg);
                            else ToastUtil.show("网络连接不佳");
                            return;
                        }

                        List<YouHuiQuanListBean.DataBean> data = mBean.getData();
                        if (data == null) {
                            ToastUtil.show("网络连接不佳");
                            return;
                        }
                        if (mIsRefresh)
                            mDataList.clear();
                        mDataLength = data.size();
                        Log.d("223311", "Type 和 数据条数：" + mType + "---" + mDataList);
                        mDataList.addAll(data);
                        if (YrmUtils.isEmptyList(mDataList)) {
                            mNullParent.setVisibility(View.VISIBLE);
                            smartRefresh_layout.setVisibility(View.GONE);
                        } else {
                            mNullParent.setVisibility(View.GONE);
                            smartRefresh_layout.setVisibility(View.VISIBLE);
                            mAdapter.updateList(mDataList);
                        }

                    } else {
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
                return YouHuiQuanListBean.class;
            }
        });


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!"".equals(mType) && isVisibleToUser && mIsCanLoad && !mIsLoaded) {
            showProgressDialog("");
            setData(mType, mPage,true);

            mIsLoaded = true;

        }

    }

    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mDataLength == LIMIT) {
                    showProgressDialog("");
                    mPage++;
                    setData(mType, mPage,false);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 0;
                mIsRefresh = true;
                smartRefresh_layout.setEnableLoadMore(true);
                setData(mType, mPage,false);
            }
        });

    }





}
