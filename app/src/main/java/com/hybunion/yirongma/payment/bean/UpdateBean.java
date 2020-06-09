package com.hybunion.yirongma.payment.bean;

/**
 * 版本更新信息 Bean
 */

public class UpdateBean {

    public UpdateBean(boolean hasNewVersion, String updateUrl, String updateDesc, String isForceUpdate){
        this.hasNewVersion = hasNewVersion;
        this.updateUrl = updateUrl;
        this.updateDesc = updateDesc;
        this.isForceUpdate = isForceUpdate;
    }

    public boolean hasNewVersion;  // 是否有新版本

    public String updateUrl;    // 更新 url

    public String updateDesc;   // 更新信息

    public String isForceUpdate;  // 是否需要强更

    @Override
    public String toString() {
        return "{" +
                "hasNewVersion=" + hasNewVersion +
                ", updateUrl='" + updateUrl + '\'' +
                ", updateDesc='" + updateDesc + '\'' +
                ", isForceUpdate='" + isForceUpdate + '\'' +
                '}';
    }
}
