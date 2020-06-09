package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 查询审核状态实体类
 * Created by android on 2016/12/14.
 */

public class BankCardApproveStatusBean extends BaseBean {

    /**
     * msg : 查询成功！
     * numberUnits :
     * obj : {"rows":[{"APPROVESTATUS":"Y","BANKACCNAME":"邓超界","BANKACCNO":"6226200103012184","BANKBRANCH":"民生银行","MID":"864000253114633","PAYBANKID":"305100000013"}],"total":1}
     * sessionExpire : false
     * success : true
     */

    private String msg;
    private String numberUnits;
    private ObjEntity obj;
    private boolean sessionExpire;
    private boolean success;

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
         * rows : [{"APPROVESTATUS":"Y","BANKACCNAME":"邓超界","BANKACCNO":"6226200103012184","BANKBRANCH":"民生银行","MID":"864000253114633","PAYBANKID":"305100000013"}]
         * total : 1
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
             * APPROVESTATUS : Y
             * BANKACCNAME : 邓超界
             * BANKACCNO : 6226200103012184
             * BANKBRANCH : 民生银行
             * MID : 864000253114633
             * PAYBANKID : 305100000013
             */
            private String ACCTYPE;
            private String APPROVESTATUS;
            private String BANKACCNAME;
            private String BANKACCNO;
            private String BANKBRANCH;
            private String MID;
            private String PAYBANKID;

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

            public void setMID(String MID) {
                this.MID = MID;
            }

            public void setPAYBANKID(String PAYBANKID) {
                this.PAYBANKID = PAYBANKID;
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

            public String getMID() {
                return MID;
            }

            public String getPAYBANKID() {
                return PAYBANKID;
            }

            public String getACCTYPE() {
                return ACCTYPE;
            }

            public void setACCTYPE(String ACCTYPE) {
                this.ACCTYPE = ACCTYPE;
            }
        }
    }
}
