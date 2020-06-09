package com.hybunion.yirongma.payment.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hxs on 2016/4/15.
 */
public class IDNumberVerification {

    /**
     * 身份证验证算法
     */
    public static boolean isValid(String str) {
        List<String> itemNums = new ArrayList<String>();//存放18位身份证的数组

        /*
        将身份证前十七位数分别与下列十七位数相乘后在相加
         */
        int check_numbers[] = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
                10, 5, 8, 4, 2};

        /*
        将十七个相乘的数相加后与11取余，结果只可能十一种，下列对应着取余后0-9，x,相对应的数组
         */
        String checkStrings[] = new String[]{"1", "0", "x", "9", "8", "7",
                "6", "5", "4", "3", "2"};
        Map checkMap = new HashMap<String, String>();
        for (int i = 0; i <= 10; i++) {
            checkMap.put(String.valueOf(i), checkStrings[i]);//将每种可能放进相应数组
        }
        int sum = 0;//身份证前十七位和的数
        if (str.length() != 18) {
            return false;
        } else {

            try{
                for (int i = 0; i < str.length() - 1; i++) {
                    itemNums.add(str.substring(i, i + 1));//将身份证前十七位截取放进数组
                }

                for (int i = 0; i < itemNums.size(); i++) {
                    sum = sum
                            + (Integer.parseInt(itemNums.get(i)) * check_numbers[i]);//按照求和规则求和
                }

                String id_num_checkcode_18 = str.substring(17, 18);//截取地十八位校验位
                String sum_checkcode = String.valueOf(sum % 11);//求取余后的结果


                if(id_num_checkcode_18.equalsIgnoreCase("x")){
                    id_num_checkcode_18 = "x";
                }

                return checkMap.get(sum_checkcode) != null
                        && checkMap.get(sum_checkcode).equals(id_num_checkcode_18);
            }catch(Exception e){
                return false;
            }

        }

    }
}
