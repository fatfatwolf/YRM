package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class MemberStoreBean extends BaseBean<List<MemberStoreBean.DataBean>> {

    public class DataBean{
        public String storeName;
        public String id;
    }
}
