package com.hybunion.yirongma.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtils;

/**
 * Created by lcy on 2015/11/12.
 */
public class MySwipe extends SwipeRefreshLayout implements OnPullListener {
    private LinearLayout ll_head;
    private int headHight;
    private Animation animation, animation2, animation3;
    private Context context;
    private View footView;
    private View footerLayout;
    //判断是否允许下拉刷新
    public boolean isCanLoad = true;
    private ImageView animationIV;
    private ImageView animationIV_foot;
    private AnimationDrawable animationDrawable;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
            }

        }
    };
    private View view;

    public MySwipe(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public void setCanLoad(boolean isCanLoad) {
        this.isCanLoad = isCanLoad;
    }

    public MySwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }


    public void hideHead() {
        getHeadView().setVisibility(View.GONE);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_refresh_head, null);
        animationIV = (ImageView) view.findViewById(R.id.loadingImg);
        animationDrawable = (AnimationDrawable) animationIV.getDrawable();
        animationDrawable.start();
        setHeadView(view);
        footerLayout = LayoutInflater.from(context).inflate(R.layout.activity_refresh_footer, null);
        setOnPullListener(this);
    }

    public TextView textMore;
    private LinearLayout ll_foot;

    public View getRefreshHead() {
        return view;
    }

    public void addFooterView() {
        getChildView().addFooterView(footerLayout);
        initFoot();
    }

    public void initFoot() {
        ll_foot = (LinearLayout) footerLayout.findViewById(R.id.ll_foot);
        textMore = (TextView) footerLayout.findViewById(R.id.text_more);
        animationIV_foot = (ImageView) footerLayout.findViewById(R.id.loadingImg_foot);
        animationDrawable = (AnimationDrawable) animationIV_foot.getDrawable();
        animationDrawable.start();
    }

    public void loadAllData() {
        isCanLoad = false;
        if (textMore != null) {
            textMore.setTextColor(Color.GRAY);
            textMore.setText("以上是全部数据");
        }

        setLoading(false);
    }

    public void resetText() {
        isCanLoad = true;
        if (textMore != null)
            textMore.setText("上拉加载更多...");
    }


    private MyOnRefresh myOnRefresh;

    public void startOnRefresh(MyOnRefresh myOnRefresh) {
        this.myOnRefresh = myOnRefresh;
    }

    @Override
    public void onPulling(View headview, int currentTop) {
        if (null == ll_head) {
            headHight = headview.getMeasuredHeight();
            ll_head = (LinearLayout) headview.findViewById(R.id.ll_head);
        }
        ll_head.setVisibility(View.VISIBLE);
        ll_head.setScaleX(Float.valueOf(currentTop) / Float.valueOf(headHight));
        ll_head.setScaleY(Float.valueOf(currentTop) / Float.valueOf(headHight));
        if (isLoading()) {
            setLoading(false);
        }
    }

    @Override
    public void onCanRefreshing(View headview) {

    }

    @Override
    public void onRefreshing(View headview) {
        Log.d("find", "onRefreshing");
        myOnRefresh.onRefresh();
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void clearHeadAnimation() {
        handler.removeMessages(0);
    }

    public void clearFootAnimation() {
    }

    public interface MyOnRefresh {
        void onRefresh();
    }

    public class MyLoad implements OnLoadListener {
        private Context context;

        public MyLoad(Context context) {
            this.context = context;
        }

        @Override
        public void onLoad() {
            if (ll_foot != null)
                ll_foot.setVisibility(View.VISIBLE);
            if (textMore != null)
                textMore.setVisibility(View.GONE);
        }

        @Override
        public void onLoadEnd() {
            LogUtils.d("MyLoadend");
            if (ll_foot != null)
                ll_foot.setVisibility(View.GONE);
            if (textMore != null)
                textMore.setVisibility(View.GONE);
            clearFootAnimation();
        }
    }


    private float mDownPosX, mDownPosY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
