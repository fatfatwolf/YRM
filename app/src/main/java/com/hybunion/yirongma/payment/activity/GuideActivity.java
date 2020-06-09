package com.hybunion.yirongma.payment.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.adapter.ViewPagerAdapter;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;


/**
 * 第一次进入滑动页
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private Button button;
    private LinearLayout mLinearLayout;// 灰点
    private ImageView point_white,img_experience;// 白点
    private int mPointWidth;// 小圆点之间的距离
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        // 初始化页面
        initViews();


    }

    private void initViews() {
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_point_group);
        point_white= (ImageView) findViewById(R.id.point_white);
        img_experience=(ImageView) findViewById(R.id.img_experience);
        button = (Button) findViewById(R.id.button);
        views = new ArrayList<View>();
        for (int i=0;i<4;i++){
            ImageView imageView=new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(imageView);
        }
        initDot();
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setPageTransformer(true,new DepthPageTransformer());
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    /**
     * 初始化点
     */
    private void initDot() {
        for(int i=0;i<4;i++){
            ImageView dotView=new ImageView(this);
            dotView.setBackgroundResource(R.drawable.dot_unfoucs);
            float density = getResources().getDisplayMetrics().density;
            int width=(int) (10*density);
            int height=(int) (10*density);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,
                    height);
            if (i > 0) {
                params.leftMargin = width;
            }
            dotView.setLayoutParams(params);
            mLinearLayout.addView(dotView);
        }

        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLinearLayout.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
                mPointWidth = mLinearLayout.getChildAt(1).getLeft()
                        - mLinearLayout.getChildAt(0).getLeft();
            }
        });

    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float leftWidth = mPointWidth * positionOffset + position * mPointWidth;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) point_white
                .getLayoutParams();// 获取白点的布局参数
        layoutParams.leftMargin = (int) leftWidth;// 设置白点的左边距
        point_white.setLayoutParams(layoutParams);
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        LogUtil.d(arg0+"arg0");
        if (arg0==3) {
            button.setVisibility(View.VISIBLE);
            if(Constant.AGENT_ID == 0)
                button.setText("");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setGuided();
                    goHome();
                }
            });
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {   //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {                             //两次按键小于2秒时，退出应用
                    HRTApplication.getInstance().finishAllActivities();
                    final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                    HRTApplication.getInstance().finishAllActivities();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    private void goHome() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferencesUtil.getInstance(this).putBooleanKey(SharedPConstant.IS_FIRST_IN,false);
    }

}
