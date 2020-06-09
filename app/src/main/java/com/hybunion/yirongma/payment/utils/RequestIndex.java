package com.hybunion.yirongma.payment.utils;

/**
 * 网络请求的标识
 * Created by king on 2016/7/6.
 */

public enum RequestIndex {
    ERROR,//错误回调
    PARAMS_TEST,//测试

    USER_LOGIN, //登录
    NOTICE, //公告

    PERSONAL_MERCHANT,//个体商户
    PERSONAL_MERCHANT_MODIFY,//修改个体商户

    COMPANY_MERCHANT,//企业商户
    COMPANY_MERCHANT_MODIFY,//修改企业商户

    FAVORABLE_MERCHANT,//优惠商户
    FAVORABLE_MERCHANT_MODIFY,//修改优惠商户

    DERATE_MERCHANT,//减免商户
    DERATE_MERCHANT_MODIFY,//修改减免商户

    GET_BANK_LIST,//获取银行名称列表
    GET_PROVINCE,//获取省
    GET_CITY,//获取市
    GET_MERCHANT_INFO,//行业类别

    ADD_MACHINE,//商户增机
    ADD_MECHINE_MID,//商户增机获取mid
    ADD_MECHINE_TID,//商户增机获取tid
    ADD_MERCHANT_GET_BMAID,//增机获取设备型号

    MERCHANT_MANGEMENT,//商户管理
    MERCHANT_MANGEMENT_WAIT,//商户管理待审批
    MERCHANT_MANGEMENT_OK,//商户管理已审批
    MERCHANT_MANGEMENT_MODIFY,//商户管理修改功能

    QUERY_TERMAIL_NUMBER,//查询终端号
    QUERY_MACHINE_NUMBER,//查询设备型号
    UPDATE_POS_INFO,
    FORGETPWD_GET_CODE,//忘记密码获取验证码
    FORGETPWD_COMMIT_CODE,//忘记密码,提交验证码进行校验
    RESET_PWD,
    REGISTERE,
    RESET_PHOTO,
    REGISTER_GET_CODE,
    CREDIT_CARD_LIST,
    BIND_RECEIPT_CODE, //绑定收款码
    GENERALREPINFOBEAN,//是否报单
    GET_INDUSTRY_LIST,//获取所属行业
    GET_BANK_INFO,//银行列表信息
    COMPANY_USER_PRESENTER,//企业用户
    BUSINESS_UER_PRESENTER,//个体工商用户
    PERSONAL_UER_PRESENTER,//个人用
    THE_DECLARATION_WAS_REJECTED,//报单被拒
    GET_USER_MESSAGE,//获取用户认证的详细信息
    BAODAN_LOGIN,//登录对账
    BANK_CARD_APPROVE_STATUS,//银行卡修改信息，审核状态
    COMMIT_MODIFY_BANKCARD,//提交修改的银行卡信息
    GET_APPROVE_MESSAGE,//获取审核的记录
    GET_BANKCARD_DETAIL,//获取银行卡号的详细信息
    MERCHANT_BASIC_INFO, //店铺基本资料
    SERACH_MERTER_BYMID,  //查询商户是否有设备
    QUERY_MERCHANT_MESSAGE,//查询商户信息
    QUERY_COLLECTION_CODE_LIST, //查询收款码列表
    NOTICE_DETAILS,//公告详情

    QUERY_WALLET_ASSET,//钱包相关信息
    WITHDRAW_CASH, //钱包提现相关
    QUERY_SING_INFO, //查询签约信息
    UPLOAD_SIGNATURE,//上传电子签名

    Query_CLERK_NAME,//查找店铺店员
    ADD_CLERK_NAME,//添加店员
    CHANGER_CLERK,//修改店员
    DELTTE_CLERK,//删除店员

    REALNAMEAUTHENTICATION,//实名认证

    NOCARDPAY_ADD_CARD,//添加
    NOCARDPAY_CARD_MANAGER,
    NOCARDPAY_REMOVE_CARD,//解绑
    SELECT_SERVICE,//服务
    NOCARDPAY_MAKE_ORDER,//下单

