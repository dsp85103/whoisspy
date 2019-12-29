package com.whoisspy.client.game;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.whoisspy.Message;
import com.whoisspy.PlayerGameInfo;
import com.whoisspy.RoomInformation;
import com.whoisspy.RoomMessage;
import com.whoisspy.server.RoomsManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class RoomPanel extends JPanel {

    private JButton roomInformationBtn;
    private JButton leaveRoomBtn;
    private JTable playerDashboardTable;
    private PlayerDashBoardTableModel playerDashBoardTableModel;
    private JTextArea chatTextArea;
    private JTextField chatTextField;
    private JButton sendChatBtn;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);
    private Font txtFont = new Font(fontName, Font.BOLD, 15);

    private RoomPanelObserver roomPanelObserver;
    private RoomInformation roomInformation;
    private ArrayList<PlayerData> playerDataArrayList;

    private String roomManager = "Admin";

    private Gson gson = new Gson();

    public RoomPanel(RoomPanelObserver roomPanelObserver, RoomInformation roomInformation, ArrayList<PlayerData> playerDataArrayList) {
        super();
        this.roomPanelObserver = roomPanelObserver;
        this.roomInformation = roomInformation;
        this.playerDataArrayList = playerDataArrayList;
        setLayout(new BorderLayout());

        roomInformationBtn = new JButton("房間資訊");
        roomInformationBtn.addActionListener(roomInformationBtnActionListener);
        roomInformationBtn.setFont(labelFont);

        leaveRoomBtn = new JButton("離開房間");
        leaveRoomBtn.addActionListener(leaveRoomBtnActionListener);
        leaveRoomBtn.setFont(labelFont);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 2));

        headerPanel.add(roomInformationBtn);
        headerPanel.add(leaveRoomBtn);

        playerDashBoardTableModel = new PlayerDashBoardTableModel(playerDataArrayList);
        playerDashboardTable = new JTable(playerDashBoardTableModel);
        playerDashboardTable.setFont(txtFont);
        playerDashboardTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        playerDashboardTable.getColumnModel().getColumn(0).setCellRenderer(new PlayerProfileLabelRenderer());
        playerDashboardTable.getColumnModel().getColumn(0).setCellEditor(new PlayerProfileLabelEditor(new JTextField()));
        playerDashboardTable.getColumnModel().getColumn(0).setMaxWidth(160);
        playerDashboardTable.getColumnModel().getColumn(0).setMinWidth(160);
        playerDashboardTable.getColumnModel().getColumn(1).setMaxWidth(80);
        playerDashboardTable.getColumnModel().getColumn(1).setMinWidth(80);
        playerDashboardTable.getColumnModel().getColumn(3).setMaxWidth(80);
        playerDashboardTable.getColumnModel().getColumn(3).setMinWidth(80);
        playerDashboardTable.setRowHeight(65);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        playerDashboardTable.setDefaultRenderer(String.class, centerRenderer);

        chatTextArea = new JTextArea(4, 20);
        chatTextArea.setFont(txtFont);
        chatTextArea.setEditable(false);
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        chatTextField = new JTextField();
        chatTextField.setFont(txtFont);

        sendChatBtn = new JButton("發送");
        sendChatBtn.addActionListener(sendChatBtnActionListener);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createTitledBorder("房間聊天區"));
        footerPanel.setFont(txtFont);
        footerPanel.add(scrollPane, BorderLayout.NORTH);
        footerPanel.add(chatTextField, BorderLayout.CENTER);
        footerPanel.add(sendChatBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(playerDashboardTable), BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private String word = "";
    public void onMessaged(Message roomMessage) {
        JsonObject data = gson.fromJson(roomMessage.getData(), JsonObject.class);
        Message.OP op = roomMessage.getOp();

        switch (op) {

            case playerJoin:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    PlayerData playerData = gson.fromJson(roomMessage.getData(), PlayerData.class);
                    playerDashBoardTableModel.playerJoin(playerData);
                    onChat(roomManager, playerData.getPlayerProfile().getAccount() + " 加入房間了，我們歡迎他。");
                }

                break;

            case playerLeave:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    PlayerData playerData = gson.fromJson(roomMessage.getData(), PlayerData.class);
                    playerDashBoardTableModel.playerLeave(playerData);
                    onChat(roomManager, playerData.getPlayerProfile().getAccount() + " 離開房間了，我們懷念他。");
                }
                break;

            case playerChat:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(data.get("account").getAsString(), data.get("chat").getAsString());
                }
                break;

            case gameStart:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    leaveRoomBtn.setEnabled(false);
                    onChat(roomManager, "人數已到齊～ 遊戲將於五秒後開始～");
                }
                break;


            case gameRefresh:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    //建立房間成功，等待 Server 將訊息送過來
                    ArrayList<PlayerData> playerDataArrayList = gson.fromJson(data.get("players").getAsString(),
                            new TypeToken<ArrayList<PlayerData>>() {
                            }.getType());
                    playerDashBoardTableModel.playerDataChanged(playerDataArrayList);

                }

                break;

            case gameWord:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    word = data.get("word").getAsString();
                    onChat(roomManager, String.format("您在這回合的身分是 '%s' 單字是 '%s'",
                            data.get("type").getAsString(),
                            data.get("word").getAsString()));
                    onChat(roomManager, "5 秒後開始作答");
                }
                break;

            case gameRoundStart:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JTextField playerInputTextField = new JTextField();
                            JLabel playerInputLabel = new JLabel("<html>請輸入您的描述<br>" +
                                    "（於 30 秒後關閉作答）<br>" +
                                    "您的單字是 " + word + "</html>");
                            JOptionPane playerInputDesOptionPane = new JOptionPane();
                            playerInputDesOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                            playerInputDesOptionPane.setMessage(new Object[]{playerInputLabel, playerInputTextField});
                            JDialog dialog = playerInputDesOptionPane.createDialog("誰是臥底");

                            // Set a 30 second timer
                            new Thread(new Runnable() {
                                int totalSecond = 30;
                                @Override
                                public void run() {
                                    try {
                                        for (int i = 0; i < totalSecond; i++) {
                                            Thread.sleep(1000);
                                            playerInputLabel.setText("<html>請輸入您的描述<br>" +
                                                    "（於 " + (totalSecond - i) + " 秒後關閉作答）<br>" +
                                                    "您的單字是 " + word + "</html>");

                                            updateTimeout(String.valueOf(totalSecond-i));
                                        }
                                        updateTimeout("離開房間");
                                    } catch (Exception e) {
                                    }
                                    dialog.dispose();
                                }
                            }).start();

                            dialog.setVisible(true);
                            String playerInputStr = playerInputTextField.getText();
                            onChat(roomManager, String.format("您輸入的描述為 '%s'", playerInputStr));
                            roomPanelObserver.onGameWordDes(playerInputStr);
                        }
                    }).start();
                }
                break;

            case gameRoundWait:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                }
                break;

            case gameVote:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    ArrayList<String> votedPlayerList = gson.fromJson(data.get("votedPlayer").getAsString(),
                            new TypeToken<ArrayList<String>>() {
                            }.getType());
                    onChat(roomManager, roomMessage.getMsg());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JLabel playerVoteLabel = new JLabel("<html>請選擇您認為的臥底<br>" +
                                    "（於 20 秒後關閉作答）</html>");
                            String[] votedPlayerArray = new String[votedPlayerList.size()];
                            votedPlayerList.toArray(votedPlayerArray);
                            JComboBox<String> votedPlayerComboBox = new JComboBox<>(votedPlayerArray);
                            votedPlayerComboBox.setSelectedIndex(0);
                            JOptionPane playerVoteOptionPane = new JOptionPane();
                            playerVoteOptionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
                            playerVoteOptionPane.setMessage(new Object[]{playerVoteLabel, votedPlayerComboBox});
                            JDialog dialog = playerVoteOptionPane.createDialog("誰是臥底");

                            // Set a 20 second timer
                            new Thread(new Runnable() {
                                int totalSecond = 20;
                                @Override
                                public void run() {
                                    try {
                                        for (int i = 0; i < totalSecond; i++) {
                                            Thread.sleep(1000);
                                            playerVoteLabel.setText("<html>請選擇您認為的臥底<br>" +
                                                    "（於 " + (totalSecond - i) + " 秒後關閉作答）</html>");

                                            updateTimeout(String.valueOf(totalSecond-i));
                                        }
                                        updateTimeout("離開房間");
                                    } catch (Exception e) {
                                    }
                                    dialog.dispose();
                                }
                            }).start();

                            dialog.setVisible(true);
                            String votedPlayerStr = String.valueOf(votedPlayerComboBox.getSelectedItem());
                            onChat(roomManager, String.format("您猜測的臥底為 '%s'", votedPlayerStr));
                            roomPanelObserver.onVote(votedPlayerStr);
                        }
                    }).start();
                }
                break;

            case gameGuessSuccess:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                }
                break;

            case gameGuessError:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                }
                break;

            case gameRoundOver:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                }
                break;

            case gameOver:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                    leaveRoomBtn.setEnabled(true);
                }
                break;

            case playerWait:

                if (roomMessage.getStatus().equals(Message.Status.process)) {
                    onChat(roomManager, roomMessage.getMsg());
                }
                break;

        }
    }

    public void updateTimeout(String txt) {
        leaveRoomBtn.setText(txt);
    }

    public void onChat(String account, String msg) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        chatTextArea.append(String.format("[%s][%s] %s\n", sdf.format(now), account, msg));
        chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
    }

    public ActionListener roomInformationBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String information = "<html>" +
                    "房間編號: " + roomInformation.getRoomId() + "<br>" +
                    "房間名稱: " + roomInformation.getRoomName() + "<br>" +
                    "房間人數: " + roomInformation.getRoomAmount() + "<br>" +
                    "房間描述: " + roomInformation.getRoomDescription() + "<br>" +
                    "私人房間: " + roomInformation.isRoomPrivate() + "<br>";
            if (roomInformation.isRoomPrivate()) {
                information += "房間密碼: " + roomInformation.getRoomPassword() + "<br></html>";
            } else {
                information += "</html>";
            }

            JLabel roomInformationLabel = new JLabel(information);
            JOptionPane.showMessageDialog(null, roomInformationLabel);
        }
    };

    public ActionListener leaveRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            roomPanelObserver.onLeaveRoom(roomInformation.getRoomId());
        }
    };

    public ActionListener sendChatBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            roomPanelObserver.onChatSend(chatTextField.getText());
            chatTextField.setText("");
        }
    };

}
