package com.hybunion.yirongma.common.util.huawei.common.handler;

import com.huawei.hms.support.api.push.TokenResult;

/**
 * 回调接口
 */
public interface ICallbackCode {
    /**
     * 回调接口
     * @param rst 结果码
     */
    void onResult(int rst, TokenResult result);

}
