package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.FeedBackBean;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.ImageUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/3/26.
 */

public class NewFeedBackActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.mrbChoice)
    RadioGroup mrbChoice;
    @Bind(R.id.rb_su)
    TextView rb_su;
    @Bind(R.id.rb_gnyc)
    RadioButton rb_gnyc;
    @Bind(R.id.rb_tywt)
    RadioButton rb_tywt;
    @Bind(R.id.rb_xgnjy)
    RadioButton rb_xgnjy;
    @Bind(R.id.rb_qt)
    RadioButton rb_qt;
    @Bind(R.id.et_feedback)
    EditText et_feedback;
    @Bind(R.id.iv_pic1)
    ImageView iv_pic1;
    @Bind(R.id.iv_pic2)
    ImageView iv_pic2;
    @Bind(R.id.iv_pic3)
    ImageView iv_pic3;
    @Bind(R.id.rel_cerifi)
    LinearLayout rel_cerifi;
    @Bind(R.id.ll_rediobutton)
    LinearLayout ll_rediobutton;
    private String problemType ="4";
    private int flag = 0;
    private String pic1, pic2,pic3;//保存图片路径
    @Bind(R.id.titlebar)
    TitleBar titleBar;

    private Intent intent;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.new_activity_feed_back;
    }

    @Override
    public void initView() {
        intent=getIntent();
        if ("1".equals(intent.getStringExtra("flag"))){
            et_feedback.setHint("请上传门头照、店内经营照，包含众维码桌牌的收银台照片。并说明申诉理由。");
            rb_su.setVisibility(View.VISIBLE);
            problemType = "4";
        }else {
            et_feedback.setHint("请输入详细反馈信息");
            ll_rediobutton.setVisibility(View.VISIBLE);
            rb_gnyc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    problemType = "0";
                }
            });
            rb_tywt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    problemType = "1";
                }
            });
            rb_xgnjy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    problemType = "2";
                }
            });
            rb_qt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    problemType = "3";
                }
            });
        }
        iv_pic1.setOnClickListener(this);
        iv_pic2.setOnClickListener(this);
        iv_pic3.setOnClickListener(this);
    }
    @Override
    public void showInfo(Map map) {
        super.showInfo(map);

    }

    @OnClick({R.id.bt_save})
    public void btSave(){
        if (TextUtils.isEmpty(et_feedback.getText().toString().trim())){
            if ("1".equals(intent.getStringExtra("flag"))) {
                ToastUtil.show("申诉内容不能为空");
            }else {
                ToastUtil.show("反馈内容不能为空");
            }
        }else if(et_feedback.getText().toString().trim().length()<10){
            if ("1".equals(intent.getStringExtra("flag"))) {
                ToastUtil.show("申诉内容不能小于10个字");
            }else {
                ToastUtil.show("反馈内容不能小于10个字");
            }
        }else{
            if ("5".equals(problemType)){
                ToastUtil.show("请选择反馈问题类型");
            }else {
                String content = et_feedback.getText().toString().trim();
                String mtype = android.os.Build.MODEL; // 手机型号
                String mtyb = android.os.Build.BRAND;//手机品牌
                getFeedBack(problemType,content,mtype,mtyb);
            }
        }
    }

    public void getFeedBack(String problemType, String content,String mtype,String mtyb) {
        String url = NetUrl.CRASH_FEED_BACK;


        OkUtils.getInstance().postFile(NewFeedBackActivity.this, url, getJsonParams(problemType, content, mtype, mtyb), getImageParams(), new MyOkCallback<FeedBackBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(FeedBackBean feedBackBean) {
                String stutas = feedBackBean.getStatus();
                String msg = feedBackBean.getMessage();
                if ("1".equals(stutas)){
                    if(!TextUtils.isEmpty(msg))
                        ToastUtil.show(msg);

                    finish();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return FeedBackBean.class;
            }
        });

    }

    private Map<String, String> getJsonParams(String problemType, String content, String mtype, String mtyb) {
        Map<String, String> mMap = new HashMap<>();
        mMap.put("problemType",problemType);
        mMap.put("feedback_info", content);
        mMap.put("user_type", "1");
        mMap.put("channel", "android");
        mMap.put("feedback_type", "0");
        mMap.put("mid", "");
        mMap.put("email", "");
        mMap.put("cellphone_no", "");
        mMap.put("user_name", SharedPreferencesUtil.getInstance(NewFeedBackActivity.this).getKey("merchantName"));
        mMap.put("cellphone_brand", mtyb + "_" + mtype);
        mMap.put("agent_id", String.valueOf(Constant.AGENT_ID));
        mMap.put("version_no", HRTApplication.versionName);
        return mMap;
    }

    private Map<String, File> getImageParams() {
        Map<String, File> path = new HashMap<>();
        String pic0 = SharedUtil.getInstance(NewFeedBackActivity.this).getString("pictureNum1");
        if (!TextUtils.isEmpty(pic0)) {
            path.put("aphoto1", new File(pic0));//身份证国徽照片
        }
        String pic1 = SharedUtil.getInstance(NewFeedBackActivity.this).getString("pictureNum2");
        if (!TextUtils.isEmpty(pic1)) {
            path.put("aphoto2", new File(pic1));//身份证人像照片
        }
        String pic2 = SharedUtil.getInstance(NewFeedBackActivity.this).getString("pictureNum3");
        if (!TextUtils.isEmpty(pic2)) {
            path.put("aphoto3", new File(pic2));//银行卡正面照片
        }
        return path;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_pic1:
                if(!YrmUtils.isHavePermission(NewFeedBackActivity.this,Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA)) return;
                flag = 1;
                ImageUtil.showPicturePicker(NewFeedBackActivity.this, false, new String[]{getString(R.string.take_photos), getString(R.string.album)});
                break;
            case R.id.iv_pic2:
                if(!YrmUtils.isHavePermission(NewFeedBackActivity.this,Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA)) return;
                flag = 2;
                ImageUtil.showPicturePicker(NewFeedBackActivity.this, false, new String[]{getString(R.string.take_photos), getString(R.string.album)});
                break;
            case R.id.iv_pic3:
                if(!YrmUtils.isHavePermission(NewFeedBackActivity.this,Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA)) return;
                flag = 3;
                ImageUtil.showPicturePicker(NewFeedBackActivity.this, false, new String[]{getString(R.string.take_photos), getString(R.string.album)});
                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_CANCELED == resultCode) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case ImageUtil.TAKE_PHOTO:
            case ImageUtil.CHOOSE_PICTURE:
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                    uri = ImageUtil.imageuri(uri, this);
                } else {
                    String fileName = getSharedPreferences("temp",
                            Context.MODE_PRIVATE).getString("tempName", "");
                    uri = Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), fileName));
                }
                if (null == uri)
                    break;
                File file2 = null;
                try {
                    file2 = new File(new URI(uri.toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    break;
                }
                String path = file2.getAbsolutePath();
                String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? Environment.getExternalStorageDirectory().getAbsolutePath() : this.getCacheDir().getAbsolutePath();
                Bitmap bitmap2 = ImageUtil.getImageThumbnail(path, 300, 200);
                Bitmap bitmap =ImageUtil.getimage(path, 480, 800);
                bitmap = ImageUtil.compressImage(bitmap, 0);//压缩后的图片
                if (flag == 1) {
                    ImageUtil.savePhotoToSDCard(bitmap, cachePath, "pictureNum1");
                    pic1 = cachePath + "/pictureNum1.png";
                    iv_pic1.setImageBitmap(bitmap2);
                    SharedUtil.getInstance(context()).putString("pictureNum1", pic1);
                    iv_pic2.setVisibility(View.VISIBLE);
                }else if (flag==2){
                    ImageUtil.savePhotoToSDCard(bitmap, cachePath, "pictureNum2");
                    pic2 = cachePath + "/pictureNum2.png";
                    iv_pic2.setImageBitmap(bitmap2);
                    SharedUtil.getInstance(context()).putString("pictureNum2", pic2);
                    iv_pic3.setVisibility(View.VISIBLE);
                }else if (flag==3){
                    ImageUtil.savePhotoToSDCard(bitmap, cachePath, "pictureNum3");
                    pic3 = cachePath + "/pictureNum3.png";
                    iv_pic3.setImageBitmap(bitmap2);
                    SharedUtil.getInstance(context()).putString("pictureNum3", pic3);
                }
                break;
            default:
                break;
        }
    }
}
