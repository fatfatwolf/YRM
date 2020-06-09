package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by Jairus on 2019/7/12.
 */

public class AppletPushBean extends BaseBean<List<AppletPushBean.DataBean>> {

    public class DataBean{

        public String couponId, couponName, condition, storeId, storeName;  // 加入商圈的门店及门店的优惠券

        public String total, fans, member;   // 查询推送人数



    }
}
