package com.hybunion.yirongma.payment.model;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 账单详情
 */

public class BillDetailsBean extends BaseBean<List<BillDetailsBean.DataBean>> {

    public class DataBean{
        public String transAmount,transDate,payChannel,status,orderNo,tid,sevenDate,refundOrderNo,refundDate,remark;
        public String payableAmount;  // 实际支付金额
        public String storeName;  // 和卡详情，增加消费门店

    }

}
