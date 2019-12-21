package com.whoisspy.client.game;

public interface LobbyPanelObserver {
    void OnClickedCreateRoomBtn();
    void OnClickedListRoomBtn();
    void OnClickedJoinRoomBtn(String roomNumber);
    void OnClickedProfileBtn();

}
