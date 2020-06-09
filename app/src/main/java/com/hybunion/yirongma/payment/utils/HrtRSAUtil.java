package com.hybunion.yirongma.payment.utils;

import com.hybunion.yirongma.payment.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * rsa对应ios加密解密工具类 from
 * :http://blog.csdn.net/showhilllee/article/details/50592003
 */
public class HrtRSAUtil {

    static {
//        String public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6PFSemrieexPE/ag0ApU\r" +
//                "WUhvspI6o0r80/T/y+Pnbn18tQBEOd4VRuvgyhMC57E+x/XnsuJEsJbw8v3OpdHj\r" +
//                "WG3v540JQMTu066YbkeRfQ4F3CX++4cerohxcg3MW2o63+dq3C4FcD2zoYuvX0qP\r" +
//                "/nr3Lts0vk7dRVx28X/SxPzKdd8T806aaFgz6NHFEHfWgL6lvwLTWQE+K9zf0+R6\r" +
//                "6knZvgqmhhQtH1M7sqmfs88q2pksy+7JNnorQoQouWiHYsGS0+fFPtv0XljdwaTy\r" +
//                "Pv1Knd3Isi9uaGWU9UIhaahZZqhiEZUN90R12ihQ1T9dGQIp5PQvBqs5ZWb5Anoz\r" +
//                "CQIDAQAB\r";
        String public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5CBWz24nvB3eFUZf4u48\n" +
                "Ttp0Frh2Zk5PAT0lOOCj+t5cV9IkRBREAjvszHA/M458KLICLemxq6MT78qfU5W6\n" +
                "0xFHjzbaK4n0ubukzq4p9NN7dJEBRAb0NhdLkj/3mumUhr7ROaM8GgC8y8P6ZbzU\n" +
                "8G6swrRhgydPe1nGTN1lVrGrc+WWbVXcllhnCeFegfXbHslKFtQROXHvkg0S6pet\n" +
                "34EVZ8TAwKmBj2Fvq3oddtMkZrX2ZN1i23UGBFB/JZqud4YtAQG9DBM44ZFuQrGt\n" +
                "2604H5xbMuysmmr4Ts0hafeunfu7Ws0wF+ODZ9+UqDPiRzG3Yar9sQF954sHjxhk\n" +
                "6QIDAQAB";

//        String private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDo8VJ6auJ57E8T\r" +
//                "9qDQClRZSG+ykjqjSvzT9P/L4+dufXy1AEQ53hVG6+DKEwLnsT7H9eey4kSwlvDy\r" +
//                "/c6l0eNYbe/njQlAxO7TrphuR5F9DgXcJf77hx6uiHFyDcxbajrf52rcLgVwPbOh\r" +
//                "i69fSo/+evcu2zS+Tt1FXHbxf9LE/Mp13xPzTppoWDPo0cUQd9aAvqW/AtNZAT4r\r" +
//                "3N/T5HrqSdm+CqaGFC0fUzuyqZ+zzyramSzL7sk2eitChCi5aIdiwZLT58U+2/Re\r" +
//                "WN3BpPI+/Uqd3ciyL25oZZT1QiFpqFlmqGIRlQ33RHXaKFDVP10ZAink9C8Gqzll\r" +
//                "ZvkCejMJAgMBAAECggEBAIgnVZAcHVgqWWZPx1sSTo8JGdCETCXZ1MGG+GSSR1l7\r" +
//                "m4KoLzirqEAV6wPx7MbEAPAbuVwDiSa2cwt0cm7VqU6so4byFrB26ylrack7p7wy\r" +
//                "kBZHVyxpo/Nb6QNDMWlVRChPOVSMybz7M51+6fiGuneCJCyND90Ud5ztGNC7Z9Se\r" +
//                "12h5FytJbjZg9aJxcdgEvobWvYPnx6gEcLduOONKj0YAwdoKLQ3PIpkD9oGoUjVL\r" +
//                "+PGyKHTig/o+JJT9kqnZv1ym94DQbbGD2btP1qpWiY+SuiVXPrW1pgAP6n5b6zG3\r" +
//                "eJZl+3iiOmQABcQIv4whXOJffuBBX8784YSXjd2L/AECgYEA/9EA2TEC8AhPDYCx\r" +
//                "WhSH+4Gui/Rypcf+Ko9HkOiPkYmiPlsi+2dDRYznsceaAaTW8jHuE4BiBDtJVMbx\r" +
//                "v5TZIf2SCn+ylnUn3z8uRbS/evI107Dk1ehqEvuO7HYShpIQIlsje7OvUrPTKUVF\r" +
//                "gW7S97nBCrmNgD5cUPGMr45LzcECgYEA6Rwd3iE0MrXMQRN1dtR9YotUxe2nXvtr\r" +
//                "I8kSkqKLOj0Isu6X5+jGnCjgwyCgaaqFxCYx6GUnGfv3F0mAbB0qfNYuGfCvQGQw\r" +
//                "+tzwwlpN8vmqyGsEtc5MtCaj+9h+wvgwoEbcUJG/DoF1Uuqt4iWUIGDXp77ajXjJ\r" +
//                "+q/3TTPMR0kCgYByjIIWvy2TkgTkBPZlYcAqTL1+Ce20ckNLh3ECYEC5aKnvxht5\r" +
//                "+5lR8XBmWPyLM+bT+Kukq944CtwhTBxAL0SzRbo5XUj4umkqPD5aFD+RrBeSPSma\r" +
//                "50FoUqCDHbPZ8lmrKKhQ4frly4QIfO5MsPVi3Bim1sOX41SvIhpfGhazgQKBgBYk\r" +
//                "To33125tqD4SLBkia9p9Y4r9XOV/uhiHE9WLzO+2NmpQkWs8yAizNU4zfikrQlN0\r" +
//                "UU9CtGkabsjueV1Lk+qRqYVbQDo8ig56CABd9YN7xYYN7D+cndqKxQ657Kh2TKAc\r" +
//                "uvaIMX6eO1ep5ORTL3O33yQW08mWTJEcP8A8iE3JAoGAedLmAgycu02MwAtaFZbv\r" +
//                "/yrE+BX976trG8xG5XbFVKQAv4ArSl9R2vID5/UF+lgcIcqZt1tGwx3+5itx1H3N\r" +
//                "Z0K/oUfNl/EIMua0EDVpep5AgsvboCXSGH7ApjSYzs8JoFU33kJ1xrVrnLth0Fi+\r" +
//                "o0IPjE+kt1ei2eZXJkIcrP8=\r";

        String private_key ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDkIFbPbie8Hd4V\n" +
                "Rl/i7jxO2nQWuHZmTk8BPSU44KP63lxX0iREFEQCO+zMcD8zjnwosgIt6bGroxPv\n" +
                "yp9TlbrTEUePNtorifS5u6TOrin003t0kQFEBvQ2F0uSP/ea6ZSGvtE5ozwaALzL\n" +
                "w/plvNTwbqzCtGGDJ097WcZM3WVWsatz5ZZtVdyWWGcJ4V6B9dseyUoW1BE5ce+S\n" +
                "DRLql63fgRVnxMDAqYGPYW+reh120yRmtfZk3WLbdQYEUH8lmq53hi0BAb0MEzjh\n" +
                "kW5Csa3brTgfnFsy7KyaavhOzSFp966d+7tazTAX44Nn35SoM+JHMbdhqv2xAX3n\n" +
                "iwePGGTpAgMBAAECggEBAL7d8+QP5Tz2vGaOgBGNhHXxPun3olCMuntt3mPNSEXQ\n" +
                "n/B2iRNQ5Wn9G24diDOpxmYCMeKaQaY6hqa9oDq97xdDwNypZzbAcTqpLgYjIEWQ\n" +
                "YB2yaR2yt8c+DGOsE43QWw22P7X6xoF3Fn9iVHGk4YDdCLzLUT77t/ubrFeJcY1q\n" +
                "+3+2JcdS6XKu5B/xkjX7Jm925MRyi9IMk/jnuf7K1F/FsHtaDYq86KsCx7OudC77\n" +
                "mBw7bjR3Ui8NkbGF+Q5SrtHLku2lgtBI58IdP7qVVZbk70bh1CbS+PVnI1iV1x+E\n" +
                "BHwp+CmEKi57uEVJRTePbd+7a92ZIY65DdHlu8EO7IECgYEA9FjNzjSd1R8JbISk\n" +
                "9+eOnBUC1S/vRSG6ySMW7vgxp0hG+LmCiYxdY8nuNT59yk1lYVfLlqGx1Hj50UVV\n" +
                "KS8c8mT0cNCXQbyZeiT5UU5Ya3WpPlwUBLC/1uEFJODsc2+Paa/iEP63o8nNgyQN\n" +
                "+QAorkdF9BjyiK32R2VSoGfhE3ECgYEA7wGAKI70zY/RVbmFuh1ZlDYiyMFXfOAr\n" +
                "d3AsDiaedxN65N7gUCQSSG83e+Ns+1KNSONSNrco5Fkd8eFSYHFYWMydsWhz2rUX\n" +
                "cxlBF/Ibfcr7x/7/YX2u6T8+sG3ZSSQ12z0ckC9A6s+Yw4P26KT5MG1n35JeoB+g\n" +
                "pZZNdeN+PPkCgYARVkOz/9O7DWqzwm2aEVnzBSKgUbheZVhFcTHtXrrXLa+rci4q\n" +
                "6ESwEeHO8VIninpI/9u4IlzlwHepO4whUfBx2mvlnUc+KM2S9xChjjeD2GNbw1jA\n" +
                "RHI4ItsklLjyL9YzH4GgxxHskyL7zREYu8QnKXNzMFn1DLLOuge0zeoQYQKBgQCA\n" +
                "6thMCEA3mPmbOcTODQLClSjSDKe6LdiERjcdMuuK4zEIvsdTswFBnOvaoWOpdG/M\n" +
                "/wDbjpQ6B2/Fhno+lyTGUShGB0dmkwOhC+A2oFr/0TtaN0tdNMPiAEE6PSZU83kx\n" +
                "VpdPlo13mp6V51qJRmM7IehDidoMmMK4TohdrGXtUQKBgQDCDhb/odaZciPtbGJq\n" +
                "nk9ZLz7DyNBIXio3wRjVYfFzHeGa55ptK1g8bcsRSs6+bQUuCVFXaCfK43QlEAJ2\n" +
                "esogskY2NjtY6NQHpRj/3+/y4Hv0ECy26gm8J6qbcKYi3UHU7Gp/IQpu2k96jQC9\n" +
                "KCYEDvyfu+4Rrr7IdCm7mg6Tsg==";
        try {
            loadPublicKey(public_key);
            loadPrivateKey(private_key);
        } catch (Exception e) {
            System.out.println("初始化错误");
        }

    }

