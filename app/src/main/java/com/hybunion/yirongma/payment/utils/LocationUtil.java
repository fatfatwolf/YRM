package com.hybunion.yirongma.payment.utils;

import android.support.v4.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hybunion.yirongma.payment.utils.LogUtil;

/**
 * 定位工具类
 * Created by ljfan on 2017/7/19.
 */
public class LocationUtil {

    private final FragmentActivity mActivity;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption;

    private static AMapLocation sAMapLocation;

    private OnLocationLitener mOnLocationLitener;

    public LocationUtil(FragmentActivity act) {
        mActivity = act;
    }

    public void start() {
        start(false);
    }

    public void start(boolean isCache) {
        if(isCache && sAMapLocation != null) {
            if(mOnLocationLitener != null) {
                mOnLocationLitener.onLocationSucc(sAMapLocation);
            }
        } else {
           /* PermissionUtil.with(mActivity)
                    .listener(new PermissionUtil.RequestPermissionListener() {
                        @Override
                        public boolean onResult(boolean succ, int requestCode) {
                            if (succ) {*/
                                if (mLocationClient == null) {
                                    //初始化定位
                                    mLocationClient = new AMapLocationClient(mActivity.getApplicationContext());
                                    //设置定位回调监听
                                    mLocationClient.setLocationListener(locationListener);
                                    //初始化AMapLocationClientOption对象
                                    mLocationOption = new AMapLocationClientOption();
                                    // 是否单次定位
                                    mLocationOption.setOnceLocation(true);
                                    //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
                                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                                    //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//                                    mLocationOption.setInterval(2000);
                                    //设置是否返回地址信息（默认返回地址信息）设置定位同时是否需要返回地址描述。
                                    mLocationOption.setNeedAddress(true);
                                    //设置是否允许模拟位置,默认为true，允许模拟位置
                                    mLocationOption.setMockEnable(false);
                                    //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。设置定位请求超时时间，默认为30秒。
                                    mLocationOption.setHttpTimeOut(30000);
                                    //false 关闭缓存机制 默认开启  设置是否开启定位缓存机制
                                    mLocationOption.setLocationCacheEnable(false);
                                }
                                mLocationClient.startLocation();
                            } /*else {
                                ToastUtil.shortShow(mActivity,"定位失败");
                            }
                            return false;
                        }
                    })
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION);
        }*/
    }

    public static AMapLocation getLastLocation() {
        return sAMapLocation;
    }

    //声明定位回调监听器
    public AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            sAMapLocation = aMapLocation;
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    mLocationClient.stopLocation();
                    if(mOnLocationLitener != null) {
                        LogUtil.d(sAMapLocation.getLongitude()+"经纬度");
                        mOnLocationLitener.onLocationSucc(sAMapLocation);
                    }
                } else {
                    mOnLocationLitener.onLocationFail("定位失败");
                }
            } else {
                mOnLocationLitener.onLocationFail("定位失败");
            }
        }
    };

    public void setOnLocationLitener(OnLocationLitener l) {
        mOnLocationLitener = l;
    }

    public void onDestroy() {
        if(mLocationClient != null){
            mLocationClient.onDestroy();
        }
    }

    public interface OnLocationLitener {

        void onLocationSucc(AMapLocation location);

        void onLocationFail(String error);
    }
}
