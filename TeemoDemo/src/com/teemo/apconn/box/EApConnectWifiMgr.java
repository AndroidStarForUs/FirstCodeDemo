/*
 * ****************************************************************************************
 * 
 * @file EApConnectWifiMgr.java
 * 
 * @brief
 * 
 * Code History: 2015年12月18日 下午6:11:59 Peter , initial version
 * 
 * Code Review:
 * 
 * ******************************************************************************
 * *************
 */

package com.teemo.apconn.box;

import android.util.Log;

import com.google.gson.Gson;
import com.teemo.apconn.ApConstant;
import com.teemo.apconn.TranSportInfo;
import com.teemo.demo.ApplicationContext;

/**
 * @brief
 * @author Peter
 * @date 2015年12月18日 下午6:11:59
 */
public class EApConnectWifiMgr implements ICommuResult {

    private static final String TAG = "EApConnectWifiMgr";

    private static EApConnectWifiMgr mEApConnectWifiMgr = null;

    private IApStateListener mIApStateListener = null;

    private ApConnection mApConnection = null; // Ap模式打开关闭管理
    private LocalServerForAp mLocalServerForAp = null; // 接收助手传递配置信息
    private WifiStateMgr mWifiStateMgr = null; // wifi状态管理

    private UDPConfigWifiConnectState mUdpConfigWifiConnectState = null;

    private TranSportInfo tranSportInfo = null;

    private volatile int taskType = ApConstant.ApConfigDefaultState;

    private static int CYCLE_TIME = 500;

    private volatile boolean isStopApConfig = false;

    private InnerTaskThread mTaskThread = null;

    public static synchronized EApConnectWifiMgr getInstance() {
        if (mEApConnectWifiMgr == null) {
            mEApConnectWifiMgr = new EApConnectWifiMgr();
        }
        return mEApConnectWifiMgr;
    }

    private EApConnectWifiMgr() {
        mApConnection = new ApConnection(ApplicationContext.GetMainContext());
        mApConnection.setApResultListener(this);
        mWifiStateMgr = mApConnection.getWifiStateMgr();
        mLocalServerForAp = new LocalServerForAp();
        mLocalServerForAp.setLocalServerStateListener(this);
        mUdpConfigWifiConnectState = new UDPConfigWifiConnectState(ApplicationContext.GetMainContext());
        mUdpConfigWifiConnectState.setCheckStateListener(this);
    }

    public void setApConfigWifiListner(IApStateListener mIApStateListener) {
        this.mIApStateListener = mIApStateListener;
    }

    public void addApConfigWifiTaskForTest(int taskType) {
        Log.d(TAG, "====>>addApConfigWifiTask taskType:" + taskType + "<=>taskName:" + ApConstant.apTasks.get(taskType));
        this.taskType = taskType;
        mTaskThread = new InnerTaskThread();
        mTaskThread.start();
    }

    private synchronized void addApConfigWifiTask(int taskType) {
        Log.d(TAG, "====>>addApConfigWifiTask taskType:" + taskType + "<=>taskName:" + ApConstant.apTasks.get(taskType));
        this.taskType = taskType;
    }

    @Override
    public void onResult(int state, String info) {
        Log.d(TAG, "===>>onResult state:" + state + "<=>info:" + info);
        // 回调结果显示日志
        mIApStateListener.apConfigWifiResult(state, info);
        if (ApConstant.createApSuccess == state) { // 创建Ap热点成功
            // TODO 给出语音提示，或者打印日志
            addApConfigWifiTask(ApConstant.Command_StartServer);
        } else if (ApConstant.closeApSuccesss == state) { // 关闭Ap成功
            // TODO 打开Wifi
            addApConfigWifiTask(ApConstant.Command_OpenWifi);
        } else if (ApConstant.receiverResultCorrect == state) { // 接收到助手端传递的配置信息正确
            tranSportInfo = parseReceiverInfo(info);
            // 停止接收数据
            mLocalServerForAp.stopServerListener();
            addApConfigWifiTask(ApConstant.Command_CancleAp);
        } else if (ApConstant.openWifiSuccess == state) { // 开启wifi成功
            // 配置网络
            addApConfigWifiTask(ApConstant.Command_ConnectWifi);
        } else if (ApConstant.closeWifiSuccess == state) { // 关闭Wifi成功
            Log.d(TAG, "===>>close wifi success");
        } else if (ApConstant.conenctWifiSucces == state) {// 网络连接成功
            // 广播检查助手端联网状态
            addApConfigWifiTask(ApConstant.Command_UdpCheckWifiConnectState);
        } else if (ApConstant.disConenctWifi == state) {// 只是回调网络状态
            // 只是显示网络状态
            Log.d(TAG, "===>>current wifi state disConnect");
        } else if (ApConstant.checkAssistantConenctWifiSuccess == state) { // 检查助手端联网成功
            // 释放所有的资源
            addApConfigWifiTask(ApConstant.Command_AllTaskFinish);
        } else if (ApConstant.checkAssistantConenctWifiFail == state
                || ApConstant.checkAssistantConenctWifiError == state) {// 检查助手端联网失败，或者错误
            // 释放所有的资源
            addApConfigWifiTask(ApConstant.Command_AllTaskFinish);
        } else {
            // 退出程序
            addApConfigWifiTask(ApConstant.Command_TaskExcuteError);
        }
    }

