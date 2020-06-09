package com.hybunion.yirongma.payment.view.engine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.StringFormatUtil;

import java.lang.ref.WeakReference;

/**
 * @author SunBingbing
 * @date 2017/2/27
 * @email freemars@yeah.net
 * @description 通过服务去初始化第三方推送服务
 */

public class PushStartService extends Service {

    // 上下文
    private Context mContext;
    String formatMemId;

    // 推送服务初始化失败
    private static final int INIT_PUSH_FAIL = 6002;
    // 延迟重试时间
    private static final int DELAYED_TIME = 2000;

    // 使用静态的 Handler 处理交互请求
    private static class PushHandler extends Handler {

        // 使用弱引用（防止内存溢出）
        private WeakReference<PushStartService> mService;

        public PushHandler (PushStartService service){
            this.mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INIT_PUSH_FAIL:
                    // 重试初始化操作
                    mService.get().initPushService();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    // 初始化 Handler
    private PushHandler mHandler = new PushHandler(this);


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("极光服务启动了");
        mContext = this;
        ifDoneKillSelf();
    }

    /**
     * 判断是否需要向极光注册
     */
    private void ifDoneKillSelf() {
        // 获取标准化的会员 ID
        String loginType = SharedPreferencesUtil.getInstance(mContext).getKey("loginType");
        if(("0").equals(loginType)){
            formatMemId = StringFormatUtil.getString(GetApplicationInfoUtil.getMerchantId());
        }else {
            formatMemId = StringFormatUtil.getString(SharedPreferencesUtil.getInstance(mContext).getKey("storeId"));
        }
        String alias = SharedPreferencesUtil.getAlias(this, "alias");

        if (formatMemId == null || "".equals(formatMemId)){//数据异常
            Log.i("xjz--极光注册","未注册");
            stopSelf();
        }else {
            initPushService();
        }
    }


    /**
     * 初始化推送服务
     */
    private void initPushService() {
        Log.i("xjz--极光注册","开始注册");
        String loginType = SharedPreferencesUtil.getInstance(mContext).getKey("loginType");
        if(("0").equals(loginType)){
             formatMemId = StringFormatUtil.getString(GetApplicationInfoUtil.getMerchantId());
        }else {
             formatMemId = StringFormatUtil.getString(SharedPreferencesUtil.getInstance(mContext).getKey("storeId"));
        }

        PushEngine.setAlias(this,formatMemId,new PushEngine.PushEngineRegisterCallback(){
            /**
             * 注册失败
             * @param code 错误码
             */
            @Override
            void onFail(int code) {
                // 重试
                Log.i("xjz--极光注册失败","code = "+code);
                LogUtil.d("极光注册失败，code="+code);
                mHandler.sendEmptyMessageDelayed(INIT_PUSH_FAIL,DELAYED_TIME);
                SharedPreferencesUtil.getInstance(mContext).putKey(SharedPConstant.JPUSH_IS_REGISTE,"失败");
            }

            /**
             * 注册成功
             * @param alias 成功返回的别名
             */
            @Override
            void onSuccess(String alias) {
                Log.i("xjz--极光注册成功","alias = "+alias);
                LogUtil.d("极光注册成功，alias="+alias);
                SharedPreferencesUtil.setNewSP(mContext, "alias", alias);
                stopSelf();
                SharedPreferencesUtil.getInstance(mContext).putKey(SharedPConstant.JPUSH_IS_REGISTE,"成功");

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("极光初始化服务销毁了");
    }
}
