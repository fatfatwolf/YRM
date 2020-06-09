package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * @description:
 * @author: luyafeng
 * @data: 2017/6/14.
 */

public class MyBalanceBean extends BaseBean {


    /**
     * msg :
     * numberUnits :
     * obj : {"b":"1","info":"提现申请已受理","ins":"正常","lag":true,"pcid":18239,"status":1}
     * sessionExpire : false
     * success : true
     */

    private String msg;
    private String numberUnits;
    private ObjBean obj;
    private boolean sessionExpire;
    private boolean success;

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
         * b : 1
         * info : 提现申请已受理
         * ins : 正常
         * lag : true
         * pcid : 18239
         * status : 1
         */

        private String b;
        private String info;
        private String ins;
        private boolean lag;
        private int pcid;
        private int status;

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getIns() {
            return ins;
        }

        public void setIns(String ins) {
            this.ins = ins;
        }

        public boolean isLag() {
            return lag;
        }

        public void setLag(boolean lag) {
            this.lag = lag;
        }

        public int getPcid() {
            return pcid;
        }

        public void setPcid(int pcid) {
            this.pcid = pcid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
