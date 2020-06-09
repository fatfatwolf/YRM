package com.hybunion.yirongma.payment.Location;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


/**
 * 高德定位
 * Created by lcy on 2016/4/6.
 */
public class GaodeLocation implements LocationWrapper, AMapLocationListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private static GaodeLocation gaodeLocation;
    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    private GaodeLocation() {

    }

    public static GaodeLocation getSingleton(Context context) {
        if (gaodeLocation == null) {
            gaodeLocation = new GaodeLocation();
        }
        gaodeLocation.init(context);
        return gaodeLocation;
    }


    private Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            if (onLocationFinish == null) {
                return;
            }
            switch (msg.what) {
                //开始定位
                case LocationUtils.MSG_LOCATION_START:
                    onLocationFinish.locationStart();
                    break;
                // 定位完成
                case LocationUtils.MSG_LOCATION_FINISH:
                    Log.d("king", "定位完成");
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = LocationUtils.getLocationStr(loc);
                    Log.d("king","MSG_LOCATION_FINISH==" + result);
                    LocationInfo locationInfo = new LocationInfo("");
                    locationInfo.setAddress(loc.getAddress());
                    locationInfo.setLatitude(loc.getLatitude());
                    locationInfo.setCity(loc.getCity());
                    locationInfo.setProvince(loc.getProvince());
                    locationInfo.setDistrict(loc.getDistrict());
                    locationInfo.setCountry(loc.getCountry());
                    locationInfo.setStreet(loc.getStreet());
                    locationInfo.setLongitude(loc.getLongitude());
                    locationInfo.setErrorCode(loc.getErrorCode());
                    locationInfo.setErrorInfo(loc.getErrorInfo());
                    locationInfo.setAccuracy(loc.getAccuracy());
                    locationInfo.setAdCode(loc.getAdCode());
                    onLocationFinish.locationFinish(locationInfo);
                    break;
                //停止定位
                case LocationUtils.MSG_LOCATION_STOP:
                    onLocationFinish.locationStop();
                    break;
                default:
                    break;
            }
        }
    };


    private void init(Context context) {
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationOption.setOnceLocation(false);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);

        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            Message msg = mHandler.obtainMessage();
            msg.obj = aMapLocation;
            msg.what = LocationUtils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }

    private OnLocationFinish onLocationFinish;

    @Override
    public void setOnLocationFinishListener(OnLocationFinish onLocationFinish) {
        this.onLocationFinish = onLocationFinish;
    }

    public interface OnLocationFinish {

        void locationStart();

        void locationFinish(LocationInfo location);

        void locationStop();
    }

    @Override
    public void startLocation() {
        locationClient.startLocation();
        mHandler.sendEmptyMessage(LocationUtils.MSG_LOCATION_START);
    }

    @Override
    public void stopLocation() {
        if (locationClient!=null){
            locationClient.stopLocation();
            locationClient.onDestroy();
            gaodeLocation=null;
        }
    }
}
