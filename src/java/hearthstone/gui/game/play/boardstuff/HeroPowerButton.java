package hearthstone.gui.game.play.boardstuff;

import hearthstone.gui.controls.ImageButton;
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

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            String path;
                path = "/images/heroes/circle_heroes/" + "paladin".    // Hero Power card should replace
                        toLowerCase().replace(' ', '_').replace("'", "") + ".png";

            image = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        g2.drawImage(image.getScaledInstance(
                width, height,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height,
                null);
    }
}
