package com.hybunion.yirongma.payment.view.cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    private Context context;
    private MemoryCache memoryCache = new MemoryCache();
    private AbstractFileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    private ExecutorService executorService;
    private static ImageLoader imageLoader;

    public static ImageLoader getInstance(Context context) {
        if (imageLoader == null)
            imageLoader = new ImageLoader(context.getApplicationContext());
        return imageLoader;
    }

    private ImageLoader(Context context) {
        this.context = context.getApplicationContext();
        fileCache = new FileCache(this.context);
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(5);
        }
    }

    // 当进入listview时默认的图片，可换成你自己的默认图片
//    final int stub_id = R.drawable.stub; 
    public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        imageViews.put(imageView, url);
        // 先从内存缓存中查找
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else if (!isLoadOnlyFromCache) {
            // 若没有的话则开启新线程加载图片
            queuePhoto(url, imageView);
            //添加自己设置的默认图片
//			imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url, int requiredWidth) {
        File f = fileCache.getFile(url);
        Bitmap b = null;
        if (f != null && f.exists()) {
            b = decodeFile(f, requiredWidth);
        }
        if (b != null) {
            return b;
        }
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f, requiredWidth);
            return bitmap;
        } catch (Exception ex) {
            Log.d("", "getBitmap catch Exception...\nmessage = " + ex.getMessage());
            return null;
        }
    }

    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
    private Bitmap decodeFile(File f, int requiredWidth) {
        try {
            // decode image size
            FileInputStream in1 = new FileInputStream(f);
            BitmapFactory.decodeStream(in1);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            if (requiredWidth > 0) {
                scale = width_tmp / requiredWidth;
                if (scale < 1) scale = 1;
            } else if (width_tmp > getScreenWidth(context)) {
                scale = width_tmp / getScreenWidth(context);
                if (scale < 1) scale = 1;
            }
            in1.close();
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream in2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(in2, null, o2);
            in2.close();
            return bitmap;
        } catch (Exception e) {
            Log.d(getClass().getName(), e.toString() + " " + e.getMessage());
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url, photoToLoad.imageView.getWidth());
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    /**
     * 防止图片错位
     *
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    // 用于在UI线程中更新界面
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);

        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            Log.d("", "CopyStream catch Exception...");
        }
    }

    private int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
}