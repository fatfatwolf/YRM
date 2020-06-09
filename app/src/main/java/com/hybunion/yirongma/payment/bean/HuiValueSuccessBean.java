package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

public class HuiValueSuccessBean extends BaseBean{
    public  DataBean historyData;
    public class DataBean{
        public String consumeMoney;
        public String otherMoney;
        public String storedPeople;
    }

}
