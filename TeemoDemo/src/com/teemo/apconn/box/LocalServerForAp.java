/*
 *****************************************************************************************
 * @file LocalServerForAp.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月18日  下午6:00:58  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn.box;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.teemo.apconn.ApConstant;
import com.teemo.apconn.TranSportInfo;
import com.teemo.system.SystemInfoUtils;

/**
 * @brief 用于与助手端通信
 * 
 * @author Peter
 *
 * @date 2015年12月18日 下午6:00:58
 */
class LocalServerForAp implements Runnable {

    private static final String TAG = "LocalServerForAp";

    ServerSocket serverSocket = null;

    private static final int SERVER_SOCKET_TIMEOUT = 300 * 1000;

    private static final int SEND_SOCKET_READ_TIMEOUT = 10 * 1000;

    private boolean isListening = false;

    private ICommuResult mICommuResult = null;

    public void setLocalServerStateListener(ICommuResult mICommuResult) {
        this.mICommuResult = mICommuResult;
    }

    public synchronized void setListening(boolean isListening) {
        this.isListening = isListening;
    }

    public synchronized boolean isListening() {
        return isListening;
    }

    @Override
    public void run() {
        if (!isListening()) {
            Log.d(TAG, "===>>this receiver wifiInfo listener is closed");
            return;
        }
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                Log.d(TAG, "===>>关闭上一个已经绑定的serverSocket端口");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            serverSocket = new ServerSocket(ApConstant.AP_HOT_PORT);
            serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
            serverSocket.setReuseAddress(true);
            setListening(true);
            if (serverSocket == null) {
                Log.e(TAG, "==>>this serverSocket  is null");
                return;
            }
            if (!serverSocket.isBound()) {
                Log.e(TAG, "==>>this serverSocket  is unBound");
                return;
            }
            Log.d(TAG, "===>>this receiver waiting  accept data");
            if (serverSocket.isClosed()) {
                Log.e(TAG, "==>>this serverSocket  is closed");
                return;
            }
            mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始接收网络配置数据.....");
            Socket assistantSocket = serverSocket.accept();
            Log.d(TAG, "==>>this assistantSocket connect  is coming");
            if (assistantSocket == null) {
                Log.e(TAG, "==>>this assistantSocket  is null");
            } else {
                if (isListening()) {
                    // close 的时候阻塞1秒钟发送完所有的数据
                    assistantSocket.setSoLinger(true, 100);
                    assistantSocket.setSoTimeout(SEND_SOCKET_READ_TIMEOUT);
                    String receivedResult = readReceivedData(assistantSocket);
                    // 校验接收到的数据
                    if (TextUtils.isEmpty(receivedResult)) {
                        mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始响应空数据.....");
                        writeResponseData(assistantSocket, getResponseInfo(true));
                        mICommuResult.onResult(ApConstant.receiverResultError, "接收结果错误");
                    } else {
                        mICommuResult.onResult(ApConstant.logAssiatantExplain, "接收到配置信息.....receivedResult:"
                                + receivedResult);
                        writeResponseData(assistantSocket, getResponseInfo(false));
                        mICommuResult.onResult(ApConstant.receiverResultCorrect, receivedResult);
                    }
                } else {
                    try {
                        assistantSocket.close();
                    } catch (Exception e) {
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "===>>current IOException has exception:" + e.toString());
            mICommuResult.onResult(ApConstant.receivedResultTimeOut,
                    ApConstant.apStateMsg.get(ApConstant.receivedResultTimeOut));
        } finally {
            setListening(false);
            if (serverSocket != null) {
                try {
                    Log.d(TAG, "===>>关闭 serverSocket");
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }

    }

    public void startLocalServerListener() {
        setListening(true);
        this.run();
    }

    public void stopServerListener() {
        Log.d(TAG, "===>>stopServerListener 关闭接收数据通道");
        setListening(false);
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                Socket socket = new Socket("localhost", ApConstant.AP_HOT_PORT);
                socket.close();
                mICommuResult.onResult(ApConstant.logAssiatantExplain, "关闭接收数据通道");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ResponseThread extends Thread {

        private Socket respSocket = new Socket();

        private String receivedResult = "";

        public ResponseThread(Socket respSocket, String receivedResult) {
            this.respSocket = respSocket;
            this.receivedResult = receivedResult;
        }

        public void run() {
            super.run();
            if (respSocket == null) {
                Log.e(TAG, "===>>respScoSocket is null");
                return;
            }
            if (respSocket.isClosed()) {
                Log.e(TAG, "===>>respScoSocket is closed");
                return;
            }
            try {
                // 校验接收到的数据
                if (TextUtils.isEmpty(receivedResult)) {
                    mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始响应空数据.....");
                    writeResponseData(respSocket, getResponseInfo(true));
                    mICommuResult.onResult(ApConstant.receiverResultError, "接收结果错误");
                } else {
                    mICommuResult.onResult(ApConstant.logAssiatantExplain, "接收到配置信息.....receivedResult:"
                            + receivedResult);
                    writeResponseData(respSocket, getResponseInfo(false));
                    mICommuResult.onResult(ApConstant.receiverResultCorrect, receivedResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (respSocket != null) {
                    try {
                        respSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @brief 读取收到的数据
     * @param respSocket
     *            void
     */
    private String readReceivedData(Socket respSocket) {
        String receivedResult = "";
        InputStream mInputStream = null;
        DataInputStream dis = null;
        ByteArrayOutputStream baos = null;
        byte[] buffer = new byte[1024 * 2];
        try {
            mInputStream = respSocket.getInputStream();
            dis = new DataInputStream(mInputStream);
            baos = new ByteArrayOutputStream();
            int receivedDataLen = dis.readInt();
            int count, tempLen = 0;
            while ((count = dis.read(buffer)) != -1) {
                baos.write(buffer);
                tempLen += count;
                if (tempLen >= receivedDataLen) {
                    break;
                }
            }
            baos.flush();
            receivedResult = new String(baos.toByteArray(), 0, receivedDataLen);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // try {
            // if (dis != null) {
            // dis.close();
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // try {
            // if (mInputStream != null) {
            // mInputStream.close();
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
        }
        return receivedResult;
    }

    /**
     * @brief 写入响应数据
     * @param respSocket
     *            void
     */
    private void writeResponseData(Socket respSocket, byte[] dataInfo) {
        DataOutputStream dos = null;
        OutputStream mOutputStream = null;
        try {
            mOutputStream = respSocket.getOutputStream();
            dos = new DataOutputStream(mOutputStream);
            dos.writeInt(dataInfo.length);
            dos.write(dataInfo);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mOutputStream != null) {
                try {
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * @brief
     * @param isError
     * @return byte[]
     * @throws UnsupportedEncodingException
     */
    public byte[] getResponseInfo(boolean isError) throws UnsupportedEncodingException {
        TranSportInfo info = new TranSportInfo();
        if (isError) {
            info.setBoxSn("");
            info.setStateCode(ApConstant.receivedWifiInfoError);
        } else {
            // 获取真实的SN号
        	//SystemInfoUtils.getSnNumber()
            info.setBoxSn(SystemInfoUtils.getSnNumber());
            info.setStateCode(ApConstant.receivedWifiInfoCorrect);
        }
        Log.d(TAG, "===>>getResponseInfo info:" + info);
        mICommuResult.onResult(ApConstant.logAssiatantExplain, "开始响应sn号.....info:" + info);
        Gson gson = new Gson();
        String jsonString = gson.toJson(info);
        return jsonString.getBytes("UTF-8");
    }

}
