package com.hybunion.yirongma.payment.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.EncryptionUtils;
import com.hybunion.yirongma.payment.utils.SaveImageToAlbum;

import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2018/2/27.
 */

public class MainMessageDetailAC extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_title;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.ll_save_code)
    LinearLayout ll_save_code;
    @Bind(R.id.webView_notice_details_activity)
    WebView webView;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.btn_save_image)
    Button btn_save_image;
    @Bind(R.id.iv_collection_qr_code)
    ImageView iv_collection_qr_code;
    @Bind(R.id.tv_title_name)
    TextView tv_title_name;
    String title,time,msgId,message,urlQr;
    private Bitmap bitmap;
    private Bitmap qrCodeBitmap;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.main_message_detail;
    }

    @Override
    public void initView() {
        title = getIntent().getStringExtra("title");
        message=getIntent().getStringExtra("message");
        time=getIntent().getStringExtra("time");
        msgId = getIntent().getStringExtra("msgId");
        urlQr = getIntent().getStringExtra("urlQr");
        ll_titlebar_back.setOnClickListener(this);
        btn_save_image.setOnClickListener(this);
        tv_title_name.setText(title);
        webView.getSettings().setSupportZoom(false);
        webView.loadDataWithBaseURL(null,"        "+message,"test/html","UTF_8",null);
//        tv_context.setText("        "+message);
        tv_time.setText(time);
        ll_save_code.setDrawingCacheEnabled(true);
        ll_save_code.buildDrawingCache();

        if(!TextUtils.isEmpty(urlQr)){
            if(urlQr.length()>4 && urlQr.substring(0,4).contains("http")){
                Glide.with(this)
                        .load(urlQr)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(iv_collection_qr_code){//加载成功
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                super.onResourceReady(bitmap, glideAnimation);

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);

                            }
                        });
            }else {
                try {
                    qrCodeBitmap = EncryptionUtils.stringtoBitmap(urlQr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(null!=qrCodeBitmap){
                    iv_collection_qr_code.setImageBitmap(qrCodeBitmap);
                }
            }


        }else {
            ll_save_code.setVisibility(View.GONE);
            btn_save_image.setVisibility(View.GONE);
        }
    }

    @Override
    protected void load() {
        super.load();
        readMsg(msgId);
    }

    /**
     * 读取信息
     */
    public void readMsg(String msgId) {
        String url = NetUrl.GET_READ_MESSAGE;
        JSONObject object = new JSONObject();
        try {
            object.put("msgId", msgId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(MainMessageDetailAC.this, url, object, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {

            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_titlebar_back:
                finish();
                break;
            case R.id.btn_save_image:
                saveToAlbum();
                break;
        }
    }

    public void saveToAlbum() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                bitmap = ll_save_code.getDrawingCache();
                SaveImageToAlbum.saveFile(this, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.show("您没有SDK卡");
        }

    }}
