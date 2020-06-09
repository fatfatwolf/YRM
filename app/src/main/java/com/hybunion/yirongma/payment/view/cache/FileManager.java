package com.hybunion.yirongma.payment.view.cache;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.hrt.consumer/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.hrt.consumer/files";
		}
	}
}
