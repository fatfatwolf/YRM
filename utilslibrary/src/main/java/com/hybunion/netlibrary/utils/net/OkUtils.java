package com.hybunion.netlibrary.utils.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hybunion.netlibrary.UtilsLib;
import com.hybunion.netlibrary.utils.CommonUtils;
import com.hybunion.netlibrary.utils.EncropyUtils;
import com.hybunion.netlibrary.utils.LogUtil;
import com.hybunion.netlibrary.utils.SPUtil;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class OkUtils<T> {
    private static OkUtils mInstance;
    private Gson mGson;
    private static final String TAG = "---n" +
            "et---";
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public String loginAction = "android.intent.action.LOGIN";

    private OkUtils() {
    }

    public static OkUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加的头
     */
    protected static JSONObject packageJsonHeader(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", "android");
            jsonObject.put("version_no", getVersion(context));
            jsonObject.put("token_id", "");

            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get version
     *
     * @param context
     * @return
     */
    private static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "1.00";
    }

    public void put(Activity tag, String url, Map<String, String> params, final MyOkCallback<T> callback) {

        PutRequest<T> putRequest = OkGo.<T>put(url)
                .tag(tag);
        if (params == null) return;
        Set<String> set = params.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            putRequest.params(key, params.get(key));
        }
        executeRequest(putRequest, callback);

    }

    public void get(Activity tag, String url, Map<String, String> params, final MyOkCallback<T> callback) {
        GetRequest<T> getRequest = OkGo.get(url);
        if (params == null) return;
        Set<String> set = params.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            getRequest.params(key, params.get(key));
        }
        executeRequest(getRequest, callback);

    }


    // Json 请求
    public void post(Activity tag, String url, JSONObject params, MyOkCallback<T> callback) {
        PostRequest<T> postRequest = OkGo.<T>post(url).tag(tag);
        JSONObject jsonObject;
        jsonObject = new JSONObject();
        try {
            jsonObject.put("header", packageJsonHeader(tag));
            jsonObject.put("body", params);
        } catch (Exception e) {
            return;
        }
        if (jsonObject != null) {
            postRequest.upJson(jsonObject);
        }
        executeRequest(postRequest, callback);
    }


    public void postNoHeader(Activity tag, String url, JSONObject params, MyOkCallback<T> callback) {
        PostRequest<T> postRequest = OkGo.<T>post(url).tag(tag);
        if (params != null) {
            postRequest.upJson(params);
        }
        executeRequest(postRequest, callback);
    }


    public void post(Activity tag, String url, Map<String, String> params, MyOkCallback<T> callback) {
        PostRequest<T> postRequest = OkGo.<T>post(url).tag(tag);
        if (params != null) {
            postRequest.upJson(new JSONObject(params));
        }
        executeRequest(postRequest, callback);
    }

    // 表单
    public void postFormData(Activity tag, String url, Map<String, String> params, MyOkCallback<T> callback) {
        postFile(tag, url, params, null, callback);
    }

    // 表单带File
    public void postFile(Activity tag, String url, Map<String, String> params, Map<String, File> picParams, MyOkCallback<T> callback) {

        PostRequest<T> postRequest = OkGo.<T>post(url).tag(tag).isMultipart(true);
        if (params != null) {
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                postRequest.params(key, params.get(key));
            }
        }
        if (picParams != null) {
            Iterator<String> iterator = picParams.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                postRequest.params(key, picParams.get(key));
            }
        }
        executeRequest(postRequest, callback);
    }

    /**
     * 供加密接口使用
     * params为正常的json传递参数
     * 加密在框架里做封装
     */
    public void postEncryp(Activity tag, String url, JSONObject params, MyOkCallback<T> callback) {
        EncropyUtils.loadPublicKey();
        EncropyUtils.loadPrivateKey();
        String str = params.toString();
        String myPrivateKey = CommonUtils.createRandom(false, 16);
        String result = EncropyUtils.encrypt(str, myPrivateKey);//私钥对参数进行加密
        LogUtil.d("okgo", "加密之前的数据：" + str);
        String testRSAEnWith64 = null;
        try {
            testRSAEnWith64 = EncropyUtils.encryptWithBase64(myPrivateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("key", testRSAEnWith64);
            jsonObject.put("value", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PostRequest<T> postRequest = OkGo.<T>post
                (url).tag(tag);
        if (params != null) {
            postRequest.upJson(jsonObject);
        }
        executeRequestEncryp(postRequest, callback, myPrivateKey);
    }

    /**
     * 加密接口调用
     */
    private void executeRequestEncryp(Request request, final MyOkCallback callback, final String myPrivateKey) {
        request.execute(new OkCallback() {
            @Override
            public void onStart(Request request) {
                super.onStart(request);
                callback.onStart();

            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                if (response.code() >= 200 && response.code() < 300) {
                    analyzingResponseEncryp(response.getRawResponse(), callback, myPrivateKey);
                } else {
                    callback.onError(new Exception("ERROR"));
                }
            }

            @Override
            public void onError(Response response) {
                super.onError(response);
                callback.onError(new Exception("ERROR"));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callback.onFinish();

            }
        });

    }

    /**
     * 接收后台返回值是加密的字符串
     * {"value","加密的字符串"}，全用String接收，做解密处理
     */

    private void analyzingResponseEncryp(okhttp3.Response response, final MyOkCallback callback, String myPrivateKey) {
        if (response == null) {
            callback.onError(new Exception("接口response返回null"));
            return;
        }
        final String body;
        String decodeContent = "";
        try {
            body = response.body().string();  // body 直接是字符串，需要解密然后解析实体类
            if (!TextUtils.isEmpty(body)) {
                try {
                    decodeContent = EncropyUtils.decryptor(body, myPrivateKey);
                    LogUtil.d("okgo", "解密之后的数据：" + decodeContent);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            } else {
                callback.onError(new Exception("---body为null---"));
                return;
            }
            if ("String".equals(callback.getClazz().getSimpleName())) {
                callback.onSuccess(decodeContent);
            } else {
                if (mGson == null) mGson = new Gson();
                try {
//                    if(TextUtils.isEmpty(decodeContent)){
//                        callback.onError(new Exception("---网络请求结果解析错误！---"));
//                        return;

//                    }
                    T o = (T) mGson.fromJson(decodeContent, callback.getClazz());
                    if (null != o)
                        callback.onSuccess(o);
                    else
                        callback.onError(new Exception("---网络请求结果解析为空！---"));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    LogUtil.d(TAG, "---网络请求结果解析错误！---");
                    callback.onError(new Exception("---网络请求结果解析错误！---"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void executeRequest(Request request, final MyOkCallback callback) {
        String token = SPUtil.getString("token", "");
        if (!TextUtils.isEmpty(token))
            request.headers("token", token);
        request.execute(new OkCallback() {
            @Override
            public void onStart(Request request) {
                super.onStart(request);
                callback.onStart();

            }

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                analyzingResponse(response.getRawResponse(), callback);
            }

            @Override
            public void onError(Response response) {
                super.onError(response);
                callback.onError(new Exception("ERROR"));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                callback.onFinish();

            }
        });

    }

    private void analyzingResponse(okhttp3.Response response, final MyOkCallback callback) {
        if (response == null) {
            callback.onError(new Exception("接口response返回null"));
            return;
        }
        final String body;
        try {
            body = response.body().string();
            try {
                JSONObject jb = new JSONObject(body);
                String status = jb.getString("status");
                if (TextUtils.isEmpty(status) || "-99".equals(status)){  // 登录过期，发广播，广播中跳转登录页面重新登录
                    UtilsLib.getContext().sendBroadcast(new Intent(loginAction));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (TextUtils.isEmpty(body)) {
                callback.onError(new Exception("返回 body 为空"));
            } else {
                if ("String".equals(callback.getClazz().getSimpleName())) {
                    callback.onSuccess(body);
                } else {
                    if (mGson == null) mGson = new Gson();
                    try {
                        T o = (T) mGson.fromJson(body, callback.getClazz());
                        if (o == null) {
                            callback.onError(new Exception("解析实体类为空"));
                        } else {
                            callback.onSuccess(o);
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        LogUtil.d(TAG, "---网络请求结果解析错误！---");
                        callback.onError(new Exception("---网络请求结果解析错误！---"));
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
