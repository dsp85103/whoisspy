package com.whoisspy.client;

public interface LoginPanelObserver {
    void OnClickedLoginBtn(String account, String password);
    void OnClickedForgotPasswordLabel();
    void OnClickedSignUpLabel();
}
