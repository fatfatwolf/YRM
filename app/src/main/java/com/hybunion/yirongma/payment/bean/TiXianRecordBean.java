package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * 体现记录
 */

public class TiXianRecordBean extends BaseBean<List<TiXianRecordBean.DataBean>> {

    public class DataBean{
        public String cashDate, cashAmt, status;
    }
}
