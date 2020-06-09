package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class MemberListBean extends BaseBean<List<MemberListBean.DataBean>> {
    public String count;

    public class DataBean{
        public String member_code;
        public String nick_name;
        public String wx_phone;
        public String trans_num;
        public String trans_amount;
        public String avgAmount;
        public String rowIndex;
    }

}
