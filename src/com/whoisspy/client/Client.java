package com.whoisspy.client;

import com.whoisspy.*;
import com.whoisspy.JPanel;

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
    private JButton loginBtn;
    private JButton signUpBtn;
    private JButton aboutAppBtn;
    private String title;
    private String version;
    private String fontName = "微軟正黑體";

    public Client(String title, String version) {

        initFrame = new InitFrame(title, version);
        this.title = title;
        this.version = version;

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

    public void Start() {
        initFrame.setVisible(true);
    }
}
