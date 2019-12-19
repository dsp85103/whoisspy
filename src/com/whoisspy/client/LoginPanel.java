package com.whoisspy.client;

import com.whoisspy.InitFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginPanel extends JPanel {

    private JLabel accountLabel;
    private JLabel passwordLabel;
    private JTextField accountTextField;
    private JPasswordField passwordPasswdField;
    private JButton loginBtn;
    private JLabel forgotPasswordLabel;
    private JLabel signUpLabel;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);

    private InitFrame mainFrame;

    public LoginPanel(InitFrame initFrame) {
        super();
        mainFrame = initFrame;
        setLayout(null);
        setBackground(Color.BLACK);

        accountLabel = new JLabel("帳號：");
        accountLabel.setLocation(130,80);
        accountLabel.setSize(75,25);
        accountLabel.setFont(labelFont);
        accountLabel.setForeground(Color.WHITE);

        passwordLabel = new JLabel("密碼：");
        passwordLabel.setLocation(130,120);
        passwordLabel.setSize(75,25);
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.WHITE);

        accountTextField = new JTextField();
        accountTextField.setLocation(220,80);
        accountTextField.setSize(280,30);
        accountTextField.setFont(defaultFont);
        accountTextField.setHorizontalAlignment(JTextField.CENTER);

        passwordPasswdField = new JPasswordField();
        passwordPasswdField.setLocation(220,120);
        passwordPasswdField.setSize(280,30);
        passwordPasswdField.setFont(defaultFont);
        passwordPasswdField.setHorizontalAlignment(JPasswordField.CENTER);
        passwordPasswdField.setEchoChar('●');

        loginBtn = new JButton("登入");
        loginBtn.setLocation(130, 165);
        loginBtn.setSize(370, 50);
        loginBtn.setFont(defaultFont);

        Font smallLabelFont = new Font(fontName, Font.BOLD, 14);
        forgotPasswordLabel = new JLabel("<html><u>忘記密碼</u></html>");
        forgotPasswordLabel.setLocation(130, 220);
        forgotPasswordLabel.setSize(100,30);
        forgotPasswordLabel.setFont(smallLabelFont);
        forgotPasswordLabel.setForeground(Color.WHITE);
        forgotPasswordLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.setCaptionText("修改密碼");
                ModifyPasswordPanel ModifyPasswordPanel = new ModifyPasswordPanel();
                mainFrame.setContentBodyPanel(ModifyPasswordPanel);
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
        });

        signUpLabel = new JLabel("<html><u>點我免費註冊</u></html>");
        signUpLabel.setLocation(410, 220);
        signUpLabel.setSize(100,30);
        signUpLabel.setFont(smallLabelFont);
        signUpLabel.setForeground(Color.WHITE);
        signUpLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
        });

        add(accountLabel);
        add(accountTextField);
        add(passwordLabel);
        add(passwordPasswdField);
        add(loginBtn);
        add(forgotPasswordLabel);
        add(signUpLabel);
    }
}
