package com.hybunion.net.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hybunion.net.utils.LogUtil;
import com.hybunion.net.remote.CmlRequestBody;
import com.hybunion.net.remote.HttpEngine;
import com.hybunion.net.remote.LoadingBean;
import com.hybunion.net.remote.Subscriber;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HYBUnionAsyncHttpEngine extends HttpEngine {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    private static final int LOADING = 2;
    public final static int CONNECT_TIMEOUT = 60;
    public final static int READ_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 15;
    private final OkHttpClient client;
    private Subscriber subscriber;
    private Class<?> type;


    public HYBUnionAsyncHttpEngine(Context mContext) {
        super(mContext);
        client = new OkHttpClient().newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();
        //     client.setConnectTimeout(10000L, TimeUnit.MILLISECONDS);
        //    client.readTimeoutMillis(10000L, TimeUnit.MILLISECONDS);
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        this.type = subscriber.getType();
    }

    //post Json
    @Override
    public void postJson(String url, JSONObject jsonRequest) throws IOException {
        postJson(url, jsonRequest, true);
    }

    //post json is have header
    @Override
    public void postJson(final String url, final JSONObject jsonRequest, final boolean hasHeader) throws IOException {
        Gson gson = new Gson();

        JSONObject jsonObject;
        if (hasHeader) {
            jsonObject = new JSONObject();

            try {
                jsonObject.put("header", packageJsonHeader(mContext));
                jsonObject.put("body", jsonRequest);
            } catch (Exception e) {
                return;
            }
        } else {
            jsonObject = jsonRequest;
        }

        //将JSONObject 对象转成Json窜
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        LogUtil.i("===================================================================");
        LogUtil.i("http url = " + url);
        LogUtil.i("http request = " + jsonObject.toString());
        client.newCall(
                new Request.Builder()
                        .url(url)
                        //      .headers(packageJsonHeader(mContext))//添加头
                        .post(body)
                        .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
                //subscriber.onError(new IOException("server is not available"));
                LogUtil.i("http response error= " + e.toString());
                LogUtil.i("===================================================================");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i("http response " + response);
                if (response.isSuccessful()) {

                    if (subscriber != null) {//通过回调把数据传出去
                        Gson gson = new Gson();
                        String string = response.body().string();

                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        // subscriber.onCompleted(gson.fromJson(string, type));
//                        LogUtil.i("http response Success= " + string);
                        showLog(string);
                    }
                }else {
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                }
                LogUtil.i("===================================================================");
            }
        });
    }

    //post 键值对
    @Override
    public void postParams(final String url, final RequestBody requestBody) throws IOException {
        LogUtil.i("========================post键值对========================================");
        LogUtil.i("============URL:"+url);
        if(requestBody != null) {
            LogUtil.i("http request = " + requestBody.contentLength());
        }
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i("========================成功开始========================================");
                if (response.isSuccessful()) {
                    if (subscriber != null) {//通过回调把数据传出去
                        Gson gson = new Gson();
                        String string = response.body().string();

//                        try {
//                            Thread.sleep(700);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        //subscriber.onCompleted(gson.fromJson(string, type));
                        showLog(string);
                        LogUtil.d("返回结果为"+string);
                    }
                } else {
                    LogUtil.i("=======请求失败========");
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    //subscriber.onError(new IOException("server is not available"));
                }
                LogUtil.i("=======================成功结束=========================================");
            }
        });
    }
    /**
     * 分块请求2        可用
     *
     * @param url
     * @throws IOException
     */
    @Override
    public void postParamsPart(final String url, Map<String, String> map, List<String> paths) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (map != null && map.size() != 0) {
            if (map.keySet() != null && map.keySet().size() != 0) {
                for (String key : map.keySet()) {
                    if (key != null) {
                        builder.addFormDataPart(key, map.get(key));
                    }
                }
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (paths != null) {
            int size = paths.size();
            if (size != 0) {
                for (String path : paths) {
                    if (path != null) {
                        builder.addFormDataPart("upload", "pic", RequestBody.create(MEDIA_TYPE_PNG, new File(path)));
                    }
                }
            }
        }

        final RequestBody body = builder.build();
        LogUtil.i("=======================分块请求=========================================");
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(body)
                        .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i("=============================响应===================================");
                if (response.isSuccessful()) {
                    if (subscriber != null) {//通过回调把数据传出去
                        Gson gson = new Gson();
                        String string = response.body().string();

                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        //subscriber.onCompleted(gson.fromJson(string, type));

                        LogUtil.i("http url = " + url);
                        LogUtil.i("http request = " + body.toString());
//                        LogUtil.i("http response Success= " + string);
                        showLog(string);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    // subscriber.onError(new IOException("server is not available"));
                }
                LogUtil.i("================================================================");
            }

        });
    }

    //-===============================================================================================

    /**
     * 分块请求3
     *
     * @param url
     * @throws IOException
     */
    @Override
    public void postParamsPart(final String url, Map<String, String> map, Map<String, String> paths) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (map != null && map.size() != 0) {
            if (map.keySet() != null && map.keySet().size() != 0) {
                for (String key : map.keySet()) {
                    if (key != null) {//可以是空串
                        String value = map.get(key);
                        if (value != null) {
                            builder.addFormDataPart(key, value);
                            LogUtil.i("===========key：" + key + ",value：" + value);
                        }
                    }
                }
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (paths != null && paths.size() != 0) {
            if (paths.keySet() != null && paths.keySet().size() != 0) {
                for (String name : paths.keySet()) {
                    if (name != null && !"".equals(name)) {//空串无意义
                        String imgPath = paths.get(name);
                        if (imgPath != null && !"".equals(imgPath)) {
                            LogUtil.i("ImageName::==============" + name + "ImagePath:============" + imgPath + ",TYPE:" + MEDIA_TYPE_PNG);
                            builder.addFormDataPart(name, "file.png", RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)));
                        }
                    }
                }


            }
        }
        final RequestBody body = builder.build();
        LogUtil.i("=======================分块请求=========================================");
        LogUtil.i("===========URL==" + url);
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(new CmlRequestBody(builder.build()) {
                            @Override
                            public void loading(long current, long total, boolean done) {
                                LoadingBean bean = new LoadingBean();
                                bean.setCurrent(current);
                                bean.setTotal(total);
                                bean.setDone(done);
                                Message message = Message.obtain();
                                message.what = LOADING;
                                message.obj = bean;
                                handler.sendMessage(message);

                            }
                        })
                        .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
                //subscriber.onError(new IOException("server is not available"));
                LogUtil.i("========IOException=====" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                LogUtil.i("=============================响应===================================");
                if (response.isSuccessful()) {
                    if (subscriber != null) {//通过回调把数据传出去
                        Gson gson = new Gson();
                        String string = response.body().string();
                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        // subscriber.onCompleted(gson.fromJson(string, type));
                        LogUtil.i("http request = " + body.toString());
//                        LogUtil.i("http response Success= " + string);
                        showLog(string);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    // subscriber.onError(new IOException("server is not available"));
                    LogUtil.i("========响应失败=====");
                }
                LogUtil.i("================================================================");
            }

        });
    }

    //上传空图片
    @Override
    public void postParamsPartNull(final String url, Map<String, String> map, Map<String, String> paths) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //遍历map中所有参数到builder
        if (map != null && map.size() != 0) {
            if (map.keySet() != null && map.keySet().size() != 0) {
                for (String key : map.keySet()) {
                    if (key != null) {//可以是空串
                        String value = map.get(key);
                        if (value != null) {
                            builder.addFormDataPart(key, value);
                            LogUtil.i("===========key：" + key + ",value：" + value);
                        }
                    }
                }
            }
        }
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (paths != null && paths.size() != 0) {
            if (paths.keySet() != null && paths.keySet().size() != 0) {
                for (String name : paths.keySet()) {
                    if (name != null && !"".equals(name)) {//空串无意义
                        String imgPath = paths.get(name);
//                        if (imgPath != null && !"".equals(imgPath)) {
                            LogUtil.i("ImageName::==============" + name + "ImagePath:============" + imgPath + ",TYPE:" + MEDIA_TYPE_PNG);
                            builder.addFormDataPart(name, "file.png", new RequestBody() {
                                @Override
                                public MediaType contentType() {
                                    return null;
                                }

                                @Override
                                public void writeTo(BufferedSink sink) throws IOException {

                                }
                            });
//                        }
                    }
                }


            }
        }
        final RequestBody body = builder.build();
        LogUtil.i("=======================分块请求=========================================");
        LogUtil.i("===========URL==" + url);
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(new CmlRequestBody(builder.build()) {
                            @Override
                            public void loading(long current, long total, boolean done) {
                                LoadingBean bean = new LoadingBean();
                                bean.setCurrent(current);
                                bean.setTotal(total);
                                bean.setDone(done);
                                Message message = Message.obtain();
                                message.what = LOADING;
                                message.obj = bean;
                                handler.sendMessage(message);

                            }
                        })
                        .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
                //subscriber.onError(new IOException("server is not available"));
                LogUtil.i("========IOException=====" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                LogUtil.i("=============================响应===================================");
                if (response.isSuccessful()) {
                    if (subscriber != null) {//通过回调把数据传出去
                        Gson gson = new Gson();
                        String string = response.body().string();
                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        // subscriber.onCompleted(gson.fromJson(string, type));
                        LogUtil.i("http request = " + body.toString());
//                        LogUtil.i("http response Success= " + string);
                        showLog(string);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    // subscriber.onError(new IOException("server is not available"));
                    LogUtil.i("========响应失败=====");
                }
                LogUtil.i("================================================================");
            }

        });
    }

    @Override
    public void postParamsPart(final String url, Map<String, String> paths) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (paths != null && paths.size() != 0) {
            if (paths.keySet() != null && paths.keySet().size() != 0) {
                for (String name : paths.keySet()) {
                    if (name != null && !"".equals(name)) {//空串无意义
                        String imgPath = paths.get(name);
                        if (imgPath != null && !"".equals(imgPath)) {
                            LogUtil.i("ImageName::==============" + name + "ImagePath:============" + imgPath + ",TYPE:" + MEDIA_TYPE_PNG);
                            builder.addFormDataPart(name, "file.png", RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)));
                        }
                    }
                }
            }
        }
        final RequestBody body = builder.build();
        LogUtil.i("=======================分块请求=========================================");
        LogUtil.i("===========URL==" + url);
        client.newCall(
                new Request.Builder()
                        .url(url)
                        .post(body)
                        .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = ERROR;
                handler.sendMessage(message);
                //subscriber.onError(new IOException("server is not available"));
                LogUtil.i("========IOException=====" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.i(response+"=============================响应===================================");
                if (response.isSuccessful()) {
                    if (subscriber != null) {//通过回调把数据传出去
//                        Gson gson = new Gson();
                        String string = response.body().string();
                        Message message = Message.obtain();
                        message.obj = string;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                        // subscriber.onCompleted(gson.fromJson(string, type));
                        LogUtil.i("http url = " + url);
                        LogUtil.i("http request = " + body.toString());
//                        LogUtil.i("http response Success= " + string);
                        showLog(string);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = ERROR;
                    handler.sendMessage(message);
                    // subscriber.onError(new IOException("server is not available"));
                    LogUtil.i("========响应失败=====");
                }
                LogUtil.i("================================================================");
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SUCCESS:
                    Gson gson = new Gson();
                    if(msg.obj == ""){

                    }else{
                        try {
                            subscriber.onCompleted(gson.fromJson((String) msg.obj, type));
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            subscriber.onError(new Exception("json error"));
                        }
                    }

                    break;
                case ERROR:
                    subscriber.onError(new IOException("server is not available"));
                    break;
                case LOADING:
                    subscriber.onProcess((LoadingBean) msg.obj);
                    break;
            }

        }

    };

    public static void showLog(String str) {
        try {
            if (str == null || "".equals(str)) {
                LogUtil.i("http response Success= " + "响应结果为空");
                return;
            }

            str = str.trim();
            int index = 0;
            int maxLength = 4000;
            String finalString;
            while (index < str.length()) {
                if (str.length() <= index + maxLength) {
                    finalString = str.substring(index);
                } else {
                    finalString = str.substring(index, maxLength);
                }
                index += maxLength;
                LogUtil.i("http response Success= " + finalString);
            }
        } catch (Exception e) {
            LogUtil.i("http response Success= " + str);
        }
    }


}