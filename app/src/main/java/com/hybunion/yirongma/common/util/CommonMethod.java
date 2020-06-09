package com.hybunion.yirongma.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hybunion.yirongma.HRTApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用了
 */
public class CommonMethod {

	public static final boolean DEBUG_HUI = true;
	public static final boolean DEBUG_ZHI = true;

	/**
	 * 是否为空
	 * 
	 * @param dostr
	 * @return
	 */
	public static boolean isEmpty(String dostr) {
        return dostr == null || "".equals(dostr.trim()) || "null".equals(dostr) || dostr == " ";
    }

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			// 如果仅仅是用来判断网络连接　　　　　　
			// 则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取可以使用的缓存目录
	 * 
	 * @param context
	 * @param uniqueName
	 *            目录名称
	 * @return
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath() : getPhoneCacheDir(context).getPath();

		File file = new File(cachePath + File.separator + uniqueName);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!file.canWrite()) {
			chmod("777", cachePath);
			// file.setWritable(true);
		}
		return file;
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


}
