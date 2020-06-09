package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by win7 on 2016/7/18.
 */
public class NoticeDetailsBean extends BaseBean<List<NoticeDetailsBean.DataBean>> {


    /**
     * msg : 成功
     * data : [{"createdate":"2017-01-24 12:29:40","description":"444444444"}]
     */

    private String msg;
    

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class DataBean {
        /**
         * createdate : 2017-01-24 12:29:40
         * description : 444444444
         */

        private String createdate;
        private String description;
        private String title;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
