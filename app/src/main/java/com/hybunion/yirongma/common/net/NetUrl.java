package com.hybunion.yirongma.common.net;

import com.hybunion.yirongma.BuildConfig;

/**
 * 新接口使用
 */
public class NetUrl {
    public static final String HTTP_PROTOCOL = BuildConfig.HTTP_PROTOCOL;//主域名
    public static final String URL = HTTP_PROTOCOL + "JHAdminConsole";//主域名+项目名
    public static final String UNION_PAY_URL = BuildConfig.UNION_PAY + "WeChatConsole/"; //银联支付地址
    public static final String XPAY_URL = BuildConfig.XPAY_URL;//交易域名+项目名
    public static final String LOAN_URL = BuildConfig.LOAN_URL;//贷款地址
    public static final String YUFUKA = BuildConfig.YUFUKA;  // 预付卡相关接口的域名



    public final static String STORE_LIST = URL +"/queryStoreList.action";   // 公告

    public final static String QUERY_BANK_CARD =URL+ "/bank/getBankPayline.do";//请求的默认图标和名称

    public static final String QUERY_MERINFO = URL + "/merchant/queryMerInfo.do";

    public final static String GET_APPROVE_MESSAGE = URL + "/phone/phoneReceiptsUpload_queryMerchantTaskDetail2.action";

    public final static String COMMIT_MODIFY_BANKCARD_DATA = URL + "/phone/phoneReceiptsUpload_addMerchantTaskDetail2.action";//提交修改的信息

    /**
     * 查询商店列表
     */
    public static final String QUERY_STORE_LIST =URL + "/queryStoreList.action";
    /**
     * 门店开通
     */
    public final static String ADD_STORE_DETAIL = URL + "/addStoreDetail.action";

    public static final String QUERY_STORE_BIND_INFO = URL+"/queryStoreBindingInfo.action";


    public static final String QUERY_YUN_BY_STOREID = URL+"/yun/queryYunByStoreId.action";
    public final static String QUERY_MERCHENT_INFO = URL + "/phone/phoneMicroMerchantInfo_queryMicroMerchant.action";//默认持卡人和银行卡

    public static final String CHECK_TID = URL + "/lmfMer/checkTidRelation.do";

    // 添加新款台
    public final static String ADD_KUANTAI = URL + "/insertCashDevice.action";

    /**
     * 绑定云喇叭新街口
     */
    public final static String NEW_BIND = URL + "/yun/storeId/bind.do";

    /**
     * 众维码删除店员
     */
    public final static String DELETE_CLERK_DETAIL = URL + "/deleteClerkDetail.action";

    /**
     * 查看店员列表
     */
    public static final String QUERY_CLERK_LIST = URL + "/qureyClerkList.action";


    /**
     * 添加店员
     */
    public final static String YRM_ADD_CLERK_DETAIL = URL+ "/addClerkDetail.action";


    /**
     * 查询款台列表
     */
    public static final String QUERY_CASHIER_LIST = URL + "/queryCashierList.action";

    /**
     * 划拨店员
     */
    public static final String UPDATE_STORE_EMPLOYEE = URL + "/updateStoreIdOfEmployee.action";

    /**
     * 未读消息总数
     */
    public final static String UNREAFCOUNT = URL + "/merMessage/unreadCount.do";


    //班结列表
    public final static String QUERY_DUTY_LISY = URL + "/duty/queryDutyList.action";

    //班结汇总
    public final static String QUERY_SET_UP_SUM = URL + "/juhebill/querySetUpSum.do";

    //添加班结
    public final static String FINISH_DAILY_DUTY =URL + "/duty/finishDailyDuty.action";

    //获取班结时间
    public final static String QUERY_DUTY_TIME = URL + "/duty/queryDutyTime.action";

    public final static String NOTICEINFO = URL + "/annou/getAnnouncementInfo.do";   // 公告

    public final static String GET_MAIN_MESSAGE = URL + "/merMessage/getMerMessage.do";// 获取推送列表数据

    public final static String GET_READ_MESSAGE = URL + "/merMessage/updateUnreadType.do";

    public static final String CRASH_FEED_BACK = URL + "/inforFeedBack/crashFeedBack.do";

    //判断会员人数
    public final static String MEMBER_COUNT = HTTP_PROTOCOL+"memberApi/getMemberCount";

    //会员详情
    public final static String MEMBER_DETAIL = HTTP_PROTOCOL+"memberApi/getMemberInfo";

