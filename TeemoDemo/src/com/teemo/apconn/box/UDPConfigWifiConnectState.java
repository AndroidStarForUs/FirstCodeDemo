/**
 * 
 */
package com.teemo.apconn.box;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.teemo.apconn.ApConstant;
import com.teemo.apconn.ConnectWifiStateInfo;
import com.teemo.apconn.util.AsyncUdpReceiver;
import com.teemo.apconn.util.AsyncUdpSender;
import com.teemo.apconn.util.WifiInfoUtils;
import com.teemo.system.SystemInfoUtils;

/**
 * @author Peter 2015-12-25下午2:36:03
 */
public class UDPConfigWifiConnectState {

    public static final String TAG = "UDPConfigWifiConnectState";

    public static final int CHECK_ASSISTANT_CHANGWIFI_TIMEOUT = 50 * 1000; // 助手在2001端口单播回应扫描结果

    private AsyncUdpSender mScanUdpSender = null; // 扫描请求发送者
    private AsyncUdpReceiver mScanUdpReceiver = null; // 扫描响应接受者

    private ArrayList<ByteBuffer> mScanRecvBufferList = null; // 接收udp的容器

    private Context mContext = null;

    private volatile boolean isStopCheck = false;

    private ICommuResult mICommuResult = null;

    private Gson mGson = new Gson();

    private static String assistantIp = "";
    private static String boxCurrentSn = "";

    public UDPConfigWifiConnectState(Context mContext) {
        this.mContext = mContext;
        mScanRecvBufferList = new ArrayList<ByteBuffer>();
        boxCurrentSn = SystemInfoUtils.getSnNumber();
    }

    /**
     * 设置结果回调监听
     * 
     * @param mICommuResult
     */
    public void setCheckStateListener(ICommuResult mICommuResult) {
        this.mICommuResult = mICommuResult;
    }

