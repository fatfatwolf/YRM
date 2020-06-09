package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by admin on 2018/1/9.
 */

public class ValueCardsListBean extends BaseBean<List<ValueCardsListBean.DataBean>> {
      private boolean hasNext;
      private String msg;
      private String memId;
    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public static class DataBean {
        private String balance;       //余额（次数）
        private String cardName;    //名称
        private String cardNo;        //卡号
        private String cardType;  //卡类型（0：次卡；1：金额卡；2：充送卡；3：折扣卡；4：任意金额卡）
        private String discountRate; //折扣
        private String expireDate;    //到期日

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(String cardName) {
            this.cardName = cardName;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(String discountRate) {
            this.discountRate = discountRate;
        }

        public String getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(String expireDate) {
            this.expireDate = expireDate;
        }
    }
}