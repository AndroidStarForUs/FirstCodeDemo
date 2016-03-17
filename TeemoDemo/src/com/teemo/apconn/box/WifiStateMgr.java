/*
 *****************************************************************************************
 * @file WifiStateMgr.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月19日  上午11:07:38  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn.box;

import java.util.Iterator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.teemo.apconn.ApConstant;
import com.teemo.apconn.util.WifiSecurity;
import com.teemo.apconn.util.WifiUtils;

/**
 * @brief
 * 
 * @author Peter
 *
 * @date 2015年12月19日 上午11:07:38
 */
class WifiStateMgr {

    private static final String TAG = "WifiStateMgr";

    private WifiManager mWifiManager = null;
    private Context mContext = null;
    private ConnectivityManager mcConnectivityManager = null;

    private volatile boolean isConnectCheck = false;
    private Object object = new Object();

    private static final long WIFI_CONENCT_TIMEOUT = 40 * 1000;

    private static final long WIFI_OPEN_TIMEOUT = 15 * 1000;
    private static final long WIFI_CLOSE_TIMEOUT = 15 * 1000;

    private Object openWifiChecklLock = new Object();
    private volatile boolean isCancleOpenWifiCheck = false;

    private Object closeWifiChecklLock = new Object();
    private volatile boolean isCancleCloseWifiCheck = false;

    private ICommuResult mICommuResult = null;

