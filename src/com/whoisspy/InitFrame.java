package com.whoisspy;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InitFrame extends JFrame {

    private javax.swing.JPanel contentPanel;
    private JLabel contentCaptionLable;
    private String title;
    private String version;

    private GridLayout contentGridLayout;

    public InitFrame(String title, String version) {

        super(title +" ver "+version);

        this.title = title;
        this.version = version;

        // initial background image component
        BufferedImage myImage;
        try {
            myImage = ImageIO.read(new File("./spy_background.png"));

//            this.setSize(myImage.getWidth()+15,myImage.getHeight()+35);
            this.setSize(myImage.getWidth()+5,myImage.getHeight()+25);

            // show on center of screen
            this.setLocationRelativeTo(null);

            // couldn't resize the frame
            this.setResizable(false);

            this.setContentPane(new ImagePanel(myImage));
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            initContentPanel();
            initCaption();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setLocation(410,30);
        contentPanel.setSize(640,540);
//        contentPanel.setBackground(Color.RED);
        BorderLayout contentBorderLayout = new BorderLayout();
        contentPanel.setLayout(contentBorderLayout);

        this.getContentPane().add(contentPanel);
    }

    public void initCaption() {
        contentCaptionLable = new JLabel(title, JLabel.CENTER);
        contentCaptionLable.setFont(new Font("微軟正黑體", Font.BOLD, 74));
        contentCaptionLable.setForeground(Color.WHITE);
        JPanel captionPanel = new JPanel();
        captionPanel.setBackground(Color.BLACK);
        captionPanel.add(contentCaptionLable);
        contentPanel.add(captionPanel, BorderLayout.NORTH);
    }

    private JPanel currentContentBodyPanel;
    public void setContentBodyPanel(String captionText, JPanel bodyPanel) {
        setCaptionText(captionText);
        if (currentContentBodyPanel != null) {
            contentPanel.remove(currentContentBodyPanel);
        }
        contentPanel.add(bodyPanel, BorderLayout.CENTER);
        currentContentBodyPanel = bodyPanel;
        revalidate();
    }

    public void setCaptionText(String captionText) {
        contentCaptionLable.setText(captionText);
//        revalidate();
    }
}
