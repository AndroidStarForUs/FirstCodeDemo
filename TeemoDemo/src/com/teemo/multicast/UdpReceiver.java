/*
*****************************************************************************************
* @file UdpReceiver.java
*
* @brief 
*
* Code History:
*       2016-3-29  下午3:47:25  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.multicast;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

import com.teemo.utils.LogMgr;

/**
 * @brief 通过广播接收UDP数据包
 * 
 * @author Teemo
 *
 * @date 2016-3-29 下午3:47:25
 */
public class UdpReceiver {
    public final String MODULE = UdpReceiver.class.getSimpleName();
    public final String TAG = UdpReceiver.class.getSimpleName();

    private int port = 0;
    private MulticastLock mScanBroadcastLock = null;
    private Selector mScanReceiverSelector = null;
    private DatagramChannel mScanReceiverDatagramChannel = null;
    private DatagramSocket mScanDatagramSocket = null;
    private int buffer_size = 80920;// 接收Buffer的大小

    public UdpReceiver(Context mContext, int port) {
        this.port = port;
        WifiManager wifiMgr = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mScanBroadcastLock = wifiMgr.createMulticastLock("ScanBroadcast");
        try {
            mScanReceiverSelector = Selector.open();
            mScanReceiverDatagramChannel = DatagramChannel.open();
            mScanReceiverDatagramChannel.configureBlocking(false);
            mScanDatagramSocket = mScanReceiverDatagramChannel.socket();
            mScanDatagramSocket.bind(new InetSocketAddress(this.port));
            mScanReceiverDatagramChannel.register(mScanReceiverSelector, SelectionKey.OP_READ);
            mScanBroadcastLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
            //colse
        }
    }

    public int receiver(ArrayList<ByteBuffer> bufferList) {
        try {
            if (mScanReceiverSelector.isOpen()) {
                int n = mScanReceiverSelector.selectNow();
                if (n == 0) {
                    LogMgr.d(MODULE, TAG, "[receiver] no selector.");
                    return n;
                }
                LogMgr.i(MODULE, TAG, "[receiver] 通道的数量:" + n + ",接收时间:" + System.currentTimeMillis());
                Set<SelectionKey> readyKeys = mScanReceiverSelector.keys();
                if (n > bufferList.size()) {
                    int a = n - bufferList.size();
                    for (int i = 0; i < a; i++) {
                        bufferList.add(ByteBuffer.allocate(buffer_size));
                    }
                }
                int i = 0;
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        DatagramChannel dc = (DatagramChannel) key.channel();
                        ByteBuffer bb = bufferList.get(i);
                        //InetSocketAddress client = (InetSocketAddress) dc.receive(bb);
                        key.interestOps(SelectionKey.OP_READ);
                        int position = bb.position();
                        if (position >= 1) {
                            i ++;
                        } else {
                            dc.register(mScanReceiverSelector, SelectionKey.OP_READ);
                        }
                    }
                }
                return i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isClosed() {
        if (mScanReceiverSelector == null || !mScanReceiverSelector.isOpen() || mScanDatagramSocket == null || mScanDatagramSocket.isClosed()) {
            return true;
        }
        return false;
    }

    public void close() {
        LogMgr.i(MODULE, TAG, "[close]");
        if (mScanBroadcastLock != null && mScanBroadcastLock.isHeld()) {
            mScanBroadcastLock.release();
        }
        mScanBroadcastLock = null;
        if (mScanReceiverSelector != null) {
            try {
                mScanReceiverSelector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mScanReceiverSelector = null;
        if (mScanReceiverDatagramChannel != null && mScanReceiverDatagramChannel.isOpen()) {
            try {
                mScanReceiverDatagramChannel.close();
            } catch (Exception e) {
            }
        }
        mScanReceiverDatagramChannel = null;
        if (mScanReceiverSelector != null) {
            try {
                mScanReceiverSelector.close();
            } catch (Exception e) {
            }
        }
        mScanReceiverSelector = null;
    }
}
