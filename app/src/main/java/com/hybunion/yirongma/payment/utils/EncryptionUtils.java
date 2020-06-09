package com.hybunion.yirongma.payment.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * base64字符串加密解密
 * @author gulin
 * @date 2018/12/27 0027
 */
public class EncryptionUtils {
        private static final String charset = "utf-8";
        /**
         * 解密
         *
         * @param data
         * @return
         * @author jqlin
         */
        public static String decode(String data) {
                try {
                        if (null == data) {
                                return null;
                        }

                        return new String(Base64.decodeBase64(data.getBytes(charset)), charset);
                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                }

                return null;
        }

        /**
         * 加密
         *
         * @param data
         * @return
         * @author jqlin
         */
        public static String encode(String data) {
                try {
                        if (null == data) {
                                return null;
                        }
                        return new String(Base64.encodeBase64(data.getBytes(charset)), charset);
                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                }

                return null;
        }

        /**
         * Base 64编码转图片
         * @param string
         * @return
         */
        public static Bitmap stringtoBitmap(String string) {
                //将字符串转换成Bitmap类型
                Bitmap bitmap = null;
                try {
                        byte[] bitmapArray;
                        bitmapArray = android.util.Base64.decode(string, android.util.Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return bitmap;
        }
}
