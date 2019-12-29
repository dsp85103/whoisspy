package com.whoisspy.client.lobby;

public interface ProfilePanelObserver {
    void OnClickedModifyPasswordBtn();
    void OnClickedSaveBtn(String email, String photoBase64);
}
