package com.hybunion.yirongma.payment.utils;


import com.hybunion.yirongma.common.util.CommonUtil;

public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.hrt.consumer/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.hrt.consumer/files";
		}
	}
}
