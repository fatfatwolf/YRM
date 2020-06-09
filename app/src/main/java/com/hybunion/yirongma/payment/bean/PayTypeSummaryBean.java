package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class PayTypeSummaryBean extends BaseBean<List<PayTypeSummaryBean.DataBean>> {

    public String totalAmt, totalCount;

    public class DataBean{

        public String payChannel,   // 支付类型
                transCount,
                transAmount,
                refundCount,
                refundAmount;

    }

}
