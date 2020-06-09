package com.hybunion.yirongma.payment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hybunion.yirongma.payment.db.BillingDataListDBManager;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YrmUtils {

    public static final int REQUEST_PERMISSION_LIST = 100;
    public static final int REQUEST_PERMISSION_CAMERA = 101;
    public static final int REQUEST_PERMISSION_READ_PHONE_STATE = 102;
    public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 103;
    public static final int REQUEST_PERMISSION_PHONE = 104;
    public static final int REQUEST_PERMISSION_LOCATION = 105;
    //判断集合是否为空
    public static boolean isEmptyList(List list) {
        if (list != null && list.size() != 0) return false;
        return true;
    }

    /**
     * 获取今日，昨日
     * @param isStart
     * @param isYesterday
     * @param date
     * @return
     */
    public static String stringDateFormat(boolean isStart, boolean isYesterday, long date) {
        SimpleDateFormat format;
        if (isStart || isYesterday)
            format = new SimpleDateFormat("yyyy-MM-dd");
        else
            format = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = new Date();
        date1.setTime(date);
        String dateAndTime = format.format(date1);
        return dateAndTime;
    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(Calendar.YEAR));
    }

    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }



    //判断是否短时间重复点击的工具类
    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }




    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }


    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {

            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    /**
     * 获取当前时间，转化
     * @param format 将格式传过来  例如： yyyy-MM-dd
     */
    public static String getNowDay(String format){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间，转化为年月日,时分秒
     */
    public static String getNowTime(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }





    /**
     * 将 yyyy-MM-dd HH:mm:ss 类型的时间转换成毫秒值。
     * @param dateStr  yyyy-MM-dd HH:mm:ss 类型的时间
     * @return
     */
    public static long getLongDate(String dateStr){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
           return 0;
        }

    }


    /**
     * 判断金额，加千位分隔符
     */

    public static String thousandSperate(String num){
        if(num!=null){
            if(num.matches("[0-9]+") || num.matches("[0-9]+[.][0-9]+")){
                Double totalAmt = Double.parseDouble(num);
                DecimalFormat df = new DecimalFormat("#,###");
                String money = df.format(totalAmt);
                return  money;
            }
        }else {
            num = "";
        }
        return num;
    }


    /**
     * 判断返回金额，加小数点后两位
     */

    public static String decimalTwoPoints(String msg){
        if(!TextUtils.isEmpty(msg)){
            if (msg.matches("[0-9]+") || msg.matches("[0-9]+[.][0-9]+") ){
                double totalAmtD = Double.parseDouble(msg);
                DecimalFormat df = new DecimalFormat("0.00");

                return df.format(totalAmtD);
            }
        }else {
            msg = "";
        }
        return msg;
    }

    /**
     * 千位分隔符添加小数点
     */
    public static String ThousandTwoPoints(String msg){
        if(msg!=null){
            String isPoint[] = msg.split("\\.");
            if(isPoint.length ==1){
                return msg+".00";
            }else if(isPoint.length == 2){
                if(isPoint[1].length() == 1){
                    return msg+"0";
                }else {
                    return msg;
                }
            }
        }

        return msg;
    }

    /**
     *
     * @param
     * @return
     */

    public static Double stringToDouble(String msg){
        if(!TextUtils.isEmpty(msg)){
            if (msg.matches("[0-9]+") || msg.matches("[0-9]+[.][0-9]+")){
                double totalAmtD = Double.parseDouble(msg);

                return totalAmtD;
            }
        }
        return 0.00;
    }

    /**
     * 给数字加上千位分隔符
     * @param number
     */
    public static String addSeparator(String number){
        Double numberD = stringToDouble(number);
        DecimalFormat df = new DecimalFormat(",###,##0.00");
        return df.format(numberD);
    }

    /**
     * 判断字符串是否全是数字
     * @param msg
     * @return
     */
    public static boolean isNumber(String msg){
        if(msg!=null){
            Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
            Matcher isNum = pattern.matcher(msg);
            if( !isNum.matches() ){
                return false;
            }
        }else {
            return false;
        }

        return true;
    }

    /**
     * 判断收款方式，用于被扫
     * @param resultData
     * @return
     */
    public static int getPayChannel(String resultData){
        int payChannel = 0;
        if(resultData.length() == 18){//微信，支付宝
            String str = resultData.substring(0,2);
            if(str.equals("10") || str.equals("12")|| str.equals("13")|| str.equals("14") || str.equals("15")){//微信
                payChannel = 55;
            }else if(str.equals("25") || str.equals("26") || str.equals("27") || str.equals("28") || str.equals("29") || str.equals("30")){
                payChannel = 56;
            }else if(str.equals("96")){  // 金福卡 小程序付款码
                payChannel = 71;

            }
        }else if(resultData.length() == 19){//二维码银联
            String str = resultData.substring(0,2);
            if(str.equals("62")){
                payChannel = 60;
            }
        }else if(resultData.length() == 21){  // 金福卡
            String str = resultData.substring(0,2);
            if (str.equals("87"))
                payChannel = 71;
        }

        return payChannel;
    }

    /**
     * Android 获取当前时间戳
     * @return
     */
    public static String getTimeStamp(){
        long time = System.currentTimeMillis()/1000;
        String str = String.valueOf(time);
        return  str;
    }


    // 将手机号处理成： 183****2196
    public static String handlePhoneNum(String phoneNum){
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length()!=11) return "";
        String first3 = phoneNum.substring(0,3);
        String last4 = phoneNum.substring(7,11);
        return first3+"****"+last4;

    }

    // 获取版本号
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 获取 app name
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取当天 0 点的时间 long 类型。
    public static String getTodayTimeLong(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
           return sdf1.parse(format+" 00:00:00").getTime()+"";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


    }

    /**
     * 计算订单列表数据库文件的大小
     */
    public static String getDBFileLength(Context context){
        File file = new File("/data/data/" + context.getPackageName() + "/databases/BillingDataBase.db");
        if (file==null) return "";
        if (!file.exists() || !file.isFile()) return "";
        DecimalFormat df = new DecimalFormat("#.0");
        double fileLength = file.length();
        Log.d("333111","fileLength: "+fileLength);
        if (fileLength == 0) return fileLength+"KB";
        String fileLengthStr = String.valueOf(file.length());
        if (fileLengthStr.length()<4){   // B 级别
            return fileLength+"B";
        }else if (fileLengthStr.length()>=4 && fileLengthStr.length()<7){   // KB 级别
            double fileLengthD = fileLength/1024;
            Log.d("333111","fileLengthD: "+fileLengthD);
            return df.format(fileLengthD)+"KB";
        }else if (fileLengthStr.length()>=7 && fileLengthStr.length()<10){  // MB 级别
            double fileLengthD = fileLength/1024D/1024D;
            return df.format(fileLengthD)+"MB";
        }else if (fileLengthStr.length()>=10){   // GB 级别
            double fileLengthD = fileLength/1024D/1024D/1024D;
            return df.format(fileLengthD)+"GB";
        }

        return "";
    }

    // 删库-准备跑路
    public static void deleteDb(Context context){
        File file = new File("/data/data/" + context.getPackageName() + "/databases/BillingDataBase.db");
        if (file.exists() && file.isFile()){
            file.delete();
        }
        BillingDataListDBManager.setNull();

    }

    // 截取字符串中的数字并返回
    public static String getNumFromStr(String str){
        if (TextUtils.isEmpty(str))
            return "";
        Pattern pattern = Pattern.compile("[^0-9.0-9]");
        Matcher matcher = pattern.matcher(str);
        String resultStr = matcher.replaceAll("").trim();
        return resultStr;
    }


    /**
     * 将时分转化为时间进行比较
     */
    public static Long TimeChangeLong(String time){
        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm")
                    .parse("1970/01/01"+" "+time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2.getTime();
    }

    /**
     * 将日期转化为long类型
     * @param day
     * @return
     */
    public static Long DayChangeToLong(String day){
        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(day);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date2.getTime();
    }


    public static String judgeMsgIsNull(String msg){
        if(null == msg){
            return "";
        }else {
            return  msg;
        }
    }

    public static String changeToDisable(String str){
        String time1[] = str.split(" ");
        String day1 = time1[0];
//		System.out.println(day1);
        String tianshu[] = day1.split(";");
//		System.out.println(tianshu[0]);
        StringBuffer buffer = new StringBuffer();
        for(int i = 0;i<tianshu.length;i++){
            switch (tianshu[i]){
                case "周一":
                    buffer.append("1,");
                    break;
                case "周二":
                    buffer.append("2,");
                    break;
                case "周三":
                    buffer.append("3,");
                    break;
                case "周四":
                    buffer.append("4,");
                    break;
                case "周五":
                    buffer.append("5,");
                    break;
                case "周六":
                    buffer.append("6,");
                    break;
                case "周日":
                    buffer.append("7,");
                    break;
            }
        }
        if(buffer.length()>0){
            String time3 = buffer.substring(0,buffer.length()-1);
            buffer.delete(0,buffer.length());
            buffer.append(time3);
            buffer.append("|");
        }
        String chooseTime = time1[1];
        String shijian[] = chooseTime.split(";");
        for(int i = 0;i<shijian.length;i++){
            buffer.append(shijian[i]+"#");
        }
        String disable = buffer.substring(0,buffer.length()-1);
        return disable;
    }

    public static String changetoHuiDiscount(String str1, String str2){
        if(TextUtils.isEmpty(str1)  || TextUtils.isEmpty(str2)){
            return "";
        }
        BigDecimal bigDecimal = new BigDecimal(str1);
        BigDecimal bigdecimal2 = new BigDecimal(str2);
        BigDecimal bigDecimal6 = bigDecimal.add(bigdecimal2);
        BigDecimal bigdecimal4 = new BigDecimal("10");
        BigDecimal bigdecimal5 = bigDecimal.multiply(bigdecimal4);
        BigDecimal bigDecimal3 = bigdecimal5.divide(bigDecimal6,1, BigDecimal.ROUND_HALF_UP);


        String discount = bigDecimal3.toPlainString();

        return discount;
    }

    public static int compareToBigDecimal(String str1,String str2){
        if(TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)){
            return 2;
        }
        BigDecimal bigDecimal = new BigDecimal(str1);
        BigDecimal bigDecimal2 = new BigDecimal(str2);

        return bigDecimal.compareTo(bigDecimal2);

    }

    // 截取字符串后4位
    public static String sub4Last(String str){
        if (TextUtils.isEmpty(str))
            return "";
        if (str.length()<=4)
            return str;
        return str.substring(str.length()-4,str.length());

    }

    public static boolean isHavePermission(Activity activity,String permission,int code){
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {permission},code);
            return false;
        }else {
            return true;
        }
    }

    private static List<String> mPermissionList = new ArrayList<>();//权限的list
    public static boolean isHavePermissionList(Activity activity, String[]permission,int code){
        mPermissionList.clear();
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission[i]);
            }
        }

        if (!YrmUtils.isEmptyList(mPermissionList) && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {//需要动态申请权限
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(activity, permissions, code);
            return false;
        }else {
            return true;
        }
    }




}
