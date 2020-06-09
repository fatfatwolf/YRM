package com.hybunion.yirongma.common.util.shortcutbadger.impl;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.hybunion.yirongma.common.util.shortcutbadger.Badger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pengyunlong on 2016/10/8 11:20.
 */
public class ZukHomeBadger implements Badger {
    private static final Uri CONTENT_URI = Uri.parse("content://" + "com.android.badge" + "/" + "badge");
    private static final String COUNT = "count";
    private static final String TAG = "tag";
    @Override
    public void executeBadge(Context context, ComponentName componentName, int badgeCount) {
        Bundle extra = new Bundle();
        extra.putStringArrayList("app_shortcut_custom_id", null);
        extra.putInt("app_badge_count", badgeCount);
        context.getContentResolver().call(CONTENT_URI,"setAppBadgeCount", null, extra);
    }

    /**
     * 需要根据ZUK手机确定launcher手机的名字
     * @return
     */
    @Override
    public List<String> getSupportLaunchers() {
        return Arrays.asList(
                "com.zui.launcher"
        );
    }
}
