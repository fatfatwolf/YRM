package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * Created by admin on 2018/1/23.
 */

public class UplodeBandCordImageBean extends BaseBean {
    //{"msg":"此户处于审核中或审批完成，为不可修改状态","numberUnits":"","sessionExpire":false,"success":false}
    private boolean success;
    private String msg;
    private String numberUnits;

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

    public String getNumberUnits() {
        return numberUnits;
    }

    public void setNumberUnits(String numberUnits) {
        this.numberUnits = numberUnits;
    }
}
