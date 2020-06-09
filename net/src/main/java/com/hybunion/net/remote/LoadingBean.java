package com.hybunion.net.remote;

/**
 * Created by shenshilei on 2018/4/10.
 * email ssl_java@163.com
 * site http://www.houziyou.com
 */

public class LoadingBean {
    private long current;
    private long total;
    private boolean done;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
