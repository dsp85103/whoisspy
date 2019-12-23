package com.whoisspy;

public class RoomInformation {

    private int roomId;
    private String roomName;
    private int roomAmount;
    private int roomCount;
    private String roomDescription;
    private boolean roomPrivate;
    private String roomPassword = "";

    public RoomInformation(int roomId, String roomName, int roomAmount, int roomCount, String roomDescription, boolean roomPrivate, String roomPassword) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomAmount = roomAmount;
        this.roomCount = roomCount;
        this.roomDescription = roomDescription;
        this.roomPrivate = roomPrivate;
        this.roomPassword = roomPassword;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(int roomAmount) {
        this.roomAmount = roomAmount;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public boolean isRoomPrivate() {
        return roomPrivate;
    }

    public void setRoomPrivate(boolean roomPrivate) {
        this.roomPrivate = roomPrivate;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }
}
