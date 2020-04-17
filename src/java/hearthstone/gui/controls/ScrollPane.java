package hearthstone.gui.controls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ScrollPane extends JScrollPane {

    public ScrollPane(JPanel panel){
        super(panel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/back.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        //g.drawImage(image, 0, 0, null);
        int width = (int)this.getSize().getWidth();
        int height = (int)this.getSize().getHeight();

        System.out.println(width + " " + height);
        g.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
    }
}
