package com.whoisspy.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ModifyPasswordPanel extends JPanel {

    private JLabel accountLabel;
    private JLabel emailLabel;
    private JLabel newPasswdLabel;
    private JLabel confirmPasswdLabel;
    private JTextField accountTextField;
    private JTextField emailTextField;
    private JPasswordField newPasswdPwdField;
    private JPasswordField confirmPasswdPwdField;
    private JButton sendModifyBtn;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);
    private Font smallLabelFont = new Font(fontName, Font.BOLD, 14);

    public ModifyPasswordPanel(ActionListener sendModifyBtnActionListener) {
        super();

        setLayout(null);
        setBackground(Color.BLACK);

        JButton goHomeBtn = new JButton("回首頁");
        goHomeBtn.setLocation(410,30);
        goHomeBtn.setSize(80,35);

        accountLabel = new JLabel("帳　　號：");
        accountLabel.setLocation(80,80);
        accountLabel.setSize(125,30);
        accountLabel.setFont(labelFont);
        accountLabel.setForeground(Color.WHITE);

        emailLabel = new JLabel("信　　箱：");
        emailLabel.setLocation(80,120);
        emailLabel.setSize(125,30);
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(Color.WHITE);

        newPasswdLabel = new JLabel("新  密  碼：");
        newPasswdLabel.setLocation(80,160);
        newPasswdLabel.setSize(125,30);
        newPasswdLabel.setFont(labelFont);
        newPasswdLabel.setForeground(Color.WHITE);

        confirmPasswdLabel = new JLabel("確認密碼：");
        confirmPasswdLabel.setLocation(80,200);
        confirmPasswdLabel.setSize(125,30);
        confirmPasswdLabel.setFont(labelFont);
        confirmPasswdLabel.setForeground(Color.WHITE);

        accountTextField = new JTextField();
        accountTextField.setLocation(220,80);
        accountTextField.setSize(280,30);
        accountTextField.setFont(defaultFont);
        accountTextField.setHorizontalAlignment(JTextField.CENTER);

        emailTextField = new JTextField();
        emailTextField.setLocation(220,120);
        emailTextField.setSize(280,30);
        emailTextField.setFont(defaultFont);
        emailTextField.setHorizontalAlignment(JTextField.CENTER);

        newPasswdPwdField = new JPasswordField();
        newPasswdPwdField.setLocation(220,160);
        newPasswdPwdField.setSize(280,30);
        newPasswdPwdField.setFont(defaultFont);
        newPasswdPwdField.setHorizontalAlignment(JPasswordField.CENTER);
        newPasswdPwdField.setEchoChar('●');

        confirmPasswdPwdField = new JPasswordField();
        confirmPasswdPwdField.setLocation(220,200);
        confirmPasswdPwdField.setSize(280,30);
        confirmPasswdPwdField.setFont(defaultFont);
        confirmPasswdPwdField.setHorizontalAlignment(JPasswordField.CENTER);
        confirmPasswdPwdField.setEchoChar('●');

        sendModifyBtn = new JButton("送出修改");
        sendModifyBtn.setLocation(320,265);
        sendModifyBtn.setSize(180,50);
        sendModifyBtn.setFont(labelFont);
        sendModifyBtn.addActionListener(sendModifyBtnActionListener);


        add(accountLabel);
        add(emailLabel);
        add(newPasswdLabel);
        add(confirmPasswdLabel);
        add(accountTextField);
        add(emailTextField);
        add(newPasswdPwdField);
        add(confirmPasswdPwdField);
        add(sendModifyBtn);
    }
}
