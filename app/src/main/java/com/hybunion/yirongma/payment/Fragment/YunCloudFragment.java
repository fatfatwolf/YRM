package com.hybunion.yirongma.payment.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.YunCloudBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.activity.BindCloudLaBaActivity;
import com.hybunion.yirongma.payment.adapter.YunCloudAdapter;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class YunCloudFragment extends BaseFragment implements View.OnClickListener {

    private View root_view;
    LinearLayout ll_dimen,ll_plug_manage,ll_no_code_layout;
    RelativeLayout ll_plug;
    String storeId = "",storeName,type;
    SmartRefreshLayout smartRefresh_layout;
    ListView lv_plug_list;
    private int page;
    int length = 0;
    private YunCloudAdapter yunCloudAdapter;
    private List<YunCloudBean.DataBean> list = new ArrayList<>();
    private Button mBindButton;
    private int isFirst;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root_view == null){
            root_view = inflater.inflate(R.layout.fragment_yun_cloud, null);
            initView(root_view);
        }else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        return root_view;
    }

    public void initView(View root_view){
        Bundle bundle = getArguments();
        storeId = bundle.getString("storeId");
        storeName = bundle.getString("storeName");
        type = bundle.getString("type");
        String loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        yunCloudAdapter = new YunCloudAdapter(getActivity());
        ll_dimen = (LinearLayout) root_view.findViewById(R.id.ll_dimen);
        ll_plug_manage = (LinearLayout) root_view.findViewById(R.id.ll_plug_manage);
        ll_plug = (RelativeLayout) root_view.findViewById(R.id.ll_plug);
        ll_no_code_layout = (LinearLayout) root_view.findViewById(R.id.ll_no_code_layout);
        smartRefresh_layout = (SmartRefreshLayout) root_view.findViewById(R.id.smartRefresh_layout);
        lv_plug_list = (ListView) root_view.findViewById(R.id.lv_plug_list);
        mBindButton = root_view.findViewById(R.id.bind_button_yun_cloud_fragment);
        mBindButton.setOnClickListener(this);
        lv_plug_list.setAdapter(yunCloudAdapter);
        handleList();
    }


    private void handleList(){
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (length == 20) {
                    isFirst = 0;
                    page = page +20;
                    // 请求数据
                    queryCollectCode(isFirst);
                } else {
                    smartRefresh_layout.setEnableLoadMore(false);
                    smartRefresh_layout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isFirst = 1;
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                queryCollectCode(isFirst);
            }
        });
    }



    public void queryCollectCode(final int isFirst) {
        String url = NetUrl.QUERY_YUN_BY_STOREID;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("storeId", storeId);
            jsonRequest.put("limit","20");
            jsonRequest.put("start",String.valueOf(page));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                if(isFirst == 0){
                    showProgressDialog("");
                }
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status = response.optString("status");
                if(status.equals("0")){
                    Gson mGson = new Gson();
                    String count = response.optString("count");

                    if("0".equals(count)){
                        ll_plug_manage.setVisibility(View.GONE);
                        ll_no_code_layout.setVisibility(View.VISIBLE);
                        ll_plug.setBackgroundColor(getResources().getColor(R.color.white));
                    }else {
                        list.clear();
                        JSONArray body = response.optJSONArray("data");
                        list = mGson.fromJson(body.toString(), new
                                TypeToken<List<YunCloudBean.DataBean>>() {
                                }.getType());
                        length = list.size();
                        if(list!=null && list.size()>0){
                            ll_no_code_layout.setVisibility(View.GONE);
                            ll_plug.setBackgroundColor(getResources().getColor(R.color.bg_f8));
                            ll_plug_manage.setVisibility(View.VISIBLE);
                            ll_dimen.setVisibility(View.GONE);
                            yunCloudAdapter.addAllList(list);
                        }else {
                            ll_dimen.setVisibility(View.VISIBLE);
                            ll_no_code_layout.setVisibility(View.VISIBLE);
                            ll_plug_manage.setVisibility(View.VISIBLE);
                            ll_plug.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }else {
                    String message = response.optString("message");
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
                return String.class;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        isFirst = 0;
        queryCollectCode(isFirst);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_button_yun_cloud_fragment:
                BindCloudLaBaActivity.start(getActivity(),storeName,storeId,type);
                break;
        }
    }





}
