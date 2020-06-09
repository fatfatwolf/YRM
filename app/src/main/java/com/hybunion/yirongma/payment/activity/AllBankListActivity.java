package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AllBankBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.QueryAllBankNameAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/19.
 */

public class AllBankListActivity extends BasicActivity {
    @Bind(R.id.et_bank_info)
    EditText etBankInfo;
    @Bind(R.id.bank_listview)
    protected ListView bank_listview;
    @Bind(R.id.bt_serch)
    Button bt_serch;
    List<AllBankBean.BankInfo> list;
    List<AllBankBean.BankInfo> newList = new ArrayList<>();
    private QueryAllBankNameAdapter adapter;
    private int flag;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.new_company_bank_info;
    }

    @Override
    protected void load() {
        super.load();
        querAllBankName();
    }
    public void querAllBankName() {
        String url = NetUrl.QUERYALLBANKNAME;
        OkUtils.getInstance().post(AllBankListActivity.this, url, (JSONObject) null, new MyOkCallback<AllBankBean>() {
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
        public void initView () {
            super.initView();
            flag = 0;
            adapter = new QueryAllBankNameAdapter(this);
            bank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AllBankBean.BankInfo info;
                    Intent intent = new Intent();
                    if (flag == 0) {
                        info = list.get(position);
                    } else {
                        info = newList.get(position);
                    }
                    intent.putExtra("bankName", info.getPaymentBank());
                    intent.putExtra("bankImg", info.getPaymentBankImg());
                    intent.putExtra("payBankId", info.getPaymentLine());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            etBankInfo.addTextChangedListener(watcher);
        }

        private TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null == s) {
                    return;
                }
                String bankInfo = etBankInfo.getText().toString().trim();
                if (TextUtils.isEmpty(bankInfo)) {
                    flag = 0;
                    bank_listview.setVisibility(View.VISIBLE);
                    adapter.addAllList(list);
                    bank_listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    if (newList.size() != 0) {
                        newList.clear();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPaymentBank().indexOf(bankInfo) != -1) {
                            newList.add(list.get(i));
                        }
                    }
                    if (newList.size() == 0) {
                        bt_serch.setEnabled(true);
                        bank_listview.setVisibility(View.GONE);
                    } else {
                        flag = 1;
                        bank_listview.setVisibility(View.VISIBLE);
                        bt_serch.setEnabled(false);
                        adapter.addAllList(newList);
                        bank_listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        };

        @OnClick(R.id.bt_serch)
        public void getSerch () {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("bankName", etBankInfo.getText().toString().trim());
            bundle.putString("chooseType", "2");
            intent.putExtra("info", bundle);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void initData () {
            super.initData();
        }

        @Override
        protected void loadData () {
            super.loadData();
        }
    }
