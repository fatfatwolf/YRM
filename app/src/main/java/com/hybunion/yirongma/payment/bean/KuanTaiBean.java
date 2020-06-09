package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.util.List;

public class KuanTaiBean extends BaseBean<List<KuanTaiBean.DataBean>> {
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public class DataBean{
        private String createDate;
        private String storeName;
        private String storeId;
        private String storeType;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }




    }
}
