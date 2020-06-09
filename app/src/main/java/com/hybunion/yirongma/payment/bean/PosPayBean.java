package com.hybunion.yirongma.payment.bean;

/**
 * Created by admin on 2018/5/9.
 */

public class PosPayBean {
    public String key;
    public String vaule;


    public PosPayBean(String key, String vaule){
        super();
        this.key = key;
        this.vaule = vaule;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVaule() {
        return vaule;
    }

    public void setVaule(String vaule) {
        this.vaule = vaule;
    }


}

