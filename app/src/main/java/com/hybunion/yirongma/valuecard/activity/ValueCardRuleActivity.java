package com.hybunion.yirongma.valuecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.GetResourceUtil;

import java.util.List;

/**
 * 功能描述：储值卡规则
 * 编写人： myy
 * 创建时间：2017/3/6
 */
public class ValueCardRuleActivity extends Activity implements View.OnClickListener {

    private TextView tv_rule, tv_name, tv_head;
    private LinearLayout ll_back;
    private String name; // 卡名称
    private String rules2 = ""; // 卡规则
    private String type = ""; // 0：次卡；1：充送卡；2：折扣卡
    private String cardType; // 卡片类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardrules);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        tv_rule = (TextView) findViewById(R.id.tv_rule);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setText("卡规则");
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");
        cardType = intent.getStringExtra("cardType");
        List<String> rules = (List<String>) intent.getSerializableExtra("rules");
        for (int i = 0; i < rules.size(); i++) {
            if (i == rules.size() - 1) {
                rules2 = rules2 + "规则" + (i + 1) + "：" + rules.get(i);
            } else {
                rules2 = rules2 + "规则" + (i + 1) + "：" + rules.get(i) + "\n";
            }
        }

        // 先判断是否是任意金额卡
        if ("任意金额卡".equals(cardType) ||"".equals(rules2)){
            tv_rule.setText(GetResourceUtil.getString(R.string.value_card_make_card_any));
        }else {
            tv_rule.setText(rules2);
        }
        tv_name.setText(name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
