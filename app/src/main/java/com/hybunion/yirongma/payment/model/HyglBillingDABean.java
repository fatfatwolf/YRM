package com.hybunion.yirongma.payment.model;

import java.util.List;

/**
 * @author lyj
 * @date 2017/8/30
 * @email freemars@yeah.net
 * @description
 */

public class HyglBillingDABean {
    private boolean hasNextPage;
    private String 	totalAmt;
    private String status;
    private String message;
    private String totalCount;
    private List<MemberDataBean> data;

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

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<MemberDataBean> getData() {
        return data;
    }

    public void setData(List<MemberDataBean> data) {
        this.data = data;
    }

    public static class MemberDataBean {
        /**
         * cot : 3
         * time : 2017/04/25
         * sum : 3
         * data : [{"orderNo":"VC20170425193730506933","showStatus":"购卡成功","orderType":"1","dateDetail":"2017/04/25 19:04:01","amount":"1","dateSimple":"2017/04/25","sum":"3","cot":"3"},{"orderNo":"VC20170425171732221977","showStatus":"充值成功","orderType":"2","dateDetail":"2017/04/25 17:04:39","amount":1,"dateSimple":"2017/04/25","sum":3,"cot":"3"},{"orderNo":"VC20170425163427663504","showStatus":"购卡成功","orderType":"1","dateDetail":"2017/04/25 16:04:38","amount":1,"dateSimple":"2017/04/25","sum":3,"cot":"3"}]
         */
        private String count;
        private String date;
        private String amount;
        private List<MemberDataDABean> dailyTrans;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public List<MemberDataDABean> getDailyTrans() {
            return dailyTrans;
        }

        public void setDailyTrans(List<MemberDataDABean> dailyTrans) {
            this.dailyTrans = dailyTrans;
        }

        public static class MemberDataDABean {
            private String description; //提示
            private String iconUrl;     //图片
            private String isScanPay;
            private String orderNo;
            private String payChannel;
            private String refundSuccessDate;
            private String sevenDate;
            private String simpleDate;
            private String status;      //状态
            private String tid;
            private String transAmount; //金额
            private String transDate;
            private String transTime;
            private String payStyle;
            public String UUID;
            public String tidName;
            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getIsScanPay() {
                return isScanPay;
            }

            public void setIsScanPay(String isScanPay) {
                this.isScanPay = isScanPay;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getPayChannel() {
                return payChannel;
            }

            public void setPayChannel(String payChannel) {
                this.payChannel = payChannel;
            }

            public String getRefundSuccessDate() {
                return refundSuccessDate;
            }

            public void setRefundSuccessDate(String refundSuccessDate) {
                this.refundSuccessDate = refundSuccessDate;
            }

            public String getSevenDate() {
                return sevenDate;
            }

            public void setSevenDate(String sevenDate) {
                this.sevenDate = sevenDate;
            }

            public String getSimpleDate() {
                return simpleDate;
            }

            public void setSimpleDate(String simpleDate) {
                this.simpleDate = simpleDate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getTransAmount() {
                return transAmount;
            }

            public void setTransAmount(String transAmount) {
                this.transAmount = transAmount;
            }

            public String getTransDate() {
                return transDate;
            }

            public void setTransDate(String transDate) {
                this.transDate = transDate;
            }

            public String getTransTime() {
                return transTime;
            }

            public void setTransTime(String transTime) {
                this.transTime = transTime;
            }

            public String getPayStyle() {
                return payStyle;
            }

            public void setPayStyle(String payStyle) {
                this.payStyle = payStyle;
            }
        }
    }
}

