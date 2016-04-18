/*
*****************************************************************************************
* @file SmackHelper.java
*
* @brief 
*
* Code History:
*       2016-4-18  下午6:37:57  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.xmpp;

import java.util.Queue;

/**
 * @brief 
 * 
 * @author Teemo
 *
 * @date 2016-4-18 下午6:37:57
 */
public class SmackHelper {

    /**
     * The singleton instance of this class.
     */
    private static SmackHelper s_SmackHelper;

    /**
     * The host, it could be an ip address or a domain name.
     */
    private final String mHost = "";

    /**
     * Port of the server.
     */
    private final int mPort = 0;

    /**
     * The name of the server.
     */
    private final String  mServerName = "cluster.openfire";

    private String mUserName = "";

    private String mPassword = "";

    private Queue<SmackTask> mTasks = null;


}
