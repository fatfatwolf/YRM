package com.hybunion.yirongma.payment.utils;

/**
 * Created by AngusFine on 2017/12/18.
 */

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NetWorkUtils {
    String lost = "";// 丢包
    String delay = "";// 延迟
    String min = "";//最小延迟
    String max = "";//最大延迟
    int maxDelay = 0;
    String lostPrecent = "";
    String result = "";
    private static final String tag = "TAG";// Log标志
    long delaytime = 0;
    String countCmd = " -c " + "10" + " ";
    String sizeCmd = " -s " + "64" + " ";
    String timeCmd = " -i " + "1" + " ";
    String ip_adress = "www.baidu.com";
    String ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;
    String ip = "www.baidu.com";
    String count = "10";
    String time = "1";
    String size = "64";
    private static NetWorkUtils mInstance;


    public interface NetWorkUtilsListener{
        void netWork(boolean object);
    }

    public static NetWorkUtils getInstance(){
        if (mInstance == null)
            mInstance = new NetWorkUtils();
        return mInstance;
    }

    public void getDelay(final NetWorkUtilsListener listener) {

        delaytime = (long) Double.parseDouble(time);
        Log.i(tag, "====MainThread====:" + Thread.currentThread().getId());


        new Thread()// 创建子线程
        {
            public void run() {

                delay = "";
                lost = "";

                Process process = null;
                BufferedReader successReader = null;
                BufferedReader errorReader = null;
                DataOutputStream dos = null;
                try {
                    process = Runtime.getRuntime().exec(ping);
                    Log.i(tag, "====receive====:");
                    InputStream in = process.getInputStream();
                    successReader = new BufferedReader(
                            new InputStreamReader(in));

                    errorReader = new BufferedReader(new InputStreamReader(
                            process.getErrorStream()));

                    String lineStr;
                    lostPrecent = "";
                    while ((lineStr = successReader.readLine()) != null) {

                        Log.i(tag, "====receive====:" + lineStr);
                        result = result + lineStr + "\n";
                        if (lineStr.contains("packet loss")) {
                            Log.i(tag, "=====Message=====" + lineStr.toString());
                            int i = lineStr.indexOf("received");
                            int j = lineStr.indexOf("%");
                            Log.i(tag,
                                    "====丢包率====:"
                                            + lineStr.substring(i + 10, j + 1));//
                            lost = lineStr.substring(i + 10, j + 1);
                            String[] str = lost.split("%");
                            lostPrecent = str[0];

                        }
                        if (lineStr.contains("avg")) {
                            int i = lineStr.indexOf("/", 20);
                            int j = lineStr.indexOf(".", i);
                            Log.i(tag,
                                    "====平均时延:===="
                                            + lineStr.substring(i + 1, j));
                            delay = lineStr.substring(i + 1, j);
                            delay = delay + "ms";
                        }
                        //rtt min/avg/max/mdev = 110.460/228.237/707.539/166.254 ms
                        if (lineStr.contains("min")) {
                            int i = lineStr.indexOf("=", 19);
                            int j = lineStr.indexOf(".", i);
                            Log.i(tag,
                                    "====最小时延:===="
                                            + lineStr.substring(i + 1, j));
                            min = lineStr.substring(i + 1, j);
                            min = min + "ms";
                        }
                        if (lineStr.contains("max")) {
                            int k = lineStr.indexOf("/", 20);
                            int i = lineStr.indexOf("/", k + 1);
                            int j = lineStr.indexOf(".", i);
                            Log.i(tag,
                                    "====最大时延:===="
                                            + lineStr.substring(i + 1, j));
                            max = lineStr.substring(i + 1, j);
                            maxDelay = Integer.parseInt(max);
                            max = max + "ms";
                        }
                        // tv_show.setText("丢包率:" + lost.toString() + "\n" +
                        // "平均时延:"

                        // + delay.toString() + "\n" + "IP地址:");// +
                        // getNetIpAddress()
                        // + getLocalIPAdress() + "\n" + "MAC地址:" +
                        // getLocalMacAddress() + getGateWay());
                        sleep(delaytime * 1000);
                    }
                    // tv_show.setText(result);
                    final String lost_f = lost;
                    final String delay_f = delay;
                    final String min_f = min;
                    final String max_f = max;
                    Handler mH = new Handler(Looper.getMainLooper());
                    mH.post(new Runnable() {
                        @Override
                        public void run() {


                            StringBuilder builder = new StringBuilder("");
                            builder.append("\n 丢包率：" + lost_f + " 平均延时： " + delay_f + "   最小延时" + min_f + "   最大延时" + max_f);

                            if (!TextUtils.isEmpty(lostPrecent)) {
                                    if (Integer.parseInt(lostPrecent) > 0.1 || maxDelay > 200) {
                                        //网络比较差,返回一个false
                                        Log.i("xjz", "网络比较差");
                                        listener.netWork(false);
                                        return;
                                    }else {
                                        listener.netWork(true);
                                    }
                            }else {
                                listener.netWork(false);
                            }

                        }
                    });
                    while ((lineStr = errorReader.readLine()) != null) {
                        Log.i(tag, "==error======" + lineStr);
                        // tv_show.setText(lineStr);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (dos != null) {
                            dos.close();
                        }
                        if (successReader != null) {
                            successReader.close();
                        }
                        if (errorReader != null) {
                            errorReader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (process != null) {
                        process.destroy();
                    }
                }
            }
        }.start();
    }



}

