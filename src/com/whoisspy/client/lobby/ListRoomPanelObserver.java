package com.whoisspy.client.lobby;

public interface ListRoomPanelObserver {
    void onClickedJoinRoomBtn(String roomId);
    void onRefreshRooms();
}
