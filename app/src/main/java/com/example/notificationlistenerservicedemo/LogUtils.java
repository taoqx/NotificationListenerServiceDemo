package com.example.notificationlistenerservicedemo;

import android.util.Log;

public class LogUtils {

    private static String TAG = "qingxin";
    private static boolean isShow = true;

    public static void i(String message) {
        if (isShow)
            Log.i(TAG, message);
    }

    public static void d(String message) {
        if (isShow)
            Log.d(TAG, message);
    }

    public static void w(String message) {
        if (isShow)
            Log.w(TAG, message);
    }

    public static void w(Throwable e) {
        if (isShow)
            Log.w(TAG, e.toString(), e);
    }

    public static void e(Throwable e) {
        if (isShow)
            Log.e(TAG, e.toString(), e);
    }

    public static void e(String e) {
        if (isShow)
            Log.e(TAG, e);
    }

}
