package hearthstone.gui.controls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private int startX, startY, width, height;
    private String imagePath;

    private boolean rtl;

    public ImagePanel(String imagePath, int width, int height){
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
    }

    public ImagePanel(String imagePath, int width, int height, int startX, int startY, boolean rtl){
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
        this.rtl = rtl;
        this.startX = startX;
        this.startY = startY;

        setPreferredSize(new Dimension(width, height));
    }

    public String getImagePath(){
        return imagePath;
    }
    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
        this.repaint();
        this.revalidate();
    }

    public boolean isRtl() {
        return rtl;
    }
    public void setRtl(boolean rtl) {
        this.rtl = rtl;
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
        if(rtl) {
            g2.drawImage(image.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH),
                    startX - (int)getBounds().getX(), 0, width, height,
                    null);
        } else {
            g2.drawImage(image.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH),
                    0, 0, width, height,
                    null);
        }
    }
}
