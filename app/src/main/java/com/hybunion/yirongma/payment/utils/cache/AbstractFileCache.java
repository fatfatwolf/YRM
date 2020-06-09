package com.hybunion.yirongma.payment.utils.cache;

import android.content.Context;

import com.hybunion.yirongma.payment.utils.FileHelper;
import com.hybunion.yirongma.payment.utils.LogUtils;

import java.io.File;

public abstract class AbstractFileCache {

	private String dirString;
	
	public AbstractFileCache(Context context) {
		
		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
		LogUtils.d("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}
	
	public File getFile(String url) {
		File f = new File(getSavePath(url));
		return f;
	}
	
	public abstract String getSavePath(String url);
	public abstract String getCacheDir();

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

}
