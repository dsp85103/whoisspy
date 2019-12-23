package com.whoisspy.client.game;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.whoisspy.RoomInformation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ListRoomPanel extends JPanel {

    private JButton joinRoomBtn;
    private JTable roomsTable;
    private RoomsTableModel roomsTableModel;

    private String fontName = "微軟正黑體";
    private Font btnFont = new Font(fontName, Font.BOLD, 18);
    private Font tableFont = new Font(fontName, Font.PLAIN, 15);

    private ListRoomPanelObserver listRoomPanelObserver;
    public ListRoomPanel(ListRoomPanelObserver listRoomPanelObserver, JsonObject roomsDataObj) {
        super();
        this.listRoomPanelObserver = listRoomPanelObserver;
        setLayout(new GridLayout(2, 1));
        setBackground(Color.BLACK);

        Gson gson = new Gson();
        ArrayList<RoomInformation> roomInformationList = gson.fromJson(roomsDataObj.get("rooms").getAsString(),
                new TypeToken<ArrayList<RoomInformation>>(){}.getType());

        roomsTableModel = new RoomsTableModel(roomInformationList);
        roomsTable = new JTable(roomsTableModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(300, 100);
            }
        };
        roomsTable.setFont(tableFont);
        roomsTable.addMouseListener(roomsTableMouseListener);


        joinRoomBtn = new JButton("加入房間");
        joinRoomBtn.setFont(btnFont);
        joinRoomBtn.setEnabled(false);

        add(new JScrollPane(roomsTable));
        add(joinRoomBtn);
    }


    public MouseListener roomsTableMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println(roomsTableModel.getSelectedRoomId(roomsTable.getSelectedRow()));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };




}
