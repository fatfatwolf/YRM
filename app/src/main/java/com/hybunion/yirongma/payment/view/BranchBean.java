package com.hybunion.yirongma.payment.view;

import java.io.Serializable;
import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/8/14
 * @email freemars@yeah.net
 * @description
 */

public class BranchBean {
    private List<Data> data;

    public class Data implements Serializable {
        private String merName;
        private String merId;
        private String mid;
        public String getMerName() {
            return merName;
        }

        public void setMerName(String merName) {
            this.merName = merName;
        }

        public String getMerId() {
            return merId;
        }

        public void setMerId(String merId) {
            this.merId = merId;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }
    }
}
