package hearthstone.client.gui.game.play.controls;

import hearthstone.HearthStone;
import hearthstone.client.configs.GUIConfigs;
import hearthstone.client.gui.controls.buttons.ImageButton;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.models.card.reward.RewardCard;
import hearthstone.util.FontType;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardRewardButton extends ImageButton {
    int width, height;
    private RewardCard card;

    private BufferedImage cardImage;
    private static BufferedImage progressMana;

    public BoardRewardButton(RewardCard card, int width, int height) {
        this.card = card;
        this.width = width;
        this.height = height;

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
        // DRAW IMAGE
        Graphics2D g2 = (Graphics2D) g;

        try {
            String path;
            if (cardImage == null) {
                if (HearthStone.currentAccount.getUnlockedCards().contains(card.getId())) {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + ".png";
                } else {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + "_bw" + ".png";
                }
                cardImage = ImageResource.getInstance().getImage(path);

                if(progressMana == null){
                    progressMana = ImageResource.getInstance().getImage("/images/green_mana.png");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.drawImage(cardImage.getScaledInstance(
                width, height - 20,
                Image.SCALE_SMOOTH),
                0, 0,
                width, height - 20,
                null);

        // DRAW GREEN MANA
        g2.drawImage(progressMana.getScaledInstance(
                GUIConfigs.progressManaWidth, GUIConfigs.progressManaHeight,
                Image.SCALE_SMOOTH),
                width - GUIConfigs.progressManaWidth, 10,
                GUIConfigs.progressManaWidth, GUIConfigs.progressManaHeight,
                null);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // DRAW MANA
        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT,0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);
        g2.setFont(font);
        drawMana(g2, fontMetrics);

        // DRAW PROGRESS
        font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT,0, 24);
        fontMetrics = g2.getFontMetrics(font);
        g2.setFont(font);
        drawProgressMana(g2, fontMetrics);

    }

    private void drawMana(Graphics2D g, FontMetrics fontMetrics) {
        final int manaX = 24;
        final int manaY = 39;

        String text;
        int textWidth;

        text = String.valueOf(card.getManaCost());
        textWidth = fontMetrics.stringWidth(text);
        g.setColor(Color.WHITE);
        g.drawString(text, manaX - textWidth / 2, manaY);
    }

    private void drawProgressMana(Graphics2D g, FontMetrics fontMetrics){
        String text = String.valueOf(card.getPercentage());

        int progressMidX = width - GUIConfigs.progressManaWidth / 2;
        int progressY = 37;
        int textWidth;

        textWidth = fontMetrics.stringWidth(text);
        g.setColor(Color.WHITE);
        g.drawString(text, progressMidX - textWidth / 2, progressY);
    }
}
