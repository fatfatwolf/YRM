package com.hybunion.yirongma.payment.bean;

import android.text.TextUtils;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QueryTransBean extends BaseBean<List<QueryTransBean.DataBean>> implements Serializable{
    public String totalCount;
    public String totalAmt;
    public String discountAmt;
    public String discountCount;

    // 惠储值使用
    public String totalRechargeAmount,  // 充值金额
            totalRechargeCount,     // 充值笔数
            totalCostAmount,  // 消费金额
            totalCostCount;   // 消费笔数

    public String refundCount, refundAmount;  // 退款总金额、总笔数

    public class DataBean implements Serializable{
        public String transAmount;
        public String transAmount1;
        public String  transTime;
        private long transTimeLong;   // 数据库用
        public long getTransTimeLong() {
            if (TextUtils.isEmpty(transTime)) return 0;
            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(transTime);
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public String transDate;
        public String iconUrl;
        public String payChannel;
        public String UUID;
        public String payStyle;
        public String orderNo;
        public String tidName;
        public String periodType;//手否在30天内
        public String redMsg;
        public String payableAmount;  // 实际支付金额

        // 惠储值列表使用
        public String orderType;  // 订单类型，0：购卡；1：充值；2：消费
//            transAmount,
//            transDate,
//            orderNo,
//            orderStatus;   // 订单状态，0：待付款；1：已付款；2：订单被取消；3：已退款；4：退款中；5：退款失败
        public String svcOrderNo;  // 惠储值订单号


        // =========== 数据库用 ============
        public String payType="-1"; // 0-微信   1-支付宝    3-银联   4-福利卡  5-和卡 数据库用
        public String isAmount;
        public String tId;     // 收款码 id
        public String sourceTid;   // 收银插件 id

        public String transType;  // 如果是退款 transType = “3”
        public String storeId;    // 列表中筛选用的门店 id
        public String kuanTaiId;   // 列表中筛选用的款台 id
//        public String payName;  // 根据“微信“  ”支付宝“ 等文字判断支付类型
        // 华为推送用
        public String payName;  // 根据微信、支付宝等字样给 payType 赋值
        public String payableAmt; // 金额
//        public String transDate; // 交易时间
        public boolean isHuiChuZhi;  // 是否是惠储值列表数据

    }

}
