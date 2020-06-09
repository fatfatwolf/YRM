package com.hybunion.netlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


//  UtilsLibrary 的初始化

public class UtilsLib {
    private static Context mContext;
    private static boolean mIsDebug = true;
    private static OkHttpClient mClient;
    private static UtilsLib mInstance;
    private OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
    private static boolean mHasSSL;

    // 超时时间，单位是 毫秒
    private long mReadTimeout=60000, mWriteTimeout=60000, mConnectTimeout=60000;


    private UtilsLib(){}

    public static UtilsLib getInstance(){
        if (mInstance == null){
            synchronized(UtilsLib.class){
                if (mInstance == null)
                    mInstance = new UtilsLib();
            }
        }
        return mInstance;
    }

    /**
     * 设置是否是debug模式（LogUtils 打Log用）
     * @param isDebug
     * @return
     */
    public UtilsLib setDebug(boolean isDebug){
        mIsDebug = isDebug;
        return this;
    }

    /**
     * 设置超时时间，传值<=0默认60000ms( 即 60s )
     * 单位是毫秒！！
     * @param readTimeout
     * @param writeTimeout
     * @param connectTimeout
     */
    public UtilsLib setTimeOut(long readTimeout, long writeTimeout, long connectTimeout){
        if (readTimeout>0) mReadTimeout=readTimeout;
        if (writeTimeout>0) mWriteTimeout = writeTimeout;
        if (connectTimeout>0) mConnectTimeout = connectTimeout;

        return this;
    }


    // ------- 设置证书校验模式  -------
    // 不设置默认信任所有证书(不安全)
    /**
     * 自定义信任规则，校验服务端证书
     * 不设置默认信任所有证书(不安全)
     * @param trustManager
     * @return
     */
    public UtilsLib setSSLParam(X509TrustManager trustManager){
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(trustManager);
        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mHasSSL = true;
        return this;
    }

    /**
     * 单向校验，使用预埋证书
     * 不设置默认信任所有证书(不安全)
     * @param certificates
     * @return
     */
    public UtilsLib setSSLParam(InputStream... certificates){
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(certificates);
        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mHasSSL = true;
        return this;
    }

    /**
     * 双向校验
     * 不设置默认信任所有证书(不安全)
     * @param bksFile    客户端 bks 文件
     * @param password
     * @param certificates
     * @return
     */
    public UtilsLib setSSLParam(InputStream bksFile, String password, InputStream... certificates){
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates);
        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mHasSSL = true;
        return this;
    }

    /**
     * 双向校验
     * 不设置默认信任所有证书(不安全)
     * @param bksFile
     * @param password
     * @param trustManager  如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
     * @return
     */
    public UtilsLib setSSLParam(InputStream bksFile, String password, X509TrustManager trustManager){
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, trustManager);
        mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        mHasSSL = true;
        return this;
    }



    public void init(Application app){
        mContext = app;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("okgo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);  // 全部打印
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        mBuilder.addInterceptor(loggingInterceptor);

        // 默认信任所有证书（不安全）
        if (!mHasSSL){
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
            mBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        }

        //全局的读取超时时间
        mBuilder.readTimeout(mReadTimeout, TimeUnit.MILLISECONDS);  // 60s
        //全局的写入超时时间
        mBuilder.writeTimeout(mWriteTimeout, TimeUnit.MILLISECONDS); //60s
        //全局的连接超时时间
        mBuilder.connectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS); //60s

        //使用内存保持cookie，app退出后，cookie消失
        mBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        mClient = mBuilder.build();
        OkGo.getInstance().init(app)
                .setOkHttpClient(mClient)                       //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonParams(params)                     //全局公共参数
    }

    /**
     * 设置 header
     * @param map
     */
    public void setHeadersMap(Map<String, String> map){
        if (map == null || map.size()==0) return;
        HttpHeaders headers = new HttpHeaders();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            headers.put(key, map.get(key));
        }

        OkGo.getInstance().addCommonHeaders(headers);


    }

    // 返回 OkGo 对象
    public static OkGo getOkGoInstance(){
        return OkGo.getInstance();
    }

    public static Context getContext(){
        return mContext;
    }

    public static boolean isDebug(){
        return mIsDebug;
    }

    // 按照 tag 取消指定的请求
    public static void clearRequest(Activity tag){
        OkGo.cancelTag(mClient, tag);
    }

}
