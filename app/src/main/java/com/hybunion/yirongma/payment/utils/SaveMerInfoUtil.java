package com.hybunion.yirongma.payment.utils;

import android.content.Context;

import com.hybunion.yirongma.payment.bean.QueryMerInfoNoneBean;
import com.hybunion.yirongma.payment.utils.Constants;

/**
 * 保存商户信息工具类
 */

public class SaveMerInfoUtil {

    public static void saveAll(Context context, QueryMerInfoNoneBean bean) {
        if (bean == null)
            return;
        String loginType = bean.getLoginType();
        SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.has_wallet, bean.getBusinessType()); // 是否是钱包商户(代理“商”计划）
        SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_HRT_WALLET, bean.getIsHRTWallet()); // 是否是 HRT 钱包商户
        SharedPreferencesUtil.getInstance(context).putKey("loginType", loginType);
        SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_COUPON,bean.getIsCoupon());  // 是否加入商圈
        if ("0".equals(loginType)) {

            SharedPreferencesUtil.getInstance(context).putKey("storeId", ""); // 老板没有 storeId，将之前的覆盖掉
            SharedPreferencesUtil.getInstance(context).putKey("storeName", ""); // storeName，将之前的覆盖掉
            SharedPreferencesUtil.getInstance(context).putKey(Constants.Name, bean.getBankAccName());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.REFRESH_IS_OPEN, "1"); // 是否开启订单列表实时刷新，默认开启
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.MERCHANT_ID, bean.getMerchantID());
            SharedPreferencesUtil.getInstance(context).putKey("merchantName", bean.getMerchantName());//账户名称
            SharedPreferencesUtil.getInstance(context).putKey("rePassword", bean.getRePassword());
            SharedPreferencesUtil.getInstance(context).putKey("contactPerson", bean.getContactPerson());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.MIN_ACCOUNT, bean.getMinAmount());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_FORCE_REMARK, bean.getIsForceRemark());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.MIN_ACCOUNT, bean.getMinAmount());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.BANK_ACCNO, bean.getBankAccNo());  //结算账户
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.BANK_BRANCH, bean.getBankBranch());  //结算账户银行
            SharedPreferencesUtil.getInstance(context).putKey(Constants.LEGAL_NUM, bean.getLegalNum());  //身份证号
            SharedUtil.getInstance(context).putString(Constants.LEGAL_NUM, bean.getLegalNum());
            SharedPreferencesUtil.getInstance(context).putKey(Constants.MERCHANT_NAME, bean.getRname());  //注册店名
            SharedPreferencesUtil.getInstance(context).putKey(Constants.CONTACT_PHONE, bean.getContactPhone());  //联系电话
            SharedPreferencesUtil.getInstance(context).putKey(Constants.ADDR, bean.getRaddr());  //注册地址
            SharedPreferencesUtil.getInstance(context).putKey(Constants.BNO, bean.getBno());  //营业执照号
            SharedPreferencesUtil.getInstance(context).putKey(Constants.RNAME, bean.getRname());  //收款码名称
            SharedPreferencesUtil.getInstance(context).putKey(Constants.ACCNUM, bean.getAccNum()); //个人入账身份证号码
            SharedUtil.getInstance(context).putString(Constants.MID, bean.getJhmid());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_READ_PROTOCOL, bean.getIsReadProtocol());
            SharedPreferencesUtil.getInstance(context).putKey("mid", bean.getJhmid());
            SharedUtil.getInstance(context).putString("agentId", bean.getAgentId());
            SharedPreferencesUtil.getInstance(context).putKey(Constants.AREATYPE, bean.getAreaType());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.VC_SALE, bean.vcSale);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.YX_BSBB,bean.yxBeiSaoBoBao);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_SHOW_LOANS,bean.isShowLoans);


        } else {

            SharedPreferencesUtil.getInstance(context).putKey("storeId", bean.getStoreId());
            SharedPreferencesUtil.getInstance(context).putKey("storeName", bean.getStoreName());
            SharedPreferencesUtil.getInstance(context).putKey("empName", bean.getEmpName());
            SharedPreferencesUtil.getInstance(context).putKey("shopId", bean.getMerchantID());
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.MERCHANT_ID, bean.getMerchantID());
            SharedPreferencesUtil.getInstance(context).putKey("mid", bean.getJhmid());
            SharedPreferencesUtil.getInstance(context).putKey("jhMid", bean.getJhmid());
            SharedPreferencesUtil.getInstance(context).putKey("staffId", bean.getStaffId());
            SharedPreferencesUtil.getInstance(context).putKey("staffType", bean.getStaffType());
            SharedPreferencesUtil.getInstance(context).putKey("couponAdmin", bean.couponAdmin);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.VC_SALE, bean.vcSale);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.YX_BSBB,bean.yxBeiSaoBoBao);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.IS_SHOW_LOANS,bean.isShowLoans);
        }


    }


}
