package hearthstone.gui.game.play.controls;

import hearthstone.gui.SizeConfigs;
import hearthstone.gui.controls.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class WeaponButton extends ImageButton {
    private int width, height;

    private WeaponCard card;

    private boolean isShowBig;

    private int playerId;

    private BufferedImage cardImage;
    private BufferedImage circleImage;

    private static BufferedImage circleNormalImage, circleActiveImage;
    private static BufferedImage swordImage;
    private static BufferedImage shieldImage;


    public WeaponButton(WeaponCard card, int width, int height, boolean isShowBig, int playerId) {
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

    public WeaponCard getCard() {
        return card;
    }

    public boolean isShowBig() {
        return isShowBig;
    }
    public void setShowBig(boolean showBig) {
        isShowBig = showBig;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        try {
            String path = "/images/cards/weapon/" + card.getName().
                    toLowerCase().replace(' ', '_').replace("'", "") + ".png";
            if (cardImage == null)
                cardImage = ImageResource.getInstance().getImage(path);
            g2.drawImage(cardImage, 20, 25, null);

            if (circleNormalImage == null)
                circleNormalImage = ImageResource.getInstance().getImage("/images/cards/weapon/" + "circle_normal_frame.png");
            if (circleActiveImage == null)
                circleActiveImage = ImageResource.getInstance().getImage("/images/cards/weapon/" + "circle_active_frame.png");

            if(circleImage == null)
                circleImage = circleNormalImage;
            g2.drawImage(circleImage, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        drawMana(g2, String.valueOf(card.getManaCost()));

        drawAttack(g2, String.valueOf(card.getAttack()));

        drawDurability(g2, String.valueOf(card.getDurability()));
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

    private void drawAttack(Graphics2D g, String text) {
        int x = 0;
        int y = width - SizeConfigs.weaponDetailHeight;
        try {
            String path = "/images/sword.png";
            if(swordImage == null) {
                swordImage = ImageResource.getInstance().getImage(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(swordImage.getScaledInstance(
                SizeConfigs.weaponDetailWidth, SizeConfigs.weaponDetailHeight,
                Image.SCALE_SMOOTH),
                x, y,
                SizeConfigs.weaponDetailWidth, SizeConfigs.weaponDetailHeight,
                null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);

        g.drawString(text,
                x + SizeConfigs.weaponDetailWidth / 2 - fontMetrics.stringWidth(text) + 7,
                y + SizeConfigs.weaponDetailHeight - 7);
    }

    private void drawDurability(Graphics2D g, String text) {
        int x = height - SizeConfigs.weaponDetailWidth;
        int y = width - SizeConfigs.weaponDetailHeight;
        try {
            String path = "/images/shield.png";
            if(shieldImage == null)
                shieldImage = ImageResource.getInstance().getImage(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawImage(shieldImage.getScaledInstance(
                SizeConfigs.weaponDetailWidth, SizeConfigs.weaponDetailHeight,
                Image.SCALE_SMOOTH),
                x, y,
                SizeConfigs.weaponDetailWidth, SizeConfigs.weaponDetailHeight,
                null);

        Font font = CredentialsFrame.getInstance().getCustomFont(0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);

        g.drawString(text,
                x + SizeConfigs.weaponDetailWidth / 2 - fontMetrics.stringWidth(text) + 7,
                y + SizeConfigs.weaponDetailHeight - 7);
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
