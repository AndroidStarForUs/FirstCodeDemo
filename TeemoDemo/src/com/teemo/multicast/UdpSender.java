/*
*****************************************************************************************
* @file UdpSender.java
*
* @brief 
*
* Code History:
*       2016-3-29  下午4:49:17  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.multicast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @brief 发送Udp数据包
 * 
 * @author Teemo
 *
 * @date 2016-3-29 下午4:49:17
 */
public class UdpSender {
    //private final String MOUDLE = UdpSender.class.getSimpleName();
    //private final String TAG = UdpSender.class.getSimpleName();

    private DatagramSocket mDatagramSocket = null;
    private String mHost;
    private int mPort;
    private DatagramPacket mDatagramPacket = null;

    public UdpSender(String ip, int port) {
        try {
            this.mPort = port;
            this.mHost = ip;
            mDatagramSocket = new DatagramSocket(null);
            mDatagramSocket.setReuseAddress(true);
            mDatagramSocket.bind(new InetSocketAddress(mPort));
            mDatagramPacket = new DatagramPacket(new byte[] {},  0, InetAddress.getByName(mHost), this.mPort);
        } catch (Exception e) {
            e.printStackTrace();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }

    public UdpSender(int port) {
        try {
            this.mPort = port;
            mDatagramSocket = new DatagramSocket(null);
            mDatagramSocket.setReuseAddress(true);
            mDatagramSocket.bind(new InetSocketAddress(mPort));
        } catch (Exception e) {
            e.printStackTrace();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }

    public void send(byte[] data, InetAddress address) {
        if (mDatagramSocket == null) {
            return;
        }
        try {
            if (mDatagramPacket == null) {
                mDatagramPacket = new DatagramPacket(new byte[] {}, 0, address, this.mPort);
            }
            mDatagramPacket.setData(data);
            mDatagramPacket.setAddress(address);
            mDatagramSocket.send(mDatagramPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        if (mDatagramSocket == null || mDatagramPacket == null) {
            return;
        }
        try {
            mDatagramPacket.setData(data, 0, data.length);
            mDatagramSocket.send(mDatagramPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        if (mDatagramSocket == null || mDatagramSocket.isClosed()) {
            return true;
        }
        return false;
    }

    public void close() {
        if (mDatagramSocket != null) {
            mDatagramSocket.close();
        }
        mDatagramSocket = null;
        mDatagramPacket = null;
    }
}
