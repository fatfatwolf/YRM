package com.hybunion.yirongma.payment.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;


/**
 * 标题栏
 * Created by hxs on 2016/6/27.
 * 自定义titlebar
 */
public class TitleBar extends LinearLayout {
    private LinearLayout ll_titlebar_back;
    private TextView tv_titlebar_back_title, tv_back_name;
    private TextView tv_right;
    private RelativeLayout content;
    private OnTitleBackClickListener titleBackListener;
    private OnRightClickListener rightListener;
    private OnRightIconClickListener rightIconClickListener;
    private ImageView mRightIcon;//右侧图标
    private ImageView iv_question;

    private RelativeLayout ll_title_image;

    public TitleBar(Context context) {
        super(context);
        initView(context);
    }

    public TitleBar(final Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        String back_title = obtainStyledAttributes.getString(R.styleable.TitleBar_titlebar_back_title);
        int back_title_fontColor = obtainStyledAttributes.getColor(R.styleable.TitleBar_titlebar_back_title_fontcolor, Color.WHITE);
        int back_title_fontsize = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_titlebar_back_title_fontsize, 0);
        String tv_right_content = obtainStyledAttributes.getString(R.styleable.TitleBar_tv_right_content);
        int tv_right_content_fontsize = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_tv_right_content_fontsize, 0);
        int tv_right_content_fontColor = obtainStyledAttributes.getColor(R.styleable.TitleBar_tv_right_content_fontColor, Color.WHITE);

        //右侧的图片
        int rightSrc = obtainStyledAttributes.getResourceId(R.styleable.TitleBar_right_image_src, 0);
        int rightSrc_with = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_right_image_with, px2dp(context, 60));
        int rightSrc_height = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_right_image_height, px2dp(context, 60));

        //右侧第二个图片
        int rightSrc2 = obtainStyledAttributes.getResourceId(R.styleable.TitleBar_right_image_src2, 0);
        int rightSrc_with2 = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_right_image_with2, px2dp(context, 60));
        int rightSrc_height2 = (int) obtainStyledAttributes.getDimension(R.styleable.TitleBar_right_image_height2, px2dp(context, 60));
        // 返回按钮文字
        String back_name = obtainStyledAttributes.getString(R.styleable.TitleBar_title_back_name);

        if (back_title == null) {
//            ll_titlebar_back.setVisibility(GONE);
        } else {
            tv_titlebar_back_title.setText(back_title);
            tv_titlebar_back_title.setTextColor(back_title_fontColor);
            tv_back_name.setText(back_name);
            if (back_title_fontsize != 0) {
                tv_titlebar_back_title.setTextSize(px2dp(context, back_title_fontsize));
            }
        }

        if (rightSrc == 0 && rightSrc2 == 0) {//设置右侧的图片
            mRightIcon.setVisibility(GONE);
            ll_title_image.setVisibility(GONE);
            iv_question.setVisibility(GONE);
        } else if(rightSrc!=0 && rightSrc2 == 0){
            mRightIcon.setVisibility(VISIBLE);
            ll_title_image.setVisibility(VISIBLE);
            mRightIcon.setBackgroundResource(rightSrc);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRightIcon.getLayoutParams();
            params.width = rightSrc_with;
            params.height = rightSrc_height;
            mRightIcon.setLayoutParams(params);
        }else if ( rightSrc == 0 && rightSrc2 != 0) {//设置右侧的图片
            {
                iv_question.setVisibility(VISIBLE);
                ll_title_image.setVisibility(VISIBLE);
                iv_question.setBackgroundResource(rightSrc2);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_question.getLayoutParams();
                params.width = rightSrc_with2;
                params.height = rightSrc_height2;
                iv_question.setLayoutParams(params);
            }
        }else {//两个图片都显示存在
            mRightIcon.setVisibility(VISIBLE);
            mRightIcon.setBackgroundResource(rightSrc);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRightIcon.getLayoutParams();
            params.width = rightSrc_with;
            params.height = rightSrc_height;
            mRightIcon.setLayoutParams(params);



            iv_question.setVisibility(VISIBLE);
            ll_title_image.setVisibility(VISIBLE);
            iv_question.setBackgroundResource(rightSrc2);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_question.getLayoutParams();
            params1.width = rightSrc_with2;
            params.height = rightSrc_height2;
            iv_question.setLayoutParams(params);
        }

        if (TextUtils.isEmpty(tv_right_content)) {
            tv_right.setVisibility(GONE);
        } else {
            tv_right.setVisibility(VISIBLE);
            tv_right.setText(tv_right_content);
            tv_right.setTextColor(tv_right_content_fontColor);
            if (tv_right_content_fontsize != 0) {
                tv_right.setTextSize(px2dp(context, tv_right_content_fontsize));
            }
        }

        int backgroundcolor = obtainStyledAttributes.getColor(R.styleable.TitleBar_backgroundcolor, getResources().getColor(R.color.main_color2));
        content.setBackgroundColor(backgroundcolor);

        ll_titlebar_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleBackListener == null) {
                    ((Activity) context).finish();
                } else {
                    titleBackListener.titleBackClick();
                }
            }
        });

        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null) {
                    rightListener.rightClick();
                }
            }
        });
        // 右侧 图片 点击监听
