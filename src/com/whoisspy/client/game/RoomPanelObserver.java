package com.whoisspy.client.game;

public interface RoomPanelObserver {
    void onChatSend(String chatText);
    void onLeaveRoom(int roomId);
    void onGameWordDes(String description);
    void onVote(String votedPlayer);
}
