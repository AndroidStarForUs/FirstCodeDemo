/*****
 * @file AsyncUdpSender.java
 * @brief 非阻塞发送udp
 * @author terry
 *
 */
package com.teemo.apconn.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import android.util.Log;

public class AsyncUdpSender {

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////Constant Definition
    // ///////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////

    public static final String LOGTAG = "AsyncUdpSender";

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////// Member Variables//
    // ///////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////

    private DatagramSocket datagramSocket;
    private String host;
    private int port;
    private DatagramPacket datagramPacket;

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////constructor method //
    // /////////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////////

    /*****
     * 该构造方法创建的对象应该调用方法send(byte[]data)来发送,否则报错
     * 
     * @param ip
     * @param port
     * @throws Exception
     */
    public AsyncUdpSender(String ip, int port) {
        try {
            this.port = port;
            host = ip;
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(port));
            datagramPacket = new DatagramPacket(new byte[] {}, 0, InetAddress.getByName(host), this.port);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                datagramSocket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            datagramSocket = null;
        }
    }

    /*****
     * 该构造方法创建的对象应该调用方法send(byte[] data,String ip)来发送,否则报错
     * 
     * @param ip
     * @param port
     * @throws Exception
     */
    public AsyncUdpSender(int port) {
        try {
            this.port = port;
            datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(port));
            Log.d(LOGTAG, "==>AsyncUdpSender::init<>::success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, "==>AsyncUdpSender::init<>::[ERROR]:"+e);
            try {
                datagramSocket.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            datagramSocket = null;
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////// Public Functions //
    // ///////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////////////

    public void send(byte[] data, InetAddress address) {
        if (datagramSocket == null) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR] resouce is null");
            return;
        }
        try {
            if (datagramPacket == null) {
                datagramPacket = new DatagramPacket(new byte[] {}, 0, address, this.port);
            }
            datagramPacket.setData(data, 0, data.length);
            datagramPacket.setAddress(address);
            datagramSocket.send(datagramPacket);
            try {
                Log.i(LOGTAG, "==>AsyncUdpSender::send():: send data:" + new String(data));
            } catch (Exception e) {
            }
        } catch (UnknownHostException e) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR] UnknownHost:host" + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR]  send exception" + e);
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        if (datagramSocket == null || datagramPacket == null) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR] resouce is null");
            return;
        }
        try {
            datagramPacket.setData(data, 0, data.length);
            datagramSocket.send(datagramPacket);
            try {
                Log.i(LOGTAG, "==>AsyncUdpSender::send():: send data:" + new String(data));
            } catch (Exception e) {
            }
        } catch (UnknownHostException e) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR] UnknownHost:host" + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOGTAG, "==>AsyncUdpSender::send()::[ERROR]  send exception" + e);
            e.printStackTrace();
        }
    }

    public boolean isClosed() {
        if (datagramSocket == null || datagramSocket.isClosed()) {
            return true;
        }
        return false;
    }

    public void close() {
        if (datagramSocket != null) {
            datagramSocket.close();
        }
        datagramSocket = null;
        datagramPacket = null;
    }
}
