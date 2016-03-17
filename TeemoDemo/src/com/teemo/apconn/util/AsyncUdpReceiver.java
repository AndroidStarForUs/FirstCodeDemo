/*****
 * @file AsyncUdpReceiver.java
 * @brief 非阻塞接收udp
 * @author terry
 *
 */
package com.teemo.apconn.util;

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
import android.util.Log;

public class AsyncUdpReceiver {

    // /////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////Constant Definition
    // /////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////////////////////
    public static final String LOGTAG = "AsyncUdpReceiver";

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////// Member Variables//
    // ///////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////

    private int port = 0;// 接收绑定的端口
    private MulticastLock mScanBroadcastLock = null; // 广播锁
    private Selector scanReceiveselector = null;//
    private DatagramChannel scanReceiverDatagramChannel = null;
    private DatagramSocket scanReceiverSocket = null;
    private int buffSize = 80920;// 接收buffer大小

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////constructor method //
    // /////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////

    public AsyncUdpReceiver(Context context, int port) {
        this.port = port;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mScanBroadcastLock = wifiMgr.createMulticastLock("ScanBroadcast");
        try {
            scanReceiveselector = Selector.open();
            scanReceiverDatagramChannel = DatagramChannel.open();
            scanReceiverDatagramChannel.configureBlocking(false);
            scanReceiverSocket = scanReceiverDatagramChannel.socket();
            scanReceiverSocket.bind(new InetSocketAddress(this.port));
            scanReceiverDatagramChannel.register(scanReceiveselector, SelectionKey.OP_READ);
            mScanBroadcastLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////// Public Functions //
    // ////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////////////////////////
    /******
     * 
     * @param list
     * @return 返回通道的数量
     * @throws Exception
     */
    public int receiver(ArrayList<ByteBuffer> bufferList) {
        try {
            if (scanReceiveselector.isOpen()) {
                int n = scanReceiveselector.selectNow();
                if (n == 0) {
                    Log.i(LOGTAG, "==>AsyncUdpReceiver::receiver()::no selector");
                    return n;
                }
                Log.i(LOGTAG, "==>AsyncUdpReceiver::receiver()::通道的数量:" + n + ",接收时间:" + System.currentTimeMillis());
                Set<SelectionKey> readyKeys = scanReceiveselector.selectedKeys();
                if (n > bufferList.size()) {
                    int a = n - bufferList.size();
                    for (int i = 0; i < a; i++) {
                        bufferList.add(ByteBuffer.allocate(buffSize));
                    }
                }
                int i = 0;
                Iterator<SelectionKey> iterator = readyKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        DatagramChannel dc = (DatagramChannel) key.channel();
                        ByteBuffer receiveBuffer = bufferList.get(i);
                        InetSocketAddress client = (InetSocketAddress) dc.receive(receiveBuffer); // 接收来自任意一个Client的数据报
                        key.interestOps(SelectionKey.OP_READ);
                        int position = receiveBuffer.position();
                        if (position >= 1) {
                            i++;
                        } else {
                            dc.register(scanReceiveselector, SelectionKey.OP_READ);
                        }

                    }// if
                }
                return i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isClosed() {
        if (scanReceiveselector == null || !scanReceiveselector.isOpen() || scanReceiverSocket == null || scanReceiverSocket.isClosed()) {
            return true;
        }
        return false;
    }

    public void close() {
        Log.i(LOGTAG, "==>AsyncUdpReceiver::close():Enter:");
        if (mScanBroadcastLock != null && mScanBroadcastLock.isHeld()) {
            mScanBroadcastLock.release();
        }
        mScanBroadcastLock = null;
        if (scanReceiveselector != null)
            try {
                scanReceiveselector.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        scanReceiveselector = null;
        if (scanReceiverDatagramChannel != null && scanReceiverDatagramChannel.isOpen())
            try {
                scanReceiverDatagramChannel.close();
            } catch (Exception e1) {
            }
        scanReceiverDatagramChannel = null;
        if (scanReceiverSocket != null)
            try {
                scanReceiverSocket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        scanReceiverSocket = null;
    }
}
