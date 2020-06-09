package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 管理员设置 列表用
 */

public class AdminSettingBean extends BaseBean<List<AdminSettingBean.DataBean>> {

    public class DataBean{
        public String userName, storeId, storeName, phone;


    }

}
