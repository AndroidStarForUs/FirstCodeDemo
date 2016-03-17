/*
 *****************************************************************************************
 * @file ApConnection.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月18日  下午4:23:27  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn.box;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.teemo.apconn.ApConstant;
import com.teemo.apconn.util.WifiInfoUtils;
import com.teemo.apconn.util.WifiSecurity;
import com.teemo.apconn.util.WifiUtils;

/**
 * @brief {@code
 * (1) 创建Ap热点，关闭Ap热点
 * (2)
 * }
 * @author Peter
 *
 * @date 2015年12月18日 下午4:23:27
 */
class ApConnection {

    private static final String TAG = "ApConnection";

    private WifiManager mWifiManager = null;
    private WifiStateMgr mWifiStateMgr = null;

    private int apHotCreateTimeout = 60 * 1000; // ap创建超时时间

    private CreateAPProcess mCreateAPProcess = null;

    private ICommuResult mICommuResult = null;

    private WifiConfiguration mWifiConfiguration = null;

    public ApConnection(Context context) {
        mWifiStateMgr = new WifiStateMgr(context);
        mWifiManager = mWifiStateMgr.getWifiManager();
        mCreateAPProcess = new CreateAPProcess();
    }

    public void setApResultListener(ICommuResult mICommuResult) {
        this.mICommuResult = mICommuResult;
        mWifiStateMgr.setWifiStateMgrListener(mICommuResult);
    }

    public WifiStateMgr getWifiStateMgr() {
        return mWifiStateMgr;
    }

    /**
     * @brief 创建Ap热点 void
     * @category 创建Ap热点为异步过程，不直接使用返回值判断，创建热点的状态
     */
    public void createApHotAppoint() {
        mWifiStateMgr.closeWifi();
        mWifiConfiguration = WifiUtils.createWifiConfigurationForAp(ApConstant.AP_HOT_NAME, ApConstant.AP_HOT_PASSWORD,
                WifiSecurity.SECURITY_NONE);
        mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始创建Ap......");
        setWiFiAPState(mWifiConfiguration, true);
        mCreateAPProcess.startCheckAPProcess();
    }

    /**
     * @brief 关闭ap热点 void
     */
    public void closeApHotAppoint() {
        if (mWifiConfiguration == null) {
            mWifiConfiguration = WifiUtils.createWifiConfigurationForAp(ApConstant.AP_HOT_NAME,
                    ApConstant.AP_HOT_PASSWORD, WifiSecurity.SECURITY_NONE);
        }
        mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始关闭Ap......");
        setWiFiAPState(mWifiConfiguration, false);
        mICommuResult.onResult(ApConstant.closeApSuccesss, ApConstant.apStateMsg.get(ApConstant.closeApSuccesss));
    }

    /**
     * @brief 停止检查Ap创建状态 void
     */
    public void stopCheckApCreateState() {
        if (mCreateAPProcess != null) {
            mCreateAPProcess.stopCheckAPProcess();
        }
    }

    /**
     * @brief 只是关闭热点不添加回调 void
     */
    public void onlyCloseApHotAppoint() {
        if (mWifiConfiguration == null) {
            mWifiConfiguration = WifiUtils.createWifiConfigurationForAp(ApConstant.AP_HOT_NAME,
                    ApConstant.AP_HOT_PASSWORD, WifiSecurity.SECURITY_NONE);
        }
        mICommuResult.onResult(ApConstant.logAssiatantExplain, "关闭Ap热点释放资源......");
        if (isWifiApEnabled()) {
            setWiFiAPState(mWifiConfiguration, false);
            Log.d(TAG, "===>>关闭Ap成功");
        } else {
            Log.d(TAG, "===>>当前Ap已经是关闭状态");
        }

    }

    /**
     * @brief
     * @param paramWifiConfiguration
     *            wifi配置信息
     * @param paramBoolean
     *            开启Or关闭 void
     */
    private void setWiFiAPState(WifiConfiguration paramWifiConfiguration, boolean paramBoolean) {
        Log.d(TAG, "====>>wifiConfig:" + paramWifiConfiguration + "<=>paramBoolean:" + paramBoolean);
        try {
            Class localClass = this.mWifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled", arrayOfClass);
            WifiManager localWifiManager = this.mWifiManager;
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = paramWifiConfiguration;
            arrayOfObject[1] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(localWifiManager, arrayOfObject);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public boolean isWifiApEnabled() {
        boolean result = false;
        try {
            Class localClass = this.mWifiManager.getClass();
            Method method = localClass.getMethod("isWifiApEnabled");
            result = (Boolean) method.invoke(this.mWifiManager);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    /**
     * @brief 获取Ap状态
     * @return int
     */
    public int getWifiApState() {
        try {
            int i = ((Integer) this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0])
                    .invoke(this.mWifiManager, new Object[0])).intValue();
            return i;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return 4;
    }

    /**
     * @brief 获取Ap的ssid
     * @return String
     */
    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null)
                return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager, new Object[0]);
            if (localObject1 == null)
                return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null)
                return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (localField1 == null)
                return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null)
                return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null)
                return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    /**
     * @brief 定时检查Ap创建的状态
     * @author Peter
     * @date 2015年12月19日 上午11:36:34
     */

    class CreateAPProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;

        public void run() {
            while (true)
                if (!this.running)
                    return;
                else {
                    int wifiState = getWifiApState();
                    String localIp = WifiInfoUtils.getLocalIpAddress();
                    Log.d("CreateAPProcess", "ap_ip:" + wifiState + "," + localIp);
                    if (((wifiState == 3) || (wifiState == 13)) && localIp != null) {
                        Log.d("CreateAPProcess", "wifi ap creat ok  and wifiState:" + wifiState);
                        stopCheckAPProcess();
                        mICommuResult.onResult(ApConstant.createApSuccess,
                                ApConstant.apStateMsg.get(ApConstant.createApSuccess));
                        return;
                    } else if (System.currentTimeMillis() - this.startTime >= apHotCreateTimeout) {
                        Log.e("CreateAPProcess", "wifi ap creat fail wifiState:" + wifiState);
                        stopCheckAPProcess();
                        mICommuResult.onResult(ApConstant.createApError,
                                ApConstant.apStateMsg.get(ApConstant.createApError));
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (Exception localException) {
                        localException.printStackTrace();
                    }
                }
        }

        public void startCheckAPProcess() {
            running = true;
            startTime = System.currentTimeMillis();
            run();
            Log.d(TAG, "===>>CreateAPProcess start");
        }

        public void stopCheckAPProcess() {
            this.running = false;
            this.startTime = 0L;
            Log.d(TAG, "===>>CreateAPProcess stop");
        }
    }

}
