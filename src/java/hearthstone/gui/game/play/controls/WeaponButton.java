package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.cards.WeaponCard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WeaponButton extends ImageButton {
    private int width, height;
    private WeaponCard card;

    public WeaponButton(WeaponCard card, int width, int height){
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

        BufferedImage cardImage = null;
        BufferedImage circleImage = null;

        try {
            String path = "/images/cards/weapon/" + card.getName().    // Hero Power card should replace
                    toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            cardImage = ImageIO.read(this.getClass().getResourceAsStream(
                    path));

            path = "/images/cards/weapon/" + "circle_frame.png";
            circleImage = ImageIO.read(this.getClass().getResourceAsStream(
                    path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        g2.drawImage(cardImage.getScaledInstance(
                width - 40, height - 40,
                Image.SCALE_SMOOTH),
                20, 20 + 5,
                width - 40, height - 40,
                null);

        g2.drawImage(circleImage.getScaledInstance(
                width, height,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height,
                null);

        drawMana(g2, String.valueOf(card.getManaCost()));

        drawAttack(g2, String.valueOf(card.getAttack()));

        drawDurability(g2, String.valueOf(card.getDurability()));
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

    private void drawAttack(Graphics2D g, String text){

    }

    private void drawDurability(Graphics2D g, String text){

    }
}
