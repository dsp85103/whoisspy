package com.whoisspy;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Iterator;

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

    public static String getImageType(Image image) {
        try {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(image);

            while (imageReaders.hasNext()) {
                ImageReader reader = (ImageReader) imageReaders.next();

                System.out.println(reader.getFormatName());
                return reader.getFormatName();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "bmp";
    }

    // image file name --> base64
    public static String ImageToBase64(String imageFileName) {

        if (!imageFileName.equals("")) {
            try {
                File imageFile = new File(imageFileName);
                byte[] data = Files.readAllBytes(imageFile.toPath());
                return Base64.getEncoder().encodeToString(data);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    // image --> base64
    public static String ImageToBase64(Image image) {

        try {
            if (image != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BufferedImage bImage = toBufferedImage(image);
                ImageIO.write(bImage, getImageType(image), bos);
                byte[] imageBytes = bos.toByteArray();
                return new String(Base64.getEncoder().encode(imageBytes), StandardCharsets.UTF_8);
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // base64 string --> Image
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

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage getCircularBufferedImage(Dimension scaleDimension, Image selectedPhoto) {
        int width = scaleDimension.width;
        int height = scaleDimension.height;
        BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, width, height));
        g2.drawImage(selectedPhoto, 0, 0, width, height, null);
        return circleBuffer;
    }

//    public static Image toImage(BufferedImage bufferedImage) {
//
//    }

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

    public static ImagePanel scaleImage(Image selectedImage, JPanel destPanel) {

        Dimension imageSize = new Dimension();
        imageSize.setSize(selectedImage.getWidth(null), selectedImage.getHeight(null));
        Dimension panelSize = destPanel.getSize();
        Dimension scaleSize = getScaledDimension(imageSize, destPanel.getSize());

        selectedImage = selectedImage.getScaledInstance(scaleSize.width, scaleSize.height, Image.SCALE_SMOOTH);

        ImagePanel imagePanel = new ImagePanel(selectedImage);
        imagePanel.setLocation(0, panelSize.height / 2 - scaleSize.height / 2);
        imagePanel.setSize(scaleSize);

        return imagePanel;
    }

    public static Image scaleImage(Image selectedImage, Dimension destDimension) {

        Dimension imageSize = new Dimension();
        imageSize.setSize(selectedImage.getWidth(null), selectedImage.getHeight(null));
        Dimension scaleSize = getScaledDimension(imageSize, destDimension);

        return selectedImage.getScaledInstance(scaleSize.width, scaleSize.height, Image.SCALE_SMOOTH);
    }

}
