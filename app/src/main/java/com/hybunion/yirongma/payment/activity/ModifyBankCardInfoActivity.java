package com.hybunion.yirongma.payment.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AllBankBean;
import com.hybunion.yirongma.payment.bean.CommitDataBean;
import com.hybunion.yirongma.payment.bean.ModifyCardMessageBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.ImageUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedUtil;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.BankIDVerification;
import com.hybunion.yirongma.payment.utils.IDNumberVerification;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.HRTToast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 变更银行卡信息
 * Created by lyf on 2017/5/20.
 */

public class ModifyBankCardInfoActivity extends BasicActivity implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.et_user_name)
    TextView mUseName;//姓名
    @Bind(R.id.et_IDCard_number)
    EditText mIDCardNumner;//身份证号
    @Bind(R.id.et_input_bankNumber)
    EditText mInputBankNumber;//银行卡号
    @Bind(R.id.tv_bankName)
    TextView mBankName;//银行名称
    @Bind(R.id.iv_bankCard_facePic)
    ImageView mBankCardFacePic;//拍照的图片

    private static final int RC_CAMERA_WRITE_PERM = 123;
    private static final int RC_SETTINGS_SCREEN = 125;
    private static final int CHOOSE_BANK = 1002;
    private int order;
    private Uri imageUri;
    private String newPicUrl;
    private String bankCardFaceFile;
    private boolean isOk = false;//是否查询银行卡名称成功
    private String mid;
    private String paymentLine;
    private String fileName;
    private String legalNum, accNum;
    private String bankAccName;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_modify_bank_card_info;
    }

    @Override
    public void initData() {
        super.initData();
        mid = SharedUtil.getInstance(context()).getString(Constants.MID);
        Intent intent = getIntent();
        bankAccName = intent.getStringExtra("bankAccName");//原有的姓名
        legalNum = intent.getStringExtra("legalNum");//原有的法人身份证号
        accNum = intent.getStringExtra("accNum");//原有的个人身份证号
        mUseName.setText(bankAccName);
        mIDCardNumner.requestFocus();
        bankNumberListener();
    }


    @OnClick(R.id.btn_commit_approve)
    //提交申请
    public void commitApprove() {
        if (checkInfo()) {
            String userName = mUseName.getText().toString().trim();//输入的姓名
            String idCardNumner = mIDCardNumner.getText().toString().trim();//输入的身份证号
            String inputBankNumber = mInputBankNumber.getText().toString().trim();
            String bankName = mBankName.getText().toString().trim();
            if (!userName.equals(bankAccName)) {
                Toast.makeText(this, "当前姓名与原有姓名不一致！", Toast.LENGTH_SHORT).show();
                return;
            }
            LogUtil.d(legalNum.toUpperCase() + "身份证号码 = ");
            LogUtil.d(accNum.toUpperCase() + "身份证号码 === ");
            LogUtil.d(SharedPreferencesUtil.getInstance(ModifyBankCardInfoActivity.this).getKey(Constants.LEGAL_NUM) + "身份证号码 = ");
            if (TextUtils.isEmpty(accNum.toUpperCase())) {
                if (!TextUtils.isEmpty(legalNum.toUpperCase())) {
                    if (!(idCardNumner.toUpperCase()).equals(legalNum.toUpperCase())) {
                        Toast.makeText(this, "当前身份证与原有身份证不一致！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                if (!(idCardNumner.toUpperCase()).equals(accNum.toUpperCase())) {
                    Toast.makeText(this, "当前身份证与原有身份证不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Map<String, String> mapParams = new HashMap<>();
            mapParams.put("bankBranch", bankName);//开户银行地址（银行名称）
            mapParams.put("bankAccNo", inputBankNumber);//开户银行账号
            mapParams.put("bankAccName", userName);//开户账号名称（人名）
            mapParams.put("payBankId", paymentLine);//支付系统行号
            mapParams.put("legalNum", idCardNumner);//身份证
            //mapParams.put("dUpLoad",bankCardFaceFile);//照片路径
            mapParams.put("mid", mid);

            // 提交参数
            commitModifyDataRequest(mapParams);
        }
    }


    //点击选择银行名称
    @OnClick(R.id.rl_get_bankName)
    public void getBankName() {
        Intent intent = new Intent(context(), AllBankActivity.class);
        startActivityForResult(intent, CHOOSE_BANK);
    }

    //拍照，6.0需要申请权限
    @OnClick(R.id.iv_bankCard_facePic)
    public void takeBankCardPhoto() {
        order = 1;
        cameraAndWriteTask();
    }

    /**
     * 输入银行卡号，请求银行详细信息
     */
    private void bankNumberListener() {
        mInputBankNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String bankCardNumber = mInputBankNumber.getText().toString().trim();
                int length = bankCardNumber.length();
                if ((length >= 8 && length <= 10 || length == 19) && !isOk) {
                    //查询银行信息
                    queryBankCardNumberRequest(bankCardNumber);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 查询银行卡号的详细信息
     *
     * @param bankCardNumber
     */
    private void queryBankCardNumberRequest(String bankCardNumber) {
        String url = NetUrl.QUERY_BANK_CARD;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("cardBin", bankCardNumber);
            jsonRequest.put("agent_id", ModifyBankCardInfoActivity.this.getString(R.string.AGENT_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ModifyBankCardInfoActivity.this, url, jsonRequest, new MyOkCallback<ModifyCardMessageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ModifyCardMessageBean modifyCardMessageBean) {
                String status = modifyCardMessageBean.getStatus();
                String paymentBank = modifyCardMessageBean.getPaymentBank();//银行名称
                String paymentBankImg = modifyCardMessageBean.getPaymentBankImg();//银行图标
                String cardType = modifyCardMessageBean.getCardType();//卡的类型
                String paymentLine = modifyCardMessageBean.getPaymentLine();//系统行号
                String msg = modifyCardMessageBean.getMessage();
                if ("0".equals(status)) {
                    mBankName.setText(paymentBank);
                    isOk = true;
                }
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
                return ModifyCardMessageBean.class;
            }
        });
    }

    /**
     * 提交修改的信息
     *
     * @param mMap
     */
    private void commitModifyDataRequest(Map<String, String> mMap) {
        String url = NetUrl.COMMIT_MODIFY_BANKCARD_DATA;
        Map<String, File> path = new HashMap<>();
        String imageURL = SharedPreferencesUtil.getInstance(ModifyBankCardInfoActivity.this).getKey(SharedPConstant.BANK_CARD_FACE_FILE);
        if (!TextUtils.isEmpty(imageURL))
            path.put("dUpLoad", new File(imageURL));

        OkUtils.getInstance().postFile(ModifyBankCardInfoActivity.this, url, mMap, path, new MyOkCallback<CommitDataBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(CommitDataBean commitDataBean) {
                if (commitDataBean != null) {
                    boolean success = commitDataBean.getSuccess();
                    String message = commitDataBean.getMsg();
                    if (success) {
                        finish();
                    }
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.show(message);

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
                return CommitDataBean.class;
            }
        });
    }

    /**
     * 校验输入信息是否为空
     */
    private boolean checkInfo() {
        String flag;
        if (TextUtils.isEmpty(mUseName.getText().toString().trim())) {
            flag = getString(R.string.null_hint_6);//姓名
            HRTToast.showToast(flag, context());
        } else if (!IDNumberVerification.isValid(mIDCardNumner.getText().toString().trim())) {
            flag = getString(R.string.null_hint_22);//身份证号
            HRTToast.showToast(flag, context());
        } else if (!BankIDVerification.checkBankCard(mInputBankNumber.getText().toString().trim(), context())) {
            flag = getString(R.string.null_hint_7);//银行卡号
            HRTToast.showToast(flag, context());
        } else if (TextUtils.isEmpty(mBankName.getText().toString().trim()) || " ".equals(mBankName.getText().toString().trim())) {
            flag = getString(R.string.null_hint_1);//银行名称
            HRTToast.showToast(flag, context());
        } else if (TextUtils.isEmpty(bankCardFaceFile)) {//照片
            flag = getString(R.string.null_hint_12);
            HRTToast.showToast(flag, context());
        } else {
            return true;
        }
        return false;
    }

    /**
     * 拍照
     */
    public void showPicturePicker() {
        ImageUtil.takePhoto(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImageUtil.TAKE_PHOTO://拍照
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                handlePhoto(data);
                break;

            case RC_SETTINGS_SCREEN:

                break;

            case CHOOSE_BANK://选择银行名称
                if (resultCode == Activity.RESULT_OK) {
                    AllBankBean.BankInfo info = (AllBankBean.BankInfo) data.getBundleExtra("info").getSerializable("info");
                    mBankName.setText(info.getPaymentBank());//显示银行名称
                    paymentLine = info.getPaymentLine();
                }
            default:
                break;
        }

    }

    /**
     * 处理照片
     */
    private void handlePhoto(Intent data) {
        Uri uri = ImageUtil.getUri(data, imageUri, context());
        String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? Environment.getExternalStorageDirectory().getAbsolutePath() : context().getCacheDir().getAbsolutePath();
        String imageString = ImageUtil.getPathFromPhoto(context(), uri);
        Bitmap bitmap;
        bitmap = ImageUtil.getimage(imageString, 853, 480);
        bitmap = ImageUtil.compressImage(bitmap, 0);
        switch (order) {
            case 1:
                fileName = "bankCardFaceFileName";
                newPicUrl = cachePath + "/bankCardFaceFileName.png";
                bankCardFaceFile = newPicUrl;//要上传文件的的路径
                SharedUtil.getInstance(context()).putString("bankCardFaceFile", bankCardFaceFile);
                mBankCardFacePic.setImageBitmap(bitmap);//显示拍完的照片
                break;

            default:
                break;
        }
        ImageUtil.savePhotoToSDCard(bitmap, cachePath, fileName);//保存到本地,上传的时候读取
    }


    //部分手机拍照后强制横竖屏切换
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("bankCardFaceFile", bankCardFaceFile);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        bankCardFaceFile = savedInstanceState.getString("bankCardFaceFile");
        mBankCardFacePic.setImageBitmap(BitmapFactory.decodeFile(bankCardFaceFile));//横竖屏切换显示
        SharedUtil.getInstance(context()).putString("bankCardFaceFile", bankCardFaceFile);
    }

    /**
     * 6.权限
     */
    public void cameraAndWriteTask() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context(), perms)) {
            showPicturePicker();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.camera_ask_again), RC_CAMERA_WRITE_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

}
