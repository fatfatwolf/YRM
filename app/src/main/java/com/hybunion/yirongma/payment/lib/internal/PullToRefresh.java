package com.hybunion.yirongma.payment.lib.internal;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.lib.PullToRefreshBase;
import com.hybunion.yirongma.payment.lib.PullToRefreshListView;

import java.util.List;

/**
 * Created by win7 on 2016/7/21.
 */
public class PullToRefresh extends PullToRefreshListView {
    private ListView listview;
    private Handler mHandler = new Handler();
    private boolean isRefreshFoot = false;
    private boolean footloading = false;
    private LinearLayout loadingLayout;
    private int page = 1;
    private BaseAdapter adapter;
    private OnItemClick onItemClick;

    private OnScroll mOnScrollListener;
    public PullToRefresh(Context context) {
        super(context);
        init(context);
    }

    public PullToRefresh(Context context, int mode) {
        super(context, mode);
        init(context);
    }

    public PullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){
        listview = this.getRefreshableView();
        loadingLayout = (LinearLayout) LayoutInflater.from(
                context).inflate(
                R.layout.loading_more_progressbar_footer, null);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPullRefreshListener( final RefreshListener refreshListener) {
        this.setPullToRefreshEnabled(true);
        this.setOnDownPullRefreshListener(new PullToRefreshBase.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initBottomLoading();
                page = 1;
                if(refreshListener != null){
                    refreshListener.onRefresh(page);
                }
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(mOnScrollListener != null)
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && isRefreshFoot && footloading) {
                    page++;
                    initBottomLoading();
                    refreshListener.pullToRefresh(page);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(mOnScrollListener != null)
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                isRefreshFoot = firstVisibleItem + visibleItemCount == totalItemCount;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if(onItemClick != null){
                    onItemClick.onItemClick(arg0,arg1,position,arg3);
                }
            }
        });
    }

    private void initBottomLoading() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingLayout.findViewById(R.id.emptyProgress).setVisibility(
                        View.VISIBLE);
                TextView tv = (TextView) loadingLayout
                        .findViewById(R.id.emptyText);
                tv.setTextColor(android.graphics.Color.GRAY);
                tv.setText(R.string.loading);
            }
        }, 1000);
    }

    private void changeBottomLoading() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingLayout.findViewById(R.id.emptyProgress).setVisibility(
                        View.GONE);
                TextView tv = (TextView) loadingLayout
                        .findViewById(R.id.emptyText);
                tv.setText(R.string.all_data_loaded);
            }
        }, 1000);
    }

    private void pullUpLoading() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingLayout.findViewById(R.id.emptyProgress).setVisibility(
                        View.GONE);
                TextView tv = (TextView) loadingLayout
                        .findViewById(R.id.emptyText);
                tv.setText("上拉加载更多");
            }
        }, 1000);
    }


    public void setIsCanRefresh(List list, int count) {
        if (adapter != null && list != null) {
            if (list.size() < count) {
                addBottomLoading();
                Log.i("lyj", "position" + 1);
                footloading = true;
            } else {
                changeBottomLoading();
                footloading = false;
            }
        } else {
            if (list.size() < count) {
                addBottomLoading();
                Log.i("lyj", "position" + 2);
                footloading = true;
            } else {
                changeBottomLoading();
                footloading = false;
            }
        }
    }

    private void addBottomLoading() {
        if (!footloading) {
            if (listview.getFooterViewsCount() == 0) {
                listview.addFooterView(loadingLayout, null, false);
                footloading = true;
            }
        }
        pullUpLoading();
    }


    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
        listview.setAdapter(adapter);
    }

    public ListAdapter getMyAdapter() {
        return listview.getAdapter();
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);
    }

    public void setOnScrollListener(OnScroll listener){
        this.mOnScrollListener = listener;
    }

    public interface OnScroll{
         void onScrollStateChanged(AbsListView view, int scrollState);
         void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }


    public interface RefreshListener{
        void  onRefresh(int page);
        void pullToRefresh(int page);
    }


}