    // 会员，门店列表
    public final static String MEMBER_STORE_LIST = HTTP_PROTOCOL + "couponApi/queryStoreList";

    //制券
    public final static String MAKE_NOTES = HTTP_PROTOCOL + "couponApi/setCoupon";

    //查询惠储值历史数据
    public final static String GET_CARD_HISTORY_DATA = HTTP_PROTOCOL+"storedValueCardApi/getCardHistoryData";
    //查询惠储值数据
    public final static String GET_CARD_ORDER_DATA = HTTP_PROTOCOL+"storedValueCardApi/getCardOrderData";

    // 惠储值详情
    public final static String HUICHUZHI_DETAIL = HTTP_PROTOCOL + "juhebill/queryValueCardOrderInfo.do";

    //查询储值卡
    public final static String QUERY_MER_CARD_INFO = HTTP_PROTOCOL+"storedValueCardApi/queryMerCardInfo";

    // 开启关闭商户的储值卡充值
    public final static String SET_CARD_FLAG = HTTP_PROTOCOL+"storedValueCardApi/setCardFlag";
    //修改储值卡
    public final static String UPDATE_MER_CARD_INFO = HTTP_PROTOCOL+"storedValueCardApi/updateMerCardInfo";

    // 优惠券列表
    public final static String YOUHUIQUAN_LIST = HTTP_PROTOCOL+"couponApi/appMemtxnList";

    //上下线
    public final static String UPDOWN_COUPON = HTTP_PROTOCOL+"couponApi/upDownCoupon";

    // 优惠券详情页-券数据
    public static final String YOUHUIQUAN_DETAILS_DATA = HTTP_PROTOCOL+"couponApi/getCouponData";
    // 优惠券详情页-券信息
    public static final String YOUHUIQUAN_DETAILS_INFO = HTTP_PROTOCOL+"couponApi/getCouponInfo";

    public final static String QUERYBANKNAME = URL + "/bank/getBankPayline.do";

    // 添加结算卡
    public static final String ADD_BANK_CARD = URL + "/merWallet/insertMerWalletSettle.do";

    // 添加新款台
    public final static String ADD_NEW_KUANTAI = URL + "/addCashierDetail.action";

    //新增储值卡
    public final static String ADD_MER_CARD = HTTP_PROTOCOL +"storedValueCardApi/addMerCard";

    public static final String QUERY_DESK_TRANS_SUM = URL + "/juhebill/queryDeskYrmTransSum.do";

    //推送消息历史
    public final static String MSH_HISTORY = URL+"/couponApi/getPushHistory";

    // 提现银行卡列表
    public static final String CHOOSE_BANK_CARD = URL + "/merWallet/queryMerWalletSettleInfo.do";

    // 提现(代理“商“计划)
    public static final String TIXIAN = URL + "/merWallet/updateMerWalletBalance.do";
    public final static String NOTICE_DETAILS = URL + "/merchant/queryHYBNoticeDescription.do";
    public final static String QUERYALLBANKNAME = URL + "/bank/getIssuingBank.do";
    // 提现记录（代理“商”计划）
    public static final String TIXIAN_RECORD = URL + "/merWallet/queryMerWalletCashInfo.do";
    // 判断有没有可以推送的优惠券
    public final static String HAS_COUPON = HTTP_PROTOCOL + "couponApi/storeAndCoupon";
    // 查询推送人数
    public final static String PUSH_NUM = HTTP_PROTOCOL + "couponApi/getPushMember";
    // 小程序推送
    public final static String PUSH_MSG = HTTP_PROTOCOL + "couponApi/pushStoreCoupon";

    public final static String QUERY_CODE = URL + "/getMsgCode/getMsgCodeToDES.do"; //验证码

    public final static String QUERY_CHECK_CODE = URL + "/merchant/checkCode.do"; //校验验证码

    public final static String VALUE_CARD_CONSUMPTION = URL + "/payment/vcardOrder.do"; //储值卡消费


    //查询储值卡列表
    public final static String QUERY_VALUE_CARD = URL + "/vcConsumption/queryVcConsumption.do";

    // 代理商计划 中 奖励详情列表
    public final static String REWARD_LIST = URL+"/merWallet/queryMerWalletReward.do";

    public final static String QUERY_SING_INFO = URL + "/phone/phoneMerchantWallet_queryMerAdjustRate.action";//签约信息

    public static final String LOGIN_URL = URL + "/merchant/yrm/merlogin.do";
    public static final String CHECK_CODE_URL = URL + "/merchant/checkCode.do";
    public static final String RES_PASSWORD_URL = URL + "/merchant/yrm/resetPassword.do";

