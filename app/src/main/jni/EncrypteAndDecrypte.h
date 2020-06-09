////
////  EncrypteAndDecrypte.h
////  merchant
////
////  Created by HRT on 15/6/2.
////  Copyright (c) 2015年 Hrtpayment. All rights reserved.
////
//
//#ifndef merchant_EncrypteAndDecrypte_h
//#define merchant_EncrypteAndDecrypte_h
///******************DES加密****************************/
//typedef unsigned char uint8;
//typedef unsigned char BOOLEAN;
//
//
//#define TRUE	1
//#define FALSE	0
//
////读取缓冲区的指定位.
//#define GET_BIT(p_array, bit_index)  \
//((p_array[(bit_index) >> 3] >> (7 - ((bit_index) & 0x07))) & 0x01)
//
////设置缓冲区的指定位.
//#define SET_BIT(p_array, bit_index, bit_val) \
//if (1 == (bit_val)) \
//{\
//p_array[(bit_index) >> 3] |= 0x01 << (7 - ((bit_index) & 0x07));\
//}\
//else\
//{\
//p_array[(bit_index) >> 3] &= ~(0x01 << (7 - ((bit_index) & 0x07)));\
//}
//
////加解密标识，这两个标识涉及到对表的读取位置,
////必须保证DES_ENCRYPT = 0 DES_DECRYPT = 1
//typedef enum
//{
//     DES_ENCRYPT = 0,
//     DES_DECRYPT = 1
//}DES_MODE;
//
///**
// *  @brief  DES加解密
// *  根据标准的DES加密算法用输入的64位密钥对64位密文进行加/解密
// 并将加/解密结果存储到p_output里
// *  @param p_data   输入, 加密时输入明文, 解密时输入密文, 64位(8字节)
// *  @param p_key    输入, 密钥, 64位(8字节)
// *  @param p_output 输出, 加密时输出密文, 解密时输入明文, 64位(8字节)
// *  @param mode     0 加密  1 解密
// */
//void des(uint8 * p_data, uint8 * p_key, uint8 * p_output, DES_MODE mode);
//
////异或
//void XOR(uint8 *input1,uint8 *input2,uint8 *output,uint8 len);
//
//
///**
// *  @brief  3DES加解密
// *  根据标准的3DES加密算法用输入的64位密钥对64位密文进行加/解密
// 并将加/解密结果存储到pRetData里
// *  @param sMacKey  输入密钥
// *  @param pInData  加密的数据(字节数要为16的倍数，否者计算的数据有误)
// *  @param pRetData 输出加解密的数据
// *  @param flag     0 加密  1 解密
// */
//void MacArithmetic(char *sMacKey,char *pInData,unsigned char*pRetData,int flag);
///**************************end************************/
//
//
///**
// *  @brief  主密钥对密文进行解密
// *
// *  @param masterKey 传人主密钥
// *  @param miKey     传入密文
// *  @param minKey    得到明文
// */
//void JiePlaintext(char *masterKey,char *miKey,char *minKey);
//
///**
// *  @brief  主对磁道信息进行解密(BB音频)
// *
// *  @param main_key      传人主密钥
// *  @param randomNumber   传入随机数
// *  @param tranckData    传入磁道密文
// *  @param tranckDtatOut 得到磁道明文
// */
//void JieTranckData(char *main_key,char *randomNumber,char *tranckData,char *tranckDtatOut);
///**
// *  @brief  计算PinBlock
// *
// *  @param pin    传入卡密码
// *  @param pan    传入卡号
// *  @param output PinBlock
// *  @param len    一般为固定长度：8
// */
//void PinBlock(char *pin,char *pan,unsigned char *output,int len);
//
///**
// *  @brief  按位异或
// *
// *  @param input1 输入数据1
// *  @param input2 输入数据2
// *  @param output 异或后的结果
// *  @param len    数据1或数据2的长度，长度要一致
// */
//void XorPinBlock(unsigned char *input1,unsigned char *input2,unsigned char *output,int len);
//
//
///**
// *  @brief  对磁道信息进行解密(新大陆  蓝牙)
// *
// *  @param main_key       传人主密钥
// *  @param tranckData    传入磁道密文
// *  @param tranckDtatOut 得到磁道明文
// *  @param flag          0:加密  1：解密
// */
//void JieXDLTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag);
//
//
///**
// *  @brief 对磁道信息进行解密（新大陆  音频）
// *
// *  @param main_key      传人主密钥
// *  @param tranckData    传入磁道密文
// *  @param tranckDtatOut 得到磁道明文
// *  @param flag          0:加密  1：解密
// */
//void JieXDLYinTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag);
//
///**
// *  @brief  对数据进行解密
// *
// *  @param main_key   传人主密钥
// *  @param input_data 传入明文
// *  @param out_data   得到密文
// *  @param flag       0:加密  1：解密
// */
//void JiaMiData(char *main_key,char *input_data,char *out_data,int flag);
//
///**
// *  @brief  对磁道信息进行解密（BP  蓝牙）
// *
// *  @param main_key      传入磁道密文
// *  @param tranckData    传入明文
// *  @param tranckDtatOut 得到磁道明文
// *  @param flag          0:加密  1：解密
// */
//void JieBpTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag);
//
///**
// *  @brief  对储值卡密码加密
// *
// *  @param key
// *  @param password
// *  @param output
// */
//void JiaMiPassWord(char *key,char *password,unsigned char *output);
//
//#endif
