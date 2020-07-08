package hearthstone.gui.game.play.controls;

import hearthstone.gui.controls.buttons.ImageButton;
import hearthstone.gui.credetials.CredentialsFrame;
import hearthstone.logic.models.card.weapon.WeaponCard;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class WeaponButton extends ImageButton {
    private int width, height;

    private WeaponCard card;

    private boolean isShowBig;

    private int playerId;

    private BufferedImage cardImage, weaponImage;

    private static BufferedImage activeWeaponImage, inactiveWeaponImage;
    private static BufferedImage activeWeaponImageHovered;

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
            g2.drawImage(cardImage, 10, 10, null);

            if (activeWeaponImage == null)
                activeWeaponImage = ImageResource.getInstance().getImage("/images/cards/weapon/" + "active_frame.png");
            if (inactiveWeaponImage == null)
                inactiveWeaponImage = ImageResource.getInstance().getImage("/images/cards/weapon/" + "inactive_frame.png");
            if (activeWeaponImageHovered == null)
                activeWeaponImageHovered = ImageResource.getInstance().getImage("/images/cards/weapon/" + "active_frame_hovered.png");

            if (weaponImage == null) {
                if (card.canAttack())
                    weaponImage = activeWeaponImage;
                else
                    weaponImage = inactiveWeaponImage;
            }

            g2.drawImage(weaponImage, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        if (card.canAttack()) {
            drawAttack(g2, String.valueOf(card.getAttack()));
            drawDurability(g2, String.valueOf(card.getDurability()));
        }
    }

    private void drawAttack(Graphics2D g, String text) {
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, 0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);

        int x = 30;

        g.drawString(text, x - fontMetrics.stringWidth(text), 85);
    }

    private void drawDurability(Graphics2D g, String text) {
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, 0, 30);
        FontMetrics fontMetrics = g.getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);

        int x = 94;

        g.drawString(text, x - fontMetrics.stringWidth(text), 90);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        if (card.canAttack()) {
            weaponImage = activeWeaponImageHovered;
        }
        repaint();
        revalidate();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if (card.canAttack()) {
            weaponImage = activeWeaponImage;
        } else {
            weaponImage = inactiveWeaponImage;
        }
        repaint();
        revalidate();
    }
}
