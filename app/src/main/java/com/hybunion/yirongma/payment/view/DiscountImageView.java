package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.hybunion.yirongma.R;

public class DiscountImageView extends AppCompatImageView {

    private Paint mPaint = new Paint();
    private String mText = "9.6折";

    // Indicates whether the view should measure the position where the text be drawn.
    private boolean isInit = false;
    // Coordinates specifies the position of text in the screen.
    private float[] mTextPosition = new float[2];
    // The degree specifies how to rotate text, It's better to avoid changing this value.
    private static final float DEGREE = -45f;

    public DiscountImageView(Context context) {
        this(context, null, 0);
    }

    public DiscountImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscountImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DiscountImageView);

        float textSize = typedArray.getDimensionPixelSize(R.styleable.DiscountImageView_text_size, -1);
        if (textSize == -1) {
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    11, context.getResources().getDisplayMetrics());
        }

        typedArray.recycle();

        // Init paint.
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Measure where the text should be drawn.
        if (!isInit) measureTextPositions();
        // Rotate the canvas so that the rest of actions will be drawn with the degrees you set.
        canvas.rotate(DEGREE, this.getWidth() / 2f, this.getHeight());
        // Draw the special text.
        canvas.drawText(mText, mTextPosition[0], mTextPosition[1], mPaint);
    }

    public void setText(String text) {

        mText = text;
        isInit = false;
        invalidate();
    }

    private void measureTextPositions() {

        isInit = true;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextPosition[0] = (float) ((getMeasuredWidth()) / 2.0);
        mTextPosition[1] = (float) ((float) ((getMeasuredHeight()) / 2.0) + textHeight / 8.0);
    }

}