package com.whoisspy.client.lobby;

public interface CreateRoomObserver {
    void OnClickedCreateBtn(String roomName, String roomAmount, String roomDescription, boolean roomPrivate, String roomPassword);
}
