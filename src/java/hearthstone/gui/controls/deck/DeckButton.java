package hearthstone.gui.controls.deck;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.game.GameFrame;
import hearthstone.logic.models.hero.Hero;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DeckButton extends ImageButton {
    int width, height;
    private Hero.Deck deck;

    private final int stringDis = 36;
    private final int stringStartY = 45;

    public DeckButton(Hero.Deck deck, int width, int height) {
        this.deck = deck;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/deck.png"));
        } catch (Exception e) {
            System.out.println(e);
            e.getStackTrace();
        }
        g2.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
        Font font = GameFrame.getInstance().getCustomFont(0, 15);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        g2.setColor(new Color(69, 27, 27));

        g2.drawString("total games: " + String.valueOf(deck.getTotalGame()) , 380, stringStartY);
        g2.drawString("win rate: " + String.valueOf(deck.getWinTotal()) + "%", 380, stringStartY + stringDis);
        g2.drawString("wins: " + String.valueOf(deck.getWinGame()), 380, stringStartY + 2 * stringDis);

        g2.setFont(font);
        g2.drawString("mana average: " + String.valueOf(deck.getManaAv()), 155, stringStartY);
        g2.drawString("name: " + deck.getName(), 163, stringStartY + stringDis);
        if (deck.getBestCard() == null)
            g2.drawString("useful card: no card", 155 , stringStartY + 2 * stringDis);
        else
            g2.drawString("favorite card: " + deck.getBestCard().getName(), 155, stringStartY + 2 * stringDis);

    }
}
