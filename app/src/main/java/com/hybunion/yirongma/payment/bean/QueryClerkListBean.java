package com.hybunion.yirongma.payment.bean;

import com.hybunion.yirongma.payment.bean.base.BaseBean;

import java.io.Serializable;
import java.util.List;

public class QueryClerkListBean extends BaseBean <List<QueryClerkListBean.ObjBean>>{
        private String status;
        private String success;
        private String merId;
        private String storeId;
        private String storeName;

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public static class ObjBean implements Serializable {
        private String employId;
        private String employName;
        private String position;
        private String employPhone;
        public boolean isClicked = false;

        public String getEmployId() {
            return employId;
        }

        public void setEmployId(String employId) {
            this.employId = employId;
        }

        public String getEmployName() {
            return employName;
        }

        public void setEmployName(String employName) {
            this.employName = employName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getEmployPhone() {
            return employPhone;
        }

        public void setEmployPhone(String employPhone) {
            this.employPhone = employPhone;
        }
    }

}
