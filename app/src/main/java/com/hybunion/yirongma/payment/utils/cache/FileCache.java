package com.hybunion.yirongma.payment.utils.cache;

import android.content.Context;

import com.hybunion.yirongma.payment.utils.FileManager;


public class FileCache extends AbstractFileCache{

	public FileCache(Context context) {
		super(context);
	
	}


	@Override
	public String getSavePath(String url) {
		String filename = String.valueOf(url.hashCode());
		return getCacheDir() + filename;
	}

	@Override
	public String getCacheDir() {
		
		return FileManager.getSaveFilePath();
	}

}
