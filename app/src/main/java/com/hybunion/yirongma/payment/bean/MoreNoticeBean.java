package com.hybunion.yirongma.payment.bean;



import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by admin on 2017/11/6.
 */

public class MoreNoticeBean extends BaseBean<List<MoreNoticeBean.DataBean>> {

    public static class DataBean {
        private String content;
        private String createDate;
        private String title;
        public String viewStatus;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
