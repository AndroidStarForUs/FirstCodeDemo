/*
*****************************************************************************************
* @file ResponseService.java
*
* @brief 
*
* Code History:
*       2016-4-14  上午11:08:42  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.aidl.service;

import com.teemo.aidl.ITeemoAidlIntf;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-4-14 上午11:08:42
 */
public class ResponseService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new ResponseServiceImpl();
    }

    public class ResponseServiceImpl extends ITeemoAidlIntf.Stub {

        @Override
        public int getSumResult(int a, int b) throws RemoteException {
            return a + b;
        }
    }
}
