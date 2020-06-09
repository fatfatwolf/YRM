package com.hybunion.yirongma.valuecard.model;

/**
 * Created by Administrator on 2015/10/12.
 */
public class VcBalanceDetailBean {

    private String transType;
    private String transTime;
    private String transAmount;
    private String itemName;
    private String empName;


    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    @Override
    public String toString() {
        return "VcBalanceDetailBean{" +
                "transType='" + transType + '\'' +
                ", transTime='" + transTime + '\'' +
                ", transAmount='" + transAmount + '\'' +
                ", itemName='" + itemName + '\'' +
                ", empName='" + empName + '\'' +
                '}';
    }

    public VcBalanceDetailBean(String transType, String transTime, String transAmount, String itemName, String empName) {
        this.transType = transType;
        this.transTime = transTime;
        this.transAmount = transAmount;
        this.itemName = itemName;
        this.empName = empName;
    }
}
