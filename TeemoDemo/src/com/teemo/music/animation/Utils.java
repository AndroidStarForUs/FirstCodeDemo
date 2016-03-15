/*
*****************************************************************************************
* @file Utils.java
*
* @brief 
*
* Code History:
*       2016-2-17  下午5:09:20  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.music.animation;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午5:09:20
 */
public class Utils {
    private int screenWidth = -1;
    private int screenHeight = -1;
    private float screenDensity = -1.0f;

    public static Utils sInstance = null;

    public static Utils getInstance() {
        if (sInstance == null) {
            synchronized (Utils.class) {
                sInstance = new Utils();
            }
        }
        return sInstance;
    }
    /*****
     * @brief 获取屏幕的像素宽度
     */
    public synchronized int getScreenWidthPx(Context context) {
        if (screenWidth <= 0) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
        }

        return screenWidth;
    }

    /*****
     * @brief 获取屏幕的像素高度
     */
    public synchronized int getScreenHeightPx(Context context) {
        if (screenHeight <= 0) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            screenHeight = dm.heightPixels;
        }

        return screenHeight;
    }

    /*****
     * @brief 获取屏幕的像素密度
     */
    public synchronized float getScreenDensity(Context context) {
        if (screenDensity <= 0.0) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            screenDensity = dm.density;
        }
        return screenDensity;
    }

    /**
     * @brief 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public int px2dip(Context context, float pxValue) {
        final float scale = getScreenDensity(context);
        //final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f); 
    }

    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = getScreenDensity(context);
        //final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
