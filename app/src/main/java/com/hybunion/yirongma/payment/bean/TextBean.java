package com.hybunion.yirongma.payment.bean;



import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

/**
 * Created by admin on 2017/11/9.
 */

public class TextBean extends BaseBean {
    private int payImage;
    private List<DataBean> rollingAdsList;

    public List<DataBean> getRollingAdsList() {
        return rollingAdsList;
    }

    public void setRollingAdsList(List<DataBean> rollingAdsList) {
        this.rollingAdsList = rollingAdsList;
    }

    public static class DataBean {
        private String adId;
        private String imgUrl;
        private String link;
        private String type;

        public String getAdId() {
            return adId;
        }

        public void setAdId(String adId) {
            this.adId = adId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public int getPayImage() {
        return payImage;
    }

    public void setPayImage(int payImage) {
        this.payImage = payImage;
    }


}
