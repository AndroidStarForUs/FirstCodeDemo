/**
 * 
 */
package com.teemo.demo;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.media.AudioManager;
import android.util.Log;

/**
 * @author alan
 *
 */
public class MyApplication extends Application {

    public static MyApplication s_Context = null;
    private AudioManager mAudioManager = null;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BootBroadcastReceiver", "====> MyApplication onCreate");
        s_Context = this;
        ApplicationContext.ContextSet(this.getPackageName(), this);
        /*BoxManager.getInstance().setBoxPolicy(BoxManager.POLICY_FOR_VOICEAPP);
        BoxManager.getInstance().stopBootLedShowing(); // close framework power
                                                       // on startUp light show
        SQLiteDatabase db = Connector.getDatabase();
        SDKInitializer.initialize(getApplicationContext());*/
        // uncatchExecptionForRestartMainApplication();
        InitHardWareSetting();
    }

    /**
     * @brief 初始化声音设置
     *
     *        void
     */
    private void InitHardWareSetting() {/*
        int currentVol = 5;  //default volume
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        JSSharedPreferences Status = new JSSharedPreferences(ApplicationContext.GetMainContext(), JSSharedPreferences.deviceSettingPreferenceName);
        if(Status != null)
        {
            currentVol = Status.getPrefInteger("systemVolume", 5);
        }
        Log.d("MyApplication", "====> get system volume: " + currentVol);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol, 0);
    */}

    /**
     * 拦截不可捕获的异常，并且重启主程序
     */
    private void uncatchExecptionForRestartMainApplication() {
        // 拦截异常
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                // 关闭AirPlay
                //EMediaControl.getInstance().controlWorker(6, "AirplayWorker", false);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

}
