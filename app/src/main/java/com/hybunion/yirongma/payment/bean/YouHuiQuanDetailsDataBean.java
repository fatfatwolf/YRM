package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * 优惠券详情页
 */

public class YouHuiQuanDetailsDataBean extends BaseBean{
    public TotalDataBean totalData;
    public TodayDataBean todayData;

    public class TotalDataBean{
        // 券数据用
        public String couponName,   // 优惠券名称
                receiveNumber,      // 优惠券数量
                cavNumber,          // 核销数量
                cavRate;           //  核销率

        // 券信息 用
        public String
                useDate,            // 使用时间
                couponNumber,      // 优惠券数量
                startDate,         // 开始时间
                endDate,           // 截止时间
                couponType,        // 优惠券类型 （1是满减券）
                useCycle,          // 使用周期

                couponStatus,      // 优惠券状态
                storeName,         // 归属门店
                effectRules,       // 优惠券生效规则.次日生效，当日生效
                conditions;        // 使用条件


        public String disableTimeOne;//不可用时间1
        public String disableTimeTwo;//不可用时间2
        public String isDisabled;//是否开启不可用时间  1关闭  2 开启
        public int validType;//使用时效
        public int effectType;//生效时间
        public int quota;//制券数量
        public int validDays;//生效后几天有效
        public String validStartTime;//开始时期
        public String validEndTime;//结束时期
        public String storeId;
        public String couponId;
        public double couponMoney;     // 单券优惠



    }

    public class TodayDataBean{
        public String couponName,   // 优惠券名称
                receiveNumber,      // 优惠券数量
                cavNumber,          // 核销数量
                cavRate;           //  核销率
    }





}
