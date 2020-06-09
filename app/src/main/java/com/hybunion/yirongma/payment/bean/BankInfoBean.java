package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;

/**
 * 功能描述：银行卡信息
 * 编写人： myy
 * 创建时间：2017/10/17
 */
public class BankInfoBean extends BaseBean implements Serializable {
    private String paymentBankImg;
    private String paymentBank;
    private String paymentLine;
    private String cardType;
    private String payBankId;
    public String getPaymentBank() {
        return paymentBank;
    }

    public void setPaymentBank(String paymentBank) {
        this.paymentBank = paymentBank;
    }

    public String getPaymentBankImg() {
        return paymentBankImg;
    }

    public void setPaymentBankImg(String paymentBankImg) {
        this.paymentBankImg = paymentBankImg;
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

    public String getPayBankId() {
        return payBankId;
    }

    public void setPayBankId(String payBankId) {
        this.payBankId = payBankId;
    }
}
