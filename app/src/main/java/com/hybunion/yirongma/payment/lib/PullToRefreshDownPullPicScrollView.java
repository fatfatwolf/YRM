package com.hybunion.yirongma.payment.lib;

import android.content.Context;
import android.util.AttributeSet;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.view.LazyScrollView;

/**
 * 只有下拉：下拉只是看图片  不上拉
 * @author 贾占彪
 *
 */
public class PullToRefreshDownPullPicScrollView extends PullToRefreshBase<LazyScrollView> {

	public PullToRefreshDownPullPicScrollView(Context context) {
		super(context);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	public PullToRefreshDownPullPicScrollView(Context context, int mode) {
		super(context, mode);

		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	public PullToRefreshDownPullPicScrollView(Context context, AttributeSet attrs) {

		super(context, attrs);
		/**
		 * Added so that by default, Pull-to-Refresh refreshes the page
		 */
	}

	@Override
	protected LazyScrollView createRefreshableView(Context context, AttributeSet attrs) {
		LazyScrollView scrollView = new LazyScrollView(context, attrs);
		scrollView.setId(R.id.webview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullDown() {
		//Log.d("text", "mScrollView.getHeight()="+this.getHeight());
		return refreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullUp() {
		LazyScrollView view = getRefreshableView();
		int off=view.getScrollY()+view.getHeight()-view.getChildAt(0).getHeight();
		//Log.d("text", "view.getScrollY()="+view.getScrollY()+",view.getChildAt(0).getHeight()"+view.getChildAt(0).getHeight());
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
