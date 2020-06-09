package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by win7 on 2016/7/29.
 */
public class AllBankBean extends BaseBean<List<AllBankBean.BankInfo>> {

    public class BankInfo implements Serializable{
        private String paymentBankImg;
        private String paymentBank;
        private String paymentLine;

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
    }
}
