package com.hybunion.yirongma;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.autonavi.bigwasp.sdk.BWBaseActivity;
import com.autonavi.bigwasp.sdk.BWHelper;
import com.autonavi.bigwasp.sdk.BigWaspListener;
import com.hybunion.netlibrary.UtilsLib;
import com.hybunion.yirongma.payment.utils.CommentMethod;
import com.hybunion.yirongma.common.util.huawei.HMSAgent;
import com.hybunion.yirongma.common.util.jpush.JPushUtils;
import com.hybunion.yirongma.common.util.jpush.JpushStatsConfig;
import com.hybunion.yirongma.common.util.jpush.VoicePlayManager;
import com.hybunion.yirongma.common.util.jpush.WakeUpActivity;
import com.hybunion.yirongma.payment.db.DBHelper;
import com.hybunion.yirongma.payment.activity.SplashActivity;
import com.hybunion.yirongma.payment.utils.CrashHandler;
import com.hybunion.yirongma.payment.utils.HRTRequestStaffEvent;
import com.hybunion.yirongma.payment.utils.GlideImageLoader;
import com.hybunion.yirongma.payment.utils.Utils;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.event.EventProcessFactory;
import com.qiyukf.unicorn.api.event.SDKEvents;
import com.qiyukf.unicorn.api.event.UnicornEventBase;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * HRTApplication，用户登录成功后，保存服务器返回的数据
 *
 * @author LZ
 */
public class HRTApplication extends MultiDexApplication {
    static {//使用static代码段可以防止内存泄漏

        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setEnableAutoLoadMore(false);
            }
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                return new ClassicsHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }


    private static HRTApplication instance;
    public static SQLiteDatabase db;
    //检测通知权限
    //存放所有的被开启的activity
    public List<Activity> activities;
    public static String versionName;
    public int appCount = 0; //计数用来检测 App 的状态（前后台，onStart:appCount++,onStop:appCount--）
    private int emuiApiLevel = 0;

    public Activity currentActivity = new Activity(); //记录前台的 Activity

    public static HRTApplication getInstance() {
        return instance;
    }

    public static List<Activity> list = new ArrayList<>();

    private void initVoicePlayManager() {
        final VoicePlayManager instance = VoicePlayManager.getInstance(this);
        instance.initSound();
    }

    /**
     * finish 具体activity
     */
    public static void finishActivity(Class clazz) {
        for (Activity activity : list) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                activity.finish();
            }
        }
    }

    // 获取当前栈顶的 Activity
    public static Activity getCurrentActivity(){
        if (!YrmUtils.isEmptyList(list)){
             return list.get(list.size()-1);
        }else{
            return null;
        }
    }


    /**
     * 除去传进来的之外，finish 所有其他 Activity
     * @param clazz
     */
    public static void finishAllExceptActivity(Class clazz){
        for (Activity activity : list) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                continue;
            }
            activity.finish();
        }
    }

    public static Activity getActivity(Class clazz) {
        for (Activity activity : list) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 在oncreat中不要做一些耗时的操作，严重影响app启动速度
     */
    @Override
    public void onCreate() {
        super.onCreate();
        //崩溃记录中需要拿到版本号先初始化
        versionName = getVersionName();

        // 异常处理，不需要处理时注释掉这两句即可！
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 注册crashHandlers
        crashHandler.init(getApplicationContext());
        activities = new ArrayList<>();
        instance = HRTApplication.this;
        DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DB_NAME);
        db = dbHelper.getWritableDatabase();
        UtilsLib.getInstance().setDebug(BuildConfig.LOG_DEBUG).init(instance);
        //监听 App 的切换进入后台的状态
        listenAppListCircle();
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            if (TextUtils.isEmpty((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}))) {
                emuiApiLevel = 0;
            } else {
                emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (emuiApiLevel > 10) {
            HMSAgent.init(this);
        }
        initVoicePlayManager();
        initBugly();
        Unicorn.init(this, "9b6376fc5d1dcbf200fe509e61051355", options(), new GlideImageLoader(this));   // 七鱼客服初始化
        Utils.init(this);
        JPushUtils.initJPush();
        JpushStatsConfig.initStats();//极光统计初始化
        handleSSLHandshake();
        initBsp();
    }


    public  void initBsp(){
        BWHelper.getInstance().initApp(this, new BigWaspListener.InitApp() {
            @Override
            public String cpName() {
                return "hrtpayment";
            }

            @Override
            public String appVersion() {
                return "测试app版本号";
            }

            @Override
            public String extCpName() {
                return "xgc_bgc_hrt";
            }

            @Override
            public Class<? extends BWBaseActivity> feedbackClazz() {
                return null;
            }

            @Override

            public Class<? extends BWBaseActivity> guideClazz() {
                return null;
            }
        });
    }

    public static void  handleSSLHandshake(){
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {

        }
    }




    // 七鱼客服
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        // 设置拦截消息，用来直接发起人工。
        options.sdkEvents = new SDKEvents();
        options.sdkEvents.eventProcessFactory = new EventProcessFactory() {
            @Override
            public UnicornEventBase eventOf(int i) {
                if (i == 0) {
                    return new HRTRequestStaffEvent();
                }
                return null;
            }
        };
        // 聊天界面的设置。
        options.uiCustomization = new UICustomization();
        options.uiCustomization.titleBackgroundColor = Color.parseColor("#fe4f3e"); // 设置标题栏颜色
        options.uiCustomization.titleBarStyle = 1;   // 设置标题栏按钮颜色为白色
        return options;
    }

    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setAppVersion(CommentMethod.getVersionName(context));
        strategy.setAppChannel("channel");
        strategy.setAppPackageName(packageName);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        String agentID = getResources().getString(R.string.AGENT_ID);
        if (agentID.equals("0")) {
            CrashReport.initCrashReport(context, "2b719a3ac7", false, strategy);
        } else if (agentID.equals("1")) {
            CrashReport.initCrashReport(context, "f6331894d0", false, strategy);
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    //用来检测 App 是否进入后台状态
    private void listenAppListCircle() {

        ActivityLifecycleCallbacks callback = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof WakeUpActivity) {
                } else if (activity instanceof SplashActivity){
                }else{
                    HRTApplication.list.add(activity);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //此时表示 Activity 被创建
                appCount++;
                currentActivity = activity;
//                if (mIsFloatOpen == 1 && !mIsOpen) {  // 开启悬浮球
//                    openFloat();
//
//                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (activity.isFinishing()) {
                    list.remove(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //此时表示 Activity 处于不可见状态
                appCount--;
                //App 处于后台状态，或者用户不在首页，则不显示悬浮球
//                if (appCount == 0 || activity.getClass().getName().equals("com.hybunion.yirongma.LMFMainActivity")) {
//                    closeFloat();
//                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };

        //在该 App 上注册 Activity 声明周期的监听
        registerActivityLifecycleCallbacks(callback);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersionName() {

        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo == null) return "";
        String version = packInfo.versionName;
        return version;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void finishAllActivities() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }


}
