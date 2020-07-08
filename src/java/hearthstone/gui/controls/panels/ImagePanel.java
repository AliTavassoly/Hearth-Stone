package hearthstone.gui.controls.panels;

import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private int startX, startY;
    private int width, height;
    private String imagePath;
    private boolean isRTL;

    private BufferedImage image;

    public ImagePanel(String imagePath, int width, int height) {
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
    }

    public ImagePanel(String imagePath, int width, int height, int startX, int startY, boolean isRTL) {
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;

        this.startX = startX;
        this.startY = startY;

        this.isRTL = isRTL;

        setPreferredSize(new Dimension(width, height));
    }

    public String getImagePath() {
        return imagePath;
    }

    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;
        try {
            if (image == null)
                image = ImageResource.getInstance().getImage("/images/" + imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isRTL) {
            g2.drawImage(image.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH),
                    startX - (int) getBounds().getX(), 0, width, height,
                    null);
        } else {
            g2.drawImage(image.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH),
                    0, 0, width, height,
                    null);
        }
    }

    public BufferedImage getImage(){
        return image;
    }

    public boolean isRTL(){
        return isRTL;
    }
}
