package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.OpenNewStoreBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.ImageUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class OpenNewStoreActivity extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.bt_new_store)
    Button bt_new_store;
    @Bind(R.id.et_shop_name)
    EditText tv_shop_name;
    @Bind(R.id.et_shop_address)
    EditText tv_shop_address;
    @Bind(R.id.et_shop_telephone)
    EditText tv_shop_telephone;
    @Bind(R.id.iv_photo)
    ImageView iv_photo;
    TextView tv_head;
    LinearLayout ll_back;

    String pic,storeName,storeAddr,storePhone,merchantID;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_open_new_store;
    }


    @Override
    public void initView() {
        super.initView();
        bt_new_store = (Button) findViewById(R.id.bt_new_store);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setText("新增门店");
        iv_photo.setOnClickListener(this);
        bt_new_store.setOnClickListener(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        merchantID = SharedPreferencesUtil.getInstance(OpenNewStoreActivity.this).getKey("merchantID");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_new_store:
                storeName = tv_shop_name.getText().toString().trim();
                storeAddr = tv_shop_address.getText().toString().trim();
                storePhone = tv_shop_telephone.getText().toString().trim();
                addStoreDetail(merchantID,storeName,storeAddr,storePhone,pic);
                break;
            case R.id.iv_photo:
                if(!YrmUtils.isHavePermission(OpenNewStoreActivity.this,Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA))  return;
                com.hybunion.yirongma.payment.utils.ImageUtil.showPicturePicker(OpenNewStoreActivity.this, false, new String[]{"拍照", "相册"});
                break;
            case R.id.tv_search:

                break;

            case R.id.ll_back:
                finish();
                break;
        }
    }

    public void addStoreDetail(String merchantID,String storeName,String storeAddr,String storePhone,String storePhoto){
        String url = NetUrl.ADD_STORE_DETAIL;
        Map<String, String> tMap = new HashMap<>();
        //等陆后保存的四个参数
        tMap.put("merId", merchantID);
        tMap.put("storeName", storeName);
        tMap.put("storeAddr", storeAddr);
        tMap.put("storePhone", storePhone);
        Map<String, File> pMap;
        if (!TextUtils.isEmpty(storePhoto)) {
            pMap = new HashMap<>();
            pMap.put("storePhoto", new File(storePhoto));
        }else{
            pMap = null;
        }
        OkUtils.getInstance().postFile(OpenNewStoreActivity.this, url, tMap, pMap, new MyOkCallback<OpenNewStoreBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(OpenNewStoreBean openNewStoreBean) {
                if ("0".equals(openNewStoreBean.getStatus())){   // 门店添加成功
                    ToastUtil.showShortToast("添加门店成功");
                    setResult(11);
                    finish();
                }else{
                    if(!TextUtils.isEmpty(openNewStoreBean.getMsg()))
                        ToastUtil.showShortToast(openNewStoreBean.getMsg());
                }

            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return OpenNewStoreBean.class;
            }
        });
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
                pic = path;
                break;
            case 0:
                if (resultCode == Activity.RESULT_OK) {


                }
                break;
            default:
                break;
        }
    }

}
