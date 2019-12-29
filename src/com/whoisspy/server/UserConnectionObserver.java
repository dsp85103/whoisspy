package com.whoisspy.server;

import com.whoisspy.Room;
import com.whoisspy.RoomInformation;

import java.util.List;

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

    RoomInformation getRoomInformation(int roomId);

    boolean onLeaveRoom(UserConnection roomPlayer, int roomId);

    Room getRoom(int roomId);

    List<RoomInformation> getRoomsList();

    String getRoomPlayersData(int roomId);

    boolean onChangeRoomOwner(UserConnection roomOwner, UserConnection roomNewOwner);

    void onLogin(UserConnection userConn);
    void onLogout(UserConnection userConn);

    boolean loginStatus(UserConnection userConn);
}
