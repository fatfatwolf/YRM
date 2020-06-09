package com.hybunion.yirongma.valuecard.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.adapter.ValueCardManagerAdapter;
import com.hybunion.yirongma.valuecard.model.MerCardInfoItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/5/11
 * @email freemars@yeah.net
 * @description 储值卡管理界面
 */

public class ValueCardManageFragment extends BaseFragment {

    private MySwipe mySwipe;
    private ListView listView;
    private TextView tv_nodata;
    private int page = 0;
    private String merchantID;//商户ID
    private ValueCardManagerAdapter mAdapter;
    private Context mContext;
    private String type;
    private boolean hasData; // 是否有下一页
    private Gson mGson = new Gson();
    private List<MerCardInfoItemBean> listCards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vc_manage,container,false);
        initView(rootView);
        return rootView;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        }
    }
    private void initView(View rootView) {
        mContext = getActivity();
        mySwipe = (MySwipe) rootView.findViewById(R.id.myswipe);
        listView = (ListView) rootView.findViewById(R.id.listcard);
        tv_nodata = (TextView) rootView.findViewById(R.id.tv_nodata);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    // 获取数据
    private void initData() {
        // 从 Activity 中获取的数据
        // 初始化 ListView
        listCards = new ArrayList<>();
        mAdapter = new ValueCardManagerAdapter(mContext,type);
        mAdapter.addList(listCards);
        listView.setAdapter(mAdapter);
        handleList();
    }


    @Override
    public void onResume() {
        super.onResume();
        merchantID = GetApplicationInfoUtil.getMerchantId();
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        page = 0;
        // 加载数据
        getData(false);
    }



    /**
     * 加载刷新监听
     */
    private void handleList() {
        mySwipe.setChildView(listView);
        mySwipe.addFooterView();
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(mContext) {
            public void onLoad() {
                super.onLoad();
                if (hasData){
                    page ++;
                    getData(true);
                }else {
                    mySwipe.loadAllData();
                    mySwipe.setLoading(false);
                }
            }

            @Override
            public void onLoadEnd() {
                super.onLoadEnd();
                mySwipe.clearFootAnimation();
            }
        });
        mySwipe.startOnRefresh(new MySwipe.MyOnRefresh() {
            @Override
            public void onRefresh() {
                page = 0;
                getData(true);
            }
        });
    }

    /**
     * 卡管理列表请求数据
     */
    private void getData(boolean isFromList) {
        if (!isFromList){
            showProgressDialog("");
        }
        // Success
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                LogUtils.d("myy", "response" + response.toString());
                try {
                    String status = response.optString("status");
                    String message = response.optString("message");
                    JSONObject body = response.optJSONObject("body");
                    JSONArray data = null;
                    if (body != null) {
                        data = body.optJSONArray("data");
                    }
                    // query success
                    if ("0".equals(status)) {
                        mySwipe.setLoading(false);
                        mySwipe.setRefreshing(false);
                        hasData = body.optString("hasNext").equals("0");
                        // 有数据
                        if (null != data && data.length() > 0) {
                            mySwipe.setVisibility(View.VISIBLE);
                            tv_nodata.setVisibility(View.INVISIBLE);
                            listCards = mGson.fromJson(data.toString(),
                                    new TypeToken<ArrayList<MerCardInfoItemBean>>() {
                                    }.getType());
                            if (page == 0){
                                mAdapter.clear();
                            }
                            mAdapter.addList(listCards);
                            if (!hasData){
                                mySwipe.loadAllData();
                            }else {
                                mySwipe.resetText();
                            }
                        } else {
                            if (page == 0){
                                mySwipe.setVisibility(View.INVISIBLE);
                                tv_nodata.setVisibility(View.VISIBLE);
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        ToastUtil.show(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // Error
        Response.ErrorListener errorlistener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                hideProgressDialog();
                ToastUtil.show(GetResourceUtil.getString(R.string.network_err_info));
            }
        };

        // Params
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("merId", merchantID);
            dataParam.put("getType", type);
            dataParam.put("page", page + "");
            dataParam.put("pages", Constant.PAGE_SIZE + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // quest
        VolleySingleton.getInstance(mContext).addRequestWithHeader(listener,errorlistener
                ,dataParam, NetUrl.MERCARDINFO);
    }

}