    private class InnerTaskThread extends Thread {

        private boolean isFinished = false;

        public boolean isFinished() {
            return isFinished;
        }

        private void setFinished(boolean isFinished) {
            this.isFinished = isFinished;
        }

        @Override
        public void run() {
            // 开始执行任务
            while (!isStopApConfig) {
                Log.d(TAG,
                        "InnerTaskThread execute  taskType:" + taskType + "<=>taskName:"
                                + ApConstant.apTasks.get(taskType) + "<=>ThreadID:" + this.getId());
                switch (taskType) {
                case ApConstant.Command_CreateAp:
                    mApConnection.createApHotAppoint();
                    break;
                case ApConstant.Command_CancleAp:
                    mApConnection.closeApHotAppoint();
                    break;
                case ApConstant.Command_StartServer:
                    mLocalServerForAp.startLocalServerListener();
                    break;
                case ApConstant.Command_StopServer:
                    mLocalServerForAp.stopServerListener();
                case ApConstant.Command_ConnectWifi:
                    if (tranSportInfo == null) {
                        Log.e(TAG, "===>>current tranSportInfo is null");
                    } else {
                        String SSID = tranSportInfo.getSSID();
                        String password = tranSportInfo.getPassword();
                        int encryptType = tranSportInfo.getEncryptType();
                        boolean isOk = mWifiStateMgr.connectWifi(SSID, password, encryptType);
                        Log.d(TAG, "===>>start conenct wifi isOk:" + isOk);
                    }
                    break;
                case ApConstant.Command_OpenWifi:
                    boolean isOpenSuccess = mWifiStateMgr.OpenWifi();
                    Log.d(TAG, "===>>open wifi isOpenSuccess:" + isOpenSuccess);
                    break;
                case ApConstant.Command_CloseWifi:
                    boolean isSuccess = mWifiStateMgr.closeWifi();
                    Log.d(TAG, "===>>close wifi isSuccess:" + isSuccess);
                    break;
                case ApConstant.Command_UdpCheckWifiConnectState: // 检查助手端的联网状态爱
                    mUdpConfigWifiConnectState.checkAssistantChangeWifiSate();
                    break;
                case ApConstant.Command_AllTaskFinish:
                    // 所有的任务执行完成
                    releaseApConfigRes();
                    Log.d(TAG, "===>>所有的任务都执行完成,退出任务执行");
                    return;
                case ApConstant.Command_TaskExcuteError:
                    // 所有的任务执行完成
                    releaseResourceAtException();
                    Log.d(TAG, "===>>任务执行出现错误,退出任务执行");
                    return;
                case ApConstant.ApConfigDefaultState:
                    Log.d(TAG, "===>>execute task is waiting");
                    try {
                        Thread.sleep(CYCLE_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            setFinished(true);
        }

    }

    /**
     * @brief 释放Ap配置过程中创建的资源 void
     */

    private void releaseApConfigRes() {
        Log.d(TAG, "===>>释放Ap配置过程中的资源引用");
        /**
         * 停止接收数据
         */
        if (mApConnection != null) {
            mApConnection.stopCheckApCreateState();
        }
        /**
         * 停止接收数据
         */
        if (mLocalServerForAp != null) {
            mLocalServerForAp.stopServerListener();
        }
        /**
         * 关闭网络状态检查
         */
        if (mWifiStateMgr != null) {
            mWifiStateMgr.stopWifiStateMgr();
        }
        mTaskThread = null;
    }

    /**
     * @brief 异常状态下释放资源 void
     */
    public void releaseResourceAtException() {
        /**
         * 关闭Ap模式
         */
        if (mApConnection != null) {
            mApConnection.onlyCloseApHotAppoint();
        }
        /**
         * 打开Wifi
         */
        if (mWifiStateMgr != null) {
            mWifiStateMgr.onlyOpenWifi();
        }
        releaseApConfigRes();
    }

    /**
     * @brief 关闭Ap配置 void
     */
    public boolean stopApConfigWifi() {
        if (mTaskThread == null) {
            return true;
        } else {
            isStopApConfig = true;
            if (mApConnection != null) {
                mApConnection.stopCheckApCreateState();
            }
            if (mLocalServerForAp != null) {
                mLocalServerForAp.stopServerListener();
            }
            if (mWifiStateMgr != null) {
                mWifiStateMgr.stopWifiStateMgr();
            }
            for (int i = 0; i < 3; i++) {
                boolean isFinish = mTaskThread.isFinished;
                if (isFinish) {
                    mTaskThread = null;
                    return true;
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mTaskThread = null;
            return false;
        }
    }

    /**
     * @brief 开启Ap配置 void
     */
    public void startApconfigWifi() {
        isStopApConfig = false;
        addApConfigWifiTask(ApConstant.Command_CreateAp);
        mTaskThread = new InnerTaskThread();
        mTaskThread.start();
        mIApStateListener.apConfigWifiResult(ApConstant.createApTaskStart, "开始进入Ap热点配置网络");
    }

    /**
     * @brief 检查解析及接收到的数据
     * @return TranSportInfo
     */
    private TranSportInfo parseReceiverInfo(String jsonString) {
        TranSportInfo tranSportInfo = new TranSportInfo();
        Gson gson = new Gson();
        tranSportInfo = gson.fromJson(jsonString, TranSportInfo.class);
        return tranSportInfo;
    }

}
