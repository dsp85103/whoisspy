package com.whoisspy.server;

import com.whoisspy.User;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private List<UserConnection> clients = new ArrayList<>();
    private User roomOwner;
    private int roomNumber;
    private String roomName;
    private int roomClientAmount;
    private int roomClientCount;
    private String roomDescription;
    private boolean roomPrivate;

    public Room(User roomOwner, String roomName, int roomClientAmount, String roomDescription, boolean roomPrivate) {
        this.roomOwner = roomOwner;
        this.roomName = roomName;
        this.roomClientAmount = roomClientAmount;
        this.roomDescription = roomDescription;
        this.roomPrivate = roomPrivate;
    }

    public boolean addPlayer(UserConnection userConnection) {
        return clients.add(userConnection);
    }

    public boolean removePlayer(UserConnection userConnection) {
        return clients.remove(userConnection);
    }

    public void start() {

    }

    public void NotifyAllPlayerMessage() {

    }

}
