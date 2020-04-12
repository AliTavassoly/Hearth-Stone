package hearthstone.gui;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.cards.Card;
import hearthstone.logic.models.cards.CardType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CardButton extends ImageButton implements MouseListener {
    Card card;
    String size;

    public CardButton(Card card) {
        this.card = card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage image = null;
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream(
                    "/images/cards" + card.getName().replace(' ', '_')));
        } catch (Exception e) {
            System.out.println(e);
            e.getStackTrace();
        }
        g2.drawImage(image, 0, 0, null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 3);
        FontMetrics fontMetrics = g2.getFontMetrics(font);
        //int textWidth = fontMetrics.stringWidth(text);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        g2.setColor(Color.white);

        //g2.drawString(text, width / 2 - textWidth / 2,
        //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
        // DRAW TEXT

        switch (card.getCardType()){
            case SPELL:
                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
                break;
            case HEROCARD:
                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);

                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
                break;
            case MINIONCARD:
                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);

                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);

                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
                break;
            case WEAPONCARD:
                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);

                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);

                //g2.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()) + tof);
                break;
        }

        //resize
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        super.mouseClicked(mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        super.mousePressed(mouseEvent);
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        super.mouseReleased(mouseEvent);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        super.mouseEntered(mouseEvent);
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        super.mouseExited(mouseEvent);
    }

}
