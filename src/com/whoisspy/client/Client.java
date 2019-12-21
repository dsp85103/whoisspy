package com.whoisspy.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.whoisspy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    private InitFrame initFrame;

    private String tag = "Client";
    private String connectionName = "unknown";
    private String title;
    private String version;
    private String fontName = "微軟正黑體";
    private Font messageFont = new Font(fontName, Font.PLAIN, 15);

    private SocketClient socketClient;
    private Gson gson = new Gson();
    private Logger logger;
    private User user;
    private boolean isLogin = false;

    public Client(String title, String version) {

        initFrame = new InitFrame(title, version);
        this.title = title;
        this.version = version;

        logger = new Logger(tag, connectionName);

        JButton goHomeBtn = new JButton("首頁");
        goHomeBtn.setLocation(320, 30);
        goHomeBtn.setSize(80, 35);
        goHomeBtn.addActionListener(goHomeBtnActionListener);
        initFrame.add(goHomeBtn);

        setupHomePanel();

        socketClient = new SocketClient("127.0.0.1", 15566, clientMessageObserver);

    }

    public void setupHomePanel() {
        HomePanel homePanel = new HomePanel(homePanelObserver);
        initFrame.setContentBodyPanel("誰是臥底", homePanel);
    }

    private void changeLoginStatus(boolean isLogin, String connName, User user) {
        connectionName = connName;
        logger.setAccount(connName);
        this.isLogin = isLogin;
        this.user= user;
        if (isLogin) {
            logger.log("change connection status to login");
        } else {
            logger.log("change connection status to logout");
        }
    }

    private void ShowMessageBox(String msg, int messageType) {

        JLabel msgLabel = new JLabel(msg);
        msgLabel.setFont(messageFont);
        JOptionPane.showMessageDialog(initFrame, msgLabel, title, messageType);

    }

    public MessageObserver clientMessageObserver = new MessageObserver() {
        @Override
        public void OnMessage(String receivedData) {
            logger.log(String.format("Receiving message '%s'", receivedData));
            Message message = gson.fromJson(receivedData, Message.class);
            JsonObject data = gson.fromJson(message.data, JsonObject.class);

            Message.OP op = message.getOp();

            switch (op) {

                case login:

                    if (message.getStatus().equals(Message.Status.success))
                    {

                        logger.log(String.format("%s login successful", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.INFORMATION_MESSAGE);

                        user = new User(data.get("account").getAsString(),
                                data.get("email").getAsString(),
                                data.get("photo").getAsString()
                        );

                        //登入成功 修改狀態
                        changeLoginStatus(true, user.getAccount(), user);

                    }
                    else if (message.getStatus().equals(Message.Status.failure))
                    {

                        logger.log(String.format("%s login failure", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.ERROR_MESSAGE);

                    }
                    break;

                case modifypwd:

                    if (message.getStatus().equals(Message.Status.success))
                    {

                        logger.log(String.format("%s modify password successful", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.INFORMATION_MESSAGE);

                        //修改成功 進行登出
                        if (user != null) {
                            logout();
                        }

                    }
                    else if (message.getStatus().equals(Message.Status.failure))
                    {

                        logger.log("modify password failure");
                        ShowMessageBox(message.msg, JOptionPane.ERROR_MESSAGE);
                    }

                    break;

                case signup:

                    if (message.getStatus().equals(Message.Status.success))
                    {

                        logger.log(String.format("%s sign up successful", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.INFORMATION_MESSAGE);

                        // 註冊成功 修改為登入狀態
                        user = new User(data.get("account").getAsString(),
                                data.get("email").getAsString(),
                                data.get("photo").getAsString()
                        );
                        changeLoginStatus(true, user.getAccount(), user);

                    }
                    else if (message.getStatus().equals(Message.Status.failure))
                    {

                        logger.log(String.format("%s sign up failure", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.WARNING_MESSAGE);

                    }

                    break;

                case logout:

                    if (message.getStatus().equals(Message.Status.success))
                    {

                        logger.log(String.format("%s logout successful", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.INFORMATION_MESSAGE);

                        //登出成功 修改為登出狀態
                        changeLoginStatus(false, "unknown", null);

                    }
                    else if (message.getStatus().equals(Message.Status.failure))
                    {

                        logger.log(String.format("%s logout failure", data.get("account").getAsString()));
                        ShowMessageBox(message.msg, JOptionPane.WARNING_MESSAGE);

                    }
                    break;

            }
        }
    };

    public HomePanelObserver homePanelObserver = new HomePanelObserver() {
        @Override
        public void OnClickedLoginBtn() {
            LoginPanel loginPanel = new LoginPanel(loginPanelObserver);
            initFrame.setContentBodyPanel("歡迎回來", loginPanel);
        }

        @Override
        public void OnClickedSignUpBtn() {
            SignUpPanel signUpPanel = new SignUpPanel(signUpPanelObserver);
            initFrame.setContentBodyPanel("免費註冊", signUpPanel);
        }

        @Override
        public void OnClickedAboutAppBtn() {
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
        }
    };

    public LoginPanelObserver loginPanelObserver = new LoginPanelObserver() {
        @Override
        public void OnClickedLoginBtn(String account, String password) {
            JsonObject data = new JsonObject();
            data.addProperty("account", account);
            data.addProperty("password", password);
            Message message = new Message(Message.OP.login, Message.Status.process, "login", data.toString());

            socketClient.send(message);
        }

        @Override
        public void OnClickedForgotPasswordLabel() {
            initFrame.setCaptionText("修改密碼");
            ModifyPasswordPanel ModifyPasswordPanel = new ModifyPasswordPanel(modifyPasswordPanelObserver);
            initFrame.setContentBodyPanel("修改密碼", ModifyPasswordPanel);
        }

        @Override
        public void OnClickedSignUpLabel() {
            SignUpPanel signUpPanel = new SignUpPanel(signUpPanelObserver);
            initFrame.setContentBodyPanel("免費註冊", signUpPanel);
        }
    };

    public SignUpPanelObserver signUpPanelObserver = new SignUpPanelObserver() {
        @Override
        public void OnClickedSignUpBtn(String account, String password, String email, String photoBase64) {
            JsonObject data = new JsonObject();
            data.addProperty("account", account);
            data.addProperty("password", password);
            data.addProperty("email", email);
            data.addProperty("photo", photoBase64);
            Message message = new Message(Message.OP.signup, Message.Status.process, "signup", data.toString());

            socketClient.send(message);
        }
    };

    public ModifyPasswordPanelObserver modifyPasswordPanelObserver = (account, email, newPwd, confirmPwd) -> {

        if (confirmPwd.equals(newPwd)) {

            JsonObject data = new JsonObject();
            data.addProperty("account", account);
            data.addProperty("email", email);
            data.addProperty("newPwd", newPwd);
            Message message = new Message(Message.OP.modifypwd, Message.Status.process, "modifypwd", data.toString());

            socketClient.send(message);
        } else {

            ShowMessageBox("兩次密碼輸入不一致！", JOptionPane.WARNING_MESSAGE);

        }

    };

    public ActionListener goHomeBtnActionListener = e -> setupHomePanel();

    public void Start() {
        initFrame.setVisible(true);
    }

    public void logout() {
        JsonObject data = new JsonObject();
        data.addProperty("account", user.getAccount());
        Message message = new Message(Message.OP.logout,
                Message.Status.process,
                "logout",
                data.toString());

        socketClient.send(message);
    }
}
