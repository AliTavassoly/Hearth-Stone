package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.Card;
import hearthstone.logic.models.card.heropower.HeroPowerCard;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class HeroPowerButton extends ImageButton {
    private int width, height;

    private HeroPowerCard card;

    private boolean isShowBig;

    private int playerId;

    private static BufferedImage circleNormalImage;
    private static BufferedImage circleActiveImage;

    private BufferedImage cardImage;
    private BufferedImage circleImage;

    public HeroPowerButton(HeroPowerCard card, int width, int height, boolean isShowBig, int playerId) {
        this.width = width;
        this.height = height;
        this.card = card;
        this.isShowBig = isShowBig;
        this.playerId = playerId;

        configButton();
    }

    private void configButton() {
        setPreferredSize(new Dimension(width, height));
        setBorderPainted(false);
        setFocusPainted(false);

        addMouseListener(this);
    }

    public int getPlayerId(){
        return playerId;
    }

    public boolean isShowBig() {
        return isShowBig;
    }
    public void setShowBig(boolean showBig) {
        isShowBig = showBig;
    }

    public Card getCard(){
        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            String path = "/images/cards/hero_power/" + card.getName().
                    toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            if (cardImage == null)
                cardImage = ImageResource.getInstance().getImage(path);

            if (circleNormalImage == null)
                circleNormalImage = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "circle_normal_frame.png");
            if (circleActiveImage == null)
                circleActiveImage = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "circle_active_frame.png");

            if(circleImage == null)
                circleImage = circleNormalImage;
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
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, 0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);
        int width = fontMetrics.stringWidth(text);

        int x = this.width / 2 - width / 2 + 2;
        int y = 25;

        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        circleImage = circleActiveImage;
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        circleImage = circleNormalImage;
        repaint();
        revalidate();
    }
}
