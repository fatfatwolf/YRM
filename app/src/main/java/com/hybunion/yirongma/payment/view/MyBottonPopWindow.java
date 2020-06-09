package com.hybunion.yirongma.payment.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.KuanTaiListAdapter2;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter2;
import com.hybunion.yirongma.payment.adapter.ViewPageAdapter;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

public class MyBottonPopWindow extends PopupWindow {
    private Activity mContext;
    private CustomViewPager mViewPager;
    View storeView, kuanTaiView, line_kuantai;
    private List<View> viewList = new ArrayList<>();
    ViewPageAdapter adapter;
    ListView listview_store;
    RelativeLayout rv_popupwindow_store;
    ImageView iv_close;
    TextView tv_store_name;
    View view_line_store;
    LinearLayout ll_kuantai;
    TextView tv_kuantai_name;
    ListView listview_kuantai;
    Button bt_sure;
    StoreListAdapter2 storeListAdapter;
    KuanTaiListAdapter2 kuanTaiListAdapter;
    List<StoreManageBean.ObjBean> storeList;
    List<StoreManageBean.ObjBean> kuantaiList;
    private TextView mTvNull;
    private TextView mTvTitle;

    int type = 0;

    public MyBottonPopWindow(Activity context, List<StoreManageBean.ObjBean> storeList) {
        super(context);
        this.mContext = context;
        this.storeList = storeList;
        initView();
    }

    public MyBottonPopWindow(View contentView, int width, int height, Activity mContext) {
        super(contentView, width, height);
        this.mContext = mContext;
    }

    public void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_store_popupwindow, null);
        setContentView(view);
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight((4 * height) / 5);
        //绑定控件Id
        mTvTitle = view.findViewById(R.id.tv_title_store_popwindow);
        mViewPager = view.findViewById(R.id.mViewPager);
        storeView = LayoutInflater.from(mContext).inflate(R.layout.layout_store_list, null);
        kuanTaiView = LayoutInflater.from(mContext).inflate(R.layout.layout_kuantai_list, null);
        listview_store = storeView.findViewById(R.id.listview_store);
        rv_popupwindow_store = view.findViewById(R.id.rv_popupwindow);
        ll_kuantai = view.findViewById(R.id.ll_kuantai);
        view_line_store = view.findViewById(R.id.view_line_store);
        tv_kuantai_name = view.findViewById(R.id.tv_kuantai_name);
        line_kuantai = view.findViewById(R.id.line_kuantai);
        listview_kuantai = kuanTaiView.findViewById(R.id.listview_kuantai);
        iv_close = view.findViewById(R.id.iv_close);
        bt_sure = view.findViewById(R.id.bt_sure);
        tv_store_name = view.findViewById(R.id.tv_store_name);
        mTvNull = view.findViewById(R.id.tv_null);
        setViewData();

    }

    // 设置 title
    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title))
            mTvTitle.setText(title);
    }


    // 没有数据，显示空界面
    public void noData() {
        mTvNull.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
    }


    public void setViewData() {
        //设置popupWindow属性
        setFocusable(true);//设置popupwindow可以获取焦点
        //给pupupwindow设置一个背景 透明的,以便点击popupwindow外边时候可以实现消失
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(false);//设置点击外部消失
//

        viewList.add(storeView);
        viewList.add(kuanTaiView);
        adapter = new ViewPageAdapter(viewList);
//
        kuanTaiListAdapter = new KuanTaiListAdapter2(mContext);
//        listview_store.setAdapter(storeListAdapter);
        listview_kuantai.setAdapter(kuanTaiListAdapter);
        scrollListener = new MyOnPageChangeListener();
        mViewPager.setOnPageChangeListener(scrollListener);
        mViewPager.setAdapter(adapter);
        mViewPager.setScanScroll(false);
        tv_store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                tv_store_name.setTextColor(mContext.getResources().getColor(R.color.btn_back_color1));
                view_line_store.setVisibility(View.VISIBLE);
                ll_kuantai.setVisibility(View.VISIBLE);
                type = 1;
            }
        });
        ll_kuantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_line_store.setVisibility(View.GONE);
                mViewPager.setCurrentItem(1);
                tv_store_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
                listview_kuantai.setVisibility(View.VISIBLE);
                tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.lmf_main_color));
                ll_kuantai.setVisibility(View.VISIBLE);
                type = 0;
            }
        });
        listview_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (storeListener != null) {
                    if (storeList != null && storeList.size() > 0)
                        tv_store_name.setText(storeList.get(position).getStoreName());

                    storeListAdapter.setSelectedPosition(position);
                    storeListAdapter.notifyDataSetInvalidated();
                    tv_kuantai_name.setText("请选择");
                    tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
                    storeListener.setStoreItemListener(position);
                }

            }
        });
        listview_kuantai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (kuanTaiListener != null) {
                    kuanTaiListener.setKuanTaiItemListener(position);
                    tv_kuantai_name.setText(kuantaiList.get(position).getStoreName());
                    tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.btn_back_color1));
                    kuanTaiListAdapter.setSelectedPosition(position);
                    kuanTaiListAdapter.notifyDataSetInvalidated();
                }

            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                setBackgroundAlpha(1.0f);
