package com.gz.family.utils;

import android.util.Log;

/**
 * Created by host on 2015/11/5.
 */
public class LogUtil {
    private static String TAG = "--------------->";

    public static void i(String info) {
        Log.i(TAG, info);
    }

    public static void i(String tag, String info) {
        Log.i(TAG + tag, info);
    }
}
