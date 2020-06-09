package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.hybunion.yirongma.payment.utils.LogUtil;

/**
 * Created by hxs on 2016/7/3.
 */
public class SignDrawingImageView extends ImageView {
    private Bitmap baseBitmap;
    private int viewHeight;
    private int viewWidth;
    private int drawLine = 0;//记录笔画长度
    private Paint paint;
    // 定义手指开始触摸的坐标
    private float startX;
    private float startY;

    private Canvas canvas = null;

    public SignDrawingImageView(Context context) {
        super(context);
        init(context);
    }

    public SignDrawingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SignDrawingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    private void init(Context context) {
        // 初始化一个画笔，笔触宽度为5，颜色为黑色
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        // this.setOnTouchListener(this);

        this.post(new Runnable() {
            @Override
            public void run() {
                // 第一次绘图初始化内存图片，指定背景为白色
                if (baseBitmap == null) {
                    baseBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                }
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            // 用户按下动作
            case MotionEvent.ACTION_DOWN:
                // 记录开始触摸的点的坐标
                startX = event.getX();
                startY = event.getY();
                break;
            // 用户手指在屏幕上移动的动作
            case MotionEvent.ACTION_MOVE:

                // 记录移动位置的点的坐标
                float stopX = event.getX();
                float stopY = event.getY();
                drawLine += (Math.abs(stopX - startX) + Math.abs(stopY - startY));
                // 根据两点坐标，绘制连线
                canvas.drawLine(startX, startY, stopX, stopY, paint);
                // 更新开始点的位置
                startX = event.getX();
                startY = event.getY();

                // 把图片展示到ImageView中
                this.setImageBitmap(baseBitmap);
                //this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //canvas.restore();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 清除画板
     */
    public void clearSign() {
        // 手动清除画板的绘图，重新创建一个画板
        if (baseBitmap != null && drawLine > 0) {

            //signRecycle();

            this.post(new Runnable() {
                @Override
                public void run() {
                    LogUtil.d("baseBitmap=======================================" + baseBitmap);
                    baseBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.RGB_565);
                    canvas = new Canvas(baseBitmap);
                    canvas.drawColor(Color.WHITE);
                    SignDrawingImageView.this.setImageBitmap(baseBitmap);
                }
            });

            drawLine = 0;
        }


    }

    public boolean isSignComplete() {
        return (drawLine > 300);
    }

    public void signRecycle() {
        // 如果图片还没有回收，强制回收
        if (!baseBitmap.isRecycled() && baseBitmap != null) {
            baseBitmap.recycle();
            baseBitmap = null;
        }
    }
}
