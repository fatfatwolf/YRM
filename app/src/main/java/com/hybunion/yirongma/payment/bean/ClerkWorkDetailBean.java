package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class ClerkWorkDetailBean extends BaseBean<List<ClerkWorkDetailBean.DataBean>>{

    public String totalCount;

    public String totalAmt;

    public String reTotalCount;

    public String reTotalAmt;

    public class DataBean{
        public String name;
        public String transCount;
        public String transSum;
    }
}