    /*****
     * 扫描资源的创建
     * 
     * @return
     */
    private boolean scanCreateResource() {
        if (null != mScanUdpReceiver && mScanUdpSender != null) {
            if (!mScanUdpReceiver.isClosed()) {
                mScanUdpReceiver.close();
            }
            if (!mScanUdpSender.isClosed()) {
                mScanUdpSender.close();
            }
        }
        try {
            mScanUdpSender = new AsyncUdpSender("255.255.255.255", ApConstant.broadcastReceivePort);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "==>scanCreateResource():[ERROR], fail to create udpsender::" + e);
            mScanUdpSender = null;
            return false;
        }
        try {
            mScanUdpReceiver = new AsyncUdpReceiver(mContext, ApConstant.singleCastSendPort);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "==>scanCreateResource():[ERROR], fail to create udpreceiver::" + e);
            mScanUdpReceiver = null;
            return false;
        }
        return true;
    }

    /**
     * 检查盒子的联网状态
     */
    public void checkAssistantChangeWifiSate() {
        scanCreateResource();
        isStopCheck = false;
        mScanRecvBufferList.clear();
        long startTime = System.currentTimeMillis();
        SendBoxConnectWifiStateThread sendInfoThread = new SendBoxConnectWifiStateThread();
        sendInfoThread.start();
        while (true) {

            if (mScanUdpReceiver.isClosed()) {
                Log.e(TAG, "==>SendBoxConnectWifiStateThread  receiver is closed");
                return;
            }

            if (isStopCheck) {
                Log.e(TAG, "==> checkAssistantChangeWifiSate is stoped");
                if (mScanUdpReceiver != null) {
                    mScanUdpReceiver.close();
                }
                return;
            }
            long checkUseTime = System.currentTimeMillis() - startTime;
            if (checkUseTime >= CHECK_ASSISTANT_CHANGWIFI_TIMEOUT) {
                Log.e(TAG, "==> checkAssistantChangeWifiSate is timeout");
                mICommuResult.onResult(ApConstant.checkAssistantConenctWifiFail, ApConstant.apStateMsg.get(ApConstant.checkAssistantConenctWifiFail));
                isStopCheck = true;
                return;
            }
            // 接收助手端响应的sn号
            int size = mScanUdpReceiver.receiver(mScanRecvBufferList);
            Log.d(TAG, "==>checkAssistantChangeWifiSate receive packet size:" + size);
            inner1: for (int i = 0; i < size; i++) {
                if (isStopCheck) {
                    break inner1;
                }
                ByteBuffer buffer = mScanRecvBufferList.get(i);
                int position = buffer.position();
                byte[] data = buffer.array();
                try {
                    String json = new String(data, 0, position);
                    Log.d(TAG, "==>checkAssistantChangeWifiSate receive json Data:" + json);
                    ConnectWifiStateInfo connectWifiStateInfo = mGson.fromJson(json, ConnectWifiStateInfo.class);
                    if (boxCurrentSn.equals(connectWifiStateInfo.getBoxSn())) {
                        // 获取助手端Ip地址
                        assistantIp = connectWifiStateInfo.getAssistantIp();
                        // 接收到助手端响应的数据，相互认证联网成功
                        Log.d(TAG, "received box sn data:" + connectWifiStateInfo.getBoxSn() + " this assistant changWifi  success");
                        isStopCheck = true;
                        mICommuResult.onResult(ApConstant.checkAssistantConenctWifiSuccess, ApConstant.apStateMsg.get(ApConstant.checkAssistantConenctWifiSuccess));
                    }
                } catch (Exception e) {
                    mICommuResult.onResult(ApConstant.checkAssistantConenctWifiError, ApConstant.apStateMsg.get(ApConstant.checkAssistantConenctWifiError));
                    isStopCheck = true;
                    e.printStackTrace();
                }
            }
            for (ByteBuffer byteBuffer : mScanRecvBufferList) {
                byteBuffer.clear();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * send BoxConenctWifiState
     */

    public class SendBoxConnectWifiStateThread extends Thread {

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (true) {
                if (mScanUdpSender.isClosed()) {
                    Log.e(TAG, "==>SendBoxConnectWifiStateThread  sender is closed");
                    return;
                }
                if (isStopCheck) {
                    Log.e(TAG, "==>SendBoxConnectWifiStateThread  sender is stop");
                    return;
                }
                long sendInfoUsedTime = System.currentTimeMillis() - startTime;
                if (sendInfoUsedTime >= CHECK_ASSISTANT_CHANGWIFI_TIMEOUT) {
                    Log.e(TAG, "==>>SendBoxConnectWifiStateThread send data timeout and useTime:" + sendInfoUsedTime);
                    if (mScanUdpSender != null) {
                        mScanUdpSender.close();
                    }
                } else {
                    ConnectWifiStateInfo connectInfo = new ConnectWifiStateInfo();
                    connectInfo.setBoxSn(boxCurrentSn);
                    connectInfo.setBoxIp(getBoxIp());
                    connectInfo.setSendDirection(ApConstant.SEND_TO_ASSISTANT);
                    connectInfo.setErrorCode(ApConstant.boxConnectWifiSuccess);
                    String sendInfo = mGson.toJson(connectInfo);
                    mScanUdpSender.send(sendInfo.getBytes());
                    mScanUdpSender.send(sendInfo.getBytes());
                    Log.d(TAG, "==>SendBoxConnectWifiStateThread send data:" + sendInfo + "and threadID:" + Thread.currentThread().getId());
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public void stopCheckAssisChangeWifi() {
        isStopCheck = true;
        if (null != mScanUdpReceiver && mScanUdpSender != null) {
            if (!mScanUdpReceiver.isClosed()) {
                mScanUdpReceiver.close();
            }
            if (!mScanUdpSender.isClosed()) {
                mScanUdpSender.close();
            }
        }

    }

    private String getBoxIp() {
        return WifiInfoUtils.getLocalIpAddress();
    }

}
