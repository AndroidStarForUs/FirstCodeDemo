/*
 *****************************************************************************************
 * @file AidlActivity.java
 *
 * @brief 
 *
 * Code History:
 *       2016-4-14  上午10:34:27  Teemo , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.aidl;

import com.teemo.demo.R;
import com.teemo.utils.LogMgr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

/**
 * @brief Invoking aidl interface.
 * 
 * @author Teemo
 * 
 * @date 2016-4-14 上午10:34:27
 */
public class AidlActivity extends Activity {
    private final String MODULE = AidlActivity.class.getSimpleName();
    private final String TAG = AidlActivity.class.getSimpleName();
    private final String AIDL_ACTION = "com.teemo.aidl_ServiceIMPL";

    //private ITeemo mITeemo;
    private ITeemoAidlIntf mAidlIntf;
    private ServiceConnection connection = null;
    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_activity);

        connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogMgr.d(MODULE, TAG, "The aidl service is connected.");
                //mITeemo = ITeemo.Stub.asInterface(service);
                mAidlIntf = ITeemoAidlIntf.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogMgr.d(MODULE, TAG, "The aidl service is disconnected.");
            }
        };
    }

    public void onBindService(View v) {
        if (connection == null) {
            return;
        }
        try {
            isBind = bindService(new Intent(AIDL_ACTION), connection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            LogMgr.d(MODULE, TAG, "onBindService error." + e.getMessage());
            isBind = false;
        }
        if (!isBind) {
            Toast.makeText(this, "binding failure.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onInvoking(View v) {
        int mRes = 0;
        if (!isBind) {
            Toast.makeText(this, "Please bind operations.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mRes = mAidlIntf.getSumResult(1, 5);
        } catch (RemoteException e) {
            LogMgr.d(MODULE, TAG, "onInvoking error." + e.getMessage());
            e.printStackTrace();
        }
        Toast.makeText(this, "Result = " + mRes, Toast.LENGTH_SHORT).show();
    }
}
