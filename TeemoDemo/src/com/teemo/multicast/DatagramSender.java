/*
 *****************************************************************************************
 * @file Sender.java
 *
 * @brief 
 *
 * Code History:
 *       2016-1-4  下午2:33:36  Teemo , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.teemo.utils.LogMgr;

/**
 * @brief
 * 
 * @author Teemo
 * 
 * @date 2016-1-4 下午2:33:36
 */
public class DatagramSender implements Callback {
    private final String Moudle = DatagramSender.class.getSimpleName();
    private final String TAG = DatagramSender.class.getSimpleName();
    private final String MULTICAST_LOCK = "multicast.test";

    public static final int SEND_ERR_XOK = 0x00;
    public static final int SEND_ERR_UNKONWHOST = 0x001;
    public static final int SEND_ERR_IOEXCEPTION = 0x002;
    public static final int SEND_ERR_UNKNOW = 0x003;
    
    private int MAXTRYCOUNT = 100;

    private SendThread mSendThread = null;
    private MulticastLock mMulticastLock;
    private MulticastSocket mSendSocket;

    private String mSendInfo = null;

    private SenderCallback mSendCallback;
    private volatile boolean isLocked = false;
    private volatile boolean isRunning = false;
    private int REMOTE_PORT = 5677;
    private String REMOTE_IP = "239.255.255.250";

    public DatagramSender() {
        if (mSendThread != null) {
            return;
        }
        mSendThread = new SendThread();
    }

    private Handler mHandler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case SEND_ERR_XOK:
            mSendCallback.onSenderCallback(true);
            break;
        case SEND_ERR_UNKONWHOST:
        case SEND_ERR_IOEXCEPTION:
        case SEND_ERR_UNKNOW:
            LogMgr.e(Moudle, TAG, "[handleMessage] The error message >>> " + msg.obj);
            mSendCallback.onSenderCallback(false);
            break;
        }
        return false;
    }

    public void sendMultiCast(String info, Context context, SenderCallback callbacl) {
        LogMgr.i(Moudle, TAG, "Send multicast.");
        this.mSendCallback = callbacl;
        this.mSendInfo = info;
        if (mSendThread == null) {
            return;
        }
        if (!isLocked) {
            isLocked = allowMulticast(context);
        }
        assert(isLocked);

        if (!isRunning) {
            // try again count.
            MAXTRYCOUNT = 100;
            isRunning = true;
            mSendThread.start();
        }
    }

    // 打开组播锁
    private boolean allowMulticast(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock(MULTICAST_LOCK);
        mMulticastLock.acquire();
        return true;
    }

    public void stopSendMultiCast() {
        isRunning = false;
        if (mMulticastLock.isHeld()) {
            mMulticastLock.release();
        }
        isLocked = false;
    }

    // 组播线程
    private class SendThread extends Thread {
        @Override
        public void run() {
            LogMgr.d(Moudle, TAG, "Start run send thread.");
            try {
                InetAddress inetAddress = InetAddress.getByName(REMOTE_IP);
                LogMgr.d(Moudle, TAG, "SendMultiCastThread>>>InetAddress：" + inetAddress.getHostName());
                mSendSocket = new MulticastSocket();
                mSendSocket.setSoTimeout(20 * 1000);
                mSendSocket.setReuseAddress(true);
                byte[] buffer = mSendInfo.getBytes("utf-8");
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, inetAddress, REMOTE_PORT);
                while (isRunning && MAXTRYCOUNT > 0) {
                    mSendSocket.send(sendPacket);
                    MAXTRYCOUNT --;
                    try {
                        sleep(100);
                    } catch (Exception e) {
                        LogMgr.e(Moudle, TAG, "Sleep exception " + e.getMessage());
                    }
                }
            } catch (UnknownHostException e1) {
                LogMgr.e(Moudle, TAG, "[SendThread] <UnknownHostException> " + e1.getMessage());
                e1.printStackTrace();
                mHandler.sendMessage(mHandler.obtainMessage(SEND_ERR_UNKONWHOST, e1.getMessage()));
            } catch (IOException e) {
                LogMgr.e(Moudle, TAG, "[SendThread] <UnknownHostException> " + e.getMessage());
                e.printStackTrace();
                mHandler.sendMessage(mHandler.obtainMessage(SEND_ERR_IOEXCEPTION, e.getMessage()));
            } finally {
                isRunning = false;
                if (mSendSocket != null) {
                    mSendSocket.close();
                }
                if (mMulticastLock.isHeld()) {
                    mMulticastLock.release();// 组播锁释放
                    isLocked = false;
                }
            }
        }
    }

    public interface SenderCallback {
        public void onSenderCallback(boolean isSucc);
    }

}
