package com.hybunion.yirongma.payment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.LoginActivity;
import com.hybunion.yirongma.payment.utils.LogUtils;

import java.util.List;

/**
 * class desc: 引导页面适配器
 */



public class ViewPagerAdapter extends PagerAdapter {

    // 界面列表
    private List<View> views;
    private Activity activity;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private Integer[] resource = {R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three, R.drawable.guide_page};


    public ViewPagerAdapter(List<View> views, Activity activity) {
        this.views = views;
        this.activity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        LogUtils.d("destroyItem");
        container.removeView(views.get(position));
    }


    @Override
    public void finishUpdate(View arg0) {
    }

    // 获得当前界面数
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    // 初始化arg1位置的界面
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        ImageView view = (ImageView) views.get(position);
        Glide.with(activity).
                load(resource[position])
                .into(view);
        viewGroup.addView(view, 0);
        if (position == views.size() - 1) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 设置已经引导
                    //setGuided();
                    //goHome();
                }
            });
        }
        return view;
    }

    private void goHome() {
        views.clear();//释放资源
        // 跳转
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = activity.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
