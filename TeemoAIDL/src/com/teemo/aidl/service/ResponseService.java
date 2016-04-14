/*
*****************************************************************************************
* @file ResponseService.java
*
* @brief 
*       1:创建Aidl文件，并设计Aidl接口，此时在gen文件中生成Aidl文件生成的Class
*       2：创建Service
*       3：在Service中建继承Aidl.stub的内部类，并实现Aidl接口方法
*       4：在Mainfeast中注册Service并配置Action
*       5：复制gen下的class到客户端(包名需要和Aidl的包名相同)
*       6：绑定Service(绑定Service需要ServiceConnect)，绑定Service成功后得到Aidl的对象
*       7：通过Aidl对象调用Aidl中的方法
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
