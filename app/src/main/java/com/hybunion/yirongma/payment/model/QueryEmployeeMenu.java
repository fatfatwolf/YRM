package com.hybunion.yirongma.payment.model;

import java.util.List;

/**
 * Created by lcy on 2015/7/13.
 */
public class QueryEmployeeMenu {

    private List<QueryEmployeeMenu.MyData> data;
    private String status, message;

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

    public List<MyData> getData() {
        return data;
    }

    public void setData(List<MyData> data) {
        this.data = data;
    }

    public class MyData {
        private String id, text, leaf, url;
        private List<MyData> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLeaf() {
            return leaf;
        }

        public void setLeaf(String leaf) {
            this.leaf = leaf;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<MyData> getChildren() {
            return children;
        }

        public void setChildren(List<MyData> children) {
            this.children = children;
        }
    }

}
