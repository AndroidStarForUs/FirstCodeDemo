/*
 *****************************************************************************************
 * @file ApConstant.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月18日  下午4:35:33  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn;

import java.util.HashMap;
import java.util.Map;

/**
 * @brief Ap模式常量定义
 * 
 * @author Peter
 *
 * @date 2015年12月18日 下午4:35:33
 */
public class ApConstant {

    public static final String AP_HOT_IP = "192.168.43.1";
//    public static final String AP_HOT_IP = "localhost";
    public static final int AP_HOT_PORT = 9099;
    
    public static Map<Integer, String> apStateMsg = new HashMap<Integer, String>();
    
    public static Map<Integer, String> apTasks = new HashMap<Integer, String>();
     /**
     * 热点信息
     */
    public static final String AP_HOT_NAME = "smallzhi-ap";
    public static final String AP_HOT_PASSWORD = "12345678";
    
    public static final String SEND_TO_BOX = "toBox";
    public static final String SEND_TO_ASSISTANT = "toAssistant";
   

    
    public static final int createApTaskStart = 700;
    public static final int createApSuccess = 701;
    public static final int createApError = 702;
    public static final int closeApSuccesss = 703;
    public static final int closeApFail = 704;
    
    public static final int receiverResultCorrect = 705;
    public static final int receiverResultError = 706;
    
    /**
     * {@link com.jushang.wifiapconnection.box.LocalServerForAp}
     */
    public static final int receivedResultTimeOut = 707;
    public static final int responseResultTimeOut = 708;
    
    public static final int conenctWifiSucces = 709;
    public static final int conenctWifiError = 710;
    public static final int conenctWifiTimeOut = 711;
    public static final int disConenctWifi = 712;
    
    
    public static final int openWifiSuccess = 713;
    public static final int openWifiFail = 714;
    public static final int closeWifiSuccess = 715;
    public static final int closeWifiFail = 716;
    
    public static final int bindingServerSocketError = 717;
    
    public static final int checkAssistantConenctWifiSuccess = 718;
    public static final int checkAssistantConenctWifiFail = 719;
    public static final int checkAssistantConenctWifiError = 720;
    
    public static final int apConfigWifiTimeout = 721;
    
    
    /**
     * 与助手端交互的状态码ErrorCode
     */
    public static final int receivedWifiInfoCorrect = 801;
    
    public static final int receivedWifiInfoError = 802;
    
    public static final int assistantChangWifiSuccess = 810;
    public static final int boxConnectWifiSuccess = 811;
    

    
    
    public static final int logAssiatantExplain = 901;
    
    static {
        apStateMsg.put(createApSuccess, "创建Ap成功");
        apStateMsg.put(createApError, "创建Ap失败");
        apStateMsg.put(closeApSuccesss, "关闭Ap成功");
        apStateMsg.put(closeApFail, "关闭Ap失败");
        apStateMsg.put(receiverResultCorrect, "接收到配置网络的数据，数据正确");
        apStateMsg.put(receiverResultError, "接收到配置，数据格式不正确");
        apStateMsg.put(receivedResultTimeOut, "接收配置网络数据超时");
        apStateMsg.put(responseResultTimeOut, "响应数据超时");
        apStateMsg.put(conenctWifiSucces,"连接网络成功");
        apStateMsg.put(conenctWifiError, "连接网络错误");
        apStateMsg.put(conenctWifiTimeOut, "连接网络超时");
        apStateMsg.put(disConenctWifi, "网络连接状态disconnected");
        apStateMsg.put(openWifiSuccess, "打开网络成功");
        apStateMsg.put(openWifiFail, "打开网络失败");
        apStateMsg.put(closeWifiSuccess, "关闭Wifi成功");
        apStateMsg.put(closeWifiFail, "关闭wifi失败");
        apStateMsg.put(bindingServerSocketError, "绑定Socket端口异常");
        apStateMsg.put(checkAssistantConenctWifiSuccess, "助手端切换网络成功");
        apStateMsg.put(checkAssistantConenctWifiFail, "检查助手端切换网络失败");
        apStateMsg.put(checkAssistantConenctWifiError, "检查助手端切换网络超时");
        apStateMsg.put(apConfigWifiTimeout, "ap热点配置网络超时");
    }
    

    public static final int ApConfigDefaultState = 0; // 默认状态，退出

    public static final int Command_CreateAp = 1001; // 创建Ap
    public static final int Command_CancleAp = 1002; // 取消App

    public static final int Command_StartServer = 1003; // 开启本地socket连接
    public static final int Command_StopServer = 1004; // 开启本地socket连接

    public static final int Command_ConnectWifi = 1005; // 连接wifi
    public static final int Command_StopConnectWifi = 1006; // 停止连接wifi
    
    public static final int Command_OpenWifi = 1007; // 打开wifi
    public static final int Command_CloseWifi = 1008; // 关闭wifi
    
    public static final int Command_AllTaskFinish = 1009; // 所有任务执行完毕
    public static final int Command_TaskExcuteError = 1010; // 所有任务执行完毕
    
    public static final int Command_UdpCheckWifiConnectState = 1011; // 使用广播检查助手端联网状态
    
    
    static {
        apTasks.put(Command_CreateAp, "创建Ap热点");
        apTasks.put(Command_CancleAp, "关闭Ap模式");
        apTasks.put(Command_StartServer, "开启接收Wifi信息的服务");
        apTasks.put(Command_StopServer, "关闭接收Wifi信息的服务");
        apTasks.put(Command_ConnectWifi, "开始连接网络");
        apTasks.put(Command_StopConnectWifi, "停止连接网络");
        apTasks.put(Command_OpenWifi, "打开wifi模块");
        apTasks.put(Command_CloseWifi, "关闭Wifi模块");
        apTasks.put(Command_AllTaskFinish, "所有任务执行完成");
        apTasks.put(Command_UdpCheckWifiConnectState, "使用广播检查助手端联网状态");
    }
    
    
    public static final int broadcastReceivePort = 20000;
    public static final int singleCastSendPort = 10001; // 助手在10001端口单播回应扫描结果
    
    


}
