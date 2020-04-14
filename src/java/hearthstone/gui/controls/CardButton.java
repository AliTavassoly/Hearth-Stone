package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.cards.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CardButton extends ImageButton /*implements MouseListener*/ {
    int width, height;
    Card card;
    String size;

    public CardButton(Card card, int width, int height) {
        this.card = card;
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
                    "/images/" + card.getName().toLowerCase().replace(' ', '_') + ".png"));
        } catch (Exception e) {
            System.out.println(e);
            e.getStackTrace();
        }
        g2.drawImage(image, 0, 0, null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        //drawStringOnCard(g2, Color.WHITE, fontMetrics);

        //resize
    }

    void drawStringOnCard(Graphics2D g, Color color, FontMetrics fontMetrics){
        final int spellManaX = 20;
        final int spellManaY = 20;

        final int minionManaX = 0;
        final int minionManaY = 0;
        final int minionAttackX = 0;
        final int minionAttackY = 0;
        final int minionHealthX = 0;
        final int minionHealthY = 0;

        final int weaponManaX = 0;
        final int weaponManaY = 0;
        final int weaponDurabilityX = 0;
        final int weaponDurabilityY = 0;
        final int weaponAttackX = 0;
        final int weaponAttackY = 0;

        final int heroManaX = 0;
        final int heroManaY = 0;

        String text;
        g.setColor(color);
        switch (card.getCardType()){
            case SPELL:
                text = String.valueOf(card.getManaCost());
                g.drawString(text, spellManaX - fontMetrics.stringWidth(text) / 2, spellManaY);
                break;
            case HEROCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
            case MINIONCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
            case WEAPONCARD:
                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));

                //g.drawString(text, width / 2 - textWidth / 2,
                //       (height / 2 - fontMetrics.getHeight() / 2 + fontMetrics.getAscent()));
                break;
        }
    }

    /*@Override
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
    }*/
}
