package com.hybunion.netlibrary.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hybunion.netlibrary.UtilsLib;

public class ImageLoadUtil {
    private static ImageLoadUtil mInstance;
    private ImageLoadUtil(){}

    public static ImageLoadUtil getInstance(){
        if (mInstance == null){
            synchronized(ImageLoadUtil.class){
                if (mInstance == null){
                    mInstance = new ImageLoadUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 图片加载
     * @param url
     * @param iv
     */
    public static void loadImage(String url, ImageView iv){
        Glide.with(UtilsLib.getContext()).load(url).into(iv);
    }


    /**
     * 带有加载错误图片的方法
     * @param url
     * @param iv
     * @param resError
     */
    public static void loadImage(String url, ImageView iv, int resError){
        Glide.with(UtilsLib.getContext())
                .load(url)
                .error(resError)
                .into(iv);
    }

    /**
     * 带有加载中图片和加载错误图片的方法
     * @param url
     * @param iv
     * @param resLoading  加载中图片
     * @param resError    加载错误图片
     */
    public static void loadImage(String url, ImageView iv, int resLoading, int resError){
        Glide.with(UtilsLib.getContext())
                .load(url)
                .placeholder(resLoading)
                .error(resError)
                .into(iv);
    }





}
