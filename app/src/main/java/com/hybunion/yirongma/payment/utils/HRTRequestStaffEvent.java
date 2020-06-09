package com.hybunion.yirongma.payment.utils;

import android.content.Context;

import com.qiyukf.unicorn.api.event.EventCallback;
import com.qiyukf.unicorn.api.event.UnicornEventBase;
import com.qiyukf.unicorn.api.event.entry.RequestStaffEntry;

/**
 * Created by Jairus on 2019/2/12.
 * 七鱼客服
 * 拦截请求客服事件回调
 * 当执行 onEvent 方法时，事件处于等待状态，只用调用了 callback 中的方法，才会继续。
 * 等待状态时，可以利用 RequestStaffEntry 对象来设置一些参数。
 *
 */

public class HRTRequestStaffEvent implements UnicornEventBase<RequestStaffEntry> {
    @Override
    public void onEvent(RequestStaffEntry requestStaffEntry, Context context, EventCallback<RequestStaffEntry> eventCallback) {
        requestStaffEntry.setHumanOnly(true);
        eventCallback.onProcessEventSuccess(requestStaffEntry);
    }
}
