package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.models.card.heropower.HeroPowerCard;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroPowerButton extends ImageButton {

    private int width, height;
    private HeroPowerCard card;

    private boolean isEnemy;

    private static BufferedImage circleImage, cardImage;

    public HeroPowerButton(HeroPowerCard card, int width, int height, boolean isEnemy) {
        this.width = width;
        this.height = height;
        this.card = card;
        this.isEnemy = isEnemy;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            String path = "/images/cards/hero_power/" + card.getName().    // Hero Power card should replace
                    toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            if (cardImage != null)
                cardImage = ImageResource.getInstance().getImage(path);

            path = "/images/cards/hero_power/" + "circle_frame.png";
            if (circleImage == null)
                circleImage = ImageResource.getInstance().getImage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        g2.drawImage(cardImage, 20, 25, null);

        g2.drawImage(circleImage, 0, 0, null);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        drawMana(g2, String.valueOf(card.getManaCost()));
    }

    private void drawMana(Graphics2D g, String text) {
        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);

        int x = this.width / 2 - width / 2 + 2;
        int y = 25;

        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
