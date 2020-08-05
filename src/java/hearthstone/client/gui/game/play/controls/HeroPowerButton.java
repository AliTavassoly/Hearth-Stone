package hearthstone.client.gui.game.play.controls;

import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.models.behaviours.Upgradeable;
import hearthstone.models.card.Card;
import hearthstone.models.card.heropower.HeroPowerBehaviour;
import hearthstone.models.card.heropower.HeroPowerCard;
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

    private static BufferedImage heroPowerFrame, heroPowerFrameHovered;
    private static BufferedImage heroPowerUpgradeFrame, heroPowerUpgradeFrameHovered;
    private static BufferedImage inactiveHeroPower;

    private BufferedImage cardImage;
    private BufferedImage frameImage;

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

    public int getPlayerId() {
        return playerId;
    }

    public boolean isShowBig() {
        return isShowBig;
    }

    public void setShowBig(boolean showBig) {
        isShowBig = showBig;
    }

    public Card getCard() {
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

            if (heroPowerFrame == null)
                heroPowerFrame = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "hero_power_frame.png");
            if (heroPowerFrameHovered == null)
                heroPowerFrameHovered = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "hero_power_frame_hovered.png");
            if (inactiveHeroPower == null)
                inactiveHeroPower = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "hero_power_inactive.png");
            if (heroPowerUpgradeFrame == null)
                heroPowerUpgradeFrame = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "hero_power_frame_upgraded.png");
            if (heroPowerUpgradeFrameHovered == null)
                heroPowerUpgradeFrameHovered = ImageResource.getInstance().getImage("/images/cards/hero_power/" + "hero_power_frame_upgraded_hovered.png");

            if (frameImage == null)
                frameImage = heroPowerFrame;
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.drawImage(cardImage, 20, 25, null);

        if (((HeroPowerBehaviour) card).canAttack()) {
            if (card instanceof Upgradeable && ((Upgradeable) card).isUpgraded())
                g2.drawImage(heroPowerUpgradeFrame, 0, 0, null);
            else
                g2.drawImage(heroPowerFrame, 0, 0, null);
        } else
            g2.drawImage(inactiveHeroPower, 0, 0, null);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (((HeroPowerBehaviour) card).canAttack())
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
        if (card instanceof Upgradeable && ((Upgradeable) card).isUpgraded())
            frameImage = heroPowerUpgradeFrameHovered;
        else
            frameImage = heroPowerFrameHovered;
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (card instanceof Upgradeable && ((Upgradeable) card).isUpgraded())
            frameImage = heroPowerUpgradeFrame;
        else
            frameImage = heroPowerFrame;
        repaint();
        revalidate();
    }
}
