package hearthstone.gui.controls.hero;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.hero.Hero;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroButton extends ImageButton {
    private Hero hero;
    int width, height;

    private BufferedImage heroImage;
    private static BufferedImage detailsImage;


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
        try {
            if (heroImage == null)
                heroImage = ImageResource.getInstance().getImage(
                        "/images/heroes/normal_heroes/" + hero.getName().
                                toLowerCase().replace(' ', '_').replace("'", "") + ".png");

            if (detailsImage == null)
                detailsImage = ImageResource.getInstance().getImage(
                        "/images/heroes/normal_heroes/" + "hero_details" + ".png");
        } catch (Exception e) {
            e.printStackTrace();
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

        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT,0, 15);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW NAME
        drawName(g2, fontMetrics);

        // DRAW HEALTH
        drawHealth(g2, fontMetrics);
    }

    private void drawName(Graphics2D g, FontMetrics fontMetrics) {
        g.setColor(Color.WHITE);
        g.drawString(hero.getName(), width / 2 - fontMetrics.stringWidth(hero.getName()) / 2 - 5, height - 105);
    }

    private void drawHealth(Graphics2D g, FontMetrics fontMetrics) {
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT,0, 25);
        g.setFont(font);
        String health = String.valueOf(hero.getHealth());
        g.setColor(Color.WHITE);
        int midWidth = width - 37;
        g.drawString(health, midWidth - fontMetrics.stringWidth(health) / 2, height - 125);
    }
}
