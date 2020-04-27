package hearthstone.gui.controls.hero;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.hero.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroButton extends ImageButton {
    private Hero hero;
    int width, height;

    public HeroButton(Hero hero, int width, int height) {
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
        BufferedImage detailsImage = null;

        try {
            heroImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/heroes/normal_heroes/" + hero.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + ".png"));

            detailsImage = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/heroes/normal_heroes/" + "hero_details" + ".png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g2.drawImage(heroImage.getScaledInstance(width - 20,
                height - SizeConfigs.bigHeroDetailHeight,
                Image.SCALE_SMOOTH), 0, 0,
                width - 20, height - SizeConfigs.bigHeroDetailHeight,
                null);

        g2.drawImage(detailsImage.getScaledInstance(SizeConfigs.bigHeroDetailWidth - 4,
                SizeConfigs.bigHeroDetailHeight, Image.SCALE_SMOOTH),
                15, height - SizeConfigs.bigHeroDetailHeight - 50,
                SizeConfigs.bigHeroDetailWidth - 4, SizeConfigs.bigHeroDetailHeight,
                null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 15);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW NAME
        String name = hero.getName();
        g2.setColor(Color.WHITE);
        g2.drawString(name, width / 2 - fontMetrics.stringWidth(name) / 2 - 5, height - 105);

        // DRAW HEALTH
        font = CredentialsFrame.getInstance().getCustomFont(0, 25);
        g2.setFont(font);
        String health = String.valueOf(hero.getHealth());
        g2.setColor(Color.WHITE);
        int midWidth = width - 37;
        g2.drawString(health, midWidth - fontMetrics.stringWidth(health) / 2, height - 125);
    }
}
