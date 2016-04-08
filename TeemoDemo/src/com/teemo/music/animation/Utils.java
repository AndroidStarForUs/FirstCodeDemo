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

import com.teemo.utils.LogMgr;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-2-17 下午5:09:20
 */
public class Utils {
    private final String MODULE = Utils.class.getSimpleName();
    private final String TAG = Utils.class.getSimpleName();

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

    public String formatTimeStr(String timeStr) {
        String result = "";
        if (timeStr == null) {
            return result;
        }
        String[] formatStr = timeStr.split(",");
        result = getHeard(formatStr);
        result = result + getTime(formatStr);
        result = result + getTimes(formatStr[7]);
        return result;
    }

    private String getHeard(String[] formatStr) {
        String week = formatStr[6];
        String heard = "";
        if (!week.equals("*")) {
            heard = getWeekHeard(formatStr);
        } else {
            heard = getCalendarHeard(formatStr);
        }
        return heard;
    }

    private String getWeekHeard(String[] formatStr) {
        String weekStr = formatStr[6];
        String[] weekDay = null;
        String result = "";
        String suffix = "";
        if (weekStr.contains("|")) {
            weekDay = weekStr.split("[|]");
            suffix = "、";
        } else if (weekStr.contains("-")) {
            weekDay = weekStr.split("-");
            suffix = "到";
        } else {
            weekDay = new String[] {weekStr};
        }
        for (String string : weekDay) {
            result = result + getWeekValue(string) + suffix;
        }
        if (result.endsWith("、") || result.endsWith("到")) {
            result = result.substring(0, result.length() - 1);
        }
        LogMgr.d(MODULE, TAG, "[getWeekHeard] :: result = " + result);
        return result;
    }

    private String getCalendarHeard(String[] formatStr) {
        String result = "";
        if (formatStr[0].equals("*") && formatStr[1].equals("*") && formatStr[2].equals("*")) {
            result = "每天";
        } else if (formatStr[0].equals("*") && formatStr[1].equals("*")) {
            result = "每月";
            result = result + getDayValue(formatStr[2]);
        } else if (formatStr[0].equals("*")) {
            result = "每年";
            result = result + getMonthValue(formatStr[1]);
            result = result + getDayValue(formatStr[2]);
        } else if (!formatStr[0].equals("*") && formatStr[1].equals("*") && formatStr[2].equals("*")) {
            result = getYearValue(formatStr[0]);
            result = result + "每天";
        } else if (!formatStr[0].equals("*") && formatStr[1].equals("*") && !formatStr[2].equals("*")) {
            result = getYearValue(formatStr[0]);
            result = result + "每月" + getDayValue(formatStr[2]);
        } else if (!formatStr[0].equals("*") && !formatStr[1].equals("*") && !formatStr[2].equals("*")) {
            result = getYearValue(formatStr[0]);
            result = result + getMonthValue(formatStr[1]) + getDayValue(formatStr[2]);
        }
        return result;
    }

    private String getYearValue(String year) {
        String result = "";
        if (year == null) {
            return result;
        }
        String[] years = null;
        String suffix = "";
        if (year.contains("|")) {
            years = year.split("[|]");
            suffix = "、";
        } else if (year.contains("-")) {
            years = year.split("-");
            suffix = "到";
        } else {
            years = new String[] {year};
        }
        for (String string : years) {
            result = result + string + "年" + suffix;
        }
        if (result.endsWith("、") || result.endsWith("到")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getMonthValue(String month) {
        String result = "";
        if (month == null) {
            return result;
        }
        String[] months = null;
        String suffix = "";
        if (month.contains("|")) {
            months = month.split("[|]");
            suffix = "、";
        } else if (month.contains("-")) {
            months = month.split("-");
            suffix = "到";
        } else {
            months = new String[] {month};
        }
        for (String string : months) {
            result = result + string + "月" + suffix;
        }
        if (result.endsWith("、") || result.endsWith("到")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getDayValue(String day) {
        String result = "";
        if (day == null) {
            return result;
        } else if (day.equals("*")) {
            return "每天";
        }
        String[] days = null;
        String suffix = "";
        if (day.contains("|")) {
            days = day.split("[|]");
            suffix = "、";
        } else if (day.contains("-")) {
            days = day.split("-");
            suffix = "到";
        } else {
            days = new String[] {day};
        }
        for (String string : days) {
            result = result + string + "日" + suffix;
        }
        if (result.endsWith("、") || result.endsWith("到")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getWeekValue(String week) {
        if ("Mon".equalsIgnoreCase(week)) {
            return "周一";
        } else if ("Tue".equalsIgnoreCase(week)) {
            return "周二";
        } else if ("Wed".equalsIgnoreCase(week)) {
            return "周三";
        } else if ("Thu".equalsIgnoreCase(week)) {
            return "周四";
        } else if ("Fri".equalsIgnoreCase(week)) {
            return "周五";
        } else if ("Sat".equalsIgnoreCase(week)) {
            return "周六";
        } else if ("Sun".equalsIgnoreCase(week)) {
            return "周日";
        } else {
            return "";
        }
    }

    private String getTime(String[] formatStr) {
        //需要根据要求进行修改
        String result = formatStr[3] + ":" + formatStr[4] + ":" + formatStr[4];
        return result;
    }

    private String getTimes(String value) {
        String valideSecond = value.substring(0, value.indexOf("*"));
        String times = value.substring(value.indexOf("*") + 1 , value.length());
        //String[] values = value.split("*", 1);
        //String[] values = value.split("*");
        if (valideSecond.equals("0") || times.equals("1")) {
            return "";
        }
        //String valideSecond = values[0];
        int min = Integer.parseInt(valideSecond) / 60;
        return "起每隔" + min + "分钟1次，共" + times + "次";
    }
}
