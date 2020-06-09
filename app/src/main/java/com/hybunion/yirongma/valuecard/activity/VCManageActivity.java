package com.hybunion.yirongma.valuecard.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseNewFragmentActivity;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.payment.view.DialogF;
import com.hybunion.yirongma.valuecard.adapter.TabPagerAdapter;
import com.hybunion.yirongma.valuecard.fragment.ValueCardManageFragment;

/**
 * @author SunBingbing
 * @date 2017/5/11
 * @email freemars@yeah.net
 * @description 卡管理界面
 */

public class VCManageActivity extends BaseNewFragmentActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout ll_back;
    private TextView tv_head;
    private ImageView iv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuecard_manage);
        initViews();
        initDatas();
    }

    private void initViews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_head = (TextView) findViewById(R.id.tv_head);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.pager_container);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_head.setText("卡管理");

        iv_info = (ImageView) findViewById(R.id.right_iv);
        iv_info.setImageDrawable(GetResourceUtil.getDrawable(R.drawable.img_information));
        iv_info.setVisibility(View.GONE);
//        iv_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog();
//            }
//        });
    }

    private void dialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vip_dialog, null);
        final DialogF builder = new DialogF(this, 0, 0, view,  0 ,0);
        //部分机型的标题有一根蓝线问题
        Context context = builder.getContext();
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = builder.findViewById(divierId);
        if(divider!=null) {
            divider.setBackgroundColor(Color.alpha(0));
        }
        builder.setCancelable(true);
        TextView tv_title,tv_content;
        tv_title = (TextView) view.findViewById(R.id.help_info_title);
        tv_content= (TextView) view.findViewById(R.id.tv);
        tv_title.setText(GetResourceUtil.getString(R.string.card_manage_help));
        tv_content.setText(GetResourceUtil.getString(R.string.delete_card_info));
        Button but = (Button) view.findViewById(R.id.cancel);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();
    }
    private void initDatas() {
        // 全部
        ValueCardManageFragment fragment_all = new ValueCardManageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type","0");
        fragment_all.setArguments(bundle);

        //已发布
        ValueCardManageFragment fragment_published = new ValueCardManageFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type","1");
        fragment_published.setArguments(bundle2);

        // 未发布
        ValueCardManageFragment fragment_checking = new ValueCardManageFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("type","2");
        fragment_checking.setArguments(bundle3);

        // 已下线
        ValueCardManageFragment fragment_unPublish = new ValueCardManageFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("type","3");
        fragment_unPublish.setArguments(bundle4);

        // 已过期
        ValueCardManageFragment fragment_offLine = new ValueCardManageFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putString("type","4");
        fragment_offLine.setArguments(bundle5);


        Fragment[] fragments = {fragment_all,fragment_published,fragment_checking,fragment_unPublish,fragment_offLine};
        String[] titles = {"全部","已发布","未发布","已下线","已过期"};
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
