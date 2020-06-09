/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.hybunion.yirongma.payment.view.FloatWindowView.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.view.FloatWindowView.permission.rom.HuaweiUtils;
import com.hybunion.yirongma.payment.view.FloatWindowView.permission.rom.MeizuUtils;
import com.hybunion.yirongma.payment.view.FloatWindowView.permission.rom.MiuiUtils;
import com.hybunion.yirongma.payment.view.FloatWindowView.permission.rom.QikuUtils;
import com.hybunion.yirongma.payment.view.FloatWindowView.permission.rom.RomUtils;

import java.lang.reflect.Field;


/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:悬浮窗权限适配，很全的适配http://blog.csdn.net/self_study/article/details/52859790
 */

public class FloatPermissionManager {
    private static final String TAG = "FloatPermissionManager";

    private static volatile FloatPermissionManager instance;

    private Dialog dialog;

    public static FloatPermissionManager getInstance() {
        if (instance == null) {
            synchronized (FloatPermissionManager.class) {
                if (instance == null) {
                    instance = new FloatPermissionManager();
                }
            }
        }
        return instance;
    }

    public boolean applyFloatWindow(Activity activity) {
        if (checkPermission(activity)) {
            return true;
        } else {
            applyPermission(activity);
            return false;
        }
    }

    private boolean checkPermission(Activity activity) {
//        if (android.os.Build.BRAND)
        String brand = android.os.Build.BRAND;
        if ("vivo".equals(brand)) {
            return true;
        }
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(activity);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(activity);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(activity);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(activity);
            }
        }
        return commonROMPermissionCheck(activity);
    }

    private boolean huaweiPermissionCheck(Activity activity) {
        return HuaweiUtils.checkFloatWindowPermission(activity);
    }

    private boolean miuiPermissionCheck(Activity activity) {
        return MiuiUtils.checkFloatWindowPermission(activity);
    }

    private boolean meizuPermissionCheck(Activity activity) {
        return MeizuUtils.checkFloatWindowPermission(activity);
    }

    private boolean qikuPermissionCheck(Activity activity) {
        return QikuUtils.checkFloatWindowPermission(activity);
    }

    private boolean commonROMPermissionCheck(Activity activity) {
        //魅族6.0的系统这种方式不好用，单独适配
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(activity);
        } else {
            Boolean result = true;
            int mode = -1;
            if (Build.VERSION.SDK_INT >= 23) {
//                try {
//                    Class clazz = Settings.class;
//                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
//                    result = (Boolean) canDrawOverlays.invoke(null, activity);
//                } catch (Exception e) {
//                    Log.e(TAG, Log.getStackTraceString(e));
//                }
                AppOpsManager appOpsMgr = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
                mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), activity.getPackageName());
                Log.e("121121", " mode=" + mode);
                result = mode == 0;
            }
            return result;
        }
    }

    private void applyPermission(Activity context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            }
        }
        commonROMPermissionApply(context);
    }

    private void ROM360PermissionApply(final Activity activity) {
        showConfirmDialog(activity, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(activity);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Activity activity) {
        showConfirmDialog(activity, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(activity);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Activity context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Activity activity) {
        showConfirmDialog(activity, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(activity);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Activity context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                Class clazz = Settings.class;
                                Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

                                Intent intent = new Intent(field.get(null).toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                                }
                                context.startActivity(intent);
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        }
                    }
                });
            }
        }
    }

    private void showConfirmDialog(Activity context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(final Activity activity, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            if (dialog.getOwnerActivity() != null && !dialog.getOwnerActivity().isFinishing())
                dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(activity).setCancelable(true).setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(false);
                                SharedPreferencesUtil.getInstance(activity).putKey(SharedPConstant.FLOAT_IS_SHOW, 0);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.setOwnerActivity(activity);
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
}
