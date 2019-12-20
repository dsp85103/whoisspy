package com.whoisspy.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.whoisspy.ImagePanel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SignUpPanel extends JPanel {

    private JLabel accountLabel;
    private JLabel emailLabel;
    private JLabel newPasswdLabel;
    private JLabel confirmPasswdLabel;
    private JTextField accountTextField;
    private JTextField emailTextField;
    private JPasswordField newPasswdPwdField;
    private JPasswordField confirmPasswdPwdField;
    private JPanel profilePhotoPanel;
    private JButton chooseProfilePhotoBtn;
    private JButton signUpBtn;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);
    private Font smallLabelFont = new Font(fontName, Font.BOLD, 14);


    public SignUpPanel() {
        super();
        setLayout(null);
        setBackground(Color.BLACK);

        accountLabel = new JLabel("帳　　號：");
        accountLabel.setLocation(220,80);
        accountLabel.setSize(125,30);
        accountLabel.setFont(labelFont);
        accountLabel.setForeground(Color.WHITE);

        emailLabel = new JLabel("信　　箱：");
        emailLabel.setLocation(220,120);
        emailLabel.setSize(125,30);
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(Color.WHITE);

        newPasswdLabel = new JLabel("新  密  碼：");
        newPasswdLabel.setLocation(220,160);
        newPasswdLabel.setSize(125,30);
        newPasswdLabel.setFont(labelFont);
        newPasswdLabel.setForeground(Color.WHITE);

        confirmPasswdLabel = new JLabel("確認密碼：");
        confirmPasswdLabel.setLocation(220,200);
        confirmPasswdLabel.setSize(125,30);
        confirmPasswdLabel.setFont(labelFont);
        confirmPasswdLabel.setForeground(Color.WHITE);

        accountTextField = new JTextField();
        accountTextField.setLocation(360,80);
        accountTextField.setSize(250,30);
        accountTextField.setFont(defaultFont);
        accountTextField.setHorizontalAlignment(JTextField.LEFT);

        emailTextField = new JTextField();
        emailTextField.setLocation(360,120);
        emailTextField.setSize(250,30);
        emailTextField.setFont(defaultFont);
        emailTextField.setHorizontalAlignment(JTextField.LEFT);

        newPasswdPwdField = new JPasswordField();
        newPasswdPwdField.setLocation(360,160);
        newPasswdPwdField.setSize(250,30);
        newPasswdPwdField.setFont(defaultFont);
        newPasswdPwdField.setHorizontalAlignment(JPasswordField.LEFT);
        newPasswdPwdField.setEchoChar('●');

        confirmPasswdPwdField = new JPasswordField();
        confirmPasswdPwdField.setLocation(360,200);
        confirmPasswdPwdField.setSize(250,30);
        confirmPasswdPwdField.setFont(defaultFont);
        confirmPasswdPwdField.setHorizontalAlignment(JPasswordField.LEFT);
        confirmPasswdPwdField.setEchoChar('●');

        profilePhotoPanel = new JPanel();
        profilePhotoPanel.setLocation(45,80);
        profilePhotoPanel.setSize(150,200);
        profilePhotoPanel.setLayout(null);
//        profilePhotoPanel.setBorder(BorderFactory.createEtchedBorder());

        chooseProfilePhotoBtn = new JButton("選擇照片");
        chooseProfilePhotoBtn.setLocation(65,300);
        chooseProfilePhotoBtn.setSize(110,30);
        chooseProfilePhotoBtn.setFont(defaultFont);
        chooseProfilePhotoBtn.addActionListener(chooseProfilePhotoActionListener);

        signUpBtn = new JButton("免費註冊");
        signUpBtn.setLocation(440,280);
        signUpBtn.setSize(170, 50);
        signUpBtn.setFont(defaultFont);

        add(accountLabel);
        add(emailLabel);
        add(newPasswdLabel);
        add(confirmPasswdLabel);
        add(accountTextField);
        add(emailTextField);
        add(newPasswdPwdField);
        add(confirmPasswdPwdField);
        add(profilePhotoPanel);
        add(chooseProfilePhotoBtn);
        add(signUpBtn);
    }

    public ActionListener chooseProfilePhotoActionListener = e -> {

        JFileChooser photoFileChooser = new JFileChooser();
        photoFileChooser.setMultiSelectionEnabled(false);
        FileFilter filter = new FileNameExtensionFilter("受支援的影像格式", ImageIO.getReaderFileSuffixes());
        photoFileChooser.setFileFilter(filter);
        photoFileChooser.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator")+ "Pictures"));
        int returnValue = photoFileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
            File selectedPhoto = photoFileChooser.getSelectedFile();
            try {
                Image selectedImage = ImageIO.read(new File(selectedPhoto.getAbsolutePath()));
                Dimension imageSize = new Dimension();
                imageSize.setSize(selectedImage.getWidth(null), selectedImage.getHeight(null));
                Dimension panelSize = profilePhotoPanel.getSize();
                Dimension scaleSize = getScaledDimension(imageSize, panelSize);

                selectedImage = selectedImage.getScaledInstance(scaleSize.width, scaleSize.height, Image.SCALE_SMOOTH);

                ImagePanel imagePanel = new ImagePanel(selectedImage);
                imagePanel.setLocation(0, panelSize.height/2 - scaleSize.height/2);
                imagePanel.setSize(scaleSize);
                profilePhotoPanel.removeAll();
                profilePhotoPanel.add(imagePanel);
                profilePhotoPanel.revalidate();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
//            System.out.println(selectedPhoto.getAbsolutePath());
        }


    };

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

}
