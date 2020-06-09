package com.hybunion.yirongma.payment.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.hybunion.yirongma.HRTApplication;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
    /**
     * Debug Log tag
     */
    public static final String TAG = "CrashHandler";
    /**
     * 是否开启日志输出,在Debug状态下开启,
     * 在Release状态下关闭以提示程序性能
     */
    public static final boolean DEBUG = false;
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * CrashHandler实例
     */
    private static CrashHandler INSTANCE;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "uncaughtException", ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //Sleep一会后结束程序
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast toast = Toast.makeText(mContext, "出错啦，程序猿和工程狮们在努力抢修！\n",
//                        Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                //关闭蓝牙与mpos的链接
//                HrtpaymentMPOS reader = HrtpaymentMPOS.getInstance(mContext);
//                reader.closeDevice(new BasicReaderListeners.CloseDeviceListener() {
//                    @Override
//                    public void closeSucc() {
//                        Toast.makeText(mContext, "关闭蓝牙链接", Toast.LENGTH_LONG).show();
//                    }
//                });
                //PubString.updateMposParam(mContext);
//                Looper.loop();
//            }
//        }.start();
        //保存错误报告文件

//        saveCrashInfoToFile(mContext, ex);
        //发送错误报告到服务器
        //sendCrashReportsToServer(mContext);
        return true;
    }

    /**
     * @param ex
     * @return
     */
    public static String saveCrashInfoToFile(Context mContext, Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(mContext.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            StringBuilder sb = new StringBuilder();
            sb.append("vercode:");
            sb.append(packageInfo.versionCode);
            sb.append(" vername:");
            sb.append(packageInfo.versionName);
            sb.append("\n");
            sb.append(result);
            result = sb.toString();
        } catch (Exception e) {

        }
        TelephonyManager mTm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb = android.os.Build.BRAND;//手机品牌
        String merchantName = com.hybunion.yirongma.payment.utils.SharedPreferencesUtil.getInstance(mContext).getKey("merchantName");
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        JSONObject jsonObject = new JSONObject();
/*        {"userType":"1","feedbackType":"0","feedbackInfo":"太给力了!",
            "userName":"马云","email":"","phone":""}
        	谢英发  13:06:48
userType：用户类型（0：会员，1：商户）
feedbackType：反馈类型（0：用户信息反馈，1：异常信息反馈）
feedbackInfo：反馈信息（用户反馈信息或异常信息）
userName：用户名称（会员或商户名称）
email：电子邮件（会员或商户的电子邮件(一般用于用户信息反馈)）
phone：电话号码（会员或商户的电子邮件(一般用于用户信息反馈）
        	*/
        try {
            jsonObject.put("cellphoneBrand", mtyb + "_" + mtype);
            jsonObject.put("userType", "1");
            jsonObject.put("feedbackType", "1");
            jsonObject.put("userName", com.hybunion.yirongma.payment.utils.SharedPreferencesUtil.getInstance(mContext).getKey("merchantName"));
            jsonObject.put("email", "");
            jsonObject.put("phone", com.hybunion.yirongma.payment.utils.SharedPreferencesUtil.getInstance(mContext).getKey("loginNumber"));
            jsonObject.put("agentId", Constant.AGENT_ID);
            jsonObject.put("appVerNum", HRTApplication.versionName);
            if (result.length() > 4000) {
                jsonObject.put("feedbackInfo", result.substring(0, 4000));
            } else {
                jsonObject.put("feedbackInfo", result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            sendMessageToServer(mContext, jsonObject);
        }
        printWriter.close();
        return null;
    }


}  
