/**
 * 
 */
package com.teemo.apconn;

/**
 * @author Peter 2015-12-25下午2:22:42
 */
public class ConnectWifiStateInfo {

    private String boxSn;
    private String boxIp;
    private String assistantIp;
    private int errorCode;
    /**
     * (1)toBox,toAssistant
     */
    private String sendDirection;

    public String getBoxSn() {
        return boxSn;
    }

    public void setBoxSn(String boxSn) {
        this.boxSn = boxSn;
    }

    public String getBoxIp() {
        return boxIp;
    }

    public void setBoxIp(String boxIp) {
        this.boxIp = boxIp;
    }

    public String getAssistantIp() {
        return assistantIp;
    }

    public void setAssistantIp(String assistantIp) {
        this.assistantIp = assistantIp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getSendDirection() {
        return sendDirection;
    }

    public void setSendDirection(String sendDirection) {
        this.sendDirection = sendDirection;
    }

}
