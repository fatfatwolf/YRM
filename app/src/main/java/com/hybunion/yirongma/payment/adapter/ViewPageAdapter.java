package com.hybunion.yirongma.payment.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {
    List<View> mListViews;

    public ViewPageAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        if(getCount()>1)
         container.removeView(mListViews.get(position));
    }


    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
