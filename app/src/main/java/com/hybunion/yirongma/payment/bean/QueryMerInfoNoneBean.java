package com.hybunion.yirongma.payment.bean;

public class QueryMerInfoNoneBean {
    private String status;
    private String message;
    private String loginType;
    private String ACCTYPE;
    private String areaType;
    private String bno;
    private String contactPerson;
    private String legalNum;
    private String legalPerson;
    private String mid;
    private String raddr;
    private String rname;
    private String APPROVESTATUS;
    private String PROCESSCONTEXT;
    private String accNum;
    private String merchantName;
    private String merchantNo;
    private String isLiMaFuMerchant;
    private String jhmid;
    private String isLmfHead;
    private String agentId;
    private String isAccType;
    private String topAgentId;
    private String topAgentName;
    private String topAgentPhone;
    private String isJhMidBindTid;
    private String bankAccName;
    private String bankAccNo;
    private String bankBranch;
    private String contactPhone;
    private String payBankId;
    private String merchantID;
    private String rePassword;
    private String businessType;  // 是否为钱包商户（代理“商”计划）
    private String isHRTWallet; // 是否为 HRT 钱包商户
    private String minAmount;//最低付款金额
    private String isForceRemark;//是否开启强制备注,0是，1否
    private String isReadProtocol;//是否阅读协议 N 否 Y 是
    public String yxBeiSaoBoBao;//是否允许插件播报
    public String isShowLoans;//0满足条件，1不满足条件

    // 非老板 用
    private String storeName;
    private String storePhone;
    private String storeId;
    private String empName;
    private String staffId;
    private String staffType;
    public String couponAdmin;//店长是否是管理员

    private String isCoupon;  // 是否加入商圈  0-是  1-不是    是商圈门店就展示优惠券入口

    public String vcSale;//0 是关闭充值 1是开启充值 2是没有创建过储值卡

    public String getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(String isCoupon) {
        this.isCoupon = isCoupon;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffType() {
        return staffType;
    }

    public void setStaffType(String staffType) {
        this.staffType = staffType;
    }

    public String getIsHRTWallet() {
        return isHRTWallet;
    }

    public void setIsHRTWallet(String isHRTWallet) {
        this.isHRTWallet = isHRTWallet;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getACCTYPE() {
        return ACCTYPE;
    }

    public void setACCTYPE(String ACCTYPE) {
        this.ACCTYPE = ACCTYPE;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getBno() {
        return bno;
    }

    public void setBno(String bno) {
        this.bno = bno;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLegalNum() {
        return legalNum;
    }

    public void setLegalNum(String legalNum) {
        this.legalNum = legalNum;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
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

    public String getAPPROVESTATUS() {
        return APPROVESTATUS;
    }

    public void setAPPROVESTATUS(String APPROVESTATUS) {
        this.APPROVESTATUS = APPROVESTATUS;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getIsLiMaFuMerchant() {
        return isLiMaFuMerchant;
    }

    public void setIsLiMaFuMerchant(String isLiMaFuMerchant) {
        this.isLiMaFuMerchant = isLiMaFuMerchant;
    }

    public String getJhmid() {
        return jhmid;
    }

    public void setJhmid(String jhmid) {
        this.jhmid = jhmid;
    }

    public String getIsLmfHead() {
        return isLmfHead;
    }

    public void setIsLmfHead(String isLmfHead) {
        this.isLmfHead = isLmfHead;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getIsAccType() {
        return isAccType;
    }

    public void setIsAccType(String isAccType) {
        this.isAccType = isAccType;
    }

    public String getTopAgentId() {
        return topAgentId;
    }

    public void setTopAgentId(String topAgentId) {
        this.topAgentId = topAgentId;
    }

    public String getTopAgentName() {
        return topAgentName;
    }

    public void setTopAgentName(String topAgentName) {
        this.topAgentName = topAgentName;
    }

    public String getTopAgentPhone() {
        return topAgentPhone;
    }

    public void setTopAgentPhone(String topAgentPhone) {
        this.topAgentPhone = topAgentPhone;
    }

    public String getIsJhMidBindTid() {
        return isJhMidBindTid;
    }

    public void setIsJhMidBindTid(String isJhMidBindTid) {
        this.isJhMidBindTid = isJhMidBindTid;
    }

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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPayBankId() {
        return payBankId;
    }

    public void setPayBankId(String payBankId) {
        this.payBankId = payBankId;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public String getIsReadProtocol() {
        return isReadProtocol;
    }

    public void setIsReadProtocol(String isReadProtocol) {
        this.isReadProtocol = isReadProtocol;
    }
}