    MORENOTICE,//公告
    REALNAMEAUTH,     //实名认证
    GETBANK,     //获取银行卡信息

    VALUECARDLIST,//储值卡列表
    VERIFYINGCODE,//获取验证码
    VALUECARDCONSUMPTION,//储值卡消费
    GETREFUND,//申请退款
    CHECKCODE,//校验验证码
    PERSONALREALNAMEAUTH,//个人报单实名认证
    UPLODEBANDCARDIMG,//上传绑定扫码补充资料

    BRANDIMAGE,//品牌形象

    MAINMASSAGE,//主页消息
    READMESSAGE,//读取信息

    FEEDBACK,//意见反馈
    ADD_CLERK_DETAIL,//添加店员
    BIND,//绑定云喇叭
    QUERY_STORE,
    STORE_LIST,  // 获取门店列表
    TID_LIST,     // 获取收款码列表
    SOURCE_LIST,   // 获取收银台插件列表
    ADD_NEW_KUANTAI, // 添加新款台

    BOSS_STORE_LIST,  // 门店汇总
    CHANGE_PASSWORD,//修改密码

    WALLET_BALANCE,  // 代理“商”计划
    WALLET_NEW_BALANCE, // 钱包
    TIXIAN_RECORD, // 提现记录（代理“商”计划）
    TIXIAN_NEW_RECODE, // 提现记录（钱包）
    CHOOSE_BANK_CARD, // 提现银行卡列表
    QUERY_KUANTAI, //查询款台
    QUERY_DESK_TRANS_SUM,
    QUERY_SET_UP_SUM,
    FINISH_DAILY_DUTY,
    QUERY_DUTY_LISY,
    QUERY_DUTY_TIME,
    GET_ALL_BANK,  // 钱包-添加结算卡-开户行列表
    ADD_BANK_CARD, // 添加结算卡
    TIXIAN, // 提现（代理“商“计划）
    TIXIAN_NEW,  // 提现 （HRT钱包）
    CODE_URL,
    CHECK_CODE,
    UPDATE_REFUND_PASSWORD,
    OLD_REFUND_PASSWORD,
    ADD_SHOUYIN,//微收银插件添加
    FIX_AMOUNT,//固定收款码
    MIN_AMOUNT,//最低收款码
    KAITONG_SHISHI, //  t0 钱包 开通 实时到账
    QUERY_REFOUND_INFO,//退款明细
    REWARD_LIST,  // 代理商计划中奖励详情列表
    YOUHUIQUAN_LIST,  // 优惠券列表
    YOUHUIQUAN_DETAILS_DATA,  // 优惠券详情-券数据
    YOUHUIQUAN_DETAILS_INFO,  // 优惠券详情-券信息
    COUPON_DATA_SUMMERY,   // 优惠券数据汇总
    ADMIN_SETTING_LIST,  // 管理员列表
    ADD_MANAGER,         // 添加管理员
	MAKE_NOTES,//制券
    UPDOWM_COUPON,//上下线
    MEMBER_DETAILS,//会员详情
    MEMBER_COUNT,//会员人数
    HISTORY_MSG,//推送记录
    STORE_AND_COUPON,  // 加入商圈的门店及门店对应的优惠券
    PUSH_NUM,  // 查询推送人数
    PUSH_MSG,  // 小程序消息推送
    QUERY_STORE_LIST,//查询符合会员的门店
    ADD_MER_CARD,//新增储值卡
    QUERY_MER_CARD_INFO,
    SET_CARD_FLAG,
    GET_CARD_HISTORY_DATA,
    GET_CARD_ORDER_DATA,
    BILLING_LIST,  // 订单列表
    HUICHUZHI_LIST ,  // 惠储值 订单列表
    UPDATE_MER_CARD_INFO,//修改储值卡，
    HUICHUZHI_DETAIL,
    BEI_SAO_BOBAO,//插件被扫播报

    }