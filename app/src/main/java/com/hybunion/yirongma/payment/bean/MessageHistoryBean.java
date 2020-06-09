package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class MessageHistoryBean extends BaseBean<List<MessageHistoryBean.DataBean>> {

    public String count;
    public class DataBean{
        public String Id;
        public String message;
        public String pushDate;
        public String memberType;
        public String memberTotal;
        public String dateType;
    }

}
