package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * @description: 签约信息实体类
 * @author: luyafeng
 * @data: 2017/6/15.
 */

public class SingInfoBean extends BaseBean {


    /**
     * msg : 查询成功
     * numberUnits :
     * obj : {"data":{"cycle":"","hrt_mid":"HRTPAY000000197","minfo1":"","minfo2":"","preSetTime":"","quotaAmt":"","secondRate":"","settmethod":""},"msg":"success","status":1}
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
         * data : {"cycle":"","hrt_mid":"HRTPAY000000197","minfo1":"","minfo2":"","preSetTime":"","quotaAmt":"","secondRate":"","settmethod":""}
         * msg : success
         * status : 1
         */

        private DataBean data;
        private String msg;
        private int status;

        public DataBean getData() {
            return data;
        }

        public void setDataX(DataBean data) {
            this.data = data;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getStatus() {
            return status;
        }

        public void setStatusX(int status) {
            this.status = status;
        }

        public static class DataBean {
            /**
             * cycle :
             * hrt_mid : HRTPAY000000197
             * minfo1 :
             * minfo2 :
             * preSetTime :
             * quotaAmt :
             * secondRate :
             * settmethod :
             */

            private String cycle;
            private String hrt_mid;
            private String minfo1;
            private String minfo2;
            private String preSetTime;
            private String quotaAmt;
            private String secondRate;
            private String settmethod;
            private String scanRate;

            public String getCycle() {
                return cycle;
            }

            public void setCycle(String cycle) {
                this.cycle = cycle;
            }

            public String getHrt_mid() {
                return hrt_mid;
            }

            public void setHrt_mid(String hrt_mid) {
                this.hrt_mid = hrt_mid;
            }

            public String getMinfo1() {
                return minfo1;
            }

            public void setMinfo1(String minfo1) {
                this.minfo1 = minfo1;
            }

            public String getMinfo2() {
                return minfo2;
            }

            public void setMinfo2(String minfo2) {
                this.minfo2 = minfo2;
            }

            public String getPreSetTime() {
                return preSetTime;
            }

            public void setPreSetTime(String preSetTime) {
                this.preSetTime = preSetTime;
            }

            public String getQuotaAmt() {
                return quotaAmt;
            }

            public void setQuotaAmt(String quotaAmt) {
                this.quotaAmt = quotaAmt;
            }

            public String getSecondRate() {
                return secondRate;
            }

            public void setSecondRate(String secondRate) {
                this.secondRate = secondRate;
            }

            public String getSettmethod() {
                return settmethod;
            }

            public void setSettmethod(String settmethod) {
                this.settmethod = settmethod;
            }

            public String getScanRate() {
                return scanRate;
            }

            public void setScanRate(String scanRate) {
                this.scanRate = scanRate;
            }
        }
    }
}
