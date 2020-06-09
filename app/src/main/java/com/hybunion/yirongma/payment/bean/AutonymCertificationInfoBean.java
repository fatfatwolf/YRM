package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * Created by win7 on 2016/8/2.
 */
public class AutonymCertificationInfoBean extends BaseBean {
    private String numberUnits;
    private boolean sessionExpire;
    private boolean success;
    private String msg;
    private ObjModel obj;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjModel getObj() {
        return obj;
    }

    public void setObj(ObjModel obj) {
        this.obj = obj;
    }

    public class ObjModel {
        private String total;
        private RowModel rows;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public RowModel getRows() {
            return rows;
        }

        public void setRows(RowModel rows) {
            this.rows = rows;
        }
    }

    public class RowModel {
        private String bankBranch;
        private String contactPerson;
        private String mid;
        private String bankAccNo;
        private String bankAccName;
        private String payBankId;
        private String accType;
        private String legalPerson;
        private String legalNum;
        private String ACCTYPE;
        private String raddr;
        private String rname;
        private String APPROVESTATUS;
        private String bno;
        private String areaType;
        private String contactPhone;
        private String accNum;
        private String PROCESSCONTEXT;
        private String minAmount;
        private String isForceRemark;

        public String getBankBranch() {
            return bankBranch;
        }

        public void setBankBranch(String bankBranch) {
            this.bankBranch = bankBranch;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getBankAccNo() {
            return bankAccNo;
        }

        public void setBankAccNo(String bankAccNo) {
            this.bankAccNo = bankAccNo;
        }

        public String getBankAccName() {
            return bankAccName;
        }

        public void setBankAccName(String bankAccName) {
            this.bankAccName = bankAccName;
        }

        public String getPayBankId() {
            return payBankId;
        }

        public void setPayBankId(String payBankId) {
            this.payBankId = payBankId;
        }

        public String getAccType() {
            return accType;
        }

        public void setAccType(String accType) {
            this.accType = accType;
        }

        public String getLegalPerson() {
            return legalPerson;
        }

        public void setLegalPerson(String legalPerson) {
            this.legalPerson = legalPerson;
        }

        public String getLegalNum() {
            return legalNum;
        }

        public void setLegalNum(String legalNum) {
            this.legalNum = legalNum;
        }

        public String getACCTYPE() {
            return ACCTYPE;
        }

        public void setACCTYPE(String aCCTYPE) {
            ACCTYPE = aCCTYPE;
        }

        public String getRname() {
            return rname;
        }

        public void setRname(String rname) {
            this.rname = rname;
        }

        public String getAPPROVESTATUS() {
            return APPROVESTATUS;
        }

        public void setAPPROVESTATUS(String aPPROVESTATUS) {
            APPROVESTATUS = aPPROVESTATUS;
        }

        public String getRaddr() {
            return raddr;
        }

        public void setRaddr(String raddr) {
            this.raddr = raddr;
        }

        public String getBno() {
            return bno;
        }

        public void setBno(String bno) {
            this.bno = bno;
        }

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getPROCESSCONTEXT() {
            return PROCESSCONTEXT;
        }

        public void setPROCESSCONTEXT(String PROCESSCONTEXT) {
            this.PROCESSCONTEXT = PROCESSCONTEXT;
        }

        public String getAccNum() {
            return accNum;
        }

        public void setAccNum(String accNum) {
            this.accNum = accNum;
        }

        public String getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(String minAmount) {
            this.minAmount = minAmount;
        }

        public String getIsForceRemark() {
            return isForceRemark;
        }

        public void setIsForceRemark(String isForceRemark) {
            this.isForceRemark = isForceRemark;
        }
    }
  /*private Row obj;
    public Row getObj() {
        return obj;
    }

    public void setObj(Row obj) {
        this.obj = obj;
    }

    public class Row {
        private AutonymCertificationInfo[] rows;
        private int total;
        public AutonymCertificationInfo[] getRows() {
            return rows;
        }

        public void setRows(AutonymCertificationInfo[] rows) {
            this.rows = rows;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public class AutonymCertificationInfo {
            private String bankAccName;
            private String bankAccNo;
            private String bankBranch;
            private String bno;
            private String APPROVESTATUS;
            private String contactPhone;
            private String legalNum;
            private String mid;
            private String raddr;
            private String rname;
            private String areaType;
            //add by king  2017-04-11
            private int depositFlag;   //是否需要扣押金      0  不需要扣押金    1 需要扣押金
            private int depositAmt;   //押金金额        0   没有押金      其他  押金金额

            public String getBankAccName() {
                return bankAccName;
            }

            public void setBankAccName(String bankAccName) {
                this.bankAccName = bankAccName;
            }

            public String getBankAccNo() {
                return bankAccNo;
            }

            public void setBankAccNo(String bankAccNo) {
                this.bankAccNo = bankAccNo;
            }

            public String getBankBranch() {
                return bankBranch;
            }

            public void setBankBranch(String bankBranch) {
                this.bankBranch = bankBranch;
            }

            public String getBno() {
                return bno;
            }

            public void setBno(String bno) {
                this.bno = bno;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public String getLegalNum() {
                return legalNum;
            }

            public void setLegalNum(String legalNum) {
                this.legalNum = legalNum;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getRaddr() {
                return raddr;
            }

            public void setRaddr(String raddr) {
                this.raddr = raddr;
            }

            public String getRname() {
                return rname;
            }

            public void setRname(String rname) {
                this.rname = rname;
            }

            public int getDepositFlag() {
                return depositFlag;
            }

            public void setDepositFlag(int depositFlag) {
                this.depositFlag = depositFlag;
            }

            public int getDepositAmt() {
                return depositAmt;
            }

            public void setDepositAmt(int depositAmt) {
                this.depositAmt = depositAmt;
            }

            public String getAreaType() {
                return areaType;
            }

            public void setAreaType(String areaType) {
                this.areaType = areaType;
            }

            public String getAPPROVESTATUS() {
                return APPROVESTATUS;
            }

            public void setAPPROVESTATUS(String APPROVESTATUS) {
                this.APPROVESTATUS = APPROVESTATUS;
            }
        }
    }*/
}
