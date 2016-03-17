/*
*****************************************************************************************
* @file ResponseBody.java
*
* @brief 
*
* Code History:
*       2015年9月16日  下午9:05:36  Peter , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.teemo.json;

import org.json.JSONObject;

import com.google.gson.JsonObject;

/**
 * @brief 
 * 
 * @author Peter
 *
 * @date 2015年9月16日 下午9:05:36
 */
public class ResponseBody {

	private int status = -1; // 响应状态码
	private String errorCode = null; // 错误代码
	private String errorMsg = null; //错误消息
	private String boxVersion = null;  // 盒子端发送，
	private JSONObject content = null; // 返回结果
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getBoxVersion() {
        return boxVersion;
    }
    public void setBoxVersion(String boxVersion) {
        this.boxVersion = boxVersion;
    }
    public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
    @Override
    public String toString() {
        return "ResponseBody [status=" + status + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg
                + ", boxVersion=" + boxVersion + ", content=" + content + "]";
    }
	
	
	
		
}
