package com.teemo.apconn.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 网络相关类
 * 
 * @author terry
 * 
 */
public class WifiUtils {

    private WifiUtils() {

    }

    /**
     * 搜索附近有没有**网络
     * 
     * @param context
     * @param ssid
     * @return
     */
    public static boolean searchWifi(Context context, String ssid) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        // wifiManager.startScan();

        List<ScanResult> scanResultList = wifiManager.getScanResults();

        if (scanResultList == null) {
            return false;
        }

        for (ScanResult result : scanResultList) {

            if (!TextUtils.isEmpty(result.SSID) && ssid.equals(result.SSID.replaceAll("\"", ""))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否连接到**网络
     * 
     * @param context
     * @param ssid
     * @return
     */
    public static boolean isConnectWifi(Context context, String ssid) {
        if (!isConnectWifi(context)) {
            return false;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null)
            return false;
        String curSsid = wifiInfo.getSSID();
        if (!TextUtils.isEmpty(curSsid) && curSsid.replaceAll("\"", "").equals(ssid)) {
            return true;
        }
        return false;
    }

    /**
     * 连接到**Wifi网络
     * 
     * @param context
     * @param ssid
     *            网络名称
     * @param pwd
     *            网络密码
     * @return 连接是否成功
     */
    public static boolean connectWifi(Context context, String ssid, String pwd, int type) {
        if (TextUtils.isEmpty(pwd)) {
            return connectToWifi(context, ssid);
        }
        return connectToWifi(context, ssid, pwd, type);
    }

    /**
     * 连接到没有密码的**网络
     * 
     * @param context
     * @return
     */
    private static boolean connectToWifi(Context context, String ssid) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configList = mWifiManager.getConfiguredNetworks();
        ArrayList<Integer> netId = new ArrayList<Integer>();
        if (configList == null) {
            return false;
        }
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i).SSID == null) {
                continue;
            }

