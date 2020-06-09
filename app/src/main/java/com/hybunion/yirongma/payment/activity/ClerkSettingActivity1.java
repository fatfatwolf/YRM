package com.hybunion.yirongma.payment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
 *  员工管理/绑定员工 （从款台进入，管理款台员工）
 */

public class ClerkSettingActivity1 extends BaseActivity implements View.OnClickListener,OnButtonClickListener{
    int page=0;
    private SmartRefreshLayout smartRefresh_layout;
    private ListView listClerk;
    private TextView tv_search;
    private EditText et_search;
    private LinearLayout ll_gone_clerk;
    private LinearLayout rl_billing_title;
    private RelativeLayout ll_add_clerk;
    private List<QueryClerkListBean.ObjBean> beanList;
    private List<QueryClerkListBean.ObjBean> beanList1 = new ArrayList<>();
    private FavoriteStationListViewAdapter mClerSettingkAdapter;
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private String merchantID,storeName,storeId;
    private String mStoreId;//门店的ID,供划拨使用
    private String workerName = "";
    private Gson mGson;
    private AlertDialog dialog;
    private boolean isRefresh;
    private int length = 0;
    private String type;
    private TitleBar mTitleBar;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d(response+"接受数据");
                    QueryClerkListBean entry = new QueryClerkListBean();
                    entry.setStatus(response.optString("status"));
                    String data = response.optString("data");
                    entry.setMessage(response.optString("message"));
                    String storeName = response.optString("storeName");
                    SharedPreferencesUtil.getInstance(ClerkSettingActivity1.this).putKey("storeName",storeName);
                    entry.setHasNextPage(Boolean.valueOf(response.optString("hasNextPage")));
                    String status = entry.getStatus();
                    String message = entry.getMessage();
                    LogUtils.d(status+message+"返回数据");
                    if ("0".equals(status)){
                        if ("查询列表为空！".equals(message)){
                            smartRefresh_layout.setVisibility(View.GONE);
                            listClerk.setVisibility(View.GONE);
                            ll_gone_clerk.setVisibility(View.VISIBLE);
                            rl_billing_title.setVisibility(View.GONE);
                        }else {
                            mGson = new Gson();
                            if(!TextUtils.isEmpty(data)){
                                beanList = mGson.fromJson(data,
                                        new TypeToken<List<QueryClerkListBean.ObjBean>>() {
                                        }.getType());
                                if (beanList!=null){
                                    if (isRefresh)
                                        beanList1.clear();

                                    beanList1.addAll(beanList);
                                    length = beanList.size();
                                }
                                if (beanList1 !=null &&  beanList1.size() > 0){
                                    smartRefresh_layout.setVisibility(View.VISIBLE);
                                    listClerk.setVisibility(View.VISIBLE);
                                    ll_gone_clerk.setVisibility(View.GONE);
                                    rl_billing_title.setVisibility(View.VISIBLE);
                                    mClerSettingkAdapter.updateList(beanList1,isRefresh);
                                }else {
                                    ll_gone_clerk.setVisibility(View.VISIBLE);
                                    rl_billing_title.setVisibility(View.GONE);
                                }
                            }
                        }
                    }else {
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
        setContentView(R.layout.activity_clerk_setting1);
        initViews();
        initDatas();
    }
    private void initDatas() {
        // 初始化 ListView
        handleList();
    }
    private void initViews() {
        type = getIntent().getStringExtra("type");
        mTitleBar = findViewById(R.id.titleBar_clerk_setting_activity1);
        listClerk = (ListView) findViewById(R.id.lv_clerk);
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        tv_search = (TextView) findViewById(R.id.tv_search);
        et_search = (EditText) findViewById(R.id.et_search);
        ll_add_clerk = (RelativeLayout) findViewById(R.id.ll_add_clerk);
        ll_add_clerk.setOnClickListener(this);
        ll_gone_clerk = (LinearLayout) findViewById(R.id.ll_gone_clerk);
        rl_billing_title = findViewById(R.id.rl_billing_title);
        merchantID= SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        storeName = getIntent().getStringExtra("storeName");
        storeId = getIntent().getStringExtra("storeId");//款台id
        mStoreId = getIntent().getStringExtra("mStoreId");
        mClerSettingkAdapter  = new FavoriteStationListViewAdapter(ClerkSettingActivity1.this, beanList1,ClerkSettingActivity1.this);
        listClerk.setAdapter(mClerSettingkAdapter);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workerName = et_search.getText().toString().trim();
                QueryClerk(page, true);
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
        strList.add("员工划拨");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog(storeName, strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {   // 添加员工
                    Intent addIntent = new Intent(ClerkSettingActivity1.this,AddClerkActivity.class);
                    addIntent.putExtra("type",type);
                    addIntent.putExtra("clerktitle","2");
                    addIntent.putExtra("storeId",storeId);
                    addIntent.putExtra("storeName",storeName);
                    startActivity(addIntent);
                }else if (position == 1){   // 员工划拨
                    Intent intent = new Intent(ClerkSettingActivity1.this,ClerkSettingActivity2.class);
                    intent.putExtra("type",type);
                    intent.putExtra("clerktitle","2");
                    intent.putExtra("storeId",storeId);
                    intent.putExtra("mStoreId",mStoreId);
                    intent.putExtra("storeName",storeName);
                    startActivity(intent);
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
                isRefresh = true;
                if (length == 20) {
                    page++;
                    // 请求数据
                    QueryClerk(page, false);
                } else {
                    smartRefresh_layout.setEnableLoadMore(false);
                    smartRefresh_layout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                QueryClerk(page, false);
            }
        });

    }
    private void  QueryClerk(final int page, final boolean isShow) {
        String url = NetUrl.QUERY_CLERK_LIST;
        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("employName", workerName);
            jsonRequest.put("page", String.valueOf(page));
            jsonRequest.put("position","");
            jsonRequest.put("rowsPerPage", String.valueOf(Constant.PAGE_SIZE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity1.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                if(isShow)
                    showProgressDialog("");
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
    private void  deleteClerk(final int position, String empId ,final int pos) {

        String url = NetUrl.DELETE_CLERK_DETAIL;
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("employeeId", empId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity1.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                    String status = response.getString("status");
                    String msg = response.getString("msg");
                    if(status.equals("0")){
                        beanList1.remove(pos);
                        mClerSettingkAdapter.notifyDataSetChanged();
                    }
                    ToastUtil.shortShow(ClerkSettingActivity1.this,msg);
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
    Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mClerSettingkAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_clerk:
                Intent intent = new Intent(ClerkSettingActivity1.this,ClerkSettingActivity2.class);
                intent.putExtra("type",type);
                intent.putExtra("clerktitle","2");
                intent.putExtra("storeId",storeId);
                intent.putExtra("mStoreId",mStoreId);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
                break;
            case R.id.ll_add_clerk:
                Intent addIntent = new Intent(ClerkSettingActivity1.this,AddClerkActivity.class);
                addIntent.putExtra("type",type);
                addIntent.putExtra("clerktitle","2");
                addIntent.putExtra("storeId",storeId);
                addIntent.putExtra("storeName",storeName);
                startActivity(addIntent);
                break;
            case R.id.btn_head_right:
                Intent addIntent1 = new Intent(ClerkSettingActivity1.this,AddClerkActivity.class);
                addIntent1.putExtra("clerktitle","2");
                startActivity(addIntent1);
                break;
        }
    }
    /**
     * delete
     * @param pos
     * @param dataBean
     */
    @Override
    public void onDeleteClerk(final int pos, final QueryClerkListBean.ObjBean dataBean) {
        LogUtils.d("Delete");
        final AlertDialog.Builder builder = new AlertDialog.Builder(ClerkSettingActivity1.this);
        View view = getLayoutInflater().from(ClerkSettingActivity1.this).inflate(R.layout.dialog_delete_clerk, null);
        TextView name=(TextView) view.findViewById(R.id.tv_detail);
        name.setText("确定删除员工"+"\""+dataBean.getEmployName()+"\""+"吗？");
        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(dataBean.getEmployName()+"删除");
                deleteClerk(pos,dataBean.getEmployId(),pos);
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(view);
    }
    /**
     * dialog
     * @param pos
     * @param dataBean
     */
    @Override
    public void onDialog(int pos, final QueryClerkListBean.ObjBean dataBean) {

    }

    /**
     * edit
     * @param pos
     * @param dataBean
     */
    @Override
    public void onModify(int pos, QueryClerkListBean.ObjBean dataBean) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        QueryClerk(page, true);
    }

}
