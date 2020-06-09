package com.hybunion.yirongma.payment.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by android on 2016/12/13.
 */

public class FormatUtil {

    //身份证号中间为*
    public static String formatIDCard(String id) {
        if (TextUtils.isEmpty(id)) {
            return " ";
        }
        return (id.substring(0, 4) + "****" + id.substring(id.length() - 4, id.length())).toUpperCase();
    }

    //姓名
    public static String formatName(String name) {
        if (TextUtils.isEmpty(name)) {
            return " ";
        }
        return name.substring(0, 1) + "**";
        //return name.replace(name.substring(0, 1), "**");
    }


    //银行卡号显示前4位，后4位，其它为*
    public static String formatBankCard(String card) {
        if (TextUtils.isEmpty(card)) {
            return " ";
        }
        if (card.length() < 10) {
            return (card.substring(0, 2) + "****" + card.substring(card.length() - 2, card.length())).toUpperCase();
        }
        return (card.substring(0, 4) + "****" + card.substring(card.length() - 4, card.length())).toUpperCase();
    }

    public static String formatPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return " ";
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
    }

    public static String formatDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
