package com.hybunion.yirongma.payment.Location;

/**
 * 对定位的封装
 * Created by lcy on 2016/4/6.
 */
public interface LocationWrapper {

    void startLocation();

    void stopLocation();


    void setOnLocationFinishListener(GaodeLocation.OnLocationFinish locationFinish);

}
