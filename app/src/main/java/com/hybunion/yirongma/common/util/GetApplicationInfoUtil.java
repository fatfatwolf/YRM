package com.hybunion.yirongma.common.util;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.payment.view.engine.ConstantField;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;

import org.json.JSONArray;

/**
 * @author SunBingbing
 * @date 2017/3/27
 * @email freemars@yeah.net
 * @description 获取应用相关信息的工具类
 */

public class GetApplicationInfoUtil {

    /**
     * 获取版本号
     * @return 当前版本的版本号
     */
    public static String getVersionNumber (){
        return HRTApplication.versionName;
    }

    /**
     * 获取代理商 Id
     * @return 代理商 Id(包含众维码)
     */
    public static String getAgentId (){
        return SharedPreferencesUtil.getInstance(HRTApplication.getInstance())
                .getKey(SharedPreferencesUtil.AGENT_ID);
    }

    /**
     * 获取商户 Id
     * @return 商户 Id
     */
    public static String getMerchantId (){
        return SharedPreferencesUtil.getInstance(HRTApplication.getInstance())
                .getKey(SharedPConstant.MERCHANT_ID);
    }

    /** 获取当前 App 渠道 */
    public static String getChannel (){
        return ConstantField.CHANNEL;
    }

    /** 获取当前 App 的 token_id */
    public static String getTokenId (){
        return ConstantField.TOKEN_ID;
    }

    /** 网络请求头中的加密字段 */
    public static JSONArray getEncryptionField (){
        return ConstantField.ENCRYPTION_FIELD;
    }
}





















