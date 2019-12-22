package com.whoisspy.server;

import com.whoisspy.User;

public interface UserConnectionObserver {
    boolean onCreateRoom(UserConnection roomOwner,
                         String roomName,
                         int roomClientAmount,
                         String roomDescription,
                         boolean roomPrivate);

    boolean onJoinRoom(UserConnection roomPlayer,
                       int roomId);

    boolean onDeleteRoom(UserConnection roomOwner,
                         int roomId);

    boolean onLeaveRoom(UserConnection roomPlayer);

    boolean onChangeRoomOwner(UserConnection roomOwner, UserConnection roomNewOwner);

    void onLogin(UserConnection userConn);
    void onLogout(UserConnection userConn);

    boolean loginStatus(UserConnection userConn);
}
