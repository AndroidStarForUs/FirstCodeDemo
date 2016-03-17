package com.teemo.apconn.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * Wifi信息辅助类
 * 
 * @author hefeng
 * 
 */
public class WifiInfoUtils {
    private WifiInfoUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return 手机IP地址
     */
    public static String getLocalIpAddress() {

        try {
            Enumeration<NetworkInterface> networks;
            Enumeration<InetAddress> inets;
            NetworkInterface network;
            InetAddress inetAddress;

            for (networks = NetworkInterface.getNetworkInterfaces(); networks.hasMoreElements();) {
                network = networks.nextElement();

                for (inets = network.getInetAddresses(); inets.hasMoreElements();) {
                    inetAddress = inets.nextElement();

                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {

                        return inetAddress.getHostAddress().toString();

                    }
                }

            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param context
     * @return 手机Mac地址
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = getWifiManager(context);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * @param context
     * @return 当前手机连接wifi的ssid
     */
    public static String getWifiSsid(Context context) {
        if (!WifiUtils.isConnectWifi(context)) {
            return "";
        }
        WifiManager wifiManager = getWifiManager(context);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        if (TextUtils.isEmpty(ssid)) {
            ssid = "";
        }
        ssid = ssid.replaceAll("\"", "");
        return ssid;
    }

    /**
     * @param context
     * @return 当前手机连接wifi的ssid
     */
    public static int getWifiNetWorkId(Context context) {
        if (!WifiUtils.isConnectWifi(context)) {
            return -1;
        }
        WifiManager wifiManager = getWifiManager(context);
        WifiInfo info = wifiManager.getConnectionInfo();
        int id = info.getNetworkId();
        return id;
    }

    public static void startScan(Context context) {
        WifiManager manager = getWifiManager(context);
        manager.startScan();
    }

    /**
     * 判断网络是否加密
     * 
     * @param type
     * @return
     */
    public static boolean isEncryption(String type) {
        if (type.contains("WPA") || type.contains("WEP") || type.contains("EAP")) {
            return true;
        }
        return false;
    }

    private static WifiManager mWifiManager;

    public synchronized static WifiManager getWifiManager(Context context) {
        if (mWifiManager == null) {
            mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        }
        // mWifiManager.startScan();
        return mWifiManager;
    }

    /**
     * 当前连接的wifi是否加密
     * 
     * @param context
     * @return
     */
    public static boolean isEncryption(Context context) {
        int type = WifiSecurity.getCurrentWifiSecurity(context);
        if (type == WifiSecurity.SECURITY_NONE) {
            return false;
        }
        return true;
    }
}
