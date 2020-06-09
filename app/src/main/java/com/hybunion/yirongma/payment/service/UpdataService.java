package com.hybunion.yirongma.payment.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import java.io.File;

/**
 * Created by admin on 2016/11/18.
 * 更新apk的服务 服务中注册了广播，在下载apk完成之后会接收到到广播
 * 下载完毕以后调用安装程序安装
 */

public class UpdataService extends Service {
    /**
     * 安卓系统下载类
     **/
    DownloadManager manager;
    private String apkName; // 下载下来的 APK 名字

    /**
     * 接收下载完的广播
     **/
    DownloadCompleteReceiver receiver;
    //存储地址
    private String DOWNLOADPATH = "/com.hybunion.yirongma.member/apk/";
    /**
     * 初始化下载器
     **/
    private void initDownManager(String url) {

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();

        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse(url));

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));down.setMimeType(mimeString);

        down.setMimeType(mimeString);

        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 显示下载界面
        down.setVisibleInDownloadsUi(true);

        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(
                DOWNLOADPATH, apkName);
        String title = getResources().getString(R.string.app_name);
        down.setTitle(title+"商户");
        // 将下载请求放入队列
        manager.enqueue(down);
        //开始下载
        ToastUtil.shortShow(getApplicationContext(), "开始下载");
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 调用下载
        String url = intent.getExtras().getString("updateUrl");
        apkName = getAPKName(url);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ DOWNLOADPATH + apkName;
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            initDownManager(url);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.shortShow(getApplicationContext(), "下载失败");
        }

//        return super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
//                    installAPK(manager.getUriForDownloadedFile(downId),context);
                    //自动安装apk
                    installAPK(context);
                } else {
                    ToastUtil.shortShow(context, "下载失败");
                }
                //停止服务并关闭广播
                UpdataService.this.stopSelf();
            }
        }

        /**
         * 调用系统的api安装apk文件
         */
        private void installAPK(Uri apk,Context context) {
            if(Build.VERSION.SDK_INT < 23){
            // 通过Intent安装APK文件
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
            // 如果不加上这句的话在apk安装完成之后点击开会崩溃
            android.os.Process.killProcess(android.os.Process.myPid());
            }else{
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+DOWNLOADPATH + apkName);
                if(file.exists()){
                    openFile(file,context);
                }
            }
        }

        /**
         * 打开文件
         * @param context
         */
        private void installAPK(Context context) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ DOWNLOADPATH + apkName);
            if(file.exists()){
                openFile(file,context);
            }else{
                Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 调用系统打开文件的方法
         * @param var0
         * @param var1
         */
        public void openFile(File var0, Context var1) {
            Intent var2 = new Intent();
            var2.addFlags(268435456);
            var2.setAction("android.intent.action.VIEW");
            String var3 = getMIMEType(var0);
            var2.setDataAndType(Uri.fromFile(var0), var3);
            try {
                var1.startActivity(var2);
            } catch (Exception var5) {
                var5.printStackTrace();
                Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
            }

        }

        /**
         * 得到文件类型
         * @param var0
         * @return
         */
        public String getMIMEType(File var0) {
            String var1 = "";
            String var2 = var0.getName();
            String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
            var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
            return var1;
        }
    }

    /**
     * 根据下载链接地址获取 APK 名字
     * @param loadUrl 下载链接地址
     * @return APK 名字
     */
    private String getAPKName(String loadUrl){

        // 因为意外下载地址的 Url 为空时，返回默认名字 “member.apk”
        if(loadUrl == null || "".equals(loadUrl)){
            return "hyb.apk";
        }

        // 从下载链接中截获文件名字
        String aPKName = loadUrl.substring(loadUrl.lastIndexOf("/"));
        if("".equals(aPKName)){
            return "hyb.apk";
        }else {
            return aPKName;
        }
    }
}
