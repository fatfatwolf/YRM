package com.hybunion.yirongma.payment.utils;

/**存放sharedPreferences的key
 * Created by lcy on 2016/1/29.
 */
public class SharedPConstant {
    //记录请求次数,防止重放攻击
    public static final String transNo="transNo";
    //用户名
    public static final String LOGIN_NUMBER ="loginNumber";
    //用户密码
    public static final String PASSWORD="password";
    public static final String token="token";
    public static final String LOGIN_NAME="loginName";


    public static final String LOGINTYPE = "loginType";
    public static final String MERCHANT_NAME = "merchantName";
    public static final String UID="UID";//登录返回的UID
    public static final String ISAUTO_LOGIN="ISAUTO_LOGIN";//是否勾选自动登录
    public static final String IS_FIRST_IN="isFirstIn";//是否第一次进去
    public static final String JSON_LIST = "jsonList";//广告返回
    public final static String MID = "mid";
    public final static String STORE_ID = "storeId";

    public static final String MERCHANT_ID="merchantID";//老板获取商户信息
    public static final String SHOP_ID = "shopId";
    public static final String UUID="UUID";
    //代理商id
    public static final String agentId="agentId";
    // 登录返回数据，保存是否有钱包功能(代理“商“计划）
    public static final String has_wallet = "hasWallet";
    // 登录返回，是否是 HRT 钱包商户
    public static final String IS_HRT_WALLET = "isHRTWallet";
    // 是否需要唤醒屏幕
    public static final String NEED_WAKE_UP = "needWakeUp";
    //添加是否需要强制开启备注的开关
    public static final String REMARK_SWITCH = "remarkSwitch";
    //判断是否是有线耳机模式
    public static final String HEAD_SET = "headSet";
    //判断是否是蓝牙耳机模式
    public static final String HEAD_SET_BULETOOTH = "headSetBlue";
    //最低付款金额
    public static final String MIN_ACCOUNT = "minAmount";
    //是否开启强制备注,0是，1否,登录时候判断
    public static final String IS_FORCE_REMARK = "isForceRemark";
    // 用户 钱包 是否开通 实时到账
    public static final String IS_SHISHI = "isShiShi";
    //入账卡号
    public final static String BANK_ACCNO = "bank_accno";
    //开户行
    public final static String BANK_BRANCH = "bank_branch";
    // 播报速度
    public final static String VOICE_SPEED = "voice_speed";
    // 播报之前的音量保存
    public final static String CUR_VOL = "current_volume";

    public final static String IS_READ_PROTOCOL = "IsReadProtocol";
    // 当前悬浮窗是否显示
    public final static String FLOAT_IS_SHOW = "float_is_show";

    // 极光注册是否成功
    public final static String JPUSH_IS_REGISTE = "jpush_is_registe";
    // 华为推送是否注册成功
    public final static String HUAWEI_IS_REGISTE = "huawei_is_registe";
    // 订单列表实时刷新是否开启  0-关闭  1-开启
    public static final String REFRESH_IS_OPEN = "refresh_is_open";
    // 华为 Token
    public static final String HUAWEI_TOKEN = "huawei_token";
    // 是否展示优惠券入口
    public static final String IS_COUPON = "is_coupon";

    //不可用时间段的新字段 ,制券用
    public static final String SAVE_DAY_list = "saveDayList";
    public static final String SAVE_TIME_LIST = "saveTimeList";
    public static final String SAVE_TIME_LIST2 = "saveTimeList2";
    //不可用时间段的新字段 ,券信息适用
    public static final String SAVE_DAY_list2 = "saveDayList1";
    public static final String SAVE_TIME_LIST3 = "saveTimeLis3";
    public static final String SAVE_TIME_LIST4 = "saveTimeLis4";

    //点亮高德 使用 等于radius字段
    public static final String ACCUARY = "accuracy";
    public static final String ADCODE = "adCode";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";


    public static final String VC_SALE = "vcSale";
    public static final String YX_BSBB = "yxBeiSaoBoBao";

    public static final String IS_SHOW_LOANS = "isShowLoans";

    public static final String RE_PASSWORD = "rePassword";

    public static final String BANK_CARD_FACE_FILE = "bankCardFaceFile";

	 public static final String TOKEN = "token";
}
