package com.hybunion.yirongma.payment.bean;

import java.util.List;

public class OpenAnimResult {
    private String message;
    private String status;

    private List<OpenImage> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OpenImage> getData() {
        return data;
    }

    public void setData(List<OpenImage> data) {
        this.data = data;
    }

    public  static  class OpenImage{
        private String ad_img_url;
        private String ad_link_url;

        public String getAd_img_url() {
            return ad_img_url;
        }

        public void setAd_img_url(String ad_img_url) {
            this.ad_img_url = ad_img_url;
        }

        public String getAd_link_url() {
            return ad_link_url;
        }

        public void setAd_link_url(String ad_link_url) {
            this.ad_link_url = ad_link_url;
        }
    }
}
