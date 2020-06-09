package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

public class DutyTimeBean extends BaseBean<DutyTimeBean.DataBean> {

    public String count;

    public class DataBean{
        public String startDate;
        public String endDate;
    }


}
