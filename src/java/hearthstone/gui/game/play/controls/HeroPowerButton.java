package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.cards.HeroPowerCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HeroPowerButton extends ImageButton {

    private int width, height;
    private HeroPowerCard card;

    public HeroPowerButton(HeroPowerCard card, int width, int height){
        this.width = width;
        this.height = height;
        this.card = card;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage cardImage = null;
        BufferedImage circleImage = null;

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        GraphicsConfiguration config = device.getDefaultConfiguration();
        BufferedImage buffy = config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics buffyG = buffy.getGraphics();

        try {
            String path = "/images/cards/hero_power/" + card.getName().    // Hero Power card should replace
                        toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            cardImage = ImageIO.read(this.getClass().getResourceAsStream(
                    path));

            path = "/images/cards/hero_power/" + "circle_frame.png";
            circleImage = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        buffyG.drawImage(cardImage, 20, 25, null);

        buffyG.drawImage(circleImage, 0, 0, null);

        g2.drawImage(buffy, 0, 0, null);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        drawMana(g2, String.valueOf(card.getManaCost()));
    }

    private void drawMana(Graphics2D g, String text){
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
