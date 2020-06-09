package com.hybunion.yirongma.payment.view.FloatWindowView.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.ConvertUtils;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:悬浮窗的布局
 */
public class FloatLayout extends FrameLayout {
    private final WindowManager mWindowManager;
    private final ImageView mFloatView;
    //    private final DraggableFlagView mDraggableFlagView;  // 小红点，不要了。
    private long startTime;
    private float mTouchStartX;
    private float mTouchStartY;
    private boolean isclick;
    private RelativeLayout.LayoutParams mWmParams;
    private Context mContext;
    private long endTime;
    private OnFloatClickListener mListener;
    private int mTouchSlop; // 系统能够识别的最小滑动距离（因手机而异）
    private boolean mIsUp = true;
    private boolean mIsHalf = false; // 是否变成了半透明
    private int mWidth, mHeight; // 控件的宽高
    private boolean mIsClick = true; // 是否是点击
    private int mScreenWidth, mScreenHeight, mScreenHalfWidth;
    private static final int HANDLER_ALPHA = 1;   // 半透明
    private static final int HANDLER_BACK = 2;  // View 自己回到边上
    private static final int MOVE_DIS = 20;  // 回到边上，每次移动的距离
    private static final int MOVE_LEFT = 0;  // 左移
    private static final int MOVE_RIGHT = 1; // 右移

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null) return;
            switch (msg.what) {
                case HANDLER_ALPHA:   // 拖动 View
                    ObjectAnimator oa = ObjectAnimator.ofFloat(FloatLayout.this, "alpha", 1f, 0.6f);
                    oa.setDuration(1000);
                    oa.start();
                    mIsHalf = true;
                    break;

                case HANDLER_BACK:   // View 自己回到边上
                    switch (msg.arg1) {
                        case MOVE_LEFT:     // 左移
                            if (getLeft() <= MOVE_DIS) {
                                layout(0,getTop(),mWidth,getBottom());
                                mHandler.sendEmptyMessageDelayed(HANDLER_ALPHA, 5000);
                            } else {
                                int left = getLeft()-MOVE_DIS;
                                layout(left,getTop(),(left+mWidth),getBottom());
                                Message msg1 = Message.obtain();
                                msg1.arg1 = MOVE_LEFT;
                                msg1.what = HANDLER_BACK;
                                mHandler.sendMessageDelayed(msg1, 5);
                            }
//                            scrollBy(10);
                            break;
                        case MOVE_RIGHT:     // 右移
                            int dis = mScreenWidth-getRight();  // 控件右边 距离 右边的距离
                            if (dis>0){
                                if (dis<=MOVE_DIS){
                                    int left = mScreenWidth-mWidth;
                                    layout(left,getTop(),mScreenWidth,getBottom());
                                    mHandler.sendEmptyMessageDelayed(HANDLER_ALPHA, 5000);
                                }else{
                                    int left = getLeft() + MOVE_DIS;
                                    layout(left,getTop(),(left+mWidth),getBottom());
                                    Message msg1 = Message.obtain();
                                    msg1.arg1 = MOVE_RIGHT;
                                    msg1.what = HANDLER_BACK;
                                    mHandler.sendMessageDelayed(msg1, 4);
                                }
                            }
                            break;
                    }
                    break;
            }

        }
    };

    public FloatLayout(Context context) {
        this(context, null);
        mContext = context;
        mHandler.sendEmptyMessageDelayed(HANDLER_ALPHA, 5000);
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mHandler.sendEmptyMessageDelayed(HANDLER_ALPHA, 5000);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_littlemonk_layout, this);
        //浮动窗口按钮
        mFloatView = (ImageView) findViewById(R.id.float_id);
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
        // 获取屏幕宽高
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHalfWidth = mScreenWidth / 2;
        mScreenHeight = dm.heightPixels;

    }


    // 设置悬浮球的点击监听
    public void setOnFloatClickListener(OnFloatClickListener listener) {
        mListener = listener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsClick = true;
                // 如果在上一次消息没有发送之前，执行了 DOWN 操作，就要取消之前要发送的消息。
                mHandler.removeMessages(HANDLER_ALPHA);
                if (mIsHalf) {
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "alpha", 0.6f, 1f);
                    oa.setDuration(200);
                    oa.start();
                    mIsHalf = false;
                }

                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - mTouchStartX;
                float moveY = event.getY() - mTouchStartY;
                if (Math.abs(moveX) > 3 || Math.abs(moveY) > 3) {  // 点击、滑动 阈值为 3
                    mIsClick = false;
                    int left = (int) (getLeft() + moveX);
                    int right = (int) (getRight() + moveX);
                    int top = (int) (getTop() + moveY);
                    int bottom = (int) (getBottom() + moveY);
                    if (left < 0) {
                        left = 0;
                        right = left + mWidth;
                    } else if (right > mScreenWidth) {
                        right = mScreenWidth;
                        left = mScreenWidth - mWidth;
                    }
                    if (top < 0) {
                        top = 0;
                        bottom = top + mHeight;
//                    } else if (event.getRawY() > (mScreenHeight - mHeight)) {
                    } else if (bottom > (mScreenHeight - ConvertUtils.dip2px(80))) {
                        bottom = mScreenHeight - ConvertUtils.dip2px(80);
                        top = bottom - mHeight;
                    }

                    layout(left, top, right, bottom);
                }

                break;
            case MotionEvent.ACTION_UP:
                if (getLeft() > 0) {
                    Message msg = Message.obtain();
                    msg.what = HANDLER_BACK;
                    if (getLeft() < mScreenHalfWidth) {  // 在屏幕的左边
                        msg.arg1 = 0;
                    } else {  // 在屏幕的右边
                        msg.arg1 = 1;
                    }
                    mHandler.sendMessage(msg);
                }else{
                    mHandler.sendEmptyMessageDelayed(HANDLER_ALPHA, 5000);
                }
                if (mIsClick) {
                    if (mListener != null)
                        mListener.onFloatClick();
                }
                mIsClick = true;
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(RelativeLayout.LayoutParams params) {
        mWmParams = params;
    }

    public interface OnFloatClickListener {
        void onFloatClick();
    }


}
