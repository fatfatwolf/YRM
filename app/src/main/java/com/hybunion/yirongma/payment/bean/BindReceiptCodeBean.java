package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * Created by king on 2017/5/20.
 */

public class BindReceiptCodeBean extends BaseBean {


    /**
     * msg : 该终端号(1000000003)已被占用!
     * numberUnits :
     * sessionExpire : false
     * success : false
     */

    private String msg;
    private String numberUnits;
    private boolean sessionExpire;
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
}
