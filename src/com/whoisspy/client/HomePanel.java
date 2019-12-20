package com.whoisspy.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {

    private JButton loginBtn;
    private JButton signUpBtn;
    private JButton aboutAppBtn;

    private String fontName = "微軟正黑體";

    private HomePanelObserver homePanelObserver;
    public HomePanel(HomePanelObserver homePanelObserver) {
        super();
        this.homePanelObserver = homePanelObserver;
        setLayout(null);
        setBackground(Color.BLACK);

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

        add(loginBtn);
        add(signUpBtn);
        add(aboutAppBtn);
    }


    public ActionListener loginActionListener = e -> {
        homePanelObserver.OnClickedLoginBtn();
    };

    public ActionListener signUpActionListener = e -> {
        homePanelObserver.OnClickedSignUpBtn();
    };

    public ActionListener aboutAppActionListener = e -> {
        homePanelObserver.OnClickedAboutAppBtn();
    };
}