    private HrtRSAUtil() {
    }

    public static String decryptWithBase64(String base64String) {
        byte[] binaryData = new byte[0];
        try {
            binaryData = decrypt(getPrivateKey(), Base64.decode(base64String));
//			binaryData = decrypt(getPrivateKey(), new BASE64Decoder().decodeBuffer(base64String));
        } catch (Exception e) {
        }
        String string = new String(binaryData);
        return string;
    }

    public static String encryptWithBase64(String string) throws Exception {
        // http://commons.apache.org/proper/commons-codec/ :
        // org.apache.commons.codec.binary.Base64
        // sun.misc.BASE64Encoder

        byte[] binaryData = encrypt(getPublicKey(), string.getBytes());
//        LogUtil.d(" ----------------------公钥加密数据：\n" + hexConvert.);
        String base64String = Base64.encode(binaryData);
        LogUtil.d(" ----------------------加密数据编码后数据：\n" + base64String);

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
     * 从文件中输入流中加载公钥
     *
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public void loadPublicKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        }
//		catch (IOException e) {
//			throw new Exception("公钥数据内容读取错误");
//		}
        catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     * <p>
     * 私钥文件名
     *
     * @return 是否成功
     * @throws Exception
     */
    public void loadPrivateKey(InputStream in) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                if (readLine.charAt(0) == '-') {
                    continue;
                } else {
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public static void loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
//			BASE64Decoder base64Decoder = new BASE64Decoder();
//			byte[] buffer = base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("私钥非法");
        }
//		catch (IOException e) {
//			throw new Exception("私钥数据内容读取错误");
//		}
        catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
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
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
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

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i < data.length - 1) {
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        try {
            String test = "JAVA";
            String testRSAEnWith64 = HrtRSAUtil.encryptWithBase64(test);
            String testRSADeWith64 = HrtRSAUtil.decryptWithBase64(testRSAEnWith64);
            System.out.println("\nEncrypt: \n" + testRSAEnWith64);
            System.out.println("\nDecrypt: \n" + testRSADeWith64);

            // 请粘贴来自IOS端加密后的字符串
            String rsaBase46StringFromIOS = "FXjA937HoEdCsTj85ie+M+awBZ2CEeX3Tbn04NYhJMJvSmneLA2VpBQ00rN6IktnS+M+d2JlanAiiqmrtlt2te+VwNM9IWnh+N0STH0F3zCyRU3ZzO89lac7nhYpaT0VPikaZ5R8CjKHVIbpad7GRI+JPxzpDkVqcWc+18y9594=";

            String decryptStringFromIOS = HrtRSAUtil.decryptWithBase64(rsaBase46StringFromIOS);
            System.out.println("Decrypt result from ios client: \n" + decryptStringFromIOS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}