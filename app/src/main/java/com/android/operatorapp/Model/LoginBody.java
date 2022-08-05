package com.android.operatorapp.Model;

import androidx.annotation.Keep;

@Keep public class LoginBody {
    private String username;
    private String password;
    private String fcmToken;
    private String device;

    public LoginBody(String username, String password, String fcmToken, String device) {
        this.username = username;
        this.password = password;
        this.fcmToken = fcmToken;
        this.device = device;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
