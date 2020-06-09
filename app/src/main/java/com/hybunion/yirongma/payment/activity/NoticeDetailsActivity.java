package com.hybunion.yirongma.payment.activity;


import android.text.TextUtils;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.NoticeDetailsBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 公告详情
 * Created on 201７/2/２６.
 */

public class NoticeDetailsActivity extends BasicActivity {

    @Bind(R.id.tv_notice_details_time)
        TextView tv_notice_details_time;
    @Bind(R.id.tv_notice_details)
        TextView tv_notice_details;
    @Bind(R.id.tv_notice_details_title)
        TextView tv_notice_details_title;
    
    private List<NoticeDetailsBean.DataBean> mNoticeData;

    @Override
    protected int getContentView() {
        return R.layout.activity_notice_details;
    }


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void load() {
        super.load();
        String id = getIntent().getStringExtra("id");
        getNoticeDetails(id);
    }

    public void getNoticeDetails(String id) {
        String url = NetUrl.NOTICE_DETAILS;

        JSONObject object = new JSONObject();
        try {
            object.put("id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(NoticeDetailsActivity.this, url, object, new MyOkCallback<NoticeDetailsBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(NoticeDetailsBean bean) {
                mNoticeData = bean.getData();
                String title = mNoticeData.get(0).getTitle();
                String details = mNoticeData.get(0).getDescription();
                String time = mNoticeData.get(0).getCreatedate();
                tv_notice_details_time.setText("公告  "+time);
                if (TextUtils.isEmpty(title) || title.contains("null")){
                    tv_notice_details_title.setText("公告");
                }else {
                    tv_notice_details_title.setText(title);
                }
                tv_notice_details.setText(details);
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
                return NoticeDetailsBean.class;
            }
        });
    }


    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map);
        if(map != null){
            mNoticeData = (List< NoticeDetailsBean.DataBean>)map.get("noticedetails");
            String title = mNoticeData.get(0).getTitle();
            String details = mNoticeData.get(0).getDescription();
            String time = mNoticeData.get(0).getCreatedate();
            tv_notice_details_time.setText("公告  "+time);
            if (TextUtils.isEmpty(title) || title.contains("null")){
                tv_notice_details_title.setText("公告");
            }else {
                tv_notice_details_title.setText(title);
            }
            tv_notice_details.setText(details);
        }
    }

    @Override
    public void initView() {
        SharedUtil.getInstance(context()).putInt(Constants.Notice_NUM,getIntent().getIntExtra("totalCount",0));
    }

    @Override
    public void initData() {
    }
}