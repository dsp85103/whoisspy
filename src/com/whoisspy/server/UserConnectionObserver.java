package com.whoisspy.server;

import com.whoisspy.Room;

public interface UserConnectionObserver {
    Room onCreateRoom(UserConnection roomOwner,
                      String roomName,
                      int roomClientAmount,
                      String roomDescription,
                      boolean roomPrivate,
                      String roomPassword);

    boolean onJoinRoom(UserConnection roomPlayer,
                       int roomId);

    boolean onDeleteRoom(UserConnection roomOwner,
                         int roomId);

    boolean onLeaveRoom(UserConnection roomPlayer);

    String onListRooms();

    boolean onChangeRoomOwner(UserConnection roomOwner, UserConnection roomNewOwner);

    void onLogin(UserConnection userConn);
    void onLogout(UserConnection userConn);

    boolean loginStatus(UserConnection userConn);
}
