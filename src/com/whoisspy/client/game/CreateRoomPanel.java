package com.whoisspy.client.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateRoomPanel extends JPanel {

    private JLabel roomNameLabel;
    private JLabel roomAmountLabel;
    private JLabel roomDescriptionLabel;
    private JLabel roomPrivateLabel;
    private JLabel roomPasswordLabel;
    private JTextField roomNameTextField;
    private JTextField roomAmountTextField;
    private JTextField roomDescriptionTextField;
    private JCheckBox roomPrivateChkBox;
    private JPasswordField roomPasswordPwdField;
    private JButton createRoomBtn;

    private CreateRoomObserver createRoomObserver;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);
    private Font txtFont = new Font(fontName, Font.BOLD, 18);

    public CreateRoomPanel(CreateRoomObserver createRoomObserver) {
        super();
        this.createRoomObserver = createRoomObserver;
        setLayout(null);
        setBackground(Color.BLACK);

        roomNameLabel = new JLabel("房間名稱：");
        roomNameLabel.setLocation(125, 80);
        roomNameLabel.setSize(125, 30);
        roomNameLabel.setFont(labelFont);
        roomNameLabel.setForeground(Color.WHITE);

        roomAmountLabel = new JLabel("房間人數：");
        roomAmountLabel.setLocation(125, 120);
        roomAmountLabel.setSize(125, 30);
        roomAmountLabel.setFont(labelFont);
        roomAmountLabel.setForeground(Color.WHITE);

        roomDescriptionLabel = new JLabel("房間描述：");
        roomDescriptionLabel.setLocation(125, 160);
        roomDescriptionLabel.setSize(125, 30);
        roomDescriptionLabel.setFont(labelFont);
        roomDescriptionLabel.setForeground(Color.WHITE);

        roomPrivateLabel = new JLabel("私人房間：");
        roomPrivateLabel.setLocation(125, 200);
        roomPrivateLabel.setSize(125, 30);
        roomPrivateLabel.setFont(labelFont);
        roomPrivateLabel.setForeground(Color.WHITE);

        roomPasswordLabel = new JLabel("房間密碼：");
        roomPasswordLabel.setLocation(125, 240);
        roomPasswordLabel.setSize(125, 30);
        roomPasswordLabel.setFont(labelFont);
        roomPasswordLabel.setForeground(Color.WHITE);

        roomNameTextField = new JTextField();
        roomNameTextField.setLocation(260, 80);
        roomNameTextField.setSize(250, 30);
        roomNameTextField.setFont(txtFont);

        roomAmountTextField = new JTextField();
        roomAmountTextField.setLocation(260, 120);
        roomAmountTextField.setSize(125, 30);
        roomAmountTextField.setFont(txtFont);

        roomDescriptionTextField = new JTextField();
        roomDescriptionTextField.setLocation(260, 160);
        roomDescriptionTextField.setSize(250, 30);
        roomDescriptionTextField.setFont(txtFont);

        roomPrivateChkBox = new JCheckBox();
        roomPrivateChkBox.setLocation(260, 200);
        roomPrivateChkBox.setSize(30, 30);

        roomPasswordPwdField = new JPasswordField();
        roomPasswordPwdField.setLocation(260, 240);
        roomPasswordPwdField.setSize(250, 30);
        roomPasswordPwdField.setFont(txtFont);

        createRoomBtn = new JButton("建立房間");
        createRoomBtn.setLocation(170, 300);
        createRoomBtn.setSize(300, 50);
        createRoomBtn.setFont(defaultFont);
        createRoomBtn.addActionListener(createRoomBtnActionListener);

        add(roomNameLabel);
        add(roomAmountLabel);
        add(roomDescriptionLabel);
        add(roomPrivateLabel);
        add(roomPasswordLabel);
        add(roomNameTextField);
        add(roomAmountTextField);
        add(roomDescriptionTextField);
        add(roomPrivateChkBox);
        add(roomPasswordPwdField);
        add(createRoomBtn);
    }


    public ActionListener createRoomBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            createRoomObserver.OnClickedCreateBtn(
                    roomNameTextField.getText(),
                    roomAmountTextField.getText(),
                    roomDescriptionTextField.getText(),
                    roomPrivateChkBox.isSelected(),
                    String.valueOf(roomPasswordPwdField.getPassword()));
        }
    };


}
