package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class ClerkRecordBean extends BaseBean<List<ClerkRecordBean.DataBean>> {
    public String count;

    public class DataBean{
        public String id;
        public String empId;
        public String cashierName;
        public String startDate;
        public String endDate;
        public String transCount;
        public String transAmount;
    }
}
