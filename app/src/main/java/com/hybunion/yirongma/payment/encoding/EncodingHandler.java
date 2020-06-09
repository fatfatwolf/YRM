package com.hybunion.yirongma.payment.encoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 生成二维码
 * Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
	ImgImageView.setImageBitmap(qrCodeBitmap);
 * @author ren
 *
 */
public final class EncodingHandler {
	public static Bitmap createQRCode(String str,int widthAndHeight,Context context) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, String.valueOf(ErrorCorrectionLevel.H));
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = Color.BLACK;
				}else {
					pixels[y * width + x] = Color.WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        Canvas canvas = new Canvas(bitmap);
//        Bitmap middleLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr);
//		int logoSize = widthAndHeight / 6;
//		middleLogo = ImageUtil.zoomBitmap(middleLogo, logoSize, logoSize);
//		int logoLeft = widthAndHeight / 2 - middleLogo.getWidth() / 2,
//			logoTop = widthAndHeight / 2 - middleLogo.getHeight() / 2;
//        canvas.drawBitmap(middleLogo, logoLeft, logoTop, new Paint());
//
//		String label = "hybunion";
//		float fontSize = logoSize/label.length()*2;
//
//		Paint rectPaint = new Paint();
//		rectPaint.setColor(Color.WHITE);
//		canvas.drawRect(logoLeft, logoLef.t + middleLogo.getWidth(), logoTop + middleLogo.getHeight(), logoTop + middleLogo.getHeight() + fontSize, rectPaint);
//
//		Paint labelPaint = new Paint();
//		labelPaint.setTextSize(fontSize);
//		labelPaint.setAntiAlias(true);//去除锯齿
//		labelPaint.setFilterBitmap(true);//对位图进行滤波处理
//		canvas.drawText(label,logoLeft,logoTop+middleLogo.getHeight(),labelPaint);
		return bitmap;
	}

	public static int getQRWidth(Context context){
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels - (int)(20*metric.density);
	}
}
