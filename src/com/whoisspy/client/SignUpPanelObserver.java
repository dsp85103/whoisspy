package com.whoisspy.client;

public interface SignUpPanelObserver {
    void OnClickedSignUpBtn(String account, String password, String email, String photoBase64);
}
