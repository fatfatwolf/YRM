package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.MoreNoticeBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/8.
 */

public class MoreNoticeAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    public List<MoreNoticeBean.DataBean> mNotice = new ArrayList<>();

    public MoreNoticeAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void clearData() {
        mNotice.clear();
    }
    @Override
    public int getCount() {
        return mNotice.size();
    }

    @Override
    public Object getItem(int i) {
        return mNotice.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder= null;
        if (null == view) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.more_notice_item, null);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_createdate= (TextView) view.findViewById(R.id.tv_createdate);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_dian = (TextView) view.findViewById(R.id.tv_dian);
            holder.img_id_notice = view.findViewById(R.id.img_id_notice);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final MoreNoticeBean.DataBean result = mNotice.get(i);
        LogUtil.d(result.getContent()+"内容");
        holder.tv_title.setText(result.getTitle());
        holder.tv_createdate.setText(result.getCreateDate());
        holder.tv_content.setText(Html.fromHtml(result.getContent()));
//        if(TextUtils.isEmpty(result.viewStatus)){
//            if ("0".equals(result.viewStatus)){
//                holder.tv_dian.setVisibility(View.GONE);
//                holder.img_id_notice.setBackgroundResource(R.drawable.img_noread_notice);
//            }else{
//                holder.tv_dian.setVisibility(View.VISIBLE);
//                holder.img_id_notice.setBackgroundResource(R.drawable.img_read_notice);
//            }
//        }
        return view;
    }
    class ViewHolder {
        TextView tv_title,tv_createdate,tv_content, tv_dian;
        ImageView img_id_notice;
    }
}
