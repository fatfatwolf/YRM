package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class RefundRecordBean extends BaseBean<List<RefundRecordBean.DataBean>> {

    public class DataBean{
        public String UUID;
        public String status;
        public String crateDate;
        public String amount;
    }
}
