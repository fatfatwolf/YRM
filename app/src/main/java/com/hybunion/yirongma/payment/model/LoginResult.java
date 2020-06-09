package com.hybunion.yirongma.payment.model;

public class LoginResult {
	private String parentID;
	private String merchantID;
	private String merchantName;
	private String permmisionValue;// 1,有发放券的权限，2，没有此权限
	private String message;
	private String status;
	private String mid;
	private String jhmid;
	private String verifyStatus;
	private String verifyResult;
	private String isHuiShangHu;//是否是惠商户(0:是，1:非)
	private String agentId;
	private String isJuHeMerchant; //是否是聚合商户（0是1否）
	private String isHeadOffice; //0是总店; 1是分店
    private String isLiMaFuMerchant;
    private String isJhMidBindTid;
	private String isPartnerMer; //是否是立马富合伙人发展的商户 0 是  1  不是
	private String topAgentId;
	private String topAgentName;
	private String isLmfHead;
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

	public String getIsJuHeMerchant() {
		return isJuHeMerchant;
	}

	public void setIsJuHeMerchant(String isJuHeMerchant) {
		this.isJuHeMerchant = isJuHeMerchant;
	}

	public String getIsHeadOffice() {
		return isHeadOffice;
	}

	public void setIsHeadOffice(String isHeadOffice) {
		this.isHeadOffice = isHeadOffice;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public void setIsHuiShangHu(String isHuiShangHu) {
		this.isHuiShangHu = isHuiShangHu;
	}

	public String getIsHuiShangHu() {
		return isHuiShangHu;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}

	public String getMid()
	{
		return mid;
	}

	public void setMid(String mid)
	{
		this.mid = mid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPermmisionValue() {
		return permmisionValue;
	}

	public void setPermmisionValue(String permmisionValue) {
		this.permmisionValue = permmisionValue;
	}

	public String getParentID() {
		return parentID;
	}
	
	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getIsJhMidBindTid() {
		return isJhMidBindTid;
	}

	public void setIsJhMidBindTid(String isJhMidBindTid) {
		this.isJhMidBindTid = isJhMidBindTid;
	}

	public String getIsPartnerMer() {
		return isPartnerMer;
	}

	public void setIsPartnerMer(String isPartnerMer) {
		this.isPartnerMer = isPartnerMer;
	}

	public String getIsLmfHead() {
		return isLmfHead;
	}

	public void setIsLmfHead(String isLmfHead) {
		this.isLmfHead = isLmfHead;
	}

	@Override
	public String toString()
	{
		return "LoginResult [parentID=" + parentID + ", merchantID="
				+ merchantID + ", merchantName=" + merchantName
				+ ", permmisionValue=" + permmisionValue + ", message="
				+ message + ", status=" + status + ", mid=" + mid + "]";
	}
	
}
