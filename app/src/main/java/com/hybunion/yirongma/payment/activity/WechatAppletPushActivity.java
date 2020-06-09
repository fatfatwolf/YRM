package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AppletPushBean;
import com.hybunion.yirongma.payment.bean.AppletPushNumBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 小程序推送
 */

public class WechatAppletPushActivity extends BasicActivity {
    @Bind(R.id.tvStoreName_applet_push_activity)
    TextView mTvStoreName;
    @Bind(R.id.arrow_select_applet_push_activity)
    ImageView mImgArrow;
    @Bind(R.id.tv_notice_applet_push_activity)
    TextView mTvNotice;
    @Bind(R.id.tv_people_num_applet_push_activity)
    TextView mTvPeopleNum;
    @Bind(R.id.push_factor1_applet_push_activity)  // 推送条件选择
            TextView mTvPushFactor1;
    @Bind(R.id.push_factor2_applet_push_activity)
    TextView mTvPushFactor2;
    @Bind(R.id.null_push_content_applet_push_activity)
    RelativeLayout mNullParent;   // 没有推送内容展示的布局
    @Bind(R.id.bt_push_wechat_applet_push)
    TextView mBtPush;  // 发送按钮
    @Bind(R.id.coupon_parent_item_girdview_applet_push)
    RelativeLayout mCouponParent;
    @Bind(R.id.img_people_num)
    ImageView mImgNumArrow;
    @Bind(R.id.name_item_girdview_applet_push)
    TextView mTvCouponName;
    @Bind(R.id.price_item_girdview_applet_push)
    TextView mTvCouponPrice;
    @Bind(R.id.titleBar)
    TitleBar titleBar;
    @Bind(R.id.tv_sendType)
    TextView tv_sendType;

    private String mTotal = "0", mFans = "0", mMember = "0";  // 可推送人数
    private String mCouponId;  // 推送优惠券的 id
    private String mDateType = "2"; // 选择的推送人员类型   1--2到4天无消费      2---5到7天无消费   默认是 2
    private String mMemberType = "1";   // 1-全部   2-会员  3-粉丝，默认全部
    private String mLoginType;
    private String mStoreId, mStoreName;
    private String mSelectedStoreId, mSelectedStoreName;  // 门店选择中，选中的门店id 和 name
    private String mSelectedCouponId, mSelectedCouponName, mSelectedCouponCondition;  // 选中门店的优惠券信息
    private boolean mHavePeople;  // 是否有可以推送的人员
    private boolean mCanScreenStore; // 是否可以筛选门店。如果没有或者店长进入，不可以筛选门店。

