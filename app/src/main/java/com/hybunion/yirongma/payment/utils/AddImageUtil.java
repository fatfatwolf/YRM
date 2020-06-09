package com.hybunion.yirongma.payment.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;


public class AddImageUtil {
    /**
     * 相册
     */
    public static final int TAKE_FROM_PHOTO = 0x01;
    /**
     * 相机
     */
    public static final int TAKE_FROM_CAMERA = 0x02;



    /**
     * 从相册选择图片
     *
     * @param activity
     * @param requestCode
     */
    public static void pickImageFromPhoto(Activity activity, int requestCode) {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);

    }


    public static boolean getSDCardStatus() {

        String state = android.os.Environment.getExternalStorageState();

        // 判断SdCard是否存在并且是可用的
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            return android.os.Environment.getExternalStorageDirectory().canWrite();
        }
        return false;
    }

    public static void pickImageFromPhotoForFragment(Fragment fragment, int requestCode) {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        fragment.startActivityForResult(intent, requestCode);

    }
}
