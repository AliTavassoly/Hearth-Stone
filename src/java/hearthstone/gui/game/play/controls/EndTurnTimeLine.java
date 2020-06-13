package hearthstone.gui.game.play.controls;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndTurnTimeLine extends JPanel {
    private int width, height;

    public EndTurnTimeLine(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;

        try {
            String path;
            path = "/images/fire.png";
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.drawImage(image.getScaledInstance(
                width, height,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height,
                null);
    }
}
