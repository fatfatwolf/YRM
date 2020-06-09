package com.hybunion.yirongma.payment.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.db.DBHelper;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Jairus on 2018/12/20.
 * 修改密码
 */

public class ResetPwdActivity extends BasicActivity {
    @Bind(R.id.edtPwd_reset_pwd_activity)
    EditText mEdtPwd;
    @Bind(R.id.imgEye_reset_pwd_activity)
    ImageView mImgEye;
    @Bind(R.id.ok_reset_pwd_activity)
    TextView mTvOk;
    @Bind(R.id.tv_loginName)
    TextView mTvPhoneNum;

    private String mUUID;
    private String mPhoneNum;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_reset_pwd;
    }

    public static void start(Context from, String phoneNum, String uuid) {
        Intent intent = new Intent(from, ResetPwdActivity.class);
        intent.putExtra("phoneNum",phoneNum);
        intent.putExtra("uuid", uuid);
        from.startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        mPhoneNum = intent.getStringExtra("phoneNum");
        mUUID = intent.getStringExtra("uuid");
        mTvPhoneNum.setText(mPhoneNum);
    }

    private boolean mIsOpen;

    @OnClick(R.id.imgEye_reset_pwd_activity)
    public void clickEye() {
        if (mIsOpen) {
            mEdtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEdtPwd.setSelection(mEdtPwd.getText().toString().length());

//            mEdtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {

            mEdtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEdtPwd.setSelection(mEdtPwd.getText().toString().length());
//            mEdtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mIsOpen = !mIsOpen;
    }

    private String mPwd;
    @OnClick(R.id.ok_reset_pwd_activity)
    public void ok() {
        mPwd = mEdtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(mPwd) || mPwd.length() < 6 || mPwd.length() > 18) {
            ToastUtil.show("请输入正确的密码");
            return;
        }
        resetPwd();
    }


    private void resetPwd() {
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("UUID", mUUID);
            jsonRequest.put("newPassword", mPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.RES_PASSWORD_URL;
        OkUtils.getInstance().post(ResetPwdActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                try {
                    hideLoading();
                    JSONObject response = new JSONObject(str);
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.show(message);
                    if (status.equals("0")) {
                        queryTwo(HRTApplication.db, mUUID);
                        HRTApplication.finishActivity(ForgetPwdActivity.class);
                        HRTApplication.finishActivity(ForgetPwdVerifyActivity.class);
                        HRTApplication.finishActivity(ChangePasswordActivity.class);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                hideLoading();
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });

    }


    /**
     * 查询数据：带参数和条件的
     *
     * @param db
     */
    private void queryTwo(SQLiteDatabase db, String uid) {
        Cursor cursor = db.query("loginTable", new String[] { "upswd" }, "uid=?", new String[]
                {uid}, null, null, null);
        while (cursor.moveToNext()) {
            String userPswd = cursor.getString(cursor.getColumnIndex("upswd"));
            if (userPswd!=null || !"".equals(userPswd)){
                upadteData(HRTApplication.db);
            }
            LogUtils.dlyj(userPswd+"查询单条密码");
        }
    }
    /**
     * 修改数据 ,第一种方法的代码：
     */
    private void upadteData(SQLiteDatabase db) {
        ContentValues value = new ContentValues();
        value.put("upswd", mPwd);
        DBHelper helper = new DBHelper(ResetPwdActivity.this, "loginTable");
        db = helper.getWritableDatabase();
        db.update("loginTable", value, "uid=?", new String[]{mUUID});
    }


}