//        mRightIcon.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rightIconClickListener != null)
//                    rightIconClickListener.rightIconClick();
//            }
//        });
        ll_title_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightIconClickListener != null)
                    rightIconClickListener.rightIconClick();
            }
        });
        iv_question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightIconClickListener != null)
                    rightIconClickListener.rightIconClick();
            }
        });
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

//    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initView(context);
//    }

    // 去掉返回箭头和分割线
    public void arrowNLineGone(){
        if (ll_titlebar_back!=null)
            ll_titlebar_back.setVisibility(GONE);

    }


    public void setTv_rightText(String text) {
        tv_right.setText(text);
    }

    public void setTv_titlebar_back_titleText(String text) {

        tv_titlebar_back_title.setText(text);
        tv_titlebar_back_title.setVisibility(VISIBLE);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.titlebar, this);
        ll_titlebar_back = (LinearLayout) this.findViewById(R.id.ll_titlebar_back);
        tv_titlebar_back_title = (TextView) this.findViewById(R.id.tv_titlebar_back_title);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        content = (RelativeLayout) this.findViewById(R.id.mpos_titlebar);
        mRightIcon = (ImageView) this.findViewById(R.id.title_image);
        tv_back_name = (TextView) this.findViewById(R.id.back_name);
        ll_title_image = this.findViewById(R.id.ll_title_image);
        iv_question = this.findViewById(R.id.iv_question);
    }


    public void setTitleBarBackground(int color) {
        content.setBackgroundColor(color);
    }

    public void setRightTexViewVisible(boolean visible) {
        if (visible) {
            tv_right.setVisibility(VISIBLE);
        } else {
            tv_right.setVisibility(GONE);
        }
    }




    public void setTitleBarBackVisible(boolean visible) {
        ll_titlebar_back.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setTitleBarOnClickListener(OnRightClickListener rightListener, OnTitleBackClickListener titleBackListener) {
        this.rightListener = rightListener;
        this.titleBackListener = titleBackListener;
    }
    // 返回键的监听
    public void setTitleBarBackClickListener(OnTitleBackClickListener titleBackListener){
        this.titleBackListener = titleBackListener;
    }

    // 右侧按钮监听
    public void setRightTextClickListener(OnRightClickListener rightListener) {
        this.rightListener = rightListener;
    }


    // 右侧 icon 点击监听（不好使...）
    public void setRightIconClickListener(OnRightIconClickListener rightIconClickListener) {
        this.rightIconClickListener = rightIconClickListener;
    }
    // 右侧 icon 点击监听
    public void setRightClickListener(OnClickListener listener){
        mRightIcon.setOnClickListener(listener);
    }

    // 右侧 icon 点击监听
    public void setRightClickListener2(OnClickListener listener){
        iv_question.setOnClickListener(listener);
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public interface OnRightClickListener {
        void rightClick();

    }

    public interface OnTitleBackClickListener {
        void titleBackClick();
    }

    public interface OnRightIconClickListener {
        void rightIconClick();
    }
}
