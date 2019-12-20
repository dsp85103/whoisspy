package com.whoisspy.client;

public interface ModifyPasswordPanelObserver {
    void OnClieckedModifyBtn(String account, String email, String newPwd, String confirmPwd);
}