    public static void start(Context from) {
        Intent intent = new Intent(from, WechatAppletPushActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wechat_applet_push;
    }

    @Override
    public void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        titleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                if (YrmUtils.isEmptyList(mStoreList)) {
                    ToastUtil.show("暂无加入商圈的门店");
                    return;
                }
                Intent intent = new Intent(WechatAppletPushActivity.this, MessageHistoryActivity.class);
                intent.putExtra("mStoreList", (Serializable) mStoreList);
                intent.putExtra("storeId", mStoreId);
                intent.putExtra("storeName", mStoreName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void load() {
        super.load();
        if ("0".equals(mLoginType)) {
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
            getCoupon("1", mStoreId);
        } else {
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
            getCoupon("2", mStoreId);
            mImgArrow.setVisibility(View.GONE);
        }

    }

    public void getCoupon(String type, String storeId){
        String url = NetUrl.HAS_COUPON;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type",type);
            jsonObject.put("storeId",storeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(WechatAppletPushActivity.this, url, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                if (bean == null) {
                    ToastUtil.show("网络错误");
                    return;
                }

                if (!"0".equals(bean.getStatus())) {
                    String msg = bean.getMessage();
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.show(msg);
                    else
                        ToastUtil.show("网络连接不佳");
                    return;
                }
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                if (YrmUtils.isEmptyList(dataList)) {   // 没有加入商圈的门店
                    mCanScreenStore = false;
                    mTvStoreName.setText("暂无加入商圈门店");
                    mTvNotice.setText("很遗憾，您当前还没有可推送优惠券信息");
                    mTvNotice.setVisibility(View.VISIBLE);
                    mImgArrow.setVisibility(View.GONE);
                    mCouponParent.setVisibility(View.GONE);
                    mNullParent.setVisibility(View.VISIBLE);
                    mHasCoupon = false;
                    mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                    mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
                } else {
                    if ("0".equals(mLoginType))
                        mImgArrow.setVisibility(View.VISIBLE);

                    mStoreList.addAll(dataList);
                    mCanScreenStore = true;
                    mCouponParent.setVisibility(View.VISIBLE);
                    mNullParent.setVisibility(View.GONE);
                    mStoreId = dataList.get(0).getStoreId();
                    mStoreName = dataList.get(0).getStoreName();
                    mTvStoreName.setText(dataList.get(0).getStoreName());   // 默认展示第一个门店
                    // 判断展示的门店是否有优惠券
                    if (!TextUtils.isEmpty(dataList.get(0).couponName) && !TextUtils.isEmpty(dataList.get(0).couponId)) {
                        mTvCouponName.setText(dataList.get(0).couponName);
                        mTvCouponPrice.setText(dataList.get(0).condition);
                        mCouponId = dataList.get(0).couponId;
                        mHasCoupon = true;
                    }

                    getPushPeopleNum(mStoreId, mDateType);   // 获取推送人数
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
                return StoreManageBean.class;
            }
        });

    }


    // 获取推送人数   dateType=1---2到4天     dateType=2---5到7天
    public void getPushPeopleNum(String storeId, String dateType){
        String url = NetUrl.PUSH_NUM;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId",storeId);
            jsonObject.put("dateType",dateType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
       OkUtils.getInstance().post(WechatAppletPushActivity.this, url, jsonObject, new MyOkCallback<AppletPushNumBean>() {
           @Override
           public void onStart() {
               showLoading();
           }

           @Override
           public void onSuccess(AppletPushNumBean bean1) {
               hideLoading();
               if (bean1 == null) {
                   ToastUtil.show("网络错误");
                   return;
               }

               if (!"0".equals(bean1.getStatus())) {
                   String msg = bean1.getMessage();
                   if (!TextUtils.isEmpty(msg))
                       ToastUtil.show(msg);
                   else
                       ToastUtil.show("网络连接不佳");
                   return;
               }

               AppletPushNumBean.DataBean data = bean1.getData();
               if (data == null) {
                   mImgNumArrow.setVisibility(View.GONE);
                   mHavePeople = false;
                   mTvNotice.setText("很遗憾，您当前还没有可推送粉丝/会员");
                   mTvNotice.setVisibility(View.VISIBLE);
                   mTotal = "0";
                   mFans = "0";
                   mMember = "0";
                   mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                   mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
               } else {
                   if (TextUtils.isEmpty(data.total) || "0".equals(data.total)){  // 没有可推送的人员
                       mTvNotice.setText("很遗憾，您当前还没有可推送粉丝/会员");
                       mTvNotice.setVisibility(View.VISIBLE);
                       mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                       mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
                       mHavePeople = false;
                   }else{
                       mHavePeople = true;
                       if (mHasCoupon) {
                           mTvNotice.setVisibility(View.GONE);
                           mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_red));
                           mBtPush.setTextColor(Color.parseColor("#ffffff"));
                       }
                   }
                   mTotal = TextUtils.isEmpty(data.total) ? "0" : data.total;
                   mFans = TextUtils.isEmpty(data.fans) ? "0" : data.fans;
                   mMember = TextUtils.isEmpty(data.member) ? "0" : data.member;
                   mTvPeopleNum.setText("总计 " + mTotal + " 人");

               }
           }

           @Override
           public void onError(Exception e) {

           }

           @Override
           public void onFinish() {
                hideLoading();
           }

           @Override
           public Class getClazz() {
               return AppletPushNumBean.class;
           }
       });

    }
    // 筛选门店
    @OnClick({R.id.selectStoreParent_applet_push_activity, R.id.push_people_parent_applet_push_activity})
    public void screenStore(LinearLayout ll) {
        switch (ll.getId()) {
            case R.id.selectStoreParent_applet_push_activity:    // 门店选择
                if (!"0".equals(mLoginType)) return;  // 不是老板不用选择门店。店长直接显示自己所属门店即可。
                if (!mCanScreenStore) return;  // 不可以筛选门店
                showStoreList();

                break;
            case R.id.push_people_parent_applet_push_activity:   // 推送人员选择
                if(!"0".equals(mTotal))
                     showDialog();

                break;
        }
    }

    private MyBottonPopWindow popWindow;
    private int storePosition = -1;
    private void showStoreList() {
        if (popWindow == null)
            popWindow = new MyBottonPopWindow(this, mStoreList);

        mImgArrow.setImageResource(R.drawable.arrow_up);
        popWindow.showPopupWindow(storePosition);
        popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
            @Override
            public void setStoreItemListener(int position) {
                storePosition = position;
                mSelectedStoreName = mStoreList.get(position).getStoreName();
                mSelectedStoreId = mStoreList.get(position).getStoreId();
                mSelectedCouponName = mStoreList.get(position).couponName;
                mSelectedCouponId = mStoreList.get(position).couponId;
                mSelectedCouponCondition = mStoreList.get(position).condition;
            }
        });

        popWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
            @Override
            public void setDissmissListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);
            }
        });

        popWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
            @Override
            public void setOnCloseListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);

            }
        });

        popWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
            @Override
            public void setButtonClickListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);
                if (TextUtils.isEmpty(mSelectedStoreId)) {
                    ToastUtil.show("请先选择门店");
                    return;
                }
                mStoreId = mSelectedStoreId;
                mStoreName = mSelectedStoreName;
                mTvStoreName.setText(mStoreName);
                mCouponId = mSelectedCouponId;
                // 判断当前选择的门店是否有优惠券
                if (TextUtils.isEmpty(mCouponId) || TextUtils.isEmpty(mSelectedCouponName)) {  // 如果优惠券id或者name为空，就算是没有优惠券
                    mTvNotice.setVisibility(View.VISIBLE);
                    mTvNotice.setText("很遗憾，您当前还没有可推送优惠券信息");
                    mCouponParent.setVisibility(View.GONE);
                    mNullParent.setVisibility(View.VISIBLE);
                    mHasCoupon = false;
                } else {
                    mHasCoupon = true;
                    mTvNotice.setVisibility(View.GONE);
                    mCouponParent.setVisibility(View.VISIBLE);
                    mNullParent.setVisibility(View.GONE);
                    mTvCouponName.setText(mSelectedCouponName);
                    mTvCouponPrice.setText(mSelectedCouponCondition);
                }
                getPushPeopleNum(mStoreId, mDateType);   // 获取推送人数


            }
        });

    }

    private MyBottomDialog mBottomDialog;

    private void showDialog() {
        List<String> strs = new ArrayList<>();
        strs.add("全部人员");
        strs.add("优质会员");
        strs.add("粉丝");
        if (mBottomDialog == null)
            mBottomDialog = new MyBottomDialog(this);
        mBottomDialog.showThisDialog("选择推送人员", strs, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                switch (position) {
                    case 0:
//                        ToastUtil.show("全部人员");
                        mTvPeopleNum.setText("总计 " + mTotal + " 人");
                        tv_sendType.setText("推送给全部人员");
                        mMemberType = "1";
                        break;
                    case 1:
//                        ToastUtil.show("会员");
                        mTvPeopleNum.setText("优质会员 " + mMember + " 人");
                        tv_sendType.setText("推送给优质会员");
                        mMemberType = "2";
                        break;
                    case 2:
//                        ToastUtil.show("粉丝");
                        mTvPeopleNum.setText("粉丝 " + mFans + " 人");
                        tv_sendType.setText("推送给粉丝");
                        mMemberType = "3";
                        break;

                }
                mBottomDialog.dismiss();
            }
        });
    }


    @OnClick({R.id.push_factor1_applet_push_activity, R.id.push_factor2_applet_push_activity})
    public void selectPushFactor(TextView tv) {
        switch (tv.getId()) {
            case R.id.push_factor1_applet_push_activity:   // 2天~4天无消费
                mTvPushFactor1.setTextColor(getResources().getColor(R.color.red_55940));
                mTvPushFactor2.setTextColor(getResources().getColor(R.color.gray_898989));
                mTvPushFactor1.setBackground(getResources().getDrawable(R.drawable.img_selected_bg_push));
                mTvPushFactor2.setBackground(getResources().getDrawable(R.drawable.img_unselected_bg_push));
                if(!"1".equals(mDateType)){
                    mDateType = "1";
                    getPushPeopleNum(mStoreId, mDateType);   // 获取推送人数
                }

                break;
            case R.id.push_factor2_applet_push_activity:   // 5天~7天无消费
                mTvPushFactor1.setTextColor(getResources().getColor(R.color.gray_898989));
                mTvPushFactor2.setTextColor(getResources().getColor(R.color.red_55940));
                mTvPushFactor1.setBackground(getResources().getDrawable(R.drawable.img_unselected_bg_push));
                mTvPushFactor2.setBackground(getResources().getDrawable(R.drawable.img_selected_bg_push));
                if(!"2".equals(mDateType)){
                    mDateType = "2";
                    getPushPeopleNum(mStoreId, mDateType);   // 获取推送人数
                }
                break;
        }

    }

    // 立即发送 按钮监听
    @OnClick(R.id.bt_push_wechat_applet_push)
    public void pushNow() {
        if (!mHasCoupon || !mHavePeople) return;
        if (!YrmUtils.isFastDoubleClick())
            pushIt(mCouponId, mMemberType, mDateType, mStoreId);
    }

    // 消息推送！
    public void pushIt(String couponId, String memberType, String dateType,String storeId){
        String url = NetUrl.PUSH_MSG;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("couponId", couponId);
            jsonObject.put("memberType",memberType);
            jsonObject.put("dateType",dateType);
            jsonObject.put("storeId",storeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

       OkUtils.getInstance().post(WechatAppletPushActivity.this, url, jsonObject, new MyOkCallback<AppletPushBean>() {
           @Override
           public void onStart() {
               showLoading();
           }

           @Override
           public void onSuccess(AppletPushBean o) {
               ToastUtil.show("推送成功");
               Intent intent = new Intent(WechatAppletPushActivity.this, SendMsgResultActivity.class);
               intent.putExtra("mStoreList", (Serializable) mStoreList);
               intent.putExtra("storeId", mStoreId);
               intent.putExtra("mStoreName", mStoreName);
               startActivity(intent);
               WechatAppletPushActivity.this.finish();
           }

           @Override
           public void onError(Exception e) {

           }

           @Override
           public void onFinish() {
                hideLoading();
           }

           @Override
           public Class getClazz() {
               return AppletPushBean.class;
           }
       });

    }

    private boolean mHasCoupon;  // 是否有可以推送的优惠券
    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList(); // 加入商圈的门店 List

    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);

        if (map == null) {
            ToastUtil.show("网络错误");
            return;
        }

        switch (type) {
            case STORE_AND_COUPON:   // 加入商圈的门店和门店的优惠券
                StoreManageBean bean = (StoreManageBean) map.get("bean");
                if (bean == null) {
                    ToastUtil.show("网络错误");
                    return;
                }

                if (!"0".equals(bean.getStatus())) {
                    String msg = bean.getMessage();
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.show(msg);
                    else
                        ToastUtil.show("网络连接不佳");
                    return;
                }
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                if (YrmUtils.isEmptyList(dataList)) {   // 没有加入商圈的门店
                    mCanScreenStore = false;
                    mTvStoreName.setText("暂无加入商圈门店");
                    mTvNotice.setText("很遗憾，您当前还没有可推送优惠券信息");
                    mTvNotice.setVisibility(View.VISIBLE);
                    mImgArrow.setVisibility(View.GONE);
                    mCouponParent.setVisibility(View.GONE);
                    mNullParent.setVisibility(View.VISIBLE);
                    mHasCoupon = false;
                    mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                    mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
                } else {
                    if("0".equals(mLoginType))
                        mImgArrow.setVisibility(View.VISIBLE);

                    mStoreList.addAll(dataList);
                    mCanScreenStore = true;
                    mCouponParent.setVisibility(View.VISIBLE);
                    mNullParent.setVisibility(View.GONE);
                    mStoreId = dataList.get(0).getStoreId();
                    mStoreName = dataList.get(0).getStoreName();
                    mTvStoreName.setText(dataList.get(0).getStoreName());   // 默认展示第一个门店
                    // 判断展示的门店是否有优惠券
                    if (!TextUtils.isEmpty(dataList.get(0).couponName) && !TextUtils.isEmpty(dataList.get(0).couponId)) {
                        mTvCouponName.setText(dataList.get(0).couponName);
                        mTvCouponPrice.setText(dataList.get(0).condition);
                        mCouponId = dataList.get(0).couponId;
                        mHasCoupon = true;
                    }

                    getPushPeopleNum(mStoreId, mDateType);   // 获取推送人数

                }
                break;

            case PUSH_NUM:   // 查询推送人数
                hideLoading();
                AppletPushNumBean bean1 = (AppletPushNumBean) map.get("bean");
                if (bean1 == null) {
                    ToastUtil.show("网络错误");
                    return;
                }

                if (!"0".equals(bean1.getStatus())) {
                    String msg = bean1.getMessage();
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.show(msg);
                    else
                        ToastUtil.show("网络连接不佳");
                    return;
                }

                AppletPushNumBean.DataBean data = bean1.getData();
                if (data == null) {
                    mImgNumArrow.setVisibility(View.GONE);
                    mHavePeople = false;
                    mTvNotice.setText("很遗憾，您当前还没有可推送粉丝/会员");
                    mTvNotice.setVisibility(View.VISIBLE);
                    mTotal = "0";
                    mFans = "0";
                    mMember = "0";
                    mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                    mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
                } else {
                    if (TextUtils.isEmpty(data.total) || "0".equals(data.total)){  // 没有可推送的人员
                        mTvNotice.setText("很遗憾，您当前还没有可推送粉丝/会员");
                        mTvNotice.setVisibility(View.VISIBLE);
                        mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_gray));
                        mBtPush.setTextColor(getResources().getColor(R.color.gray_e1e1e1));
                        mHavePeople = false;
                    }else{
                        mHavePeople = true;
                        if (mHasCoupon) {
                            mTvNotice.setVisibility(View.GONE);
                            mBtPush.setBackground(getResources().getDrawable(R.drawable.shape_applet_push_button_red));
                            mBtPush.setTextColor(Color.parseColor("#ffffff"));
                        }
                    }
                    mTotal = TextUtils.isEmpty(data.total) ? "0" : data.total;
                    mFans = TextUtils.isEmpty(data.fans) ? "0" : data.fans;
                    mMember = TextUtils.isEmpty(data.member) ? "0" : data.member;
                    mTvPeopleNum.setText("总计 " + mTotal + " 人");

                }
                break;

            case PUSH_MSG:   // 消息推送
                hideLoading();
                ToastUtil.show("推送成功");
//                SendMsgResultActivity
                Intent intent = new Intent(WechatAppletPushActivity.this, SendMsgResultActivity.class);
                intent.putExtra("mStoreList", (Serializable) mStoreList);
                intent.putExtra("storeId", mStoreId);
                intent.putExtra("mStoreName", mStoreName);
                startActivity(intent);
                WechatAppletPushActivity.this.finish();
                break;

        }

    }
}
