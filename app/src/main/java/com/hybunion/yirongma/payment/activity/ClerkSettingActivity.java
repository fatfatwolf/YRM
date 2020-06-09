package com.hybunion.yirongma.payment.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.payment.inteface.OnButtonClickListener;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.adapter.FavoriteStationListViewAdapter;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


/**
 * 员工管理（从门店列表进入，管理门店员工）
 */

public class ClerkSettingActivity extends BaseActivity implements OnButtonClickListener {
    int page = 0;
    private SmartRefreshLayout smartRefresh_layout;
    private ListView listClerk;
    private TextView tv_search;
    private EditText et_search;
    private LinearLayout ll_gone_clerk;
    private LinearLayout rl_billing_title;
    private List<QueryClerkListBean.ObjBean> beanList;
    private List<QueryClerkListBean.ObjBean> beanList1 = new ArrayList<>();
    private FavoriteStationListViewAdapter mClerSettingkAdapter;
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private String merchantID, storeName, storeId;
    private String workerName = "";
    private Gson mGson;
    private AlertDialog dialog;
    private boolean isRefresh;
    private int length = 0;
    private String type;
    private int isFirst;
    private TitleBar mTitleBar;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    hideProgressDialog();
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d(response + "接受数据");
                    QueryClerkListBean entry = new QueryClerkListBean();
                    entry.setStatus(response.optString("status"));
                    String data = response.optString("data");
                    entry.setMessage(response.optString("message"));
                    String storeName = response.optString("storeName");
                    SharedPreferencesUtil.getInstance(ClerkSettingActivity.this).putKey("storeName", storeName);
                    entry.setHasNextPage(Boolean.valueOf(response.optString("hasNextPage")));
                    String status = entry.getStatus();
                    String message = entry.getMessage();
                    LogUtils.d(status + message + "返回数据");
                    if ("0".equals(status)) {
                        if ("查询列表为空！".equals(message)) {
                            smartRefresh_layout.setVisibility(View.GONE);
                            listClerk.setVisibility(View.GONE);
                            ll_gone_clerk.setVisibility(View.VISIBLE);
                            rl_billing_title.setVisibility(View.GONE);
                        } else {
                            mGson = new Gson();
                            if (!TextUtils.isEmpty(data)) {
                                beanList = mGson.fromJson(data,
                                        new TypeToken<List<QueryClerkListBean.ObjBean>>() {
                                        }.getType());
                                if (beanList != null) {
                                    if (isRefresh)
                                        beanList1.clear();

                                    beanList1.addAll(beanList);
                                    length = beanList.size();
                                }
                                if (beanList1 != null && beanList1.size() > 0) {
                                    smartRefresh_layout.setVisibility(View.VISIBLE);
                                    listClerk.setVisibility(View.VISIBLE);
                                    ll_gone_clerk.setVisibility(View.GONE);
                                    rl_billing_title.setVisibility(View.VISIBLE);
                                    mClerSettingkAdapter.updateList(beanList1, isRefresh);
                                } else {
                                    smartRefresh_layout.setVisibility(View.GONE);
                                    ll_gone_clerk.setVisibility(View.VISIBLE);
                                    rl_billing_title.setVisibility(View.GONE);
                                }
                            }
                        }
                    } else {
                        ToastUtil.show(message);
                    }
                    break;
                case ERROR:
                    ToastUtil.show(GetResourceUtil.getString(R.string.network_err_info));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_setting);
        initViews();
        initDatas();
    }

    private void initDatas() {
        // 初始化 ListView
        handleList();
    }

    private void initViews() {
//        tvHead=(TextView) findViewById(R.id.tv_head);
//        tvHead.setText("员工管理");
        type = getIntent().getStringExtra("type");
        mTitleBar = findViewById(R.id.titleBar_clerk_setting_activity);
        listClerk = (ListView) findViewById(R.id.lv_clerk);
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        tv_search = (TextView) findViewById(R.id.tv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        ll_gone_clerk = (LinearLayout) findViewById(R.id.ll_gone_clerk);
        rl_billing_title = findViewById(R.id.rl_billing_title);
        merchantID = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        storeName = getIntent().getStringExtra("storeName");
        storeId = getIntent().getStringExtra("storeId");
        mClerSettingkAdapter = new FavoriteStationListViewAdapter(ClerkSettingActivity.this, beanList1, ClerkSettingActivity.this);
        listClerk.setAdapter(mClerSettingkAdapter);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFirst = 0;
                workerName = et_search.getText().toString().trim();
                QueryClerk(isFirst, page, true);
            }
        });
        // 加号 监听
        mTitleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyDialog();
            }
        });

    }

    private MyBottomDialog mDialog;

    private void showMyDialog() {
        List<String> strList = new ArrayList<>();
        strList.add("添加员工");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog(storeName, strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    Intent addIntent = new Intent(ClerkSettingActivity.this, AddClerkActivity.class);
                    addIntent.putExtra("type", type);
                    addIntent.putExtra("clerktitle", "2");
                    addIntent.putExtra("storeId", storeId);
                    addIntent.putExtra("storeName", storeName);
                    startActivity(addIntent);
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });

    }


    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                if (length == 20) {
                    isFirst = 0;
                    page++;
                    // 请求数据
                    QueryClerk(isFirst, page, true);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isFirst = 1;
                isRefresh = true;
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                QueryClerk(isFirst, page, true);
            }
        });
    }

    private void QueryClerk(final int isFirst, final int page, boolean isFromListView) {
        String url = NetUrl.QUERY_CLERK_LIST;
        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("storeId", storeId);
            jsonRequest.put("employName", workerName);
            jsonRequest.put("page", String.valueOf(page));
            jsonRequest.put("position", "");
            jsonRequest.put("rowsPerPage", String.valueOf(Constant.PAGE_SIZE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                if (isFirst == 0) {
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
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show(GetResourceUtil.getString(R.string.network_err_info));
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

    private void deleteClerk(final int position, String empId, final int pos) {
        String url = NetUrl.DELETE_CLERK_DETAIL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("employeeId", empId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                    String status = response.getString("status");
                    String msg = response.getString("msg");
                    if (status.equals("0")) {
                        beanList1.remove(pos);
                        mClerSettingkAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.shortShow(ClerkSettingActivity.this, msg);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mClerSettingkAdapter.notifyDataSetChanged();
        }
    };

    /**
     * delete
     *
     * @param pos
     * @param dataBean
     */
    @Override
    public void onDeleteClerk(final int pos, final QueryClerkListBean.ObjBean dataBean) {
        LogUtils.d("Delete");
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClerkSettingActivity.this);
        View view = getLayoutInflater().from(ClerkSettingActivity.this).inflate(R.layout.dialog_delete_clerk, null);
        TextView name = (TextView) view.findViewById(R.id.tv_detail);
        name.setText("确定删除员工" + "\"" + dataBean.getEmployName() + "\"" + "吗？");
        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(dataBean.getEmployName() + "删除");
                deleteClerk(pos, dataBean.getEmployId(), pos);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
    }

    /**
     * dialog
     *
     * @param pos
     * @param dataBean
     */
    @Override
    public void onDialog(int pos, final QueryClerkListBean.ObjBean dataBean) {


    }

    /**
     * edit
     *
     * @param pos
     * @param dataBean
     */
    @Override
    public void onModify(int pos, QueryClerkListBean.ObjBean dataBean) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirst = 0;
        isRefresh = true;
        QueryClerk(isFirst, page, true);
    }

}
