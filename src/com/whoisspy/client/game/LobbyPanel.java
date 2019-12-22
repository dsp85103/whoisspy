package com.whoisspy.client.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyPanel extends JPanel {

    private JButton createRoomBtn;
    private JButton listRoomBtn;
    private JButton joinRoomBtn;
    private JButton profileBtn;

    private String fontName = "微軟正黑體";
    private Font btnFont = new Font(fontName, Font.BOLD, 18);

    private LobbyPanelObserver lobbyPanelObserver;

    public LobbyPanel(LobbyPanelObserver lobbyPanelObserver) {
        super();
        this.lobbyPanelObserver = lobbyPanelObserver;
        setLayout(null);
        setBackground(Color.BLACK);

        createRoomBtn = new JButton("創立房間");
        createRoomBtn.setLocation(235, 80);
        createRoomBtn.setSize(165, 50);
        createRoomBtn.setFont(btnFont);
        createRoomBtn.addActionListener(createRoomBtnActionListener);

        listRoomBtn = new JButton("查看所有房間");
        listRoomBtn.setLocation(235, 140);
        listRoomBtn.setSize(165, 50);
        listRoomBtn.setFont(btnFont);
        listRoomBtn.addActionListener(listRoomBtnActionListener);

        joinRoomBtn = new JButton("加入房間");
        joinRoomBtn.setLocation(235, 200);
        joinRoomBtn.setSize(165, 50);
        joinRoomBtn.setFont(btnFont);
        joinRoomBtn.addActionListener(joinRoomBtnActionListener);

        profileBtn = new JButton("查看個人檔案");
        profileBtn.setLocation(235, 260);
        profileBtn.setSize(165, 50);
        profileBtn.setFont(btnFont);
        profileBtn.addActionListener(profileRoomBtnActionListener);

        add(createRoomBtn);
        add(listRoomBtn);
        add(joinRoomBtn);
        add(profileBtn);
    }

    public ActionListener createRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            lobbyPanelObserver.OnClickedCreateRoomBtn();
        }
    };

    public ActionListener listRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            lobbyPanelObserver.OnClickedListRoomBtn();
        }
    };

    public ActionListener joinRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = JOptionPane.showInputDialog("請輸入房間號碼");
            if (input != null) {
                lobbyPanelObserver.OnClickedJoinRoomBtn(input);
            }
        }
    };


    public ActionListener profileRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            lobbyPanelObserver.OnClickedProfileBtn();
        }
    };

}
