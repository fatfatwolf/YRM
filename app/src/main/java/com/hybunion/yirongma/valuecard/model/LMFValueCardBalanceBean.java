package com.hybunion.yirongma.valuecard.model;

import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/8/30
 * @email freemars@yeah.net
 * @description
 */

public class LMFValueCardBalanceBean {
    private List<ValueCardBean> data;

    public List<ValueCardBean> getData() {
        return data;
    }

    public void setData(List<ValueCardBean> data) {
        this.data = data;
    }

    public static  class ValueCardBean{
        private String balance;
        private String cardNo;
        private String cardType;
        private String phoneNum;
        private String typeName;
        private String userAlias;
        private String memCode;
        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
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

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getUserAlias() {
            return userAlias;
        }

        public void setUserAlias(String userAlias) {
            this.userAlias = userAlias;
        }

        public String getMemCode() {
            return memCode;
        }

        public void setMemCode(String memCode) {
            this.memCode = memCode;
        }
    }
}
