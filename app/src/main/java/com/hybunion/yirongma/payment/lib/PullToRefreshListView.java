package com.hybunion.yirongma.payment.lib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;

import com.hybunion.yirongma.payment.lib.internal.EmptyViewMethodAccessor;


public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {

	class InternalListView extends ListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}
		
		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(false);
	}
	
	public PullToRefreshListView(Context context, int mode) {
		super(context, mode);
		this.setDisableScrollingWhileRefreshing(false);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setDisableScrollingWhileRefreshing(false);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	@Override
	protected final ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);
		/*<style name="all_listView">
        <item name="android:id">@android:id/list</item>
        <item name="android:layout_width">fill_parent</item>
        <item name="android:layout_height">fill_parent</item>
        <item name="android:drawSelectorOnTop">false</item>
        <item name="android:fadingEdge">none</item>
        <item name="android:scrollbars">none</item>
        <item name="android:fastScrollEnabled">false</item>
        <item name="android:listSelector">@drawable/switch_common_list_item_bg</item>
        <item name="android:cacheColorHint">@android:color/transparent</item>
        <!-- 防止黑背景 -->
        <item name="android:groupIndicator">@null</item>
        <item name="android:dividerHeight">0.0px</item>
        <item name="android:divider">@drawable/line</item>
        </style>*/
		//lv.setDivider(context.getResources().getDrawable(R.drawable.common_list_item_xuxian));
		lv.setId(android.R.id.list);
		lv.setCacheColorHint(Color.TRANSPARENT);
		return lv;
	}

}
