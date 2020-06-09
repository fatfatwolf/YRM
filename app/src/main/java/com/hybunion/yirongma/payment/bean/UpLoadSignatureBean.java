package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * @description:
 * @author: luyafeng
 * @data: 2017/7/21.
 */

public class UpLoadSignatureBean extends BaseBean{
    private String msg;
    private String numberUnits;
    private String sessionExpire;
    private boolean success;

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

    public String getSessionExpire() {
        return sessionExpire;
    }

    public void setSessionExpire(String sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
