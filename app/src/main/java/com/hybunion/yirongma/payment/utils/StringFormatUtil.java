package com.hybunion.yirongma.payment.utils;

/**
 * @author SunBingbing
 * @date 2017/2/27
 * @email freemars@yeah.net
 * @description 用于去除 String 字符串中的“-”
 */

public class StringFormatUtil {

    /**
     * 格式化到指定的字符串格式
     * @param originStr 原始字符串
     * @return 去掉“-”后的新串
     */
    public static String getString(String originStr){
        // 对原始字符串进行非空判断
        if (null != originStr && !"".equals(originStr)){
            return originStr.replaceAll("-","");
        }else {
            return "input a null or no content string";
        }
    }
}
