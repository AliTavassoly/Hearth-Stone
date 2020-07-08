package hearthstone.gui.controls.buttons;

import hearthstone.logic.models.passive.Passive;
import hearthstone.util.getresource.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PassiveButton extends JButton {
    private final int width, height;
    private final Passive passive;

    private BufferedImage image;

    public PassiveButton(Passive passive, int width, int height) {
        this.passive = passive;
        this.width = width;
        this.height = height;

        configButton();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            if(image == null) {
                image = ImageResource.getInstance().getImage(
                        "/images/passives/" +
                                passive.getName().toLowerCase().
                                        replace(' ', '_').
                                        replace("'", "")
                                + ".png");
            }
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
