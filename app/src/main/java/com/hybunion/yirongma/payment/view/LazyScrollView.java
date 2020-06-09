package com.hybunion.yirongma.payment.view;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class LazyScrollView extends ScrollView {
	
	private static final String tag="LazyScrollView";
	private Handler handler;
	private View view;
	public LazyScrollView(Context context) {
		super(context);
	}
	public LazyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LazyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		onScrollListener.onAutoScroll(l, t, oldl, oldt);
	}
	
	public int computeVerticalScrollRange(){
		return super.computeHorizontalScrollRange();
	}
	public int computeVerticalScrollOffset(){
		return super.computeVerticalScrollOffset();
	}
	private void init(){
		
		this.setOnTouchListener(onTouchListener);
//		handler=new Handler(){
//        	@Override
//			public void handleMessage(Message msg) {
//				// process incoming messages here
//				super.handleMessage(msg);
//				switch(msg.what){
//				case 1:
//					if(view.getMeasuredHeight() <= getScrollY() + getHeight()) {
//						if(onScrollListener!=null){
//							onScrollListener.onBottom();
//						}
//						
//					}else if(getScrollY()==0){
//						if(onScrollListener!=null){
//							onScrollListener.onTop();
//						}
//					}
//					else{
//						if(onScrollListener!=null){
//							onScrollListener.onScroll();
//						}
//					}
//					break;
//				default:
//					break;
//				}
//			}
//        };
		
	}
	
	  OnTouchListener onTouchListener=new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if(view!=null&&onScrollListener!=null){
						if(view.getMeasuredHeight() <= getScrollY() + getHeight()) {
							if(onScrollListener!=null){
								onScrollListener.onBottom();
							}
							
						}else if(getScrollY()==0){
							if(onScrollListener!=null){
								onScrollListener.onTop();
							}
						}
						else{
							if(onScrollListener!=null){
								onScrollListener.onScroll();
							}
						}
//						handler.sendMessage(handler.obtainMessage(1));
					}
					break;

				default:
					break;
				}
				return false;
			}
	    	
	    };
	    
	    public void getView(){
	    	this.view=getChildAt(0);
	    	if(view!=null){
	    		init();
	    	}
	    }

	    public interface OnScrollListener{
	    	void onBottom();
	    	void onTop();
	    	void onScroll();
	    	void onAutoScroll(int l, int t, int oldl, int oldt);
	    }
	    private OnScrollListener onScrollListener;
	    public void setOnScrollListener(OnScrollListener onScrollListener){
	    	this.onScrollListener=onScrollListener;
	    }
	    
//	    @Override
//        public void fling(int velocityY) {
//            super.fling(velocityY*2);
//        }

}
