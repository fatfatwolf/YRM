package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 体现记录
 */

public class TiXianNewRecordBean extends BaseBean {
    public boolean success;
    public List<TiXianNewRecordBean.DataBean> obj;
    public String msg;
    public class DataBean{
        public double CASHAMT,   // 提现金额
                CASHFEE;       // 提现手续费
        public String CASHSTATUS;      // 提现状态 1-审核中  2 4-提现成功   5-处理中

        public String CASHDATE;        // 提现时间

    }

}
