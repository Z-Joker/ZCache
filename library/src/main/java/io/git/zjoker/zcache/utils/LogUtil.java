package io.git.zjoker.zcache.utils;

import android.util.Log;

import io.git.zjoker.zcache.BuildConfig;

public class LogUtil {
    private static final String TAG = "ZCache";
    public static boolean DEBUG = true;

    public static void d(String log) {
        if (DEBUG) {
            Log.d(TAG, log);
        }
    }
}
