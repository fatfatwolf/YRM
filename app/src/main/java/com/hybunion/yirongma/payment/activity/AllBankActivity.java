package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.bean.AllBankBean;
import com.hybunion.yirongma.payment.adapter.QueryAllBankNameAdapter;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;

public class AllBankActivity extends BasicActivity {

    @Bind(R.id.bank_listview)
    protected ListView bank_listview;
    List<AllBankBean.BankInfo> list;

    private QueryAllBankNameAdapter adapter;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_all_bank;
    }

    @Override
    protected void load() {
        super.load();
        querAllBankName();
    }

    public void querAllBankName() {
        String url = NetUrl.QUERYALLBANKNAME;
        OkUtils.getInstance().post(AllBankActivity.this, url, (JSONObject) null, new MyOkCallback<AllBankBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(AllBankBean bean) {
                list = bean.getData();
                adapter.addAllList(list);
                bank_listview.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return AllBankBean.class;
            }
        });
    }


    @Override
    public void initView() {
        super.initView();
        adapter = new QueryAllBankNameAdapter(this);
        bank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllBankBean.BankInfo info;
                Intent intent=new Intent();
                info = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info",info);
                intent.putExtra("info",bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected void loadData() {
        super.loadData();
    }
}
