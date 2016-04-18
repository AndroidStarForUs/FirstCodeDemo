/*
*****************************************************************************************
* @file SmackTask.java
*
* @brief 
*
* Code History:
*       2016-4-18  下午6:46:47  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.xmpp;
/**
 * @brief Tasks for the SessionSmackHelper to do. Typically, the SessionClient creates a task and
 * send to the SessionSmackHelper.
 * @author Teemo
 *
 * @date 2016-4-18 下午6:46:47
 */
public class SmackTask {

    public enum TASK_TYPE {
        TASK_NONE,              // default, means nothing
        TASK_LOGIN,             // to login when the app launches.
        TASK_SEND,              // to send a message to the server.
        TASK_LOGOUT,            // to logout, disconnect the xmpp connection.
        TASK_CLEAN,             // to clean up when the app stops
        TASK_SEND_RECEIPT,      // to send a receipt to the server.
    }

    /**
     * The type of this SessionTask, default is none.
     */
    private TASK_TYPE mType = TASK_TYPE.TASK_NONE;

    
    /**
     * Main part of this task.
     * for TASK_NONE: enpty String.
     * for TASK_LOGIN: user name and password || empty string(when user name and password are already set).
     * for TASK_SEND: String in json/xml format that contains the message.
     * for TASK_CLEAN: empty String.
     */
    private String mContent = "";

    private String mUUId = "";

    private int mRetryTime = 3;

    /**
     * @brief Default constructor.
     */
    public SmackTask() {}

    public SmackTask(TASK_TYPE type) {
        this.mType = type;
    }

    /**
     * @brief Set the type of the SessionTask.
     *
     * @param type Type to be set.
     */
    public void setType(TASK_TYPE type) {
        this.mType = type;
    }

    public TASK_TYPE getType() {
        return mType;
    }

    /**
     * @brief Get current content.
     *
     * @return Current content.
     */
    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getId() {
        return mUUId;
    }

    public void setId(String mUUId) {
        this.mUUId = mUUId;
    }

    public int getRetryTime() {
        return mRetryTime;
    }

    public void setRetryTime(int mRetryTime) {
        this.mRetryTime = mRetryTime;
    }

    public void onTaskFailed() {
        mRetryTime = -1;
    }
}
