package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by android on 2016/12/12.
 */
public class BankCardInfoBean extends BaseBean {

    /**
     * msg : 查询成功！
     * numberUnits :
     * obj : {"rows":[{"bankAccName":"邓超界","bankAccNo":"6226200103012184","bankBranch":"民生银行","bno":" ","contactPhone":"15510771051","legalNum":"410526198803118218","mid":"864000253114633","raddr":"北京市海淀区中关村","rname":"邓超界"}],"total":1}
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
        return msg.contains("查询成功") ? "0" : "1";
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
         * rows : [{"bankAccName":"邓超界","bankAccNo":"6226200103012184","bankBranch":"民生银行","bno":" ","contactPhone":"15510771051","legalNum":"410526198803118218","mid":"864000253114633","raddr":"北京市海淀区中关村","rname":"邓超界"}]
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
             * bankAccName : 邓超界
             * bankAccNo : 6226200103012184
             * bankBranch : 民生银行
             * bno :
             * contactPhone : 15510771051
             * legalNum : 410526198803118218
             * mid : 864000253114633
             * raddr : 北京市海淀区中关村
             * rname : 邓超界
             */

            private String bankAccName;
            private String bankAccNo;
            private String bankBranch;
            private String bno;
            private String contactPhone;
            private String legalNum;
            private String mid;
            private String raddr;
            private String rname;

            public void setBankAccName(String bankAccName) {
                this.bankAccName = bankAccName;
            }

            public void setBankAccNo(String bankAccNo) {
                this.bankAccNo = bankAccNo;
            }

            public void setBankBranch(String bankBranch) {
                this.bankBranch = bankBranch;
            }

            public void setBno(String bno) {
                this.bno = bno;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public void setLegalNum(String legalNum) {
                this.legalNum = legalNum;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public void setRaddr(String raddr) {
                this.raddr = raddr;
            }

            public void setRname(String rname) {
                this.rname = rname;
            }

            public String getBankAccName() {
                return bankAccName;
            }

            public String getBankAccNo() {
                return bankAccNo;
            }

            public String getBankBranch() {
                return bankBranch;
            }

            public String getBno() {
                return bno;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public String getLegalNum() {
                return legalNum;
            }

            public String getMid() {
                return mid;
            }

            public String getRaddr() {
                return raddr;
            }

            public String getRname() {
                return rname;
            }
        }
    }
}
