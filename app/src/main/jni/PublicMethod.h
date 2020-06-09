////
////  PublicMethod.h
////  merchant
////
////  Created by HRT on 15/6/2.
////  Copyright (c) 2015年 Hrtpayment. All rights reserved.
////
//
//#ifndef merchant_PublicMethod_h
//#define merchant_PublicMethod_h
//
////大写转小写
//#define ZHUANHUAN(input)\
//int len=(int)strlen(input);\
//for(int i=0;i<len;i++)\
//{\
//if(*(input+i)>='A'&&*(input+i)<='F')\
//*(input+i)+=32;\
//}
//
////小写转大写
//#define ZHUANTOXIAO(input,output)\
//{\
//int len=(int)strlen(input);\
//strcpy(output,input);\
//for(int i=0;i<len;i++)\
//{\
//if(*(output+i)>='a'&&*(output+i)<='f')\
//*(output+i)-=32;\
//}\
//}
//
////异或
//#define XORPINBLOCK(input1,input2,output,len)\
//for(int i=0;i<len;i++)\
//{\
//*(output+i)=*(input1+i)^*(input2+i);\
//}
////签到
//#define FIELD61(input1,input2,output)\
//{\
//int len=(int)strlen(input1);\
//if(len==6)\
//strcat(output,input1);\
//else\
//{\
//for(int i=0;i<6-len;i++)\
//strcat(output,"0");\
//strcat(output,input1);\
//}\
//strcat(output,input2);\
//}
//
//
///**
// *  @brief  把字符串前的0去掉
// *
// *  @param input  输入的字符串
// *  @param output 去0后的字符串
// */
//void RemoveZero(char *input,char *output);
///**
// *  @brief  得到卡的有效期
// *
// *  @param input  传入二磁道数据
// *  @param output 得到的有效期
// */
//void ValdOfCard(char *input,char *output);
///**
// *  @brief  在字符串前加0
// *
// *  @param input  输入的字符串
// *  @param output 加0后的字符串
// *  @param length 需要得到的字符串额长度
// */
//void TransToString(char *input,char *output,int length);
///**
// *  @brief  组报文的61域
// *
// *  @param input1 批次号
// *  @param input2 001
// *  @param input3 查询号
// *  @param output 得到的字符串
// */
//void TransToString61(char *input1,char *input2,char *input3,char *output);
///**
// *  @brief 组冲正报文的62域
// *
// *  @param input1 0400
// *  @param input2 流水号
// *  @param input3 0000000000
// *  @param output 得到的字符串
// */
//void TransToString62(char *input1,char *input2,char *input3,char *output);
///**
// *  @brief  组撤销报文的61域
// *
// *  @param input1 消息类型
// *  @param input2 原流水号
// *  @param input3 交易日期
// *  @param input4 交易时间
// *  @param output 得到的字符串
// */
//void TransToStringVoid(char *input1,char *input2,char *input3,char *input4,char *output);
//
///**
// *  @brief  把字符串形式的数据转bcd码
// *
// *  @param sz_Input    输入字符串数据
// *  @param pbt_Output 转化后的bcd数据
// *  @param pi_Length  转化后的数据长度
// *
// *  @return 转化后的bcd数据
// */
//int iHexToByte (char *sz_Input, unsigned char *pbt_Output, int *pi_Length);
//int iHexToByte1 (char *sz_Input, unsigned char *pbt_Output, int *pi_Length);
//
///**
// *  @brief  把字符串转成另一种字符串
// *
// *  @param pstHex 输入字符串数据
// *  @param inLen  输入字符串数据长度
// *  @param pstStr 转化后的字符串
// */
//void vdHex2Str (char *pstHex, int inLen, char *pstStr);
//
///**
// *  @brief  把bcd码形式的数据转字符串
// *
// *  @param pbt_Input 输入bcd数据
// *  @param i_Length  bcd数据长度
// *  @param sz_Output  转化后的字符串
// *
// *  @return 转化后的字符串
// */
//int iByteToHex(unsigned char *pbt_Input,int i_Length,char *sz_Output);
//
///**
// *  @brief  根据返回的39域的数字返回相应的错误信息
// *
// *  @param n      把返回的39域字符串转成数字
// *  @param output 根据返回的39域的数字返回相应的错误信息
// */
//void ErrOfBack1(int n,char *output);
//
///**
// *  @brief  对银行返回的报文的54域进行处理
// *
// *  @param input  54域字符串
// *  @param output 处理后的数据（主要是加小数点的位置）
// */
//void QueryOfCard(unsigned char *input,char *output);
//
//
///**
// *  @brief  金额在存入数据库前作处理
// *
// *  @param input   金额
// *  @param output 处理后的数据（主要是加小数点的位置
// */
//void QueryOfCard1(char *input,char *output);
//
///**
// *  @brief  把bcd数据转成字符串
// *
// *  @param sz_Input  bcd数据
// *  @param sz_Output 转化后的字符串
// *  @param len       接收到的数据长度
// 如输入unsigned char buf[3]={0x12,0x23,0x34}; 那输出：122334
// */
//void HexToString(unsigned char *sz_Input,char *sz_Output,int len);
//
//void vdStr2HexStr (char *i_szAsc, char *ppbt_Byte, int *iByteLength);
//
//
///**
// *  @brief  把拼音转成16进制的字符串
// *
// *  @param input  拼音字符串可包含空格
// *  @param output 处理后的数据
// */
//void HexStrFromPinYin(char *input,char *output);
//
//#endif
