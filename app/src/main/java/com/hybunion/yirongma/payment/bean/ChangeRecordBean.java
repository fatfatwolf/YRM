package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by android on 2016/12/14.
 */

public class ChangeRecordBean extends BaseBean {

    /**
     * msg : 查询成功！
     * numberUnits :
     * obj : {"rows":[{"APPROVESTATUS":"Z","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"农业银行","MAINTAINDATE":"2016-12-14 16:54:08"},{"APPROVESTATUS":"K","BANKACCNAME":"邓吵架","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 15:35:41","PROCESSCONTEXT":"2222"},{"APPROVESTATUS":"K","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"中国银行","MAINTAINDATE":"2016-12-14 11:51:49","PROCESSCONTEXT":"22222"},{"APPROVESTATUS":"K","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"农业银行","MAINTAINDATE":"2016-12-14 11:32:37","PROCESSCONTEXT":"333"},{"APPROVESTATUS":"K","BANKACCNAME":"我","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 11:32:35","PROCESSCONTEXT":"333"},{"APPROVESTATUS":"K","BANKACCNAME":"我","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 11:29:57","PROCESSCONTEXT":"222"},{"APPROVESTATUS":"K","BANKACCNAME":"天天","BANKACCNO":"6226201500004196","BANKBRANCH":"民生银行","MAINTAINDATE":"2016-12-14 11:15:25","PROCESSCONTEXT":"222"}],"total":7}
     * sessionExpire : false
     * success : true
     */

    private String msg;
    private String numberUnits;
    private ObjEntity obj;
    private boolean sessionExpire;
    private boolean success;


    @Override
    public String getStatus() {
        return success ? "0" : "1";
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNumberUnits(String numberUnits) {
        this.numberUnits = numberUnits;
    }

    public void setObj(ObjEntity obj) {
        this.obj = obj;
    }

    public void setSessionExpire(boolean sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public String getNumberUnits() {
        return numberUnits;
    }

    public ObjEntity getObj() {
        return obj;
    }

    public boolean getSessionExpire() {
        return sessionExpire;
    }

    public boolean getSuccess() {
        return success;
    }

    public static class ObjEntity {
        /**
         * rows : [{"APPROVESTATUS":"Z","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"农业银行","MAINTAINDATE":"2016-12-14 16:54:08"},{"APPROVESTATUS":"K","BANKACCNAME":"邓吵架","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 15:35:41","PROCESSCONTEXT":"2222"},{"APPROVESTATUS":"K","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"中国银行","MAINTAINDATE":"2016-12-14 11:51:49","PROCESSCONTEXT":"22222"},{"APPROVESTATUS":"K","BANKACCNAME":"王志超","BANKACCNO":"6228480018556673673","BANKBRANCH":"农业银行","MAINTAINDATE":"2016-12-14 11:32:37","PROCESSCONTEXT":"333"},{"APPROVESTATUS":"K","BANKACCNAME":"我","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 11:32:35","PROCESSCONTEXT":"333"},{"APPROVESTATUS":"K","BANKACCNAME":"我","BANKACCNO":"6222020504008814247","BANKBRANCH":"工商银行","MAINTAINDATE":"2016-12-14 11:29:57","PROCESSCONTEXT":"222"},{"APPROVESTATUS":"K","BANKACCNAME":"天天","BANKACCNO":"6226201500004196","BANKBRANCH":"民生银行","MAINTAINDATE":"2016-12-14 11:15:25","PROCESSCONTEXT":"222"}]
         * total : 7
         */

        private int total;
        private List<RowsEntity> rows;

        public void setTotal(int total) {
            this.total = total;
        }

        public void setRows(List<RowsEntity> rows) {
            this.rows = rows;
        }

        public int getTotal() {
            return total;
        }

        public List<RowsEntity> getRows() {
            return rows;
        }

        public static class RowsEntity {
            /**
             * APPROVESTATUS : Z
             * BANKACCNAME : 王志超
             * BANKACCNO : 6228480018556673673
             * BANKBRANCH : 农业银行
             * MAINTAINDATE : 2016-12-14 16:54:08
             */

            private String APPROVESTATUS;
            private String BANKACCNAME;
            private String BANKACCNO;
            private String BANKBRANCH;
            private String MAINTAINDATE;
            private String PROCESSCONTEXT;

            public String getPROCESSCONTEXT() {
                return PROCESSCONTEXT;
            }

            public void setPROCESSCONTEXT(String PROCESSCONTEXT) {
                this.PROCESSCONTEXT = PROCESSCONTEXT;
            }

            public void setAPPROVESTATUS(String APPROVESTATUS) {
                this.APPROVESTATUS = APPROVESTATUS;
            }

            public void setBANKACCNAME(String BANKACCNAME) {
                this.BANKACCNAME = BANKACCNAME;
            }

            public void setBANKACCNO(String BANKACCNO) {
                this.BANKACCNO = BANKACCNO;
            }

            public void setBANKBRANCH(String BANKBRANCH) {
                this.BANKBRANCH = BANKBRANCH;
            }

            public void setMAINTAINDATE(String MAINTAINDATE) {
                this.MAINTAINDATE = MAINTAINDATE;
            }

            public String getAPPROVESTATUS() {
                return APPROVESTATUS;
            }

            public String getBANKACCNAME() {
                return BANKACCNAME;
            }

            public String getBANKACCNO() {
                return BANKACCNO;
            }

            public String getBANKBRANCH() {
                return BANKBRANCH;
            }

            public String getMAINTAINDATE() {
                return MAINTAINDATE;
            }
        }
    }
}
