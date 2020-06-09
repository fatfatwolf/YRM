package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.hybunion.yirongma.payment.bean.TerminalBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.encoding.EncodingHandler;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.SaveImageToAlbum;

import butterknife.Bind;
import butterknife.OnClick;

public class TerminalDetailsActivity extends BasicActivity {
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.tv_tidName)
    TextView tv_tidName;
    @Bind(R.id.tv_tid)
    TextView tv_tid;
    @Bind(R.id.tv_type)
    TextView tv_type;
    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_createDate)
    TextView tv_createDate;
    @Bind(R.id.tv_fjStatus)
    TextView tv_fjStatus;
    @Bind(R.id.iv_collection_qr_code)
    ImageView iv_collection_qr_code;
    @Bind(R.id.tv_code_qr)
    TextView tv_code_qr;
    @Bind(R.id.ll_save_code)
    LinearLayout ll_save_code;
    @Bind(R.id.btn_save_image)
    Button btn_save_image;
    @Bind(R.id.ll_min_amount)
    LinearLayout ll_min_amount;
    @Bind(R.id.ll_fix_amount)
    LinearLayout ll_fix_amount;
    @Bind(R.id.tv_min_account)
    TextView tv_min_account;
    @Bind(R.id.tv_goods_account)
    TextView tv_goods_account;
    @Bind(R.id.tv_goods_name)
    TextView tv_goods_name;
    @Bind(R.id.tv_fix_account)
    TextView tv_fix_account;
    @Bind(R.id.view)
    View view;
    public String tidName,tid,type,createDate,storeName,tidUrl,limitAmt,minAmt,remark;
    private Bitmap bitmap;
    private Bitmap qrCodeBitmap;
    private Bitmap newCode;
    private TerminalBean.DataBean mBean;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_terminal_details;
    }

    @Override
    public void initView() {
        super.initView();
        tv_right.setVisibility(View.GONE);
        ll_save_code.setDrawingCacheEnabled(true);
        ll_save_code.buildDrawingCache();
        tidName = getIntent().getStringExtra("tidName");
        tid = getIntent().getStringExtra("tid");
        type = getIntent().getStringExtra("type");
        createDate = getIntent().getStringExtra("createDate");
        storeName = getIntent().getStringExtra("storeName");
        tidUrl = getIntent().getStringExtra("tidUrl");
        limitAmt = getIntent().getStringExtra("limitAmt");
        remark = getIntent().getStringExtra("remark");
        mBean = (TerminalBean.DataBean) getIntent().getSerializableExtra("bean");
        if(TextUtils.isEmpty(remark)){
            remark = "设置商品名称";
        }
        tv_goods_name.setText(remark);
        minAmt = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MIN_ACCOUNT);
        tv_tidName.setText(tidName);
        tv_tid.setText(tid);
        if(TextUtils.isEmpty(minAmt)){
            view.setVisibility(View.GONE);
            ll_min_amount.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
            tv_min_account.setText(minAmt+"元");
            ll_min_amount.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(limitAmt)){
            ll_fix_amount.setVisibility(View.GONE);
            tv_goods_name.setVisibility(View.INVISIBLE);
            tv_goods_account.setVisibility(View.GONE);
        }else {
            tv_goods_name.setVisibility(View.VISIBLE);
            tv_goods_account.setVisibility(View.VISIBLE);
            tv_goods_account.setVisibility(View.VISIBLE);
            tv_goods_account.setText("¥"+limitAmt);
            ll_fix_amount.setVisibility(View.VISIBLE);
            tv_fix_account.setText(limitAmt+"元");

        }
        if("0".equals(type)){
            if (!TextUtils.isEmpty(mBean.sn)){  // sn 不为空，则为收款设备
                tv_type.setText(mBean.snName+mBean.snModel);
                ll_save_code.setVisibility(View.VISIBLE);
                btn_save_image.setVisibility(View.VISIBLE);
            }else{
                tv_type.setText("收款码");
                ll_save_code.setVisibility(View.VISIBLE);
                btn_save_image.setVisibility(View.VISIBLE);
            }
        }else if("1".equals(type)){
            tv_type.setText("微收银插件");
            ll_save_code.setVisibility(View.GONE);
            btn_save_image.setVisibility(View.GONE);
        }
        tv_shop_name.setText(storeName);
        tv_createDate.setText(createDate);
        tv_fjStatus.setText("激活");
        tv_code_qr.setText("二维码编号:"+tid);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TerminalDetailsActivity.this,SetTerminalNameActivity.class);
                intent.putExtra("storeName",storeName);
                startActivity(intent);
            }
        });
        }

    @Override
    public void initData() {
        super.initData();
        try {
            int width = EncodingHandler.getQRWidth(this);
            if(tidUrl!=null && !tidUrl.equals("")){
                qrCodeBitmap = EncodingHandler.createQRCode(tidUrl, width, this);
                iv_collection_qr_code.setImageBitmap(qrCodeBitmap);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }
        if(tidUrl!=null && !tidUrl.equals("")) {
            newCode = mergeBitmap(qrCodeBitmap);
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
}
