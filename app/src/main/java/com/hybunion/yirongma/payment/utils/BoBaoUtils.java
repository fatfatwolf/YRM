package com.hybunion.yirongma.payment.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.POWER_SERVICE;

public class BoBaoUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * 判断是否开启了通知权限 true 开启 false 未开启
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        boolean isEnabled = notification.areNotificationsEnabled();
        return isEnabled;
    }


    //获取手机类型
    private static String getMobileType() {
        return Build.MANUFACTURER;
    }



    /**
     * 忽略电池优化,未开启就弹框
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void ignoreBatteryOptimization(Context context) {

        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);

        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        String aa= context.getPackageName();
        if (!hasIgnored) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"已设置电池优化", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 判断是否忽略电池省电
     *true 开启忽略电池优化  false 不开启忽略电池优化
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isBatteryOpen(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);

        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        return hasIgnored;
    }

    /**
     * 跳转到通知管理的界面，去手动开启
     * @param context
     */
    public static void intentToNotification(Context context){
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);


    }

    /**
     * 跳转到自启动页面
     * @param context
     */
    public static void jumpStartInterface(Context context) {
        Intent intent = new Intent();
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.e("HLQ_Struggle", "******************当前手机型号为：" + getMobileType());
            String aa = getMobileType();
            ComponentName componentName = null;
            if (getMobileType().equals("Xiaomi")) { // 红米Note4测试通过
                componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (getMobileType().equals("Letv")) { // 乐视2测试通过
                intent.setAction("com.letv.android.permissionautoboot");
            } else if (getMobileType().equals("samsung")) { // 三星Note5测试通过
                componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (getMobileType().equals("HUAWEI")) { // 华为测试通过
                intent.setAction("huawei.intent.action.HSM_BOOTAPP_MANAGER");
                componentName = ComponentName.unflattenFromString("com.huawei.systemmanager/.startupmgr.ui.StartupNormalAppListActivity");//跳自启动管理
//                componentName = new ComponentName("com.huawei.systemmanager",
//                        "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (getMobileType().equals("vivo")) { // VIVO测试通过
//                componentName = ComponentName.unflattenFromString("com.iqoo.secure" +
//                        "/.safeguard.PurviewTabActivity");
//                componentName = ComponentName.unflattenFromString("com.iqoo.secure/.ui.phoneoptimize.AddWhiteListActivity");
                componentName = ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.PurviewTabActivity");
            } else if (getMobileType().equals("Meizu")) { //万恶的魅族
                // 通过测试，发现魅族是真恶心，也是够了，之前版本还能查看到关于设置自启动这一界面，
                // 系统更新之后，完全找不到了，心里默默Fuck！
                // 针对魅族，我们只能通过魅族内置手机管家去设置自启动，
                // 所以我在这里直接跳转到魅族内置手机管家界面，具体结果请看图
                componentName = ComponentName.unflattenFromString("com.meizu.safe" +
                        "/.permission.PermissionMainActivity");
            } else if (getMobileType().equals("OPPO")) { // OPPO R8205测试通过
                componentName = ComponentName.unflattenFromString("com.oppo.safe" +
                        "/.permission.startup.StartupAppListActivity");
                Intent intentOppo = new Intent();
                intentOppo.setClassName("com.oppo.safe/.permission.startup",
                        "StartupAppListActivity");
                if (context.getPackageManager().resolveActivity(intentOppo, 0) == null) {
                    componentName = ComponentName.unflattenFromString("com.coloros.safecenter" +
                            "/.startupapp.StartupAppListActivity");
                }

            } else if (getMobileType().equals("ulong")) { // 360手机 未测试
                componentName = new ComponentName("com.yulong.android.coolsafe",
                        ".ui.activity.autorun.AutoRunListActivity");
            } else {
                // 以上只是市面上主流机型，由于公司你懂的，所以很不容易才凑齐以上设备
                // 针对于其他设备，我们只能调整当前系统app查看详情界面
                // 在此根据用户手机当前版本跳转系统设置界面
                if (Build.VERSION.SDK_INT >= 9) {
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                } else if (Build.VERSION.SDK_INT <= 8) {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setClassName("com.android.settings",
                            "com.android.settings.InstalledAppDetails");
                    intent.putExtra("com.android.settings.ApplicationPkgName",
                            context.getPackageName());
                }
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    // 去 WIFI 设置界面
    public static void goToSetWIFI(Context context){
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }



}
