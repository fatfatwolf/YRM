package com.hybunion.yirongma.payment.utils;

public class Constants {

    public final static String TEST_CODE = "(001)";

    public static final String ADDR = "address";

    public static final String BNO = "bno";

    public static final String RNAME = "rname";

    public static final String AREATYPE = "areaType";

    public final static String MID = "mid";                                                          //商户编号

    public final static String MERCHANTID = "merchantID";

    public final static String MERCHANT_NAME = "merchantName";    //商户名称
    public final static String CONTACT_PHONE = "contactPhone";    //联系电话

    public final static String Notice_NUM = "notice_num";  //notice_num

    public final static String Name = "bankAccName";  //姓名

    public final static String LEGAL_NUM = "legalNum";  //身份证号

    public final static String ACCNUM = "accNum";  //入账人身份证号


    public enum  PAY_WAY{
        WX,
        ZFB,
        KJZF,
        QQ,

        YL,
        BD,
        JD,
        CASH
    }
}
