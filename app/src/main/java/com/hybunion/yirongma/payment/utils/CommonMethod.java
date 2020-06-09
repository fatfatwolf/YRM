package com.hybunion.yirongma.payment.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.view.DigitPasswordKeyPad;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethod {

    public static final boolean DEBUG_ZHI = true;
    private static WindowManager windowmanager;
    private static View passwdview;
    private static String defaultDatePattern = null;

    public static void hidePassWdPadView() {

        if (windowmanager != null && passwdview != null) {
            LogUtils.d("hidePassWdPadView");
            windowmanager.removeView(passwdview);
            //9/22添加
            passwdview = null;
        }
    }


    public interface ConfirmListener {
        void confirm(String result);

        void cancel();
    }



    /**
     * 是否为空
     *
     * @param dostr
     * @return
     */
    public static boolean isEmpty(String dostr) {
        return dostr == null || "".equals(dostr.trim()) || "null".equals(dostr) || dostr == " ";
    }
    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }




    /**
     * 获取程序外部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    /**
     * 获取程序内部的缓存目录
     *
     * @param context
     * @return
     */
    public static File getPhoneCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(context.getCacheDir().getPath() + cacheDir);
    }

    /**
     * 改变文件夹权限
     *
     * @param permission
     * @param path
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public static void log(String tag, String msg) {
        if (Constant.LOG_DEBUG)
            Log.d(tag, msg);
    }

    /***
     * 输出  如:yyyy-MM-dd
     *
     * @param aDate
     * @return
     */

    public static final String getDate(String aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        Date bDate = null;
        if (!com.hybunion.yirongma.common.util.CommonMethod.isEmpty(aDate)) {
            df = new SimpleDateFormat(getDatePattern());
            try {
                bDate = df.parse(aDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            returnValue = df.format(bDate);
        } else {
            returnValue = "";
        }
        return (returnValue);
    }

    public static synchronized String getDatePattern() {
        defaultDatePattern = "yyyy-MM-dd";
        return defaultDatePattern;
    }

    //比较两个时间大小
    public static boolean compareDate(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    /**
     * 验证手机号码是否格式正确
     *
     * @param mobiles 要验证的手机号
     * @return 正确为true, 错误为false
     */
    public static boolean isMobileNumBer(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }

    }

    /**
     * 转化为货币形式
     *
     * @param string
     * @return
     */
    public static String getMoneyType(String string) {
        // 把string类型的货币转换为double类型。
        Double numDouble = Double.parseDouble(string);
        // 想要转换成指定国家的货币格式
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CHINA);
        // 把转换后的货币String类型返回
        String numString = format.format(numDouble);
        return numString;
    }

    /**
     * 转化为货币形式
     *
     * @param string  需要格式化的数据
     * @param digital 保留精度
     * @return 123，456.20
     */
    public static String getMoneyType(String string, int digital) {
        // 把string类型的货币转换为double类型。
        Double numDouble = Double.parseDouble(string);
        DecimalFormat format = new DecimalFormat();
        //设置分组数量为3
        format.setGroupingSize(3);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
        //设置小数分割符为'.'
        dfs.setDecimalSeparator('.');
        //设置组分割符未','
        dfs.setGroupingSeparator(',');
        format.setDecimalFormatSymbols(dfs);
        //精确到小数点后的位数
        //最小位数是两位10.1 - 10.10
        format.setMinimumFractionDigits(digital);
        //最大位数是两位10.001-10.00
        format.setMaximumFractionDigits(digital);
        // 把转换后的货币String类型返回
        String numString = format.format(numDouble);
        return numString;
    }

    /**
     * 用来判断两个类是否相等
     * @param obj1
     * @param obj2
     * @return true 相等 false 不相等
     */
    public <T> boolean compare(T obj1, T obj2 ){
        Field[] fields = obj1.getClass().getDeclaredFields();
        Object v1 = null;
        Object v2 = null;
        for (Field field:fields ){
            field.setAccessible(true);
            try {
                v1 = field.get(obj1);
                v2 = field.get(obj2);
                if (!equals(v1,v2)){
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 比较两个对象的字段是否相等
     * @param obj1
     * @param obj2
     * @return
     */
    private boolean equals(Object obj1, Object obj2) {
        if (obj1==obj2){
            return true;
        }
        if (obj1==null&&obj2!=null||(obj1!=null&&obj2==null)){
            return false;
        }
        return obj1.equals(obj2);
    }
}
