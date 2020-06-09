////
////  EncrypteAndDecrypte.c
////  merchant
////
////  Created by HRT on 15/6/2.
////  Copyright (c) 2015年 Hrtpayment. All rights reserved.
////
//
//#include "EncrypteAndDecrypte.h"
//#include "PublicMethod.h"
//#include <string.h>
//
//
///**************************des 加密********************************/
//// 初始置换
//const uint8 tableIP[64] =
//{
//     58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
//     62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
//     57, 49, 41, 33, 25, 17,  9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
//     61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
//};
//
//// 末置换
//const uint8 tableIP1[64] =
//{
//     40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
//     38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
//     36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
//     34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,  9, 49, 17, 57, 25
//};
//
//// 扩展置换
//const uint8 tableIP2[48] =
//{
//     32,  1,  2,  3,  4,  5,  4,  5,  6,  7,  8,  9,
//     8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
//     16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
//     24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
//};
//
//// 密钥初始置换
//const uint8 tableIP3[56] = {
//     57, 49, 41, 33, 25, 17,  9,  1, 58, 50, 42, 34, 26, 18,
//     10,  2, 59, 51, 43, 35, 27, 19, 11,  3, 60, 52, 44, 36,
//     63, 55, 47, 39, 31, 23, 15,  7, 62, 54, 46, 38, 30, 22,
//     14,  6, 61, 53, 45, 37, 29, 21, 13,  5, 28, 20, 12,  4
//};
//
//// 左右移运算
//const signed char tableIP4[2][16] =
//{
//     //加密左移
//     {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1},
//
//     //解密右移
//     {0, -1, -2, -2, -2, -2, -2, -2, -1, -2, -2, -2, -2, -2, -2, -1}
//};
//
//// 密钥压缩置换
//const uint8 tableIP5[48] =
//{
//     14, 17, 11, 24,  1,  5,  3, 28, 15,  6, 21, 10,
//     23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
//     41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
//     44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
//};
//
//// S盒
//const uint8 tableIP6[8][4][16] =
//{
//     14, 4, 13, 1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
//     0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
//     4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
//     15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13,
//     15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
//     3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
//     0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
//     13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9,
//     10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
//     13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
//     13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
//     1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12,
//     7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
//     13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
//     10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
//     3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14,
//     2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
//     14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
//     4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
//     11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3,
//     12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
//     10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
//     9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
//     4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13,
//     4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
//     13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
//     1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
//     6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12,
//     13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
//     1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
//     7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
//     2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11
//};
//
//// P盒置换
//const uint8 tableIP7[32] =
//{
//     16, 7, 20, 21, 29, 12, 28, 17, 1,  15, 23, 26, 5, 18, 31, 10,
//     2,  8, 24, 14, 32, 27, 3,  9,  19, 13, 30, 6, 22, 11, 4,  25
//};
//
////对两块大小相同的内存区进行异或
////异或结果保存到第一块内存
////uint8 * p_buf_1		内存区1
////const uint8 * p_buf_2	内存区2
////uint8 bytes			内存区大小(单位：字节)
//void Xor(uint8 * p_buf_1, const uint8 * p_buf_2, uint8 bytes)
//{
//     while(bytes > 0)
//     {
//          bytes--;
//
//          p_buf_1[bytes] ^= p_buf_2[bytes];
//     }
//}
//
////将缓冲区从第bit_start位到第bit_end进行循环左移
////offset只能是1，2
////本段代码还可以优化。
//void move_left(uint8 * p_input, uint8 bit_start, uint8 bit_end, uint8 offset)
//{
//     uint8 b_val = 0;
//     uint8 b_tmp1 = 0;
//     uint8 b_tmp2 = 0;
//
//     //读取bit_start位
//     b_tmp1 = GET_BIT(p_input, bit_start);
//     b_tmp2 = GET_BIT(p_input, bit_start + 1);
//
//     //循环左移offset位
//     for(; bit_start <= (bit_end - offset); bit_start++)
//     {
//          b_val = GET_BIT(p_input, bit_start + offset);
//          SET_BIT(p_input, bit_start, b_val);
//     }
//
//     //将bit_start开始的offset位移到bit_end后头来
//     if (1 == offset)
//     {
//          SET_BIT(p_input, bit_end, b_tmp1);
//     }
//     else
//     {
//          SET_BIT(p_input, bit_end, b_tmp2);
//          SET_BIT(p_input, bit_end - 1, b_tmp1);
//     }
//}
//
////将缓冲区从第bit_start位到第bit_end进行循环右移
////offset只能是1，2
////本段代码在性能上还可以优化。
//void move_right(uint8 * p_input, uint8 bit_start, uint8 bit_end, uint8 offset)
//{
//     uint8 b_val = 0;
//     uint8 b_tmp1 = 0;
//     uint8 b_tmp2 = 0;
//
//     //读取bit_end位
//     b_tmp1 = GET_BIT(p_input, bit_end);
//     b_tmp2 = GET_BIT(p_input, bit_end - 1);
//
//     //循环左移offset位
//     for(; bit_end >= (bit_start + offset); bit_end--)
//     {
//          b_val = GET_BIT(p_input, bit_end - offset);
//          SET_BIT(p_input, bit_end, b_val);
//     }
//
//     //将bit_end倒数的offset位移到bit_start来
//     if (1 == offset)
//     {
//          SET_BIT(p_input, bit_start, b_tmp1);
//     }
//     else
//     {
//          SET_BIT(p_input, bit_start, b_tmp2);
//          SET_BIT(p_input, bit_start + 1, b_tmp1);
//     }
//}
//
////缓冲区移位
////offset大于0时左移
////offset小于0时右移
//void move_bits(uint8 * p_input, uint8 bit_start, uint8 bit_end, char offset)
//{
//     if(0 < offset)	//左移
//     {
//          move_left(p_input, bit_start, bit_end, offset);
//     }
//     else if(0 > offset)	//右移
//     {
//          move_right(p_input, bit_start, bit_end, -offset);
//     }
//}
//
////通用置换函数, bits <= 64
////p_input与p_output不能指向同一个地址，否则置换会出错。
//void Permutation(uint8 * p_input, uint8 * p_output, const uint8 * Table, uint8 bits)
//{
//     uint8 b_val = FALSE;
//     uint8 bit_index = 0;
//
//     for(bit_index = 0; bit_index < bits; bit_index++)
//     {
//          b_val = GET_BIT(p_input, Table[bit_index] - 1);
//
//          SET_BIT(p_output, bit_index, b_val);
//     }
//}
//
////获取从bit_s为起始的第1, 6 位组成行
//uint8 S_GetLine(uint8 * p_data_ext, uint8 bit_s)
//{
//     return (GET_BIT(p_data_ext, bit_s + 0) << 1) + GET_BIT(p_data_ext, bit_s + 5);
//}
//
////获取从bit_s为起始的第2,3,4,5位组成列
//uint8 S_GetRow(uint8 * p_data_ext, uint8 bit_s)
//{
//     uint8 row;
//
//     //2,3,4,5位组成列
//     row = GET_BIT(p_data_ext, bit_s + 1);
//     row <<= 1;
//     row += GET_BIT(p_data_ext, bit_s + 2);
//     row <<= 1;
//     row += GET_BIT(p_data_ext, bit_s + 3);
//     row <<= 1;
//     row += GET_BIT(p_data_ext, bit_s + 4);
//
//     return row;
//}
//
/////////////////////////////////////////////////////////////////
////	函 数 名 : des
////	函数功能 : DES加解密
////	处理过程 : 根据标准的DES加密算法用输入的64位密钥对64位密文进行加/解密
////				并将加/解密结果存储到p_output里
////	返 回 值 :
////	参数说明 :	const char * p_data		输入, 加密时输入明文, 解密时输入密文, 64位(8字节)
////				const char * p_key		输入, 密钥, 64位(8字节)
////				char * p_output			输出, 加密时输出密文, 解密时输入明文, 64位(8字节)
////				uint8 mode				DES_ENCRYPT 加密  DES_DECRYPT 解密
/////////////////////////////////////////////////////////////////
//void des(uint8 * p_data, uint8 * p_key, uint8 * p_output, DES_MODE mode)
//{
//     uint8 loop = 0;		//16轮运算的循环计数器
//     uint8 key_tmp[8];	//密钥运算时存储中间结果
//     uint8 sub_key[6];	//用于存储子密钥
//
//     uint8 * p_left;
//     uint8 * p_right;
//
//     uint8 p_right_ext[8];	//R[i]经过扩展置换生成的48位数据(6字节), 及最终结果的存储
//     uint8 p_right_s[4];		//经过S_BOX置换后的32位数据(4字节)
//
//     uint8 s_loop = 0;		//S_BOX置换的循环计数器
//
//     //密钥第一次缩小换位, 得到一组56位的密钥数据
//     Permutation(p_key, key_tmp,tableIP3, 56);
//
//     //明文初始化置换
//     Permutation(p_data, p_output,tableIP, 64);
//
//     p_left  = p_output;		//L0
//     p_right = &p_output[4];	//R0
//
//     for(loop = 0; loop <16; loop++)
//     {
//          //把缩进小后的把这56位分为左28位和右28位,
//          //对左28位和右28位分别循环左/右移, 得到一组新数据
//          //加解密操作时只在移位时有差异
//          move_bits(key_tmp, 0, 27,tableIP4[mode][loop]);
//          move_bits(key_tmp, 28, 55,tableIP4[mode][loop]);
//
//          //密钥第二次缩小换位，得到一组子48位的子密钥
//          Permutation(key_tmp, sub_key,tableIP5, 48);
//
//          //R0扩展置换
//          Permutation(p_right, p_right_ext,tableIP2, 48);
//
//          //将R0扩展置换后得到的48位数据(6字节)与子密钥进行异或
//          Xor(p_right_ext, sub_key, 6);
//
//          //S_BOX置换
//          for(s_loop = 0; s_loop < 4; s_loop++)
//          {
//               uint8 s_line = 0;
//               uint8 s_row = 0;
//               uint8 s_bit = s_loop * 12;
//
//               s_line = S_GetLine(p_right_ext, s_bit);
//               s_row  = S_GetRow(p_right_ext,	s_bit);
//
//               p_right_s[s_loop] =tableIP6[s_loop * 2][s_line][s_row];
//
//               s_bit += 6;
//
//               s_line = S_GetLine(p_right_ext, s_bit);
//               s_row  = S_GetRow(p_right_ext,	s_bit);
//
//               p_right_s[s_loop] <<= 4;
//               p_right_s[s_loop] +=tableIP6[(s_loop * 2) + 1][s_line][s_row];
//          }
//
//          //P置换
//          Permutation(p_right_s, p_right_ext,tableIP7, 32);
//
//          Xor(p_right_ext, p_left, 4);
//
//          memcpy(p_left, p_right, 4);
//          memcpy(p_right, p_right_ext, 4);
//     }
//
//     memcpy(&p_right_ext[4], p_left, 4);
//     memcpy(p_right_ext,	p_right, 4);
//
//     //最后再进行一次逆置换, 得到最终加密结果
//     Permutation(p_right_ext, p_output,tableIP1, 64);
//}
//
//void XOR(uint8 *input1,uint8 *input2,uint8 *output,uint8 len)
//{
//     while (len)
//     {
//          *output++=*input1++^*input2++;
//          len--;
//     }
//}
//
///*
// *@brief: 根据输入数据计算MAC，初始IV向量默认为"x00x00x00x00x00x00x00x00"
// *@param: sMacKey1 密钥1
// *@param: sMacKey2 密钥2
// *@param: pInData 输入数据
// *@param: iInLen 输入数据长度
// *@param: pRetData 计算出来的MAC
// *@param: flag   标志位，flag=1时代表对密码加密，flag=2时代表对mac字符串加密
// *@调用自定义xor和des函数
// */
//void MacArithmetic(char *sMacKey,char *pInData,unsigned char *pRetData,int flag)
//{
//     unsigned char sUpData[512];
//     unsigned char sData[20];
//     unsigned char sXorData[20];
//     unsigned char sDesData[20];
//     unsigned char sMacKey1[8+1];
//     unsigned char sMacKey2[8+1];
//     unsigned char MacBuf[16+1];
//     unsigned char sDesData1[20];//测试
//     char     inputData[100];
//     int i,n;
//
//     memset(sUpData,0,sizeof(sUpData));
//     memset(sData,0,sizeof(sData));
//     memset(sXorData,0,sizeof(sXorData));
//     memset(sDesData,0,sizeof(sDesData));
//     memset(sMacKey1,0,sizeof(sMacKey1));
//     memset(sMacKey2,0,sizeof(sMacKey2));
//     memset(MacBuf,0,sizeof(MacBuf));
//     memset(sDesData1,0,sizeof(sDesData1));
//     memset(inputData,0,sizeof(inputData));
//
//
//     int length1=(int)strlen(pInData);
//     int leng16=length1%16;
//     if(leng16==0)
//     {
//          strcpy(inputData,pInData);
//     }
//     else
//     {
//          strcpy(inputData,pInData);
//          for(int j=0;j<16-leng16;j++)
//               strcat(inputData,"0");
//     }
//
//     int len;
//     iHexToByte1((char *)inputData,sUpData,&len);//对输入的数据进行转换
//     n=len/8;
//
//     int length;
//     iHexToByte1((char *)sMacKey,MacBuf,&length);
//     memcpy(sMacKey1,MacBuf,length/2);
//     memcpy(sMacKey2,MacBuf+length/2,length/2);
//     //print_hex("sMacKey1 [输出]密钥1",sMacKey1,8);
//     //print_hex("sMacKey2 [输出]密钥2",sMacKey2,8);
//
//     memcpy(sData,sUpData,8);
//     // print_hex("sData [输出]明文1 ",sData,8);
//     if(flag==1)
//          des(sData,sMacKey1,sDesData,DES_ENCRYPT);
//     else if (flag==2)
//          des(sData,sMacKey1,sDesData,DES_DECRYPT);
//     //print_hex("sDesData [输出]密文1 ",sDesData,8);
//     n=n-1;
//     for(i=0;i<n;i++)
//     {
//          memcpy(sData,sUpData+(i+1)*8,8);
//          //print_hex("sData [输出]明文2 ",sData,8);
//
//          XOR(sDesData,sData,sXorData,8);
//          //print_hex("sXorData [输出]密文2 ",sXorData,8);
//
//          des(sXorData,sMacKey1,sDesData,DES_ENCRYPT);
//          //print_hex("sDesData [输出]密文3 ",sDesData,8);
//     }
//     unsigned char en_data[8];
//     unsigned char en_out_put[8];
//     if(flag==1)
//     {
//          des(sDesData,sMacKey2, en_data, DES_DECRYPT);
//          //print_hex("en_data [输出]密文4 ",en_data,8);
//          des(en_data,sMacKey1, en_out_put, DES_ENCRYPT);
//          memcpy(pRetData,en_out_put,8);
//          //print_hex("pRetData [输出]密文5 ",pRetData,8);
//     }
//     else if(flag==2)
//     {
//          des(sDesData,sMacKey2, en_data, DES_ENCRYPT);
//          //print_hex("en_data [输出]密文4 ",en_data,8);
//          des(en_data,sMacKey1, en_out_put, DES_DECRYPT);
//          memcpy(pRetData,en_out_put,8);
//          //print_hex("pRetData [输出]密文5 ",pRetData,8);
//     }
//     return ;
//}/*********************end****************************/
//
//
//
//void XorPinBlock(unsigned char *input1,unsigned char *input2,unsigned char *output,int len)
//{
//     while (len)
//     {
//          *output++=*input1++^*input2++;
//          len--;
//     }
//}
//
////计算出pinblock
//void PinBlock(char *pin,char *pan,unsigned char *output,int len)
//{
//     char pinblock[17];
//     char panblock[17];
//     unsigned char pinblock1[9];
//     unsigned char panblock1[9];
//     memset(pinblock,0,sizeof(pinblock));
//     memset(panblock,0,sizeof(panblock));
//     memset(pinblock1,0,sizeof(pinblock1));
//     memset(panblock1,0,sizeof(panblock1));
//     strcat(pinblock,"06");
//     strcat(pinblock,pin);
//     strcat(pinblock,"FFFFFFFF");
//
//     int length=(int)strlen(pan);
//     strcat(panblock,"0000");
//     strncat(panblock,pan+length-13,12);
//
//     int length1;
//     iHexToByte1(pinblock,pinblock1,&length1);
//     iHexToByte1(panblock,panblock1,&length1);
//
//     XorPinBlock(pinblock1,panblock1,output,8);
//}
//
//void JiePlaintext(char *masterKey,char *miKey,char *mingKey)
//{
//     char qianBuf[17];
//     char houBuf[17];
//     unsigned char qianOfHex[9];
//     unsigned char houOfHex[9];
//     char zhuanBuf[17];
//     char zhuanBuf1[17];
//     memset(zhuanBuf,0,sizeof(zhuanBuf));
//     memset(zhuanBuf1,0,sizeof(zhuanBuf1));
//     memset(qianBuf,0,sizeof(qianBuf));
//     memset(houBuf,0,sizeof(houBuf));
//     memset(qianOfHex,0,sizeof(qianOfHex));
//     memset(houOfHex,0,sizeof(houOfHex));
//
//     strncpy(qianBuf,miKey,16);
//     strncpy(houBuf,miKey+16,16);
//
//     MacArithmetic(masterKey,qianBuf,qianOfHex,2);
//     MacArithmetic(masterKey,houBuf,houOfHex,2);
//
//     HexToString(qianOfHex,zhuanBuf,8);
//     HexToString(houOfHex,zhuanBuf1,8);
//
//     strcpy(mingKey,zhuanBuf);
//     strcat(mingKey,zhuanBuf1);
//}
//
//
//void JieTranckData(char *main_key,char *randomNumber,char *tranckData,char *tranckDtatOut)
//{
//     unsigned char randomNumberBuf[9];
//     unsigned char randomNumberBuf1[9];
//     char randomNumberFan[17];
//     memset(randomNumberBuf,0,sizeof(randomNumberBuf));
//     memset(randomNumberBuf1,0,sizeof(randomNumberBuf1));
//     memset(randomNumberFan,0,sizeof(randomNumberFan));
//     int len;
//     //取反
//     unsigned char buf[9];
//     memset(buf,0,sizeof(buf));
//     iHexToByte1("FFFFFFFFFFFFFFFF",buf,&len);
//     iHexToByte1(randomNumber,randomNumberBuf,&len);
//     XORPINBLOCK((char *)randomNumberBuf,buf,randomNumberBuf1,len);
//     HexToString(randomNumberBuf1,randomNumberFan,len);
//
//     //工作密钥
//     unsigned char qianMiYao[9];
//     unsigned char houMiYao[9];
//     char HexQianToStr[33];
//     char HexHouToStr[17];
//     memset(HexQianToStr,0,sizeof(HexQianToStr));
//     memset(HexHouToStr,0,sizeof(HexHouToStr));
//     memset(qianMiYao,0,sizeof(qianMiYao));
//     memset(houMiYao,0,sizeof(houMiYao));
//     MacArithmetic(main_key,randomNumber,qianMiYao,1);
//     MacArithmetic(main_key,randomNumberFan,houMiYao,1);
//     HexToString(qianMiYao,HexQianToStr,8);
//     HexToString(houMiYao,HexHouToStr,8);
//     strcat(HexQianToStr,HexHouToStr);
//
//     //解密
//     int length=(int)strlen(tranckData);
//     int n=length/16;
//     char Data[17];
//     unsigned char DataOfZ[9];
//     char DataOfStr[17];
//     for(int i=0;i<n;i++)
//     {
//          memset(Data,0,sizeof(Data));
//          memset(DataOfZ,0,sizeof(DataOfZ));
//          memset(DataOfStr,0,sizeof(DataOfStr));
//          strncpy(Data,tranckData+i*16,16);
//          MacArithmetic(HexQianToStr,Data,DataOfZ,2);
//          HexToString(DataOfZ,DataOfStr,8);
//          strcat(tranckDtatOut,DataOfStr);
//     }
//}
//
//
//void JieXDLTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag)
//{
//     int length=(int)strlen(tranckData);
//     int len=(length/2)%2;
//
//     char trData[length/2+1];
//     memset(trData,0,sizeof(trData));
//     vdStr2HexStr(tranckData,trData,&length);
//
//     int len1=(int)strlen(trData);
//     char miTrack[17];
//     char moWei[2];
//     char mingBuf[17];
//     unsigned char jieBuf[9];
//     memset(miTrack,0,sizeof(miTrack));
//     memset(moWei,0,sizeof(moWei));
//     memset(jieBuf,0,sizeof(jieBuf));
//     memset(mingBuf,0,sizeof(mingBuf));
//     if(len==1)
//     {
//          memcpy(miTrack,trData+len1-17,16);
//          memcpy(tranckDtatOut,trData,len1-17);
//          memcpy(moWei,trData+length-1,1);
//
//          MacArithmetic(main_key,miTrack,jieBuf,flag);
//          HexToString(jieBuf,mingBuf,8);
//
//          strcat(tranckDtatOut,mingBuf);
//          strcat(tranckDtatOut,moWei);
//     }
//     else if (len==0)
//     {
//          memcpy(miTrack,trData+len1-16,16);
//          memcpy(tranckDtatOut,trData,len1-16);
//
//          MacArithmetic(main_key,miTrack,jieBuf,flag);
//          HexToString(jieBuf,mingBuf,8);
//          strcat(tranckDtatOut,mingBuf);
//     }
//}
//
//
//void JieXDLYinTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag)
//{
//     int length=(int)strlen(tranckData);
//     char trData[length/2+1];
//     memset(trData,0,sizeof(trData));
//     vdStr2HexStr(tranckData,trData,&length);
//     int len1=(int)strlen(trData);
//     int len2=len1%2;
//     char miTrack[17];
//     char moWei[2];
//     char mingBuf[17];
//     char jiewei[3];
//     unsigned char jieBuf[9];
//     memset(miTrack,0,sizeof(miTrack));
//     memset(moWei,0,sizeof(moWei));
//     memset(jieBuf,0,sizeof(jieBuf));
//     memset(mingBuf,0,sizeof(mingBuf));
//     memset(jiewei,0,sizeof(jiewei));
//     if(len2==1)
//     {
//          memcpy(miTrack,trData+len1-17,16);
//          memcpy(tranckDtatOut,trData,len1-17);
//          memcpy(moWei,trData+length-1,1);
//
//          MacArithmetic(main_key,miTrack,jieBuf,flag);
//          HexToString(jieBuf,mingBuf,8);
//
//          strcat(tranckDtatOut,mingBuf);
//          strcat(tranckDtatOut,moWei);
//     }
//     else if (len2==0)
//     {
//          memcpy(miTrack,trData+len1-18,16);
//          memcpy(tranckDtatOut,trData,len1-18);
//          memcpy(jiewei,trData+len1-2,2);
//          MacArithmetic(main_key,miTrack,jieBuf,flag);
//          HexToString(jieBuf,mingBuf,8);
//          strcat(tranckDtatOut,mingBuf);
//          strcat(tranckDtatOut,jiewei);
//     }
//}
//
//void JiaMiData(char *main_key,char *input_data,char *out_data,int flag)
//{
//     int length=(int)strlen(input_data);
//     int n=length/16;
//
//     char Data[17];
//     unsigned char DataOfZ[9];
//     char DataOfStr[17];
//     for(int i=0;i<n;i++)
//     {
//          memset(Data,0,sizeof(Data));
//          memset(DataOfZ,0,sizeof(DataOfZ));
//          memset(DataOfStr,0,sizeof(DataOfStr));
//
//          strncpy(Data,input_data+i*16,16);
//          MacArithmetic(main_key,Data,DataOfZ,flag);
//          HexToString(DataOfZ,DataOfStr,8);
//          strcat(out_data,DataOfStr);
//     }
//}
//
//void JieBpTranckData(char *main_key,char *tranckData,char *tranckDtatOut,int flag)
//{
//     int length=(int)strlen(tranckData);
//     int n=length/16;
//
//     char miTrack[17];
//     unsigned char DataOfZ[9];
//     char DataOfStr[17];
//     for(int i=0;i<n;i++)
//     {
//          memset(miTrack,0,sizeof(miTrack));
//          memset(DataOfZ,0,sizeof(DataOfZ));
//          memset(DataOfStr,0,sizeof(DataOfStr));
//          strncpy(miTrack,tranckData+i*16,16);
//
//          MacArithmetic(main_key,miTrack,DataOfZ,flag);
//          HexToString(DataOfZ,DataOfStr,8);
//          strcat(tranckDtatOut,DataOfStr);
//     }
//}
//
//
//void JiaMiPassWord(char *key,char *password,unsigned char *output)
//{
//    unsigned char sUpData[9];
//    unsigned char sData[20];
//    unsigned char sDesData[20];
//    unsigned char sMacKey1[8+1];
//    unsigned char sMacKey2[8+1];
//    unsigned char sMacKey3[8+1];
//    unsigned char MacBuf[16+1];
//    char     inputData[17];
//
//    memset(sUpData,0,sizeof(sUpData));
//    memset(sData,0,sizeof(sData));
//    memset(sDesData,0,sizeof(sDesData));
//    memset(sMacKey1,0,sizeof(sMacKey1));
//    memset(sMacKey2,0,sizeof(sMacKey2));
//    memset(sMacKey3,0,sizeof(sMacKey3));
//    memset(MacBuf,0,sizeof(MacBuf));
//    memset(inputData,0,sizeof(inputData));
//
//
//    strcpy(inputData,password);
//
//    int len;
//    iHexToByte1((char *)inputData,sUpData,&len);
//
//    int length;
//    iHexToByte1((char *)key,MacBuf,&length);
//    memcpy(sMacKey1,MacBuf,length/3);
//    memcpy(sMacKey2,MacBuf+length/3,length/3);
//    memcpy(sMacKey3,MacBuf+(length/3)*2,length/3);
//
//    memcpy(sData,sUpData,8);
//
//
//    des(sData,sMacKey1,sDesData,DES_ENCRYPT);
//
//    unsigned char en_data[8];
//    unsigned char en_out_put[8];
//
//    des(sDesData,sMacKey2, en_data, DES_DECRYPT);
//
//    des(en_data,sMacKey3, en_out_put, DES_ENCRYPT);
//
//    memcpy(output,en_out_put,8);
//
//    return ;
//}
