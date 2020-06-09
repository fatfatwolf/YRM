package com.hybunion.yirongma.payment.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.view.MyDialogView;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;

/**
 */
public class BaseFragment extends Fragment {
    protected RelativeLayout mProgressDialog;
    private TextView tv_loading_title;
    private MyDialogView myDialogView;
    private View activity_dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initTransparencyTitle();
        LayoutInflater li = LayoutInflater.from(getActivity());
        mProgressDialog = (RelativeLayout) li.inflate(R.layout.dialog_progressbar, null);
        tv_loading_title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
        activity_dialog = getActivity().getLayoutInflater().inflate(R.layout.activity_dialog_icon, null);
        myDialogView = (MyDialogView) activity_dialog.findViewById(R.id.myDialog);
    }
    protected void initTransparencyTitle() {
//        initTransparencyTitle(0);
    }
//    protected void initTransparencyTitle(int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
//            Window window = getActivity().getWindow();
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            View statusBarView = new View(getActivity());
//            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(getActivity(), 25));
//            if (color == 0) {
//                statusBarView.setBackgroundColor(getResources().getColor(setStatusBarColor()));
//            } else {
//                statusBarView.setBackgroundColor(getResources().getColor(color));
//            }
//
//            ViewGroup view = (ViewGroup) getActivity().getWindow().getDecorView();
//            statusBarView.setLayoutParams(lParams);
//            view.addView(statusBarView);
//            for (int i = 0; i < view.getChildCount(); i++) {
//                LogUtils.d("hxs", view.getChildAt(i).toString());
//            }
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
//            Window window = getActivity().getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (color == 0) {
//                window.setStatusBarColor(getResources().getColor(setStatusBarColor()));//calculateStatusColor(Color.WHITE, (int) alphaValue)
//            } else {
//                window.setStatusBarColor(getResources().getColor(color));
//            }
//        }
//    }
    protected int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
//    protected int setStatusBarColor() {
//        return R.color.lmf_main_color;
//    }
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


    public void showMyProgressDialog(final ViewGroup top, String titleString) {
        if (titleString != null) {
            tv_loading_title.setText(titleString);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
                top.addView(activity_dialog);
                myDialogView.startAnim();
            }
        });

    }

    //    //权限的回调处理
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
