package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jairus on 2018/10/22.
 */

public class ChooseBankCardBean extends BaseBean<List<ChooseBankCardBean.DataBean>> implements Serializable {

    public class DataBean  implements Serializable{
        public String accNo, bankImg, cardType, bankName, bankAccNo, bankAccName, paybankId;
    }
}
