package hearthstone.gui.controls;

import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.gui.game.GameFrame;
import hearthstone.logic.models.Deck;
import hearthstone.logic.models.cards.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DeckButton extends ImageButton /*implements MouseListener*/ {
    int width, height;
    private Deck deck;

    public DeckButton(Card card, int width, int height) {
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
        Font font = GameFrame.getInstance().getCustomFont(0, 30);
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
