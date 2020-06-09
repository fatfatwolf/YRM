package com.hybunion.yirongma.payment.inteface;

import com.hybunion.yirongma.payment.bean.QueryClerkListBean;

/**
 * @author SunBingbing
 * @date 2017/8/20
 * @email freemars@yeah.net
 * @description
 */

public interface OnButtonClickListener{
    void onDeleteClerk(int pos,QueryClerkListBean.ObjBean dataBean);
    void onDialog(int pos,QueryClerkListBean.ObjBean dataBean);
    void onModify(int pos,QueryClerkListBean.ObjBean dataBean);
}