package com.whoisspy.client;

import com.whoisspy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    private InitFrame initFrame;

    private String title;
    private String version;
    private String fontName = "微軟正黑體";

    public Client(String title, String version) {

        initFrame = new InitFrame(title, version);
        this.title = title;
        this.version = version;

        JButton goHomeBtn = new JButton("首頁");
        goHomeBtn.setLocation(320,30);
        goHomeBtn.setSize(80,35);
        goHomeBtn.addActionListener(goHomeBtnActionListener);
        initFrame.add(goHomeBtn);

        setupHomePanel();
    }

    public void setupHomePanel() {
        HomePanel homePanel = new HomePanel(homeLoginBtnActionListener, homeSignUpBtnActionListener, homeAboutAppBtnActionListener);
        initFrame.setContentBodyPanel("誰是臥底", homePanel);
    }

    public ActionListener loginBtnActionListener = e -> {

    };

    public ActionListener sendModifyBtnActionListener = e -> {

    };

    public MouseListener forgotPasswordLabelMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            initFrame.setCaptionText("修改密碼");
            ModifyPasswordPanel ModifyPasswordPanel = new ModifyPasswordPanel(sendModifyBtnActionListener);
            initFrame.setContentBodyPanel("修改密碼", ModifyPasswordPanel);
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

    public MouseListener signUpLabelMouseListener = new MouseListener() {
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
    };

    public ActionListener homeLoginBtnActionListener = e -> {
        LoginPanel loginPanel = new LoginPanel(loginBtnActionListener, forgotPasswordLabelMouseListener, signUpLabelMouseListener);
        initFrame.setContentBodyPanel("歡迎回來", loginPanel);
    };

    public ActionListener homeSignUpBtnActionListener = e -> {
        SignUpPanel signUpPanel = new SignUpPanel();
        initFrame.setContentBodyPanel("免費註冊", signUpPanel);
    };

    public ActionListener homeAboutAppBtnActionListener = e -> {
        String sb = "<html>" +
                "誰是臥底 Who is spy<br>" +
                "Author：B10609011 徐儀翔<br>" +
                "Email：b10609011@gapps.ntust.edu.tw<br>" +
                "Runtime version：" + version + "<br>" +
                "Git：<a href=\"https://github.com/dsp85103/whoisspy\">https://github.com/dsp85103/whoisspy</a><br>" +
                "License：MIT License<br>" +
                "</html>";
        JLabel aboutAppLabel = new JLabel(sb);
        aboutAppLabel.setFont(new Font(fontName, Font.BOLD, 12));
        aboutAppLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/dsp85103/whoisspy"));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Open browser error.");
                }
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

        JOptionPane.showMessageDialog(initFrame, aboutAppLabel, title, JOptionPane.INFORMATION_MESSAGE);
    };

    public ActionListener goHomeBtnActionListener = e -> setupHomePanel();


    public void Start() {
        initFrame.setVisible(true);
    }
}
