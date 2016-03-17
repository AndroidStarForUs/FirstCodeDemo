/*
*****************************************************************************************
* @file ApReceverDemo.java
*
* @brief 
*
* Code History:
*       2016-3-9  下午5:47:18  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.apconn;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.teemo.apconn.box.EApConnectWifiMgr;
import com.teemo.apconn.box.IApStateListener;
import com.teemo.demo.R;
import com.teemo.json.BaseJsonEntity;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-3-9 下午5:47:18
 */
public class ApReceverDemo extends Activity implements OnClickListener, IApStateListener {
    private final String TAG = ApReceverDemo.class.getSimpleName();
    private EApConnectWifiMgr mEApConnectWifiMgr;

    private TranSportInfo mTranSportInfo = new TranSportInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_recever_demo);
        mEApConnectWifiMgr = EApConnectWifiMgr.getInstance();
        mEApConnectWifiMgr.setApConfigWifiListner(this);

        this.findViewById(R.id.ap_recever_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.ap_recever_btn:
            startApConfigWifi();
            break;
        }
    }

    @Override
    public void apConfigWifiResult(int state, String resultInfo) {
        Log.d(TAG, "===>>state:" + state + "<=>resultInfo:" + resultInfo);
        if (state == ApConstant.checkAssistantConenctWifiSuccess) {
            if (mTranSportInfo == null) {
                return;
            }
            // 发送登陆XMPP消息
            //sendLoginXmppMsg();
            //bindingBoxSendBoxInfo(mTranSportInfo.getUserId(), mTranSportInfo.getPhoneNumber(), mTranSportInfo.getPhoneNumber());
            // 开始定位
            //mBaiduLocation.startLocation();
            // 家电与服务器同步数据
            //updataAppLastData();
            //sendEmptyMessage(SEND_MUTICAST_START);
        } else if (state == ApConstant.conenctWifiSucces) {
            Log.d(TAG, "====>>apConfigWifiResult 配置网络成功,配置信息:" + mTranSportInfo);
        } else if (state == ApConstant.conenctWifiError) {
            //ttsPlayer.ePlay(this.getClass().getSimpleName(), ETTSConstant.WifiErrorAndCheck);
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.connectNetEnd);
        } else if (state == ApConstant.conenctWifiTimeOut) {
            //ttsPlayer.ePlay(this.getClass().getSimpleName(), ETTSConstant.WifiErrorAndCheck);
            Log.e(TAG, "connect wifi time out !");
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.connectNetEnd);
        } else if (state == ApConstant.receiverResultCorrect) {
            BaseJsonEntity<TranSportInfo> jsonEntity = new BaseJsonEntity<TranSportInfo>(TranSportInfo.class);
            mTranSportInfo = jsonEntity.parseJsonToEntity(resultInfo);
            // 关闭黄灯光圈效果
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.configNetOK);
            // 开始配置网络
            // ELightCtrl.mainLightCtrl(TAG, PlayAction.connectingNet);
        } else if (state == ApConstant.receiverResultError || state == ApConstant.receivedResultTimeOut) {
            // 关闭黄灯光圈效果
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.configNetOK);
            //ttsPlayer.ePlay(this.getClass().getSimpleName(), ETTSConstant.WifiErrorAndReConfig);
        } else if (state == ApConstant.createApTaskStart) { // 进入Ap配置网络模式打开，配置灯光效果
            // 网络配置过程中，显示黄色灯光效果
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.configNet);
        } else if (state == ApConstant.checkAssistantConenctWifiError
                || state == ApConstant.checkAssistantConenctWifiFail || state == ApConstant.apConfigWifiTimeout) {
            //ttsPlayer.ePlay(this.getClass().getSimpleName(), ETTSConstant.WifiErrorAndCheck);
            //ELightCtrl.mainLightCtrl(TAG, PlayAction.connectNetEnd);
        }
    }

    public void startApConfigWifi() {
        Log.d(TAG, "===>>startApConfigWifi start ap config wifi");
        boolean isStoped = mEApConnectWifiMgr.stopApConfigWifi();
        if (isStoped) {
            mEApConnectWifiMgr.startApconfigWifi();
        } else {
            Log.e(TAG, "last time apConfig process is not finish");
            return;
        }
        // when enter AP mode also show power led splash
    }
}
