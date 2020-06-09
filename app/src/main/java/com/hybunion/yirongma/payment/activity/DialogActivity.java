package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 伪装成dialog
 * Created by lcy on 2015/9/30.
 */
public class DialogActivity extends BasicActivity {
    @Bind(R.id.tv_title_check_dialog)
    TextView mTvTitle;
    @Bind(R.id.tv_content_check_dialog)
    TextView mTvContent;

    private static OnButtonClickListener mListener;

    public static void start(Context from, String title, String content, OnButtonClickListener listener){
        mListener = listener;
        Intent intent = new Intent(from, DialogActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.check_dialog;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        mTvTitle.setText(title);
        mTvContent.setText(content);

    }

    @OnClick({R.id.tv_cancel_check_dialog, R.id.tv_ok_check_dialog})
    public void buttonClick(TextView tv){
        switch (tv.getId()){
            case R.id.tv_cancel_check_dialog:     // 取消
                if (mListener!=null)
                    mListener.cancel();
                finish();
                break;

            case R.id.tv_ok_check_dialog:          // 确定
                    if (mListener!=null)
                        mListener.ok();
                finish();
                break;

        }
    }

    /**
     *  设置标题
     * @param title
     */
    public void setTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     *  设置内容
     */
    public void setContent(String content){
        mTvContent.setText(content);
    }


    public void setOnButtonClickListener(OnButtonClickListener listener){
        mListener = listener;
    }

    public interface OnButtonClickListener{
        void cancel();
        void ok();
    }



}
