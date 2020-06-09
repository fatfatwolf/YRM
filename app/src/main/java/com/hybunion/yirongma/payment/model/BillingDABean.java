package com.hybunion.yirongma.payment.model;

import java.util.List;

/**
 * @author lyj
 * @date 2017/8/28
 * @email freemars@yeah.net
 * @description
 */
public class BillingDABean {
    private boolean hasNextPage;
    private String totalAmount;
    private String status;
    private String message;
    private String totalCount;
    private String count;
    private List<DataBeanX> data;

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * cot : 3
         * time : 2017/04/25
         * sum : 3
         * data : [{"orderNo":"VC20170425193730506933","showStatus":"购卡成功","orderType":"1","dateDetail":"2017/04/25 19:04:01","amount":"1","dateSimple":"2017/04/25","sum":"3","cot":"3"},{"orderNo":"VC20170425171732221977","showStatus":"充值成功","orderType":"2","dateDetail":"2017/04/25 17:04:39","amount":1,"dateSimple":"2017/04/25","sum":3,"cot":"3"},{"orderNo":"VC20170425163427663504","showStatus":"购卡成功","orderType":"1","dateDetail":"2017/04/25 16:04:38","amount":1,"dateSimple":"2017/04/25","sum":3,"cot":"3"}]
         */
        private String dayCount;
        private String dayDate;
        private String dayAmount;
        private List<DataBean> details;

        public String getDayCount() {
            return dayCount;
        }

        public void setDayCount(String dayCount) {
            this.dayCount = dayCount;
        }

        public String getDayDate() {
            return dayDate;
        }

        public void setDayDate(String dayDate) {
            this.dayDate = dayDate;
        }

        public String getDayAmount() {
            return dayAmount;
        }

        public void setDayAmount(String dayAmount) {
            this.dayAmount = dayAmount;
        }

        public List<DataBean> getDetails() {
            return details;
        }

        public void setDetails(List<DataBean> details) {
            this.details = details;
        }

        public static class DataBean {
            /**
             * orderNo : VC20170425193730506933
             * showStatus : 购卡成功
             * orderType : 1
             * dateDetail : 2017/04/25 19:04:01
             * amount : 1
             * dateSimple : 2017/04/25
             * sum : 3
             * cot : 3
             */
            private String orderNo;//订单号
            private String showStatus;//交易类型 - 汉字
            private String cardType;    //卡类型（0：次卡；1：金额卡；2：充送卡；3：折扣卡；4：任意金额卡）
            private String simpleDate;    //日期 - 时分秒
            private String transAmount;    //交易金额
            private String cardNo;    //储值卡号 - 后四位
            private String transType;    //交易类型 - 数字
            private String dateSimple;
            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getShowStatus() {
                return showStatus;
            }

            public void setShowStatus(String showStatus) {
                this.showStatus = showStatus;
            }

            public String getCardType() {
                return cardType;
            }

            public void setCardType(String cardType) {
                this.cardType = cardType;
            }

            public String getSimpleDate() {
                return simpleDate;
            }

            public void setSimpleDate(String simpleDate) {
                this.simpleDate = simpleDate;
            }

            public String getTransAmount() {
                return transAmount;
            }

            public void setTransAmount(String transAmount) {
                this.transAmount = transAmount;
            }

            public String getCardNo() {
                return cardNo;
            }

            public void setCardNo(String cardNo) {
                this.cardNo = cardNo;
            }

            public String getTransType() {
                return transType;
            }

            public void setTransType(String transType) {
                this.transType = transType;
            }

            public String getDateSimple() {
                return dateSimple;
            }

            public void setDateSimple(String dateSimple) {
                this.dateSimple = dateSimple;
            }
        }
    }
}
