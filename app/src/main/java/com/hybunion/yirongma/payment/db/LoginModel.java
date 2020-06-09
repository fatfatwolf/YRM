package com.hybunion.yirongma.payment.db;

public class LoginModel {
	
	private String uid;
	private String uname;
	private String upswd;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpswd() {
		return upswd;
	}
	public void setUpswd(String upswd) {
		this.upswd = upswd;
	}

	@Override
	public String toString() {
		return "LoginModel{" +
				"uid='" + uid + '\'' +
				", uname='" + uname + '\'' +
				", upswd='" + upswd + '\'' +
				'}';
	}
}
