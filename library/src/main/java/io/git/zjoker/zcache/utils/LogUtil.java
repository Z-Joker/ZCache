package io.git.zjoker.zcache.utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "ZCache";
    public static boolean DEBUG = false;

    public static void d(String log) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }
}
