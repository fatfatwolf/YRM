package com.hybunion.yirongma.common.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;

import org.json.JSONObject;

/**
 * Created by king on 2015/9/6.
 * 封装了基本数据请求
 *
 */
public class HYBUnionVolleyApi {

    private final static String HEADER_CHANNEL = "channel";

    private final static String HEADER_ID = "agent_id";

    private final static String HEADER_VER = "version_no";

    private final static String TOKEN_ID = "token_id";

    private static boolean sendToServer(JSONObject jsonRequest, Context context, String url,
                                        Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("header", packageJsonHeader(context));
            jsonObject.put("body", jsonRequest);
        } catch (Exception e) {
            return false;
        }

        LogUtils.iking(jsonObject.toString());
        VolleySingleton.getInstance(context).addJsonObjectRequest(listener, errorListener, jsonObject, url);
        return true;
    }


    private static boolean sendToServer_old(JSONObject jsonRequest, Context context, String url,
                                            Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = VolleySingleton.getInstance(context)
                .getRequestQueue();
        VolleySingleton.getInstance(context).addJsonObjectRequest(listener, errorListener, jsonRequest, url);
        return true;

    }




    /**
     * 给请求添加header
     * @param context
     * @return
     */
    public static JSONObject packageJsonHeader(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(HEADER_CHANNEL, "android");
            jsonObject.put(HEADER_ID, Constant.AGENT_ID + "");
            jsonObject.put(HEADER_VER, getVersion(context));
            jsonObject.put(TOKEN_ID, "");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

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
















