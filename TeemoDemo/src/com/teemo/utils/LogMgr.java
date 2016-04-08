package com.teemo.utils;

import android.util.Log;

public final class LogMgr {

    public static boolean Initialize() {

        return true;
    }

    public static boolean Uninitialize() {

        return true;
    }

    public static void v(String strMod, String strTag, String strMsg) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        Log.v(strAndrTag, strMsg);
    }

    public static void v(String strMod, String strTag, String strMsg, Throwable tr) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        String strAndrMsg = strMsg + '\n' + Log.getStackTraceString(tr);
        Log.v(strAndrTag, strAndrMsg);
    }

    public static void i(String strMod, String strTag, String strMsg) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        Log.i(strAndrTag, strMsg);
    }

    public static void i(String strMod, String strTag, String strMsg, Throwable tr) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        String strAndrMsg = strMsg + '\n' + Log.getStackTraceString(tr);
        Log.i(strAndrTag, strAndrMsg);
    }

    public static void d(String strMod, String strTag, String strMsg) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        Log.d(strAndrTag, strMsg);
    }

    public static void d(String strMod, String strTag, String strMsg, Throwable tr) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        String strAndrMsg = strMsg + '\n' + Log.getStackTraceString(tr);
        Log.d(strAndrTag, strAndrMsg);
    }

    public static void e(String strMod, String strTag, String strMsg) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        Log.e(strAndrTag, strMsg);
    }

    public static void e(String strMod, String strTag, String strMsg, Throwable tr) {
        String strAndrTag = "[" + strMod + "] [" + strTag + "] ";
        String strAndrMsg = strMsg + '\n' + Log.getStackTraceString(tr);
        Log.e(strAndrTag, strAndrMsg);
    }

}
