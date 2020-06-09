package com.hybunion.yirongma.payment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by king on 2016/8/15.
 */
public class Keyboard extends LinearLayout {

    TextView btn0;

    TextView btn1;

    TextView btn2;

    TextView btn3;

    TextView btn4;

    TextView btn5;

    TextView btn6;

    TextView btn7;

    TextView btn8;

    TextView btn9;

    TextView btnac;

    TextView btn_clear;

    LinearLayout btnDel;

    TextView btnDot;

//    TextView btnConfirm;

    TextView amt;

    RecyclerView mRecyclerView;

    ImageView leftIcon;

    ImageView rightIcon;

    private ConfirmListener listener;

    private boolean changed = false;

    private DecimalFormat df = new DecimalFormat("0.00");

    private String lastAmt = "";

    private Context mContext;

    private List<String> mTitles = new ArrayList<>();

    private List<Integer> mIcons = new ArrayList<>();

    public Keyboard(Context context) {
        super(context);
    }

    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //在构造函数中将Xml中定义的布局解析出来。
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.input_amt, this, true);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        amt = (TextView) findViewById(R.id.tv_amt);
        btn0 = (TextView) findViewById(R.id.btn_0);
        btn1 = (TextView) findViewById(R.id.btn_1);
        btn2 = (TextView) findViewById(R.id.btn_2);
        btn3 = (TextView) findViewById(R.id.btn_3);
        btn4 = (TextView) findViewById(R.id.btn_4);
        btn5 = (TextView) findViewById(R.id.btn_5);
        btn6 = (TextView) findViewById(R.id.btn_6);
        btn7 = (TextView) findViewById(R.id.btn_7);
        btn8 = (TextView) findViewById(R.id.btn_8);
        btn9 = (TextView) findViewById(R.id.btn_9);
        btn_clear = (TextView) findViewById(R.id.btn_clear);
        leftIcon = (ImageView) findViewById(R.id.recycler_view_left_arrow);
        rightIcon = (ImageView) findViewById(R.id.recycler_view_right_arrow);

        //   btnac = (TextView) findViewById(R.id.btn_ac);
        btnDel = (LinearLayout) findViewById(R.id.btn_del);
        btnDot = (TextView) findViewById(R.id.btn_dot);
        //    btnConfirm = (TextView) findViewById(R.id.btn_confirm);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        initDatas();
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, R.drawable.fenge));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        HorizontalAdapter mAdapter = new HorizontalAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setMyItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                com.hybunion.yirongma.payment.utils.LogUtil.d(amt.getText().toString().trim()+"输入金额");
                if ((amt.getText().toString()).matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")) {
                    if (TextUtils.isEmpty(amt.getText().toString().trim())) {
                        ToastUtil.show("请输入收款金额");
                        return;
                    }
                    listener.onKeyboardClick();
                }else {
                    ToastUtil.show("请输入收款金额");
                }
            }
        });
//        amt.setText("请输入收款金额");
    }

    private void initDatas() {
        //mTitles.add("银行卡");
        mTitles.add("微信");
        mTitles.add("支付宝");
        mTitles.add("快捷支付");
        mTitles.add("QQ钱包");
        mTitles.add("银联扫码");
        mTitles.add("百度钱包");
        mTitles.add("京东支付");
//        mTitles.add("储值卡");
        //mTitles.add("现金");

        //mIcons.add(R.drawable.swipe);
        mIcons.add(R.drawable.wx);
        mIcons.add(R.drawable.zfb);
        mIcons.add(R.drawable.img_qucik_pay);
        mIcons.add(R.drawable.qqqb);
        mIcons.add(R.drawable.yinlian);
        mIcons.add(R.drawable.bdqb);
        mIcons.add(R.drawable.jd);
//        mIcons.add(R.drawable.valuecard);
        //mIcons.add(R.drawable.xj);

    }

    @OnClick(R.id.btn_0)
    public void onClickBtn0() {
        LogUtil.d("run here");
        setAmt("0");
    }

    @OnClick(R.id.btn_1)
    public void onClickBtn1() {
        setAmt("1");
    }

    @OnClick(R.id.btn_2)
    public void onClickBtn2() {
        setAmt("2");
    }

    @OnClick(R.id.btn_3)
    public void onClickBtn3() {
        setAmt("3");
    }

    @OnClick(R.id.btn_4)
    public void onClickBtn4() {
        setAmt("4");
    }

    @OnClick(R.id.btn_5)
    public void onClickBtn5() {
        setAmt("5");
    }

    @OnClick(R.id.btn_6)
    public void onClickBtn6() {
        setAmt("6");
    }

    @OnClick(R.id.btn_7)
    public void onClickBtn7() {
        setAmt("7");
    }

    @OnClick(R.id.btn_8)
    public void onClickBtn8() {
        setAmt("8");
    }

    @OnClick(R.id.btn_9)
    public void onClickBtn9() {
        setAmt("9");
    }

    @OnClick(R.id.btn_dot)
    public void onClickBtnDot() {
        setAmt(".");
    }

    @OnClick(R.id.btn_clear)
    public void onClickClear(){
        amt.setText("0.00");
        lastAmt = "";
    }
    @OnClick(R.id.ll_wx)
    public void onClickWx(){
        if(TextUtils.isEmpty(amt.getText().toString().trim()) || Double.parseDouble(amt.getText().toString().trim())<=0){
                ToastUtil.show("收款金额大于0");
                return;
        }
        listener.onKeyboardClick();
    }

    //  @OnClick(R.id.btn_ac)
    public void onClickBtnAc() {

    }

    @OnClick(R.id.btn_del)
    public void onClickBtnDel() {
        clearAmt();
    }