    /**
     * 检查是否需要更新
     */
    public static final String QUERY_APK_VERSION = URL + "/merchant/queryVersion.do";
    /**
     * 查询报表
     */
    public static final String REPORT_FORM = URL + "/valuecard/report/getWeeklyAmount.do";
    public static final String REPORT_TIME_FORM = URL + "/valuecard/report/getTimeAmount.do";

    //开机广告
    public static final String OPEN_ANIMATION = URL + "/phoneAd/queryAdUrl.do";

    /**
     *众维码弹公告
     */
    public static final String LMF_NOTICE = URL + "/annou/getPopupAnnounce.do";

    /**
     * 储值卡余额详细报表
     */
    public static final String TEABS_DETAIL = URL + "/member/balance/getTransDetail.do";
    /**
     * 用户权限
     */
    public static final String EMPLOYEEMENU = URL + "/merEmployee/queryEmployeeMenu.do";


    /**
     * 会员余额报表
     */

    public static final String MEMBER_BALANCE = URL + "/member/balance/getMemberBalanceNew.do";

    /**
     * 找回密码加密
     */
    public static final String FORGET_Encryption_CODE_URL = URL + "/merchant/getYrmCodeToDES.do";


    /**
     * 上传token
     */
    public static final String KEEP_DEVICE_TOKEN = URL + "/umeng/keepDeviceToken.do";
    /**
     * 批量售卡batchActivatedCard
     */
    public static final String BATCH_ACTIVATED_CARD = URL + "/merchant/valuecard/batchActivatedCard.do";
    /**
     * 商户申请制卡
     */
    public static final String NEWMAKECARD = URL + "/newValueCard/makeCard.do";

    /**
     * 卡管理储值卡信息查询
     */
    public static final String MERCARDINFO = URL + "/newValueCard/getMerCardInf.do";
    /**
     * 微信、支付宝扫码支付
     */
    public static final String PAY_SCAN = UNION_PAY_URL + "payment/scanBarcode.do";
    /**
     * 储值卡管理-储值卡下线
     */
    public static final String DISABLE_VALUE_CARD = URL + "/newvaluecard/disableVcCard.do";

    /**
     * 储值卡管理-上传规则
     */
    public static final String UPDATE_VALUE_CARD_RULE = URL + "/newValueCard/upIsDec.do";
    /**
     * 储值卡汇总报表
     */
    public static final String VALUE_CARD_SUMMARY = URL + "/newvaluecard/cardTotal.do";
    /**
     * 新储值卡账单明细
     * */
    public static final String QUERY_CZK_BILLING_DA = URL + "/vcTransFlow/getVCTransFlow.do";
    /**
     * 新众维码账单明细
     * */
    public static final String QUERY_LMF_NEW_BILLING_DA = URL + "/juhebill/queryYrmTransDetail.do";

    public final static String BANNER = URL + "/lmfPartner/adv/queryAdv.do";   // banner图

    public static final String KEEP_TOKEN = URL + "/vendor/keepToken.do";

    public final static String REMARK_UPDATE = URL + "/yrm/appsetting/update.action";   // 添加强制备注
    public final static String ISREADPROCTO = URL + "/merchant/yrm/updateIsReadProtocolType.do";   // 公告

    public final static String QUERY_COLLECTION_CODE_LIST = URL + "/phone/phoneMicroMerchantInfo_serachAggPayMerTerByMidNew.action";//收款码管理列表


    public final static String BANKCARD_APPROVE_STATUS = URL + "/phone/phoneReceiptsUpload_queryMerchantBankCard.action";
    public final static String WITHDRAW_RECORD_LIST = URL + "/phone/phoneMerchantWallet_queryCashWithDrawalListData.action";//提现记录

    public final static String QUERY_INFOR_URL = URL + "/payment/orderQuery.do";

    public static final String CHECK_REFUND =  XPAY_URL + "refund/apply.do";

    // 账单明细
    public final static String BILL_DETAIL = URL + "/juhebill/queryOrderInfo.do";

    public static final String CHANGE_PASSWORD_URL = URL + "/lmfMer/resetPassword.do";
    // 钱包余额
    public static final String WALLET_BALANCE = URL + "/merWallet/queryMerWalletBalance.do";
    // 提现记录 （钱包）
    public static final String TIXIAN_NEW_RECORD = URL + "/merWallet/queryCashWithDrawalListData.do";
    // HRT钱包提现
    public static final String TIXIAN_NEW = URL + "/merWallet/queryCashWithDrawal.do";

