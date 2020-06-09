package com.hybunion.yirongma.payment.lib.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;


public class LoadingLayout extends BaseLoadingLayout {

    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

    private final ImageView headerImage, header_pic;
    //private final ProgressBar headerProgress;
    private final TextView headerText;
    //private final TextView headerText1;
    private final Animation ani, ani2, ani3;
    private final ImageView headerImage2, headerImage3;
    private TextView loadingText;

    private String pullLabel, pullLabel1;
    private String refreshingLabel;
    private String releaseLabel, releaseLabel1;
    private String currentTime;//当前的更新时间
    private String oldTime;//上一次更新时间
    private Context mContext;
    private boolean flagShow;//是否出现几亿人那个图片：

    //private final Animation resetRotateAnimation;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    headerImage.startAnimation(ani);
                    headerImage2.startAnimation(ani2);
                    headerImage3.startAnimation(ani3);
                    break;
            }
        }
    };

    public LoadingLayout(Context context, final int mode, String releaseLabel, String releaseLabel1, String pullLabel, String pullLabel1, String refreshingLabel, boolean flagShow1) {
        super(context, mode, releaseLabel, releaseLabel1, pullLabel, pullLabel1, refreshingLabel);
        mContext = context;
        ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);
        headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);//下拉可以刷新
        //headerText1 = (TextView) header.findViewById(R.id.pull_to_refresh_text1);//下拉可以刷新
        loadingText = (TextView) header.findViewById(R.id.pull_to_refresh_loading_text);//刷新加载
        headerImage = (ImageView) header.findViewById(R.id.pull_to_refresh_image);
        headerImage2 = (ImageView) header.findViewById(R.id.pull_to_refresh_image2);
        headerImage3 = (ImageView) header.findViewById(R.id.pull_to_refresh_image3);
        //headerProgress = (ProgressBar) header.findViewById(R.id.pull_to_refresh_progress);
        header_pic = (ImageView) header.findViewById(R.id.pull_head_pic);
        flagShow = flagShow1;

        //if(flagShow) viewVisible();

        ani = AnimationUtils.loadAnimation(mContext, R.anim.log_move);
        ani2 = AnimationUtils.loadAnimation(mContext, R.anim.log_move1);
        ani3 = AnimationUtils.loadAnimation(mContext, R.anim.log_move2);
        //final Interpolator interpolator = new LinearInterpolator();
//		rotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//		        0.5f);
//		rotateAnimation.setInterpolator(interpolator);
//		rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
//		rotateAnimation.setFillAfter(true);

//		resetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
//		        Animation.RELATIVE_TO_SELF, 0.5f);
//		resetRotateAnimation.setInterpolator(interpolator);
//		resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
//		resetRotateAnimation.setFillAfter(true);

        this.releaseLabel = releaseLabel;//"松开即可刷新"
        this.pullLabel = pullLabel;//拖拉可以刷新
//		this.releaseLabel1 = releaseLabel1;
        this.refreshingLabel = refreshingLabel;
//
//		if(pullLabel1.equals("")){
//			this.pullLabel1 = mContext.getResources().getString(R.string.pull_loading_old_geng_ganggang);
//		}else{
//			this.pullLabel1 = mContext.getResources().getString(R.string.pull_loading_old_geng)+oldTime;
//		}
//
//		currentTime= CommonMethod.getMyCurrentTime();
//        this.releaseLabel1 = mContext.getResources().getString(R.string.pull_loading_new_geng) + currentTime;

//        switch (mode) {
//            case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
//                headerImage.setImageResource(R.drawable.isloading1);
//                headerImage2.setImageResource(R.drawable.isloading2);
//                headerImage3.setImageResource(R.drawable.isloading3);
//                break;
//            case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
//                headerImage.setImageResource(R.drawable.isloading1);
//                headerImage2.setImageResource(R.drawable.isloading2);
//                headerImage3.setImageResource(R.drawable.isloading3);
//                break;
//            default:
//                break;
//        }
    }

//	//几亿图片提示：
//	private void viewVisible(){
//		header_pic.setVisibility(View.VISIBLE);headerImage.setVisibility(View.GONE);headerProgress.setVisibility(View.GONE);headerText.setVisibility(View.GONE);
//		headerText1.setVisibility(View.GONE);loadingText.setVisibility(View.GONE);
//	}
//
//	//没有几亿图片提示：
//	private void viewGone(){
//		header_pic.setVisibility(View.GONE);headerImage.setVisibility(View.VISIBLE);headerProgress.setVisibility(View.VISIBLE);headerText.setVisibility(View.VISIBLE);
//		headerText1.setVisibility(View.VISIBLE);loadingText.setVisibility(View.VISIBLE);
//	}

    public void reset() {//避免最后又出现一下
//		headerText.setText(pullLabel);
//		headerImage.setVisibility(View.VISIBLE);
//		if(pullLabel1.equals("")){
//			this.pullLabel1 = mContext.getResources().getString(R.string.pull_loading_old_geng_ganggang);
//		}else{
//			this.pullLabel1 = mContext.getResources().getString(R.string.pull_loading_old_geng)+oldTime;
//		}

        //headerProgress.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);

        headerText.setVisibility(View.VISIBLE);
        //headerText1.setVisibility(View.VISIBLE);
        headerText.setText(pullLabel);
        //headerText1.setText(pullLabel1);
//        headerImage.startAnimation(set);
//        headerImage2.startAnimation(set);
//        headerImage3.startAnimation(set);

        //if(flagShow) viewVisible();
    }

    public void releaseToRefresh() {
//		currentTime= CommonMethod.getMyCurrentTime();
//		this.releaseLabel1 =mContext.getResources().getString(R.string.pull_loading_new_geng)+currentTime;
//		oldTime=currentTime;//记住当前更新时间，下次下拉显示
        headerText.setVisibility(View.VISIBLE);
        //	headerText1.setVisibility(View.VISIBLE);
        headerText.setText(releaseLabel);
        //	headerText1.setText(releaseLabel1);

        headerImage.startAnimation(ani);
        headerImage2.startAnimation(ani2);
        headerImage3.startAnimation(ani3);
        ani3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(0, 50);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //if(flagShow) viewVisible();
    }

    public void setPullLabel(String pullLabel) {
        this.pullLabel = pullLabel;
    }

    public void refreshing() {

        loadingText.setVisibility(View.VISIBLE);
        loadingText.setText(pullLabel);
        headerText.setVisibility(View.GONE);
        //	headerText1.setVisibility(View.GONE);
        //	headerImage.setVisibility(View.GONE);
//		headerProgress.setVisibility(View.VISIBLE);
        headerImage.startAnimation(ani);
        headerImage2.startAnimation(ani2);
        headerImage3.startAnimation(ani3);
        ani3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(0, 50);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//
//		if(flagShow) viewVisible();
    }

    public void setRefreshingLabel(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }
//
//	public void setReleaseLabel(String releaseLabel) {
//		this.releaseLabel = releaseLabel;
//	}

    public void pullToRefresh() {
        headerText.setText(pullLabel);
        //headerImage.clearAnimation();
        //headerImage.startAnimation(resetRotateAnimation);
        headerImage.startAnimation(ani);
        headerImage2.startAnimation(ani2);
        headerImage3.startAnimation(ani3);
        ani3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(0, 50);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setTextColor(int color) {
        headerText.setTextColor(color);
    }

}
