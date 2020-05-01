package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private String imagePath;
    private int width, height;

    public ImagePanel(String imagePath, int width, int height){
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
    }

    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/" + imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
    }
}
