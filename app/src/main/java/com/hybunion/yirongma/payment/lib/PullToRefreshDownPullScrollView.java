package com.hybunion.yirongma.payment.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.hybunion.yirongma.R;

/**
 * 普通的ScrollView 实现pull:下拉刷新  不加上拉
 * @author 贾占彪
 *
 */
public class PullToRefreshDownPullScrollView extends PullToRefreshBase<ScrollView> {

	public PullToRefreshDownPullScrollView(Context context) {
		super(context);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	public PullToRefreshDownPullScrollView(Context context, int mode) {
		super(context, mode);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	public PullToRefreshDownPullScrollView(Context context, AttributeSet attrs) {

		super(context, attrs);
		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView = new ScrollView(context, attrs);
		scrollView.setId(R.id.webview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullDown() {
//		Log.d("text", "mScrollView.getHeight()="+this.getHeight());
		return refreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp() {
		ScrollView view = getRefreshableView();
		int off=view.getScrollY()+view.getHeight()-view.getChildAt(0).getHeight();
//		Log.d("text", "view.getScrollY()="+view.getScrollY()+",view.getChildAt(0).getHeight()"+view.getChildAt(0).getHeight());
        return off == 0;
        /* if (view.getScrollY() + view.getHeight() >=  view.getMeasuredHeight()) {  
             //加载数据代码  
        	 return true;
         } else{
	    	return false;
	    }  */
		//return (refreshableView.getScrollY() >= refreshableView.getHeight());
	}
}