package com.hybunion.yirongma.payment.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 保存图片到相册
 * Created by lyf on 2017/6/8.
 */

public class SaveImageToAlbum {
    public static void saveFile(Context context, Bitmap bmp) {
        String cachePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cachePath = "/sdcard";
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        File image = new File(cachePath, File.separator + context.getResources().getString(R.string.photo));
        File myCaptureFile = new File(cachePath);
        if (!myCaptureFile.exists()) {
            myCaptureFile.mkdir();
        }
            try {
                if(image.exists()){
                    image.delete();
                }
                image.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(image));
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), image.getAbsolutePath(), context.getResources().getString(R.string.photo), null);
            } catch (IOException e) {
                e.printStackTrace();
            }


        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(image);
        intent.setData(uri);
        context.sendBroadcast(intent); //通知图库更新
        ToastUtil.show("保存成功");

    }
}
