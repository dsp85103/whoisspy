package com.whoisspy.client.game;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDashBoardTableModel extends AbstractTableModel {

    private List<PlayerData> playersDataList;
    private Map<String, Integer> playersDataIndexMap = new HashMap<>();
    private String[] columnNames =  {"玩家", "狀態", "描述", "投票"};
    private static final Class<?>[] COLUMN_TYPES = new Class<?>[] {JLabel.class, String.class, String.class,  String.class};

    public PlayerDashBoardTableModel(List<PlayerData> playersData) {
        playersDataList = playersData;
        for (int index = 0; index < playersDataList.size() ; index++) {
            playersDataIndexMap.put(playersDataList.get(index).getPlayerProfile().getAccount(), index);
        }
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
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_TYPES[columnIndex];
    }

    @Override
    public int getRowCount() {
        return playersDataList.size();
    }

    private Map<String, JScrollPane> playerTextArea = new HashMap<>();
    @Override
    public Object getValueAt(int row, int column) {
        Object playerAttribute = null;
        PlayerData playerData = playersDataList.get(row);
        switch(column) {
            case 0:
                playerAttribute = playerData.getPlayerProfile();
                break;
            case 1:
                playerAttribute = playerData.getPlayerStatus();
                break;
            case 2:
                playerAttribute = playerData.getPlayerText();
                break;
            case 3:
                playerAttribute = playerData.getPlayerVotes();
                break;
            default:
                break;
        }
        return playerAttribute;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        if (columnIndex == 0) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public void playerJoin(PlayerData playerData) {
        if (!playersDataList.contains(playerData)) {
            playersDataList.add(playerData);
        }
        fireTableDataChanged();
    }

    public void playerLeave(PlayerData playerData) {
        for (PlayerData deletePlayer : playersDataList) {
            if (deletePlayer.getPlayerProfile().getAccount().equals(playerData.getPlayerProfile().getAccount())) {
                playersDataList.remove(deletePlayer);
                break;
            }
        }
        fireTableDataChanged();
    }

    @Override
    public void fireTableDataChanged() {
        playersDataIndexMap.clear();
        for (PlayerData playerData : playersDataList) {
            playersDataIndexMap.put(playerData.getPlayerProfile().getAccount(), playersDataList.indexOf(playerData));
        }
        super.fireTableDataChanged();
    }

    public void playerDataChanged(ArrayList<PlayerData> changedData) {

        for (PlayerData changePlayerData : changedData) {
            int index = playersDataIndexMap.get(changePlayerData.getPlayerProfile().getAccount());
            PlayerData playerData = playersDataList.get(index);
            playerData.setPlayerStatus(changePlayerData.getPlayerStatus());
            playerData.setPlayerText(changePlayerData.getPlayerText());
            playerData.setPlayerVotes(changePlayerData.getPlayerVotes());
        }
        fireTableDataChanged();
    }


}
