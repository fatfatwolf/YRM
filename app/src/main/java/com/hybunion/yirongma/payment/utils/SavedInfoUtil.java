package com.hybunion.yirongma.payment.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SunBingbing
 * @date 2017/5/26
 * @email freemars@yeah.net
 * @description 从本地获取保存的数据信息
 */

public class SavedInfoUtil {

    public static File picFolder, objFolder, picAllFileFolder;
    /**
     * 获取 Mid
     * @param context 上下文
     * @return Mid
     */
    public static String getMid(Context context) {
        return SharedPreferencesUtil.getInstance(context).getKey("mid");
    }

    /**
     * 初始化内存卡
     *
     * @param context
     */
    public static void initSdcard(Context context) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            picFolder = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/shop/.picfolder");
            objFolder = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/shop/.objfolder");
            picAllFileFolder = new File(Environment
                    .getExternalStorageDirectory().getAbsoluteFile()
                    + "/shop/picAllFileFolder");
        } else {
            picFolder = new File(context.getCacheDir().getAbsolutePath()
                    + "/shop/.picfolder");
            objFolder = new File(context.getCacheDir().getAbsolutePath()
                    + "/shop/.objfolder");
            picAllFileFolder = new File(context.getCacheDir().getAbsolutePath()
                    + "/shop/picAllFileFolder");
        }
        if (!picFolder.exists()) {

            picFolder.mkdirs();
        }
        if (!objFolder.exists()) {

            objFolder.mkdirs();
        }
        if (!picAllFileFolder.exists()) {

            picAllFileFolder.mkdirs();
        }
    }

    /**
     * 对当前界面进行截屏
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
         /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();

        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

//        Matrix matrix = new Matrix();
//        matrix.postScale(0.5f, 0.5f);//产生缩放后的Bitmap对象

        //去掉状态栏
//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width, height - statusBarHeight,matrix,true);
        Bitmap bitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), width, height - statusBarHeight, true);
        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    public static String GetCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
