package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.SizeConfigs;
import hearthstone.logic.models.hero.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardHeroButton extends ImageButton {
    private Hero hero;
    int width, height;

    public BoardHeroButton(Hero hero, int width, int height) {
        this.hero = hero;
        this.height = height;
        this.width = width;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage heroImage = null;
        BufferedImage healthBackground = null;

        try {
            heroImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/heroes/normal_heroes/" +
                            hero.getName().toLowerCase().replace(' ', '_') + ".png"));

            healthBackground = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/health_background.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g2.drawImage(heroImage.getScaledInstance(width,
                height,
                Image.SCALE_SMOOTH), 0, 0,
                width ,
                height,
                null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 15);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW HEALTH
        font = CredentialsFrame.getInstance().getCustomFont(0, 20);
        g2.setFont(font);
        String health = String.valueOf(hero.getHealth());
        g2.setColor(Color.WHITE);
        int midWidth = width - 25;
        g2.drawImage(healthBackground.getScaledInstance(
                SizeConfigs.healthWidth, SizeConfigs.healthHeight,
                Image.SCALE_SMOOTH),
                midWidth - SizeConfigs.healthWidth / 2 + 5, height - 50,
                SizeConfigs.healthWidth, SizeConfigs.healthHeight,
                null);

        g2.drawString(health, midWidth - fontMetrics.stringWidth(health) / 2 + 5, height - 17);

    }
}
