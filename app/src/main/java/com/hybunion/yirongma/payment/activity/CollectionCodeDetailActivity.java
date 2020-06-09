package com.hybunion.yirongma.payment.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.encoding.EncodingHandler;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.utils.SaveImageToAlbum;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 收款码详情
 * Created by lyf on 2017/5/19.
 */

public class CollectionCodeDetailActivity extends BaseActivity {

    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_serial_number)
    TextView tv_serial_number;
    @Bind(R.id.tv_stores_owned)
    TextView tv_stores_owned;
    @Bind(R.id.iv_collection_qr_code)
    ImageView iv_collection_qr_code;
    @Bind(R.id.btn_save_image)
    Button btn_save_image;
    @Bind(R.id.tv_code_qr)
    TextView tv_code_qr;
    @Bind(R.id.tv_scan_equipment)
    TextView tv_scan_equipment;
    @Bind(R.id.tv_equip_name)
    TextView tv_equip_name;
    @Bind(R.id.tv_sn_name)
    TextView tv_sn_name;
    @Bind(R.id.tv_isDocking)
    TextView tv_isDocking;
    @Bind(R.id.ll_save_code)
    LinearLayout ll_save_code;
    @Bind(R.id.ll_isReward)
    LinearLayout ll_isReward;
    @Bind(R.id.ll_skip_reward)
    LinearLayout ll_skip_reward;


    private Bitmap qrCodeBitmap = null;//二维码
    private Bitmap newCode;
    private String deviceUrl;
    private String bindDate,storeName,storeId,tid,tidName,fjStatus,sn,snName;
    private Bitmap bitmap;


    @Override
    public void initView() {
        Bundle info = getIntent().getBundleExtra("info");
        ll_save_code.setDrawingCacheEnabled(true);
        ll_save_code.buildDrawingCache();
        storeName = getIntent().getStringExtra("storeName");
        storeId = getIntent().getStringExtra("storeId");
        deviceUrl = getIntent().getStringExtra("url");
        tid = getIntent().getStringExtra("tid");
        tidName = getIntent().getStringExtra("tidName");
        fjStatus = getIntent().getStringExtra("fjStatus");
        sn = getIntent().getStringExtra("sn");
        snName = getIntent().getStringExtra("snName");
        tv_shop_name.setText(tidName);
        tv_stores_owned.setText(storeName);
        tv_serial_number.setText(tid);
        if(sn!=null){
            if(sn.equals("")){
                ll_isReward.setVisibility(View.GONE);
            }else {
                tv_sn_name.setText(sn);
                ll_isReward.setVisibility(View.VISIBLE);

                if(snName!=null)
                    tv_equip_name.setText(snName);

                if(fjStatus!=null)
                     tv_isDocking.setText(fjStatus);

            }
        }


    }

    /**
     * 两张图片合成一张
     * @param qrCodeBitmap
     * @return
     */
    private Bitmap mergeBitmap(Bitmap qrCodeBitmap){
        Bitmap codeBg = BitmapFactory.decodeResource(getResources(),R.drawable.qrcode_bg_icon);
        Bitmap bitmap = Bitmap.createBitmap(codeBg.getWidth(),codeBg.getHeight(),codeBg.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(codeBg,new Matrix(),null);
        canvas.drawBitmap(qrCodeBitmap,530,651,null);

        return bitmap;
    }

    @Override
    public void initData() {
        try {
            int width = EncodingHandler.getQRWidth(this);
            if(deviceUrl!=null && !deviceUrl.equals("")){
                qrCodeBitmap = EncodingHandler.createQRCode(deviceUrl, width, this);
                iv_collection_qr_code.setImageBitmap(qrCodeBitmap);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        if(deviceUrl!=null && !deviceUrl.equals("")) {
            newCode = mergeBitmap(qrCodeBitmap);
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_collection_code;
    }

    @Override
    protected void loadData() {

    }

    //保存到相册
    @OnClick(R.id.btn_save_image)


    public void saveToAlbum() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                bitmap = ll_save_code.getDrawingCache();
//                saveBitmap(ll_save_code);
                SaveImageToAlbum.saveFile(this, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.show("您没有SDK卡");
        }

    }
//    //保存签名文件
//    protected boolean saveBitmap(View rl_share_lmf) {
//        try {
//            // 将图片保存到所有图片文件夹中
//            Bitmap bitmap = CommonMethod.takeScreenShot(rl_share_lmf);
//            int signPicWidth = rl_share_lmf.getWidth();
//            int signPicHeight = rl_share_lmf.getHeight() - CommonMethod.dip2px(this, 5);
//            Bitmap bmptemp = Bitmap.createBitmap(bitmap, 0, 0, signPicWidth, signPicHeight);
//            if (bmptemp == null) {
//                return false;
//            } else {
//                write(bmptemp);
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    final MediaScannerConnection msc = new MediaScannerConnection(CollectionCodeDetailActivity.this, new MediaScannerConnection.MediaScannerConnectionClient() {
//        public void onMediaScannerConnected() {
//            msc.scanFile("/sdcard/image.jpg", "image/jpeg");
//        }
//
//        public void onScanCompleted(String path, Uri uri) {
//            msc.disconnect();
//        }
//    });
//    public void write(Bitmap bmptemp) {
//        MediaStore.Images.Media.insertImage(getContentResolver(), bmptemp, "", "");
//        msc.connect();
//    }
}
