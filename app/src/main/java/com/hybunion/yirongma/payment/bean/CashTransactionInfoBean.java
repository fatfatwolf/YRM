package com.hybunion.yirongma.payment.bean;

import java.util.List;

/**
 * 提现记录实体类
 *
 */
public class CashTransactionInfoBean {

    /**
     * countTxnAmount : 22
     * msg : 查询成功！
     * numberUnits :
     * obj : [{"CASHAMT":0.02,"CASHDATE":"20170614 18:53:10","CASHFEE":0,"CASHSTATUS":1},{"CASHAMT":100,"CASHDATE":"20170614 17:42:31","CASHFEE":2.07,"CASHSTATUS":-1},{"CASHAMT":100,"CASHDATE":"20170614 17:42:29","CASHFEE":2.07,"CASHSTATUS":-1},{"CASHAMT":100,"CASHDATE":"20170614 17:42:26","CASHFEE":2.07,"CASHSTATUS":-1},{"CASHAMT":100,"CASHDATE":"20170614 17:42:23","CASHFEE":2.07,"CASHSTATUS":-1},{"CASHAMT":10,"CASHDATE":"20170614 17:42:15","CASHFEE":2.01,"CASHSTATUS":-1},{"CASHAMT":10,"CASHDATE":"20170614 17:09:00","CASHFEE":2.01,"CASHSTATUS":-1},{"CASHAMT":10,"CASHDATE":"20170614 17:08:58","CASHFEE":2.01,"CASHSTATUS":-1},{"CASHAMT":10,"CASHDATE":"20170614 17:08:55","CASHFEE":2.01,"CASHSTATUS":-1},{"CASHAMT":10,"CASHDATE":"20170614 17:08:52","CASHFEE":2.01,"CASHSTATUS":-1}]
     * sessionExpire : false
     * success : true
     */

    private String countTxnAmount;
    private String msg;
    private String numberUnits;
    private boolean sessionExpire;
    private boolean success;
    private List<ObjBean> obj;

    public String getCountTxnAmount() {
        return countTxnAmount;
    }

    public void setCountTxnAmount(String countTxnAmount) {
        this.countTxnAmount = countTxnAmount;
    }

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

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * CASHAMT : 0.02
         * CASHDATE : 20170614 18:53:10
         * CASHFEE : 0
         * CASHSTATUS : 1
         */

        private double CASHAMT;
        private String CASHDATE;
        private double CASHFEE;
        private String CASHSTATUS;

        public double getCASHAMT() {
            return CASHAMT;
        }

        public void setCASHAMT(double CASHAMT) {
            this.CASHAMT = CASHAMT;
        }

        public String getCASHDATE() {
            return CASHDATE;
        }

        public void setCASHDATE(String CASHDATE) {
            this.CASHDATE = CASHDATE;
        }

        public double getCASHFEE() {
            return CASHFEE;
        }

        public void setCASHFEE(double CASHFEE) {
            this.CASHFEE = CASHFEE;
        }

        public String getCASHSTATUS() {
            return CASHSTATUS;
        }

        public void setCASHSTATUS(String CASHSTATUS) {
            this.CASHSTATUS = CASHSTATUS;
        }
    }
}
