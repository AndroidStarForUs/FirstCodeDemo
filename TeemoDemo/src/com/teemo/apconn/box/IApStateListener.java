/*
 *****************************************************************************************
 * @file IApStateListener.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月18日  下午6:16:19  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn.box;

/**
 * @brief
 * 
 * @author Peter
 *
 * @date 2015年12月18日 下午6:16:19
 */
public interface IApStateListener {
    /**
     * @brief ap配置状态信息
     * @param state
     * @param resultInfo
     *            void
     */
    void apConfigWifiResult(int state, String resultInfo);
}
