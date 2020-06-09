package com.hybunion.yirongma.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.view.MyDialogView;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected LayoutInflater mInflater;
    private RelativeLayout mProgressDialog;
    private View activity_dialog;
    private MyDialogView myDialogView;
    private TextView tv_loading_title;

    protected abstract int getLayoutId();

    protected View view;

    protected Activity mActivity;
    private static final int REQUEST_CALL_PHONE = 102;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        mProgressDialog = (RelativeLayout) inflater.inflate(R.layout.dialog_progressbar, null);
        tv_loading_title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
        activity_dialog = getActivity().getLayoutInflater().inflate(R.layout.activity_dialog_icon, null);
        myDialogView = (MyDialogView) activity_dialog.findViewById(R.id.myDialog);
        initView();
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private static final long FAST_CLICK_LIMIT = 1000L;
    private long mPreviousClickTime;

    
    protected boolean isFastClick() {
        if (System.currentTimeMillis() - mPreviousClickTime < FAST_CLICK_LIMIT) {
            mPreviousClickTime = System.currentTimeMillis();
            LogUtils.d(TAG, "click too fast!");
            return true;
        } else {
            mPreviousClickTime = System.currentTimeMillis();
            return false;
        }
    }

    public void showProgressDialog(String title) {
        if (title != null && !"".equals(title)) {
            tv_loading_title.setText(title);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
                ViewGroup top = (ViewGroup) getActivity().getWindow().getDecorView();
                top.addView(activity_dialog);
                myDialogView.startAnim();
            }
        });
    }

    public void hideProgressDialog() {
        Context context = getActivity();
        if (context == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
            }
        });
    }


    //权限的回调处理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case YrmUtils.REQUEST_PERMISSION_LIST:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//未申请权限
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i]);
                        if (showRequestPermission) {//已申请

                        } else {//未申请
                            ToastUtil.show("请到应用管理中开启相应权限");
                        }
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取相机权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_PHONE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取电话权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取存储权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                    if (!showRequestPermission) {//false为未申请
                        ToastUtil.show("获取电话权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

}
