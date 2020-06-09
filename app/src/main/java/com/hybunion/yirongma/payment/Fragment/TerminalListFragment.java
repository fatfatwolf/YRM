package com.hybunion.yirongma.payment.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.TerminalBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.activity.BindChaJianActivity;
import com.hybunion.yirongma.payment.activity.BindReceiptCodeActivity1;
import com.hybunion.yirongma.payment.activity.TerminalDetailsActivity;
import com.hybunion.yirongma.payment.adapter.TerminalListAdapter;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TerminalListFragment extends BaseFragment implements View.OnClickListener {
    private View root_view;
    private SmartRefreshLayout smartRefresh_layout;
    private ListView lv_code_list;
    private int page;
    int length = 0;
    String storeId = "",storeName = "";
    LinearLayout ll_no_code_layout;
    LinearLayout ll_code_details;
    LinearLayout ll_dimen;
    RelativeLayout rv_terminal;
    private List<TerminalBean.DataBean> list = new ArrayList<>();
    private TerminalListAdapter terminalListAdapter;
    private Button mBindButton;
    private int isFirst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.fragment_terminal_list, null);
            initView(root_view);
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        return root_view;
    }


    public void  initView(View root_view){
        Bundle bundle = getArguments();

        storeId = bundle.getString("storeId");
        storeName = bundle.getString("storeName");
        ll_no_code_layout = (LinearLayout) root_view.findViewById(R.id.ll_no_code_layout);
        rv_terminal = root_view.findViewById(R.id.rv_terminal);
        smartRefresh_layout = (SmartRefreshLayout) root_view.findViewById(R.id.smartRefresh_layout);
        lv_code_list = (ListView) root_view.findViewById(R.id.lv_code_list);
        ll_code_details = (LinearLayout) root_view.findViewById(R.id.ll_code_details);
        ll_dimen = (LinearLayout) root_view.findViewById(R.id.ll_dimen);
        mBindButton = root_view.findViewById(R.id.bind_button_terminal_list_fragment);
        mBindButton.setOnClickListener(this);
        terminalListAdapter = new TerminalListAdapter(getActivity());
        lv_code_list.setAdapter(terminalListAdapter);
        lv_code_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.get(position).type.equals("0")){
                    Intent intent = new Intent(getActivity(), TerminalDetailsActivity.class);
                    intent.putExtra("tidName",list.get(position).tidName);
                    intent.putExtra("tid",list.get(position).tid);
                    intent.putExtra("type",list.get(position).type);
                    intent.putExtra("createDate",list.get(position).createDate);
                    intent.putExtra("tidUrl",list.get(position).tidUrl);
                    intent.putExtra("storeName",storeName);
                    intent.putExtra("limitAmt",list.get(position).limitAmt);
                    intent.putExtra("remark",list.get(position).remark);
                    intent.putExtra("bean", list.get(position));
                    startActivity(intent);
                }

            }
        });
        handleList();
        queryTerminalList(isFirst);
    }


    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (length == 20) {
                    isFirst = 0;
                    page = page +20;
                    // 请求数据
                    queryTerminalList(isFirst);
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
                queryTerminalList(isFirst);
            }
        });
    }


    public void queryTerminalList(final int isFirst) {

        String url = NetUrl.QUERY_STORE_BIND_INFO;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("storeId", storeId);
            jsonRequest.put("query","");
            jsonRequest.put("limit","20");
            jsonRequest.put("type","");
            jsonRequest.put("start",String.valueOf(page));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(getActivity(), url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
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
                        ll_code_details.setVisibility(View.GONE);
                        ll_no_code_layout.setVisibility(View.VISIBLE);
                        rv_terminal.setBackgroundColor(getResources().getColor(R.color.white));
                    }else {
                        list.clear();
                        JSONArray body = response.optJSONArray("data");
                        list = mGson.fromJson(body.toString(), new
                                TypeToken<List<TerminalBean.DataBean>>() {
                                }.getType());
                        length = list.size();
                        if(list!=null && list.size()>0){
                            ll_no_code_layout.setVisibility(View.GONE);
                            rv_terminal.setBackgroundColor(getResources().getColor(R.color.bg_f8));
                            ll_code_details.setVisibility(View.VISIBLE);
                            ll_dimen.setVisibility(View.GONE);
                            terminalListAdapter.addAllList(list);
                        }else {
                            ll_dimen.setVisibility(View.VISIBLE);
                            ll_no_code_layout.setVisibility(View.VISIBLE);
                            ll_code_details.setVisibility(View.VISIBLE);
                            rv_terminal.setBackgroundColor(getResources().getColor(R.color.white));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_button_terminal_list_fragment:
                showDilaog(storeName, storeId);
                break;

        }
    }
    private MyBottomDialog mDialog;
    public void showDilaog(final String storeName, final String storeId) {
        final List<String> strList = new ArrayList<>();
        strList.add("绑定收款设备");
        strList.add("绑定收银插件");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(getActivity());
        }
        mDialog.showThisDialog(storeName, strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                switch (position) {
                    case 0:        // 绑定收款码
                        BindReceiptCodeActivity1.start(getActivity(), storeName, storeId);

                        break;
                    case 1:        // 绑定收银插件
                        BindChaJianActivity.start(getActivity(),storeName,storeId);
                        break;
                }
                if (mDialog != null)
                    mDialog.
                            dismiss();
            }
        });
    }



}