            if (configList.get(i).SSID.equals("\"" + ssid + "\"")) {
                System.out.println(configList.get(i).networkId);
                netId.add(configList.get(i).networkId);
            }
        }

        for (int i = 0; i < netId.size(); i++) {

            mWifiManager.removeNetwork(netId.get(i));

        }

        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int wcgID = mWifiManager.addNetwork(config);

        boolean isOpen = mWifiManager.enableNetwork(wcgID, true);

        if (wcgID == -1 || !isOpen) {

            return false;

        }

        return true;
    }

    public static boolean connectToConfigredWifi(Context context, String ssid, int wifiNetworkId) {
        try {
            if (wifiNetworkId == -1 || TextUtils.isEmpty(ssid))
                return false;
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            List<WifiConfiguration> configList = wifiManager.getConfiguredNetworks();

            for (int i = 0; i < configList.size(); i++) {
                WifiConfiguration config = configList.get(i);
                String configSSID = config.SSID.replaceAll("\"", "");
                if (configSSID.equals(ssid) && wifiNetworkId == config.networkId) {
                    boolean success = wifiManager.enableNetwork(config.networkId, true);
                    if (success) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 连接到**Wifi网络
     * 
     * @param context
     * @param ssid
     *            网络名称
     * @param pwd
     *            网络密码
     * @return 连接是否成功
     */
    private static boolean connectToWifi(Context context, String ssid, String pwd, int type) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configList = wifiManager.getConfiguredNetworks();
        if (configList == null) {
            Log.d("WifiUtils", "===>>get wifi configList is null");
            return false;
        } else {
            Log.d("WifiUtils", "===>>get wifi configList:" + configList.size());
        }
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i).SSID == null) {
                continue;
            }
            if (configList.get(i).SSID.replaceAll("\"", "").equals(ssid)) {
                wifiManager.removeNetwork(configList.get(i).networkId);
            }
        }
        WifiConfiguration configuration = createWifiConfiguration(ssid, pwd, type);
        if (configuration == null) {
            return false;
        }
        int id = wifiManager.addNetwork(configuration);
        if (id == -1)
            return false;
        wifiManager.saveConfiguration();
        boolean success = wifiManager.enableNetwork(id, true);
        if (!success) {
            return false;
        }
        return true;
    }

    /**
     * 判断wifi是可用
     * 
     * @return
     */
    public static boolean pingWifi() {

        try {
            // 其中 -c 1为发送的次数，1为表示发送1次，-w 表示发送后等待响应的时间。
            Process process = Runtime.getRuntime().exec("ping -c 1 -i 0.2 -W 1 www.baidu.com");
            int status = process.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 判断是否打开wifi网络
     * 
     * @param context
     * @return
     */
    public static boolean isOpenWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 扫描网络
     * 
     * @param context
     */
    public static void startScan(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
    }

    // public static boolean checkNetworkConnection(Context context) {
    // final ConnectivityManager connMgr = (ConnectivityManager)
    // context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //
    // final android.net.NetworkInfo wifi =
    // connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    // final android.net.NetworkInfo mobile =
    // connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    // //04-08 13:55:04.735: E/hefeng(14627):
    // [WifiUtils.java|checkNetworkConnection]============true||false||false
    // if (wifi.isAvailable() || mobile.isAvailable()) {
    // return true;
    //
    // } else {
    // return false;
    // }
    // }

    /**
     * 判断是否打开网络（wifi或移动网络）
     * 
     * @param context
     * @return true表示打开
     */
    public static boolean isOpenNet(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            return conManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    public static boolean isConnectWifi(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // final android.net.NetworkInfo mobile =
        // connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 自动打开wifi
     * 
     * @param context
     */
    public static void autoOpenWiFi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(true);

        } else if (wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(false);
        }
    }

    public static WifiConfiguration createWifiConfigurationForAp(String ssid, String password, int type) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = ssid;
        configuration.allowedAuthAlgorithms.set(0);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        configuration.wepTxKeyIndex = 0;
        if (type == WifiSecurity.SECURITY_NONE) {
            configuration.hiddenSSID = false;
            configuration.status = WifiConfiguration.Status.ENABLED;
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == WifiSecurity.SECURITY_WEP) {
            configuration.wepKeys[0] = "";
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            configuration.wepTxKeyIndex = 0;
        } else if (type == WifiSecurity.SECURITY_PSK) {
            configuration.hiddenSSID = true;
            // configuration.wepKeys[0] = password;
            configuration.preSharedKey = password;
            // configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        } else if (type == WifiSecurity.SECURITY_EAP) {
            configuration.preSharedKey = password;
            configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        }
        return configuration;

    }

    public static WifiConfiguration createWifiConfiguration(String ssid, String password, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        if (type == WifiSecurity.SECURITY_NONE) {
            config.hiddenSSID = false;
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        } else if (type == WifiSecurity.SECURITY_PSK) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.priority = 1;

        } else if (type == WifiSecurity.SECURITY_WEP) {
            config.wepKeys[0] = "\"" + password + "\"";
            // config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;

        } else if (type == WifiSecurity.SECURITY_EAP) {
            config.hiddenSSID = false;
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.preSharedKey = "\"" + password + "\"";

        } else {
            return null;
        }
        return config;
    }

    public static void removeWifiConfiguration(Context context, String ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configList = wifiManager.getConfiguredNetworks();
        if (configList == null) {
            Log.d("WifiUtils", "===>>get wifi configList is null");
            return;
        } else {
            Log.d("WifiUtils", "===>>get wifi configList:" + configList.size());
        }
        for (int i = 0; i < configList.size(); i++) {
            if (configList.get(i).SSID == null) {
                continue;
            }
            if (configList.get(i).SSID.replaceAll("\"", "").equals(ssid)) {
                wifiManager.removeNetwork(configList.get(i).networkId);
            }
        }
    }
}
