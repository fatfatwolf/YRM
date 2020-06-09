package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

/**
 * Created by android on 2016/12/14.
 */

public class CommitDataBean extends BaseBean {

    /**
     * msg : 工单提交失败
     * numberUnits :
     * sessionExpire : false
     * success : false
     */

    private String msg;
    private String numberUnits;
    private boolean sessionExpire;
    private boolean success;

    @Override
    public String getStatus() {
        return success ? "0" : "1";
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNumberUnits(String numberUnits) {
        this.numberUnits = numberUnits;
    }

    public void setSessionExpire(boolean sessionExpire) {
        this.sessionExpire = sessionExpire;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public String getNumberUnits() {
        return numberUnits;
    }

    public boolean getSessionExpire() {
        return sessionExpire;
    }

    public boolean getSuccess() {
        return success;
    }
}