//    @OnClick(R.id.btn_confirm)
//    public void onClickBtnConfirm() {
//        if ((amt.getText().toString()).matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")) {
//
//            if ("0.00".equals(amt.getText().toString())) {
//                return;
//            }
//            listener.onClick();
//        }
//    }

    private void setAmt(String num) {
//        if (num.contains(".")) {
//            return;
//        }
        if("0.00".equals(lastAmt)){
            lastAmt = "";
        }

        if (amt.getText().toString().trim().replace(".", "").length() >= 9) {
            return;
        }
        String afterAmt = lastAmt + num;
        setTextOnScreen(afterAmt);
    }

    private void setTextOnScreen(String s) {
        if ("".equals(s)){
            amt.setText("");
//            amt.setTextColor(getResources().getColor(R.color.textColorCar));
            lastAmt = s;
            return;
        }
        if (s.startsWith(".")) {
            s = "0.";
        }
        if (s.startsWith("0") && s.length() > 1 && !s.startsWith("0.")) {
            s = s.substring(1);
        }
        if (s.contains(".")) {
            btnDot.setEnabled(false);
            int dotIndex = s.indexOf(".");
            if (s.length() > dotIndex + 3) {
                s = s.substring(0, dotIndex + 3);
            }
        } else {
            btnDot.setEnabled(true);
        }
        amt.setText(s);
//        amt.setTextColor(getResources().getColor(R.color.black));
        lastAmt = s;
    }

    private void clearAmt() {

        if (amt.getText().toString().trim().equals("0.00")) {
            return;
        }

        if (lastAmt.length()>1){
            String afterDelAmt = lastAmt.substring(0, lastAmt.length() - 1);
            setTextOnScreen(afterDelAmt);
        }else {
            setTextOnScreen("");
        }
    }

    private void initData() {
        amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((amt.getText().toString()).matches("^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$")) {
                    //    btnConfirm.setEnabled(true);
                } else {
                    //   btnConfirm.setEnabled(false);
                }
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    int firstVisibleItem = manager.findFirstCompletelyVisibleItemPosition();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        //加载更多功能的代码
                        rightIcon.setVisibility(INVISIBLE);
                        //    leftIcon.setVisibility(VISIBLE);
                    } else if (lastVisibleItem <= (totalItemCount - 2)) {
                        rightIcon.setVisibility(VISIBLE);
                        //    leftIcon.setVisibility(INVISIBLE);
                    }

                    if (firstVisibleItem > 0) {
                        leftIcon.setVisibility(VISIBLE);
                    } else if (firstVisibleItem == 0) {
                        leftIcon.setVisibility(INVISIBLE);
                    }


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //大于0表示正在向右滚动
//小于等于0表示停止或向左滚动
                isSlidingToLast = dx > 0;
            }
        });
    }

    public void setConfirmListener(ConfirmListener listener) {
        this.listener = listener;
    }

    public String getAmount() {
        return amt.getText().toString().trim();
    }

    public void resetAmount() {
        amt.setText("0.00");
        lastAmt = "";
    }

    public interface ConfirmListener {
        void onKeyboardClick();
    }

    private class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

        private MyItemClickListener mMyItemClickListener;

        public void setMyItemClickListener(MyItemClickListener listener) {
            this.mMyItemClickListener = listener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_recycler_item,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.mIcon = (ImageView) view
                    .findViewById(R.id.id_index_gallery_item_image);
            viewHolder.mTitle = (TextView) view
                    .findViewById(R.id.id_index_gallery_item_text);
            viewHolder.recycler_view_item = (RelativeLayout) view
                    .findViewById(R.id.recycler_view_item);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mIcon.setImageResource(mIcons.get(position));
            holder.mTitle.setText(mTitles.get(position));

            holder.recycler_view_item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMyItemClickListener != null)
                        mMyItemClickListener.onItemClick(v, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTitles.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }


            TextView mTitle;
            ImageView mIcon;
            RelativeLayout recycler_view_item;
        }


    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}



