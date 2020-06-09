package com.hybunion.yirongma.payment.base;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.base.BaseBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.usecase.base.BaseSubscriber;
import com.hybunion.yirongma.payment.usecase.base.UseCase;
import com.hybunion.net.remote.LoadingBean;


import java.util.Map;

/**
 * Created by king on 2016/6/17.
 */

public abstract class BasePresenter<T extends UseCase, P extends BaseBean> {

    protected T useCase;

    protected Subscriber subscriber;

    protected BaseActivity mContext;

    protected IView view;

    protected abstract T getUseCase();

    protected abstract void success(P p);
    protected abstract void onProcess(LoadingBean bean);

    protected abstract Class<?> getGenericsClass();

    protected void failed(String errorMsg) {
        mContext.showError(errorMsg);
    }

    protected void netError() {
    }

    protected String getSuccessCode() {
        return "1";
    }


    public BasePresenter(BaseActivity context) {
        this.mContext = context;
        useCase = getUseCase();
        subscriber = new Subscriber();
    }

    public void setInterf(IView view) {
        this.view = view;
    }

    //==============回调回来的数据===========================================
    protected class Subscriber extends BaseSubscriber<P> {

        @Override
        public void onCompleted(final P p) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    success(p);
                    LogUtil.d("BasePresenter：run here:-------SUCCESS");
                    //mContext.hideLoading();
                }
            });
        }


        @Override
        public void onError(Throwable e) {
            mContext.hideLoading();
            myError();
        }

        @Override
        public void onProcess(final LoadingBean bean) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BasePresenter.this.onProcess(bean);
                }
            });
        }


        @Override
        public Class<?> getType() {
            return getGenericsClass();
        }
    }

    protected void myError(){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.hideLoading();
                netError();
                error();
                mContext.showError(mContext.getString(R.string.poor_network));
            }
        });
        error();
    }

    protected void error() {
    }

    public interface IView {
        void showInfo(Map map, RequestIndex type);

        void showInfo(RequestIndex type);

        void showInfo(Map map);

        void onProcess(LoadingBean bean);

        void showInfo();    }


}
