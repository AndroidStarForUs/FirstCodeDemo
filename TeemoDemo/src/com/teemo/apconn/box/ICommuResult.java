/*
 *****************************************************************************************
 * @file ICommuResult.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月18日  下午6:07:31  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn.box;

/**
 * @brief 内部通信回调
 * 
 * @author Peter
 *
 * @date 2015年12月18日 下午6:07:31
 */
public interface ICommuResult {
    void onResult(int state, String info);
}
