package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 优惠券列表用
 */

public class YouHuiQuanListBean extends BaseBean<List<YouHuiQuanListBean.DataBean>> {

    public class DataBean{
        public String couponId, // 优惠券 id
                couponName,       // 优惠券主题
                couponNumber,     // 优惠券总数
                type,             // 优惠券类别
                receiveNumber,    // 优惠券领取数量
                couponStatus,    // 0未上线 1已上线 2已下线 3已过期
                validStartDate,   // 优惠券开始生效时间
                validEndtDate,    // 优惠券结束生效时间
                withAmount,       // 满多少金额
                usedAmount,       // 减多少金额
                couponRules;     // 优惠券规则，如 "3元券，满10元使用"

    }


}
