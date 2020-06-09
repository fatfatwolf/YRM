package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 收款码实体类
 */

public class StoreManageBean extends BaseBean<List<StoreManageBean.ObjBean>> implements Serializable {


    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public static class ObjBean implements Serializable{
        private String storeAddr;
        private String storePhone;
        private String storeName;
        private String storePhoto;
        private String storeId;
        public boolean isSeleted;
        public String createDate;
        public String id;
        // 查询TID 接口使用  和 查询收银台插件使用
        public String tid,tidName;

        public String couponName, condition, couponId;

        public boolean isStoreChoose;//多选门店使用

        public boolean isStoreChoose2;//回显时候判断新添加的门店点击

        public boolean isStoreChoose3;//判断选中门店数量

        public String getStoreAddr() {
            return storeAddr;
        }

        public void setStoreAddr(String storeAddr) {
            this.storeAddr = storeAddr;
        }

        public String getStorePhone() {
            return storePhone;
        }

        public void setStorePhone(String storePhone) {
            this.storePhone = storePhone;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStorePhoto() {
            return storePhoto;
        }

        public void setStorePhoto(String storePhoto) {
            this.storePhoto = storePhoto;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }
    }
}
