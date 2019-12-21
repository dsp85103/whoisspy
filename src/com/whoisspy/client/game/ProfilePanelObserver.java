package com.whoisspy.client.game;

public interface ProfilePanelObserver {
    void OnClickedModifyPasswordBtn();
    void OnClickedSaveBtn(String email, String photoBase64);
}
