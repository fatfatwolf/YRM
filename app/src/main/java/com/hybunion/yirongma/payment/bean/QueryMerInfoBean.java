package com.hybunion.yirongma.payment.bean;

public class QueryMerInfoBean {
    private String status;
    private String message;
    private String loginType;
    private String storeName;
    private String storePhone;
    private String storeId;
    private String empName;
    private String merchantID;
    public String jhmid;
    private String staffId;
    private String staffType;
    private String rePassword;
    private String businessType;// 是否为钱包商户（代理“商”计划）
    private String isHRTWallet; // 是否为 HRT 钱包商户
    private String minAmount;//最低付款金额
    private String isForceRemark;//是否开启强制备注,0是，1否

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

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
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
}
