package com.hybunion.yirongma.payment.model;

import com.hybunion.yirongma.payment.bean.QueryTransBean;

import java.util.List;

/**
 * 订单列表 数据库 使用
 * 用于数据库查询返回
 */

public class QueryDataBean {
    public int  transCount;
    public double transAmount;
    public List<QueryTransBean.DataBean> dataList;


}
