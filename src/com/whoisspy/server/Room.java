package com.whoisspy.server;

import com.whoisspy.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private Map<String, UserConnection> clients = new HashMap<>();
    private UserConnection roomOwner;
    private int roomId;
    private String roomName;
    private int roomClientAmount;
    private String roomDescription;
    private boolean roomPrivate;

    public Room(UserConnection roomOwner, Integer id, String roomName, int roomClientAmount, String roomDescription, boolean roomPrivate) {
        this.roomOwner = roomOwner;
        this.roomId = id;
        this.roomName = roomName;
        this.roomClientAmount = roomClientAmount;
        this.roomDescription = roomDescription;
        this.roomPrivate = roomPrivate;

        clients.put(roomOwner.getUser().getAccount(), roomOwner);
    }

    public boolean addPlayer(UserConnection userConnection) {
        if (!clients.containsKey(userConnection.getUser().getAccount())) {
            clients.put(userConnection.getUser().getAccount(), userConnection);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(UserConnection userConnection) {
        if (clients.containsKey(userConnection.getUser().getAccount())) {
            clients.remove(userConnection.getUser().getAccount());
            return true;
        } else {
            return false;
        }
    }

    public boolean changeOwner(UserConnection newOwner) {
        this.roomOwner = newOwner;
        return true;
    }

    public boolean isOwner(UserConnection userConn) {
        return roomOwner == userConn;
    }

    public int getRoomClientAmount() {
        return roomClientAmount;
    }

    public int getRoomClientCount() {
        return clients.size();
    }

    public void start() {

    }

    public void NotifyAllPlayerMessage() {

    }

}
