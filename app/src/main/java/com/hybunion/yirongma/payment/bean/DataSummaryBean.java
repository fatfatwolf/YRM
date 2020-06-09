package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class DataSummaryBean extends BaseBean<List<DataSummaryBean.DataBean>> {
    public String totalCount;
    public String totalAmt;
    public String totalCouponCount;  // 优惠券交易笔数
    public String totalCouponAmt;    // 优惠券交易金额
    public String totalRegCount;//	充值总笔数
    public String totalRegAmt;//充值总金额
    public String totalSettleAmt;//可结算金额

    public String totaltransCount;//消费总笔数
    public String totaltransAmt;//消费总金额

    public String totalRefundAmt, totalRefundCount;  //总退款金额  总退款笔数



    public class DataBean{
        public String storeId;

        public String storeName;

        public String transCount;

        public String transSum;

        public String refundCount;

        public String refundSum;
    }




}