    public static final String UPDATE_REFUND_PASSWORD = URL + "/merchant/yrm/updateRefundPassword.do";

    public static final String QUERY_IS_REFUNDPWD = URL + "/merchant/yrm/queryIsRefoundPawd.do";

    public static final String QUERY_XIUCAN_INFO = URL + "/xiucan/queryXiuCanMerInfo.do";
    // 钱包 余额查询
    public static final String WALLET_NEW_BALANCE = URL + "/merWallet/queryHRTWalletBalance.do";

    /**
     * 设置固定收款码
     */
    public final static String FIX_AMOUNT = URL +"/yrm/appsetting/update.action";

    /**
     * 设置最低收款码
     */
    public final static String MIN_ACCOUNT =URL + "/yrm/appsetting/update.action";

    // t0钱包 开通 实时到账
    public final static String KAITONG_SHISHI = URL + "/merWallet/updateCashStatus.do";

    // 管理员列表
    public static final String ADMIN_SETTING_LIST = HTTP_PROTOCOL + "/couponApi/getListShareAdmin";
    // 添加管理员
    public static final String ADD_MANAGER = HTTP_PROTOCOL + "/couponApi/setShareRole";
    // 会员，门店列表
    // 订单列表
    public final static String BILLING_LIST = URL + "/juhebill/queryYrmTransDetail.do";
    // 惠储值列表

    // 插件被扫播报
    public final static String BEI_SAO_BOBAO = URL + "/merchantInfo/beisaoBoBao.do";

    // 订单详情
    public final static String BILLING_DETAILS = URL + "/juhebill/queryOrderInfo.do";

    // 结算--收款码 url
    public final static String SETTLEMENT_URL = URL + "/HrtAppHtml/settlement.html?mid=";
    // 结算--和卡 url
    public final static String SETTLEMENTJ_URL = YUFUKA + "h5/settle/settlementj.html?mid=";

    // 用户使用协议
    public final static String AGREEMENT = "http://www.hybunion.com/aggrement.html";

    // 隐私政策
    public final static String PERSONALMENT = "http://www.hybunion.com/personalment.html";

    // 数据汇总
    public static final String SUMMARY = URL + "/juhebill/queryDeskYrmTransSum.do";

    // 数据汇总-按照支付类型查询
    public static final String SUMMARY_PAY_TYPE = URL + "/juhebill/sumByTransType.action";

    // 惠储值列表
    public static final String HUICHUZHI_LIST = URL + "/juhebill/queryWiseStoreYrmTransDetail.do";

    // 银联交易查询
    public static final String YINLIAN_QUERY = UNION_PAY_URL + "payment/scan/orderQuery.do";

    // 被扫交易
    public static final String SCAN_PAY = XPAY_URL + "payment/unifiedOrder.do";

    // 商户入驻资料查询（是否开通和卡）
    public static final String QUERY_MER_INFO = YUFUKA + "open/api/POST/query-mer-register";

    public final static String QUERY_REFUND_INFO = URL + "/juhebill/queryReFoundInfo.do";//退款明细
    // 查询收款码列表
    public static final String QUERY_CODE_LIST = URL + "/juhebill/queryTidInfo.do";

    // 查询收款码插件列表
    public static final String QUERY_SOURCE_LIST = URL + "/juhebill/querySource.do";

    public final static String UPLODEBANDCODEIMG = URL + "/phone/phoneMicroMerchantInfo_updateLMFMerchantInfo.action"; //上传补充资料
    //绑定收款码
    public final static String BIND_RECEIPT_CODE = URL + "/phone/phoneMicroMerchantInfo_addAggPayMerchantTermianalInfo.action";
    //储值卡消费退款
    public final static String GETREFUND = XPAY_URL + "refund/vcConsumeApply.do";

    // H5 url
    public static final String green_plan_URL = URL+"/HrtAppHtml/loanProject/loanHMDUpgrade.html";
    public static final String use_help_yrm_URL = URL+"/HrtAppHtml/yrmVoiceHelp.html";
    public static final String use_help_lmf_URL = URL+"/HrtAppHtml/lmfVoiceHelp.html";
    public static final String voice_help_URL = URL+"/HrtAppHtml/voiceHelp.html";
    public static final String Ty_Protocol_URL = URL+"/HrtAppHtml/zwmTyProtocol.html";


}
