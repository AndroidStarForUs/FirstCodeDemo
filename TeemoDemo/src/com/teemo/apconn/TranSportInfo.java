/*
 *****************************************************************************************
 * @file TranSportInfo.java
 *
 * @brief 
 *
 * Code History:
 *       2015年12月19日  下午4:02:51  Peter , initial version
 *
 * Code Review:
 *
 ********************************************************************************************
 */

package com.teemo.apconn;

/**
 * @brief
 * 
 * @author Peter
 *
 * @date 2015年12月19日 下午4:02:51
 */
public class TranSportInfo {

    private String SSID;
    private String password ;
    private Integer encryptType = -1;
    private String userId ;
    private String phoneNumber;
    private String boxSn;
    private int stateCode;

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String sSID) {
        SSID = sSID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(Integer encryptType) {
        this.encryptType = encryptType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBoxSn() {
        return boxSn;
    }

    public void setBoxSn(String boxSn) {
        this.boxSn = boxSn;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "TranSportInfo [SSID=" + SSID + ", password=" + password + ", encryptType=" + encryptType + ", userId="
                + userId + ", phoneNumber=" + phoneNumber + ", boxSn=" + boxSn + ", stateCode=" + stateCode + "]";
    }

 
}
