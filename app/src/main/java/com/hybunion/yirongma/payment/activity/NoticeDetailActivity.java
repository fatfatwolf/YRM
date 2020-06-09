package com.hybunion.yirongma.payment.activity;

import android.webkit.WebView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2017/12/12.
 */

public class NoticeDetailActivity extends BasicActivity {

    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_titlebar;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.webView_notice_details_activity)
    WebView mWebView;
    String title,time,noticeContent;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.notice_details_activity;
    }

    @Override
    public void initView() {
        super.initView();
        tv_titlebar.setText("公告详情");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        noticeContent=getIntent().getStringExtra("content");
        tv_title.setText(title);
        tv_time.setText("公告  "+time);

        mWebView.getSettings().setSupportZoom(false);
        mWebView.loadDataWithBaseURL(null,noticeContent,"test/html","UTF_8",null);
    }

    @OnClick(R.id.ll_titlebar_back)
    public void backUp(){
        finish();
    }
}
