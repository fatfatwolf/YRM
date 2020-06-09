package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class XiuCanMerBean extends BaseBean<List<XiuCanMerBean.DataBean>> {

    public class DataBean{
        public String shopName;
        public String tel;
        public String city;
        public String address;
    }
}
