package com.weikk.utils;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @Descripttion:
 * @Author: Kevin
 * @Email: weikk90@163.com
 * @Date: 2019-07-15 00:37:13
 * @LastEditors: Kevin
 * @LastEditTime: 2019-07-15
 */
public class Logger {
    public static boolean logSwitch = true;

    public static void close() {
        logSwitch = false;
    }

    public static void open() {
        logSwitch = true;
    }

    public static void d(@NonNull String tag, String msg) {
        if (logSwitch)
            Log.d(tag, msg);
    }

    public static void i(@NonNull String tag, String msg) {
        if (logSwitch)
            Log.i(tag, msg);
    }

    public static void w(@NonNull String tag, String msg) {
        if (logSwitch)
            Log.w(tag, msg);
    }

    public static void e(@NonNull String tag, String msg) {
        if (logSwitch)
            Log.e(tag, msg);
    }

    public static void d(String msg) {
        String tag = getClassName();
        if (logSwitch)
            Log.d(tag, msg);
    }

    public static void i(String msg) {
        String tag = getClassName();
        if (logSwitch)
            Log.i(tag, msg);
    }

    public static void w(String msg) {
        String tag = getClassName();
        if (logSwitch)
            Log.w(tag, msg);
    }

    public static void e(String msg) {
        String tag = getClassName();
        if (logSwitch)
            Log.e(tag, msg);
    }

    private static String getClassName() {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        String result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1);
        return result;
    }
}
