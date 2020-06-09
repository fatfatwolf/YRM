package com.hybunion.yirongma.payment.Location;

import android.location.Location;

/**
 * Created by lcy on 2016/4/6.
 */
public class LocationInfo extends Location {
    //经度
    private double longitude;
    //纬度
    private double latitude;
    //国家
    private String country;
    //省
    private String province;
    //市
    private String city;
    //区
    private String district;
    //地址
    private String address;
    //街道
    private String street;


    private int errorCode;

    private String errorInfo;

    private float accuracy;

    private String AdCode;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public LocationInfo(String provider) {
        super(provider);
    }

    public LocationInfo(Location l) {
        super(l);
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getAdCode() {
        return AdCode;
    }

    public void setAdCode(String adCode) {
        AdCode = adCode;
    }
}
