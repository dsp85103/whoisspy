package com.whoisspy.client.lobby;

public interface LobbyPanelObserver {
    void OnClickedCreateRoomBtn();
    void OnClickedListRoomBtn();
    void OnClickedJoinRoomBtn(String roomNumberStr);
    void OnClickedProfileBtn();

}
