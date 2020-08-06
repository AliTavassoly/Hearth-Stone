package hearthstone.client.gui.controls.buttons;

import hearthstone.HearthStone;
import hearthstone.client.HSClient;
import hearthstone.client.data.GUIConfigs;
import hearthstone.client.gui.controls.interfaces.HaveCard;
import hearthstone.client.gui.credetials.CredentialsFrame;
import hearthstone.models.card.Card;
import hearthstone.models.card.CardType;
import hearthstone.models.card.minion.MinionCard;
import hearthstone.models.card.weapon.WeaponCard;
import hearthstone.util.FontType;
import hearthstone.util.SoundPlayer;
import hearthstone.util.getresource.ImageResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CardButton extends ImageButton implements MouseListener, HaveCard {
    int width, height, number;
    private Card card;
    private boolean markable;
    private boolean writeInfo;

    private boolean isMark;

    private BufferedImage cardImage;
    private static BufferedImage numberImage, redMark;

    public CardButton(Card card, int width, int height, int number) {
        this.card = card;
        this.width = width;
        this.height = height;
        this.number = number;
        this.writeInfo = true;

        configButton();
    }

    public CardButton(Card card, int width, int height, int number, boolean markable) {
        this.card = card;
        this.width = width;
        this.height = height;
        this.number = number;
        this.markable = markable;
        this.writeInfo = true;

        isMark = false;

        configButton();
    }

    public CardButton(Card card, boolean writeInfo, int width, int height, int number) {
        this.card = card;
        this.writeInfo = writeInfo;
        this.width = width;
        this.height = height;
        this.number = number;

        isMark = false;

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
                if (HSClient.currentAccount.getUnlockedCards().contains(card.getId())) {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + ".png";
                } else {
                    path = "/images/cards/" + card.getName().
                            toLowerCase().replace(' ', '_').replace("'", "") + "_bw" + ".png";
                }
                cardImage = ImageResource.getInstance().getImage(path);
            }

            if (numberImage == null)
                numberImage = ImageResource.getInstance().getImage("/images/flag.png");
            if (redMark == null) {
                redMark = ImageResource.getInstance().getImage("/images/red_mark.png");
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

        if (number != -1) {
            g2.drawImage(numberImage.getScaledInstance(
                    GUIConfigs.numberOfCardFlagWidth,
                    GUIConfigs.numberOfCardFlagHeight,
                    Image.SCALE_SMOOTH),
                    width / 2 - GUIConfigs.numberOfCardFlagWidth / 2, height - 38,
                    GUIConfigs.numberOfCardFlagWidth,
                    GUIConfigs.numberOfCardFlagHeight,
                    null);
        }

        if (isMark) {
            g2.drawImage(redMark.getScaledInstance(
                    GUIConfigs.bigRedMarkWidth,
                    GUIConfigs.bigRedMarkHeight,
                    Image.SCALE_SMOOTH),
                    width / 2 - GUIConfigs.bigRedMarkWidth / 2,
                    height / 2 - GUIConfigs.bigRedMarkHeight / 2,
                    GUIConfigs.bigRedMarkWidth,
                    GUIConfigs.bigRedMarkHeight,
                    null);
        }

        Font font = CredentialsFrame.getInstance().getCustomFont(FontType.TEXT, 0, 30);
        FontMetrics fontMetrics = g2.getFontMetrics(font);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);

        // DRAW TEXT
        drawCardInfo(g2, Color.WHITE, fontMetrics);

        if (number != -1)
            drawCardNumber(g2, fontMetrics);
    }

    private void drawCardInfo(Graphics2D g, Color color, FontMetrics fontMetrics) {
        final int spellManaX = 24;
        final int spellManaY = 39;

        final int heroPowerManaX = width / 2 + 1;
        final int heroPowerManaY = 27;

        final int minionManaX = 22;
        final int minionManaY = 38;
        final int minionAttackX = 24;
        final int minionAttackY = height - 28;
        final int minionHealthX = width - 19;
        final int minionHealthY = height - 28;

        final int weaponManaX = 24;
        final int weaponManaY = 39;
        final int weaponDurabilityX = width - 17;
        final int weaponDurabilityY = height - 25;
        final int weaponAttackX = 25;
        final int weaponAttackY = height - 25;

        String text;
        int textWidth;

        switch (card.getCardType()) {
            case SPELL:
            case REWARD_CARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, spellManaX - textWidth / 2,
                        spellManaY, color);
                break;
            case HERO_POWER:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, heroPowerManaX - textWidth / 2,
                        heroPowerManaY, color);
                break;
            case MINION_CARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionManaX - textWidth / 2,
                        minionManaY, color);

                text = String.valueOf(((MinionCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionAttackX - textWidth / 2,
                        minionAttackY, color);

                text = String.valueOf(((MinionCard) card).getHealth());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, minionHealthX - textWidth / 2,
                        minionHealthY, color);
                break;
            case WEAPON_CARD:
                text = String.valueOf(card.getManaCost());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponManaX - textWidth / 2,
                        weaponManaY, color);

                text = String.valueOf(((WeaponCard) card).getAttack());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponAttackX - textWidth / 2,
                        weaponAttackY, color);

                text = String.valueOf(((WeaponCard) card).getDurability());
                textWidth = fontMetrics.stringWidth(text);
                drawStringOnCard(g, text, weaponDurabilityX - textWidth / 2,
                        weaponDurabilityY, color);
                break;
        }
    }

    private void drawCardNumber(Graphics2D g, FontMetrics fontMetrics) {
        drawStringOnCard(g, String.valueOf(number),
                width / 2 - fontMetrics.stringWidth(String.valueOf(number)) / 2,
                height - 10, Color.WHITE);
    }

    private void drawStringOnCard(Graphics2D g, String text, int x, int y, Color color) {
        if(!writeInfo)
            return;
        g.setColor(color);
        g.drawString(text, x, y);
    }

    public boolean isMark() {
        return isMark;
    }

    public boolean isWriteInfo() {
        return writeInfo;
    }

    public void setWriteInfo(boolean writeInfo) {
        this.writeInfo = writeInfo;
    }

    public Card getCard() {
        return card;
    }

    public void setMySize(int width, int height){
        this.width = width;
        this.height = height;
        setSize(new Dimension(width, height));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (!markable) {
            String path;
            if (card.getCardType() == CardType.MINION_CARD) {
                path = "/sounds/minions/" + card.getName().toLowerCase().replace(' ', '_') + ".wav";
            } else if (card.getCardType() == CardType.SPELL) {
                path = "/sounds/spells/" + "spell" + ".wav";
            } else if (card.getCardType() == CardType.WEAPON_CARD) {
                path = "/sounds/weapons/" + "weapon" + ".wav";
            } else if (card.getCardType() == CardType.REWARD_CARD) {
                path = "/sounds/rewards/" + "reward" + ".wav";
            } else {
                return;
            }
            SoundPlayer soundPlayer = new SoundPlayer(path);
            soundPlayer.playOnce();
        } else {
            changeMarkState();
            repaint();
            revalidate();
        }
    }

    private void changeMarkState() {
        if (isMark)
            isMark = false;
        else
            isMark = true;
    }
}