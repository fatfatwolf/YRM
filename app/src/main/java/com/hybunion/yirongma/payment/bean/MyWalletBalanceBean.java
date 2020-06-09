package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * @description: 钱包余额实体类
 * @author: luyafeng
 * @data: 2017/6/13.
 */

public class MyWalletBalanceBean extends BaseBean {


    /**
     * msg : 查询成功！
     * numberUnits :
     * obj : {"BALANCE":55259,"CASHAMT":10,"MONEY":15.87,"STATUS":1,"TXNAMT":30}
     * sessionExpire : false
     * success : true
     */

    private String msg;
    private String numberUnits;
    private ObjBean obj;
    private boolean sessionExpire;
    private boolean success;
    public double balance;
    public String minCash, totalCash;

    // HRT 钱包用
    public double
            money; // 可提现金额
//    public double txnamt,cashamt;  // 后台返回了但是没用

    public String cashstatus;    // 实时到账状态标识。 0-审核成功  1-审核失败  2-审核中


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNumberUnits() {
        return numberUnits;
    }

    public void setNumberUnits(String numberUnits) {
        this.numberUnits = numberUnits;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public boolean isSessionExpire() {
        return sessionExpire;
    }

    public void setSessionExpire(boolean sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class ObjBean {
        /**
         * BALANCE : 55259
         * CASHAMT : 10
         * MONEY : 15.87
         * STATUS : 1
         * TXNAMT : 30
         */
        private int STATUS;

        public int getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(int STATUS) {
            this.STATUS = STATUS;
        }
    }
}
