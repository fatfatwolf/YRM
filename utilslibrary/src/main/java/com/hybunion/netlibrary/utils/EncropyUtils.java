package com.hybunion.netlibrary.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncropyUtils {
    private static final String charset = "utf-8";
//    static String mypublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCt8gwr42wV+O8EVRtEBO/iHmH2USzbeVtl9JKEw/j0f3LvK3QM4mC/S" +
//            "XLKBpjjAKJCPxFR/nRGEcvhcJ5hhSbdL4TtkkS25+h" +
//            "u5Az0duqytNoxarq8yGRhWWtgq4VmjFC3HMpOKi" +
//            "huobH8ugJei9WrgLHZfTqlh1bsW824xDuD3wIDAQAB";
    private static String mypublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCe1sGtF4nn2x8xFbkRI6TYV/g5yfBV+nkfOCR6SKCr+hYo/" +
        "eEtQJoFYZEMfKUvSQt1wy6hrCkHgN330aRivZSrXZUbqvddRBtO8/mhnBq9rlsI7N7G+M1BS2zRUfZ0+LsAZSjMWHm76tppV0szMpErWojN07gwM+X+uApCXWGiNQIDAQAB";

    public static String encrypt(String data,String MyPrivateKey) {
        String strs = null;
        try {
            byte[] bt = encryptByKey(data.getBytes(charset), MyPrivateKey);//先加密
            strs = Base64.encodeToString(bt,Base64.NO_WRAP);//再Base64转换
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    public static String decryptor(String data,String MyPrivateKey) throws Exception {  //对string进行BASE64转换
        byte[] strbyte = Base64.decode(data.getBytes("UTF-8"),Base64.NO_WRAP);//再Base64转换
        byte[] bt = decrypt(strbyte, MyPrivateKey);
        String str = new String(bt,"UTF-8");
        return str;
    }

    /**`
     * DES加密
     * @param datasource
     * @param key
     * @return
     */
    private static byte[] encryptByKey(byte[] datasource, String key) {
        try{
            SecureRandom random = new SecureRandom();

            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES 解密
     * @param src
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    public static String encryptWithBase64(String string) throws Exception {
        // http://commons.apache.org/proper/commons-codec/ :
        // org.apache.commons.codec.binary.Base64
        // sun.misc.BASE64Encoder

        byte[] binaryData = encrypt(getPublicKey(), string.getBytes());
//        LogUtil.d(" ----------------------公钥加密数据：\n" + hexConvert.);
        String base64String = Base64.encodeToString(binaryData,Base64.NO_WRAP);
//        LogUtil.d(" ----------------------加密数据编码后数据：\n" + base64String);

//		String base64String = new BASE64Encoder().encodeBuffer(binaryData);
        // org.apache.commons.codec.binary.Base64.encodeBase64(binaryData);
        return base64String;
    }

    /**
     * 私钥
     */
    private static RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private static RSAPublicKey publicKey;

    /**
     * 获取私钥
     *
     * @return 当前的私钥对象
     */
    public static RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     *
     * @return 当前的公钥对象
     */
    public static RSAPublicKey getPublicKey() {
        return publicKey;
    }


    /**
     * 从字符串中加载公钥
     *
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey() {
        try {
            byte[] buffer = Base64.decode(mypublicKey,Base64.NO_WRAP);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {

        }
    }


    public static void loadPrivateKey(){
        try {
            byte[] buffer = Base64.decode(CommonUtils.createRandom(false,16),Base64.NO_WRAP);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {

        }
    }

    /**
     * 加密过程
     *
     * @param publicKey     公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //            cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     *
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static byte[] decrypt2(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");// , new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    public static String decryptWithBase64(String base64String) {
        byte[] binaryData = new byte[0];
        try {
            binaryData = decrypt2(getPrivateKey(),Base64.decode(base64String,Base64.NO_WRAP));
//			binaryData = decrypt(getPrivateKey(), new BASE64Decoder().decodeBuffer(base64String));
        } catch (Exception e) {
        }
        String string = new String(binaryData);
        return string;
    }
}
