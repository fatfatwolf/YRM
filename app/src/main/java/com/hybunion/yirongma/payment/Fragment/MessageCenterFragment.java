package com.hybunion.yirongma.payment.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.MainMassageBean;
import com.hybunion.yirongma.payment.bean.MoreNoticeBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.jpush.JpushStatsConfig;
import com.hybunion.yirongma.payment.activity.NewFeedBackActivity;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.activity.MainMassageActivity;
import com.hybunion.yirongma.payment.activity.MoreNoticeActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 本界面接口：
 */

public class MessageCenterFragment extends BaseFragment implements View.OnClickListener {

    private View root_view;
    LinearLayout ll_notice_message;
    LinearLayout ll_help;
    LinearLayout ll_kefu;
    LinearLayout ll_push_message;
    private TitleBar titlebar;
    String merchantName, merchantID;
    TextView mTvPushMsgCount; // 推送消息未读数
    private MessageReceiver mMessageReceiver;
    public static String ACTION_INTENT_RECEIVER = "com.hybunion.hyb.common.util.jpush";
    private List<MoreNoticeBean.DataBean> mNoticeDataList = new ArrayList<>();
    private TextView mTvNoticeContent;
    private TextView mTvPushMsg;
    private TextView mTvNoticeTime;
    private TextView mTvPushMsgTime;
    private int page = 0;
    private LMFMainActivity mMainActivity;
    private String mKefuTitle = "在线客服";
    private ConsultSource mSource;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.fragment_message_center, null);
            initView(root_view);
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        // 注册 获取推动通知的广播接受者
        mMessageReceiver = new MessageReceiver();
        mMainActivity = (LMFMainActivity) getActivity();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
        mSource = new ConsultSource("","","");
        mSource.faqGroupId=1220006;
        return root_view;

    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取列表数据，并将第一条数据的内容展示到界面上。
        // 获取未读推送消息数
        queryMessage();
        // 获取推送消息列表数据
        getPushMsgData();
        // 获取公告消息列表数据
        getNoticeInfo();
    }

    public void initView(View root_view) {
        merchantName = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantName");
        merchantID = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantID");
        mTvNoticeContent = (TextView) root_view.findViewById(R.id.noticeContent_message_center_fragment);
        mTvPushMsg = (TextView) root_view.findViewById(R.id.pushMsgContent_message_center_fragment);
        mTvPushMsgCount = (TextView) root_view.findViewById(R.id.pushMsgCount_message_center_fragment);
        mTvNoticeTime = (TextView) root_view.findViewById(R.id.noticeTime_message_center_fragment);
        mTvPushMsgTime = (TextView) root_view.findViewById(R.id.pushMsgTime_message_center_fragment);
        titlebar = (TitleBar) root_view.findViewById(R.id.titlebar);
        ll_notice_message = (LinearLayout) root_view.findViewById(R.id.ll_notice_message);
        ll_help = (LinearLayout) root_view.findViewById(R.id.ll_help);
        ll_kefu = root_view.findViewById(R.id.ll_kefu);
        ll_push_message = (LinearLayout) root_view.findViewById(R.id.ll_push_message);
        root_view.findViewById(R.id.tv_kefu_tele).setOnClickListener(this);
        root_view.findViewById(R.id.ll_yijian).setOnClickListener(this);
        titlebar.arrowNLineGone();
        ll_notice_message.setOnClickListener(this);
        ll_help.setOnClickListener(this);
        ll_kefu.setOnClickListener(this);
        ll_push_message.setOnClickListener(this);
    }

    // 推送通知的广播接收者
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            queryMessage();
            // 刷新列表数据，并将第一条数据的内容展示到界面上。

            // 推送消息数据
            getPushMsgData();
        }
    }

    // 获取推送消息的未读消息数
    private void queryMessage() {
        String url = NetUrl.UNREAFCOUNT;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.MERCHANTID));
            jsonObject.put("type", "7");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                String status, count;
                try {
                    JSONObject response = new JSONObject(str);
                    status = response.getString("status");
                    if ("0".equals(status)) {
                        count = response.getString("count");
                        LogUtil.d(count + "总数");
                        if (!TextUtils.isEmpty(count) && !"0".equals(count)) {
                            mTvPushMsgCount.setVisibility(View.VISIBLE);
                            mTvPushMsgCount.setText(count);
                            mMainActivity.setDot(true, count);  // 服务中心右上角展示数字
                        } else {
                            mTvPushMsgCount.setVisibility(View.INVISIBLE);
                            mMainActivity.setDot(false,"");// 服务中心右上角数字去掉
                        }
                    } else {
                        mTvPushMsgCount.setVisibility(View.INVISIBLE);
                        mMainActivity.setDot(false,"");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络状况不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    // 获取公告消息列表数据，将第一条的内容展示到界面上。
    private void getNoticeInfo() {
        String url = NetUrl.NOTICEINFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("platForm", "1");
            jsonObject.put("page", String.valueOf(page));
            jsonObject.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status;
                try {
                    status = response.getString("status");
                    Gson mGson = new Gson();
                    if ("0".equals(status)) {
                        JSONArray body = response.optJSONArray("data");
                        mNoticeDataList = mGson.fromJson(body.toString(), new
                                TypeToken<List<MoreNoticeBean.DataBean>>() {
                                }.getType());
                        if (!YrmUtils.isEmptyList(mNoticeDataList)) {
                            String title = mNoticeDataList.get(0).getTitle();
                            String time = mNoticeDataList.get(0).getCreateDate();
                            if(!TextUtils.isEmpty(title)){
                                mTvNoticeContent.setText(title);
                            }
                            if(!TextUtils.isEmpty(time))
                                mTvNoticeTime.setText(time);

                        } else {
                            mTvNoticeContent.setText("暂无新消息");
                        }
                    } else {
                        mTvNoticeContent.setText("暂无新消息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mTvNoticeContent.setText("暂无新消息");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络状况不佳");
                mTvNoticeTime.setText("暂无新消息");
                mTvNoticeTime.setText(YrmUtils.getNowDay("yyyy-MM-dd"));
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    private List<MainMassageBean.DataBean> mPushMsgDataList;

    // 获取推送列表数据
    public void getPushMsgData() {
        String url = NetUrl.GET_MAIN_MESSAGE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.MERCHANTID));
            jsonObject.put("page", "0");
            jsonObject.put("pageSize", "30");
            jsonObject.put("type", "7");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status;
                try {
                    status = response.getString("status");
                    Gson mGson = new Gson();
                    if ("0".equals(status)) {
                        JSONArray body = response.optJSONArray("data");
                        mPushMsgDataList = mGson.fromJson(body.toString(), new
                                TypeToken<List<MainMassageBean.DataBean>>() {
                                }.getType());
                        if (!YrmUtils.isEmptyList(mPushMsgDataList)) {
                            String time = mPushMsgDataList.get(0).getCreateDate();
                            String title = mPushMsgDataList.get(0).getTitle();
                            mTvPushMsg.setText(title);
                            String[] dates = time.split(" ");
                            if (dates != null || dates.length > 0) {
                                String time1 = dates[0];
                                mTvPushMsgTime.setText(time1);
                            }

                        } else {
                            mTvPushMsg.setText("暂无新消息");
                        }
                    } else {
                        mTvPushMsg.setText("暂无新消息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mTvPushMsg.setText("暂无新消息");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络状况不佳");
                mTvPushMsg.setText("暂无新消息");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_yijian:  // 意见反馈
                JpushStatsConfig.onCountEvent(getActivity(),"service_home_feedback",null);
                Intent feedback1 = new Intent(getActivity(), NewFeedBackActivity.class);
                feedback1.putExtra("flag","2");
                startActivity(feedback1);
                break;
            case R.id.tv_kefu_tele:   // 客服
                telServiceDialog();
                break;
            case R.id.ll_notice_message:  // 公告消息
                JpushStatsConfig.onCountEvent(getActivity(),"service_home_announce",null);
                Intent intent2 = new Intent(getActivity(), MoreNoticeActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_help:    // 帮助
                JpushStatsConfig.onCountEvent(getActivity(),"service_home_help",null);
                Intent helpIntent = new Intent(getActivity(), LMFRedRainActivity.class);
                helpIntent.putExtra("webViewUrl", "3");
                startActivity(helpIntent);
                break;
            case R.id.ll_kefu:  // 客服
                JpushStatsConfig.onCountEvent(getActivity(),"service_home_customer",null);
                Unicorn.openServiceActivity(getActivity(), mKefuTitle, mSource);

                break;
            case R.id.ll_push_message:   // 推送消息
                JpushStatsConfig.onCountEvent(getActivity(),"service_home_push",null);
                Intent msgIntent = new Intent(getActivity(), MainMassageActivity.class);
                startActivity(msgIntent);
                break;
        }
    }

    public void telServiceDialog() {
        @SuppressLint("RestrictedApi") LayoutInflater inflater = this.getLayoutInflater(getArguments());
        View view = inflater.inflate(R.layout.tel_service_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialogs);
        dialog.setContentView(view);
        TextView bt_sure = (TextView) view.findViewById(R.id.tv_sure);
        TextView bt_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(YrmUtils.isHavePermission(getActivity(),Manifest.permission.CALL_PHONE,YrmUtils.REQUEST_PERMISSION_PHONE)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400-010-5670"));
                    startActivity(intent);
                }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMessageReceiver != null) {
            getActivity().unregisterReceiver(mMessageReceiver);
        }

    }
}
