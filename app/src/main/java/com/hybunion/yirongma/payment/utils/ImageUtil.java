package com.hybunion.yirongma.payment.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hybunion.yirongma.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Tools for handler picture
 *
 * @author Ryan.Tang
 */
public final class ImageUtil {

    /**
     * Check the SD card
     *
     * @return
     */
    public static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    /**
     * 对图片进行比例压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath, float targetWidth, float targetHeghit) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = targetHeghit;//这里设置高度为800f
        float ww = targetWidth;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        LogUtils.d("getimage,相册图片路径：" + srcPath);
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    /**
     * 对图片进行质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int targetSize) {

        LogUtils.d("zwl", "压缩前：" + image.getByteCount());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        if (targetSize <= 0) {//默认为100
            targetSize = 100;
        }
        while (options > 20 && baos.toByteArray().length / 1024 > targetSize) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        LogUtils.d("compressImage压缩后：" + baos.toByteArray());
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     * @param photoName
     * @param path
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName + ".png");
           // File photoFile = new File(path, photoName + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void deletePhotoAtPathAndName(String path, String fileName) {
        if (checkSDCardAvailable()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getName());
                if (files[i].getName().equals(fileName)) {
                    files[i].delete();
                }
            }
        }
    }

    public static final int TAKE_PHOTO = 0x1001;
    public static final int CHOOSE_PICTURE = 0x1002;
    public static final int CROP = 0x1003;

    /**
     * 选择图片来源
     *
     * @param
     */
    public static void showPicturePicker(final Activity context, final boolean isCorp, String[] strings, final com.hybunion.yirongma.payment.utils.ImageUtil.DialogDoThing... dialogDoThing) {
        if (strings.length == 2) {
            if (!context.getString(R.string.take_photos).equals(strings[0]) || !context.getString(R.string.album).equals(strings[1])) {
                new Throwable("String数组必须使用{拍照,相册}");
                return;
            }
        }

        //=======================弹出选择的对话框=============================================
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("文件来源");
        builder.setNegativeButton(R.string.cancel, null);
        builder.setItems(strings, new DialogInterface.OnClickListener() {
            //类型码
            int REQUEST_CODE;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //拍照
                    case 0:
                        Uri imageUri = null;
                        String fileName = null;
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (isCorp) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = TAKE_PHOTO;
                        }
                        //删除上一次截图的临时文件
                        SharedPreferences sharedPreferences = context.getSharedPreferences("temp", Context.MODE_PRIVATE);
                        com.hybunion.yirongma.payment.utils.ImageUtil.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
                        //保存本次截图临时文件名字
                        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("tempName", fileName);
                        editor.commit();
                        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context,CommentMethod.getPackageName(context)+".FileProvider", new File(Environment.getExternalStorageDirectory(), fileName)));
                        }else {
                            imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        }
                        context.startActivityForResult(openCameraIntent, REQUEST_CODE);

//                        //刷新系统相册
//                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(String.valueOf(getUri(new Intent(), imageUri, context)))));
                        break;

                    //相册获取图片
                    case 1:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (isCorp) {
                            REQUEST_CODE = CROP;
                        } else {
                            REQUEST_CODE = CHOOSE_PICTURE;
                        }


                        if (Build.VERSION.SDK_INT < 19) {
                            openAlbumIntent.setAction(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        } else {
                            openAlbumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        }

                        context.startActivityForResult(openAlbumIntent, REQUEST_CODE);
                        break;
                    case 2:
                        if (dialogDoThing != null && dialogDoThing.length > 0) {
                            dialogDoThing[0].doThing();
                        }

                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }


    public interface DialogDoThing {
        void doThing();
    }

    /**
     * 转换uri
     *
     * @param uri
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    public static Uri imageuri(Uri uri, Context activity) {
        if (uri.toString().toLowerCase().startsWith("file")) {
            return uri;
        }
        String path = "";
        boolean isDocumentUri = false;
        try {
            isDocumentUri = DocumentsContract.isDocumentUri(activity, uri);
        } catch (NoClassDefFoundError e) {
            isDocumentUri = false;
        }
        if (isDocumentUri) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                    new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(uri,
                    projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        }
        uri = Uri.fromFile(new File(path));
        return uri;
    }

    /**
     * 返回原照片的地址 从相册回来后，获取该图文件地址 可以直接去头像裁剪
     * eg:/mnt/sdcard/DCIM/Camera/C360_2013-01-07-17-26-44_org.jpg
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromPhoto(Context context, Uri uri) {
        String img_path = null;
        if (uri.toString().contains("content")) {
            Cursor cursor = null;
            try {

                //从mediastore的uri中获得文件名和路径
                String[] proj = {MediaStore.Images.Media.DATA};
                //获取本机中的所有图片
                android.support.v4.content.CursorLoader cursorLoader = new
                        android.support.v4.content.CursorLoader(context, uri, proj, null, null, null);
                Cursor actualimagecursor = cursorLoader.loadInBackground();
                //返回指定列的名称
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //定位为第一行
                actualimagecursor.moveToFirst();
                img_path = actualimagecursor.getString(actual_image_column_index);
                System.out.println("-----+++++++++=-------img_path-----------" + img_path);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    try {
                        cursor.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            img_path = uri.getPath();
            System.out.println("------------img_path-----------" + img_path);
        }
        if (CommonMethod.isEmpty(img_path))
            return "";
//			img_path = compressImage(context, img_path, 100);
        System.out.println("------------img_path----=====-------" + img_path);
        return img_path;
    }
    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static void takePhoto(final Activity context) {

        //类型码
        int REQUEST_CODE;

        Uri imageUri;
        String fileName;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        REQUEST_CODE = TAKE_PHOTO;
        //删除上一次截图的临时文件
        SharedPreferences sharedPreferences = context.getSharedPreferences("temp", Context.MODE_PRIVATE);
        ImageUtil.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));
        //保存本次截图临时文件名字
        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tempName", fileName);
        editor.commit();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, CommentMethod.getPackageName(context)+".FileProvider", new File(Environment.getExternalStorageDirectory(), fileName)));
        }else {
            imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }

        context.startActivityForResult(openCameraIntent, REQUEST_CODE);

    }


    /**
     * 获取照片的Uri
     *
     * @param data
     * @param imageUri
     * @param context
     * @return
     */
    public static Uri getUri(Intent data, Uri imageUri, Context context) {
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
            if (uri == null) {
                if (imageUri != null)
                    uri = imageUri;
                else {
                    String fileName = context.getSharedPreferences("temp", Context.MODE_PRIVATE).getString("tempName", "");
                    uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                }

            }
            uri = ImageUtil.imageuri(uri, context);
        } else {
            String fileName = context.getSharedPreferences("temp", Context.MODE_PRIVATE).getString("tempName", "");
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
        }
        return uri;
    }

}
