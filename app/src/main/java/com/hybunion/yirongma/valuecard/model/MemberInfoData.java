package com.hybunion.yirongma.valuecard.model;

import java.io.Serializable;

/**
 * Created by xiao on 2016/6/22.
 */
public class MemberInfoData  implements Serializable {

    private String balance;//	惠余额(惠商户独有)
    private String histHmdPaidAmount;//历史惠买单交易金额(惠商户独有)
    private String histHmdTransCount;//	历史惠买单交易数量(惠商户独有
    private String ydayHmdPaidAmount;//昨日惠买单应付(惠商户独有)
    private String ydayHmdTransCount;//昨日惠买单交易数量(惠商户独有)
    private String histNumMem;//共有会员(普通商户独有
    private String memNumCoupon;//会员领取优惠券(普通商户独有)
    private String memUseNumCoupon;//会员使用优惠券(普通商户独有)
    private String todayBirthdayNumMem;//今日生日会员(普通商户独有)
    private String ydayNumNewMem;//昨日新加入会员数
    private String merId;
    private String merType;//商户类型(0为普通商户1为惠商户

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType;
    }
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHistHmdPaidAmount() {
        return histHmdPaidAmount;
    }

    public void setHistHmdPaidAmount(String histHmdPaidAmount) {
        this.histHmdPaidAmount = histHmdPaidAmount;
    }

    public String getHistHmdTransCount() {
        return histHmdTransCount;
    }

    public void setHistHmdTransCount(String histHmdTransCount) {
        this.histHmdTransCount = histHmdTransCount;
    }

    public String getYdayHmdPaidAmount() {
        return ydayHmdPaidAmount;
    }

    public void setYdayHmdPaidAmount(String ydayHmdPaidAmount) {
        this.ydayHmdPaidAmount = ydayHmdPaidAmount;
    }

    public String getYdayHmdTransCount() {
        return ydayHmdTransCount;
    }

    public void setYdayHmdTransCount(String ydayHmdTransCount) {
        this.ydayHmdTransCount = ydayHmdTransCount;
    }

    public String getHistNumMem() {
        return histNumMem;
    }

    public void setHistNumMem(String histNumMem) {
        this.histNumMem = histNumMem;
    }

    public String getMemNumCoupon() {
        return memNumCoupon;
    }

    public void setMemNumCoupon(String memNumCoupon) {
        this.memNumCoupon = memNumCoupon;
    }

    public String getMemUseNumCoupon() {
        return memUseNumCoupon;
    }

    public void setMemUseNumCoupon(String memUseNumCoupon) {
        this.memUseNumCoupon = memUseNumCoupon;
    }

    public String getTodayBirthdayNumMem() {
        return todayBirthdayNumMem;
    }

    public void setTodayBirthdayNumMem(String todayBirthdayNumMem) {
        this.todayBirthdayNumMem = todayBirthdayNumMem;
    }

    public String getYdayNumNewMem() {
        return ydayNumNewMem;
    }

    public void setYdayNumNewMem(String ydayNumNewMem) {
        this.ydayNumNewMem = ydayNumNewMem;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    @Override
    public String toString() {
        return "MemberInfoData{" +
                "balance='" + balance + '\'' +
                ", histHmdPaidAmount='" + histHmdPaidAmount + '\'' +
                ", histHmdTransCount='" + histHmdTransCount + '\'' +
                ", ydayHmdPaidAmount='" + ydayHmdPaidAmount + '\'' +
                ", ydayHmdTransCount='" + ydayHmdTransCount + '\'' +
                ", histNumMem='" + histNumMem + '\'' +
                ", memNumCoupon='" + memNumCoupon + '\'' +
                ", memUseNumCoupon='" + memUseNumCoupon + '\'' +
                ", todayBirthdayNumMem='" + todayBirthdayNumMem + '\'' +
                ", ydayNumNewMem='" + ydayNumNewMem + '\'' +
                ", merId='" + merId + '\'' +
                ", merType='" + merType + '\'' +
                '}';
    }
}
