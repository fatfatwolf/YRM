package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by admin on 2018/2/27.
 */

public class MainMassageBean extends BaseBean<List<MainMassageBean.DataBean>>{
    private String total;
    private String msg;
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String createDate;
        private String message;
        private String msgId;
        private String title;
        private String viewStatus;
        private String urlQr;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getViewStatus() {
            return viewStatus;
        }

        public void setViewStatus(String viewStatus) {
            this.viewStatus = viewStatus;
        }

        public String getUrlQr() {
            return urlQr;
        }

        public void setUrlQr(String urlQr) {
            this.urlQr = urlQr;
        }
    }
}
