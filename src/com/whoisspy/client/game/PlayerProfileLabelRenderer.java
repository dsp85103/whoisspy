package com.whoisspy.client.game;

import com.whoisspy.ImageExtensions;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.EventObject;

public class PlayerProfileLabelRenderer extends JLabel implements TableCellRenderer {

    private PlayerProfile playerProfile;
    private JButton playerProfileBtn;
    private Image playerPhoto;
    private Dimension scaleDimension = new Dimension(60, 60);

    public PlayerProfileLabelRenderer() {
        setOpaque(true);
        super.setHorizontalAlignment(JLabel.CENTER);
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public ActionListener playerProfileBtnActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //不能將email等資訊送出來 不然會可以強制改密碼
            System.out.println("you clicked the account profile button");
        }
    };

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        playerProfile =  (PlayerProfile) value;
        setText(playerProfile.getAccount());
        Image profilePhoto = ImageExtensions.base64StringToImage(playerProfile.getPhotoBase64());
        if (profilePhoto != null) {
            playerPhoto = ImageExtensions.scaleImage(profilePhoto, scaleDimension);
            setIcon(new ImageIcon(ImageExtensions.getCircularBufferedImage(scaleDimension, playerPhoto)));
        }
        setFont(new Font("微軟正黑體", Font.BOLD, 15));

        return this;
    }
}
