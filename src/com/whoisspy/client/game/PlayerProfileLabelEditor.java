package com.whoisspy.client.game;

import com.whoisspy.ImageExtensions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayerProfileLabelEditor extends DefaultCellEditor  {

    private JLabel playerLabel;
    private Image playerPhoto;
    private PlayerProfile playerProfile;
    private Dimension scaleDimension = new Dimension(60, 60);

    public PlayerProfileLabelEditor(JTextField txt) {
        super(txt);
        playerLabel = new JLabel();
        playerLabel.setOpaque(true);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        playerProfile =  (PlayerProfile) value;
        playerLabel.setText(playerProfile.getAccount());
        Image profilePhoto = ImageExtensions.base64StringToImage(playerProfile.getPhotoBase64());
        if (profilePhoto != null) {
            playerPhoto = ImageExtensions.scaleImage(profilePhoto, scaleDimension);
            playerLabel.setIcon(new ImageIcon(ImageExtensions.getCircularBufferedImage(scaleDimension, playerPhoto)));
        }

        return playerLabel;
    }

    @Override
    public Object getCellEditorValue() {
        System.out.println("getCellEditorValue() called");
        return new String("test");
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }


}
