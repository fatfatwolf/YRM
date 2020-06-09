package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.base.BaseActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

/**
 * Created by android on 2016/8/1.
 */
public class ItemNoticeActivity extends BaseActivity {

    @Bind(R.id.tv_notice_item_date)
    TextView mDate;

    @Bind(R.id.tv_notice_item_time)
    TextView mTime;

    @Bind(R.id.tv_notice_item_content)
    TextView mContent;

    @Override
    protected int getContentView() {
        return R.layout.item_notice_content;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String msgDesc = intent.getStringExtra("msgDesc");
        String time = intent.getStringExtra("msgSendTime");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            LogUtil.d("date = " + date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        mContent.setText(msgDesc);
        Spanned spanned = Html.fromHtml(msgDesc);//把带有样式的html文本转成字符串
        mContent.setText(spanned);
        mContent.setMovementMethod(LinkMovementMethod.getInstance());//textview 实现webview效果，超链接可打开

        mDate.setText(time.substring(5, 10).replace('-', '/'));
        mTime.setText(date.getHours() + ":" + date.getMinutes());
    }
}
