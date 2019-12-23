package com.whoisspy;

import com.whoisspy.server.UserConnection;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private Map<String, UserConnection> clients = new HashMap<>();
    private UserConnection roomOwner;
    private RoomInformation roomInformation;

    public Room(UserConnection roomOwner,
                Integer id,
                String roomName,
                int roomAmount,
                String roomDescription,
                boolean roomPrivate,
                String roomPassword) {

        this.roomOwner = roomOwner;
        roomInformation = new RoomInformation(id, roomName, roomAmount, 0, roomDescription, roomPrivate, roomPassword);

        //clients.put(roomOwner.getUser().getAccount(), roomOwner);
    }

    public Map<String, UserConnection> getClients() {
        return clients;
    }

    public boolean addPlayer(UserConnection userConnection) {
        if (!clients.containsKey(userConnection.getUser().getAccount())) {
            clients.put(userConnection.getUser().getAccount(), userConnection);
            roomInformation.setRoomCount(roomInformation.getRoomCount()+1);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(UserConnection userConnection) {
        if (clients.containsKey(userConnection.getUser().getAccount())) {
            clients.remove(userConnection.getUser().getAccount());
            roomInformation.setRoomCount(roomInformation.getRoomCount()+1);
            return true;
        } else {
            return false;
        }
    }

    public boolean changeOwner(UserConnection newOwner) {
        this.roomOwner = newOwner;
        return true;
    }

    public RoomInformation getRoomInformation() {
        return roomInformation;
    }

    public boolean isOwner(UserConnection userConn) {
        return roomOwner == userConn;
    }

    public boolean isRoomPrivate() {
        return roomInformation.isRoomPrivate();
    }

    public String getRoomName() {
        return roomInformation.getRoomName();
    }

    public int getRoomAmount() {
        return roomInformation.getRoomAmount();
    }

    public String getRoomDescription() {
        return roomInformation.getRoomDescription();
    }

    public String getRoomPassword() {
        return roomInformation.getRoomPassword();
    }

    public int getRoomId() {
        return roomInformation.getRoomId();
    }

    public int getRoomClientCount() {
        return clients.size();
    }

    public void start() {

    }

    public void NotifyAllPlayerMessage() {
        for (UserConnection player : clients.values()) {

            //player.send();
        }
    }

}
