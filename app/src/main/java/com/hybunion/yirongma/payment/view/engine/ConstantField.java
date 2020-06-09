package com.hybunion.yirongma.payment.view.engine;

import org.json.JSONArray;

/**
 * @author SunBingbing
 * @date 2017/4/6
 * @email freemars@yeah.net
 * @description 项目中的所有常量字段都在此定义
 */

public class ConstantField {

    /** 友盟数据统计计数事件字段 */
    // 惠买单
    public static final String CLICK_HUIORDER_REALTIME_DEAL = "click_huiorder_realtime_deal"; // 点击 惠买单-实时交易
    public static final String CLICK_HUIORDER_REPORT_CHART_ALL = "click_huiorder_report_chart_all"; // 点击 惠买单-报表-汇总报表
    public static final String CLICK_HUIORDER_REPORT_CHART_CHECK_MONEY = "click_huiorder_report_chart_check_money"; // 点击 惠买单-报表-对账折线图
    public static final String CLICK_HUIORDER_REPORT_CHART_RECHARGE = "click_huiorder_report_chart_recharge"; // 点击 惠买单-报表-充值
    public static final String CLICK_HUIORDER_REPORT_CHART_SCAN = "click_huiorder_report_chart_scan"; // 点击 惠买单-报表-扫码绩效
    public static final String CLICK_HUIORDER_REPORT_CHART_TIP = "click_huiorder_report_chart_tip"; // 点击 惠买单-报表-打赏
    public static final String CLICK_HUIORDER_RECHARGE = "click_huiorder_recharge"; // 点击 惠买单-充值
    public static final String CLICK_HUIORDER_UNION = "click_huiorder_union"; // 点击 惠买单-银联收款
    public static final String CLICK_HUIORDER_JOIN = "click_huiorder_join"; // 点击 惠买单-加入惠买单
    public static final String CLICK_HUIORDER_KNOW = "click_huiorder_know"; // 点击 惠买单-了解惠买单

    // 会员管理
    public static final String CLICK_MEMBER_MANAGE_MEMBER_DETAIL = "click_member_manage_member_detail"; // 点击 会员管理-会员资料
    public static final String CLICK_MEMBER_MANAGE_BIRTHDAY = "click_member_manage_birthday"; // 点击 会员管理-生日提醒
    public static final String CLICK_MEMBER_MANAGE_PUSH_MESSAGE = "click_member_manage_push_message"; // 点击 会员管理-推送消息

    // 优惠券
    public static final String CLICK_COUPON_CREATE = "click_coupon_create"; // 点击 优惠券-制券
    public static final String CLICK_COUPON_NO_VISIBLE = "click_coupon_no_visible"; // 点击 优惠券-未公开
    public static final String CLICK_COUPON_VISIBLE = "click_coupon_visible"; // 点击 优惠券-已公开

    // 储值卡
    public static final String CLICK_VALUE_CARD_CONSUME = "click_value_card_consume"; // 点击 储值卡-消费
    public static final String CLICK_VALUE_CARD_CREATE = "click_value_card_create"; // 点击 储值卡-制卡
    public static final String CLICK_VALUE_CARD_RECHARGE = "click_value_card_recharge"; // 点击 储值卡-充值
    public static final String CLICK_VALUE_CARD_PROVIDE = "click_value_card_provide"; // 点击 储值卡-发卡
    public static final String CLICK_VALUE_CARD_LOCK = "click_value_card_lock"; // 点击 储值卡-锁卡
    public static final String CLICK_VALUE_CARD_WEEK_RECHARGE = "click_value_card_week_recharge"; // 点击 储值卡-报表-星期充值率
    public static final String CLICK_VALUE_CARD_TIME_RECHARGE = "click_value_card_time_recharge"; // 点击 储值卡-报表-时段充值率
    public static final String CLICK_VALUE_CARD_DAY_RECHARGE = "click_value_card_day_recharge"; // 点击 储值卡-报表-日期充值率
    public static final String CLICK_VALUE_CARD_WEEK_CONSUME = "click_value_card_week_consume"; // 点击 储值卡-报表-星期消费率
    public static final String CLICK_VALUE_CARD_TIME_CONSUME = "click_value_card_time_consume"; // 点击 储值卡-报表-时段消费率
    public static final String CLICK_VALUE_CARD_DAY_CONSUME = "click_value_card_day_consume"; // 点击 储值卡-报表-日期消费率
    public static final String CLICK_VALUE_CARD_MEMBER_BALANCE = "click_value_card_member_balance"; // 点击 储值卡-报表-会员余额报表
    public static final String CLICK_VALUE_CARD_TRANSACTION_FLOW = "click_value_card_transaction_flow"; // 点击 储值卡-报表-交易流水
    public static final String CLICK_VALUE_CARD_ALL = "click_value_card_all"; // 点击 储值卡-报表-储值卡汇总报表
    public static final String CLICK_VALUE_CARD_ACTIVATE = "click_value_card_activate"; // 点击 储值卡-激活
    public static final String CLICK_VALUE_CARD_MANAGE = "click_value_card_manage"; // 点击 储值卡-卡管理

    // 设置
    public static final String CLICK_SET_LOCATION_MERCHANT = "click_set_location_merchant"; // 点击 设置-定位商铺
    public static final String CLICK_SET_MY_MERCHANT = "click_set_my_merchant"; // 点击 设置-我的商铺
    public static final String CLICK_SET_MY_QR_CODE = "click_set_my_qr_code"; // 点击 设置-我的二维码
    public static final String CLICK_SET_MEMBER_COMMENT = "click_set_member_comment"; // 点击 设置-会员评论
    public static final String CLICK_SET_EMPLOYEE_MANAGE = "click_set_employee_manage"; // 点击 设置-员工管理
    public static final String CLICK_SET_COMMIT_SUGGEST = "click_set_commit_suggest"; // 点击 设置-意见反馈
    public static final String CLICK_SET_ABOUT_US = "click_set_about_us"; // 点击 设置-关于我们
    public static final String CLICK_SET_PUSH_SET = "click_set_push_set"; // 点击 设置-推送设置
    public static final String CLICK_SET_LOGIN_OUT = "click_set_login_out"; // 点击 设置-退出

    // 其他
    public static final String CLICK_LOGIN_IN = "click_login_in"; // 点击 登录
    public static final String CLICK_REGISTER = "click_register"; // 点击 注册
    public static final String CLICK_FORGET_PASSWORD = "click_forget_password"; // 点击 忘记密码

    /** App 渠道 */
    public static final String CHANNEL = "android"; // 当前渠道为：android

    /** token_id */
    public static final String TOKEN_ID = ""; // 当前请求的 token_id

    /** 请求需要加密的字段 */
    public static final JSONArray ENCRYPTION_FIELD = new JSONArray(); // 请求中的加密字段




}
