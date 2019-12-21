package com.whoisspy.client.game;

import com.whoisspy.ImageExtensions;
import com.whoisspy.ImagePanel;
import com.whoisspy.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ProfilePanel extends JPanel {

    private JLabel accountLabel;
    private JLabel emailLabel;
    private JLabel accountTextLabel;
    private JTextField emailTextField;
    private JLabel passwordLabel;
    private JButton changePasswordBtn;
    private JPanel profilePhotoPanel;
    private JButton changeProfilePhotoBtn;
    private JButton saveProfileBtn;

    private String fontName = "微軟正黑體";
    private Font defaultFont = new Font(fontName, Font.BOLD, 16);
    private Font labelFont = new Font(fontName, Font.BOLD, 25);
    private Font txtFont = new Font(fontName, Font.BOLD, 18);

    private ProfilePanelObserver profilePanelObserver;
    private User user;

    private boolean photoIsEdit = false;
    private boolean emailIsEdit = false;

    public ProfilePanel(ProfilePanelObserver profilePanelObserver, User user) {
        super();
        this.profilePanelObserver = profilePanelObserver;
        this.user = user;
        setLayout(null);
        setBackground(Color.BLACK);

        accountLabel = new JLabel("帳號：");
        accountLabel.setLocation(250, 80);
        accountLabel.setSize(85, 30);
        accountLabel.setFont(labelFont);
        accountLabel.setForeground(Color.WHITE);

        emailLabel = new JLabel("信箱：");
        emailLabel.setLocation(250, 120);
        emailLabel.setSize(85, 30);
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(Color.WHITE);

        passwordLabel = new JLabel("密碼：");
        passwordLabel.setLocation(250, 160);
        passwordLabel.setSize(85, 30);
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.WHITE);

        accountTextLabel = new JLabel(user.getAccount());
        accountTextLabel.setLocation(350, 80);
        accountTextLabel.setSize(125, 30);
        accountTextLabel.setFont(labelFont);
        accountTextLabel.setForeground(Color.WHITE);

        emailTextField = new JTextField(user.getEmail());
        emailTextField.setLocation(350, 120);
        emailTextField.setSize(280, 30);
        emailTextField.setFont(txtFont);
        emailTextField.getDocument().addDocumentListener(emailTextFieldDocumentListener);

        changePasswordBtn = new JButton("修改密碼");
        changePasswordBtn.setLocation(350, 160);
        changePasswordBtn.setSize(280, 30);
        changePasswordBtn.setFont(defaultFont);
        changePasswordBtn.addActionListener(changePasswordBtnActionListener);

        profilePhotoPanel = new JPanel();
        profilePhotoPanel.setLocation(45, 80);
        profilePhotoPanel.setSize(150, 200);
        profilePhotoPanel.setLayout(null);
        Image photo = user.getPhoto();
        if (photo != null) {
            ImagePanel imagePanel = ImageExtensions.scaleImage(photo, profilePhotoPanel);
            profilePhotoPanel.add(imagePanel);
        }

        changeProfilePhotoBtn = new JButton("選擇照片");
        changeProfilePhotoBtn.setLocation(65, 300);
        changeProfilePhotoBtn.setSize(110, 30);
        changeProfilePhotoBtn.setFont(defaultFont);
        changeProfilePhotoBtn.addActionListener(changeProfilePhotoActionListener);

        saveProfileBtn = new JButton("儲存修改");
        saveProfileBtn.setLocation(440, 280);
        saveProfileBtn.setSize(190, 50);
        saveProfileBtn.setFont(defaultFont);
        saveProfileBtn.setEnabled(false);
        saveProfileBtn.addActionListener(saveProfileBtnActionListener);

        add(accountLabel);
        add(accountTextLabel);
        add(emailLabel);
        add(emailTextField);
        add(passwordLabel);
        add(changePasswordBtn);
        add(profilePhotoPanel);
        add(changeProfilePhotoBtn);
        add(saveProfileBtn);
    }

    private String imageFileName = "";
    private ActionListener changeProfilePhotoActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser photoFileChooser = new JFileChooser();
            photoFileChooser.setMultiSelectionEnabled(false);
            FileFilter filter = new FileNameExtensionFilter("受支援的影像格式", ImageIO.getReaderFileSuffixes());
            photoFileChooser.setFileFilter(filter);
            photoFileChooser.setCurrentDirectory(new File
                    (System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures"));
            int returnValue = photoFileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {

                File selectedPhoto = photoFileChooser.getSelectedFile();
                imageFileName = selectedPhoto.getAbsolutePath();

                ImagePanel imagePanel = ImageExtensions.scaleImage(selectedPhoto, profilePhotoPanel);

                profilePhotoPanel.removeAll();
                profilePhotoPanel.add(imagePanel);
                profilePhotoPanel.revalidate();

                valueChange("photo");
            }
        }
    };

    public DocumentListener emailTextFieldDocumentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            valueChange("email");
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            valueChange("email");
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            valueChange("email");
        }
    };

    public void valueChange(String changedValue) {
        if (changedValue.equals("email")) {
            if (emailTextField.getText().equals(user.getEmail())) {
                emailIsEdit = false;
            } else {
                emailIsEdit = true;
            }
        } else if (changedValue.equals("photo")) {
            photoIsEdit = true;
        }

        if (emailIsEdit || photoIsEdit) {
            saveProfileBtn.setEnabled(true);
        } else {
            saveProfileBtn.setEnabled(false);
        }
    }

    public ActionListener changePasswordBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            profilePanelObserver.OnClickedModifyPasswordBtn();
        }
    };

    public ActionListener saveProfileBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (photoIsEdit) {
                profilePanelObserver.OnClickedSaveBtn(emailTextField.getText(),
                        ImageExtensions.ImageToBase64(imageFileName));
            } else {
                profilePanelObserver.OnClickedSaveBtn(emailTextField.getText(),
                        ImageExtensions.ImageToBase64(user.getPhoto()));
            }

        }
    };

}