    public WifiStateMgr(Context mContext) {
        this.mContext = mContext;
        this.mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        this.mcConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void setWifiStateMgrListener(ICommuResult mICommuResult) {
        this.mICommuResult = mICommuResult;
    }

    /**
     * 
     * @brief 返回值不代表wifi的状态
     * @return boolean
     */
    public boolean OpenWifi() {
        boolean isEnable = false;
        if (!this.mWifiManager.isWifiEnabled())
            isEnable = this.mWifiManager.setWifiEnabled(true);
        else
            isEnable = true;
        checkOpenWifiResult();
        return isEnable;
    }

    /**
     * 
     * @brief 仅仅打开wifi
     * @return boolean
     */
    public void onlyOpenWifi() {
        if (!this.mWifiManager.isWifiEnabled())
            this.mWifiManager.setWifiEnabled(true);
    }

    /**
     * @brief
     * @return boolean 返回值不代表wifi的状态
     */
    public boolean closeWifi() {
        boolean isDisable = false;
        isDisable = this.mWifiManager.setWifiEnabled(false);
        checkCloseWifiResult();
        return isDisable;
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    public WifiConfiguration isExsits(String paramString) {
        if (this.mWifiManager.getConfiguredNetworks() == null) {
            return null;
        }
        Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if (!localIterator.hasNext())
                return null;
            localWifiConfiguration = (WifiConfiguration) localIterator.next();
        } while (!localWifiConfiguration.SSID.equals("\"" + paramString + "\""));
        return localWifiConfiguration;
    }

    /**
     * @brief 连接网络
     * @param SSID
     *            账号
     * @param password
     *            密码
     * @param encryptType
     *            加密方式 void
     */
    public boolean connectWifi(String SSID, String password, int encryptType) {
        Log.d(TAG, "==>>SSID:" + SSID + "<=>password:" + password + "<=>encryptType:" + encryptType);
        mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始连接网络.....SSID:" + SSID + "<=>password:" + password);
        if (encryptType == -1) {
            encryptType = WifiSecurity.getCipherTypeFromScanResult(mContext, SSID);
        }
        boolean isConenct = WifiUtils.connectWifi(mContext, SSID, password, encryptType);
        startCheckWifiConnectState();
        return isConenct;
    }

    /**
     * @brief 开始检查wifi连接状态 void
     */
    public void startCheckWifiConnectState() {
        isConnectCheck = true;
        long startTime = System.currentTimeMillis();
        while (true) {
            if (!isConnectCheck) {
                Log.d(TAG, "====>>startCheckWifiConnectState stop check");
                return;
            }
            long conenctUseTime = System.currentTimeMillis() - startTime;
            if (conenctUseTime > WIFI_CONENCT_TIMEOUT) {
                Log.e(TAG, "===>>conenct wifi timeout and conenctUseTime:" + conenctUseTime);
                if (mICommuResult != null) {
                    mICommuResult.onResult(ApConstant.conenctWifiTimeOut,
                            ApConstant.apStateMsg.get(ApConstant.conenctWifiTimeOut));
                }
                return;
            }
            NetworkInfo info = mcConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                State state = info.getState();
                Log.d(TAG, "===>>current net conenct state:" + state);
                switch (state) {
                case CONNECTING:
                    break;
                case CONNECTED:
                    if (mICommuResult != null) {
                        mICommuResult.onResult(ApConstant.conenctWifiSucces,
                                ApConstant.apStateMsg.get(ApConstant.conenctWifiSucces));
                    }
                    isConnectCheck = false;
                    break;
                case DISCONNECTED:
                    if (mICommuResult != null) {
                        mICommuResult.onResult(ApConstant.disConenctWifi,
                                ApConstant.apStateMsg.get(ApConstant.disConenctWifi));
                    }
                    break;
                case DISCONNECTING:
                    break;
                case UNKNOWN:
                    if (mICommuResult != null) {
                        mICommuResult.onResult(ApConstant.conenctWifiError,
                                ApConstant.apStateMsg.get(ApConstant.conenctWifiError));
                    }
                    return;
                default:
                    break;
                }
            } else {
                Log.e(TAG, "===>>get NetworkInfo is null");
            }
            try {
                synchronized (object) {
                    object.wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @brief 关闭状态检查 void
     */
    private void stopCheckWifiState() {
        Log.d(TAG, "====>>stopCheckWifiState");
        isConnectCheck = false;
        synchronized (object) {
            object.notifyAll();
        }
    }

    /**
     * @brief 释放资源 void
     */
    public void stopWifiStateMgr() {
        stopCheckWifiState();
        cancleCheckOpenWifiResult();
        cancleCheckCloseWifiResult();
    }

    private void checkOpenWifiResult() {
        long startTime = System.currentTimeMillis();
        long openWifiUseTime = 0;
        isCancleOpenWifiCheck = false;
        while (true) {
            openWifiUseTime = System.currentTimeMillis() - startTime;
            if (openWifiUseTime > WIFI_OPEN_TIMEOUT) {
                Log.e(TAG, "===>>wifi open timeout");
                mICommuResult.onResult(ApConstant.openWifiFail, ApConstant.apStateMsg.get(ApConstant.openWifiFail));
                return;
            }
            if (isCancleOpenWifiCheck) {
                Log.d(TAG, "===>>wifi open check enable task cancle");
                return;
            }
            int wifiState = mWifiManager.getWifiState();
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                mICommuResult.onResult(ApConstant.openWifiSuccess,
                        ApConstant.apStateMsg.get(ApConstant.openWifiSuccess));
                isCancleOpenWifiCheck = true;
                return;
            }
            try {
                synchronized (openWifiChecklLock) {
                    openWifiChecklLock.wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancleCheckOpenWifiResult() {
        isCancleOpenWifiCheck = true;
        synchronized (openWifiChecklLock) {
            openWifiChecklLock.notify();
        }

    }

    private void checkCloseWifiResult() {
        long startTime = System.currentTimeMillis();
        long closeWifiUseTime = 0;
        isCancleCloseWifiCheck = false;
        while (true) {
            closeWifiUseTime = System.currentTimeMillis() - startTime;
            if (closeWifiUseTime > WIFI_CLOSE_TIMEOUT) {
                Log.e(TAG, "===>>wifi close timeout");
                mICommuResult.onResult(ApConstant.closeWifiFail, ApConstant.apStateMsg.get(ApConstant.closeWifiFail));
                return;
            }
            if (isCancleCloseWifiCheck) {
                Log.d(TAG, "===>>checkCloseWifiResult cancle");
                return;
            }
            int wifiState = mWifiManager.getWifiState();
            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                mICommuResult.onResult(ApConstant.closeWifiSuccess,
                        ApConstant.apStateMsg.get(ApConstant.closeWifiSuccess));
                isCancleCloseWifiCheck = true;
                return;
            }
            try {
                synchronized (closeWifiChecklLock) {
                    closeWifiChecklLock.wait(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void cancleCheckCloseWifiResult() {
        isCancleCloseWifiCheck = true;
        synchronized (closeWifiChecklLock) {
            closeWifiChecklLock.notify();
        }
    }
}
