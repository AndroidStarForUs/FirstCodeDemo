/*
 *****************************************************************************************
 * @file SystemInfoUtils.java
 *
 * @brief 
 *
 * Code History:
 *       2015年8月19日  下午2:55:18  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

/**
 * @brief
 * 
 * @author Peter
 *
 * @date 2015年8月19日 下午2:55:18
 */
@SuppressLint("NewApi")
public class SystemInfoUtils {

    private static final String TAG = "SystemInfoUtils";

    /***
     * 获取版本号
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            // 当前版本的版本号
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "===>>getVersionCode and code:" + versionCode);
        return versionCode;
    }

    /**
     * 获取版本名称
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "0.0.0";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "===>>getVersionName and versionName:" + versionName);
        return versionName;
    }

    /**
     * @brief 获取手机品牌 +型号
     * @return String
     */

    public static String getBrandModel() {
        return android.os.Build.BRAND + " " + android.os.Build.MODEL;
    }

    /**
     * @brief 获取sn号
     * @return String
     */
    public static String getSnNumber() {
         return "SZA0B1008B4Y";
        /*String sn = BoxManager.getInstance().getSnNumber();
        if (TextUtils.isEmpty(sn)) {
            sn = android.os.Build.SERIAL;
        }
        return sn;*/
    }

    /**
     * @brief 获取设备的IMEI号码
     * @param context
     * @return String
     */
    public static String getIMEI(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return phoneManager.getDeviceId();
    }

    /**
     * @brief 获取手机号
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return phoneManager.getLine1Number();
    }

    /**
     * @brief 获取系统版本
     * @param context
     * @return String
     */
    public static String getSystemVersionCode() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @brief 获取系统的SDK版本
     * @return int
     */
    public static int getSystemSDKLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * @brief 获取sim的运营商
     * @param context
     * @return String
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager phoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return phoneManager.getSimOperatorName();
    }

    /**
     * 获得SD卡总大小
     * 
     * @return
     */
    public static String getSDTotalSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     * 
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    /**
     * 获得机身运行内存总大小
     * 
     * @return
     */
    public static String getRamTotalSize(Context context) {
        return getTotalMemory(context);
    }

    /**
     * 获得机身运行可用内存
     * 
     * @return
     */
    public static String getRamAvailableSize(Context context) {
        return getAvailMemory(context);
    }

    /**
     * 获取可用运行内存
     * 
     * @return
     */
    private static String getAvailMemory(Context context) {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);// mi.availMem; 当前系统的可用内存
        String availMemory = mi.availMem + "";
        Log.d(TAG, "====>>AvailMemory:" + availMemory);
        // Formatter.formatFileSize(context, mi.availMem)
        return availMemory;// 将获取的内存大小规格化

    }

    /**
     * 获取全部运行内存
     * 
     * @return
     */
    private static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }

        String totalSize = initial_memory + "";
        Formatter.formatFileSize(context, initial_memory); // Byte转换为KB或者MB，内存大小规格化\
        Log.d(TAG, "=====>>totalSize:" + totalSize);
        return totalSize;
    }
}
