///*************************************************************************
//*功能：实现DES及3DES加解密的算法，核心代码来自网上搜集，
//*主要修改了适应和融通的支付功能相关的代码，更易用
//*主要用于对数字和字母加密
//*作者：和融通支付技术部 无名氏 刘元兵 黄金
//*日期：2016-1-18
//*V1.0 仅限于对数字和字母加密，不能用于对大量汉字加密
//*************************************************************************/
//
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#include "des.h"
//#include "com_hybunion_member_utils_ndk_JniUtils.h"
//
///*************************************************************************
//*函数声明
//*************************************************************************/
//
//int encode(char* strSrc,  char* strDest);//外部调用这个函数加密
//int decode(char* strSrc,  char* strDest);//外包调用这个函数解密
//
//int Do_DES(char* strSrc, char* strKey, char* strDest, char flag);
//int Do_3DES(char* strSrc, char* strKey, char* strDest, char flag);
//char* leftFetch(char *dst,char *src, int n);
//char* midFetch(char *dst,char *src, int n,int m);
//int ByteToBCD(unsigned char* bytes, int count,unsigned char* strBCD);
//int BCDToByte(unsigned char* strBCD, int count, unsigned char* bytes);
//
//const char* key="9999999988888888";
//
//
//
///*************************************************************************
//*主函数，测试用
//*************************************************************************/
//int main(int argc, char** argv)
//{
//
//    char security_text[1000];
//    char plain_text[1000];
//
//    memset(security_text,0,sizeof(security_text));
//    memset(plain_text,0,sizeof(plain_text));
//
//    if(strcmp(argv[1],"-e") == 0)
//    {
//        encode("12345678",security_text);
//        printf("%s",security_text);
//    }
//    else if(strcmp(argv[1],"-d") == 0)
//    {
//        decode("4E3C6A875082258FE4F64EF208E81ACB",plain_text);
//        printf("%s",plain_text);
//    }
//
//    return 0;
//}
//
///*************************************************************************
//*功能：加密
//*strInSrc 输入的明文
//*strOutDest 输出的密文
//*************************************************************************/
//int encode(char* strInSrc,  char* strOutDest)
//{
//
//    char src16[16+1],key16[16+1],key48[48+1],dest16[16+1];
//
//    memset(src16,0,sizeof(src16));
//    memset(key48,0,sizeof(key48));
//    memset(dest16,0,sizeof(dest16));
//
//    char chTemp[8];
//    int i=0;
//
//    for(i=0; i<=strlen(strInSrc)/8; i++)
//    {
//        memset(chTemp,0,sizeof(chTemp));
//        memset(src16,0,sizeof(src16));
//
//        midFetch(chTemp,strInSrc,8,i*8);
//
//        LOGD("chTemp %s\n",chTemp);
//        ByteToBCD(chTemp,strlen(chTemp),src16);
//        ByteToBCD(key,strlen(key),key48);
//
//        LOGD("src16 = %s\n",src16);
//        //  printf("%s\n",key48);
//
//        Do_3DES(src16,key48,dest16,'e');
//
//        strcat(strOutDest,dest16);
//    }
//
//    LOGD("Result: [%s]\n",strOutDest);
//    return 0;
//}
//
///*************************************************************************
//*功能：解密
//*strInSrc 输入的密文
//*strOutDest 输出的明文
//*************************************************************************/
//int decode(char* strInSrc, char* strOutDest)
//{
//    char src16[16+1],key16[16+1],key48[48+1],dest16[16+1];
//
//    memset(key48,0,sizeof(key48));
//    memset(dest16,0,sizeof(dest16));
//
//    char chTemp[16];
//    int i=0;
//
//    for(i=0; i<strlen(strInSrc)/16; i++)
//    {
//
//        memset(chTemp,0,sizeof(chTemp));
//
//        midFetch(chTemp,strInSrc,16,i*16);
//
//        ByteToBCD(key,16,key48);
//
//        Do_3DES(chTemp,key48,dest16,'d');
//
//        unsigned char data[100];
//        memset(data,0,sizeof(data));
//
//        BCDToByte(dest16,strlen(dest16),data);
//        strcat(strOutDest,data);
//    }
//
//    return 0;
//}
//
////做DES加密或解密运算
//int Do_DES(char* strSrc, char* strKey, char* strDest, char flag)
//{
//    int i,j;
//    unsigned char subKey[16][48+1],byte8[8+1],bits[64+1],strTmp[64+1];
//    unsigned char L0[32+1],R0[32+1],Lx[32+1],Rx[32+1];
//
//    if(!( flag == 'e' || flag == 'E' || flag == 'd' || flag == 'D'))
//        return -1;
//    if(strSrc == NULL || strKey == NULL)
//        return -2;
//
//    if(flag == 'e' || flag == 'E')
//    {
//        memset(byte8,0,sizeof(byte8));
//        BCDToByte(strKey, 16, byte8);
//        memset(bits,0,sizeof(bits));
//        ByteToBit(byte8, 8, bits);
//
//        Des_GenSubKey(bits,subKey);
//
//        BCDToByte(strSrc, 16, byte8);
//        ByteToBit(byte8, 8, bits);
//        Des_IP(bits, strTmp);
//        memcpy(L0,strTmp,32);
//        memcpy(R0,strTmp+32,32);
//
//        for(i=0; i<16; i++)
//        {
//            memcpy(Lx,R0,32);
//            Des_F(R0,subKey[i],Rx);
//            Do_XOR(L0,32,Rx);
//            memcpy(L0,Lx,32);
//            memcpy(R0,Rx,32);
//        }
//        memcpy(bits,R0,32);
//        memcpy(bits+32,L0,32);
//        Des_IP_1(bits,strTmp);
//        BitToByte(strTmp,64,byte8);
//        ByteToBCD(byte8,8,strDest);
//    }
//    else
//    {
//        memset(byte8,0,sizeof(byte8));
//        BCDToByte(strKey, 16, byte8);
//        memset(bits,0,sizeof(bits));
//        ByteToBit(byte8, 8, bits);
//
//        Des_GenSubKey(bits,subKey);
//
//        BCDToByte(strSrc, 16, byte8);
//        ByteToBit(byte8, 8, bits);
//        Des_IP(bits, strTmp);
//        memcpy(L0,strTmp,32);
//        memcpy(R0,strTmp+32,32);
//
//        for(i=0; i<16; i++)
//        {
//            memcpy(Lx,R0,32);
//            Des_F(R0,subKey[15-i],Rx);
//            Do_XOR(L0,32,Rx);
//            memcpy(L0,Lx,32);
//            memcpy(R0,Rx,32);
//        }
//        memcpy(bits,R0,32);
//        memcpy(bits+32,L0,32);
//        Des_IP_1(bits,strTmp);
//        BitToByte(strTmp,64,byte8);
//        ByteToBCD(byte8,8,strDest);
//    }
//
//    return 0;
//}
//
////做3DES加密或解密运算
//int Do_3DES(char* strSrc, char* strKey, char* strDest, char flag)
//{
//    unsigned char strBCDKey[32+1],strByteKey[16+1];
//    unsigned char strMidDest1[16+1],strMidDest2[16+1];
//    unsigned char strLKey[16+1],strMKey[16+1],strRKey[16+1];
//
//    if(!( flag == 'e' || flag == 'E' || flag == 'd' || flag == 'D'))
//        return -1;
//    if(strSrc == NULL || strKey == NULL)
//        return -2;
//
//    if(strlen(strKey) < 32)
//        return -3;
//
//    if(flag == 'e' || flag == 'E')
//    {
//        memset(strBCDKey,0,sizeof(strBCDKey));
//        memcpy(strBCDKey,strKey,32);
//
//        memset(strLKey,0,sizeof(strLKey));
//        memcpy(strLKey,strBCDKey,16);
//        memset(strRKey,0,sizeof(strRKey));
//        memcpy(strRKey,strBCDKey+16,16);
//
//        Do_DES(strSrc,strLKey,strMidDest1,'e');
//        Do_DES(strMidDest1,strRKey,strMidDest2,'d');
//        Do_DES(strMidDest2,strLKey,strMidDest1,'e');
//
//        memcpy(strDest,strMidDest1,16);
//    }
//    else
//    {
//        memset(strBCDKey,0,sizeof(strBCDKey));
//        memcpy(strBCDKey,strKey,32);
//
//        memset(strLKey,0,sizeof(strLKey));
//        memcpy(strLKey,strBCDKey,16);
//        memset(strRKey,0,sizeof(strRKey));
//        memcpy(strRKey,strBCDKey+16,16);
//
//        Do_DES(strSrc,strLKey,strMidDest1,'d');
//        Do_DES(strMidDest1,strRKey,strMidDest2,'e');
//        Do_DES(strMidDest2,strLKey,strMidDest1,'d');
//
//        memcpy(strDest,strMidDest1,16);
//    }
//
//    return 0;
//}
//
///*************************************************************************
//*字符串转换为ASCII码，对输入的字节串作BCD编码扩展
//*************************************************************************/
//int ByteToBCD(unsigned char* bytes, int count,unsigned char* strBCD)
//{
//    unsigned char cTmp;
//    int i;
//
//    for(i=0; i<count; i++)
//    {
//        cTmp = (bytes[i] & 0xF0) >> 4;
//        strBCD[i*2] = (cTmp > 9) ? cTmp - 10 + 'A' : cTmp + '0';
//        cTmp = bytes[i] & 0x0F;
//        strBCD[i*2+1] = (cTmp > 9) ? cTmp - 10 + 'A' : cTmp + '0';
//    }
//
//    return (count*2);
//}
//
//
///*************************************************************************
//*ASCII码转换为字符串，把输入的BCD编码串还原成字节串
//*************************************************************************/
//int BCDToByte(unsigned char* strBCD, int count, unsigned char* bytes)
//{
//    unsigned char cTmp;
//    int i;
//
//    for(i=0; i<count/2; i++)
//    {
//        cTmp = strBCD[i*2];
//        if(cTmp >= 'A' && cTmp <= 'F')
//            cTmp = cTmp - 'A' + 10;
//        else if(cTmp >= 'a' && cTmp <= 'f')
//            cTmp = cTmp - 'a' + 10;
//        else
//            cTmp &= 0x0F;
//        bytes[i] = cTmp << 4;
//        cTmp = strBCD[i*2+1];
//        if(cTmp >= 'A' && cTmp <= 'F')
//            cTmp = cTmp - 'A' + 10;
//        else if(cTmp >= 'a' && cTmp <= 'f')
//            cTmp = cTmp - 'a' + 10;
//        else
//            cTmp &= 0x0F;
//        bytes[i] += cTmp;
//    }
//
//    return (count/2);
//}
//
////把字节串变成比特串
//int ByteToBit(unsigned char* bytes, int count, unsigned char* strBit)
//{
//    unsigned char cTmp;
//    int i,j;
//
//    for(i=0; i<count; i++)
//    {
//        cTmp = 0x80;
//        for(j=0; j<8; j++)
//        {
//            strBit[i*8+j] = (bytes[i] & cTmp) >> (7-j);
//            cTmp = cTmp >> 1;
//        }
//    }
//
//    return (count*8);
//}
//
////把比特串变成字节串
//int BitToByte(unsigned char* strBit, int count, unsigned char* bytes)
//{
//    unsigned char cTmp;
//    int i,j;
//
//    for(i=0; i<(count/8); i++)
//    {
//        cTmp = 0x00;
//        for(j=0; j<8; j++)
//        {
//            cTmp += (strBit[i*8+j] << (7-j));
//        }
//        bytes[i] = cTmp;
//    }
//
//    return (count/8);
//}
//
////做异或操作
//int Do_XOR(unsigned char* strSrc, int count, unsigned char* strDest)
//{
//    int i;
//
//    if(strSrc == NULL || strDest == NULL)
//        return -1;
//
//    for(i=0; i<count; i++)
//        strDest[i] ^= strSrc[i];
//
//    return 0;
//}
//
////des算法PC-1变换，把64比特的密钥K变换成56比特
//int Des_PC_1(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<56; i++)
//        strOut[i] = strIn[pc_1_table[i]-1];
//
//    return 56;
//}
//
////des算法PC-2变换，把56比特变换成48比特
//int Des_PC_2(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<48; i++)
//        strOut[i] = strIn[pc_2_table[i]-1];
//
//    return 48;
//}
//
////des算法的循环左移位运算
//int Des_LS(unsigned char* strIn, int count, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<28; i++)
//        strOut[i] = strIn[(i+count)];
//
//    return 28;
//}
//
////des算法中通过父密钥产生16个48比特位的子密钥
//int Des_GenSubKey(unsigned char* strKey, unsigned char strSubKey[16][48+1])
//{
//    unsigned char tmp[56+1],C0[28+1],D0[28+1],Cx[28+1],Dx[28+1];
//    int i,j;
//
//    memset(tmp,0,sizeof(tmp));
//    memset(C0,0,sizeof(C0));
//    memset(D0,0,sizeof(D0));
//    memset(Cx,0,sizeof(Cx));
//    memset(Dx,0,sizeof(Dx));
//
//    Des_PC_1(strKey, tmp);
//
//    memcpy(C0,tmp,28);
//    memcpy(D0,tmp+28,28);
//
//    for(i=0; i<16; i++)
//    {
//        Des_LS(C0,ls_num_table[i],Cx);
//        Des_LS(D0,ls_num_table[i],Dx);
//        memcpy(tmp,Cx,28);
//        memcpy(tmp+28,Dx,28);
//        Des_PC_2(tmp,strSubKey[i]);
//        memcpy(C0,Cx,28);
//        memcpy(D0,Dx,28);
//    }
//
//    return 0;
//}
//
////des算法IP置换
//int Des_IP(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<64; i++)
//        strOut[i] = strIn[ip_table[i]-1];
//
//    return 64;
//}
//
////des算法IP-1置换
//int Des_IP_1(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<64; i++)
//        strOut[i] = strIn[ip_1_table[i]-1];
//
//    return 64;
//}
//
////des算法e变换，将32比特变成48比特
//int Des_E(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<48; i++)
//        strOut[i] = strIn[e_table[i]-1];
//
//    return 48;
//}
//
////des算法P变换
//int Des_P(unsigned char* strIn, unsigned char* strOut)
//{
//    int i;
//
//    for(i=0; i<32; i++)
//        strOut[i] = strIn[p_table[i]-1];
//
//    return 32;
//}
//
///*************************************************************************
//*des算法S盒变换
//*************************************************************************/
//int Des_S_Box(unsigned char* strIn, int nSBox, unsigned char* strOut)
//{
//    int x,y,i,nValue;
//    unsigned char c;
//
//    if(nSBox < 1 || nSBox > 8)
//        return -1;
//
//    x = strIn[0] * 2 + strIn[5];
//    y = strIn[1] * 8 + strIn[2] * 4 + strIn[3] * 2 + strIn[4];
//
//    nValue = s_box_table[nSBox-1][x][y];
//    c = 0x08;
//    for(i=0; i<4; i++)
//    {
//        strOut[i] = (nValue & c) >> (3 - i);
//        c = c >> 1;
//    }
//
//    return 4;
//}
//
///*************************************************************************
//*des算法F函数，对Ri-1和Ki进行运算
//*************************************************************************/
//int Des_F(unsigned char* strR, unsigned char* strK, unsigned char* strOut)
//{
//    int i,j,k;
//    unsigned char strAftE[48],strPreP[32],sbIn[8][6],sbOut[8][4];
//
//    for(i=0; i<48; i++)
//        strAftE[i] = strR[e_table[i]-1];
//    Do_XOR(strK, 48, strAftE);
//
//    for(i=0; i<8; i++)
//        for(j=0; j<6; j++)
//            sbIn[i][j] = strAftE[i*6+j];
//
//    for(i=0; i<8; i++)
//        Des_S_Box(sbIn[i], i+1, sbOut[i]);
//
//    for(i=0; i<32; i++)
//        strPreP[i] = sbOut[i/4][i%4];
//    Des_P(strPreP, strOut);
//
//    return 32;
//}
//
///*************************************************************************
//*从字符串的左边截取n个字符
//*************************************************************************/
//char* leftFetch(char *dst,char *src, int n)
//{
//    char *p = src;
//    char *q = dst;
//    int len = strlen(src);
//    if(n>len) n = len;
//    /*p += (len-n);*/   /*从右边第n个字符开始*/
//    while(n--) *(q++) = *(p++);
//    *(q++)='\0'; /*有必要吗？很有必要*/
//    return dst;
//}
//
//
//
///*************************************************************************
//*从字符串的右边截取n个字符
//*************************************************************************/
//char* rightFetch(char *dst,char *src, int n)
//{
//    char *p = src;
//    char *q = dst;
//    int len = strlen(src);
//    if(n>len) n = len;
//    p += (len-n);   /*从右边第n个字符开始*/
//    while(*(q++) = *(p++));
//    return dst;
//}
//
///*************************************************************************
//*从字符串的中间截取n个字符
//*************************************************************************/
//char * midFetch(char *dst,char *src, int n,int m) /*n为长度，m为位置*/
//{
//    char *p = src;
//    char *q = dst;
//    int len = strlen(src);
//    if(n>len) n = len-m;    /*从第m个到最后*/
//    if(m<0) m=0;    /*从第一个开始*/
//    if(m>len) return NULL;
//    p += m;
//    while(n--) *(q++) = *(p++);
//    *(q++)='\0'; /*有必要吗？很有必要*/
//    return dst;
//}
//
//
//
//
//
//
//
