package com.hybunion.yirongma.valuecard.model;

import java.io.Serializable;
import java.util.List;

/**
 * 储值卡实体类 2017/3/6.
 */
public class MerCardInfoItemBean implements Serializable {
    private String cardName; // 卡名称
    private String cardStatus; // 卡片状态
    private String cardType; // 卡片类型
    private String expireDate; // 有效期
    private String remaindNum; // 剩余次数
    private String totalNumber; // 总次数
    private List<String> rechargeRule; // 充值规则
    private String cardApplyTempId; // ID
    private List<ObjBean> ruleArry;


    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getRemaindNum() {
        return remaindNum;
    }

    public void setRemaindNum(String remaindNum) {
        this.remaindNum = remaindNum;
    }

    public String getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(String totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<String> getRechargeRule() {
        return rechargeRule;
    }

    public void setRechargeRule(List<String> rechargeRule) {
        this.rechargeRule = rechargeRule;
    }

    public String getCardApplyTempId() {
        return cardApplyTempId;
    }

    public void setCardApplyTempId(String cardApplyTempId) {
        this.cardApplyTempId = cardApplyTempId;
    }

    public List<ObjBean> getRuleArry() {
        return ruleArry;
    }

    public void setRuleArry(List<ObjBean> ruleArry) {
        this.ruleArry = ruleArry;
    }

    public static class ObjBean implements Serializable{

        /** 最大的被选中的条件数量 */
        public static final int MAX_SELECTED_COUNT = 2;

        /** 选中 */
        public static final String REC_SELECTED = "0";

        /** 未选中 */
        public static final String REC_UNSELECT = "1";

        public String ruleId;

        public String  isRec;

        public String ruleInfo;

        public String getRuleId() {
            return ruleId;
        }

        public void setRuleId(String ruleId) {
            this.ruleId = ruleId;
        }

        public boolean isRec() {
            return REC_SELECTED.equals(isRec);  //选中
        }

        public String getRuleInfo() {
            return ruleInfo;
        }

        public void setRuleInfo(String ruleInfo) {
            this.ruleInfo = ruleInfo;
        }
    }
}
