package com.hybunion.yirongma.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.net.remote.LoadingBean;
import com.hybunion.yirongma.payment.base.BasePresenter;

import java.util.Map;

/**
 * Created by king on 2016/7/4.
 */
public abstract class BasicFragment<T extends BasePresenter> extends BaseFragment {

    protected abstract T getPresenter();

    protected T presenter;


    private ViewHandler viewHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            super.onCreateView(inflater, container, savedInstanceState);
            presenter = getPresenter();
            viewHandler = new ViewHandler();
            if (presenter != null) {
                presenter.setInterf(viewHandler);
            }

            load();
        }

        return view;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//            presenter = getPresenter();
//            viewHandler = new ViewHandler();
//            if (presenter != null) {
//                presenter.setInterf(viewHandler);
//            }
//
//            if (savedInstanceState == null) {
//                //    showLoading();
//                load();
//            }
//    }

    public void addPresenter(BasePresenter p) {
        p.setInterf(viewHandler);
    }

    protected void load() {
    }

    public void showInfo(Map map) {

    }

    public void showInfo() {

    }

    public void showInfo(Map map, RequestIndex type) {

    }

    public void showInfo(RequestIndex type) {

    }

    /**
     * 数据返回操作接口
     */
    protected class ViewHandler implements BasePresenter.IView {
        @Override
        public void showInfo(Map map, RequestIndex type) {
            BasicFragment.this.showInfo(map, type);
        }

        @Override
        public void showInfo(RequestIndex type) {
            BasicFragment.this.showInfo(type);
        }

        @Override
        public void showInfo(Map map) {
            BasicFragment.this.showInfo(map);
        }

        @Override

        public void onProcess(LoadingBean bean) {

        }

        @Override
        public void showInfo() {
            BasicFragment.this.showInfo();
        }
    }
}
