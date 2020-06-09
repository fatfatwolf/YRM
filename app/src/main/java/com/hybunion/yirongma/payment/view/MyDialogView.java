package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtils;

/**
 * 加载动画
 * Created by lcy on 2015/12/22.
 */
public class MyDialogView extends RelativeLayout {

    ///private ImageView imageView, imageView1, imageView2;
    private Animation animation, animation2, animation3;
    private Context context;
    private ImageView animationIV;
    private AnimationDrawable animationDrawable;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
//                    imageView.startAnimation(animation);
//                    imageView1.startAnimation(animation2);
//                    imageView2.startAnimation(animation3);
                    break;
                case 1:
                    break;
            }

        }
    };

    public MyDialogView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.activity_refresh_dialog, this, true);
        animationIV=(ImageView) findViewById(R.id.loadingImg);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
//        imageView = (ImageView) findViewById(R.id.iv1);
//        imageView1 = (ImageView) findViewById(R.id.iv2);
//        imageView2 = (ImageView) findViewById(R.id.iv3);
    }

    public MyDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.activity_refresh_dialog, this, true);
        animationIV=(ImageView) findViewById(R.id.loadingImg);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
//        imageView = (ImageView) findViewById(R.id.iv1);
//        imageView1 = (ImageView) findViewById(R.id.iv2);
//        imageView2 = (ImageView) findViewById(R.id.iv3);
        LogUtils.dlyj("MyDialogView2");

    }

    public MyDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.activity_refresh_dialog, this, true);
        animationIV=(ImageView) findViewById(R.id.loadingImg);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
//        imageView = (ImageView) findViewById(R.id.iv1);
//        imageView1 = (ImageView) findViewById(R.id.iv2);
//        imageView2 = (ImageView) findViewById(R.id.iv3);
        LogUtils.dlyj("MyDialogView");
    }



    /**
     * 开始动画
     */
    public void startAnim() {
        //  initAnimation(0, context);
//        imageView.startAnimation(animation);
//        imageView1.startAnimation(animation2);
//        imageView2.startAnimation(animation3);


        animationDrawable.start();
    }


    private void initAnimation(final int tag, Context context) {
        if (animation != null && animation != null && animation != null)
            return;
        animation = AnimationUtils.loadAnimation(context, R.anim.log_move);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.log_move1);
        animation3 = AnimationUtils.loadAnimation(context, R.anim.log_move2);
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //0为头部 1为脚部
                handler.sendEmptyMessageDelayed(tag, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
