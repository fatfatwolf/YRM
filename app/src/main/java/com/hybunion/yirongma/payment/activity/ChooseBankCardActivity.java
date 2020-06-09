package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ChooseBankCardBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 选择结算卡
 */

public class ChooseBankCardActivity extends BasicActivity {
    @Bind(R.id.titleBar_choose_bank_card_activity)
    TitleBar mTitleBar;
    @Bind(R.id.listView_bank_card_choose_bank_card_activity)
    ListView mLv;
    @Bind(R.id.tvNull_choose_bank_card_activity)
    TextView mTvNull;

    private CommonAdapter1 mAdapter;
    private String mLoginType;
    private String mMerId;

    @Override
    protected BasePresenter getPresenter() {
        return null ;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_choose_bank_card_layout;
    }

    @Override
    public void initView() {
        super.initView();
        // 添加 结算卡 加号 点击监听
        mTitleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChooseBankCardActivity.this,AddBankCardActivity.class));
                AddBankCardActivity.start(ChooseBankCardActivity.this, mTiXianName);
            }
        });

        mLv.setAdapter(mAdapter = new CommonAdapter1<ChooseBankCardBean.DataBean>(this,mDataList,R.layout.item_choose_bank_card) {
            @Override
            public void convert(ViewHolder holder, ChooseBankCardBean.DataBean item, int position) {
                ImageView imgBank = holder.findView(R.id.img_bank_item_choose_bank_activity);
                TextView tvBankName = holder.findView(R.id.tv_bankName_item_choose_bank_activity);
                TextView tvWeiHao = holder.findView(R.id.tv_bankDetail_item_choose_bank_activity);
                Glide.with(ChooseBankCardActivity.this).load(item.bankImg).into(imgBank);
                tvBankName.setText(item.bankName);
                tvWeiHao.setText("尾号 "+item.accNo+" "+item.cardType);
            }
        });
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseBankCardBean.DataBean dataBean = mDataList.get(position);
                Intent intent = new Intent();
                intent.putExtra("data",dataBean);
                setResult(132,intent);
                ChooseBankCardActivity.this.finish();
            }
        });


        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {  // 老板
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBankList(mMerId);
    }

    public void getBankList(String merId){
        String url = NetUrl.CHOOSE_BANK_CARD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId",merId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(ChooseBankCardActivity.this, url, jsonObject, new MyOkCallback<ChooseBankCardBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ChooseBankCardBean chooseBankCardBean) {
                if ("0".equals(chooseBankCardBean.getStatus())){
                    if (chooseBankCardBean.getData()!=null && chooseBankCardBean.getData().size()!=0){
                        mDataList.clear();
                        mDataList.addAll(chooseBankCardBean.getData());
                        mAdapter.updateList(mDataList);
                        mTvNull.setVisibility(View.GONE);
                        mTiXianName = mDataList.get(mDataList.size()-1).bankAccName;
                    }else{
                        mTvNull.setVisibility(View.VISIBLE);
                        mTiXianName = "";
                    }
                }else{
                    String msg = chooseBankCardBean.getMessage();
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.show(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ChooseBankCardBean.class;
            }
        });

    }


    private List<ChooseBankCardBean.DataBean> mDataList = new ArrayList<>();
    // 提现人一旦固定不可更改。没有添加结算卡时，第一次添加时，提现人可以手动输入；一旦添加了提现卡，再添加时，提现人就是银行卡最后一条数据的名字，不可更改。
    private String mTiXianName;

}
