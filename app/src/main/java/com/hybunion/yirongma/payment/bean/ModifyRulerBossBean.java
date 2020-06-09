package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class ModifyRulerBossBean extends BaseBean<List<ModifyRulerBossBean.DataBean>> {

    public class DataBean{
        public String id;
        public String popFlag;
        public String vcSale;
        public String isShare;
        public String shareNum;

        public List<HuiListBean2> cardRules;

        public List<StoreIdBean> storeIds;
    }
}
