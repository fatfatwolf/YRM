package com.hybunion.yirongma.payment.usecase.base;
import com.hybunion.yirongma.payment.bean.base.BaseBean;
import com.hybunion.net.remote.Subscriber;

public abstract class BaseSubscriber<T extends BaseBean> implements Subscriber<T> {

}
