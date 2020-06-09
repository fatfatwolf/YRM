package com.hybunion.yirongma.valuecard.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseNewFragmentActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.model.MerCardInfoItemBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：储值卡管理
 * 编写人： myy
 * 创建时间：2017/3/6
 */
public class ValueCardManagerAdapter extends BaseAdapter {

    private Context mContext;

    private List<MerCardInfoItemBean> dataList;

    private String type;

    private int mMerCardCount = 0;

    private String message,msg;

    private String merchantID;//商户ID
    public ValueCardManagerAdapter(Context context, String type) {
        this.mContext = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null == dataList ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<MerCardInfoItemBean> list) {
        if (list == null) {
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.clear();
        dataList.addAll(list);
        merCardCount();

        this.notifyDataSetChanged();
    }

    public void addList(List<MerCardInfoItemBean> list) {
        if (list == null) {
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(list);
        merCardCount();

        this.notifyDataSetChanged();
    }

    private void merCardCount() {
        mMerCardCount = 0;
        for (MerCardInfoItemBean bean : dataList) {
            for (MerCardInfoItemBean.ObjBean ob : bean.getRuleArry()) {
                if (ob.isRec()) {
                    mMerCardCount++;
                }
            }
        }
    }

    public void clear() {
        this.dataList.clear();
    }

    public void removeList(int position) {
        if (null != dataList && dataList.size() > 0) {
            dataList.remove(position);
        }
        this.notifyDataSetChanged();
    }

    public List<MerCardInfoItemBean> getDataList() {
        return dataList;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final MerCardInfoItemBean bean = dataList.get(position);
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_valuecardmanager, null);
            holder.tv_cardname = (TextView) convertView.findViewById(R.id.tv_cardname);
            holder.tv_cardtype = (TextView) convertView.findViewById(R.id.tv_cardtype);
            holder.tv_cardyue = (TextView) convertView.findViewById(R.id.tv_cardyue);
            holder.tv_cardexpiredate = (TextView) convertView.findViewById(R.id.tv_cardexpiredate);
            holder.tv_ffline = (TextView) convertView.findViewById(R.id.tv_ffline);
            holder.ll_rule = (LinearLayout) convertView.findViewById(R.id.ll_rule);
            holder.rel_background = (RelativeLayout) convertView.findViewById(R.id.rel_background);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("已发布".equals(bean.getCardStatus())) {
            holder.rel_background.setBackground(mContext.getResources().getDrawable(R.drawable.img_published));
            holder.rel_background.setPadding(0, 0, 0, 0);
            holder.tv_ffline.setVisibility(View.VISIBLE);
        } else if ("审核中".equals(bean.getCardStatus())) {
            holder.rel_background.setBackground(mContext.getResources().getDrawable(R.drawable.img_unpublished));
            holder.rel_background.setPadding(0, 0, 0, 0);
            holder.tv_ffline.setVisibility(View.VISIBLE);
        } else if ("已下线".equals(bean.getCardStatus())) {
            holder.rel_background.setBackground(mContext.getResources().getDrawable(R.drawable.img_downline));
            holder.rel_background.setPadding(0, 0, 0, 0);
            holder.tv_ffline.setVisibility(View.GONE);
        } else if ("已过期".equals(bean.getCardStatus())) {
            holder.rel_background.setBackground(mContext.getResources().getDrawable(R.drawable.img_expired));
            holder.rel_background.setPadding(0, 0, 0, 0);
            holder.tv_ffline.setVisibility(View.GONE);
        }

        holder.tv_cardname.setText(bean.getCardName());
        holder.tv_cardtype.setText(bean.getCardType());
        holder.tv_cardyue.setText(bean.getRemaindNum() + "/" + bean.getTotalNumber() + "张");
        String date = bean.getExpireDate().replace("-", ".");
        holder.tv_cardexpiredate.setText("有效期：" + date);

        holder.ll_rule.removeAllViews();
        List<MerCardInfoItemBean.ObjBean> rules = bean.getRuleArry();

        for (MerCardInfoItemBean.ObjBean ob : rules) {
            TextView view = (TextView) View.inflate(mContext, R.layout.item_value_card_rule, null);
            showRuleInfo(view, ob, bean);
            holder.ll_rule.addView(view);
            view.setOnClickListener(onRuleClickListener);
        }

        holder.tv_ffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(position);
            }
        });

        return convertView;
    }

    private View.OnClickListener onRuleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MerCardInfoItemBean.ObjBean bean = (MerCardInfoItemBean.ObjBean) view.getTag(R.id.mer_card_count);
            MerCardInfoItemBean merCardInfoItemBean = (MerCardInfoItemBean) view.getTag(R.id.mer_card_state);
            if ("已下线".equals(merCardInfoItemBean.getCardStatus())) {
                ToastUtil.show("已下线规则不可以推荐");
            } else if ("已过期".equals(merCardInfoItemBean.getCardStatus())) {
                ToastUtil.show("已过期规则不可以推荐");
            } else if ("审核中".equals(merCardInfoItemBean.getCardStatus())){
                ToastUtil.show("未发布规则不可以推荐");
            }else {
                if (mMerCardCount >= MerCardInfoItemBean.ObjBean.MAX_SELECTED_COUNT && !bean.isRec()) {
                    // 已经超过了最大的选中数量
                    ToastUtil.show("最多推荐两个规则");
                    return;
                }
                // 如果已选中 那么数量-1 如果未选中数量+1
                if (bean.isRec()) {
                    bean.isRec = MerCardInfoItemBean.ObjBean.REC_UNSELECT;
                    mMerCardCount = mMerCardCount - 1;
                } else {
                    bean.isRec = MerCardInfoItemBean.ObjBean.REC_SELECTED;
                    mMerCardCount = mMerCardCount + 1;
                }
                showRuleInfo((TextView) view, bean, merCardInfoItemBean);
                // 此刷新会重新执行getItem方法 所以mer_card_count_selected的tag也会重新计算
                updateData(bean.isRec, bean.getRuleId());
                notifyDataSetChanged();
                // 告诉服务器选中发送变化了
            }
        }
    };

    private void showRuleInfo(TextView view, MerCardInfoItemBean.ObjBean bean, MerCardInfoItemBean merCardInfoItemBean) {
        view.setText(bean.getRuleInfo());
        int resId;
        if (bean.isRec()) {
            resId = R.drawable.img_selected;
        } else {
            resId = R.drawable.img_unselected;
        }
        Drawable drawable = mContext.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, 40, 40);// 对图片进行压缩
        view.setCompoundDrawables(drawable, null, null, null);
        view.setTag(R.id.mer_card_count, bean);  // 此tag用于点击事件内部的check判断
        view.setTag(R.id.mer_card_state, merCardInfoItemBean);
    }

    class ViewHolder {
        TextView tv_cardname;
        TextView tv_cardtype;
        TextView tv_cardyue;
        TextView tv_cardexpiredate;
        TextView tv_ffline;
        LinearLayout ll_rule;
        RelativeLayout rel_background;
    }

    private void showDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


        builder.setTitle(GetResourceUtil.getString(R.string.delete_card_title));
        builder.setMessage(GetResourceUtil.getString(R.string.delete_card_content));
        builder.setNegativeButton(GetResourceUtil.getString(R.string.delete_card_cancel), null);
        builder.setPositiveButton(GetResourceUtil.getString(R.string.delete_card_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getData(position);
            }
        });
        builder.create().show();
    }
    private void getData(final int position) {
        ((BaseNewFragmentActivity) mContext).showProgressDialog("");
        JSONObject jsonParams = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            jsonParams.put("cardApplyTempId", dataList.get(position).getCardApplyTempId());
            body.put("body", jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 回调
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseNewFragmentActivity) mContext).hideProgressDialog();
                String status = response.optString("status");
                String message = response.optString("message");
                ToastUtil.show(message);
                if ("0".equals(status)) { // 下线成功
                    dataList.get(position).setCardStatus(GetResourceUtil.getString(R.string.delete_card_disabled));
                    notifyDataSetChanged();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseNewFragmentActivity) mContext).hideProgressDialog();
                ToastUtil.show(GetResourceUtil.getString(R.string.poor_network));
            }
        };

        // 请求数据
        VolleySingleton.getInstance(mContext).addJsonObjectRequest(listener, errorListener, body, NetUrl.DISABLE_VALUE_CARD);
    }

    private void updateData(String ruleId, String isRecType) {

        ((BaseNewFragmentActivity) mContext).showProgressDialog("");
        JSONObject jsonParams = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            jsonParams.put("ruleId", isRecType);
            jsonParams.put("isRecType", ruleId);
            jsonParams.put("merId", GetApplicationInfoUtil.getMerchantId());
            body.put("body", jsonParams);
            LogUtil.d(body.toString() + "上传参数");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 回调
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseNewFragmentActivity) mContext).hideProgressDialog();
                com.hybunion.yirongma.payment.utils.LogUtil.d(response.toString()+"返回数据");
                String status = response.optString("status");
                msg = response.optString("msg");
                if ("0".equals(status)) { // 下线成功
                    ToastUtil.show(msg);
                    notifyDataSetChanged();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseNewFragmentActivity) mContext).hideProgressDialog();
                ToastUtil.show(GetResourceUtil.getString(R.string.poor_network));
            }
        };

        // 请求数据
        VolleySingleton.getInstance(mContext).addJsonObjectRequest(listener, errorListener, body, NetUrl.UPDATE_VALUE_CARD_RULE);
    }
}
