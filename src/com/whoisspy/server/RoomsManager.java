package com.whoisspy.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.whoisspy.Room;
import com.whoisspy.RoomInformation;
import com.whoisspy.User;

import java.util.*;

// 房間需要的操作行為 統一由 room manager 處理

public class RoomsManager {

    private Map<Integer, Room> rooms = new HashMap<>();
    private List<Integer> roomIds = new ArrayList<>();

    public RoomsManager() {

    }

    private Gson gson = new Gson();

    public Room createRoom(UserConnection owner,
                           String roomName,
                           int roomClientAmount,
                           String roomDescription,
                           boolean roomPrivate,
                           String roomPassword) {

        int roomId;

        do {

            roomId = (int) (Math.random() * 1000) + 1;

        } while (roomIds.contains(roomId));

        Room createdRoom = new Room(owner, roomId, roomName, roomClientAmount, roomDescription, roomPrivate, roomPassword);
        rooms.put(roomId, createdRoom);
        return createdRoom;
    }

    public boolean joinRoom(UserConnection roomPlayer, Integer destRoomId) {
        if (rooms.containsKey(destRoomId)) {
            rooms.get(destRoomId).addPlayer(roomPlayer);
            return true;
        } else {
            return false;
        }
    }

    public boolean leaveRoom(UserConnection roomPlayer, Integer roomId) {
        if (rooms.containsKey(roomId)) {
            Room room = rooms.get(roomId);
            room.removePlayer(roomPlayer);
            if (room.getRoomClientCount() == 0) {
                return deleteRoom(roomId);
            }
            return true;
        } else {
            return false;
        }
    }

    public String listRooms() {

        JsonObject roomsData = new JsonObject();
        List<RoomInformation> roomInformationList = new ArrayList<>();
        if (rooms.size() != 0) {
            for (Room room : rooms.values()) {
                roomInformationList.add(room.getRoomInformation());
            }
        } else {
            return "";
        }

        roomsData.addProperty("rooms", gson.toJson(roomInformationList, new TypeToken<List<RoomInformation>>() {}.getType()));
        return gson.toJson(roomsData);

    }

    public boolean forceLeaveRoom(String account) {
        for (Room room : rooms.values()) {
            if (room.getClients().containsKey(account)) {
                room.removePlayer(room.getClients().get(account));
                if (room.getRoomClientCount() == 0) {
                    return deleteRoom(room.getRoomId());
                }
                return true;
            }
        }
        return false;
    }

    public boolean deleteRoom(Integer roomId) {
        if (rooms.containsKey(roomId)) {
            rooms.remove(roomId);
            return true;
        } else {
            return false;
        }
    }

    public boolean changeRoomOwner(UserConnection owner, Integer roomId, UserConnection newOwner) {
        if (rooms.containsKey(roomId)) {
            Room room = rooms.get(roomId);
            if (room.isOwner(owner)) {
                return room.changeOwner(newOwner);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
