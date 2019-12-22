package com.whoisspy.client.game;

public interface CreateRoomObserver {
    void OnClickedCreateBtn(String roomName, String roomAmount, String roomDescription, boolean roomPrivate, String roomPassword);
}
