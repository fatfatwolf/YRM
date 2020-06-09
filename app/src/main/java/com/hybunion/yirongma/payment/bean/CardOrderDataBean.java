package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class CardOrderDataBean extends BaseBean {
    public List<DataBean> orderData;
    public class DataBean{
        public String storedNum;
        public String acturalMoney;
        public String storedMoney;
        public String merId;
        public String consumeMoney;
        public String consumeNum;
    }
}
