package com.hybunion.yirongma.common.util.shortcutbadger;

import android.util.Log;

public class ShortcutBadgeException extends Exception {
    public ShortcutBadgeException(String message) {
        super(message);
    }

    public ShortcutBadgeException(String message, Exception e) {
        super(message, e);
        Log.e("PL",e.toString());
    }

}
