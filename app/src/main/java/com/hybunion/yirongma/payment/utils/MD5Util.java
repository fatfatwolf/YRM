package com.hybunion.yirongma.payment.utils;

/**
 * Created by xjz on 2019/10/31.
 */
/**
 *@Description: 将字符串转化为MD5
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static String[] hex = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 输出为大写 String类型
     * @param msg
     * @return
     */
    public static String md5(String msg) {

        try {
            byte[] data = msg.getBytes("utf-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] encryptData = md.digest();
            return byte2Hex(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输出为小写String类型
     * @param msg
     * @return
     */
    public static String smallMd5(String msg) {

        try {
            byte[] data = msg.getBytes("utf-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] encryptData = md.digest();
            return byteHex(encryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输出为byte类型
     * @param data
     * @return
     */
    public static byte[] md5(byte[] data) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 输出得为大写
     * @param data
     * @return
     */
    public static String byte2Hex(byte[] data) {

        StringBuilder builder = new StringBuilder();
        int tmp;
        for (int i = 0; i < data.length; i++) {

            tmp = (data[i] & 0xF0) >> 4;
            builder.append(hex[tmp]);
            tmp = data[i] & 0x0F;
            builder.append(hex[tmp]);
        }
        return builder.toString().toUpperCase();
    }

    /**
     * 输出得为小写
     * @param data
     * @return
     */
    public static String byteHex(byte[] data) {

        StringBuilder builder = new StringBuilder();
        int tmp;
        for (int i = 0; i < data.length; i++) {

            tmp = (data[i] & 0xF0) >> 4;
            builder.append(hex[tmp]);
            tmp = data[i] & 0x0F;
            builder.append(hex[tmp]);
        }
        return builder.toString().toLowerCase();
    }


    public static byte[] hex2Byte(String data) {

        char[] ch = data.toCharArray();
        if (ch.length % 2 != 0) return null;

        byte[] dd = new byte[ch.length / 2];
        for (int i = 0; i < ch.length - 1; i = i++) {
            int hi = Character.digit(ch[i++], 16);
            int lo = Character.digit(ch[i], 16);
            dd[i / 2] = (byte) ((hi << 4) + lo);
        }
        return dd;
    }

    private static byte[] initKey(String key) {

        if (key == null || key.length() == 0)
            return null;
        byte[] b_key = key.getBytes();
        byte[] state = new byte[256];
        for (int i = 0; i < 256; i++)
            state[i] = (byte) i;
        int index1 = 0;
        int index2 = 0;

        for (int i = 0; i < 256; i++) {

            index2 = ((b_key[index1] & 0xFF) + (state[i] & 0xFF) + index2) & 0xFF;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    public static byte[] RC4(byte[] data, String key) {

        int x = 0, y = 0;
        byte b_key[] = initKey(key);
        if (b_key == null)
            return null;
        int xorIndex;
        byte[] result = new byte[data.length];

        for (int i = 0; i < data.length; i++) {

            x = (x + 1) & 0xFF;
            y = ((b_key[x] & 0xFF) + y) & 0xFF;
            byte tmp = b_key[x];
            b_key[y] = tmp;
            xorIndex = ((b_key[x] & 0xFF) + (b_key[y] & 0xFF)) & 0xFF;
            result[i] = (byte) (data[i] ^ b_key[xorIndex]);
        }
        return result;
    }



}
