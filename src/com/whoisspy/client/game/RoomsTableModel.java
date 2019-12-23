package com.whoisspy.client.game;
import com.whoisspy.RoomInformation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class RoomsTableModel extends AbstractTableModel {

    private List<RoomInformation> roomsData = new ArrayList<RoomInformation>();
    private String[] columnNames =  {"房號", "名稱", "人數", "描述", "私人?"};

    public RoomsTableModel(List<RoomInformation> roomsData) {
        this.roomsData = roomsData;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return roomsData.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Object roomAttribute = null;
        RoomInformation roomObject = roomsData.get(row);
        switch(column) {
            case 0: roomAttribute = roomObject.getRoomId(); break;
            case 1: roomAttribute = roomObject.getRoomName(); break;
            case 2: roomAttribute = roomObject.getRoomCount()+"/"+roomObject.getRoomAmount(); break;
            case 3: roomAttribute = roomObject.getRoomDescription(); break;
            case 4: roomAttribute = roomObject.isRoomPrivate(); break;
            default: break;
        }
        return roomAttribute;
    }

    public Object getSelectedRoomId(int row) {
        return getValueAt(row, 0);
    }

    public void addRoom(RoomInformation room) {
        roomsData.add(room);
        fireTableDataChanged();
    }
}
