package com.android.operatorapp.Model;

public class CallItem {
    String phNumber,
            contactName,
            callType,
            callDate,
            callTime,
            callDuration,uniqueId;
    int state;

    public CallItem(String phNumber, String contactName, String callType, String callDate, String callTime, String callDuration, int state,String uniqueId) {
        this.phNumber = phNumber;
        this.contactName = contactName;
        this.callType = callType;
        this.callDate = callDate;
        this.callTime = callTime;
        this.callDuration = callDuration;
        this.state = state;
        this.uniqueId=uniqueId;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
