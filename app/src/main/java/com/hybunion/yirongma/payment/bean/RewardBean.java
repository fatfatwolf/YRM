package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 代理商计划中的奖励详情列表用
 */

public class RewardBean extends BaseBean<List<RewardBean.DataBean>> {

    public class DataBean{
        public String rewardAmt, createDate, remark;

    }

}
