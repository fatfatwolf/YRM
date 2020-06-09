package com.hybunion.yirongma.valuecard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author SunBingbing
 * @date 2017/2/28
 * @email freemars@yeah.net
 * @description TabLayout 和 ViewPager 联动适配器
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] mFragments;
    private String[] mTitles;

    /**
     * 通过构造方法传入需要的 Fragment 和 Tab 标题数组
     * @param fm FragmentManager
     * @param fragments 需要填充的 Fragment 数组
     * @param titles 需要显示的 Tab 标题数组
     */
    public TabPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
