package com.teemo.apconn.util;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

public class WifiSecurity {
    /**
     * These values are matched in string arrays -- changes must be kept in sync
     */
    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

    public static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    /**
     * 获取当前网络加密方式
     * 
     * @param context
     * @return
     */
    public static int getCurrentWifiSecurity(Context context) {

        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = mWifiManager.getConnectionInfo();

        // 得到配置好的网络连接
        List<WifiConfiguration> wifiConfigList = mWifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigList.size(); i++) {
            // 12-08 16:24:52.237: E/info(11005): Tenda============"Tenda"
            String ssid = wifiConfigList.get(i).SSID;
            ssid = ssid.replaceAll("\"", "");
            Log.e("WifiSecurity", info.getSSID() + "============" + ssid);
            if (info.getSSID().replaceAll("\"", "").equals(ssid) && info.getNetworkId() == wifiConfigList.get(i).networkId) {
                Log.e("WifiSecurity", info.getSSID() + " come ....");
                return getSecurity(wifiConfigList.get(i));
            }
        }
        return SECURITY_PSK;
    }

    /**
     * 获取连接成功过的网络加密方式
     * 
     * @param context
     * @return
     */
    public static int getWifiSecurityFromConfigured(Context context, String ssId, int networkId) {

        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // 得到配置好的网络连接
        List<WifiConfiguration> wifiConfigList = mWifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigList.size(); i++) {
            // 12-08 16:24:52.237: E/info(11005): Tenda============"Tenda"
            String ssid = wifiConfigList.get(i).SSID;
            ssid = ssid.replaceAll("\"", "");
            Log.e("WifiSecurity", ssId + "============" + ssid);
            if (ssId.equals(ssid) && networkId == wifiConfigList.get(i).networkId) {
                Log.e("WifiSecurity", ssid + " come ....");
                return getSecurity(wifiConfigList.get(i));
            }
        }

        return SECURITY_PSK;
    }

    /**
     * 获取扫描结果中wifi加密类型
     * 
     * @param ssid
     * @return
     */
    public static int getCipherTypeFromScanResult(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int type = SECURITY_PSK;

        List<ScanResult> list = wifiManager.getScanResults();

        for (ScanResult scResult : list) {

            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
                String capabilities = scResult.capabilities;
                Log.i("WifiSecurity", "---->BSSID=" + scResult.BSSID + "---->SSID=" + scResult.SSID + "---->capabilities=" + capabilities + "---->frequency=" + scResult.frequency + "---->level="
                        + scResult.level);

                if (!TextUtils.isEmpty(capabilities)) {

                    if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                        type = SECURITY_PSK;

                    } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                        type = SECURITY_WEP;

                    } else if (capabilities.contains("EAP") || capabilities.contains("eap")) {
                        type = SECURITY_EAP;

                    } else {
                        type = SECURITY_NONE;
                    }
                }
            }
        }
        return type;
    }
}
