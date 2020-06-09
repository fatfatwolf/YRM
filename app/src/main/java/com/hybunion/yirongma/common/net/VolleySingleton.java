package com.hybunion.yirongma.common.net;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.payment.utils.MyHttp;
import com.hybunion.yirongma.payment.utils.LogUtil;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.Map;

/**
 * 网络请求工作类：
 * 基本的网路请求目前都走 Volley
 */
public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private BasicCookieStore cookieStore;

    private VolleySingleton(Context context) {
        DefaultHttpClient httpClient = (DefaultHttpClient) MyHttp.getHttp();
        cookieStore = new BasicCookieStore();
        httpClient.setCookieStore(cookieStore);
        HttpStack httpStack = new HttpClientStack(httpClient);
        mRequestQueue = Volley.newRequestQueue(context, httpStack);
    }

    /**
     * 获取实例
     */
    public static VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void add(Request request) {
        mRequestQueue.add(request);
    }

    /**
     * 网络请求的 Header
     *
     * @return 请求 Header
     */
    private JSONObject getHeader() {
        JSONObject header = null;
        try {
            header = new JSONObject();
            header.put("channel", GetApplicationInfoUtil.getChannel());
            header.put("agent_id", GetApplicationInfoUtil.getAgentId());
            header.put("version_no", GetApplicationInfoUtil.getVersionNumber());
            header.put("token_id", GetApplicationInfoUtil.getTokenId());
            header.put("encryption_field", GetApplicationInfoUtil.getEncryptionField());
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return header;
    }

    /**
     * 发起网路请求
     *
     * @param listener      成功回调
     * @param errorlistener 失败回调
     * @param json          网络请求数据
     * @param url           请求接口地址
     */
    public synchronized void addJsonObjectRequest(Response.Listener<JSONObject> listener,
                                                  final Response.ErrorListener errorlistener, JSONObject json, String url) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, json, listener,
                errorlistener);
        // 超时时间 60 秒，不重试
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);

    }

    /**
     * 发起网路请求(含有 header)
     *
     * @param listener      成功回调
     * @param errorlistener 失败回调
     * @param json          网络请求数据
     * @param url           请求接口地址
     */
    public synchronized void addRequestWithHeader(Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorlistener, JSONObject json, String url) {

        JSONObject params = null;
        JSONObject header;
        try {
            header = getHeader();
            params = new JSONObject();
            params.put("header", header);
            params.put("body", json);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }

        LogUtil.d("请求参数是：" + params);
        // 封装 Volley 请求体
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, params, listener,
                errorlistener);

        //超时时间 60 秒，不重试
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }

    /**
     * 使用键值对进行网络请求
     *
     * @param listener      成功回调接口
     * @param errorListener 错误回调接口
     * @param map           封装的数据请求
     * @param url           请求链接地址
     */
    public synchronized void addMap(Response.Listener<String> listener,
                                    Response.ErrorListener errorListener, final Map<String, String> map, String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };
        // 超时时间 60 秒，不重试
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, 1.0f));
        mRequestQueue.add(stringRequest);
    }


}
