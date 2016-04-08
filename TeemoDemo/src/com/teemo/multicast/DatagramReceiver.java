/*
 *****************************************************************************************
 * @file Sniffer.java
 *
 * @brief 
 *
 * Code History:
 *       2016-01-04  下午2:35:09  Teemo , initial version
 *       2016-03-18  下午14:01:09  Teemo , Modify the structure.
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.os.Handler;
import android.os.Message;

import com.teemo.utils.LogMgr;

/**
 * @brief Broadcast to send IP information by multicast.
 * 
 * @author Teemo
 * 
 * @date 2016-1-4 下午2:35:09
 */
public class DatagramReceiver implements Handler.Callback {
    private final String MOUDLE = DatagramReceiver.class.getSimpleName();
    private final String TAG = DatagramReceiver.class.getSimpleName();

    private final int SNIFFER_RECEVIER_DATA_SUCC = 0;
    private final int SNIFFER_RECEVIER_DATA_FAIL = 1;

    public static final int SNIFFER_ERR_BAD_STATE  = 0x000;
    public static final int SNIFFER_ERR_XOK        = 0x001;

    private DatagramThread mReceivedThread = null;

    private final int RECEIVE_PORT = 5677;
    private DatagramSocket mReceiveSocket = null;
    private ReceiverCallback mCallback;
    private volatile Boolean isRunning = false;

    private Handler mHandler = new Handler(this);

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
        case SNIFFER_RECEVIER_DATA_SUCC:
            mCallback.onReceiverCallback(msg.obj.toString(), true);
            LogMgr.i(MOUDLE, TAG, "Received datagram, then stop receiving data.");
            stopReceiveMessage();
            break;
        case SNIFFER_RECEVIER_DATA_FAIL:
            mCallback.onReceiverCallback(msg.obj.toString(), false);
            break;
        }
        return false;
    }

    public DatagramReceiver() {
        LogMgr.i(MOUDLE, TAG, "Create Sniffer object.");
        if (mReceivedThread != null) {
            return;
        }
        mReceivedThread = new DatagramThread();
    }

    /**
     * @brief Start the thread to receiver message from DatagramScoket.
     *
     * @param callback The receive callback.
     */
    public int receiveMessage(ReceiverCallback callback) {
        LogMgr.e(MOUDLE, TAG, "[receiveMessage] >>> recevied data from DatagramSocket.");
        int mRes = SNIFFER_ERR_XOK;
        this.mCallback = callback;

        if (mReceivedThread == null) {
            mRes=  SNIFFER_ERR_BAD_STATE;
        }
        assert(mRes == SNIFFER_ERR_XOK);

        if (isRunning) {
            LogMgr.i(MOUDLE, TAG, "The Sniffer thread is running.");
        } else {
            LogMgr.i(MOUDLE, TAG, "Start run Sniffer thread.");
            mReceivedThread.start();
            isRunning = true;
        }
        return SNIFFER_ERR_XOK;
    }

    // 停止接收单播数据
    public int stopReceiveMessage() {
        if (mReceiveSocket == null && !isRunning) {
            return SNIFFER_ERR_XOK;
        }
        isRunning = false;
        if (mReceiveSocket == null || mReceiveSocket.isClosed()) {
            LogMgr.i(MOUDLE, TAG, "The Receiver socket is close.");
            return SNIFFER_ERR_XOK;
        }
        mReceiveSocket.disconnect();
        mReceiveSocket.close();
        mReceiveSocket = null;
        return SNIFFER_ERR_XOK;
    }

    private class DatagramThread extends Thread {
        private final int BUFFER_SIZE = 1024;

        @Override
        public void run() {
            LogMgr.e(MOUDLE, TAG, "[DatagramThread :: run] start run.");
            try {
                mReceiveSocket = new DatagramSocket(RECEIVE_PORT);
                mReceiveSocket.setSoTimeout(60 * 1000);
                byte[] mBuffer = new byte[BUFFER_SIZE];
                DatagramPacket getPacket = null;
                try {
                    getPacket = new DatagramPacket(mBuffer, mBuffer.length);
                    LogMgr.d(MOUDLE, TAG, "while receive data.");
                    mReceiveSocket.receive(getPacket);
                    String getMes = new String(mBuffer, 0, getPacket.getLength());
                    LogMgr.d(MOUDLE, TAG, "[DatagramThread :: run] Recevied data and that is [" + getMes +"].");
                    mHandler.sendMessage(mHandler.obtainMessage(SNIFFER_RECEVIER_DATA_SUCC, getMes));
                } catch (IOException e) {
                    LogMgr.e(MOUDLE, TAG, "[DatagramThread :: run] sniff failed >>> " + e.toString());
                    mHandler.sendMessage(mHandler.obtainMessage(SNIFFER_RECEVIER_DATA_FAIL, "sniff failed"));
                    e.printStackTrace();
                }
            } catch (SocketException e) {
                LogMgr.e(MOUDLE, TAG, "[DatagramThread :: run] sniff failed >>> " + e.toString());
                mReceiveSocket = null;
            } finally {
                if (mReceiveSocket != null) {
                    try {
                        mReceiveSocket.close();
                    } catch (Exception e2) {
                    }
                }
            }
        }
    }

    public interface ReceiverCallback {
        public void onReceiverCallback(String receivedValue, boolean isSucc);
    }

}
