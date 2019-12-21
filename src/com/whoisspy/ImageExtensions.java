package com.whoisspy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ImageExtensions {

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

    public static String ImageToBase64(String imageFileName) {
        if (!imageFileName.equals("")) {
            try {
                BufferedImage bImage = ImageIO.read(new File(imageFileName));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos );
                byte[] data = bos.toByteArray();
                return new String(Base64.getEncoder().encode(data), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static Image base64StringToImage(String base64String) {
        try {
            byte[] bytes1 = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImagePanel scaleImage(File selectedFile, JPanel destPanel) {

        try {
            String imageFileName = selectedFile.getAbsolutePath();
            Image selectedImage = ImageIO.read(new File(imageFileName));
            Dimension imageSize = new Dimension();
            imageSize.setSize(selectedImage.getWidth(null), selectedImage.getHeight(null));
            Dimension panelSize = destPanel.getSize();
            Dimension scaleSize = getScaledDimension(imageSize, destPanel.getSize());

            selectedImage = selectedImage.getScaledInstance(scaleSize.width, scaleSize.height, Image.SCALE_SMOOTH);

            ImagePanel imagePanel = new ImagePanel(selectedImage);
            imagePanel.setLocation(0, panelSize.height / 2 - scaleSize.height / 2);
            imagePanel.setSize(scaleSize);

            return imagePanel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
