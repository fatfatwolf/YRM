package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * Created by android on 2016/12/12.
 */

public class ModifyCardMessageBean extends BaseBean {
    //    {"status":"0","message":"查询成功","paymentLine":"104100000004","paymentBank":"中国银行"
//        ,"paymentBankImg":"http://www.hybchina.com.cn/CubeCoreConsole/CubeImages/bank/zhongguo.png","cardType":"借记卡"}
    private String paymentBankImg;//图标
    private String paymentBank;//银行名称
    private String paymentLine;//系统行号
    private String cardType;//卡的类型


    public String getPaymentBankImg() {
        return paymentBankImg;
    }

    public void setPaymentBankImg(String paymentBankImg) {
        this.paymentBankImg = paymentBankImg;
    }

    public String getPaymentBank() {
        return paymentBank;
    }

    public void setPaymentBank(String paymentBank) {
        this.paymentBank = paymentBank;
    }

    public String getPaymentLine() {
        return paymentLine;
    }

    public void setPaymentLine(String paymentLine) {
        this.paymentLine = paymentLine;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
