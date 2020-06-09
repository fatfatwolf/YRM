package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

public class TerminalBean extends BaseBean<List<TerminalBean.DataBean>> {

    public String count;

    public class DataBean implements Serializable {
        public String cloudId;
        public String tidUrl;
        public String hornStatus;
        public String tid;
        public String tidName;
        public String type;
        public String source;

        public String secretKey;
        public String sn;
        public String snName;
        public String fjStatus;
        public String snModel;//QR65 SL51 QM50 ME50
        public String createDate;
        public String jh_mid;
        public String limitAmt;
        public String remark;
    }
}