//                listview_kuantai.setVisibility(View.GONE);
//                listview_store.setVisibility(View.VISIBLE);
//                ll_kuantai.setVisibility(View.GONE);
//                tv_store_name.setText("请选择");
//                storeListAdapter.setSelectedPosition(-1);
//                storeListAdapter.notifyDataSetChanged();

                if (dissmissListener != null)
                    dissmissListener.setDissmissListener();
            }
        });


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                setBackgroundAlpha(1.0f);
//                listview_kuantai.setVisibility(View.GONE);
//                listview_store.setVisibility(View.VISIBLE);
//                ll_kuantai.setVisibility(View.GONE);
//                tv_store_name.setText("请选择");
//                storeListAdapter.setSelectedPosition(-1);
//                storeListAdapter.notifyDataSetChanged();
                if (onCloseListener != null)
                    onCloseListener.setOnCloseListener();
            }
        });

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.setButtonClickListener();
                if (!mIsStoreClerk)
                    dismiss();
            }
        });

        if (storeList != null) {
            if (storeListAdapter == null) {
                storeListAdapter = new StoreListAdapter2(mContext, storeList);
            }
            listview_store.setAdapter(storeListAdapter);
        }
    }


    /**
     * 设置屏幕透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mContext.getWindow().setAttributes(lp);
    }

    public void showPopupWindow(int chooseStorePosition) {

        setBackgroundAlpha(0.5f);
        storeListAdapter.setSelectedPosition(chooseStorePosition);
        setAnimationStyle(R.style.dialog_anim);
        showAtLocation(rv_popupwindow_store, Gravity.BOTTOM, 0, 0);

    }


    public void showKuanTaiList(List<StoreManageBean.ObjBean> kuantaiList, OnKuanTaiItemListener kuanTaiListener) {
        this.kuanTaiListener = kuanTaiListener;
        this.kuantaiList = kuantaiList;
        if (kuantaiList != null && kuantaiList.size() > 0) {
//            if(viewList.size()<2){
//                viewList.add(kuanTaiView);
//                adapter.notifyDataSetChanged();
//
//            }
            mViewPager.setScanScroll(true);
            type = 1;
            view_line_store.setVisibility(View.GONE);
            ll_kuantai.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(1);
            tv_store_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
            listview_kuantai.setVisibility(View.VISIBLE);
            kuanTaiListAdapter.addAllList(kuantaiList);
        } else {
            ll_kuantai.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
            mViewPager.setScanScroll(false);
            type = 0;
        }
    }

    // 店员列表
    private List<QueryClerkListBean.ObjBean> mClerkList = new ArrayList<>();
    private CommonAdapter1 mClerkAdapter;
    private boolean mIsStoreClerk;

    public void showClerkList(List<QueryClerkListBean.ObjBean> list, OnKuanTaiItemListener kuanTaiListener) {
        mIsStoreClerk = true;
        mClerkList = list;
        this.kuanTaiListener = kuanTaiListener;
        if (!YrmUtils.isEmptyList(mClerkList)) {
            mViewPager.setScanScroll(true);
            type = 1;
            view_line_store.setVisibility(View.GONE);
            ll_kuantai.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(1);
            tv_store_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
            listview_kuantai.setVisibility(View.VISIBLE);
            listview_kuantai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (MyBottonPopWindow.this.kuanTaiListener != null) {
                        MyBottonPopWindow.this.kuanTaiListener.setKuanTaiItemListener(position);
                    }
                    tv_kuantai_name.setText(mClerkList.get(position).getEmployName());
                    tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.btn_back_color1));
                    mClerkList.get(position).isClicked = true;
                    if (mClerkAdapter != null)
                        mClerkAdapter.notifyDataSetChanged();


                }
            });
            if (mClerkAdapter == null)
                listview_kuantai.setAdapter(mClerkAdapter = new CommonAdapter1<QueryClerkListBean.ObjBean>(mContext, mClerkList, R.layout.item_store_list2) {
                    @Override
                    public void convert(ViewHolder holder, QueryClerkListBean.ObjBean item, int position) {
                        TextView tvName = holder.findView(R.id.tv_list_name);
                        ImageView img = holder.findView(R.id.iv_right);
                        tvName.setText(item.getEmployName() + " " + item.getEmployPhone());
                        if (item.isClicked)
                            img.setVisibility(View.VISIBLE);
                        else
                            img.setVisibility(View.GONE);
                    }
                });


        }

    }


    public void setStoreItemListListener(OnStoreItemListener storeListener) {
        this.storeListener = storeListener;
    }

    public void setButtonClickListener(OnSureClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDissmissListener(onDissmissListener dissmissListener) {
        this.dissmissListener = dissmissListener;
    }

    public void setOnCloseListener(onCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }


    //门店列表的点击回调
    public OnStoreItemListener storeListener;

    public interface OnStoreItemListener {
        void setStoreItemListener(int position);
    }


    //款台列表的点击回调
    public OnKuanTaiItemListener kuanTaiListener;

    public interface OnKuanTaiItemListener {
        void setKuanTaiItemListener(int position);
    }

    //确定按钮的回调
    public OnSureClickListener clickListener;

    public interface OnSureClickListener {
        void setButtonClickListener();
    }

    //点击外部消失的回调
    public onDissmissListener dissmissListener;

    public interface onDissmissListener {
        void setDissmissListener();
    }

    //点击外部消失的回调
    public onCloseListener onCloseListener;

    public interface onCloseListener {
        void setOnCloseListener();
    }


    public MyOnPageChangeListener scrollListener;

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //页面滑动时候调用。在滑动停止前，此方法一直得到回调
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        //状态改变时候调用
        @Override
        public void onPageScrollStateChanged(int i) {
            if (i == 2) {//滑动完毕
                if (type == 1) {
                    view_line_store.setVisibility(View.GONE);
                    mViewPager.setCurrentItem(1);
                    tv_store_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
                    listview_kuantai.setVisibility(View.VISIBLE);
                    tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.lmf_main_color));
                    ll_kuantai.setVisibility(View.VISIBLE);
                    type = 0;
                } else if (type == 0) {
                    mViewPager.setCurrentItem(0);
                    tv_store_name.setTextColor(mContext.getResources().getColor(R.color.lmf_main_color));
                    view_line_store.setVisibility(View.VISIBLE);
                    tv_kuantai_name.setTextColor(mContext.getResources().getColor(R.color.text_color2));
                    line_kuantai.setVisibility(View.GONE);
                    type = 1;
                }
            }
        }
    }

}
