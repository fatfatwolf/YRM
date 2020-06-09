////
////  PublicMethod.c
////  merchant
////
////  Created by HRT on 15/6/2.
////  Copyright (c) 2015年 Hrtpayment. All rights reserved.
////
//
//#include "PublicMethod.h"
//#include <stdarg.h>
//#include <stdio.h>
//#include <stdlib.h>
//#include <errno.h>
//#include <string.h>
//
//
//void vdHex2Str (char *pstHex, int inLen, char *pstStr)
//{
//     int inCnt;
//     char pstTemp[3];
//
//     memset( pstTemp, 0, sizeof(pstTemp) );
//
//     *pstStr = 0;
//     for(inCnt=0; inCnt<inLen; inCnt++)
//     {
//          sprintf(pstTemp, "%02X", pstHex[inCnt]);
//          strcat((char *)pstStr, pstTemp);
//     }
//
//     return;
//}
//
//
//
//int iByteToHex(unsigned char *pbt_Input,int i_Length,char *sz_Output)
//{
//     int iLeft,iRight,iPos=0,iLoop;
//     if(i_Length<0) return 0;
//     for(iLoop=0;iLoop<i_Length;iLoop++)
//     {
//          iLeft=(pbt_Input[iLoop] & 0xF0)>>4;
//          iRight=pbt_Input[iLoop] &0x0F;
//          sz_Output[iPos++]=(iLeft<10)?iLeft+48:iLeft+45;
//          sz_Output[iPos++]=(iRight<10)?iRight+48:iRight+55;
//     }
//     sz_Output[iPos]=0;
//     return 0;
//}
//
//int iHexToByte (char *sz_Input, unsigned char *pbt_Output, int *pi_Length)
//{
//     int iLen;
//     int iPos = 0, iLeft, iRight, iIdx;
//
//     if (!sz_Input) return 1;
//
//     iLen = (int)strlen (sz_Input);
//
//     for (iIdx = 0; iIdx < iLen; iIdx++, iPos++)
//     {
//          iLeft = sz_Input[iIdx++];
//          iRight = sz_Input[iIdx];
//
//          iLeft = (iLeft < 58) ? (iLeft - 48) : (iLeft - 55);
//          iRight = (iRight < 58) ? (iRight - 48) : (iRight - 55);
//
//          pbt_Output[iPos] = (iLeft << 4) | iRight;
//     }
//     *pi_Length = iPos;
//     return 0;
//}
//
//int iHexToByte1 (char *sz_Input, unsigned char *pbt_Output, int *pi_Length)
//{
//     int iLen;
//     int i,j;
//     if (!sz_Input) return 1;
//     iLen = (int)strlen (sz_Input);
//     char hb;
//     char lb;
//     for(i=0,j=0;i<iLen;i++)
//     {
//          hb=*(sz_Input+i);
//          if( hb>='A' && hb<='F' )
//               hb = hb - 'A' + 10;
//          else if(hb>='a'&&hb<='f')
//               hb=hb-'a'+10;
//          else if( hb>='0' && hb<='9' )
//               hb = hb - '0';
//          i++;
//          lb=*(sz_Input+i);
//          if( lb>='A' && lb<='F' )
//               lb = lb - 'A' + 10;
//          else if( lb>='a' && lb<='f' )
//               lb = lb - 'a' + 10;
//          else if( lb>='0' && lb<='9' )
//               lb = lb - '0';
//          *(pbt_Output+j)=(hb<<4)|(lb);
//          j++;
//     }
//     *pi_Length=j;
//     return 0;
//}
//
//
//void HexToString(unsigned char *sz_Input,char *sz_Output,int len)
//{
//     unsigned char hb;
//     unsigned char lb;
//     for(int i=0;i<len;i++)
//     {
//          hb=(*(sz_Input+i)&0xf0)>>4;
//
//          if( hb>=0 && hb<=9 )
//               hb += 0x30;
//          else if( hb>=10 &&hb <=15 )
//               hb = hb -10 + 'A';
//          else
//               return;
//
//          lb = *(sz_Input+i)&0x0f;
//          if( lb>=0 && lb<=9 )
//               lb += 0x30;
//          else if( lb>=10 && lb<=15 )
//               lb = lb - 10 + 'A';
//          else
//               return;
//          *(sz_Output+i*2)=hb;
//          *(sz_Output+i*2+1)=lb;
//     }
//}
//
//
//void ErrOfBack1(int n,char *output)//银行返回的错误情况 39域
//{
//     char buf[101][30]={"交易成功",
//          "请与银行联系",
//          "ISO 错误 #02",
//          "无效商户",
//          "ISO 错误 #04",
//          "不批准交易",
//          "ISO 错误 #06",
//          "ISO 错误 #07",
//          "请与银行联系",
//          "ISO 错误 #09",
//          "ISO 错误 #10",
//          "ISO 错误 #11",
//          "无效交易",
//          "无效金额",
//          "无此卡号",
//          "发卡行错误",
//          "ISO 错误 #16",
//          "ISO 错误 #17",
//          "ISO 错误 #18",
//          "稍后重做交易",
//          "ISO 错误 #20",
//          "不做任何处理",
//          "ISO 错误 #22",
//          "ISO 错误 #23",
//          "ISO 错误 #24",
//          "无此交易",
//          "重复交易",
//          "ISO 错误 #27",
//          "交易无法处理",
//          "文件更新不成功",
//          "格式错误",
//          "ISO 错误 #31",
//          "ISO 错误 #32",
//          "到期卡, 请没收",
//          "舞弊嫌疑, 请没收",
//          "联系保安，没收卡",
//          "ISO 错误 #36",
//          "联系卡中心，没收卡",
//          "密码错误次数已满",
//          "无此账户",
//          "非法功能",
//          "遗失卡, 请没收",
//          "无一般账",
//          "被盗卡, 请没收",
//          "无投资账",
//          "ISO 错误 #45",
//          "ISO 错误 #46",
//          "ISO 错误 #47",
//          "ISO 错误 #48",
//          "ISO 错误 #49",
//          "ISO 错误 #50",
//          "余额不足",
//          "ISO 错误 #52",
//          "ISO 错误 #53",
//          "过期卡",
//          "正常卡, 密码不符",
//          "无卡记录",
//          "持卡人无效交易",
//          "终端无效交易",
//          "舞弊嫌疑",
//          "请查询银行",
//          "超限额",
//          "受限制卡",
//          "违反安全保密规定",
//          "初始账额不正确",
//          "交易次数超限",
//          "ISO 错误 #66",
//          "请留起此卡",
//          "ISO 错误 #68",
//          "ISO 错误 #69",
//          "ISO 错误 #70",
//          "ISO 错误 #71",
//          "ISO 错误 #72",
//          "ISO 错误 #73",
//          "ISO 错误 #74",
//          "超过密码次数",
//          "ISO 错误 #76",
//          "结账不平",
//          "止付卡",
//          "非法账户",
//          "交易拒绝",
//          "卡已作废",
//          "ISO 错误 #82",
//          "ISO 错误 #83",
//          "联网暂断，重做交易",
//          "ISO 错误 #85",
//          "ISO 错误 #86",
//          "PIN密钥同步错",
//          "MAC密钥同步错",
//          "ISO 错误 #89",
//          "交易拒绝,请插卡交易",
//          "交易超时",
//          "重做交易或电话授权",
//          "ISO 错误 #93",
//          "重复交易",
//          "结账不平，上送交易",
//          "系统异常",
//          "终端号错误",
//          "暂与发卡行失去联系",
//          "PIN格式错",
//          "未知响应码"};
//     strcpy(output,(char *)(buf+n));
//}
//
//
//void QueryOfCard(unsigned char *input,char *output)//对银行返回的报文的54域进行处理
//{
//     char Buf1[13];
//     char Buf2[13];
//     int MoneyLen,j=0;
//     memset(Buf1,0,sizeof(Buf1));
//     memset(Buf2,0,sizeof(Buf2));
//
//     memcpy(Buf1,input+8,12);
//
//     MoneyLen=(int)strlen(Buf1);
//
//     if(!memcmp(Buf1,"000000000000",12))
//     {
//          strcpy(output,"0");
//          strcat(output,".");
//          strcat(output,"00");
//     }
//     else
//     {
//
//          for(int i=0;i<MoneyLen;i++)
//          {
//               if(Buf1[i]!='0')
//               {
//                    j=i;
//                    break;
//               }
//          }
//
//          strcpy(Buf2,Buf1+j);
//
//          int length=(int)strlen(Buf2);
//          if(length==1)
//          {
//               strcpy(output,"0.0");
//               strcat(output,Buf2);
//          }
//          else if (length==2)
//          {
//               strcpy(output,"0.");
//               strcat(output,Buf2);
//          }
//          else
//          {
//               strncpy(output,Buf2,length-2);
//
//               strcat(output,".");
//
//               strcat(output,Buf2+length-2);
//          }
//     }
//
//     return;
//}
//
//
//void vdStr2HexStr (char *i_szAsc, char *ppbt_Byte, int *iByteLength)
//{
//     int iLength = *iByteLength;
//     int iPos	= 0;
//     int iLeft;
//     int iRight;
//     int iIndex;
//
//
//     for (iIndex = 0; iIndex < iLength; iIndex++, iPos++)
//     {
//          iLeft = i_szAsc[iIndex++];
//          iRight = i_szAsc[iIndex];
//
//          iLeft = (iLeft < 58) ? (iLeft - 48) : (iLeft - 55);
//          iRight = (iRight < 58) ? (iRight - 48) : (iRight - 55);
//
//          ppbt_Byte[iPos] = (iLeft << 4) | iRight;
//     }
//
//     *iByteLength = iPos;
//
//     return;
//}
//
//void QueryOfCard1(char *input,char *output)
//{
//     int length=(int)strlen((char *)input);
//     char buf[13];
//     memset(buf,0,sizeof(buf));
//     int i,j=0;
//     for(i=0;i<length;i++)
//     {
//          if(*(input+i)!='0')
//          {
//               j=i;
//               break;
//          }
//     }
//     strcpy(buf,input+j);
//     int len=(int)strlen(buf);
//     if(len==1)
//     {
//          strcpy(output,"0.0");
//          strcat(output,buf);
//     }
//     else if (len==2)
//     {
//          strcpy(output,"0.");
//          strcat(output,buf);
//     }
//     else
//     {
//          strncpy(output,buf,len-2);
//          strcpy(output+len-2,".");
//          strncpy(output+len-1,input+len-2,2);
//     }
//}
//
//void RemoveZero(char *input,char *output)
//{
//     int len=(int)strlen(input);
//     int j=0;
//     for(int i=0;i<len;i++)
//     {
//          if(*(input+i)!='0')
//          {
//               j=i;
//               break;
//          }
//     }
//     strcpy(output,input+j);
//}
//
//void ValdOfCard(char *input,char *output)
//{
//     int len=(int)strlen(input);
//     int j=0;
//     for(int k=0;k<len;k++)
//     {
//          if(*(input+k)=='=' || *(input+k)=='d' || *(input+k)=='D')
//          {
//               j=k;
//               break;
//          }
//     }
//     strncpy(output,input+j+1,4);
//}
//
//void TransToString(char *input,char *output,int length)
//{
//     int len=(int)strlen(input);
//
//     if(len==length)
//          strcpy(output,input);
//     else
//     {
//          for(int i=0;i<length-len;i++)
//               strcat(output,"0");
//          strcat(output,input);
//     }
//
//}
//
//void TransToString61(char *input1,char *input2,char *input3,char *output)
//{
//     int len1=(int)strlen(input1);
//     int len2=(int)strlen(input3);
//     if(len1==6)
//          strcpy(output,input1);
//     else
//     {
//          for(int i=0;i<6-len1;i++)
//               strcat(output,"0");
//          strcat(output,input1);
//     }
//     strcat(output,input2);
//     if(len2==6)
//          strcat(output,input3);
//     else
//     {
//          for(int j=0;j<6-len2;j++)
//               strcat(output,"0");
//          strcat(output,input3);
//     }
//}
//
//void TransToString62(char *input1,char *input2,char *input3,char *output)
//{
//     int len=(int)strlen(input2);
//     strcat(output,input1);
//     if(len==6)
//          strcat(output,input2);
//     else
//     {
//          for(int i=0;i<6-len;i++)
//               strcat(output,"0");
//          strcat(output,input2);
//          strcat(output,input3);
//     }
//}
//
//void TransToStringVoid(char *input1,char *input2,char *input3,char *input4,char *output)
//{
//     int len=(int)strlen(input2);
//     strcat(output,input1);
//     if(len==6)
//          strcat(output,input2);
//     else
//     {
//          for(int i=0;i<6-len;i++)
//               strcat(output,"0");
//          strcat(output,input2);
//          strcat(output,input3);
//          strcat(output,input4);
//     }
//}
//
//
//
//void HexStrFromPinYin(char *input,char *output)
//{
//     int length;
//     int num;
//     int Quotient;
//     int remainder;
//     char word[2];
//     char buf[2];
//     char outStr[21]={0};
//     int CapitalOrNot;
//     length = (int)strlen(input);
//
//     if(length > 20)
//          return;
//
//     for (int i=0;i<length;i++)
//     {
//          memset(word,0,sizeof(word));
//          memset(buf,0,sizeof(buf));
//          num = *(input + i);
//
//          if(num == 20)
//          {
//               strcat(outStr,"20");
//               break;
//          }
//          if(num >= 65 && num <= 90)
//          {
//               CapitalOrNot = 1;
//          }
//          else if (num >= 97 && num <= 122)
//          {
//               CapitalOrNot = 2;
//          }
//          Quotient = num / 16;
//          remainder = num % 16 ;
//          sprintf(buf,"%d",Quotient);
//          switch (remainder)
//          {
//               case 10:
//                    if(num == 1)
//                         strcpy(word,"A");
//                    else
//                         strcpy(word,"a");
//                    break;
//               case 11:
//                    if(num == 1)
//                         strcpy(word,"B");
//                    else
//                         strcpy(word,"b");
//                    break;
//               case 12:
//                    if(num == 1)
//                         strcpy(word,"C");
//                    else
//                         strcpy(word,"c");
//                    break;
//               case 13:
//                    if(num == 1)
//                         strcpy(word,"D");
//                    else
//                         strcpy(word,"d");
//                    break;
//               case 14:
//                    if(num == 1)
//                         strcpy(word,"E");
//                    else
//                         strcpy(word,"e");
//                    break;
//               case 16:
//                    if(num == 1)
//                         strcpy(word,"F");
//                    else
//                         strcpy(word,"f");
//                    break;
//               default:
//                    sprintf(word,"%d",remainder);
//                    break;
//          }
//
//          strcat(outStr,buf);
//          strcat(outStr,word);
//     }
//
//     strcpy(output,outStr);
//}
