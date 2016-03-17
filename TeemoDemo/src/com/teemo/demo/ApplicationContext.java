package com.teemo.demo;

import android.content.Context;
import android.os.Handler;

public class ApplicationContext {

    private static String mPacketName = null;
    private static Context mMainContext = null;

    public static final boolean isTestUI = true;

    public static Handler mhandler = null;

    public static void ContextSet(String PacketName, Context MainContext) {
        mPacketName = PacketName;
        mMainContext = MainContext;
    }

    public static String GetPacketName() {
        return mPacketName;
    }

    public static Context GetMainContext() {
        return mMainContext;
    }

    public static Handler getMhandler() {
        return mhandler;
    }

    public static void setMhandler(Handler mhandler) {
        ApplicationContext.mhandler = mhandler;
    }

}
