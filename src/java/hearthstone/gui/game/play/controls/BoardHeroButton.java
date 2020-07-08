package hearthstone.gui.game.play.controls;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.hero.IHero;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardHeroButton extends ImageButton {
    private IHero hero;
    private int width, height;

    private static BufferedImage immuneImage;
    private static BufferedImage freezeImage;

    private BufferedImage heroImage;
    private BufferedImage healthBackground;

    private int playerId;

    public BoardHeroButton(IHero hero, int width, int height, int playerId) {
        this.hero = hero;
        this.height = height;
        this.width = width;
        this.playerId = playerId;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    public int getPlayerId() {
        return playerId;
    }

    public IHero getHero() {
        return hero;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        try {
            if (heroImage == null)
                heroImage = ImageResource.getInstance().getImage(
                        "/images/heroes/normal_heroes/" +
                                hero.getName().toLowerCase().replace(' ', '_') + ".png");
            if (healthBackground == null)
                healthBackground = ImageResource.getInstance().getImage(
                        "/images/health_background.png");

            if(immuneImage == null)
                immuneImage = ImageResource.getInstance().getImage(
                        "/images/heroes/normal_heroes/immune_frame.png");

            if(freezeImage == null)
                freezeImage = ImageResource.getInstance().getImage(
                        "/images/heroes/normal_heroes/freeze_frame.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.drawImage(heroImage.getScaledInstance(width,
                height,
                Image.SCALE_SMOOTH), 0, 0,
                width,
                height,
                null);

        if(hero.isImmune()){
            g2.drawImage(immuneImage.getScaledInstance(width,
                    height,
                    Image.SCALE_SMOOTH), 0, 0,
                    width,
                    height,
                    null);
        }
        if(hero.isFreeze()){
            g2.drawImage(freezeImage.getScaledInstance(width,
                    height,
                    Image.SCALE_SMOOTH), 0, 0,
                    width,
                    height,
                    null);
        }

        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.NUMBER,0, 15);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW HEALTH
        int midWidth = width - 25;
        g2.drawImage(healthBackground.getScaledInstance(
                SizeConfigs.healthWidth, SizeConfigs.healthHeight,
                Image.SCALE_SMOOTH),
                midWidth - SizeConfigs.healthWidth / 2 + 5, height - 50,
                SizeConfigs.healthWidth, SizeConfigs.healthHeight,
                null);

        drawHealth(g2, String.valueOf(hero.getHealth()), fontMetrics);
    }

    private void drawHealth(Graphics2D g, String text, FontMetrics fontMetrics) {
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.NUMBER, 0, 20);
        g.setFont(font);
        String health = String.valueOf(hero.getHealth());
        g.setColor(Color.WHITE);
        int midWidth = width - 25;
        g.drawString(health, midWidth - fontMetrics.stringWidth(health) / 2 + 5, height - 17);
    }
}
