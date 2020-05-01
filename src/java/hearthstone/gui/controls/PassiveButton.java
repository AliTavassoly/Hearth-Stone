package hearthstone.gui.controls;

import hearthstone.logic.models.Passive;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PassiveButton extends JButton {
    private final int width, height;
    private final Passive passive;

    public PassiveButton(Passive passive, int width, int height) {
        this.passive = passive;
        this.width = width;
        this.height = height;

        configButton();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/passives/" +
                            passive.getName().toLowerCase().
                                    replace(' ', '_').
                                    replace("'", "")
                            + ".png"));

        } catch (Exception e) {
            e.getStackTrace();
        }
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
    }

    private void configButton() {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
    }
}
