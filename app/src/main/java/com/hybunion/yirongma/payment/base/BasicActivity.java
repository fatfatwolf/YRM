package com.hybunion.yirongma.payment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.net.remote.LoadingBean;

import java.util.Map;

/**
 * Created by king on 2016/7/5.
 */

public abstract class BasicActivity<T extends BasePresenter> extends BaseActivity {

    protected abstract T getPresenter();

    protected T presenter;


    protected void load() {
    }

    protected void canDo() {
    }

    public void showInfo(Map map) {
    }

    public void showInfo() {
    }
    public void onProcess(LoadingBean bean){

    }

    public void showInfo(Map map, RequestIndex type) {
    }

    public void showInfo(RequestIndex type) {
    }
    private ViewHandler view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        view = new ViewHandler();

        if (presenter != null) {
            presenter.setInterf(view);
        }

        canDo();
        if (savedInstanceState == null) {
            //    showLoading();
            load();
        }
    }

    public void addPresenter(BasePresenter p) {//重复使用一个共有的presenter
        p.setInterf(view);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void loadData() {

    }

    /**
     * 数据返回操作接口
     */
    protected class ViewHandler implements BasePresenter.IView {
        @Override
        public void showInfo(Map map, RequestIndex type) {
            BasicActivity.this.showInfo(map, type);
        }

        @Override
        public void showInfo(RequestIndex type) {
            BasicActivity.this.showInfo(type);
        }

        @Override
        public void showInfo(Map map) {
            BasicActivity.this.showInfo(map);
        }

        @Override
        public void onProcess(LoadingBean bean) {
            BasicActivity.this.onProcess(bean);
        }


        @Override
        public void showInfo() {
            BasicActivity.this.showInfo();
        }
    }

}
