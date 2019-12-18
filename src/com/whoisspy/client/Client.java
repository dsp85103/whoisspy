package com.whoisspy.client;

import com.whoisspy.*;
import com.whoisspy.JPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client {

    private InitFrame initFrame;
    private JButton loginBtn;
    private JButton signUpBtn;
    private JButton aboutAppBtn;
    private String title;
    private String version;
    private String fontName = "微軟正黑體";

    public Client(String title, String version) {

        initFrame = new InitFrame("誰是臥底", "0.0.1");

        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(null);
        bodyPanel.setBackground(Color.BLACK);

        loginBtn = new JButton("登入");
        loginBtn.setFont(new Font(fontName, Font.BOLD, 18));
        loginBtn.setLocation(235,100);
        loginBtn.setSize(175,40);
        loginBtn.addActionListener(loginActionListener);

        signUpBtn = new JButton("免費註冊");
        signUpBtn.setFont(new Font(fontName, Font.BOLD, 18));
        signUpBtn.setLocation(235,150);
        signUpBtn.setSize(175,40);
        signUpBtn.addActionListener(signUpActionListener);

        aboutAppBtn = new JButton("關於我們");
        aboutAppBtn.setFont(new Font(fontName, Font.BOLD, 18));
        aboutAppBtn.setLocation(235,200);
        aboutAppBtn.setSize(175,40);
        aboutAppBtn.addActionListener(aboutAppActionListener);

        bodyPanel.add(loginBtn);
        bodyPanel.add(signUpBtn);
        bodyPanel.add(aboutAppBtn);
        initFrame.setContentBodyPanel(bodyPanel);
    }

    public ActionListener loginActionListener = e -> {

    };

    public ActionListener signUpActionListener = e -> {

    };

    public ActionListener aboutAppActionListener = e -> {

    };

    public void Start() {
        initFrame.setVisible(true);
    }
}
