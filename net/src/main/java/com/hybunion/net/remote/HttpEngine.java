package com.hybunion.net.remote;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by king on 2016/6/20.
 */

public abstract class HttpEngine {

    protected final static String HEADER_CHANNEL = "channel";

    protected final static String HEADER_ID = "agent_id";

    protected final static String HEADER_VER = "version_no";

    protected final static String TOKEN_ID = "token_id";

    protected Context mContext;

    public HttpEngine(Context context) {
        this.mContext = context;
    }

    public abstract void postJson(String url, JSONObject jsonRequest) throws IOException;

    public abstract void postJson(String url, JSONObject jsonRequest, boolean hasHeader) throws IOException;

    public abstract void postParams(String url, RequestBody requestBody) throws IOException;

    // public abstract void postParamsPart(String url, RequestBody jsonBody, RequestBody imageBody) throws IOException;
    public abstract void postParamsPart(String url, Map<String, String> map, List<String> paths) throws IOException;

    public abstract void postParamsPart(String url, Map<String, String> map, Map<String, String> paths) throws IOException;

    public abstract void postParamsPart(String url, Map<String, String> paths) throws IOException;
    public abstract void postParamsPartNull(String url, Map<String, String> map, Map<String, String> paths) throws IOException;
    /**
     * 添加的头
     */
    protected static JSONObject packageJsonHeader(Context context) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HEADER_CHANNEL, "android");
            jsonObject.put(HEADER_VER, getVersion(context));
            jsonObject.put(TOKEN_ID, "");

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
}
