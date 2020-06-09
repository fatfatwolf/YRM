package com.hybunion.yirongma.payment.usecase.base;

import android.content.Context;

import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.net.http.RequestImpl;
import com.hybunion.net.remote.Subscriber;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public abstract class UseCase {
    protected Context mContext;

    protected RequestImpl request;

    protected Subscriber subscriber;

    private JSONObject jsonObject;

    private RequestBody params;
    private RequestBody jsonParams;
    private RequestBody imageParams;
    Map<String, String> map;
    List<String> paths;

    Map<String, String> mMap;
    Map<String, String> mPaths;

    public UseCase(Context context) {
        this.mContext = context;
        request = RequestImpl.getInstance(mContext);
    }

    public abstract void execute(RequestIndex type);

    public UseCase setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
        request.setSubscriber(subscriber);
        return this;
    }

    public UseCase setPackage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        return this;
    }

    protected JSONObject getPackage() {
        return this.jsonObject;
    }

    public UseCase setParams(RequestBody params) {
        this.params = params;
        return this;
    }

    protected RequestBody getParams() {
        return this.params;
    }

    //=================================================================
    public UseCase setParamsPart(RequestBody jsonParams, RequestBody imageParams) {
        this.jsonParams = jsonParams;
        this.imageParams = imageParams;
        return this;
    }

    protected RequestBody getPartJson() {
        return this.jsonParams;
    }

    protected RequestBody getPartImage() {
        return this.imageParams;
    }

    //==================================================
    //正在使用 参数为：字符串集合  图片路径
    public UseCase setParamsPartMP(Map<String, String> map, List<String> paths) {
        this.map = map;
        this.paths = paths;
        return this;
    }

    protected Map<String, String> getPartMap() {
        return this.map;
    }

    protected List<String> getPartPath() {
        return this.paths;
    }

    //==================================================
    //字段 和 图片 上传
    public UseCase setParamsPartMM(Map<String, String> mMap, Map<String, String> mPaths) {
        this.mMap = mMap;
        this.mPaths = mPaths;
        return this;
    }
    //图片 上传
    public UseCase setParamsPartMM( Map<String, String> mPaths) {
        this.mPaths = mPaths;
        return this;
    }
    protected Map<String, String> getPartMMap() {
        return this.mMap;
    }

    protected Map<String, String> getPartMMPath() {
        return this.mPaths;
    }
}
